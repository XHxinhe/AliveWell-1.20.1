package com.XHxinhe.aliveandwell.mixin.aliveandwell.villager;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.village.VillagerGossips;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VillagerEntity.class)
public interface VillagerEntityAccessor {
    @Accessor("gossip")
    VillagerGossips getGossip();
}
