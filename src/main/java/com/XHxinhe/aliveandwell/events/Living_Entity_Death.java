package com.XHxinhe.aliveandwell.events;

import net.minecraft.entity.Entity;                   // 替换混淆名 class_1297
import net.minecraft.entity.damage.DamageSource;      // 替换混淆名 class_1282
import net.minecraft.world.World;                     // 替换混淆名 class_1937

/**
 * 生物死亡事件的回调接口 (混淆名替换版)
 * <p>
 * 这是一个功能性接口，作为 EntityEvents 中定义的自定义事件的类型。
 * 它定义了监听 "生物死亡" 事件时，回调函数所必须遵循的方法签名。
 * <p>
 * 这是一个通知性事件，在生物死亡时触发，允许开发者执行与死亡相关的后续逻辑。
 *
 * <b>使用示例:</b>
 * <pre>{@code
 * EntityEvents.LIVING_ENTITY_DEATH.EVENT.register((world, entity, source) -> {
 *     // 检查是否是玩家杀死了这个实体
 *     if (source.getAttacker() instanceof PlayerEntity player) {
 *         // 向玩家发送一条祝贺信息
 *         player.sendMessage(Text.of("You have slain a " + entity.getType().getName().getString()), false);
 *
 *         // 在实体死亡处生成一个灵魂粒子效果
 *         if (world.isClient) {
 *             world.addParticle(ParticleTypes.SOUL, entity.getX(), entity.getY() + 0.5, entity.getZ(), 0, 0.1, 0);
 *         }
 *     }
 * });
 * }</pre>
 */
@FunctionalInterface
public interface Living_Entity_Death { // 在实际代码中，这通常写作内部接口: public static interface Living_Entity_Death

    /**
     * 当一个生物（LivingEntity）死亡时调用的方法。
     *
     * @param world  事件发生的所在世界 (World)。
     * @param entity 刚刚死亡的实体 (Entity)。
     * @param source 导致该实体死亡的伤害来源 (DamageSource)。
     */
    void onDeath(World world, Entity entity, DamageSource source);
}