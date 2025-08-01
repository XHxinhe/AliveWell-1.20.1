package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.dimensions.DimsRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherSkeletonEntity.class)
public abstract class WitherSkeletonEntityMixin extends AbstractSkeletonEntity {
    protected WitherSkeletonEntityMixin(EntityType<? extends AbstractSkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at=@At("RETURN"), method="<init>")
    public void Constructor(CallbackInfo info) {
        double i = 0;
        double j = 0;
        double i1 = 0;
        double j1 = 0;
        if(AliveAndWellMain.day >= 24) {
            i = 1.0*(AliveAndWellMain.day-24) /10;
            j =  1.0*(AliveAndWellMain.day-24) / 10;
            i1 = 1.0*(AliveAndWellMain.day-24) /10;
            j1 =1.0* (AliveAndWellMain.day-24) / 10;
        }
        if(j>=15){
            j=15;
        }
        if(j1>=15){
            j1=15;
        }

        if (getWorld().getRegistryKey() == DimsRegistry.UNDER_WORLD_KEY  || getWorld().getRegistryKey() == World.NETHER) {
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(50.0+1.5*i1);
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(8.0+j1);
        }else {
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(30.0+1.5*i);
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(6.0+j);
        }
    }
}
