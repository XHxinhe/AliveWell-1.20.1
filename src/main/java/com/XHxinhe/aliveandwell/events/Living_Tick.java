package com.XHxinhe.aliveandwell.events;

import net.minecraft.entity.Entity; // 替换混淆名 class_1297
import net.minecraft.world.World;   // 替换混淆名 class_1937

/**
 * 生物周期性更新（Tick）事件的回调接口 (混淆名替换版)
 * <p>
 * 这是一个功能性接口，作为 EntityEvents 中定义的自定义事件的类型。
 * 它定义了监听 "生物Tick" 事件时，回调函数所必须遵循的方法签名。
 * <p>
 * 此事件在每个生物的 tick() 方法被调用时触发（通常每秒20次）。
 * 它允许开发者为生物添加需要持续更新的逻辑。
 *
 * <b>使用示例:</b>
 * <pre>{@code
 * EntityEvents.LIVING_TICK.EVENT.register((world, entity) -> {
 *     // 检查实体是否在水中
 *     if (entity.isInWater()) {
 *         // 如果在水中，每20个tick（约1秒）给它一个短暂的速度提升效果
 *         if (entity.age % 20 == 0 && entity instanceof LivingEntity livingEntity) {
 *             livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 40, 0)); // 持续2秒的速度I
 *         }
 *     }
 * });
 * }</pre>
 */
@FunctionalInterface
public interface Living_Tick { // 在实际代码中，这通常写作内部接口: public static interface Living_Tick

    /**
     * 当生物（LivingEntity）的 tick() 方法被调用时执行。
     *
     * @param world  事件发生的所在世界 (World)。
     * @param entity 正在进行 tick 更新的实体 (Entity)。
     */
    void onTick(World world, Entity entity);
}