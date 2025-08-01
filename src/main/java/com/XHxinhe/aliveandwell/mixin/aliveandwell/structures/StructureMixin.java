package com.XHxinhe.aliveandwell.mixin.aliveandwell.structures;

import com.XHxinhe.aliveandwell.block.randompos.RandomManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Structure.class)
public abstract class StructureMixin {

    @Shadow
    @Final
    protected Structure.Config config;
    @Shadow public abstract StructureType<?> getType();
    //Structure.class
    @Inject(at = @At("HEAD"), method = "isBiomeValid", cancellable = true)
    private static void isBiomeValid(Structure.StructurePosition result, Structure.Context context, CallbackInfoReturnable<Boolean> cir) {
        BlockPos blockPos = result.position();

        if(!RandomManager.canSpawnStructure){
            RegistryKey<Biome> biome = context.chunkGenerator().getBiomeSource().getBiome(BiomeCoords.fromBlock(blockPos.getX()), BiomeCoords.fromBlock(blockPos.getY()), BiomeCoords.fromBlock(blockPos.getZ()), context.noiseConfig().getMultiNoiseSampler()).getKey().get();
            if(!biome.getValue().toString().contains("twilightforest:")
                || !biome.getValue().toString().contains("minecells:")
                || !biome.getValue().toString().contains("aliveandwell:")
                || !biome.getValue().toString().contains("ad_astra:")
            ){
                cir.setReturnValue(false);
            } else {
                cir.setReturnValue(context.biomePredicate().test(context.chunkGenerator().getBiomeSource().getBiome(BiomeCoords.fromBlock(blockPos.getX()), BiomeCoords.fromBlock(blockPos.getY()), BiomeCoords.fromBlock(blockPos.getZ()), context.noiseConfig().getMultiNoiseSampler())));
            }
        }

    }
}
