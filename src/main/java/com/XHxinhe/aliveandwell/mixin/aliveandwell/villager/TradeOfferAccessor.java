package com.XHxinhe.aliveandwell.mixin.aliveandwell.villager;

import net.minecraft.item.ItemStack;
import net.minecraft.village.TradeOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TradeOffer.class)
public interface TradeOfferAccessor {
    @Accessor("firstBuyItem")
    ItemStack getFirstBuyItem();
}
