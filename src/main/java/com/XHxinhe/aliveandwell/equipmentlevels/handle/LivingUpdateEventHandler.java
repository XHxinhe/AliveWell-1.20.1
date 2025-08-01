package com.XHxinhe.aliveandwell.equipmentlevels.handle;

import com.XHxinhe.aliveandwell.equipmentlevels.core.Experience;
import com.XHxinhe.aliveandwell.equipmentlevels.util.EAUtil;
import com.XHxinhe.aliveandwell.equipmentlevels.util.NBTUtil;
import com.XHxinhe.aliveandwell.events.EntityEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

/**
 * 生物更新事件处理器（混淆名替换版）
 * <p>
 * 监听生物的tick更新事件，主要用于在游戏运行时持续检查玩家背包，
 * 并为符合条件的、尚未初始化的装备自动添加升级系统所需的NBT标签。
 */
public class LivingUpdateEventHandler {
    // 这个计数器在当前代码中并未被使用，可能是遗留的或为未来功能准备的。
    private static int count = 0;

    public LivingUpdateEventHandler() {
    }

    /**
     * 初始化事件监听器。
     */
    public static void onUpdate() {
        // 注册一个监听器，该监听器会在每个生物的每个tick（游戏更新周期）被调用
        EntityEvents.LIVING_TICK.register((world, entity) -> {
            // 逻辑只对玩家生效
            if (entity instanceof PlayerEntity player) {
                // 获取玩家的主物品栏列表
                DefaultedList<ItemStack> mainInventory = player.getInventory().main;

                // 仅在非创造模式下执行，以避免在创造模式下不必要的检查
                if (!player.getAbilities().creativeMode) {
                    // 遍历主物品栏中的每一个物品堆栈
                    for (ItemStack stack : mainInventory) {
                        // 如果物品堆栈不为空
                        if (stack != ItemStack.EMPTY) {
                            Item item = stack.getItem();
                            // 检查该物品是否是模组支持强化的类型
                            if (EAUtil.canEnhance(item)) {
                                NbtCompound nbt = NBTUtil.loadStackNBT(stack);
                                // 检查该物品是否已经被启用（即拥有 "EA_ENABLED" 标签）
                                if (!Experience.isEnabled(nbt)) {
                                    // 如果未启用，则为其启用。
                                    // 这里的 'okay' 变量似乎是多余的，但我们保留原始逻辑。
                                    boolean okay = true;
                                    if (okay) {
                                        Experience.enable(nbt, true);
                                        // 注意：这里只修改了nbt对象，但没有调用 NBTUtil.saveStackNBT。
                                        // 这意味着NBT的保存可能依赖于Minecraft自身的物品更新机制，
                                        // 或者这是一个潜在的逻辑缺陷。但在反编译的代码中，确实没有保存步骤。
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}