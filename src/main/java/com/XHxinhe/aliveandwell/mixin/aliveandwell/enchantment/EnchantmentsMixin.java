package com.XHxinhe.aliveandwell.mixin.aliveandwell.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Enchantments.class)
public class EnchantmentsMixin {
    public EnchantmentsMixin() {
    }
    @ModifyArg(
            method = "<clinit>",
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=protection"
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/enchantment/ProtectionEnchantment;<init>(Lnet/minecraft/enchantment/Enchantment$Rarity;Lnet/minecraft/enchantment/ProtectionEnchantment$Type;[Lnet/minecraft/entity/EquipmentSlot;)V",
                    ordinal = 0
            ),
            index = 0
    )
    private static Enchantment.Rarity protection(Enchantment.Rarity rarity){
        return Enchantment.Rarity.RARE;
    }

    @ModifyArg(
            method = "<clinit>",
            slice = @Slice(
                    from = @At(value = "CONSTANT", args = "stringValue=feather_falling"
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/enchantment/ProtectionEnchantment;<init>(Lnet/minecraft/enchantment/Enchantment$Rarity;Lnet/minecraft/enchantment/ProtectionEnchantment$Type;[Lnet/minecraft/entity/EquipmentSlot;)V",
                    ordinal = 0
            ),
            index = 0
    )
    private static Enchantment.Rarity feather_falling(Enchantment.Rarity rarity){
        return Enchantment.Rarity.VERY_RARE;
    }

    @ModifyArg(
            method = "<clinit>",
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT", args = "stringValue=fortune"
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/enchantment/LuckEnchantment;<init>(Lnet/minecraft/enchantment/Enchantment$Rarity;Lnet/minecraft/enchantment/EnchantmentTarget;[Lnet/minecraft/entity/EquipmentSlot;)V",
                    ordinal = 0
            ),
            index = 0
    )
    private static Enchantment.Rarity fortuneRarity(Enchantment.Rarity rarity){
        return Enchantment.Rarity.VERY_RARE;
    }

    @ModifyArg(
            method = "<clinit>",
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=efficiency"
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/enchantment/LuckEnchantment;<init>(Lnet/minecraft/enchantment/Enchantment$Rarity;Lnet/minecraft/enchantment/EnchantmentTarget;[Lnet/minecraft/entity/EquipmentSlot;)V",
                    ordinal = 0
            ),
            index = 0
    )
    private static Enchantment.Rarity efficiencyRarity(Enchantment.Rarity rarity){
        return Enchantment.Rarity.RARE;
    }
}
