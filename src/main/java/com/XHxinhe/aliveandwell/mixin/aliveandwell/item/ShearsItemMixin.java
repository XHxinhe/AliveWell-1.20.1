package com.XHxinhe.aliveandwell.mixin.aliveandwell.item;

import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShearsItem;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.block.Block.dropStack;

@Mixin(ShearsItem.class)
public abstract class ShearsItemMixin extends Item {
    public ShearsItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(at = @At("HEAD"), method = "useOnBlock", cancellable = true)
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> ca) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (block instanceof LeavesBlock || block.getDefaultState().isIn(BlockTags.WOOL)  || block instanceof CobwebBlock) {
            world.removeBlock(blockPos,true);
            dropStack(world, blockPos, new ItemStack(block.asItem()));

            ItemStack itemStack = context.getStack();
            itemStack.damage(1,context.getPlayer(), (player) -> {
                player.sendToolBreakStatus(context.getHand());
            });

            world.playSound(context.getPlayer(),blockPos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
            ca.setReturnValue(ActionResult.success(world.isClient));
        }
    }
}
