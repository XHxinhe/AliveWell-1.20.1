package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.CrossbowAttackGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PillagerEntity.class)
public abstract class PillagerEntityMixin extends IllagerEntity implements CrossbowUser, InventoryOwner {
    @Unique
    private final CrossbowAttackGoal<PillagerEntityMixin> bowAttackGoal = new CrossbowAttackGoal<PillagerEntityMixin>(this, 1.0, 8.0f);
    @Unique
    private final MeleeAttackGoal meleeAttackGoal = new MeleeAttackGoal(this, 1.2, false){

        @Override
        public void stop() {
            super.stop();
            PillagerEntityMixin.this.setAttacking(false);
        }

        @Override
        public void start() {
            super.start();
            PillagerEntityMixin.this.setAttacking(true);
        }
    };

    protected PillagerEntityMixin(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void PillagerEntity(EntityType entityType, World world, CallbackInfo ci) {
        this.updateAttackType();
    }

    @Inject(at = @At("HEAD"), method = "initialize")
    public void initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        this.updateAttackType();
    }

    @Unique
    public void updateAttackType() {
        if (this.getWorld() == null || this.getWorld().isClient) {
            return;
        }
        this.goalSelector.remove(this.meleeAttackGoal);
        this.goalSelector.remove(this.bowAttackGoal);
        ItemStack itemStack = this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.CROSSBOW));
        if (itemStack.isOf(Items.CROSSBOW)) {
            this.goalSelector.add(4, this.bowAttackGoal);
        } else {
            this.goalSelector.add(4, this.meleeAttackGoal);
        }
    }

    @Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        this.updateAttackType();
    }

    @Inject(at = @At("TAIL"), method = "initEquipment")
    protected void initEquipment(Random random, LocalDifficulty localDifficulty, CallbackInfo ci) {
        if (!this.getWorld().isClient) {
            this.updateAttackType();
        }
    }

    @Override
    public boolean canUseRangedWeapon(RangedWeaponItem weapon) {
        return weapon == Items.CROSSBOW;
    }
}
