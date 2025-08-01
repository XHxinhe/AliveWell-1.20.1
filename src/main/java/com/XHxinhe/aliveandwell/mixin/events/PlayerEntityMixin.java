package com.XHxinhe.aliveandwell.mixin.events;

import com.XHxinhe.aliveandwell.events.EntityEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * 这是一个用于 PlayerEntity (玩家实体) 的 Mixin。
 * 它为玩家实体也添加了伤害计算的事件钩子。
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * 玩家承伤计算事件 (Damage Calculation Event)
     * 这段代码与 LivingEntityMixin 中的完全相同。
     * 这可能是为了确保对玩家的伤害也能被正确地监听和修改，或者是一个冗余的实现。
     */
    @ModifyVariable(
            method = {"applyDamage"},
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Ljava/lang/Math;max(FF)F",
                    ordinal = 0
            ),
            ordinal = 0,
            argsOnly = true
    )
    private float LivingEntity_actuallyHurt(float f, DamageSource damageSource, float damage) {
        LivingEntity livingEntity = (LivingEntity)(Object)this;
        World world = livingEntity.getWorld();

        float newDamage = EntityEvents.ON_LIVING_DAMAGE_CALC.invoker().onLivingDamageCalc(world, livingEntity, damageSource, f);
        return newDamage != -1.0F && newDamage != f ? newDamage : f;
    }
}
