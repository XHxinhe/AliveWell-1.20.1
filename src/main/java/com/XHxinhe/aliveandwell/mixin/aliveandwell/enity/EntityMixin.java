package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import com.XHxinhe.aliveandwell.registry.ItemInit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(Entity.class)
public abstract class EntityMixin implements Nameable, EntityLike, CommandOutput {
    @Shadow public abstract BlockPos getBlockPos();

    @Shadow private BlockPos blockPos;
    @Shadow
    private int fireTicks = -this.getBurningDuration();
    @Shadow
    private World world;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void onStruckByLightning(ServerWorld world, LightningEntity lightning) {
        if((Entity)(Object)this instanceof ServerPlayerEntity player) {
            if(player.getStackInHand(player.getActiveHand()).getItem() == ItemInit.ANCIENT_SWORD){
                this.setFireTicks(this.fireTicks + 1);
                if (this.fireTicks == 0) {
                    this.setOnFireFor(0);
                }
                this.damage(this.getDamageSources().lightningBolt(), 0.0f);
            }
        }

        List<PlayerEntity> players = this.getWorld().<PlayerEntity>getEntitiesByClass(
                PlayerEntity.class,
                new Box(this.getBlockPos().getX()-8,this.getBlockPos().getY()-8,this.getBlockPos().getZ()-8,
                        this.getBlockPos().getX()+8,this.getBlockPos().getY()+8,this.getBlockPos().getZ()+8),
                (e) -> e instanceof PlayerEntity
        );

        boolean hasPlayer = false;

        for (PlayerEntity player : players){
            if (player != null && player.getStackInHand(player.getActiveHand()).getItem() == ItemInit.ANCIENT_SWORD) {
                hasPlayer=true;
                break;
            }
        }


        if (hasPlayer) {
            if((Entity)(Object)this instanceof ItemEntity || (Entity)(Object)this instanceof PlayerEntity){
                this.setFireTicks(this.fireTicks + 1);
                if (this.fireTicks == 0) {
                    this.setOnFireFor(0);
                }
                this.damage(this.getDamageSources().lightningBolt(), 0.0f);
            }else {
                this.setFireTicks(this.fireTicks + 1);
                if (this.fireTicks == 0) {
                    this.setOnFireFor(8);
                }
                this.damage(this.getDamageSources().lightningBolt(), 50.0f);
            }
        } else {
            this.setFireTicks(this.fireTicks + 1);
            if (this.fireTicks == 0) {
                this.setOnFireFor(8);
            }
            this.damage(this.getDamageSources().lightningBolt(), 25.0f);
        }
    }

    @Shadow
    public abstract void setOnFireFor(int seconds);

    @Shadow
    public void setFireTicks(int fireTicks) {}

    @Shadow
    protected int getBurningDuration() {
        return 1;
    }

    @Shadow
    public abstract boolean damage(DamageSource source, float amount);

    @Shadow
    public abstract DamageSources getDamageSources();

    @Shadow
    public World getWorld() {
        return this.world;
    }
}
