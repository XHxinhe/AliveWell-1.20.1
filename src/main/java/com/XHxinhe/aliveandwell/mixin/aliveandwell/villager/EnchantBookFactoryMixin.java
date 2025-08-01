package com.XHxinhe.aliveandwell.mixin.aliveandwell.villager;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(TradeOffers.EnchantBookFactory.class)
public abstract class EnchantBookFactoryMixin implements TradeOffers.Factory {

    /**
     * @author
     * @reason
     */
    @Overwrite
    public TradeOffer create(Entity entity, Random random) {
        List<Enchantment> list = (List) Registries.ENCHANTMENT.stream().filter(Enchantment::isAvailableForEnchantedBookOffer).collect(Collectors.toList());
        Enchantment enchantment = (Enchantment)list.get(random.nextInt(list.size()));
        int i = MathHelper.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
        ItemStack itemStack = EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(enchantment, i));
        int j = 2 + random.nextInt(5 + i * 10) + 3 * i;
        if (enchantment.isTreasure()) {
            j *= 3;
        }

        if (j > 34) {
            j = 34;
        }
        if(j<=15){
            j=15;
        }

        return new TradeOffer(new ItemStack(Items.DIAMOND, j), new ItemStack(Items.BOOK), itemStack, 12, ((EnchantBookFactoryAccessor)(Object)this).getExperience(), 0.2F);
    }
}
