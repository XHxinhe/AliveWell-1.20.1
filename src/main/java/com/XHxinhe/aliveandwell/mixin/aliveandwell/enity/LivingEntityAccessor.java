package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Accessor("activeStatusEffects")
    Map<StatusEffect, StatusEffectInstance> getActiveStatusEffects();
}
