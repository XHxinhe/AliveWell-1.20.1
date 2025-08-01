package com.XHxinhe.aliveandwell.events;

import net.minecraft.entity.Entity;                   // 替换混淆名 class_1297
import net.minecraft.entity.damage.DamageSource;      // 替换混淆名 class_1282
import net.minecraft.world.World;                     // 替换混淆名 class_1937

/**
 * 生物承伤计算事件的回调接口 (混淆名替换版)
 * <p>
 * 这是一个功能性接口，作为 EntityEvents 中定义的自定义事件的类型。
 * 它定义了监听 "生物承伤计算" 事件时，回调函数所必须遵循的方法签名。
 * <p>
 * 这是一个可修改的事件。通过返回一个新的浮点数值，可以改变实体最终受到的伤害量。
 *
 * <b>使用示例:</b>
 * <pre>{@code
 * EntityEvents.ENTITY_LIVING_DAMAGE.EVENT.register((world, entity, source, amount) -> {
 *     // 检查实体是否拥有“石肤”效果
 *     if (entity instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(StatusEffects.RESISTANCE)) {
 *         // 在原版抗性效果的基础上，再额外减少 10% 的伤害
 *         float finalAmount = amount * 0.9f;
 *         return finalAmount;
 *     }
 *     // 对于所有其他情况，不修改伤害值
 *     return amount;
 * });
 * }</pre>
 */
@FunctionalInterface
public interface Entity_Living_Damage { // 在实际代码中，这通常写作内部接口: public static interface Entity_Living_Damage

    /**
     * 当一个生物即将受到伤害，在进行最终伤害计算时调用的方法。
     *
     * @param world   事件发生的所在世界 (World)。
     * @param entity  即将受到伤害的实体 (Entity)。
     * @param source  本次伤害的来源 (DamageSource)。
     * @param amount  原始的、未经修改的伤害值。
     * @return 返回计算后的最终伤害值。可以返回 0 来免疫伤害，或返回比 `amount` 更大的值来增加伤害。
     */
    float onLivingDamageCalc(World world, Entity entity, DamageSource source, float amount);
}