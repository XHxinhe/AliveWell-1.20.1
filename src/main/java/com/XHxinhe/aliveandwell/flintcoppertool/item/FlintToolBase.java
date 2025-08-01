package com.XHxinhe.aliveandwell.flintcoppertool.item;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

/**
 * 定义了“燧石”工具材质的属性。
 * <p>
 * 这个类实现了 ToolMaterial 接口，为一套完整的燧石工具提供了所有必要的配置。
 * 从其属性来看，这套工具被设计为游戏最最前期的入门级工具，
 * 性能与木制工具相当或更差，并且无法修复，是典型的消耗品。
 */
public class FlintToolBase implements ToolMaterial {

    public FlintToolBase() {
    }

    /**
     * 获取工具的耐久度。
     * 值为 20，非常低，比金制工具(32)还低，强调了其消耗品属性。
     */
    @Override
    public int getDurability() {
        return 20;
    }

    /**
     * 获取工具的挖掘速度乘数。
     * 值为 2.0F，与木制工具相同。
     */
    @Override
    public float getMiningSpeedMultiplier() {
        return 2.0F;
    }

    /**
     * 获取工具的攻击伤害加成。
     * 值为 0.0F，与木制工具相同。
     */
    @Override
    public float getAttackDamage() {
        return 0.0F;
    }

    /**
     * 获取工具的挖掘等级。
     * 等级 0，与木制工具相同，只能开采石头和煤矿。
     */
    @Override
    public int getMiningLevel() {
        return 0;
    }

    /**
     * 获取材质的附魔能力。
     * 值为 0，意味着它完全无法被附魔。
     */
    @Override
    public int getEnchantability() {
        return 0;
    }

    /**
     * 获取用于在铁砧上修复此材质工具的物品。
     * 返回 null，意味着这套工具无法被修复。
     */
    @Override
    public Ingredient getRepairIngredient() {
        return null;
    }
}