package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.MiningToolItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndCrystalEntity.class)
public abstract class EndCrystalEntityMixin extends Entity {

    public EndCrystalEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "damage",cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> ca) {
        if(source.getAttacker() instanceof PlayerEntity player){
            Item item = player.getMainHandStack().getItem();
            if(item instanceof MiningToolItem miningToolItem){
                if(miningToolItem.getMaterial().getMiningLevel() >= 3){
                    if (!this.isRemoved() && !this.getWorld().isClient) {
                        this.remove(RemovalReason.KILLED);
                        if (!source.isOf(DamageTypes.EXPLOSION)) {
                            this.getWorld().createExplosion((Entity)null, this.getX(), this.getY(), this.getZ(), 6.0F, true, World.ExplosionSourceType.BLOCK);
                        }
                        this.crystalDestroyed(source);
                    }
                    ca.setReturnValue(true);
                }
            }
        }
        ca.setReturnValue(false);
    }

    @Shadow
    private void crystalDestroyed(DamageSource source) {
        if (this.getWorld() instanceof ServerWorld) {
            EnderDragonFight enderDragonFight = ((ServerWorld) this.getWorld()).getEnderDragonFight();
            if (enderDragonFight != null) {
                enderDragonFight.crystalDestroyed((EndCrystalEntity) (Object)this, source);
            }
        }

    }


}
