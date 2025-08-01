package com.XHxinhe.aliveandwell.events;

import net.minecraft.entity.player.PlayerEntity; // 替换混淆名 class_1657
import net.minecraft.item.ItemStack;             // 替换混淆名 class_1799
import org.jetbrains.annotations.NotNull;

/**
 * 玩家物品栏插入事件的回调接口 (混淆名替换版)
 * <p>
 * 这是一个功能性接口，作为 PlayerEvents 中定义的自定义事件的类型。
 * 它定义了监听 "物品插入玩家物品栏" 事件时，回调函数所必须遵循的方法签名。
 * <p>
 * 这是一个通知性事件，在物品被添加到玩家物品栏时触发。
 *
 * <b>使用示例:</b>
 * <pre>{@code
 * PlayerEvents.PLAYER_INVENTORY_INSERT.EVENT.register((player, stack) -> {
 *     // 检查玩家是否拾取了“古老的遗物”
 *     if (stack.isOf(MyModItems.ANCIENT_RELIC)) {
 *         // 向玩家发送一条消息
 *         player.sendMessage(Text.of("You feel a strange power emanating from the relic."), true);
 *
 *         // 给物品添加一个“已绑定”的NBT标签
 *         stack.getOrCreateNbt().putBoolean("isBound", true);
 *     }
 * });
 * }</pre>
 */
@FunctionalInterface
public interface PlayerInventoryInsert { // 在实际代码中，这通常写作内部接口: public static interface PlayerInventoryInsert

    /**
     * 当一个物品堆栈被插入到玩家物品栏时调用的方法。
     *
     * @param player 正在接收物品的玩家 (PlayerEntity)。
     * @param stack  被插入到物品栏的物品堆栈 (ItemStack)。这是一个非空（@NotNull）的引用。
     */
    void onPlayerInventoryInsert(PlayerEntity player, @NotNull ItemStack stack);
}