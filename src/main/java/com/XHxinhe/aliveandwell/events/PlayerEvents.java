package com.XHxinhe.aliveandwell.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

/**
 * 模组的自定义玩家事件中心。
 * <p>
 * 这个类使用 Fabric API 的事件系统来定义和管理一系列与玩家相关的自定义事件。
 * 其他代码可以通过此类中定义的静态 Event 字段来注册监听器。
 */
public final class PlayerEvents { // 'final' is a good practice for utility classes with private constructors

    // --- Event Definitions ---

    /**
     * 在玩家松开弓弦、即将发射箭矢时触发。
     * <p>
     * <b>Invoker 逻辑:</b> 遍历所有监听器。只要有一个监听器返回了与原始蓄力值不同的新值，
     * 事件链就会立即中断，并返回这个新值。如果所有监听器都未作修改（即都返回了原始蓄力值），
     * 则最终返回一个哨兵值 {@code -1}，表示应使用原版逻辑。
     * <p>
     * 这是一个可修改的事件。
     */
    public static final Event<Arrow_Loose> ON_ARROW_LOOSE = EventFactory.createArrayBacked(Arrow_Loose.class,
            (callbacks) -> (player, bow, world, charge, hasAmmo) -> {
                for (Arrow_Loose callback : callbacks) {
                    int newCharge = callback.onArrowLoose(player, bow, world, charge, hasAmmo);
                    // 如果蓄力值被任何一个回调修改，则立即返回新的值
                    if (newCharge != charge) {
                        return newCharge;
                    }
                }
                // -1 是一个哨兵值，表示没有监听器修改蓄力值
                return -1;
            });

    /**
     * 在物品被插入到玩家物品栏时触发。
     * <p>
     * <b>Invoker 逻辑:</b> 遍历所有监听器并依次调用它们。
     * <p>
     * 这是一个通知性事件。
     */
    public static final Event<PlayerInventoryInsert> PLAYER_INVENTORY_INSERT = EventFactory.createArrayBacked(PlayerInventoryInsert.class,
            (callbacks) -> (player, itemStack) -> {
                for (PlayerInventoryInsert callback : callbacks) {
                    callback.onPlayerInventoryInsert(player, itemStack);
                }
            });

    /**
     * 私有构造函数，防止该工具类被实例化。
     */
    private PlayerEvents() {
    }

    // --- Callback Interface Definitions ---

    /**
     * @see com.XHxinhe.aliveandwell.events.PlayerInventoryInsert
     */
    @FunctionalInterface
    public interface PlayerInventoryInsert {
        void onPlayerInventoryInsert(PlayerEntity player, @NotNull ItemStack stack);
    }

    /**
     * @see com.XHxinhe.aliveandwell.events.Arrow_Loose
     */
    @FunctionalInterface
    public interface Arrow_Loose {
        int onArrowLoose(PlayerEntity player, @NotNull ItemStack bowStack, World world, int charge, boolean hasAmmo);
    }
}