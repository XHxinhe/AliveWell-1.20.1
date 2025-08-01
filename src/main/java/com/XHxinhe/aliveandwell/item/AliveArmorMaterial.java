package com.XHxinhe.aliveandwell.item;

import com.XHxinhe.aliveandwell.registry.ItemInit;
import net.minecraft.item.ArmorItem;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Lazy;
import net.minecraft.util.Util;

import java.util.EnumMap;
import java.util.function.Supplier;

/**
 * 自定义护甲材质枚举，实现了Minecraft的ArmorMaterial接口
 */
public enum AliveArmorMaterial implements net.minecraft.item.ArmorMaterial {
    // 定义三种自定义护甲材质
    // WUJIN：耐久倍率60，防御值{8,10,12,8}，附魔性6，装备音效为钻石护甲，坚韧2.0，击退抗性0.0，修复材料为nugget_wujin
    WUJIN("wujin", 60, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 8);         // 靴子防御值
        map.put(ArmorItem.Type.LEGGINGS, 10);     // 护腿防御值
        map.put(ArmorItem.Type.CHESTPLATE, 12);   // 胸甲防御值
        map.put(ArmorItem.Type.HELMET, 8);        // 头盔防御值
    }), 6, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F, 0.0F, () -> Ingredient.ofItems(ItemInit.nugget_wujin)),

    // MITHRIL：耐久倍率120，防御值{10,12,16,10}，附魔性8，装备音效为钻石护甲，坚韧3.0，击退抗性0.1，修复材料为nugget_mithril
    MITHRIL("mithril", 120, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 10);
        map.put(ArmorItem.Type.LEGGINGS, 12);
        map.put(ArmorItem.Type.CHESTPLATE, 16);
        map.put(ArmorItem.Type.HELMET, 10);
    }), 8, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3.0F, 0.1F, () -> Ingredient.ofItems(ItemInit.nugget_mithril)),

    // ADAMANTIUM：耐久倍率300，防御值{12,18,20,12}，附魔性12，装备音效为下界合金护甲，坚韧6.0，击退抗性0.2，修复材料为nugget_adamantium
    ADAMANTIUM("adamantium", 300, Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 12);
        map.put(ArmorItem.Type.LEGGINGS, 18);
        map.put(ArmorItem.Type.CHESTPLATE, 20);
        map.put(ArmorItem.Type.HELMET, 12);
    }), 12, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 6.0F, 0.2F, () -> Ingredient.ofItems(ItemInit.nugget_adamantium));

    // 定义基础耐久值，顺序为：靴子、护腿、胸甲、头盔
    private static final EnumMap<ArmorItem.Type, Integer> BASE_DURABILITY = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 13);        // 靴子基础耐久
        map.put(ArmorItem.Type.LEGGINGS, 15);     // 护腿基础耐久
        map.put(ArmorItem.Type.CHESTPLATE, 16);   // 胸甲基础耐久
        map.put(ArmorItem.Type.HELMET, 11);       // 头盔基础耐久
    });

    private final String name; // 材质名称
    private final int durabilityMultiplier; // 耐久倍率
    private final EnumMap<ArmorItem.Type, Integer> protectionAmounts; // 各部位防御值
    private final int enchantability; // 附魔能力
    private final SoundEvent equipSound; // 穿戴音效
    private final float toughness; // 坚韧
    private final float knockbackResistance; // 击退抗性
    private final Lazy<Ingredient> repairIngredientSupplier; // 修复材料（惰性加载）

    /**
     * 构造方法，初始化所有字段
     */
    private AliveArmorMaterial(String name,
                               int durabilityMultiplier,
                               EnumMap<ArmorItem.Type, Integer> protectionAmounts,
                               int enchantability,
                               SoundEvent equipSound,
                               float toughness,
                               float knockbackResistance,
                               Supplier<Ingredient> repairIngredientSupplier) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredientSupplier = new Lazy<>(repairIngredientSupplier);
    }

    /**
     * 获取指定部位的耐久值
     * @param type 护甲部位类型
     * @return 实际耐久值 = 基础耐久 * 耐久倍率
     */
    @Override
    public int getDurability(ArmorItem.Type type) {
        return BASE_DURABILITY.get(type) * this.durabilityMultiplier;
    }

    /**
     * 获取指定部位的防御值
     * @param type 护甲部位类型
     * @return 防御值
     */
    @Override
    public int getProtection(ArmorItem.Type type) {
        return this.protectionAmounts.get(type);
    }

    /**
     * 获取附魔能力
     */
    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    /**
     * 获取穿戴音效
     */
    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    /**
     * 获取修复材料
     */
    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredientSupplier.get();
    }

    /**
     * 获取材质名称
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * 获取坚韧
     */
    @Override
    public float getToughness() {
        return this.toughness;
    }

    /**
     * 获取击退抗性
     */
    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}