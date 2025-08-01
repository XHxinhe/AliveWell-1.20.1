package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GhastEntity.class)
public abstract class GhastEntityMixin extends FlyingEntity implements Monster {
    protected GhastEntityMixin(EntityType<? extends FlyingEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getLimitPerChunk() {
        return 8;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getFireballStrength() {
        return 2;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static boolean canSpawn(EntityType<GhastEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL && random.nextInt(10) == 0 && canMobSpawn(type, world, spawnReason, pos, random);
    }
}
