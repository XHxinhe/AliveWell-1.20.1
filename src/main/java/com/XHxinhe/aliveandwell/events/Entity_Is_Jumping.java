package com.XHxinhe.aliveandwell.events;

import net.minecraft.entity.Entity; // 替换混淆名 class_1297
import net.minecraft.world.World;   // 替换混淆名 class_1937

/**
 * 生物跳跃事件的回调接口 (混淆名替换版)
 * <p>
 * 这是一个功能性接口，作为 EntityEvents 中定义的自定义事件的类型。
 * 它定义了监听 "生物跳跃" 事件时，回调函数所必须遵循的方法签名。
 * <p>
 * 此事件在生物（LivingEntity）执行跳跃动作时触发，允许开发者添加与跳跃相关的逻辑。
 *
 * <b>使用示例:</b>
 * <pre>{@code
 * EntityEvents.ENTITY_IS_JUMPING.EVENT.register((world, entity) -> {
 *     // 检查是否是玩家在跳跃
 *     if (entity instanceof PlayerEntity) {
 *         // 在玩家脚下生成一圈云朵粒子效果
 *         if (world.isClient) { // 仅在客户端执行，避免不必要的服务器负载
 *             world.addParticle(ParticleTypes.CLOUD, entity.getX(), entity.getY(), entity.getZ(), 0.0, 0.0, 0.0);
 *         }
 *     }
 * });
 * }</pre>
 */
@FunctionalInterface
public interface Entity_Is_Jumping { // 在实际代码中，这通常写作内部接口: public static interface Entity_Is_Jumping

    /**
     * 当生物（LivingEntity）跳跃时调用的方法。
     *
     * @param world  事件发生的所在世界 (World)。
     * @param entity 正在跳跃的实体 (Entity)。
     */
    void onLivingJump(World world, Entity entity);
}