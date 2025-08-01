package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitchEntity.class)
public abstract class WitchEntityMxin extends RaiderEntity implements RangedAttackMob {
    protected WitchEntityMxin(EntityType<? extends RaiderEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static DefaultAttributeContainer.Builder createWitchAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 52.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25).add(EntityAttributes.GENERIC_ARMOR,5.0);
    }

    @Inject(at = @At("HEAD"), method = "tickMovement",cancellable = true)
    public void tickMovement(CallbackInfo info) {
        if (this.getTarget() == null) {
            PlayerEntity player1 = getWorld().getClosestPlayer(getX(), getY(), getZ(), 16, false);
            if (player1 != null) {
                if (this.canSee(player1)) {
                    this.setTarget(player1);
                } else {
                    PlayerEntity player2 = this.getWorld().getClosestPlayer(getX(), getY(), getZ(), 8, false);
                    if (player2 != null) {
                        this.setTarget(player2);
                    }
                }
            }
        }
    }
}
