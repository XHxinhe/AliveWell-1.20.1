package com.XHxinhe.aliveandwell.item.food;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * 附魔金胡萝卜
 * 一个简单的食物物品，始终显示附魔光效。
 */
public class EnchantedGoldenCarrotItem extends Item {

    public EnchantedGoldenCarrotItem(Settings settings) {
        super(settings);
    }

    /**
     * 使物品始终拥有附魔光效。
     */
    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}