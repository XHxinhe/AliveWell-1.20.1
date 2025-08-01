package com.XHxinhe.aliveandwell.events;

import net.minecraft.entity.Entity;                   // 替换混淆名 class_1297
import net.minecraft.entity.damage.DamageSource;      // 替换混淆名 class_1282
import net.minecraft.world.World;                     // 替换混淆名 class_1937

/**
 * 生物攻击事件的回调接口 (混淆名替换版)
 * <p>
 * 这是一个功能性接口，作为 EntityEvents 中定义的自定义事件的类型。
 * 它定义了监听 "生物攻击" 事件时，回调函数所必须遵循的方法签名。
 * <p>
 * 这是一个可取消的事件。通过返回 {@code true}，可以阻止本次攻击的发生。
 *
 * <b>使用示例:</b>
 * <pre>{@code
 * EntityEvents.ENTITY_LIVING_ATTACK.EVENT.register((world, target, source, amount) -> {
 *     // 检查攻击者是否是玩家
 *     if (source.getAttacker() instanceof PlayerEntity attacker) {
 *         // 如果玩家正在潜行，则取消攻击（例如，实现一个“和平潜行”模式）
 *         if (attacker.isSneaking()) {
 *             attacker.sendMessage(Text.of("You cannot attack while sneaking."), true);
 *             return true; // 返回 true，取消本次攻击
 *         }
 *     }
 *     // 对于所有其他情况，不干预
 *     return false; // 返回 false，允许攻击继续
 * });
 * }</pre>
 */
@FunctionalInterface
public interface Entity_Living_Attack { // 在实际代码中，这通常写作内部接口: public static interface Entity_Living_Attack

    /**
     * 当一个生物即将攻击另一个实体时调用的方法。
     *
     * @param world   事件发生的所在世界 (World)。
     * @param target  被攻击的目标实体 (Entity)。
     * @param source  本次攻击的伤害来源 (DamageSource)，包含了攻击者和伤害类型等信息。
     * @param amount  本次攻击的基础伤害值。
     * @return 返回 {@code true} 来取消本次攻击，返回 {@code false} 则允许攻击继续。
     */
    boolean onLivingAttack(World world, Entity target, DamageSource source, float amount);
}