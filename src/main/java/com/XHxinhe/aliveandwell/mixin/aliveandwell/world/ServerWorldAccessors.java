package com.XHxinhe.aliveandwell.mixin.aliveandwell.world;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.SleepManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerWorld.class)
public interface ServerWorldAccessors {
    @Accessor("sleepManager")
    SleepManager getSleepManager();
}