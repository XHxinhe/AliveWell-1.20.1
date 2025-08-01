package com.XHxinhe.aliveandwell.mixin.xpgui;

import com.XHxinhe.aliveandwell.xpgui.common.PlayerStatsManagerAccess;
import com.XHxinhe.aliveandwell.xpgui.common.XPStates;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity  implements PlayerStatsManagerAccess {
    @Shadow public abstract void addExperience(int experience);

    @Unique
    private final PlayerEntity playerEntity = (PlayerEntity) (Object) this;
    @Unique
    private final XPStates xpStates = new XPStates(playerEntity);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At(value = "TAIL"))
    public void readCustomDataFromNbtMixin(NbtCompound tag, CallbackInfo info) {
        this.xpStates.readNbt(tag);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At(value = "TAIL"))
    public void writeCustomDataToNbtMixin(NbtCompound tag, CallbackInfo info) {
        this.xpStates.writeNbt(tag);
    }

    @Override
    public XPStates getPlayerStatsManager() {
        return this.xpStates;
    }

}
