package com.XHxinhe.aliveandwell.mixin.aliveandwell.world;

import net.minecraft.world.gen.feature.OrePlacedFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(OrePlacedFeatures.class)
public class OrePlacedFeaturesMixin {

    @ModifyArg(
            method = "bootstrap",
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/OrePlacedFeatures;ORE_COPPER:Lnet/minecraft/registry/RegistryKey;")
            ),
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/feature/OrePlacedFeatures;modifiersWithCount(ILnet/minecraft/world/gen/placementmodifier/PlacementModifier;)Ljava/util/List;",ordinal = 0),
            index = 0)
    private static int ore_copper(int original){
        return 10;
    }

    @ModifyArg(
            method = "bootstrap",
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/OrePlacedFeatures;ORE_COPPER_LARGE:Lnet/minecraft/registry/RegistryKey;")
            ),
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/feature/OrePlacedFeatures;modifiersWithCount(ILnet/minecraft/world/gen/placementmodifier/PlacementModifier;)Ljava/util/List;",ordinal = 0),
            index = 0)
    private static int ore_copper_large(int original){
        return 10;
    }

    @ModifyArg(
            method = "bootstrap",
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/OrePlacedFeatures;ORE_IRON_UPPER:Lnet/minecraft/registry/RegistryKey;")
            ),
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/feature/OrePlacedFeatures;modifiersWithCount(ILnet/minecraft/world/gen/placementmodifier/PlacementModifier;)Ljava/util/List;",ordinal = 0),
            index = 0)
    private static int ore_iron_upper(int original){
        return 5;
    }
    @ModifyArg(
            method = "bootstrap",
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/OrePlacedFeatures;ORE_IRON_MIDDLE:Lnet/minecraft/registry/RegistryKey;")
            ),
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/feature/OrePlacedFeatures;modifiersWithCount(ILnet/minecraft/world/gen/placementmodifier/PlacementModifier;)Ljava/util/List;",ordinal = 0),
            index = 0)
    private static int ore_iron_middle(int original){
        return 5;
    }
    @ModifyArg(
            method = "bootstrap",
            slice = @Slice(
                    from = @At(value = "FIELD", target = "Lnet/minecraft/world/gen/feature/OrePlacedFeatures;ORE_IRON_SMALL:Lnet/minecraft/registry/RegistryKey;")
            ),
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/feature/OrePlacedFeatures;modifiersWithCount(ILnet/minecraft/world/gen/placementmodifier/PlacementModifier;)Ljava/util/List;",ordinal = 0),
            index = 0)
    private static int ore_iron_small(int original){
        return 5;
    }
}
