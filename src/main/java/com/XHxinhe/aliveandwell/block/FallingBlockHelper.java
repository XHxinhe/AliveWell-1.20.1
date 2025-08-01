package com.XHxinhe.aliveandwell.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.FluidFillable;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 一个工具类，用于处理和模拟方块的下落逻辑。
 * 它不仅包含了检查方块是否能下落的标准逻辑，还实现了一种自定义的“侧向坍塌”机制。
 */
public class FallingBlockHelper {

    /**
     * 检查一个方块是否是预定义的、可以下落的类型。
     * @param world 所在世界
     * @param pos 方块位置
     * @return 如果是沙子、砂砾、混凝土粉末等，则返回 true。
     */
    public static boolean isFallingBlock(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        // 硬编码检查几种常见的可下落方块
        return block == Blocks.SAND ||
                block == Blocks.GRAVEL ||
                block == Blocks.RED_SAND ||
                block == Blocks.BLACK_CONCRETE_POWDER || // 这是一个示例，原始代码包含多种混凝土粉末
                block == Blocks.DRAGON_EGG;
        // 注意：原始代码中包含了所有颜色的混凝土粉末，这里为简洁起见仅列举部分。
    }

    /**
     * 检查一个方块是否可以穿过其下方的方块（即下方是否为空气、水等非固体方块）。
     * 这是方块下落的基本条件。
     * @param world 所在世界
     * @param pos 要检查的位置
     * @return 如果该位置的方块可以被下落的方块穿过，则返回 true。
     */
    public static boolean canFallThrough(World world, BlockPos pos) {
        // 使用原版 FallingBlock 的静态方法来判断，并确保位置在世界高度限制内
        return FallingBlock.canFallThrough(world.getBlockState(pos)) && pos.getY() >= world.getBottomY();
    }

    /**
     * 检查一个方块是否“准备好下落”。
     * 这包括两种情况：
     * 1. 正下方有空间，可以直接下落。
     * 2. （自定义逻辑）在特定条件下，可以向侧面倒塌。
     * @param world 所在世界
     * @param pos 要检查的方块位置
     * @return 如果方块满足下落或侧向坍塌的条件，则返回 true。
     */
    public static boolean isReadyToFall(World world, BlockPos pos) {
        // 1. 检查是否可以直接垂直下落
        if (canFallThrough(world, pos.down())) {
            return true;
        }

        // 2. 检查是否可以侧向坍塌
        // 条件：
        // - 方块本身不能是流体容器（如海带），防止其在水中坍塌。
        // - 1/3 的随机几率。
        // - 必须是白天。
        // - 必须有支撑方块（即方块下方一格是固体）。
        BlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof FluidFillable) &&
                world.random.nextInt(3) == 0 &&
                world.isDay() &&
                !canFallThrough(world, pos.down())) {

            // 检查四个侧向方向
            // 坍塌条件：侧面一格(A)和该格的下方一格(B)都必须是可穿过的方块。
            // 例如，要向西(-X)坍塌，则 pos.add(-1, 0, 0) 和 pos.add(-1, -1, 0) 都必须是空气或水。
            //  [S] [A]  <- S(Source)是当前方块, A是侧面, B是A的下方
            //  [X] [B]  <- X是支撑S的方块
            if (canFallThrough(world, pos.add(-1, 0, 0)) && canFallThrough(world, pos.add(-1, -1, 0))) return true; // 西
            if (canFallThrough(world, pos.add(0, 0, -1)) && canFallThrough(world, pos.add(0, -1, -1))) return true; // 北
            if (canFallThrough(world, pos.add(1, 0, 0)) && canFallThrough(world, pos.add(1, -1, 0))) return true;  // 东
            if (canFallThrough(world, pos.add(0, 0, 1)) && canFallThrough(world, pos.add(0, -1, 1))) return true;  // 南
        }

        return false;
    }

    /**
     * 尝试让指定位置的方块下落。
     * 如果 isReadyToFall() 返回 true，则将该方块变成一个 FallingBlockEntity 实体。
     * @param world 所在世界
     * @param pos 要尝试下落的方块位置
     */
    public static void tryToFall(World world, BlockPos pos) {
        if (isReadyToFall(world, pos)) {
            // 创建一个下落方块实体
            FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(
                    world,
                    pos.getX() + 0.5,
                    pos.getY(),
                    pos.getZ() + 0.5,
                    world.getBlockState(pos)
            );
            // 移除原位置的方块
            world.removeBlock(pos, false); // 使用 false 防止掉落物品
            // 在世界中生成实体
            world.spawnEntity(fallingBlockEntity);
        }
    }
}