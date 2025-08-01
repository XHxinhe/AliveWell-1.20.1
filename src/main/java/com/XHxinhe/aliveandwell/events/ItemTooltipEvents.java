package com.XHxinhe.aliveandwell.events;

import java.util.List;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.item.TooltipContext; // 替换混淆名 class_1836
import net.minecraft.entity.player.PlayerEntity; // 替换混淆名 class_1657
import net.minecraft.item.ItemStack;             // 替换混淆名 class_1799
import net.minecraft.text.Text;                 // 替换混淆名 class_2561
import org.jetbrains.annotations.Nullable;

/**
 * 物品提示框（Tooltip）事件的回调接口。
 * <p>
 * 此接口用于监听和修改当鼠标悬停在物品上时显示的提示信息。
 * 开发者可以通过注册此事件的监听器，向提示框中添加自定义的文本行。
 */
@FunctionalInterface // 虽然未显式声明，但它符合功能性接口的定义
public interface ItemTooltipEvents {

    /**
     * 当游戏为物品构建提示框时触发。
     * <p>
     * 这是一个通知性/修改性事件。监听器通过向 `tooltip` 列表中添加元素来修改最终的提示框内容。
     * <p>
     * <b>注意:</b> 原始代码中的字段名 "LIVING_TICK" 是一个明显的拼写错误，
     * 已在此处更正为 "ITEM_TOOLTIP" 以反映其真实用途。
     *
     * <b>使用示例:</b>
     * <pre>{@code
     * ItemTooltipEvents.ITEM_TOOLTIP.register((stack, player, tooltip, context) -> {
     *     // 检查物品是否是钻石剑
     *     if (stack.isOf(Items.DIAMOND_SWORD)) {
     *         // 在提示框中添加一行新的、红色的文本
     *         tooltip.add(Text.literal("A legendary blade!").formatted(Formatting.RED));
     *     }
     * });
     * }</pre>
     */
    Event<ItemTooltipEvents> LIVING_TICK = EventFactory.createArrayBacked(ItemTooltipEvents.class,
            (callbacks) -> (itemStack, player, tooltip, context) -> {
                // Invoker 逻辑：遍历所有注册的回调并依次调用它们
                for (ItemTooltipEvents callback : callbacks) {
                    callback.onItemTooltip(itemStack, player, tooltip, context);
                }
            });

    /**
     * 当物品提示框被构建时调用的方法。
     *
     * @param stack   正在显示提示框的物品堆栈 (ItemStack)。
     * @param player  查看该提示框的玩家 (PlayerEntity)。可能为 null，例如在创造模式物品栏的搜索界面。
     * @param tooltip 一个包含当前所有提示框文本行（Text）的列表。向此列表添加元素即可添加新行。
     * @param context 提示框的上下文 (TooltipContext)，包含了如是否启用高级提示框（F3+H）等信息。
     */
    void onItemTooltip(ItemStack stack, @Nullable PlayerEntity player, List<Text> tooltip, TooltipContext context);
}