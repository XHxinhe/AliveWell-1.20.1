package com.XHxinhe.aliveandwell.mixin.time;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.util.timeutil.WorldTimeHelper;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.GameRules;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
    @Shadow @Final private ServerWorldProperties worldProperties;
    @Shadow @Final private boolean shouldTickTime;
    @Shadow @Final private MinecraftServer server;
    @Unique
    private boolean aliveAndWell$isAccelerating = false;
    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }
    @Shadow
    public abstract void setTimeOfDay(long time);
    /**
     * @author hctian
     * 核心的时间加速逻辑。此方法被重构以避免代码重复。
     */
    @Unique
    private void aliveAndWell$tickAcceleratedTime() {
        WorldTimeHelper timeHelper = (WorldTimeHelper) this.worldProperties;
        if (!this.aliveAndWell$isAccelerating) {
            timeHelper.SetDoubleTime((double) this.properties.getTime());
            this.aliveAndWell$isAccelerating = true;
        }

        timeHelper.SetDoubleTime(timeHelper.GetDoubleTime() + AliveAndWellMain.time_increment);
    }

    /**
     * @author hctian
     * @reason Custom time progression logic
     * 注入到 tickTime 方法的开头，以在特定条件下覆盖原版的时间处理。
     * 此版本经过了逻辑简化，将重复的代码合并。
     */
    @Inject(at = @At("HEAD"), method = "tickTime")
    public void onTickTime(CallbackInfo info) {
        // getTimeOfDay() 和 getLunarTime() 在功能上是等价的，都返回一天中的时刻 (0-23999)。
        long timeInDay = this.getTimeOfDay() % 24000L;

        // 条件1：游戏第一天，且时间在白天 (0-15000 ticks)
        boolean isFirstDayDuringDaytime = (AliveAndWellMain.day == 1 && timeInDay < 15000L);
        // 条件2：非第一天，且时间在上午 (6000-9000 ticks)
        boolean isLaterDayDuringMidday = (AliveAndWellMain.day > 1 && timeInDay >= 6000L && timeInDay <= 9000L);

        // 检查是否满足任一加速条件
        if (isFirstDayDuringDaytime || isLaterDayDuringMidday) {
            // 检查当前世界是否应该推进时间 (通常是主世界)
            if (this.shouldTickTime) {
                // 1. 调用自定义的时间加速方法
                this.aliveAndWell$tickAcceleratedTime();

                // 2. 获取加速后的时间
                WorldTimeHelper timeHelper = (WorldTimeHelper) this.properties;
                long newTime = (long) timeHelper.GetDoubleTime();

                // 3. 将新时间应用到游戏中 (新版MC的API)
                this.worldProperties.setTime(newTime); // 更新总时间
                // 处理计划任务，这是新版MC tickTime 中的重要部分，必须调用
                this.worldProperties.getScheduledEvents().processEvents(this.server, newTime);

                // 4. 如果游戏规则允许，则更新每日时间和天气
                if (this.properties.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
                    this.setTimeOfDay(newTime);
                } else {
                    // 如果日光循环被关闭，则重置状态
                    this.aliveAndWell$isAccelerating = false;
                }
            } else {
                // 如果当前世界不负责推进时间，重置状态
                this.aliveAndWell$isAccelerating = false;
            }
        } else {
            // 如果时间段不匹配，重置状态，让原版逻辑接管
            this.aliveAndWell$isAccelerating = false;
        }
    }
}