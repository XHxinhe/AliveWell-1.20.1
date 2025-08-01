package com.XHxinhe.aliveandwell.mixin.aliveandwell.world;

import net.minecraft.world.gen.feature.OreConfiguredFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(OreConfiguredFeatures.class)
public class OreConfiguredFeaturesMixin {

    @ModifyArg(
            method = "bootstrap",
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/OreConfiguredFeatures;ORE_COPPER_SMALL:Lnet/minecraft/registry/RegistryKey;")
            ),
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/feature/OreFeatureConfig;<init>(Ljava/util/List;I)V",ordinal = 0),
            index = 1)
    private static int ore_copper_small(int original){
        return 8;
    }
    @ModifyArg(
            method = "bootstrap",
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/OreConfiguredFeatures;ORE_COPPER_LARGE:Lnet/minecraft/registry/RegistryKey;")
            ),
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/feature/OreFeatureConfig;<init>(Ljava/util/List;I)V",ordinal = 0),
            index = 1)
    private static int ore_copper_large(int original){
        return 10;
    }
    @ModifyArg(
            method = "bootstrap",
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/OreConfiguredFeatures;ORE_IRON:Lnet/minecraft/registry/RegistryKey;")
            ),
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/feature/OreFeatureConfig;<init>(Ljava/util/List;I)V",ordinal = 0),
            index = 1)
    private static int ore_iron(int original){
        return 5;
    }
    @ModifyArg(
            method = "bootstrap",
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/OreConfiguredFeatures;ORE_IRON_SMALL:Lnet/minecraft/registry/RegistryKey;")
            ),
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/feature/OreFeatureConfig;<init>(Ljava/util/List;I)V",ordinal = 0),
            index = 1)
    private static int ore_iron_small(int original){
        return 2;
    }
}
