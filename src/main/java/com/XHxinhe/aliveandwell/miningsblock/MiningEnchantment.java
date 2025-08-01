package com.XHxinhe.aliveandwell.miningsblock; // 包声明

import net.minecraft.enchantment.Enchantment; // 导入附魔基类
import net.minecraft.enchantment.EnchantmentTarget; // 导入附魔目标类型
import net.minecraft.entity.EquipmentSlot; // 导入装备槽枚举

/**
 * MiningEnchantment
 * 自定义挖矿附魔，适用于挖掘类工具，仅限主手装备。
 */
public class MiningEnchantment extends Enchantment {

    /**
     * 构造方法，定义附魔的稀有度、目标类型和适用装备槽。
     */
    public MiningEnchantment() {
        // Rarity.RARE: 稀有度为稀有
        // EnchantmentTarget.DIGGER: 仅能附魔在挖掘工具（如镐、铲等）上
        // new EquipmentSlot[] {EquipmentSlot.MAINHAND}: 仅主手可用
        super(Rarity.RARE, EnchantmentTarget.DIGGER, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }

    /**
     * 获取该附魔的最大等级。
     * @return 最大等级为6
     */
    @Override
    public int getMaxLevel() {
        return 6; // 最大等级设为6
    }

    /**
     * 获取指定等级的最小附魔能力值（用于附魔台消耗计算）。
     * @param level 附魔等级
     * @return 最小能力值，等级1为15，每升一级+9
     */
    @Override
    public int getMinPower(int level) {
        return 15 + (level - 1) * 9; // 计算最小能力值
    }

    /**
     * 获取指定等级的最大附魔能力值（用于附魔台消耗计算）。
     * @param level 附魔等级
     * @return 最大能力值，等于最小能力值+50
     */
    @Override
    public int getMaxPower(int level) {
        return getMinPower(level) + 50; // 计算最大能力值
    }
}