package com.XHxinhe.aliveandwell.events;

import net.minecraft.entity.Entity; // 替换混淆名 class_1297
import net.minecraft.world.World;   // 替换混淆名 class_1937

/**
 * 实体攻击距离事件的回调接口 (混淆名替换版)
 * <p>
 * 这是一个功能性接口，作为 EntityEvents 中定义的自定义事件的类型。
 * 它定义了监听 "实体攻击距离" 事件时，回调函数所必须遵循的方法签名。
 * <p>
 * 使用 @FunctionalInterface 注解意味着它可以方便地通过 Lambda 表达式进行注册。
 * <p>
 * <b>使用示例:</b>
 * <pre>{@code
 * EntityEvents.ENTITY_ATTACK_DISTANCE.EVENT.register((world, entity) -> {
 *     // 在这里编写当事件触发时需要执行的代码
 *     // 例如，检查实体类型并可能修改其属性
 *     if (entity instanceof ZombieEntity) {
 *         System.out.println("A zombie is checking its attack distance in world " + world.getRegistryKey().getValue());
 *     }
 * });
 * }</pre>
 */
@FunctionalInterface
public interface ENTITY_ATTACK_DISTANCE { // 在实际代码中，这通常写作内部接口: public static interface ENTITY_ATTACK_DISTANCE

    /**
     * 当 "实体攻击距离" 事件被触发时调用的方法。
     *
     * @param world  事件发生的所在世界 (World)。
     * @param entity 触发此事件的实体 (Entity)。
     */
    void onEntityAttackDistance(World world, Entity entity);
}