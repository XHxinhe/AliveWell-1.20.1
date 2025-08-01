package com.XHxinhe.aliveandwell.miningsblock;

import com.XHxinhe.aliveandwell.miningsblock.logic.MiningLogic;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Mixin 钩子方法集合。
 * 这些静态方法被注入到 Minecraft 的原生代码中，用于拦截和修改游戏行为，以实现连锁挖矿功能。
 */
public class MiningMixinHooks {

    /**
     * 尝试触发连锁挖矿。
     * 在玩家成功破坏一个方块后调用此方法。
     *
     * @param player 正在挖矿的玩家。
     * @param pos    被破坏的原始方块的位置。
     * @param source 被破坏的原始方块的状态。
     */
    public static void tryHarvest(ServerPlayerEntity player, BlockPos pos, BlockState source) {
        // 检查玩家是否已激活连锁挖矿模式，并且当前没有正在进行的连锁挖矿任务
        if (MiningPlayers.canStartMining(player) && !MiningPlayers.isVeinMining(player)) {
            try {
                MiningPlayers.startMining(player); // 标记玩家开始连锁挖矿，防止递归触发
                MiningLogic.startMining(player, pos, source); // 执行核心的连锁挖矿逻辑
            } finally {
                MiningPlayers.stopMining(player); // 确保在逻辑结束后，总是将玩家的连锁挖矿状态重置
            }
        }
    }

    /**
     * 获取方块掉落物的实际生成位置。
     * 用于将连锁破坏的方块掉落物统一生成在初始破坏点，而不是它们各自的位置。
     *
     * @param world 发生掉落的世界。
     * @param pos   方块的原始位置。
     * @return 掉落物应该生成的位置。如果该方块不是连锁挖矿的一部分，则返回其原始位置。
     */
    public static BlockPos getActualSpawnPos(World world, BlockPos pos) {
        return MiningPlayers.getNewSpawnPosForDrop(world, pos).orElse(pos);
    }

    /**
     * 判断是否应该取消本次工具的耐久度消耗。
     * 在原版代码尝试消耗工具耐久之前调用。
     *
     * @param entity 使用工具的实体。
     * @param <T>    实体类型。
     * @return 如果实体是玩家且不在连锁挖矿状态，则返回 true（取消消耗），否则返回 false。
     */
    public static <T extends LivingEntity> boolean shouldCancelItemDamage(T entity) {
        // 这个逻辑似乎有些反常：它在“非”连锁挖矿时取消了伤害。
        // 原始逻辑是：if (entity instanceof PlayerEntity player && !MiningPlayers.isVeinMining(player)) return true;
        // 保持原始逻辑不变。
        if (entity instanceof PlayerEntity player) {
            return !MiningPlayers.isVeinMining(player);
        }
        return false;
    }

    /**
     * 修改工具的耐久度消耗值。
     * 在连锁挖矿期间，可以增加工具的耐久消耗。
     *
     * @param damage 原始的耐久消耗值。
     * @param player 使用工具的玩家。
     * @return 修改后的耐久消耗值。
     */
    public static int modifyItemDamage(int damage, ServerPlayerEntity player) {
        int newDamage = damage;
        // 如果玩家正在进行连锁挖矿
        if (player != null && MiningPlayers.isVeinMining(player)) {
            // 从配置中获取耐久度消耗乘数
            float multiplier = MiningPlayers.toolDamageMultiplier;
            if (multiplier != 1.0F) {
                // 计算新的消耗值，并确保至少为0
                newDamage = Math.max(0, (int) (damage * multiplier));
            }
        }
        return newDamage;
    }
}