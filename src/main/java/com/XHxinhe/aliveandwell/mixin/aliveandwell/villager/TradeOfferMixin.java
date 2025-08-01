package com.XHxinhe.aliveandwell.mixin.aliveandwell.villager;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.TradeOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TradeOffer.class)
public abstract class TradeOfferMixin {
    @Shadow
    private int specialPrice;
    @Shadow
    private float priceMultiplier;
    @Shadow
    private int demandBonus;

//    @Overwrite
//    public int getSpecialPrice() {
//        //打折最低价格为八五折
////        int k = (int)Math.floor(0.85 *(double)this.getOriginalFirstBuyItem().getCount());
//        int k = ((TradeOfferAccessor)(Object)this).getFirstBuyItem().getCount();
//        if(this.specialPrice <= k){
//            this.specialPrice = k;
//        }
//        return this.specialPrice;
//    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public ItemStack getAdjustedFirstBuyItem() {
        int i = ((TradeOfferAccessor)(Object)this).getFirstBuyItem().getCount();
        int i1 = (int) ((((TradeOfferAccessor)(Object)this).getFirstBuyItem().getCount())*0.85);
        if(i1 <= 1){
            i1=1;
        }
        ItemStack itemStack = ((TradeOfferAccessor)(Object)this).getFirstBuyItem().copy();
        int j = Math.max(0, MathHelper.floor((float)(i * this.demandBonus) * this.priceMultiplier));
        itemStack.setCount(MathHelper.clamp(i + j + this.specialPrice, i1, ((TradeOfferAccessor)(Object)this).getFirstBuyItem().getItem().getMaxCount()));
        return itemStack;
    }
}
