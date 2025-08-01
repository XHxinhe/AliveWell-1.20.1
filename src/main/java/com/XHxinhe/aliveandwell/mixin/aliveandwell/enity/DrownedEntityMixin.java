package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import com.XHxinhe.aliveandwell.dimensions.DimsRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DrownedEntity.class)
public abstract class DrownedEntityMixin extends ZombieEntity implements RangedAttackMob {
    public DrownedEntityMixin(World world) {
        super(world);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static boolean canSpawn(EntityType<DrownedEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        boolean bl;
        RegistryEntry<Biome> registryEntry = world.getBiome(pos);
        boolean bl2 = bl = world.getDifficulty() != Difficulty.PEACEFUL && DrownedEntity.isSpawnDark(world, pos, random) && (spawnReason == SpawnReason.SPAWNER || world.getFluidState(pos).isIn(FluidTags.WATER));
        if (registryEntry.isIn(BiomeTags.MORE_FREQUENT_DROWNED_SPAWNS)) {
            return random.nextInt(10) == 0 && bl;
        }

        if (world.getDimension().effects() == DimsRegistry.UNDER_WORLD_KEY_TYPE.getValue()) {
            return random.nextInt(3) == 0 && bl;
        }

        return random.nextInt(15) == 0 && isValidSpawnDepth(world, pos) && bl;
    }

    @Shadow
    private static boolean isValidSpawnDepth(WorldAccess world, BlockPos pos) {
        return pos.getY() <= world.getSeaLevel() - 1;
    }

    @Inject(at = @At("HEAD"), method = "isValidSpawnDepth", cancellable = true)
    private static void isValidSpawnDepth1(WorldAccess world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(pos.getY() <= world.getSeaLevel() - 1);
    }
}
