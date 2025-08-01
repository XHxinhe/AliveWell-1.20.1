package com.XHxinhe.aliveandwell.block.portal;

import com.XHxinhe.aliveandwell.block.randompos.BaseMithrilRandomBlock;
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

public class RandomAPortalBlock extends CustomPortalBlock {

    public RandomAPortalBlock(AbstractBlock.Settings settings) {
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
                RandomManager serverState = RandomManager.getServerState(Objects.requireNonNull(world.getServer()));
                Direction.Axis axis = CustomPortalHelper.getAxisFrom(state);

                // 定义用于存储传送门角落信息的变量
                Block block1, block2, block3, block4;

                // --- 传送门朝向为 X 轴 ---
                if (axis == Direction.Axis.X) {
                    // 简化结构检查：检查任意一侧的框架和顶部框架即可确定结构
                    // 检查 Z- 方向的框架
                    if (world.getBlockState(pos.west()).getBlock() == BlockInit.FRAME_MITHRIL && world.getBlockState(pos.up(3)).getBlock() == BlockInit.FRAME_MITHRIL) {
                        block1 = world.getBlockState(pos.up(3).west()).getBlock();
                        block2 = world.getBlockState(pos.down().west()).getBlock();
                        block3 = world.getBlockState(pos.up(3).east(2)).getBlock();
                        block4 = world.getBlockState(pos.down().east(2)).getBlock();
                        handlePortalTeleport(world, player, serverState, block1, block2, block3, block4, pos);
                        return;
                    }
                    // 检查 Z+ 方向的框架
                    if (world.getBlockState(pos.east()).getBlock() == BlockInit.FRAME_MITHRIL && world.getBlockState(pos.up(3)).getBlock() == BlockInit.FRAME_MITHRIL) {
                        block1 = world.getBlockState(pos.up(3).east()).getBlock();
                        block2 = world.getBlockState(pos.down().east()).getBlock();
                        block3 = world.getBlockState(pos.up(3).west(2)).getBlock();
                        block4 = world.getBlockState(pos.down().west(2)).getBlock();
                        handlePortalTeleport(world, player, serverState, block1, block2, block3, block4, pos);
                        return;
                    }
                }

                // --- 传送门朝向为 Z 轴 ---
                if (axis == Direction.Axis.Z) {
                    // 检查 X- 方向的框架
                    if (world.getBlockState(pos.north()).getBlock() == BlockInit.FRAME_MITHRIL && world.getBlockState(pos.up(3)).getBlock() == BlockInit.FRAME_MITHRIL) {
                        block1 = world.getBlockState(pos.up(3).north()).getBlock();
                        block2 = world.getBlockState(pos.down().north()).getBlock();
                        block3 = world.getBlockState(pos.up(3).south(2)).getBlock();
                        block4 = world.getBlockState(pos.down().south(2)).getBlock();
                        handlePortalTeleport(world, player, serverState, block1, block2, block3, block4, pos);
                        return;
                    }
                    // 检查 X+ 方向的框架
                    if (world.getBlockState(pos.south()).getBlock() == BlockInit.FRAME_MITHRIL && world.getBlockState(pos.up(3)).getBlock() == BlockInit.FRAME_MITHRIL) {
                        block1 = world.getBlockState(pos.up(3).south()).getBlock();
                        block2 = world.getBlockState(pos.down().south()).getBlock();
                        block3 = world.getBlockState(pos.up(3).north(2)).getBlock();
                        block4 = world.getBlockState(pos.down().north(2)).getBlock();
                        handlePortalTeleport(world, player, serverState, block1, block2, block3, block4, pos);
                        return;
                    }
                }

                // 如果所有结构检查都失败，则发送错误消息
                player.sendMessage(Text.translatable("aliveandwell.randomportal.wrong_structure").formatted(Formatting.RED), false);
            }
        }
    }

    /**
     * 封装了传送逻辑，用于处理方块验证、目的地获取和传送过程。
     */
    private void handlePortalTeleport(World world, ServerPlayerEntity player, RandomManager serverState, Block b1, Block b2, Block b3, Block b4, BlockPos portalPos) {
        // 检查四个角落是否都是自定义的随机方块
        if (b1 instanceof BaseMithrilRandomBlock && b2 instanceof BaseMithrilRandomBlock &&
                b3 instanceof BaseMithrilRandomBlock && b4 instanceof BaseMithrilRandomBlock) {

            String name1 = b1.getTranslationKey();
            String name2 = b2.getTranslationKey();
            String name3 = b3.getTranslationKey();
            String name4 = b4.getTranslationKey();

            // 验证四个角落的方块是否各不相同
            if (areNamesUnique(name1, name2, name3, name4)) {
                // 创建一个代表此传送门唯一标识的对象
                // *** FIX: 使用正确的 ChunkPos 对象 ***
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
                    // 向所有玩家广播消息，告知有玩家正在开辟新航路
                    broadcastToAllPlayers(world, Text.translatable("aliveandwell.randomportalA.through", player.getDisplayName()).formatted(Formatting.YELLOW));

                    // 执行随机传送
                    TpUtil.RandomTpA((ServerWorld) world, player);
                    BlockPos newPos = player.getBlockPos();

                    // 将新的位置信息存入管理器
                    serverState.randomPosMap.put(randomPos, newPos);
                    serverState.markDirty(); // 标记为需要保存

                    // 再次广播，告知新航路已建立
                    broadcastToAllPlayers(world, Text.translatable("aliveandwell.randomportalA.pass", player.getDisplayName()).formatted(Formatting.GREEN));
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
            for (ServerPlayerEntity p : server.getPlayerManager().getPlayerList()) {
                p.sendMessage(message, false);
            }
        }
    }
}