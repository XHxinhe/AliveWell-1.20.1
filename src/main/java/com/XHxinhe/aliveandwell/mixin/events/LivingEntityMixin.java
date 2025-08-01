package com.XHxinhe.aliveandwell.mixin.events;

import com.XHxinhe.aliveandwell.events.EntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 这是一个用于 LivingEntity (生物实体) 的 Mixin。
 * 它创建了多个与实体生命周期相关的事件钩子，如 tick更新、受伤、死亡等。
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * 事件一：生物Tick更新事件 (Living Tick Event)
     * 在每个生物实体的 tick 方法开头触发。
     */
    @Inject(method = "tick()V", at = @At("HEAD"))
    public void LivingEntity_tick(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        World world = entity.getWorld();
        // 广播 "Living_Tick" 事件
        ((EntityEvents.Living_Tick) EntityEvents.LIVING_TICK.invoker()).onTick(world, entity);
    }

    /**
     * 事件二：生物承伤计算事件 (Damage Calculation Event)
     * 在计算最终伤害值时触发，允许修改伤害值。
     * @param f 原始伤害值。
     * @return 修改后的伤害值。
     */
    @ModifyVariable(method = "applyDamage", at = @At(value= "INVOKE_ASSIGN", target = "Ljava/lang/Math;max(FF)F", ordinal = 0), ordinal = 0, argsOnly = true)
    private float LivingEntity_actuallyHurt(float f, DamageSource damageSource, float damage) {
        LivingEntity livingEntity = (LivingEntity)(Object)this;
        World world = livingEntity.getWorld();

        // 广播 "ON_LIVING_DAMAGE_CALC" 事件，并获取返回值
        float newDamage = ((EntityEvents.Entity_Living_Damage) EntityEvents.ON_LIVING_DAMAGE_CALC.invoker()).onLivingDamageCalc(world, livingEntity, damageSource, f);
        // 如果事件监听者返回了一个有效的新伤害值，则使用它；否则，使用原伤害值。
        return newDamage != -1.0F && newDamage != f ? newDamage : f;
        }

    /**
     * 事件三：生物受击事件 (Attack Event)
     * 在生物即将受到伤害前触发，可以取消这次伤害。
     */
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void LivingEntity_hurt(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> ci) {
        LivingEntity livingEntity = (LivingEntity)(Object)this;
        World world = livingEntity.getWorld();

        if (!((EntityEvents.Entity_Living_Attack)EntityEvents.ON_LIVING_ATTACK.invoker()).onLivingAttack(world, livingEntity, damageSource, f)) {
            ci.setReturnValue(false);
        }
    }

    /**
     * 事件四：生物死亡事件 (Death Event)
     * 在生物死亡时触发。
     */
    @Inject(method = "onDeath", at = @At("HEAD"))
    public void LivingEntity_die(DamageSource damageSource, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        World world = entity.getWorld();

        // 广播 "LIVING_ENTITY_DEATH" 事件
        ((EntityEvents.Living_Entity_Death) EntityEvents.LIVING_ENTITY_DEATH.invoker()).onDeath(world, entity, damageSource);
    }
}
