package com.XHxinhe.aliveandwell.mixin.aliveandwell.screen;

import com.google.common.collect.Lists;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(at = @At("HEAD"), method = "calculateRequiredExperienceLevel", cancellable = true)
    private static void getEnchantmentCost(net.minecraft.util.math.random.Random random, int slotIndex, int bookshelfCount, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        Item item = stack.getItem();
        int i = stack.getItem().getEnchantability();
        if (i <= 0) {
            cir.setReturnValue(0);
        }else if(item == Items.GOLDEN_APPLE || item == Items.GOLDEN_CARROT){
            cir.setReturnValue(8);
        }else {
            if (bookshelfCount >= 24) {
                bookshelfCount = 24;
            }

            int j = random.nextInt(8) + 1 + (bookshelfCount >> 1) + random.nextInt(bookshelfCount + 1);
            if (slotIndex == 0) {
                cir.setReturnValue(Math.max(j / 3, 8));
            } else if(slotIndex == 1) {
                cir.setReturnValue(j * 2 / 3 + 8);//28
            }else {
                cir.setReturnValue(Math.max(Math.min(j + 8, 44), bookshelfCount * 2-4));
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getPossibleEntries", cancellable = true)
    private static void getPossibleEntries(int power, ItemStack stack, boolean treasureAllowed, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
        ArrayList<EnchantmentLevelEntry> list = Lists.newArrayList();
        Item item = stack.getItem();

        if(power>= 44){//35
            power = power-9;
        }else if(power>=39){//25-29
            power=power-14;
        }else if(power>=30){//21-29
            power=power-9;
        }else if(power >=28){//25-27
            power =power-3;
        }else {
            power =power-2;
        }
        int cap  = new Random().nextInt(2)+2;
        int cap2 = new Random().nextInt(3)+3;
        int cap3 = new Random().nextInt(2)+3;

        boolean bl = stack.isOf(Items.BOOK);
        block0: for (Enchantment enchantment : Registries.ENCHANTMENT) {
            if (enchantment.isTreasure() && !treasureAllowed || !enchantment.isAvailableForRandomSelection() || !enchantment.target.isAcceptableItem(item) && !bl) continue;
            for (int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {
                if (power < enchantment.getMinPower(i) || power > enchantment.getMaxPower(i)) continue;
                if(power >= 30 ){
                    if(enchantment == Enchantments.FORTUNE){//2-3时运
                        list.add(new EnchantmentLevelEntry(enchantment, cap));
                    }else if(enchantment == Enchantments.EFFICIENCY){//3-5//效率
                        list.add(new EnchantmentLevelEntry(enchantment, cap2));
                    }else if(enchantment == Enchantments.PROTECTION){//3-4//保护
                        list.add(new EnchantmentLevelEntry(enchantment, cap3));
                    }else if(enchantment == Enchantments.FEATHER_FALLING){//3-4//摔落
                        list.add(new EnchantmentLevelEntry(enchantment, cap3));
                    }else if(enchantment == Enchantments.LOOTING){//2-3//抢夺
                        list.add(new EnchantmentLevelEntry(enchantment, cap));
                    }else if(enchantment == Enchantments.UNBREAKING){//2-3//耐久
                        list.add(new EnchantmentLevelEntry(enchantment, cap));
                    }else if(enchantment == Enchantments.POWER){//3-5//力量
                        list.add(new EnchantmentLevelEntry(enchantment, cap2));
                    }else if(enchantment == Enchantments.SHARPNESS){//3-5//锋利
                        list.add(new EnchantmentLevelEntry(enchantment, cap2));
                    }else {
                        list.add(new EnchantmentLevelEntry(enchantment, i));
                    }
                }else if(power>=25){
                    if(enchantment == Enchantments.PROTECTION){//1-2//保护
                        list.add(new EnchantmentLevelEntry(enchantment, i>=3 ? 2 : i));
                    }else if(enchantment == Enchantments.FORTUNE){//1-2//时运
                        list.add(new EnchantmentLevelEntry(enchantment, i>=3 ? 2 : i));
                    }else if(enchantment == Enchantments.EFFICIENCY){//1-2//效率
                        list.add(new EnchantmentLevelEntry(enchantment,  i>=3 ? 2 : i));
                    }else if(enchantment == Enchantments.FEATHER_FALLING){//1-2//摔落
                        list.add(new EnchantmentLevelEntry(enchantment,  i>=3 ? 2 : i));
                    }else if(enchantment == Enchantments.LOOTING){//1-2//抢夺
                        list.add(new EnchantmentLevelEntry(enchantment,  i>=3 ? 2 : i));
                    }else if(enchantment == Enchantments.UNBREAKING){//1-2//耐久
                        list.add(new EnchantmentLevelEntry(enchantment,  i>=3 ? 2 : i));
                    }else if(enchantment == Enchantments.POWER){//1-3//力量
                        list.add(new EnchantmentLevelEntry(enchantment,  i>=4 ? 3 : i));
                    }else if(enchantment == Enchantments.SHARPNESS){//1-3//锋利
                        list.add(new EnchantmentLevelEntry(enchantment,  i>=4 ? 3 : i));
                    }else {
                        list.add(new EnchantmentLevelEntry(enchantment, i));
                    }
                }else {
                    if(enchantment == Enchantments.PROTECTION){//1-2//保护
                        list.add(new EnchantmentLevelEntry(enchantment, 1));
                    }else if(enchantment == Enchantments.FORTUNE){//1-2//时运
                        list.add(new EnchantmentLevelEntry(enchantment, 1));
                    }else if(enchantment == Enchantments.EFFICIENCY){//1-2//效率
                        list.add(new EnchantmentLevelEntry(enchantment,  1));
                    }else if(enchantment == Enchantments.FEATHER_FALLING){//1-2//摔落
                        list.add(new EnchantmentLevelEntry(enchantment,  1));
                    }else if(enchantment == Enchantments.LOOTING){//1-2//抢夺
                        list.add(new EnchantmentLevelEntry(enchantment,  1));
                    }else if(enchantment == Enchantments.UNBREAKING){//1-2//耐久
                        list.add(new EnchantmentLevelEntry(enchantment,  1));
                    }else if(enchantment == Enchantments.POWER){//1-3//力量
                        list.add(new EnchantmentLevelEntry(enchantment,  1));
                    }else if(enchantment == Enchantments.SHARPNESS){//1-3//锋利
                        list.add(new EnchantmentLevelEntry(enchantment,  1));
                    }else {
                        list.add(new EnchantmentLevelEntry(enchantment, i));
                    }
                }
                continue block0;
            }
        }
        cir.setReturnValue(list);
    }
}
