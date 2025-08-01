package com.XHxinhe.aliveandwell.registry.events;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.util.ActionResult;

/**
 * 玩家挖掘或放置方块时增加饥饿值消耗（疲劳值）
 */
public class AddExhaustion {

    /**
     * 注册事件监听器，初始化方法
     */
    public static void init() {

        // 监听玩家破坏方块事件（服务端）
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
            // 只在服务端执行，防止客户端重复调用
            if (!player.getWorld().isClient) {
                // 增加玩家疲劳值（饥饿消耗），数值可根据需要调整
                player.addExhaustion(0.002f);
            }
        });

        // 监听玩家放置方块事件（客户端和服务端都会触发）
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            // 增加玩家疲劳值（饥饿消耗）
            player.addExhaustion(0.002f);
            // 返回PASS表示不阻止后续事件处理
            return ActionResult.PASS;
        });
    }
}