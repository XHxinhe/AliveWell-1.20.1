package com.XHxinhe.aliveandwell.events;

import net.minecraft.entity.player.PlayerEntity; // 替换混淆名 class_1657
import net.minecraft.item.ItemStack;             // 替换混淆名 class_1799
import net.minecraft.world.World;                 // 替换混淆名 class_1937
import org.jetbrains.annotations.NotNull;

/**
 * 玩家射箭事件的回调接口 (混淆名替换版)
 * <p>
 * 这是一个功能性接口，作为 PlayerEvents 中定义的自定义事件的类型。
 * 它定义了监听 "玩家射箭" 事件时，回调函数所必须遵循的方法签名。
 * <p>
 * 这是一个可修改的事件，在玩家松开弓弦、即将发射箭矢时触发。
 * 它允许监听器修改射击的蓄力值，从而影响箭矢的速度和伤害。
 *
 * <b>使用示例:</b>
 * <pre>{@code
 * PlayerEvents.ARROW_LOOSE.EVENT.register((player, bowStack, world, charge, hasAmmo) -> {
 *     // 如果玩家正在潜行
 *     if (player.isSneaking()) {
 *         // 无论蓄力多久，都视为满蓄力（原版满蓄力值为20）
 *         // 这里的实现可能需要根据具体情况调整，此处仅为示例
 *         return 20;
 *     }
 *     // 否则，不修改原始蓄力值
 *     return charge;
 * });
 * }</pre>
 */
@FunctionalInterface
public interface Arrow_Loose { // 在实际代码中，这通常写作内部接口: public static interface Arrow_Loose

    /**
     * 当玩家松开弓弦发射箭矢时调用的方法。
     *
     * @param player   正在射箭的玩家 (PlayerEntity)。
     * @param bowStack 玩家正在使用的弓的物品堆栈 (ItemStack)。
     * @param world    事件发生的所在世界 (World)。
     * @param charge   原始的蓄力时间（ticks）。这个值越大，射击力度越强。
     * @param hasAmmo  一个布尔值，通常表示玩家是否拥有弹药（或者是否处于创造模式）。
     * @return 返回修改后的蓄力时间。返回原始的 `charge` 值表示不作修改。
     */
    int onArrowLoose(PlayerEntity player, @NotNull ItemStack bowStack, World world, int charge, boolean hasAmmo);
}