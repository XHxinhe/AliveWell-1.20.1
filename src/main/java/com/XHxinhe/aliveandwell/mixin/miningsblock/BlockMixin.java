package com.XHxinhe.aliveandwell.mixin.miningsblock;

import com.XHxinhe.aliveandwell.miningsblock.MiningMixinHooks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * 这是一个用于 Block (方块) 的 Mixin。
 * 它的核心作用是拦截方块掉落物的生成过程，并将掉落物的位置重定向到一个统一的坐标。
 * 这通常是实现“连锁挖矿”(Vein Mining)功能的一部分，以确保所有连锁破坏的方块的掉落物都生成在同一个地方，方便玩家拾取。
 */
@Mixin(Block.class)
public class BlockMixin {
    public BlockMixin() {
    }

    /**
     * 修改 dropStack(World, BlockPos, ItemStack) 方法的 'pos' 参数。
     * @param pos 原始方法中传入的掉落物生成位置。
     * @param level       当前的世界。
     * @param unused      原始的 BlockPos 参数，这里我们不需要再次使用它，但必须在签名中声明以匹配。
     * @param stack       掉落的物品堆栈。
     * @return 一个新的 BlockPos，这将成为实际生成掉落物的位置。
     */
    @ModifyVariable(
        method = "dropStack(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)V",
        at = @At("HEAD"), // 在方法开头修改
        argsOnly = true   // 只针对方法的参数进行修改
    )
    private static BlockPos veinmining$dropStack(BlockPos pos, World level, BlockPos unused, ItemStack stack) {
        // 调用一个外部的辅助方法来获取“真正的”掉落位置。
        // 这个辅助方法很可能返回的是玩家最初挖掘的那个方块的位置。
        return MiningMixinHooks.getActualSpawnPos(level, pos);
    }
    /**
     * 对 dropStack 的另一个重载方法做同样的操作。
     * 这个版本额外接受一个 'Direction' 参数。
     */
    @ModifyVariable(
        method = "dropStack(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Lnet/minecraft/item/ItemStack;)V",
        at = @At("HEAD"),
        argsOnly = true
    )
    private static BlockPos veinmining$dropStackFromFace(BlockPos pos, World level, BlockPos unused, Direction direction, ItemStack stack) {
        // 同样调用辅助方法来重定向掉落位置。
        return MiningMixinHooks.getActualSpawnPos(level, pos);
    }
}
