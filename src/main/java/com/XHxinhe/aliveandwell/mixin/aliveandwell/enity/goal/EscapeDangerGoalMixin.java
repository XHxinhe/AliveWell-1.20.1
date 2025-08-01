package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EscapeDangerGoal.class)
public class EscapeDangerGoalMixin  {
    @Final
    @Shadow
    protected PathAwareEntity mob;
    @Final
    @Shadow
    protected double speed;
    @Shadow
    protected double targetX;
    @Shadow
    protected double targetY;
    @Shadow
    protected double targetZ;
    @Shadow
    protected boolean active;

    @Inject(
            at=@At("HEAD"),
            method="canStart"
    )
    public void canStart(CallbackInfoReturnable<Boolean> info) {
        World world = this.mob.getWorld();
        PlayerEntity close = world.getClosestPlayer(mob.getX(), mob.getY(), mob.getZ(), 8, (p) -> {
            PlayerEntity player = (PlayerEntity) p;
            if (this.mob instanceof HorseEntity) {
                if (((HorseEntity)mob).isTame()) {
                    return false;
                }
            }
            if (this.mob instanceof TameableEntity) {
                if (((TameableEntity)mob).isTamed()) {
                    return false;
                }
            }
            return !player.isCreative() && !player.isSneaking();
        });


        List<LivingEntity> scared = world.getEntitiesByClass(
                LivingEntity.class,
                new Box(mob.getX() - 8, mob.getY() - 8, mob.getZ() - 8, mob.getX() + 8, mob.getY() + 8, mob.getZ() + 8),
                (e) -> e instanceof AnimalEntity && e.getAttacker() != null
        );

        if (close != null) {
            //玩家靠近是否攻击了生物
            for (LivingEntity livingEntity : scared) {
                if (livingEntity.getAttacker() != null) {
                    this.mob.setAttacker(livingEntity.getAttacker());
                    break;

                }
            }

            if(this.mob.getName().toString().contains("goose")
                    || this.mob.getName().toString().contains("boar")
                    || this.mob.getName().toString().contains("coyote")
                    || this.mob.getName().toString().contains("bear")
                    || this.mob.getName().toString().contains("wolf")
                    || this.mob.getName().toString().contains("lion")
                    || this.mob.getName().toString().contains("coyote")
                    || this.mob.getName().toString().contains("bear")
                    || this.mob.getName().toString().contains("horned_sheep")
                    || this.mob.getName().toString().contains("snake")
            ){
                this.mob.setAttacker(close);
            }
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void start() {
        if(this.mob instanceof CowEntity){
            this.mob.getNavigation().startMovingTo(this.targetX, this.targetY, this.targetZ, this.speed*0.95);
        }else {
            this.mob.getNavigation().startMovingTo(this.targetX, this.targetY, this.targetZ, this.speed*1.15);
        }
        this.active = true;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean findTarget() {
        Vec3d vec3d = NoPenaltyTargeting.find(this.mob, 5, 4);
        LivingEntity attacker = this.mob.getAttacker();
        if (attacker != null) {
            Vec3d direction = mob.getPos().subtract(attacker.getPos()).normalize();
            vec3d = NoPenaltyTargeting.findTo(this.mob, 10, 5, direction.multiply(5).add(this.mob.getPos()), 15.0D);
        }

        if (vec3d == null) {
            return false;
        } else {
            this.targetX = vec3d.x;
            this.targetY = vec3d.y;
            this.targetZ = vec3d.z;
            return true;
        }
    }
}
