package com.XHxinhe.aliveandwell.flintcoppertool.utils;

import com.XHxinhe.aliveandwell.registry.ItemInit;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Random;

/**
 * 处理打制燧石事件的类。
 */
public class FlintKnapEvent {
    public static final Random RANDOM = new Random();

    public FlintKnapEvent() {
    }

    /**
     * 注册打制燧石事件。
     * 当玩家右键点击方块时，此事件会被触发。
     */
    public static void knapEvent() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            // 获取玩家手中的物品和被点击的方块信息
            ItemStack stackInHand = player.getStackInHand(hand);
            BlockPos blockPos = hitResult.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);

            // 检查玩家是否有效
            if (player == null) {
                return ActionResult.PASS;
            }

            // 定义成功的条件：
            // 1. 玩家主手必须拿着燧石 (Items.FLINT)
            // 2. 点击的方块必须是石头、深板岩、凝灰岩等（通过方块标签判断）
            // 3. 玩家必须点击的是方块的顶面 (Direction.UP)
            boolean isHoldingFlint = player.getMainHandStack().getItem() == Items.FLINT;
            boolean isStoneLikeBlock = blockState.isIn(BlockTags.STONE_ORE_REPLACEABLES) ||
                    blockState.isIn(BlockTags.DEEPSLATE_ORE_REPLACEABLES) ||
                    blockState.isIn(BlockTags.BASE_STONE_OVERWORLD) ||
                    blockState.isIn(BlockTags.BASE_STONE_NETHER) ||
                    blockState.isIn(BlockTags.TERRACOTTA) ||
                    blockState.isIn(BlockTags.SAND) ||
                    blockState.isIn(BlockTags.DIRT); // 简化了原有的多个标签检查
            boolean isHittingTopFace = hitResult.getSide() == Direction.UP;

            if (isHoldingFlint && isStoneLikeBlock && isHittingTopFace) {
                // 只在服务器端执行逻辑，防止重复执行和作弊
                if (!world.isClient) {
                    // 有 10% 的几率成功打制
                    if (RANDOM.nextDouble() <= 0.1) {
                        // 成功打制后，有 80% 的几率掉落物品
                        if (RANDOM.nextDouble() <= 0.8) {
                            // 掉落 1-2 个燧石碎片
                            int dropCount = RANDOM.nextInt(2) + 1;
                            ItemEntity itemEntity = new ItemEntity(
                                    world,
                                    hitResult.getPos().x,
                                    hitResult.getPos().y,
                                    hitResult.getPos().z,
                                    new ItemStack(ItemInit.FLINT_SHARD, dropCount)
                            );
                            world.spawnEntity(itemEntity);
                        }

                        // 消耗一个燧石
                        stackInHand.decrement(1);
                        // 播放石头破碎的声音
                        world.playSound(null, blockPos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0F, 0.5F);
                    } else {
                        // 如果打制失败，只播放敲击声
                        world.playSound(null, blockPos, SoundEvents.BLOCK_STONE_HIT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    }
                }
                // 返回 SUCCESS，表示事件已成功处理，阻止后续的游戏行为（如放置方块）
                return ActionResult.SUCCESS;
            }

            // 如果条件不满足，返回 PASS，让游戏继续处理该右键点击事件
            return ActionResult.PASS;
        });
    }
}