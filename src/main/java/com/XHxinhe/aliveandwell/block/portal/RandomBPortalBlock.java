package com.XHxinhe.aliveandwell.block.portal;

import com.XHxinhe.aliveandwell.block.randompos.BaseAdamantiumRandomBlock;
import com.XHxinhe.aliveandwell.block.randompos.RandomManager;
import com.XHxinhe.aliveandwell.block.randompos.RandomPos;
import com.XHxinhe.aliveandwell.registry.BlockInit;
import com.XHxinhe.aliveandwell.util.TpUtil;

import java.util.Objects;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.kyrptonaught.customportalapi.interfaces.EntityInCustomPortal;
import net.kyrptonaught.customportalapi.util.CustomPortalHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class RandomBPortalBlock extends CustomPortalBlock {

    public RandomBPortalBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    /**
     * 当实体与方块碰撞时调用。
     * @param state 方块状态
     * @param world 实体所在的世界
     * @param pos 方块的位置
     * @param entity 与方块碰撞的实体
     */
    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        EntityInCustomPortal entityInPortal = (EntityInCustomPortal) entity;
        entityInPortal.tickInPortal(pos.toImmutable());

        // 检查实体是否已传送以及在传送门内的时间是否足够
        if (!entityInPortal.didTeleport() && entityInPortal.getTimeInPortal() >= entity.getMaxNetherPortalTime()) {
            entityInPortal.setDidTP(true);

            // 仅在服务器端且实体是玩家时执行传送逻辑
            if (!world.isClient() && entity instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) entity;
                Direction.Axis axis = CustomPortalHelper.getAxisFrom(state);

                // 尝试根据传送门结构进行传送
                // 重构后的方法将复杂的结构检查逻辑封装起来
                if (!tryTeleportBasedOnStructure(world, player, pos, axis)) {
                    // 如果所有结构检查都失败，则发送错误消息
                    player.sendMessage(Text.translatable("aliveandwell.randomportal.wrong_structure").formatted(Formatting.RED), false);
                }
            }
        }
    }

    /**
     * 检查传送门结构并执行传送。
     * 这个方法整合了原始代码中所有重复的结构检查逻辑。
     * @return 如果成功找到有效结构并处理了传送逻辑，则返回 true。
     */
    private boolean tryTeleportBasedOnStructure(World world, ServerPlayerEntity player, BlockPos portalPos, Direction.Axis axis) {
        // 传送门结构为 3x4 (宽x高)，内部空间 1x2
        // 检查两种可能的框架方向
        Direction[] horizontalDirs = (axis == Direction.Axis.X) ?
                new Direction[]{Direction.WEST, Direction.EAST} :
                new Direction[]{Direction.NORTH, Direction.SOUTH};

        for (Direction dir : horizontalDirs) {
            // 检查框架的基本组成部分
            BlockState frameSide = world.getBlockState(portalPos.offset(dir));
            BlockState frameTop = world.getBlockState(portalPos.up(3));

            if (frameSide.isOf(BlockInit.FRAME_ADAMANTIUM) && frameTop.isOf(BlockInit.FRAME_ADAMANTIUM)) {
                // 确定四个角落的方块
                Direction oppositeDir = dir.getOpposite();
                Block block1 = world.getBlockState(portalPos.up(3).offset(dir)).getBlock(); // top-left
                Block block2 = world.getBlockState(portalPos.down().offset(dir)).getBlock(); // bottom-left
                Block block3 = world.getBlockState(portalPos.up(3).offset(oppositeDir, 2)).getBlock(); // top-right
                Block block4 = world.getBlockState(portalPos.down().offset(oppositeDir, 2)).getBlock(); // bottom-right

                // 处理传送逻辑
                handlePortalTeleport(world, player, block1, block2, block3, block4, portalPos);
                return true; // 找到有效结构，处理完毕
            }
        }
        return false; // 未找到有效结构
    }

    /**
     * 封装了传送逻辑，用于处理方块验证、目的地获取和传送过程。
     */
    private void handlePortalTeleport(World world, ServerPlayerEntity player, Block b1, Block b2, Block b3, Block b4, BlockPos portalPos) {
        // 检查四个角落是否都是艾德曼合金随机方块
        if (b1 instanceof BaseAdamantiumRandomBlock && b2 instanceof BaseAdamantiumRandomBlock &&
                b3 instanceof BaseAdamantiumRandomBlock && b4 instanceof BaseAdamantiumRandomBlock) {

            String name1 = b1.getTranslationKey();
            String name2 = b2.getTranslationKey();
            String name3 = b3.getTranslationKey();
            String name4 = b4.getTranslationKey();

            // 验证四个角落的方块是否各不相同
            if (areNamesUnique(name1, name2, name3, name4)) {
                RandomManager serverState = RandomManager.getServerState(Objects.requireNonNull(world.getServer()));

                // 创建一个代表此传送门唯一标识的对象
                RandomPos randomPos = new RandomPos(
                        player.getGameProfile().getId().toString(), // 玩家UUID
                        new ChunkPos(portalPos), // 传送门所在的区块坐标
                        name1, name2, name3, name4
                );

                // 从管理器中获取已存在的目标位置
                BlockPos destination = serverState.getDestination(randomPos, world.getServer());

                if (destination != null) {
                    // 如果目标已存在，直接传送到该位置
                    player.teleport((ServerWorld) world, destination.getX() + 0.5, destination.getY(), destination.getZ() + 0.5, player.getYaw(), player.getPitch());
                } else {
                    // 如果是第一次通过，进行随机传送
                    broadcastToAllPlayers(world, Text.translatable("aliveandwell.randomportalB.through", player.getDisplayName()).formatted(Formatting.YELLOW));

                    // 执行随机传送
                    TpUtil.RandomTpB((ServerWorld) world, player);
                    BlockPos newPos = player.getBlockPos();

                    // 将新的位置信息存入管理器
                    serverState.randomPosMap.put(randomPos, newPos);
                    serverState.markDirty(); // 标记为需要保存

                    broadcastToAllPlayers(world, Text.translatable("aliveandwell.randomportalB.pass", player.getDisplayName()).formatted(Formatting.GREEN));
                }
            } else {
                // 角落方块信息有重复
                player.sendMessage(Text.translatable("aliveandwell.randomportal.wrong_info.duplicate").formatted(Formatting.RED), false);
            }
        } else {
            // 角落方块类型不正确
            player.sendMessage(Text.translatable("aliveandwell.randomportal.wrong_info.type").formatted(Formatting.RED), false);
        }
    }

    /**
     * 验证四个字符串是否互不相同。
     * (原 `valid` 方法的重构版本)
     */
    private boolean areNamesUnique(String name1, String name2, String name3, String name4) {
        return !name1.equals(name2) && !name1.equals(name3) && !name1.equals(name4) &&
                !name2.equals(name3) && !name2.equals(name4) &&
                !name3.equals(name4);
    }

    /**
     * 向服务器上的所有玩家发送消息。
     */
    private void broadcastToAllPlayers(World world, Text message) {
        MinecraftServer server = world.getServer();
        if (server != null) {
            server.getPlayerManager().broadcast(message, false);
        }
    }
}