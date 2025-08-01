package com.XHxinhe.aliveandwell.mixin.aliveandwell.item;

import com.XHxinhe.aliveandwell.registry.ItemInit;
import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MiningToolItem.class)
public abstract class MiningToolItemMixin extends ToolItem implements Vanishable {
    public MiningToolItemMixin(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Inject(at = @At("HEAD"), method = "isSuitableFor", cancellable = true)
    public void isSuitableFor(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        //铁镐可挖掘黑曜石
        int i = this.getMaterial().getMiningLevel();
        if (i >= MiningLevels.IRON && state.isIn(BlockTags.NEEDS_DIAMOND_TOOL)){
           if(state.getBlock() == Blocks.OBSIDIAN || state.getBlock() == Blocks.CRYING_OBSIDIAN){
               cir.setReturnValue(true);
           }
        }
        if(i <= 0){
            if(Registries.BLOCK.getId(state.getBlock()).toString().contains("lignite_coal_ore")){
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(at=@At("RETURN"), method="postHit")
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> info) {
        int i = this.getMaterial().getMiningLevel();
        if(i>= 3){
            i = i -(int)(i / 2);
        }
        stack.damage(2+i*2, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
    }

    @Inject(at=@At("RETURN"), method="postMine")
    public void postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, CallbackInfoReturnable<Boolean> info) {
        int i = this.getMaterial().getMiningLevel();
        if(i>= 3){
            i = i -(int)(i / 2);
        }
        if (!world.isClient && state.getHardness(world, pos) != 0.0f) {
            stack.damage(1+i*2, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        if(stack.getItem() == ItemInit.copper_shears){
            if (!world.isClient && state.getHardness(world, pos) != 0.0f) {
                stack.damage(1, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            }
        }
    }
}
