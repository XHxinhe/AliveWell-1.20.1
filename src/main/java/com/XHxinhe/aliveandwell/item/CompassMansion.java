package com.XHxinhe.aliveandwell.item; // 声明包名

import com.XHxinhe.aliveandwell.util.ModTags;
import com.mojang.datafixers.util.Pair; // 导入Pair工具类，用于返回两个相关对象
import java.util.Optional; // 导入Optional，用于避免空指针异常

import net.minecraft.entity.Entity; // 导入实体类
import net.minecraft.item.Item; // 导入物品基类
import net.minecraft.item.ItemStack; // 导入物品堆类
import net.minecraft.nbt.NbtHelper; // 导入NBT辅助类
import net.minecraft.registry.RegistryKeys; // 导入注册表键
import net.minecraft.registry.entry.RegistryEntry; // 导入注册表条目
import net.minecraft.registry.entry.RegistryEntryList; // 导入注册表条目列表
import net.minecraft.server.world.ServerWorld; // 导入服务端世界类
import net.minecraft.util.math.BlockPos; // 导入方块坐标类
import net.minecraft.util.math.GlobalPos; // 导入全局坐标类（含维度信息）
import net.minecraft.world.World; // 导入世界基类
import net.minecraft.world.dimension.DimensionTypes; // 导入维度类型
import net.minecraft.world.gen.structure.Structure; // 导入结构类

/**
 * 林地府邸指南针物品类
 * 自动定位最近的林地府邸结构，并将其坐标写入物品NBT
 */
public class CompassMansion extends Item { // 定义CompassMansion类，继承自Item

    // 构造方法，传入物品设置参数
    public CompassMansion(Item.Settings settings) {
        super(settings); // 调用父类构造方法
    }

    // 背包tick事件，每tick自动调用
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected); // 调用父类方法，保持原有行为
        // 只在服务端执行，每40tick（2秒）执行一次定位
        if (!world.isClient && entity.age % 40 == 0) {
            this.updatePos((ServerWorld) world, entity.getBlockPos(), stack); // 更新结构坐标
        }
    }

    /**
     * 查找最近的林地府邸结构，并将其坐标写入物品NBT
     * @param world 当前服务端世界
     * @param center 查找中心点（通常为玩家位置）
     * @param stack 物品堆
     */
    public void updatePos(ServerWorld world, BlockPos center, ItemStack stack) {
        Optional<RegistryEntryList.Named<Structure>> optional; // 定义Optional用于存储结构标签列表
        // 只在主世界（Overworld）查找
        if (world.getDimensionKey() == DimensionTypes.OVERWORLD) {
            optional = world.getRegistryManager() // 获取注册表管理器
                    .get(RegistryKeys.STRUCTURE) // 获取结构注册表
                    .getEntryList(ModTags.Structures.MANSION); // 获取林地府邸结构标签列表
        } else {
            optional = Optional.empty(); // 如果不是主世界，返回空
        }

        // 如果找到了结构标签
        if (optional.isPresent()) {
            // 查找最近的结构，半径100区块
            Pair<BlockPos, RegistryEntry<Structure>> pair = world.getChunkManager()
                    .getChunkGenerator()
                    .locateStructure(world, optional.get(), center, 100, false);
            // 如果NBT存在且找到了结构位置
            if (stack.getOrCreateNbt() != null && pair != null && stack.getNbt() != null) {
                // 写入NBT，key为structurePos，值为结构坐标
                stack.getNbt().put("structurePos", NbtHelper.fromBlockPos(pair.getFirst()));
            }
        }
    }

    /**
     * 从物品NBT中读取结构坐标
     * @param world 当前世界
     * @param stack 物品堆
     * @return 结构的全局坐标（含维度），如果没有则返回null
     */
    public GlobalPos getStructurePos(World world, ItemStack stack) {
        // 如果NBT存在，读取structurePos字段，返回GlobalPos，否则返回null
        return stack.hasNbt()
                ? GlobalPos.create(
                world.getRegistryKey(), // 当前世界的注册表键（维度信息）
                NbtHelper.toBlockPos(stack.getNbt().getCompound("structurePos")) // 从NBT读取方块坐标
        )
                : null;
    }
}