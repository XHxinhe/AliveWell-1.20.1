package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ZombieAttackGoal.class)
public abstract class ZombieAttackGoalMixin extends MeleeAttackGoal {
    public ZombieAttackGoalMixin(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
        super(mob, speed, pauseWhenMobIdle);
    }

    protected void attack(LivingEntity target, double squaredDistance) {
        double d = this.getSquaredMaxAttackDistance(target);
        Vec3d vec3d = new Vec3d(this.mob.getX(), this.mob.getEyeY()-1.14d, this.mob.getZ());
        Vec3d vec3d2 = new Vec3d(target.getX(), target.getEyeY(), target.getZ());
        boolean b = this.mob.getWorld().raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this.mob)).getType() == HitResult.Type.MISS;
        if (squaredDistance <= d && this.cooldown <= 0 && (this.mob.canSee(target) || b)) {
            this.resetCooldown();
            this.mob.swingHand(Hand.MAIN_HAND);
            this.mob.tryAttack(target);
        }
    }
    protected double getSquaredMaxAttackDistance(LivingEntity entity) {
        return 4.1f + entity.getWidth();
    }
}
