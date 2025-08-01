package com.XHxinhe.aliveandwell.mixin.aliveandwell.block;

import net.minecraft.block.*;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(FallingBlock.class)

public abstract class FallingBlockMixin extends Block implements LandingBlock {
    public FallingBlockMixin(Settings settings) {
        super(settings);
    }

    // @Shadow
    // 这是一个注解，表示我们想要“引用”原版 FallingBlock 类中的同名方法。
    // 我们不需要写这个方法的具体实现，Mixin会自动帮我们链接到原版的方法。
    // 这个方法用于配置下落方块实体的一些属性（如下落时间）。
    @Shadow
    public void configureFallingBlockEntity(FallingBlockEntity entity) {
    }

    // 同样，引用原版的 onBlockAdded 方法。
    @Shadow
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
    }

    // @Unique
    // 这是一个注解，表示下面的方法是我们这个Mixin完全新增的，原版 FallingBlock 类中没有。
    // 这个方法用于判断一个位置是否能让方块“穿过”（比如空气、水）。
    @Unique
    public boolean canFall(World world, BlockPos pos) {
        // 调用原版 FallingBlock 的静态方法来判断方块是否可以穿过，并且确保位置在世界最低点之上。
        return FallingBlock.canFallThrough(world.getBlockState(pos)) && pos.getY() >= world.getBottomY();
    }

    // @Unique
    // 新增的核心逻辑方法：判断一个方块现在是否“准备好下落”。
    @Unique
    public boolean isReadyToFall(World world, BlockPos pos) {
        boolean fall = false; // 创建一个标志，默认为不下落。

        // 1. 标准垂直下落检查：
        // 如果脚下的方块可以被穿过，那么就准备下落。
        if (canFall(world, pos.down())) {
            fall = true;
        }
        // 2. 侧向坍塌检查：
        // 如果脚下是实体方块，并且自己不是铁砧（铁砧有特殊的下落逻辑，这里排除了它）。
        else if (!(world.getBlockState(pos).getBlock() instanceof AnvilBlock)){
            // 有 1/3 的概率进行侧向检查，并且只在服务器端进行。
            if (world.getRandom().nextInt(3) == 0 && !world.isClient()) {

                // 如果头顶有空间（防止在封闭空间内坍塌）...
                if (canFall(world, pos.add(0, 1, 0))) {
                    // ...然后检查四个侧向：
                    // 如果X负方向有空间，并且X负方向的下方也有空间（形成一个“L”形缺口）...
                    if (canFall(world, pos.add(-1, 0, 0)) && canFall(world, pos.add(-1, -1, 0))) {
                        fall = true; // ...那么就准备下落。
                        // 检查Z负方向...
                    } else if (canFall(world, pos.add(0, 0, -1)) && canFall(world, pos.add(0, -1, -1))) {
                        fall = true;
                        // 检查X正方向...
                    } else if (canFall(world, pos.add(1, 0, 0)) && canFall(world, pos.add(1, -1, 0))) {
                        fall = true;
                        // 检查Z正方向...
                    } else if (canFall(world, pos.add(0, 0, 1)) && canFall(world, pos.add(0, -1, 1))) {
                        fall = true;
                    }
                }
            }
        }
        // 返回最终的判断结果。
        return fall;
    }

    // @Unique
    // 新增方法：尝试让方块下落。
    @Unique
    public void tryToFall(World world, BlockPos pos) {
        // 如果方块“准备好下落”...
        if (isReadyToFall(world, pos)) {
            // ...创建一个新的下落方块实体。
            FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(world, (double) pos.getX() + 0.5D,
                    (double) pos.getY(), (double) pos.getZ() + 0.5D, world.getBlockState(pos));
            // 调用我们引用的原版方法来配置这个实体。
            this.configureFallingBlockEntity(fallingBlockEntity);
            // 移除原位置的方块。
            world.removeBlock(pos, true);
            // 在世界中生成这个下落的实体。
            world.spawnEntity(fallingBlockEntity);
        }
    }

    // @Inject(...)
    // 这是注入注解，表示我们要向原版方法中“插入”我们自己的代码。
    // at = @At("HEAD"): 指定插入点在原版方法的“头部”，即方法一开始就执行我们的代码。
    // method = "scheduledTick": 指定要注入的目标方法是 `scheduledTick`。这个方法会在游戏计划的时间刻被调用，用于处理方块的随机更新。
    @Inject(at = @At("HEAD"), method = "scheduledTick")
    // 这是我们注入的代码。
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, net.minecraft.util.math.random.Random random, CallbackInfo ci) {
        // 检查当前方块是不是泥土、沙土、带根泥土、灵魂沙、灵魂土或干草块。
        // (Block)(Object)this 是一种强制类型转换，用来获取当前的方块实例。
        if ((Block)(Object)this == Blocks.DIRT || (Block)(Object)this == Blocks.COARSE_DIRT || (Block)(Object)this == Blocks.ROOTED_DIRT || (Block)(Object)this == Blocks.SOUL_SAND || (Block)(Object)this == Blocks.SOUL_SOIL || (Block)(Object)this == Blocks.HAY_BLOCK) {
            // 如果是这些方块，并且它脚下的方块不是一个完整的固体方块...
            if (!world.getBlockState(pos.down()).isSolidBlock(world, pos.down())) {
                // ...就尝试让它下落。
                tryToFall(world, pos);
            }
        }
    }
}