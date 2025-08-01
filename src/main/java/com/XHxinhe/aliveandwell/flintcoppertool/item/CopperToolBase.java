package com.XHxinhe.aliveandwell.flintcoppertool.item;

import com.XHxinhe.aliveandwell.registry.ItemInit;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

/**
 * 定义了“铜”工具材质的属性。
 * <p>
 * 这个类实现了 ToolMaterial 接口，为一套完整的铜制工具提供了所有必要的配置。
 * 从其属性来看，这套工具的定位是石制工具的直接升级版，
 * 在挖掘等级上与石制工具相同，但在耐久度和挖掘速度上有所提升。
 */
public class CopperToolBase implements ToolMaterial {

    public CopperToolBase() {
    }

    /**
     * 获取工具的耐久度。
     * 值为 131，与原版的石制工具完全相同。
     */
    @Override
    public int getDurability() {
        return 131;
    }

    /**
     * 获取工具的挖掘速度乘数。
     * 值为 4.0F，与石制工具相同，但低于铁制工具的 6.0F。
     */
    @Override
    public float getMiningSpeedMultiplier() {
        return 4.0F;
    }

    /**
     * 获取工具的攻击伤害加成。
     * 值为 1.0F，与石制工具相同，低于铁制工具的 2.0F。
     */
    @Override
    public float getAttackDamage() {
        return 1.0F;
    }

    /**
     * 获取工具的挖掘等级。
     * 等级 1，即“石质”等级，能够开采煤矿、铁矿和青金石矿。
     * 这是它相比木制工具（等级0）的关键优势。
     */
    @Override
    public int getMiningLevel() {
        return 1;
    }

    /**
     * 获取材质的附魔能力。
     * 值为 5，与石制工具相同，附魔能力较差。
     */
    @Override
    public int getEnchantability() {
        return 5;
    }

    /**
     * 获取用于在铁砧上修复此材质工具的物品。
     * 使用自定义的铜粒（COPPER_NUGGET）进行修复。
     */
    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(ItemInit.COPPER_NUGGET);
    }
}