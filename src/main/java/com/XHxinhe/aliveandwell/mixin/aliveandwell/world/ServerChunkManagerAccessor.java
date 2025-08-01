package com.XHxinhe.aliveandwell.mixin.aliveandwell.world;

import net.minecraft.server.world.ServerChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerChunkManager.class)
public interface ServerChunkManagerAccessor {
    @Accessor("spawnAnimals")
    void setSpawnAnimals(boolean sleepTimer);
}
