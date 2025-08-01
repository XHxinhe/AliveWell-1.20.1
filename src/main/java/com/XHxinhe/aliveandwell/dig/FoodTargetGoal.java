package com.XHxinhe.aliveandwell.dig;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

/**
 * 寻找食物目标AI（混淆名替换版）
 * <p>
 * 该AI允许生物（MobEntity）在周围寻找掉落的食物物品（ItemEntity），并移动过去将其“吃掉”。
 * 可以通过布尔值配置生物是食肉、食草还是两者都吃。
 */
public class FoodTargetGoal extends Goal {
    protected @Nullable ItemEntity targetEntity;
    public boolean onlyEatsMeat;
    public boolean onlyEatsPlants;
    public MobEntity mob;
    private boolean active = false;

    public FoodTargetGoal(MobEntity mob, boolean checkVisibility) {
        this.mob = mob;
        // 此AI需要控制实体的移动
        this.setControls(EnumSet.of(Control.MOVE));
    }

    /**
     * 决定此AI任务是否可以开始执行。
     */
    @Override
    public boolean canStart() {
        this.findClosestTarget();
        return this.targetEntity != null;
    }

    /**
     * 获取搜索范围。
     */
    public int getFollowRange() {
        return 8;
    }

    /**
     * 根据给定的距离创建一个搜索用的边界框。
     */
    protected Box getSearchBox(double distance) {
        return this.mob.getBoundingBox().expand(distance, 4.0, distance);
    }

    /**
     * 在搜索框内寻找最近的有效食物目标。
     */
    protected void findClosestTarget() {
        // 获取世界中所有符合条件的掉落物实体
        List<ItemEntity> items = this.mob.getWorld().getEntitiesByClass(ItemEntity.class, this.getSearchBox(this.getFollowRange()), (itemEntity) -> {
            ItemStack itemStack = itemEntity.getStack();
            if (itemStack == null) {
                return false;
            }

            // 如果只吃植物，检查是否是特定的种子或蔬菜
            if (this.onlyEatsPlants) {
                Item item = itemStack.getItem();
                if (item == Items.CARROT || item == Items.POTATO || item == Items.WHEAT_SEEDS || item == Items.MELON_SEEDS || item == Items.PUMPKIN_SEEDS || item == Items.BEETROOT_SEEDS) {
                    return true;
                }
            }

            // 检查物品是否是食物
            if (itemStack.getItem().isFood()) {
                boolean isMeat = itemStack.getItem().getFoodComponent().isMeat();
                // 如果只吃肉，则物品必须是肉
                // 如果只吃植物，则物品必须不是肉
                // 如果两者都吃，则没有限制
                return (isMeat || !this.onlyEatsMeat) && (!isMeat || !this.onlyEatsPlants);
            } else {
                return false;
            }
        });

        float minDistance = Float.MAX_VALUE;
        // 遍历找到的物品，选出最近的一个
        for (ItemEntity entity : items) {
            float dist = entity.distanceTo(this.mob);
            if (dist < minDistance) {
                minDistance = dist;
                this.targetEntity = entity;
            }
        }
    }

    /**
     * AI任务开始时调用。
     */
    @Override
    public void start() {
        this.mob.setTarget(null); // 清除生物的攻击目标
        // 命令生物移动到食物的位置
        this.mob.getNavigation().startMovingTo(this.targetEntity.getX(), this.targetEntity.getY(), this.targetEntity.getZ(), 1.0);
        this.active = true;
        super.start();
    }

    /**
     * AI任务结束或被中断时调用。
     */
    @Override
    public void stop() {
        this.active = false;
    }

    /**
     * AI任务的每tick更新。
     */
    @Override
    public void tick() {
        super.tick();
        // 如果与目标的距离小于等于1格，则“吃掉”它
        if (this.targetEntity.distanceTo(this.mob) <= 1.0F) {
            // 如果吃的是药水，则替换为空瓶
            if (this.targetEntity.getStack().getItem() instanceof PotionItem) {
                this.targetEntity.setStack(new ItemStack(Items.GLASS_BOTTLE));
            } else {
                // 否则直接销毁物品实体
                this.targetEntity.discard();
            }
            // 任务完成，停止
            this.stop();
        }
    }

    /**
     * 决定此AI任务是否应该继续执行。
     */
    @Override
    public boolean shouldContinue() {
        // 如果导航空闲，或者目标不存在，或者目标已死亡（被捡起或销毁），则停止
        return !this.mob.getNavigation().isIdle() || this.targetEntity == null || !this.targetEntity.isAlive();
    }
}