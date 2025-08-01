package com.XHxinhe.aliveandwell.flintcoppertool.utils;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ActionResult;

/**
 * 禁用原版的木制和石制工具。
 * <p>
 * 为了提升模组引入的燧石和铜工具的价值，这个类通过监听玩家的各种交互事件，
 * 阻止了非创造模式下的玩家使用木制和石制工具。
 * 这强制玩家必须遵循模组设定的“打制燧石 -> 制作燧石工具”的游戏流程。
 */
public class DisableWoodStoneTools {

    public DisableWoodStoneTools() {
    }

    /**
     * 注册所有用于禁用木/石工具的事件回调。
     */
    public static void noStoneWoodTier() {
        // 统一的处理逻辑，用于检查玩家是否在非创造模式下使用被禁用的工具
        java.util.function.Predicate<ItemStack> isDisabledTool = (itemHeld) -> {
            // 检查物品是否是工具(ToolItem)或剑(SwordItem)
            if (itemHeld.getItem() instanceof ToolItem || itemHeld.getItem() instanceof SwordItem) {
                // 使用物品标签来判断是否为木制或石制工具
                if (itemHeld.isIn(ModItemTags.WOODEN_TOOLS) || itemHeld.isIn(ModItemTags.STONE_TOOLS)) {
                    return true; // 是被禁用的工具
                }
            }
            return false; // 不是被禁用的工具
        };

        // 监听：玩家右键使用方块
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (player == null || player.isCreative()) {
                return ActionResult.PASS; // 如果玩家为空或处于创造模式，则不禁用
            }
            if (isDisabledTool.test(player.getStackInHand(hand))) {
                return ActionResult.FAIL; // 禁用工具的使用
            }
            return ActionResult.PASS;
        });

        // 监听：玩家右键使用实体
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (player == null || player.isCreative()) {
                return ActionResult.PASS;
            }
            if (isDisabledTool.test(player.getStackInHand(hand))) {
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });

        // 监听：玩家左键攻击方块（开始挖掘）
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            if (player == null || player.isCreative()) {
                return ActionResult.PASS;
            }
            if (isDisabledTool.test(player.getStackInHand(hand))) {
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });

        // 监听：玩家左键攻击实体
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (player == null || player.isCreative()) {
                return ActionResult.PASS;
            }
            if (isDisabledTool.test(player.getStackInHand(hand))) {
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });
    }
}