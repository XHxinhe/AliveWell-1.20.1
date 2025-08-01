package com.XHxinhe.aliveandwell.flintcoppertool.armor;

import com.XHxinhe.aliveandwell.registry.ItemInit;

import java.util.EnumMap;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;

/**
 * 定义了“铜”盔甲材质的属性。
 * <p>
 * 这个类实现了 ArmorMaterial 接口，为一套完整的铜盔甲提供了所有必要的配置。
 * 从其属性来看，这套盔甲的定位介于锁链甲和铁甲之间，
 * 提供了不错的保护，但没有额外的韧性或击退抗性。
 */
public class CopperArmorBase implements ArmorMaterial {

    /**
     * 映射了各个盔甲部位的基础耐久度。
     * 这些值会乘以一个系数（在此为8）来计算最终耐久度。
     */
    private static final EnumMap<ArmorItem.Type, Integer> BASE_DURABILITY = Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
        map.put(ArmorItem.Type.HELMET, 13);      // 13 * 8 = 104
        map.put(ArmorItem.Type.CHESTPLATE, 15); // 15 * 8 = 120
        map.put(ArmorItem.Type.LEGGINGS, 16);   // 16 * 8 = 128
        map.put(ArmorItem.Type.BOOTS, 11);      // 11 * 8 = 88
    });

    /**
     * 映射了各个盔甲部位提供的保护值。
     */
    private final EnumMap<ArmorItem.Type, Integer> PROTECTION = Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
        map.put(ArmorItem.Type.HELMET, 2);      // 头盔
        map.put(ArmorItem.Type.CHESTPLATE, 4);  // 胸甲
        map.put(ArmorItem.Type.LEGGINGS, 6);    // 护腿
        map.put(ArmorItem.Type.BOOTS, 2);       // 靴子
    });

    public CopperArmorBase() {
    }

    /**
     * 获取指定类型盔甲的耐久度。
     * 计算方式为基础值乘以8。这使得铜甲的耐久度低于铁甲（乘数为15）。
     * 例如，头盔耐久度为 13 * 8 = 104，介于锁链甲(165)和皮革(55)之间。
     */
    @Override
    public int getDurability(ArmorItem.Type type) {
        return BASE_DURABILITY.get(type) * 8;
    }

    /**
     * 获取指定类型盔甲的保护值。
     * 整套盔甲总保护值为 2+4+6+2 = 14，略低于铁甲的15点，但高于锁链甲的12点。
     */
    @Override
    public int getProtection(ArmorItem.Type type) {
        return this.PROTECTION.get(type);
    }

    /**
     * 获取盔甲的附魔能力。
     * 值为 12，高于铁甲(9)，低于金甲(25)，意味着它能获得相对不错的附魔。
     */
    @Override
    public int getEnchantability() {
        return 12;
    }

    /**
     * 获取装备盔甲时的声音事件。
     * 使用了铁甲的装备音效，符合其金属质感。
     */
    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_IRON;
    }

    /**
     * 获取用于在铁砧上修复此材质盔甲的物品。
     * 使用自定义的铜粒（COPPER_NUGGET）进行修复。
     */
    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(ItemInit.COPPER_NUGGET);
    }

    /**
     * 获取盔甲材质的名称。
     * 这个名称用于资源包中定位盔甲纹理，例如 "textures/models/armor/copper_layer_1.png"。
     */
    @Override
    public String getName() {
        return "copper";
    }

    /**
     * 获取盔甲的韧性。
     * 值为 0.0F，意味着它不提供额外的伤害减免，与铁甲及以下等级的盔甲相同。
     */
    @Override
    public float getToughness() {
        return 0.0F;
    }

    /**
     * 获取盔甲的击退抗性。
     * 值为 0.0F，不提供任何击退抗性。
     */
    @Override
    public float getKnockbackResistance() {
        return 0.0F;
    }
}