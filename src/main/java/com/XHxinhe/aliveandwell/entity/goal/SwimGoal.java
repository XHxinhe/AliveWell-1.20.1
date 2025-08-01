package com.XHxinhe.aliveandwell.entity.goal;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * 自定义游泳AI（根据最新源码修正）
 * <p>
 * 这个AI控制一个鱿鱼实体的行为。它不仅包含随机游动，
 * 还包含了追踪和攻击目标（包括玩家和船只）的逻辑。
 */
public class SwimGoal extends Goal {
    private final SquidEntity squid;

    public SwimGoal(SquidEntity squid) {
        this.squid = squid;
    }

    /**
     * 决定此AI任务是否可以开始执行。总是可以。
     */
    @Override
    public boolean canStart() {
        return true;
    }

    /**
     * 决定此AI任务是否应该继续执行。总会继续。
     */
    @Override
    public boolean shouldContinue() {
        return true;
    }

    /**
     * AI任务的每tick更新。
     */
    @Override
    public void tick() {
        int despawnCounter = this.squid.getDespawnCounter();
        // 假设这是一个自定义的鱿鱼子类，它有getTarget()方法
        Entity target = this.squid.getTarget();

        if (target != null) {
            // 如果目标在船上，将目标设为船本身
            if (target.getVehicle() instanceof BoatEntity) {
                target = target.getVehicle();
            }
            // 如果目标不在水或气泡柱中，则清除目标
            else if (!target.isInsideWaterOrBubbleColumn()) {
                target = null;
            }
        }

        // 如果目标有效且在近距离
        if (target != null && target.distanceTo(this.squid) <= 2.0f) {
            // 向下拉扯目标，使其下沉
            target.setVelocity(target.getVelocity().x, target.getVelocity().y - 0.05f, target.getVelocity().z);

            if (target instanceof LivingEntity living) {
                // 如果目标没有缓慢效果，则施加效果
                if (!living.hasStatusEffect(StatusEffects.SLOWNESS)) {
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 6 * 20, 2)); // 缓慢III，持续6秒
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 8 * 20, 0));   // 中毒I，持续8秒
                    // 如果目标中毒且生命值极低，造成1点通用伤害
                    if (living.hasStatusEffect(StatusEffects.POISON) && living.getHealth() <= 1.0f) {
                        living.damage(living.getDamageSources().generic(), 1.0f);
                    }
                }
            }
            if (target instanceof BoatEntity boat) {
                // 对船造成气泡柱效果（将其向下拉）
                boat.onBubbleColumnCollision(true);
                // 造成巨额坠落伤害以摧毁船
                boat.damage(boat.getDamageSources().fall(), 999);
            }
        }

        if (despawnCounter > 100) {
            // 如果即将消失，则停止游动
            this.squid.setSwimmingVector(0.0F, 0.0F, 0.0F);
        } else if (this.squid.getRandom().nextInt(50) == 0 || !this.squid.hasSwimmingVector()) {
            // 如果有目标，朝远离目标的方向游动
            if (target != null) {
                Vec3d directionAway = new Vec3d(this.squid.getX() - target.getX(), this.squid.getY() - target.getY(), this.squid.getZ() - target.getZ());
                BlockPos nextPos = BlockPos.ofFloored(this.squid.getX() + directionAway.x, this.squid.getY() + directionAway.y, this.squid.getZ() + directionAway.z);
                BlockState blockState = this.squid.getWorld().getBlockState(nextPos);
                FluidState fluidState = this.squid.getWorld().getFluidState(nextPos);

                // 只有当目标位置是水或空气时才移动
                if (fluidState.isIn(FluidTags.WATER) || blockState.isAir()) {
                    double distance = directionAway.length();
                    if (distance > 0.0D) {
                        directionAway = directionAway.normalize();
                        float speedFactor = 3.0F;
                        // 距离越远，逃离速度越慢
                        if (distance > 5.0D) {
                            speedFactor = (float) (speedFactor - (distance - 5.0D) / 5.0D);
                        }
                        if (speedFactor > 0.0F) {
                            directionAway = directionAway.multiply(speedFactor);
                        }
                    }
                    // 如果目标方向是空气，则取消垂直移动
                    if (blockState.isAir()) {
                        directionAway = directionAway.subtract(0.0D, directionAway.y, 0.0D);
                    }
                    // 设置鱿鱼的游动向量（反向，因为是逃离）
                    this.squid.setSwimmingVector((float) -directionAway.x / 40.0F, (float) -directionAway.y / 40.0F, (float) -directionAway.z / 40.0F);
                }
            } else {
                // 如果没有目标，则随机游动
                float angle = this.squid.getRandom().nextFloat() * (float) Math.PI * 2.0F;
                float randomX = MathHelper.cos(angle) * 0.2F;
                float randomY = -0.1F + this.squid.getRandom().nextFloat() * 0.2F;
                float randomZ = MathHelper.sin(angle) * 0.2F;
                this.squid.setSwimmingVector(randomX, randomY, randomZ);
            }
        }
    }
}