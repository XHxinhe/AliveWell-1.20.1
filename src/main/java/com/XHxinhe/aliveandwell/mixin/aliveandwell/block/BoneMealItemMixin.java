package com.XHxinhe.aliveandwell.mixin.aliveandwell.block;

import net.minecraft.block.*;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BoneMealItem.class)
public abstract class BoneMealItemMixin extends Item {

    // Mixin 必需的构造函数
    public BoneMealItemMixin(Settings settings) {
        super(settings);
    }

    /**
     * @author [作者名]
     * @reason [修改原因] - 禁用骨粉对特定植物和作物的催熟功能，以增加游戏难度或平衡性。
     * @Overwrite
     * 完全重写原版的 useOnFertilizable 方法。
     * 这个方法是当骨粉被用于一个实现了 Fertilizable 接口的方块时调用的核心逻辑。
     */
    @Overwrite
    public static boolean useOnFertilizable(ItemStack stack, World world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();

        // 检查目标方块是否是可施肥的
        if (block instanceof Fertilizable fertilizable) {
            // isFertilizable 检查骨粉是否能应用在此方块上（例如，水下的海草）
            if (fertilizable.isFertilizable(world, pos, blockState, world.isClient)) {
                // 逻辑只在服务器端执行
                if (world instanceof ServerWorld serverWorld) {
                    // canGrow 检查方块是否还能继续生长（例如，作物是否已达最大年龄）
                    if (fertilizable.canGrow(world, world.random, pos, blockState)) {

                        // --- 这是Mod的核心修改点 ---
                        // 创建一个“黑名单”，如果目标方块是这些类型之一，则禁用骨粉效果。
                        if (fertilizable instanceof CropBlock ||         // 如小麦、胡萝卜、马铃薯
                                fertilizable instanceof SaplingBlock ||      // 各种树苗
                                fertilizable instanceof PlantBlock ||        // 如向日葵、牡丹等大型植物
                                fertilizable instanceof CaveVinesHeadBlock ||// 洞穴藤蔓（荧光浆果）
                                fertilizable instanceof MossBlock ||         // 苔藓块
                                fertilizable instanceof CocoaBlock) {        // 可可豆

                            // 如果是黑名单中的方块，直接返回 false，表示使用失败。
                            // 骨粉不会被消耗，方块也不会生长。
                            return false;
                        }
                        // --- 修改结束 ---

                        // 如果不在黑名单上，则执行正常的生长逻辑
                        fertilizable.grow(serverWorld, world.random, pos, blockState);
                    }
                    // 无论是否生长，只要 canGrow 检查通过（或被跳过），就消耗一个骨粉。
                    // 注意：这里的逻辑有一个小缺陷，见下文分析。
                    stack.decrement(1);
                }
                // 返回 true 表示使用成功（即使可能没有实际生长）
                return true;
            }
        }
        // 如果方块不可施肥，返回 false
        return false;
    }
}