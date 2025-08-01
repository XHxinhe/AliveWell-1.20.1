package com.XHxinhe.aliveandwell.equipmentlevels.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.text.Text;

import java.util.List;
import java.util.UUID;

/**
 * 核心工具类 (最终源码版)
 * <p>
 * 提供一系列静态辅助方法，用于物品类型检查、实体查找和伤害源判断等。
 * 这是模组进行逻辑判断的基础。
 */
public class EAUtil {

    // 一个包含所有原版可强化物品的不可变列表。
    public static final ImmutableList<Item> vanillaItems = ImmutableList.of(
            Items.IRON_SWORD, Items.IRON_AXE, Items.IRON_HOE, Items.IRON_BOOTS, Items.IRON_CHESTPLATE, Items.IRON_HELMET,
            Items.IRON_LEGGINGS, Items.DIAMOND_AXE, Items.DIAMOND_HOE, Items.DIAMOND_SWORD, Items.DIAMOND_BOOTS, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_HELMET, Items.DIAMOND_LEGGINGS,
            Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.GOLDEN_SWORD, Items.GOLDEN_BOOTS, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_HELMET, Items.GOLDEN_LEGGINGS,
            Items.STONE_AXE, Items.STONE_HOE, Items.STONE_SWORD, Items.WOODEN_AXE, Items.WOODEN_HOE, Items.WOODEN_SWORD, Items.BOW, Items.CROSSBOW, Items.TRIDENT,
            Items.NETHERITE_AXE, Items.NETHERITE_HOE, Items.NETHERITE_SWORD, Items.NETHERITE_BOOTS, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_HELMET, Items.NETHERITE_LEGGINGS,
            Items.CHAINMAIL_BOOTS, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_HELMET, Items.CHAINMAIL_LEGGINGS
    );

    /**
     * 检查一个物品是否是本模组支持强化的任何类型。
     * @param item 要检查的物品
     * @return 如果是剑、斧、锄、弓、弩、三叉戟或盔甲，则返回 true
     */
    public static boolean canEnhance(Item item) {
        return item instanceof SwordItem || item instanceof AxeItem || item instanceof HoeItem || item instanceof BowItem || item instanceof ArmorItem || item instanceof CrossbowItem || item instanceof TridentItem;
    }

    /**
     * 检查一个物品是否是可强化的“武器”（包括近战和远程）。
     * @param item 要检查的物品
     * @return 如果是可强化的物品且不是盔甲，则返回 true
     */
    public static boolean canEnhanceWeapon(Item item) {
        return canEnhance(item) && !(item instanceof ArmorItem);
    }

    /**
     * 检查一个物品是否是可强化的“近战武器”。
     * @param item 要检查的物品
     * @return 如果是武器，但不是盔甲、弓或弩，则返回 true
     */
    public static boolean canEnhanceMelee(Item item) {
        return canEnhance(item) && !(item instanceof ArmorItem) && !(item instanceof BowItem) && !(item instanceof CrossbowItem);
    }

    /**
     * 检查一个物品是否是可强化的“远程武器”。
     * @param item 要检查的物品
     * @return 如果是弓、弩或三叉戟，则返回 true
     */
    public static boolean canEnhanceRanged(Item item) {
        return canEnhance(item) && (item instanceof BowItem || item instanceof CrossbowItem || item instanceof TridentItem);
    }

    /**
     * 检查一个物品是否是可强化的“护甲”。
     * @param item 要检查的物品
     * @return 如果是盔甲，则返回 true
     */
    public static boolean canEnhanceArmor(Item item) {
        return canEnhance(item) && item instanceof ArmorItem;
    }

    /**
     * 在客户端世界中通过UUID查找实体。
     * @param uniqueId 要查找的实体的UUID
     * @return 找到的实体对象，如果未找到则返回 null
     */
    public static Entity getEntityByUniqueId(UUID uniqueId) {
        // 遍历客户端世界中的所有实体
        for (Entity entity : MinecraftClient.getInstance().world.getEntities()) {
            if (entity.getUuid().equals(uniqueId)) {
                return entity; // 找到后立即返回
            }
        }
        return null; // 遍历完所有实体后仍未找到，返回 null
    }

    /**
     * 检查伤害来源是否是“允许的”（即来源于一个生物）。
     * 这用于过滤掉环境伤害，确保只有战斗伤害才能触发装备效果或增加经验。
     * @param damageSource 伤害来源
     * @return 如果伤害直接来源于一个 LivingEntity，则返回 true
     */
    public static boolean isDamageSourceAllowed(DamageSource damageSource) {
        return damageSource.getSource() instanceof LivingEntity;
    }

    /**
     * 检查一个文本列表（Tooltip）中是否包含某个确切的字符串。
     * @param tooltip 物品的Tooltip列表
     * @param string  要查找的字符串
     * @return 如果找到完全匹配的行，则返回 true
     */
    public static boolean containsString(List<Text> tooltip, String string) {
        if (tooltip.isEmpty()) {
            return false;
        }
        for (Text component : tooltip) {
            if (component.getString().equals(string)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 在一个文本列表（Tooltip）中查找包含某个确切字符串的行的索引。
     * @param tooltip 物品的Tooltip列表
     * @param string  要查找的字符串
     * @return 如果找到，返回该行的索引；否则返回 -1
     */
    public static int lineContainsString(List<Text> tooltip, String string) {
        if (tooltip.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < tooltip.size(); ++i) {
            if (tooltip.get(i).getString().equals(string)) {
                return i;
            }
        }
        return -1;
    }
}