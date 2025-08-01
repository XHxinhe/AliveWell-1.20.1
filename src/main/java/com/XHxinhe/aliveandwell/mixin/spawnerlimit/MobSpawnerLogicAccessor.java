package com.XHxinhe.aliveandwell.mixin.spawnerlimit;

import net.minecraft.world.MobSpawnerLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
@Mixin(MobSpawnerLogic.class)
public interface MobSpawnerLogicAccessor {
    @Accessor("minSpawnDelay")
    void setMinSpawnDelay(int i);

    @Accessor("maxSpawnDelay")
    void setMaxSpawnDelay(int i);

    @Accessor("requiredPlayerRange")
    void setRequiredPlayerRange(int requiredPlayerRange);

}
