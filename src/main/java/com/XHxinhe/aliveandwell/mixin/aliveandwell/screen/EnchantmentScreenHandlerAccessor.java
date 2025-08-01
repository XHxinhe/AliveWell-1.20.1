package com.XHxinhe.aliveandwell.mixin.aliveandwell.screen;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnchantmentScreenHandler.class)
public interface EnchantmentScreenHandlerAccessor {
    @Accessor("random")
    Random getRandom();

    @Accessor("seed")
    Property getSeed();

    @Accessor("context")
    ScreenHandlerContext getContext();

    @Accessor("enchantmentPower")
    int[] getEnchantmentPower();

    @Accessor("enchantmentId")
    int[] getEnchantmentId();

    @Accessor("enchantmentLevel")
    int[] getEnchantmentLevel();

    @Accessor("inventory")
    Inventory getEnchantSlots();
}
