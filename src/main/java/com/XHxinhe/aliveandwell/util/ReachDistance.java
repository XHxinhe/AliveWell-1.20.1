package com.XHxinhe.aliveandwell.util;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import java.util.Objects;

public class ReachDistance {
    public ReachDistance() {
    }

    /**
     * 根据玩家当前手持的物品和是否在空中，动态设置玩家的触及距离和攻击距离。
     * @param player 目标玩家
     */
    public static void setReachDistance(PlayerEntity player) {
        // 基础的距离增量，默认为 -0.5。
        float reach = -0.5f;

        // 获取玩家主手上的物品
        ItemStack selectedStack = player.getMainHandStack();
        if (selectedStack != null && !selectedStack.isEmpty()) {
            Item item = selectedStack.getItem();

            // 根据物品类型增加触及距离
            if (item == Items.STICK) { // 如果是木棍
                reach += 0.25F;
            } else if (item == Items.BONE) { // 如果是骨头
                reach += 0.25F;
            } else if (item instanceof TridentItem) { // 如果是三叉戟
                reach += 1.25F;
            } else if (item instanceof ShearsItem) { // 如果是剪刀
                reach += 0.5F;
            } else if (item instanceof ToolItem) { // 如果是任何工具 (包括镐、斧、锹、锄)
                reach += 0.75F;
            }
        }

        // 如果玩家不在地面上 (即在空中，如跳跃或下落时)
        if (!player.isOnGround()) {
            reach += 0.5F;
        }

        // 将计算出的 reach 值应用到玩家的触及距离和攻击距离属性上
        Objects.requireNonNull(player.getAttributeInstance(ReachEntityAttributes.REACH)).setBaseValue(reach);
        Objects.requireNonNull(player.getAttributeInstance(ReachEntityAttributes.ATTACK_RANGE)).setBaseValue(reach);
    }
}