package com.XHxinhe.aliveandwell.item; // 包声明

import com.XHxinhe.aliveandwell.util.ModTags; // 导入自定义标签工具类
import com.mojang.datafixers.util.Pair; // 导入Pair工具类，用于返回结构位置和结构类型
import net.minecraft.entity.Entity; // 导入实体类
import net.minecraft.item.Item; // 导入物品基类
import net.minecraft.item.ItemStack; // 导入物品堆类
import net.minecraft.nbt.NbtHelper; // 导入NBT辅助类
import net.minecraft.registry.RegistryKeys; // 导入注册表键
import net.minecraft.registry.entry.RegistryEntry; // 导入注册表条目
import net.minecraft.registry.entry.RegistryEntryList; // 导入注册表条目列表
import net.minecraft.server.world.ServerWorld; // 导入服务端世界类
import net.minecraft.util.math.BlockPos; // 导入方块坐标类
import net.minecraft.util.math.GlobalPos; // 导入全局坐标类（含维度）
import net.minecraft.world.World; // 导入世界类
import net.minecraft.world.dimension.DimensionTypes; // 导入维度类型
import net.minecraft.world.gen.structure.Structure; // 导入结构类

import java.util.Optional; // 导入Optional工具类

/**
 * 古城指南针物品类，自动定位最近的远古城市结构
 */
public class CompassAncientCity extends Item { // 定义CompassAncientCity类，继承自Item

    public CompassAncientCity(Settings settings) { // 构造方法，传入物品设置
        super(settings); // 调用父类构造方法
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) { // 背包tick事件，每tick自动调用
        super.inventoryTick(stack, world, entity, slot, selected); // 调用父类方法
        if (!world.isClient && entity.age % 40 == 0) { // 只在服务端执行，每40tick（2秒）执行一次
            this.updatePos((ServerWorld) world, entity.getBlockPos(), stack); // 更新结构坐标
        }
    }

    /**
     * 查找最近的远古城市结构，并将其坐标写入物品NBT
     * @param world 当前服务端世界
     * @param center 查找中心点（通常为玩家位置）
     * @param stack 物品堆
     */
    public void updatePos(ServerWorld world, BlockPos center, ItemStack stack) { // 更新结构坐标方法
        Optional<RegistryEntryList.Named<Structure>> optional; // 定义Optional用于存储结构标签列表
        if (world.getDimensionKey() == DimensionTypes.OVERWORLD) { // 只在主世界查找
            optional = world.getRegistryManager() // 获取注册表管理器
                    .get(RegistryKeys.STRUCTURE) // 获取结构注册表
                    .getEntryList(ModTags.Structures.ANCIENT_CITY); // 获取自定义标签的结构列表
        } else {
            optional = Optional.empty(); // 如果不是主世界，返回空
        }

        if (optional.isPresent()) { // 如果找到了结构标签
            Pair<BlockPos, RegistryEntry<Structure>> pair = world.getChunkManager() // 获取区块管理器
                    .getChunkGenerator() // 获取区块生成器
                    .locateStructure(world, optional.get(), center, 100, false); // 查找最近的结构，半径100区块
            if (pair != null) { // 如果找到了结构位置
                stack.getOrCreateNbt().put("structurePos", NbtHelper.fromBlockPos(pair.getFirst())); // 写入NBT，key为structurePos，值为结构坐标
            }
        }
    }

    /**
     * 从物品NBT中读取结构坐标
     * @param world 当前世界
     * @param stack 物品堆
     * @return 结构的全局坐标（含维度），如果没有则返回null
     */
    public GlobalPos getStructurePos(World world, ItemStack stack) { // 获取结构坐标方法
        if (stack.hasNbt() && stack.getNbt().contains("structurePos")) { // 如果NBT存在且包含structurePos字段
            return GlobalPos.create( // 创建全局坐标对象
                    world.getRegistryKey(), // 当前世界的注册表键（维度信息）
                    NbtHelper.toBlockPos(stack.getNbt().getCompound("structurePos")) // 从NBT读取方块坐标
            );
        } else {
            return null; // 如果没有，返回null
        }
    }
}