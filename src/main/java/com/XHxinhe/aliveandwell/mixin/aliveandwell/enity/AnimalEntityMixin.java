package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import net.minecraft.block.GrassBlock;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin extends PassiveEntity  {
    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @Unique
    private int moreTime;
    @Unique
    private int smokeTime;

    @Unique
    private boolean sick;

    protected AnimalEntityMixin(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    public boolean shouldDropLoot() {
        return super.shouldDropLoot() && !isSick();
    }

    @Inject(
            at = @At("TAIL"),
            method = "writeCustomDataToNbt"
    )
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("moreTime", this.moreTime);
        nbt.putBoolean("sick", this.sick);
    }

    @Inject(
            at = @At("TAIL"),
            method = "readCustomDataFromNbt"
    )
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        moreTime = nbt.getInt("moreTime");
        sick = nbt.getBoolean("sick");
    }

    @Inject(
            at = @At("HEAD"),
            method = "mobTick"
    )
    public void mobTick(CallbackInfo ci) {
        List<AnimalEntity> scared = getWorld().<AnimalEntity>getEntitiesByClass(
                AnimalEntity.class,
                new Box(this.getX() - 8, this.getY() - 8, this.getZ() - 8, this.getX() + 8, this.getY() + 8, this.getZ() + 8),
                (e) -> e instanceof AnimalEntity
        );

        if(scared.size() >= 16){
            moreTime++;
        }else {
            moreTime=0;
        }

        sick = moreTime > 60 * 20;
    }

    @Inject(at = @At("TAIL"), method = "tickMovement")
    public void tickMovement(CallbackInfo ci) {
        if(isSick() ){
            if(smokeTime > 0){
                smokeTime--;
            }else {
                smokeTime=600;
            }
        }else {
            smokeTime=600;
        }

        if(isSick() && this.smokeTime % 60 == 0){
            this.damage(this.getDamageSources().dryOut(),0.2f);
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
        }
    }

    @Inject(at = @At("RETURN"), method = "isValidNaturalSpawn", cancellable = true)
    private static void isValidNaturalSpawn(EntityType<? extends AnimalEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue((world.getBlockState(pos.down()).getBlock() instanceof GrassBlock) && world.getBaseLightLevel(pos, 0) > 8);
    }

    @Inject(
            method = "canBreedWith",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/passive/AnimalEntity;isInLove()Z"
            ),
            cancellable = true
    )
    public void canBreedWith(AnimalEntity other, CallbackInfoReturnable<Boolean> cir) {
        if(isSick()){
            cir.setReturnValue(false);
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean isBreedingItem(ItemStack stack) {
        if(isSick()){
            return false;
        }
        return stack.isOf(Items.WHEAT);
    }

    @Override
    public int getXpToDrop() {
        return isSick() ? 0 : 1 + this.getWorld().random.nextInt(3);
    }

    @Unique
    public boolean isSick() {
        return sick;
    }
}
