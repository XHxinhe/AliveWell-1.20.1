package com.XHxinhe.aliveandwell.registry.events;

import com.XHxinhe.aliveandwell.mixin.aliveandwell.enity.PlayerEntityAccessor;
import com.XHxinhe.aliveandwell.mixin.aliveandwell.world.ServerWorldAccessors;
import com.XHxinhe.aliveandwell.mixin.aliveandwell.world.WorldAccessor;
import com.XHxinhe.aliveandwell.util.timeutil.WorldTimeHelper;
import com.XHxinhe.aliveandwell.AliveAndWellMain;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.level.ServerWorldProperties;

import java.util.List;

public class AllowSleep {
    public static void init() {

        // 禁止重置时间（即不让睡觉直接跳到白天）
        EntitySleepEvents.ALLOW_RESETTING_TIME.register(player -> false);

        // 允许附近有怪物时也能睡觉
        EntitySleepEvents.ALLOW_NEARBY_MONSTERS.register((player, sleepingPos, vanillaResult) -> ActionResult.SUCCESS);

        // 控制睡觉时间和安全性
        EntitySleepEvents.ALLOW_SLEEP_TIME.register((player, sleepingPos, vanillaResult) -> {
            // 只在服务端世界处理
            if (player.getWorld() instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) player.getWorld();

                // 计算当前天数
                long dayCount = player.getWorld().getTimeOfDay() / 24000L;
                int dayIndex = (int) dayCount;

                // 睡觉床位坐标（东面+1，北面+1）
                int bedX = sleepingPos.getX() + 1;
                int bedY = sleepingPos.getY();
                int bedZ = sleepingPos.getZ() + 1;

                // 用于统计各方向可见空气格数量
                int countWest = 0;
                int countEast = 0;
                int countSouth = 0;
                int countNorth = 0;
                int countUp = 0;

                // 检查床周围空气可见性（西、东、南、北、上）
                // 西面20格
                for (int offset = -20; offset <= 20; offset++) {
                    int checkZ = bedZ + offset;
                    for (int yOffset = 0; yOffset <= 3; yOffset++) {
                        int checkY = bedY + yOffset;
                        Vec3d target = new Vec3d(bedX - 20, checkY, checkZ);
                        for (int h = 0; h <= 2; h++) {
                            Vec3d from = new Vec3d(bedX, bedY + h, bedZ);
                            if (player.getWorld().raycast(new RaycastContext(from, target, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player)).getType() == HitResult.Type.MISS) {
                                countWest++;
                            }
                        }
                    }
                }
                // 东面20格
                for (int offset = -20; offset <= 20; offset++) {
                    int checkZ = bedZ + offset;
                    for (int yOffset = 0; yOffset <= 3; yOffset++) {
                        int checkY = bedY + yOffset;
                        Vec3d target = new Vec3d(bedX + 20, checkY, checkZ);
                        for (int h = 0; h <= 2; h++) {
                            Vec3d from = new Vec3d(bedX, bedY + h, bedZ);
                            if (player.getWorld().raycast(new RaycastContext(from, target, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player)).getType() == HitResult.Type.MISS) {
                                countEast++;
                            }
                        }
                    }
                }
                // 南面20格
                for (int offset = -20; offset <= 20; offset++) {
                    int checkX = bedX + offset;
                    for (int yOffset = 0; yOffset <= 3; yOffset++) {
                        int checkY = bedY + yOffset;
                        Vec3d target = new Vec3d(checkX, checkY, bedZ - 20);
                        for (int h = 0; h <= 2; h++) {
                            Vec3d from = new Vec3d(bedX, bedY + h, bedZ);
                            if (player.getWorld().raycast(new RaycastContext(from, target, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player)).getType() == HitResult.Type.MISS) {
                                countSouth++;
                            }
                        }
                    }
                }
                // 北面20格
                for (int offset = -20; offset <= 20; offset++) {
                    int checkX = bedX + offset;
                    for (int yOffset = 0; yOffset <= 3; yOffset++) {
                        int checkY = bedY + yOffset;
                        Vec3d target = new Vec3d(checkX, checkY, bedZ + 20);
                        for (int h = 0; h <= 2; h++) {
                            Vec3d from = new Vec3d(bedX, bedY + h, bedZ);
                            if (player.getWorld().raycast(new RaycastContext(from, target, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player)).getType() == HitResult.Type.MISS) {
                                countNorth++;
                            }
                        }
                    }
                }
                // 上方20格
                for (int xOffset = -3; xOffset <= 3; xOffset++) {
                    int checkX = bedX + xOffset;
                    for (int zOffset = -3; zOffset <= 3; zOffset++) {
                        int checkZ = bedZ + zOffset;
                        Vec3d target = new Vec3d(checkX, bedY + 20, checkZ);
                        for (int h = 0; h <= 2; h++) {
                            Vec3d from = new Vec3d(bedX, bedY + h, bedZ);
                            if (player.getWorld().raycast(new RaycastContext(from, target, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player)).getType() == HitResult.Type.MISS) {
                                countUp++;
                            }
                        }
                    }
                }

                // 判断床周围是否安全（空气格过多说明暴露，睡觉失败）
                if (countWest > 42 || countEast > 53 || countSouth > 26 || countNorth > 94 || countUp > 72) {
                    player.sendMessage(Text.translatable("aliveandwell.sleepsafe.info1"));
                    return ActionResult.FAIL;
                }

                // 饥饿值为0不能睡觉
                if (player.getHungerManager().getFoodLevel() == 0) {
                    return ActionResult.FAIL;
                }

                // 判断是否在允许睡觉的时间段（夜晚）
                long lunarTime = player.getWorld().getLunarTime() - dayIndex * 24000L;
                if (lunarTime < 15000L || lunarTime > 21000L) {
                    // 非夜晚，重置睡眠计时器
                    int sleepTimer = ((PlayerEntityAccessor) (Object) player).getSleepTimer();
                    if (sleepTimer > 0) {
                        ((PlayerEntityAccessor) (Object) player).setSleepTimer(0);
                        if (player.getHungerManager().getFoodLevel() <= 0) {
                            player.wakeUp();
                            player.sendMessage(Text.translatable("aliveandwell.sleepsafe.info2").formatted(Formatting.RED));
                            return ActionResult.FAIL;
                        } else {
                            return ActionResult.SUCCESS;
                        }
                    }
                } else {
                    // 夜晚且满足跳夜条件
                    if (((ServerWorldAccessors) (Object) (serverWorld)).getSleepManager().canSkipNight(100)) {
                        if (((PlayerEntityAccessor) (Object) player).getSleepTimer() >= 100 && player.getWorld().getRegistryKey() == World.OVERWORLD) {
                            // 获取世界时间辅助工具
                            WorldTimeHelper timeHelper = (WorldTimeHelper) (((WorldAccessor) (player.getWorld())).getProperties());
                            long currentTime = (long) timeHelper.GetDoubleTime();

                            // 计算每tick推进的时间量
                            long timeStep = (24000L - (currentTime - (AliveAndWellMain.day - 1) * 24000)) / 100;

                            // 睡眠推进过程（5秒）
                            for (int tick = 0; tick <= 5 * 20; tick++) {
                                player.addExhaustion(0.0125f); // 增加饥饿消耗
                                if (player.getHungerManager().getFoodLevel() <= 0) {
                                    player.wakeUp();
                                    player.sendMessage(Text.translatable("aliveandwell.sleepsafe.info2").formatted(Formatting.RED));
                                    tick = 0;
                                    timeHelper.SetDoubleTime(currentTime + timeStep * tick);
                                    return ActionResult.FAIL;
                                } else {
                                    if (tick >= 5 * 20) {
                                        // 跳到第二天早晨
                                        timeHelper.SetDoubleTime(AliveAndWellMain.day * 24000L);
                                        long newTime = (long) timeHelper.GetDoubleTime();
                                        ((ServerWorldProperties) serverWorld.getLevelProperties()).setTime(newTime);
                                        ((ServerWorldProperties) serverWorld.getLevelProperties()).getScheduledEvents().processEvents(serverWorld.getServer(), newTime);
                                        serverWorld.setTimeOfDay(newTime);
                                        player.heal(player.getMaxHealth());
                                        // 主世界广播天亮消息
                                        List<? extends PlayerEntity> players = player.getWorld().getPlayers();
                                        for (PlayerEntity p : players) {
                                            p.sendMessage(Text.translatable("aliveandwell.sleepsafe.info3").formatted(Formatting.YELLOW));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // 其它情况交给原版处理
            return ActionResult.PASS;
        });
    }
}