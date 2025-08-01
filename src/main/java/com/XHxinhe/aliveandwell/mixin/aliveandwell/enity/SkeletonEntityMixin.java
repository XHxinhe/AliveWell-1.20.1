package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.dimensions.DimsRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ToolItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkeletonEntity.class)
public abstract class SkeletonEntityMixin extends AbstractSkeletonEntity {

    @Unique
    public int time;
    protected SkeletonEntityMixin(EntityType<? extends AbstractSkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at=@At("RETURN"), method="<init>")
    public void Constructor(CallbackInfo info) {
        double i = 0;
        double j = 0;
        double i1 = 0;
        double j1 = 0;
        if(AliveAndWellMain.day >= 24) {
            i =1.0* (AliveAndWellMain.day-24) /8;//主世界health
            j = 1.0* (AliveAndWellMain.day-24) / 10;//主世界damage
            i1 = 1.0*(AliveAndWellMain.day-32) /8;//地下地狱health
            j1 =1.0* (AliveAndWellMain.day-48) / 10;//地下地狱damage
        }
        if(i<0)i=0;
        if(j<0)j=0;
        if(i1<0)i1=0;
        if(j1<0)j1=0;

        if(j>=10){
            j=10;
        }
        if(j1>=10){
            j1=10;
        }

        if (getWorld().getRegistryKey() == DimsRegistry.UNDER_WORLD_KEY  || getWorld().getRegistryKey() == World.NETHER) {
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(20.0+1.5*i1);
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(3.0+j1);
        }else {
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(6.0+1.5*i);
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(1.0+j);
        }
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo info) {
        if (this.getTarget() == null) {
            PlayerEntity player1 = getWorld().getClosestPlayer(getX(), getY(), getZ(), 32, false);
            PlayerEntity player2= this.getWorld().getClosestPlayer(getX(), getY(), getZ(), 32, false);
            PlayerEntity player3= this.getWorld().getClosestPlayer(getX(), getY(), getZ(), 32, false);
            PlayerEntity player4= this.getWorld().getClosestPlayer(getX(), getY(), getZ(), 16, false);

            if (getWorld().getRegistryKey() == DimsRegistry.UNDER_WORLD_KEY){
                if(this.getAttacker() != null){
                    this.setTarget(this.getAttacker());
                }else {
                    if (player3 != null) {
                        if (this.canSee(player3)) {
                            this.setTarget(player3);
                        }else {
                            if (player4 != null) {
                                this.setTarget(player4);
                            }
                        }
                    }
                }
            }else {
                if(this.getAttacker() != null){
                    this.setTarget(this.getAttacker());
                }else {
                    if (player1 != null) {
                        if (this.canSee(player1)) {
                            this.setTarget(player1);
                        } else {
                            if (player2 != null) {
                                this.setTarget(player2);
                            }
                        }
                    }
                }
            }

        }


        if(this.getEquippedStack(EquipmentSlot.MAINHAND).getItem() instanceof ToolItem){
            this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.26D * (Math.min((1+AliveAndWellMain.day*0.0002)*1.07,1.2d)));
        }else {
            this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.26D * (Math.min(1+AliveAndWellMain.day*0.0002,1.2d)));
        }

        if(AliveAndWellMain.day >= 96){
            time++;
            if (time >= 3*60 * 20) {
                if (this.getWorld().getRegistryKey() != DimsRegistry.UNDER_WORLD_KEY) {
                    if (random.nextInt(120) + 1 == 1 && !this.hasStatusEffect(StatusEffects.STRENGTH)) {
                        this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 10 * 60 * 20));
                        time = 0;
                    } else if (random.nextInt(120) + 1 == 10 && !this.hasStatusEffect(StatusEffects.STRENGTH)) {
                        this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 10 * 60 * 20, 2));
                        time = 0;
                    }
                }else {
                    if (random.nextInt(80) + 1 == 1 && !this.hasStatusEffect(StatusEffects.STRENGTH)) {
                        this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 10 * 60 * 20));
                        time = 0;
                    } else if (random.nextInt(80) + 1 == 10 && !this.hasStatusEffect(StatusEffects.STRENGTH)) {
                        this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 10 * 60 * 20, 2));
                        time = 0;
                    }
                }
            }
        }
    }
}

