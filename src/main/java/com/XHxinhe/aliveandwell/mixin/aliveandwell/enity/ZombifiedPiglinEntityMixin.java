package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.dimensions.DimsRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombifiedPiglinEntity.class)
public abstract class ZombifiedPiglinEntityMixin extends ZombieEntity implements Angerable {

    @Mutable
    @Final
    @Shadow private static final UniformIntProvider ANGER_PASSING_COOLDOWN_RANGE;
    public ZombifiedPiglinEntityMixin(EntityType<? extends ZombieEntity> entityType, World world) {
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
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(50.0+1.5*i1);
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(4.0+j1);
        }else {
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(30.0+1.5*i);
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(2.0+j);
        }
    }

    @Inject(at=@At("HEAD"), method="initCustomGoals")
    protected void initCustomGoals(CallbackInfo info) {
        this.targetSelector.add(2, new ActiveTargetGoal<>(this,PlayerEntity .class, true));
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static DefaultAttributeContainer.Builder createZombifiedPiglinAttributes() {
        return ZombieEntity.createZombieAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 4.0).add(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS, 0.2).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.19000000417232513).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void playAngrySound() {
        this.playSound(SoundEvents.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, this.getSoundVolume() * 0.4F, this.getSoundPitch() * 0.36F);
    }


    static {
        ANGER_PASSING_COOLDOWN_RANGE = TimeHelper.betweenSeconds(4, 6);
    }
}
