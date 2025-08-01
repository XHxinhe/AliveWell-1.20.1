package com.XHxinhe.aliveandwell.events;

import net.minecraft.entity.Entity; // 替换混淆名 class_1297
import net.minecraft.world.World;   // 替换混淆名 class_1937

/**
 * 实体加入世界前事件的回调接口 (混淆名替换版)
 * <p>
 * 这是一个功能性接口，作为 EntityEvents 中定义的自定义事件的类型。
 * 它定义了监听 "实体加入世界前" 事件时，回调函数所必须遵循的方法签名。
 * <p>
 * 这是一个可取消的事件。通过返回 {@code true}，可以阻止实体被添加到世界中。
 * 这个事件通常比专门的生成事件更早、更底层。
 *
 * <b>使用示例:</b>
 * <pre>{@code
 * EntityEvents.PRE_ENTITY_JOIN_WORLD.EVENT.register((world, entity) -> {
 *     // 如果是服务器端，并且将要加入的是一只羊
 *     if (!world.isClient() && entity instanceof SheepEntity sheep) {
 *         // 有 5% 的几率阻止这只羊加入世界，并替换为一只狼
 *         if (world.random.nextFloat() < 0.05f) {
 *             WolfEntity wolf = new WolfEntity(EntityType.WOLF, world);
 *             wolf.refreshPositionAndAngles(sheep.getX(), sheep.getY(), sheep.getZ(), sheep.getYaw(), sheep.getPitch());
 *             world.spawnEntity(wolf);
 *             return true; // 返回 true，取消原绵羊的加入
 *         }
 *     }
 *     // 对于所有其他情况，不干预
 *     return false; // 返回 false，允许实体加入世界
 * });
 * }</pre>
 */
@FunctionalInterface
public interface Pre_Entity_Join_World { // 在实际代码中，这通常写作内部接口: public static interface Pre_Entity_Join_World

    /**
     * 当一个实体即将被添加到世界中时调用的方法。
     *
     * @param world  实体将要加入的世界 (World)。可以是客户端世界或服务器世界。
     * @param entity 即将加入世界的实体 (Entity)。
     * @return 返回 {@code true} 来取消本次加入，返回 {@code false} 则允许其继续。
     */
    boolean onPreSpawn(World world, Entity entity);
}