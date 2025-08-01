package com.XHxinhe.aliveandwell.mixin.aliveandwell.block;

import com.XHxinhe.aliveandwell.block.FallingBlockHelper;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin extends AbstractBlock {
    public BlockMixin(Settings settings) {
        super(settings);
    }

//    @Overwrite
//    public void  onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
    @Inject(at = @At("HEAD"), method = "onPlaced")
    public void  onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack,CallbackInfo ca) {
        if (!world.isClient()) {
            if (placer != null) {
                if ((Object)this == Blocks.DIRT || (Object)this == Blocks.COARSE_DIRT || (Object)this == Blocks.ROOTED_DIRT || (Object)this == Blocks.SOUL_SAND || (Object)this == Blocks.SOUL_SOIL ) {
                    FallingBlockHelper.tryToFall(world, pos);
                }
                if ((Object)this == Blocks.HAY_BLOCK) {
                    if (!world.getBlockState(pos.down()).isSolidBlock(world, pos.down())) {
                        FallingBlockHelper.tryToFall(world, pos);
                    }
                }
            }
        }
    }
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!world.isClient()) {
            if ((Object)this == Blocks.DIRT || (Object)this == Blocks.COARSE_DIRT || (Object)this == Blocks.ROOTED_DIRT || (Object)this == Blocks.SOUL_SAND || (Object)this == Blocks.SOUL_SOIL || (Object)this == Blocks.HAY_BLOCK) {
                world.scheduleBlockTick(pos, (Block)(Object)this, 2);
            }
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Inject(at = @At("RETURN"), method = "onLandedUpon")
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ca) {
        if (!world.isClient()) {
//            if ((Block)(Object)this == Blocks.DIRT || (Block)(Object)this == Blocks.COARSE_DIRT || (Block)(Object)this == Blocks.ROOTED_DIRT || (Block)(Object)this == Blocks.SOUL_SAND || (Block)(Object)this == Blocks.SOUL_SOIL || (Block)(Object)this instanceof FallingBlock || (Block)(Object)this == Blocks.HAY_BLOCK) {
            if ((Object)this == Blocks.DIRT || (Object)this == Blocks.COARSE_DIRT || (Object)this == Blocks.ROOTED_DIRT || (Object)this == Blocks.SOUL_SAND || (Object)this == Blocks.SOUL_SOIL  || (Object)this == Blocks.HAY_BLOCK) {
                FallingBlockHelper.tryToFall(world, pos);
            }
        }

    }
    @Shadow
    public Item asItem() {
        return null;
    }

    @Shadow
    protected Block asBlock() {
        return null;
    }
}

