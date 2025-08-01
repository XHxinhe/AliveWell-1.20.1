package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import com.XHxinhe.aliveandwell.entity.goal.SwimGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SquidEntity.class)
public abstract class SquidEntityMixin extends WaterCreatureEntity {

    protected SquidEntityMixin(EntityType<? extends WaterCreatureEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "initGoals", cancellable = true)
    protected void initGoals(CallbackInfo info) {
        this.goalSelector.add(0, new SwimGoal((SquidEntity)(Object)this));
        this.goalSelector.add(1, new ActiveTargetGoal<>((SquidEntity)(Object)this, PlayerEntity.class, true));
        this.goalSelector.add(2, new ActiveTargetGoal<>((SquidEntity)(Object)this, AnimalEntity.class, true));
        info.cancel();
    }
}
