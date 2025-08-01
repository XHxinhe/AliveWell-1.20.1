package com.XHxinhe.aliveandwell.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 重生之石
 * 一个带有说明性提示的物品。
 */
public class RebornStone extends Item {

    public RebornStone(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.aliveandwell.reborn_stone.tooltip_0").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("item.aliveandwell.reborn_stone.tooltip_1").formatted(Formatting.GOLD));
    }
}