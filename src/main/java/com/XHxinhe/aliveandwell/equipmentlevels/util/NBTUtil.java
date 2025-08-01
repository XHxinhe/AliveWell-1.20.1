package com.XHxinhe.aliveandwell.equipmentlevels.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

/**
 * NBT 工具类 (混淆名替换版)
 * <p>
 * 提供了一组简单的静态方法，用于安全地读取和写入 ItemStack 的 NBT 数据。
 * 这是一个常见的辅助类，旨在简化 NBT 操作并提高代码可读性。
 */
public class NBTUtil {

    public NBTUtil() {
    }

    /**
     * 加载或创建 ItemStack 的 NBT 标签。
     * <p>
     * 这个方法会获取与给定 ItemStack 关联的 NbtCompound。
     * 如果该 ItemStack 当前没有 NBT 数据，它会自动创建一个新的、空的 NbtCompound，
     * 将其附加到 ItemStack 上，然后返回它。这可以有效防止 NullPointerException。
     *
     * @param stack 要读取 NBT 的物品堆栈 (ItemStack)
     * @return 该物品堆栈的 NbtCompound，绝不会为 null。
     */
    public static NbtCompound loadStackNBT(ItemStack stack) {
        // ItemStack.getOrCreateNbt() 是获取或创建NBT的标准方法
        return stack.getOrCreateNbt();
    }

    /**
     * 将 NBT 标签保存到 ItemStack 上。
     * <p>
     * 这个方法将一个 NbtCompound 对象设置到给定的 ItemStack 上。
     * 如果传入的 nbt 对象为 null，则不会执行任何操作。
     *
     * @param stack 要写入 NBT 的物品堆栈 (ItemStack)
     * @param nbt   要保存的 NbtCompound 数据
     */
    public static void saveStackNBT(ItemStack stack, NbtCompound nbt) {
        if (nbt != null) {
            // ItemStack.setNbt() 是设置NBT的标准方法
            stack.setNbt(nbt);
        }
    }
}