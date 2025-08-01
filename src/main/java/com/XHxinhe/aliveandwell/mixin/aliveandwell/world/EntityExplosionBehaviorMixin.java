package com.XHxinhe.aliveandwell.mixin.aliveandwell.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.explosion.EntityExplosionBehavior;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityExplosionBehavior.class)
public class EntityExplosionBehaviorMixin {
    @Shadow @Final private  Entity entity;

    @Inject(at = @At("HEAD"), method = "canDestroyBlock", cancellable = true)
    public void canDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power, CallbackInfoReturnable<Boolean> ca) {
        Block block = state.getBlock();
        if(explosion.getCausingEntity() instanceof CreeperEntity){
            CreeperEntity creeperEntity = (CreeperEntity)(explosion.getCausingEntity());
            if(creeperEntity.shouldRenderOverlay()){
                ca.setReturnValue(true);
            }else {
                if(block.getBlastResistance() >= 6.0f && !(block instanceof SlabBlock)){
                    ca.setReturnValue(false);
                }else {
                    ca.setReturnValue(true);
                }
            }
        }else {
            ca.setReturnValue(this.entity.canExplosionDestroyBlock(explosion, world, pos, state, power));
        }

    }
}
