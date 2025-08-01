package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.dimensions.DimsRegistry;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Objects;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends HostileEntity {
    @Final
    @Shadow
    private static TrackedData<Integer> FUSE_SPEED;
    @Final
    @Shadow
    private static TrackedData<Boolean> CHARGED;
    @Final
    @Shadow
    private static TrackedData<Boolean> IGNITED;
    @Shadow
    private int fuseTime = 78;
    @Shadow
    @Mutable
    private int explosionRadius = 3;

    protected CreeperEntityMixin(EntityType<? extends CreeperEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at=@At("RETURN"), method="<init>")
    public void Constructor(CallbackInfo info) {
        double i = 0;
        if(AliveAndWellMain.day >= 32) {
            i = 1.0*AliveAndWellMain.day / 30;
        }
        if(this.shouldRenderOverlay()){
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)).setBaseValue(80.0+1.5*i);
        }else {
            if (getWorld().getRegistryKey() == DimsRegistry.UNDER_WORLD_KEY  || getWorld().getRegistryKey() == World.NETHER) {
                Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)).setBaseValue(20.0+1.5*i);
            }else {
                Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)).setBaseValue(10.0+1.5*i);
            }
        }
    }


    public void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new CreeperIgniteGoal((CreeperEntity)(Object)this));
        this.goalSelector.add(3, new FleeEntityGoal<>(this, OcelotEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.add(3, new FleeEntityGoal<>(this, CatEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.add(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 48.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new RevengeGoal(this));
    }



    @Override
    public void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(FUSE_SPEED, -1);
        this.dataTracker.startTracking(CHARGED, false);
        this.dataTracker.startTracking(IGNITED, false);
    }

    @Shadow
    public void ignite(){}
    @Shadow
    public boolean isIgnited(){return false;}


    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo info) {
        this.fuseTime =AliveAndWellMain.day <= 160 ? (int) (78-AliveAndWellMain.day*0.3f) : 30;
        if(this.getAttacker() != null){
            this.setTarget(this.getAttacker());
        }
        if (this.getTarget() == null) {
            PlayerEntity player = getWorld().getClosestPlayer(getX(), getY(), getZ(), 24, false);
            if (player != null) {
                if (this.canSee(player)) {
                    this.setTarget(player);
                }else {
                    PlayerEntity player1 = this.getWorld().getClosestPlayer(getX(), getY(), getZ(), 16, false);
                    if (player1 != null) {
                        this.setTarget(player1);
                    }
                }
            }

        }
    }

    @Inject(at = @At("HEAD"), method = "explode", cancellable = true)
    private void explode(CallbackInfo ci) {
        if (!this.getWorld().isClient) {
//            Explosion.DestructionType destructionType = this.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;
            float f = this.shouldRenderOverlay() ? 2.0f + (AliveAndWellMain.day-32)*0.03f : 1.0f;
            this.dead = true;
//            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionRadius * f, destructionType);
            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionRadius * f, World.ExplosionSourceType.MOB);
            this.discard();
            this.spawnEffectsCloud();
        }
        ci.cancel();
    }

    @Shadow
    public boolean shouldRenderOverlay() {
        return this.dataTracker.get(CHARGED);
    }

    @Shadow
    private void spawnEffectsCloud() {
        Collection<StatusEffectInstance> collection = this.getStatusEffects();
        if (!collection.isEmpty()) {
            AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(this.getWorld(), this.getX(), this.getY(), this.getZ());
            areaEffectCloudEntity.setRadius(2.5f);
            areaEffectCloudEntity.setRadiusOnUse(-0.5f);
            areaEffectCloudEntity.setWaitTime(10);
            areaEffectCloudEntity.setDuration(areaEffectCloudEntity.getDuration() / 2);
            areaEffectCloudEntity.setRadiusGrowth(-areaEffectCloudEntity.getRadius() / (float)areaEffectCloudEntity.getDuration());
            for (StatusEffectInstance statusEffectInstance : collection) {
                areaEffectCloudEntity.addEffect(new StatusEffectInstance(statusEffectInstance));
            }
            this.getWorld().spawnEntity(areaEffectCloudEntity);
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if ((Boolean)this.dataTracker.get(CHARGED)) {
            nbt.putBoolean("powered", true);
        }

        nbt.putShort("Fuse", (short)this.fuseTime);
        nbt.putByte("ExplosionRadius", (byte)this.explosionRadius);
        nbt.putBoolean("ignited", this.isIgnited());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        this.dataTracker.set(CHARGED, nbt.getBoolean("powered"));
        if (nbt.contains("Fuse", 99)) {
            this.fuseTime = nbt.getShort("Fuse");
        }

        if (nbt.contains("ExplosionRadius", 99)) {
            this.explosionRadius = nbt.getByte("ExplosionRadius");
        }

        if (nbt.getBoolean("ignited")) {
            this.ignite();
        }

    }

}
