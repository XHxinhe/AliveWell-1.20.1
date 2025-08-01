package com.XHxinhe.aliveandwell.registry.events;

import com.XHxinhe.aliveandwell.events.EntityEvents;
import com.XHxinhe.aliveandwell.registry.ItemInit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class PlayerHurtEventHandler {

    // 注册玩家受伤事件
    public static void onHurt() {
        EntityEvents.ON_LIVING_DAMAGE_CALC.register((world, entity, damageSource, damageAmount) -> {
            Entity target = damageSource.getAttacker();
           if (entity instanceof PlayerEntity player) {//PLAYER IS GETTING HURT
               if(target != null) {
                   String mob = target.getName().toString();

                    // 随机获取一件护甲
                    ItemStack stack = player.getInventory().armor.get(world.random.nextInt(4));
                    // 获取主手物品
                    ItemStack stack1 = player.getInventory().getMainHandStack();

                    // 针对银鱼特殊处理
                    if (target instanceof SilverfishEntity) {
                        String dim = player.getWorld().getRegistryKey().getValue().toString();
                        int rand20 = world.random.nextInt(20);
                        int rand100 = world.random.nextInt(100);
                        if (dim.contains("twilightforest")) {
                            if (rand20 == 2) {
                                hunger(player);
                                blind(player);
                            }
                        }else if(player.getWorld().getRegistryKey().getValue().toString().contains("minecells")){
                            if(new Random().nextInt(20) == 2){
                                hunger(player);
                            }
                        }else {
//                            player.sendMessage(Text.translatable("生物群落名字一："+world.getBiome(entity.getBlockPos()).getKey().get().getValue().toString()));
                            if(new Random().nextInt(100) == 2){
                                hunger(player);
                            }
                        }
                    }

                    // 针对 terrarianslimes 模组史莱姆
                    if (mob.contains("terrarianslimes.")) {
                        int random1 = world.random.nextInt(11); // 0~10
                        int random2 = world.random.nextInt(5);  // 0~4

                        // 绿色史莱姆：饥饿
                        if (mob.contains("green_slime")) {
                            if (random2 == 0) hunger(player);
                        }
                        // 蓝色史莱姆：中毒
                        if (mob.contains("blue_slime")) {
                            if (random2 == 0) potion(player);
                        }
                        // 红色史莱姆：缓慢或中毒
                        if (mob.contains("red_slime")) {
                            if (random2 == 0) {
                                slow(player);
                            } else if (random2 == 2) {
                                potion(player);
                            } else {
                                slow(player);
                                potion(player);
                            }
                        }
                        // 紫色史莱姆：饥饿
                        if (mob.contains("purple_slime")) {
                            if (random2 == 0) hunger(player);
                        }
                        // 黄色史莱姆：饥饿、漂浮
                        if (mob.contains("yellow_slime")) {
                            if (random2 == 0) {
                                hunger(player);
                            } else if (random2 == 2) {
                                fly(player);
                            } else {
                                hunger(player);
                                fly(player);
                            }
                        }
                        // 黑色史莱姆：腐蚀装备/物品/食物
                        if (mob.contains("black_slime")) {
                            if (random1 == 0) {
                                durability(player, stack); // 腐蚀护甲
                            } else if (random1 == 5) {
                                damageItem(player, player.getInventory().main.get(world.random.nextInt(36))); // 腐蚀物品
                            } else if (random1 == 10) {
                                damageFood(player, player.getInventory().main.get(world.random.nextInt(36))); // 腐蚀食物
                            } else if (random1 == 6) {
                                durability(player, stack1); // 腐蚀主手
                            }
                        }
                        // 冰雪史莱姆：冰冻
                        if (mob.contains("ice_slime")) {
                            if (random2 == 0) ice(player);
                        }
                        // 沙史莱姆：饥饿、缓慢
                        if (mob.contains("sand_slime")) {
                            if (random2 == 0) hunger(player);
                            if (random2 == 4) slow(player); // 修正原来random2==5的错误
                        }
                        // 丛林史莱姆：漂浮
                        if (mob.contains("green_slime")) {
                            if (random2 == 0) fly(player);
                        }
                        // 尖刺冰雪史莱姆：漂浮
                        if (mob.contains("spiked_ice_slime")) {
                            if (random2 == 0) fly(player);
                        }
                        // 尖刺丛林史莱姆：漂浮
                        if (mob.contains("spiked_jungle_slime")) {
                            if (random2 == 0) fly(player);
                        }
                        // 史莱姆之母：饥饿+腐蚀
                        if (mob.contains("mother_slime")) {
                            hunger(player);
                            if (random1 == 0) {
                                durability(player, stack);
                            } else if (random1 == 5) {
                                damageItem(player, player.getInventory().main.get(world.random.nextInt(36)));
                            } else if (random1 == 10) {
                                damageFood(player, player.getInventory().main.get(world.random.nextInt(36)));
                            } else if (random1 == 6) {
                                durability(player, stack1);
                            }
                        }
                        // 熔岩史莱姆：着火
                        if (mob.contains("lava_slime")) {
                            if (random2 == 0) fire(player);
                        }
                        // 粉史莱姆：饥饿
                        if (mob.contains("pinky")) {
                            hunger(player);
                        }
                        // 史莱姆王：饥饿、致盲、腐蚀
                        if (mob.contains("king_slime")) {
                            hunger(player);
                            blind(player);
                            if (random1 == 0) {
                                durability(player, stack);
                            } else if (random1 == 5) {
                                damageItem(player, player.getInventory().main.get(world.random.nextInt(36)));
                            } else if (random1 == 10) {
                                damageFood(player, player.getInventory().main.get(world.random.nextInt(36)));
                            } else if (random1 == 6) {
                                durability(player, stack1);
                            }
                        }
                        // 尖刺史莱姆：漂浮
                        if (mob.contains("spiked_slime")) {
                            if (random2 == 0) fly(player);
                        }
                        // 雨伞史莱姆：饥饿
                        if (mob.contains("umbrella_slime")) {
                            if (random2 == 0) hunger(player);
                        }
                        // 腐化史莱姆：腐蚀
                        if (mob.contains("corrupt_slime")) {
                            if (random1 == 0) {
                                durability(player, stack);
                            } else if (random1 == 5) {
                                damageItem(player, player.getInventory().main.get(world.random.nextInt(36)));
                            } else if (random1 == 10) {
                                damageFood(player, player.getInventory().main.get(world.random.nextInt(36)));
                            } else if (random1 == 6) {
                                durability(player, stack1);
                            }
                        }
                        // 猩红史莱姆：缓慢/中毒
                        if (mob.contains("crimslime")) {
                            if (random1 == 0) {
                                slow(player);
                            } else if (random1 == 5) {
                                potion(player);
                            } else if (random1 == 10) {
                                slow(player);
                            }
                        }
                        // 夜明史莱姆：致盲
                        if (mob.contains("illuminant_slime")) {
                            if (random2 == 0) blind(player);
                        }
                        // 彩虹史莱姆：虚弱
                        if (mob.contains("rainbow_slime")) {
                            weakness(player);
                        }
                    }
                }
            } else {
                // 如果攻击者是玩家（比如反伤等情况）
                if (target instanceof ServerPlayerEntity player) {
                    // 如果玩家手持远古之剑，召唤闪电
                    if (player.getStackInHand(player.getActiveHand()).getItem() == ItemInit.ANCIENT_SWORD) {
                        LightningEntity lightningEntity;
                        BlockPos blockPos = entity.getBlockPos();
                        // 创建闪电实体
                        if ((lightningEntity = EntityType.LIGHTNING_BOLT.create(entity.getWorld())) != null) {
                            lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
                            lightningEntity.setChanneler(player);
                            entity.getWorld().spawnEntity(lightningEntity);
                        }
                        // 播放雷鸣音效
                        entity.getWorld().playSound(entity, entity.getBlockPos(), SoundEvents.ITEM_TRIDENT_THUNDER, SoundCategory.PLAYERS, 0.3f, 0.3f);
                    }
                }
            }
            return damageAmount; // 返回最终伤害值
        });
    }

    // 腐蚀装备/武器耐久
    public static void durability(PlayerEntity player, ItemStack stack) {
        // 判断物品是否为工具或护甲
        if (stack != null && (stack.getItem() instanceof ToolItem || stack.getItem() instanceof ArmorItem)) {
            // 造成2点耐久损耗
            if (player instanceof ServerPlayerEntity serverPlayer) {
                stack.damage(2, player.getWorld().random, serverPlayer);
            }
        }
    }

    // 腐蚀一般物品数量
    public static void damageItem(PlayerEntity player, ItemStack stack) {
        // 非食物且不是工具/护甲
        if (stack != null && !stack.getItem().isFood() && !(stack.getItem() instanceof ToolItem) && !(stack.getItem() instanceof ArmorItem)) {
            int amount = player.getWorld().random.nextInt(2) + 1; // 1~2
            stack.decrement(amount); // 减少物品数量
            player.getWorld().playSound(player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 1.0f, 1.0f, true);
        }
    }

    // 腐蚀食物数量
    public static void damageFood(PlayerEntity player, ItemStack stack) {
        if (stack != null && stack.getItem().isFood()) {
            int amount = player.getWorld().random.nextInt(3) + 1; // 1~3
            stack.decrement(amount); // 减少食物数量
            player.getWorld().playSound(player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 1.0f, 1.0f, true);
        }
    }

    // 行动缓慢效果
    public static void slow(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20 * 2, 1));
    }

    // 饥饿效果
    public static void hunger(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 20 * 3));
    }

    // 中毒+反胃
    public static void potion(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 20 * 3));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20 * 3));
    }

    // 致盲
    public static void blind(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20 * 3));
    }

    // 冰冻
    public static void ice(PlayerEntity player) {
        player.setFrozenTicks(Math.min(player.getMinFreezeDamageTicks(), 20 * 20 + 1));
    }

    // 漂浮
    public static void fly(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 20 * 5));
    }

    // 虚弱
    public static void weakness(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 20 * 5));
    }

    // 着火
    public static void fire(PlayerEntity player) {
        player.setOnFireFor(4);
    }
}