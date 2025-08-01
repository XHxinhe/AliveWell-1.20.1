package com.XHxinhe.aliveandwell.mixin.aliveandwell.block;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin extends PlantBlock implements Fertilizable {

    // Mixin 必需的构造函数
    public CropBlockMixin(Settings settings) {
        super(settings);
    }

    /**
     * 注入到 CropBlock.getAvailableMoisture 方法的开头，并取消原方法。
     * 这个方法的作用是计算作物可用的水分，该值直接影响作物的生长速度。
     * 此 Mixin 的目的是重写水分计算逻辑，引入一个基于全局游戏天数的周期性惩罚。
     */
    @Inject(at = @At("HEAD"), method = "getAvailableMoisture", cancellable = true)
    private static void modifyMoistureBasedOnDay(Block block, BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir) {

        // --- 第一部分：几乎完全复制原版的水分计算逻辑 ---

        // 基础水分值
        float moisture = 1.0F;
        BlockPos farmlandPos = pos.down();

        // 检查下方 3x3 区域的方块
        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                float bonus = 0.0F;
                BlockState neighborState = world.getBlockState(farmlandPos.add(i, 0, j));
                // 如果是耕地
                if (neighborState.isOf(Blocks.FARMLAND)) {
                    bonus = 1.0F;
                    // 如果是湿润的耕地，奖励更高
                    if (neighborState.get(FarmlandBlock.MOISTURE) > 0) {
                        bonus = 3.0F;
                    }
                }
                // 周围方块的贡献度要除以4
                if (i != 0 || j != 0) {
                    bonus /= 4.0F;
                }
                moisture += bonus;
            }
        }

        // --- 第二部分：几乎完全复制原版的“密集种植惩罚”逻辑 ---

        BlockPos north = pos.north();
        BlockPos south = pos.south();
        BlockPos west = pos.west();
        BlockPos east = pos.east();
        // 检查东西方向是否有同类作物
        boolean hasWestEastNeighbor = world.getBlockState(west).isOf(block) || world.getBlockState(east).isOf(block);
        // 检查南北方向是否有同类作物
        boolean hasNorthSouthNeighbor = world.getBlockState(north).isOf(block) || world.getBlockState(south).isOf(block);

        // 如果作物被其他同类作物紧密包围，水分减半
        if (hasWestEastNeighbor && hasNorthSouthNeighbor) {
            moisture /= 2.0F;
        } else {
            // 检查对角线方向是否有同类作物
            boolean hasDiagonalNeighbor = world.getBlockState(west.north()).isOf(block) || world.getBlockState(east.north()).isOf(block) || world.getBlockState(east.south()).isOf(block) || world.getBlockState(west.south()).isOf(block);
            if (hasDiagonalNeighbor) {
                moisture /= 2.0F;
            }
        }

        // --- 第三部分：Mod的核心修改点 ---
        // 在计算出标准水分值后，根据全局游戏天数应用一个乘数。

        if (AliveAndWellMain.day % 6 == 0) {
            // 在游戏天数是6的倍数的日子里，作物只能获得标准水分的 35%。
            cir.setReturnValue(moisture * 0.35f);
        } else {
            // 在其他所有日子里，作物只能获得标准水分的 15%。
            cir.setReturnValue(moisture * 0.15f);
        }
    }
}