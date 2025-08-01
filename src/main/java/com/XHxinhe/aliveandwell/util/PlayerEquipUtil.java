package com.XHxinhe.aliveandwell.util;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;

public class PlayerEquipUtil {
    public PlayerEquipUtil() {
    }

    /**
     * 计算玩家身上穿着的“毁灭” (Doom) 套装组件数量。
     * @param player 目标玩家实体
     * @return 穿着的“毁灭”盔甲件数 (0-4)
     */
    public static int getWearingDoomArmorCount(PlayerEntity player) {
        // 获取玩家各个盔甲槽位的物品
        ItemStack head = player.getEquippedStack(EquipmentSlot.HEAD);
        ItemStack chest = player.getEquippedStack(EquipmentSlot.CHEST);
        ItemStack legs = player.getEquippedStack(EquipmentSlot.LEGS);
        ItemStack feet = player.getEquippedStack(EquipmentSlot.FEET);

        // 获取各物品的注册表ID名称
        String headName = Registries.ITEM.getId(head.getItem()).toString();
        String chestName = Registries.ITEM.getId(chest.getItem()).toString();
        String legsName = Registries.ITEM.getId(legs.getItem()).toString();
        String feetName = Registries.ITEM.getId(feet.getItem()).toString();

        // 分别检查每个部位的盔甲名称是否包含 "doom"
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int count4 = 0;
        if (headName.contains("doom")) {
            count1 = 1;
        }

        if (chestName.contains("doom")) {
            count2 = 1;
        }

        if (legsName.contains("doom")) {
            count3 = 1;
        }

        if (feetName.contains("doom")) {
            count4 = 1;
        }

        // 返回总件数
        return count1 + count2 + count3 + count4;
    }

    /**
     * 计算玩家身上穿着的“艾德曼” (Adamantium) 套装组件数量。
     * @param player 目标玩家实体
     * @return 穿着的“艾德曼”盔甲件数 (0-4)
     */
    public static int getWearingAamanArmorCount(PlayerEntity player) {
        // 获取玩家各个盔甲槽位的物品
        ItemStack head = player.getEquippedStack(EquipmentSlot.HEAD);
        ItemStack chest = player.getEquippedStack(EquipmentSlot.CHEST);
        ItemStack legs = player.getEquippedStack(EquipmentSlot.LEGS);
        ItemStack feet = player.getEquippedStack(EquipmentSlot.FEET);

        // 获取各物品的注册表ID名称
        String headName = Registries.ITEM.getId(head.getItem()).toString();
        String chestName = Registries.ITEM.getId(chest.getItem()).toString();
        String legsName = Registries.ITEM.getId(legs.getItem()).toString();
        String feetName = Registries.ITEM.getId(feet.getItem()).toString();

        // 分别检查每个部位的盔甲是否为对应的艾德曼盔甲
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int count4 = 0;
        if (headName.contains("adamantium_helmet")) {
            count1 = 1;
        }

        if (chestName.contains("adamantium_chestplate")) {
            count2 = 1;
        }

        if (legsName.contains("adamantium_leggings")) {
            count3 = 1;
        }

        if (feetName.contains("adamantium_boots")) {
            count4 = 1;
        }

        // 返回总件数
        return count1 + count2 + count3 + count4;
    }
}