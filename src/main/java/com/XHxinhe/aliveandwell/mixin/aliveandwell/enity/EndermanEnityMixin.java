package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.dimensions.DimsRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(EndermanEntity.class)
public abstract class EndermanEnityMixin extends HostileEntity implements Angerable {
    @Unique
    public Random random = new Random();
    @Unique
    public int time;

    protected EndermanEnityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at=@At("RETURN"), method="<init>")
    public void Constructor(EntityType<? extends HostileEntity> entityType, World world, CallbackInfo info) {
        double i = 0;
        double j = 0;
        double i1 = 0;
        double j1 = 0;
        if(AliveAndWellMain.day >= 32) {
            i = 1.0*AliveAndWellMain.day /10;
            j = 1.0*AliveAndWellMain.day / 16;
            i1 = 1.0*(AliveAndWellMain.day-10) /10;
            j1 = 1.0*(AliveAndWellMain.day-10) / 16;
        }
        if(j>=20){
            j=20;
        }
        if(j1>=20){
            j1=20;
        }
        if (world.getRegistryKey() == DimsRegistry.UNDER_WORLD_KEY  || world.getRegistryKey() == World.NETHER) {
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(150.0+i1);
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(60.0+j1);
        }else {
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(100.0+i);
            this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(50.0+j);
        }
    }


    @Inject(at=@At("HEAD"), method="mobTick")
    protected void mobTick(CallbackInfo ca) {
        time++;
        if (this.getTarget() == null) {
            PlayerEntity player = getWorld().getClosestPlayer(getX(), getY(), getZ(), 128, false);
            if(player != null){
                if(player.getInventory().contains(Items.ENDER_EYE.getDefaultStack()) || player.getInventory().contains(Items.ENDER_PEARL.getDefaultStack())){
                    if(time>=300 && random.nextInt(200) == 5){
                        time = 0 ;
                        if(this.canSee(player)){
                            this.setTarget(player);
                        }
                    }
                }
            }
        }

        if(this.getAttacker() != null){
            this.setTarget(this.getAttacker());
        }
    }

}
