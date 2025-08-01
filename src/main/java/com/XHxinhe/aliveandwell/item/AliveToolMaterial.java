package com.XHxinhe.aliveandwell.item;

import com.XHxinhe.aliveandwell.registry.ItemInit;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

/**
 * 自定义工具材质枚举，实现了Minecraft的ToolMaterial接口
 */
public enum AliveToolMaterial implements ToolMaterial {

    // COPPER：挖掘等级1，耐久131，挖掘速度4.0，攻击伤害1.0，附魔性5，修复材料为自定义铜粒
    COPPER(1, 131, 4.0F, 1.0F, 5, () -> Ingredient.ofItems(ItemInit.COPPER_NUGGET)),

    // EN_GENSTONE：挖掘等级4，耐久2000，挖掘速度9.0，攻击伤害3.0，附魔性10，修复材料为自定义能量宝石
    EN_GENSTONE(4, 2500, 9.0F, 3.0F, 10, () -> Ingredient.ofItems(ItemInit.ITEM_EN_GENSTONE)),

    // WUJIN：挖掘等级3，耐久1000，挖掘速度8.0，攻击伤害3.0，附魔性15，修复材料为自定义钨金粒
    WUJIN(3, 1000, 8.0F, 3.0F, 15, () -> Ingredient.ofItems(ItemInit.nugget_wujin)),

    // MITHRIL：挖掘等级3，耐久2000，挖掘速度9.0，攻击伤害5.0，附魔性15，修复材料为自定义秘银粒
    MITHRIL(3, 2000, 9.0F, 5.0F, 15, () -> Ingredient.ofItems(ItemInit.nugget_mithril)),

    // ADAMANTIUM：挖掘等级4，耐久2500，挖掘速度10.0，攻击伤害7.0，附魔性15，修复材料为自定义精金粒
    ADAMANTIUM(4, 2500, 10.0F, 7.0F, 15, () -> Ingredient.ofItems(ItemInit.nugget_adamantium)),
    // ANCIENT：挖掘等级5，耐久3500，挖掘速度15.0，攻击伤害10.0，附魔性15，修复材料为烈焰粉
    ANCIENT(5, 3500, 15.0F, 10.0F, 15, () -> Ingredient.ofItems(Items.BLAZE_POWDER));

    // 挖掘等级（决定能挖掘什么方块）
    private final int miningLevel;
    // 工具耐久
    private final int itemDurability;
    // 挖掘速度
    private final float miningSpeed;
    // 攻击伤害加成
    private final float attackDamage;
    // 附魔能力
    private final int enchantability;
    // 修复材料（惰性加载）
    private final Lazy<Ingredient> repairIngredient;

    /**
     * 构造方法，初始化所有字段
     */
    private AliveToolMaterial(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.miningLevel = miningLevel;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = new Lazy<>(repairIngredient);
    }

    /**
     * 获取工具耐久
     */
    @Override
    public int getDurability() {
        return this.itemDurability;
    }

    /**
     * 获取挖掘速度倍率
     */
    @Override
    public float getMiningSpeedMultiplier() {
        return this.miningSpeed;
    }

    /**
     * 获取基础攻击伤害
     */
    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    /**
     * 获取挖掘等级
     */
    @Override
    public int getMiningLevel() {
        return this.miningLevel;
    }

    /**
     * 获取附魔能力
     */
    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    /**
     * 获取修复材料
     */
    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}