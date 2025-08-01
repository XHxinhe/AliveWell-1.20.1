package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(
        targets = "net.minecraft.entity.mob.SpiderEntity$AttackGoal"
)
public abstract class SpiderAttackGoalMixin extends MeleeAttackGoal {
    public SpiderAttackGoalMixin(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
        super(mob, speed, pauseWhenMobIdle);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean canStart() {
        LivingEntity target = this.mob.getTarget();
        if(target != null){
            if(!(this.mob.canSee(target))){
                return false;
            }
        }
        return super.canStart() && !this.mob.hasPassengers();
    }
}
