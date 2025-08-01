package com.XHxinhe.aliveandwell.miningsblock.logic;

import com.google.common.collect.Sets;
import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.miningsblock.MiningPlayers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * 连锁挖矿的核心逻辑。
 */
public class MiningLogic {

    /**
     * 开始连锁挖矿过程。
     * @param player 玩家
     * @param pos    起始方块的位置
     * @param sourceState 起始方块的状态
     */
    public static void startMining(ServerPlayerEntity player, BlockPos pos, BlockState sourceState) {
        ServerWorld world = player.getServerWorld();
        ItemStack tool = player.getMainHandStack();

        // 检查玩家是否可以开始新的连锁挖矿
        if (!MiningPlayers.canStartMining(player)) {
            return;
        }

        // 如果工具对起始方块无效，则不执行
        if (!tool.isSuitableFor(sourceState)) {
            return;
        }

        // 获取附魔等级，决定最大挖掘数量和距离
        int miningLevel = EnchantmentHelper.getLevel(AliveAndWellMain.MINING_BLOCK, tool);
        if (miningLevel <= 0) {
            return;
        }

        int maxBlocks = 1 + miningLevel;
        int maxDistance = 14 + miningLevel;
        Block sourceBlock = sourceState.getBlock();

        // 使用广度优先搜索 (BFS) 来寻找和破坏方块
        Set<BlockPos> visited = Sets.newHashSet(pos);
        Queue<Pair<BlockPos, Integer>> candidates = new LinkedList<>();
        addValidNeighbors(candidates, pos, 1);

        int blocksMined = 1; // 起始方块算作第1个

        while (!candidates.isEmpty() && blocksMined < maxBlocks) {
            // 检查是否应提前停止 (例如工具耐久不足)
            if (shouldStopMining(tool, miningLevel)) {
                return;
            }

            Pair<BlockPos, Integer> candidate = candidates.poll();
            BlockPos currentPos = candidate.getLeft();
            int distance = candidate.getRight();

            if (visited.add(currentPos)) {
                BlockState currentState = world.getBlockState(currentPos);
                if (BlockProcessor.isValidTarget(currentState, world, currentPos, sourceBlock)) {
                    if (harvestBlock(player, currentPos, pos)) {
                        blocksMined++;
                        if (distance < maxDistance) {
                            addValidNeighbors(candidates, currentPos, distance + 1);
                        }
                    }
                }
            }
        }
    }

    /**
     * 检查是否应该停止连锁挖矿，主要基于工具的剩余耐久。
     * @param stack 工具物品
     * @param miningLevel 附魔等级
     * @return 如果应停止则返回 true
     */
    private static boolean shouldStopMining(ItemStack stack, int miningLevel) {
        if (!stack.isDamageable()) {
            return false;
        }
        // 剩余耐久
        int remainingDurability = stack.getMaxDamage() - stack.getDamage();

        // 原始逻辑的近似实现
        if (stack.getItem() instanceof MiningToolItem) {
            int miningLevelOfTool = ((MiningToolItem) stack.getItem()).getMaterial().getMiningLevel();
            int durabilityCost = (miningLevelOfTool >= 3) ? (2 * miningLevelOfTool + 1) / 2 : (2 * miningLevelOfTool + 1);
            return remainingDurability <= (miningLevel + 1) * durabilityCost;
        } else {
            // 对于非工具物品的默认保护值
            return remainingDurability <= 10;
        }
    }

    /**
     * 将一个方块周围的所有26个邻居添加到待处理队列中。
     * @param candidates 待处理队列
     * @param center     中心方块位置
     * @param distance   当前距离
     */
    private static void addValidNeighbors(Queue<Pair<BlockPos, Integer>> candidates, BlockPos center, int distance) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    candidates.add(new Pair<>(center.add(x, y, z), distance));
                }
            }
        }
    }

    /**
     * 破坏一个方块，并处理相关逻辑（掉落物、经验、工具耐久、玩家饥饿度）。
     * @param player    玩家
     * @param pos       要破坏的方块位置
     * @param originPos 连锁挖矿的起始位置
     * @return 如果成功破坏则返回 true
     */
    public static boolean harvestBlock(ServerPlayerEntity player, BlockPos pos, BlockPos originPos) {
        ServerWorld world = player.getServerWorld();
        BlockState blockState = world.getBlockState(pos);

        // 检查玩家是否有权限破坏方块
        if (!player.interactionManager.tryBreakBlock(pos)) {
            return false;
        }

        Block block = blockState.getBlock();
        BlockEntity blockEntity = world.getBlockEntity(pos);
        ItemStack tool = player.getMainHandStack();

        // 标记此方块正在被连锁挖矿破坏，以触发自定义掉落/经验逻辑
        MiningPlayers.addMiningBlock(world, pos, originPos);

        // 记录破坏前的饥饿消耗
        float initialExhaustion = player.getHungerManager().getExhaustion();

        // 核心破坏逻辑
        boolean removed = world.removeBlock(pos, false);
        if (removed) {
            block.onBroken(world, pos, blockState);
            block.afterBreak(world, player, pos, blockState, blockEntity, tool.copy());
        }

        // 移除标记
        MiningPlayers.removeMiningBlock(world, pos);

        // 如果成功破坏，处理耐久和饥饿度
        if (removed) {
            // 消耗工具耐久
            tool.postMine(world, blockState, pos, player);

            // 增加额外的饥饿度消耗 (原始逻辑为增加10%)
            float exhaustionDiff = player.getHungerManager().getExhaustion() - initialExhaustion;
            player.getHungerManager().setExhaustion(initialExhaustion); // 重置回破坏前
            player.addExhaustion(exhaustionDiff * 1.1f); // 增加调整后的消耗
        }

        return removed;
    }
}