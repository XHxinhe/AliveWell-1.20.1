package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerEntity.class)
public interface PlayerEntityAccessor {
//    @Accessor("abilities")
//    PlayerAbilities getAbilities();

    @Accessor("sleepTimer")
    int getSleepTimer();

    @Accessor("sleepTimer")
    void setSleepTimer(int sleepTimer);

}
