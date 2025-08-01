package com.XHxinhe.aliveandwell.events;

import net.minecraft.entity.Entity;             // 替换混淆名 class_1297
import net.minecraft.server.world.ServerWorld;  // 替换混淆名 class_3218

/**
 * 实体生成事件的回调接口 (混淆名替换版)
 * <p>
 * 这是一个功能性接口，作为 EntityEvents 中定义的自定义事件的类型。
 * 它定义了监听 "实体生成" 事件时，回调函数所必须遵循的方法签名。
 * <p>
 * 这是一个可取消的服务器端事件。通过返回 {@code true}，可以阻止实体的生成。
 *
 * <b>使用示例:</b>
 * <pre>{@code
 * EntityEvents.ENTITY_SPAWN.EVENT.register((serverWorld, entity) -> {
 *     // 检查是否是苦力怕（Creeper）将要生成
 *     if (entity instanceof CreeperEntity) {
 *         // 如果是在主世界（Overworld）生成，则取消它
 *         if (serverWorld.getRegistryKey() == World.OVERWORLD) {
 *             // 可以在这里向服务器日志输出一条信息
 *             System.out.println("Prevented a Creeper from spawning in the Overworld.");
 *             return true; // 返回 true，取消生成
 *         }
 *     }
 *     // 对于所有其他情况，不干预
 *     return false; // 返回 false，允许生成
 * });
 * }</pre>
 */
@FunctionalInterface
public interface Entity_Spawn { // 在实际代码中，这通常写作内部接口: public static interface Entity_Spawn

    /**
     * 当一个实体即将在服务器世界中生成时调用的方法。
     *
     * @param serverWorld 将要生成实体的服务器世界 (ServerWorld)。
     * @param entity      将要生成的实体实例 (Entity)。
     * @return 返回 {@code true} 来取消本次生成，返回 {@code false} 则允许生成。
     */
    boolean onEnitySpawn(ServerWorld serverWorld, Entity entity);
}