package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownEntity.class)
public abstract class ThrownEntityMixin extends ProjectileEntity {

    public ThrownEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/thrown/ThrownEntity;onCollision(Lnet/minecraft/util/hit/HitResult;)V"))
    public void tick(CallbackInfo ci) {
        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        onCollisionAlive(hitResult);
    }

    @Unique
    public void onEntityHitAlive(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();

        String name = Registries.ENTITY_TYPE.getId(this.getType()).toString();
//        System.out.println("222222222222222222222XXXXXXXXXX");

        if(name.contains("pswg:") && name.contains("_bolt")){
            entity.damage(this.getDamageSources().thrown(this, this.getOwner()), 26.0f);
//            System.out.println("11111111111111111XXXXXXXXXX");
        }
    }

    @Unique
    protected void onCollisionAlive(HitResult hitResult) {
        HitResult.Type type = hitResult.getType();
        if (type == HitResult.Type.ENTITY) {
            this.onEntityHitAlive((EntityHitResult)hitResult);
            this.getWorld().emitGameEvent(GameEvent.PROJECTILE_LAND, hitResult.getPos(), GameEvent.Emitter.of(this, null));
        } else if (type == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult)hitResult;
            this.onBlockHit(blockHitResult);
            BlockPos blockPos = blockHitResult.getBlockPos();
            this.getWorld().emitGameEvent(GameEvent.PROJECTILE_LAND, blockPos, GameEvent.Emitter.of(this, this.getWorld().getBlockState(blockPos)));
        }
    }
}
