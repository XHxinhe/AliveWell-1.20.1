package com.XHxinhe.aliveandwell.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.projectile.ProjectileEntity; // 替换混淆名 class_1676
import net.minecraft.util.hit.HitResult;              // 替换混淆名 class_239

/**
 * 抛射物命中事件的回调接口。
 * <p>
 * 此接口用于监听和处理当一个抛射物（如箭、雪球等）击中一个实体或方块时的事件。
 */
@FunctionalInterface // 虽然未显式声明，但它符合功能性接口的定义
public interface ProjectileImpactEvents {

    /**
     * 当抛射物命中时触发的事件。
     * <p>
     * 这是一个可取消的事件。通过返回 {@code true}，可以阻止抛射物命中的默认行为
     * （例如，造成伤害、播放声音、销毁自身等）。
     * <p>
     * <b>Invoker 逻辑:</b> 只要有任何一个监听器返回 {@code true}，事件就会被取消。
     *
     * <b>使用示例:</b>
     * <pre>{@code
     * ProjectileImpactEvents.EVENT.register((projectile, hitResult) -> {
     *     // 检查是否是雪球命中了
     *     if (projectile instanceof SnowballEntity) {
     *         // 在命中点创建一个小型的、无害的爆炸效果
     *         projectile.world.createExplosion(null, hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z, 1.0f, Explosion.DestructionType.NONE);
     *
     *         // 返回 true 来取消雪球的原版命中效果（比如对烈焰人造成伤害）
     *         return true;
     *     }
     *
     *     // 对于其他抛射物，不干预
     *     return false;
     * });
     * }</pre>
     */
    Event<ProjectileImpactEvents> EVENT = EventFactory.createArrayBacked(ProjectileImpactEvents.class,
            (callbacks) -> (projectile, hitResult) -> {
                for (ProjectileImpactEvents callback : callbacks) {
                    // 如果任何一个回调返回 true，则立即中断并返回 true（取消事件）
                    if (callback.onImpact(projectile, hitResult)) {
                        return true;
                    }
                }
                // 如果所有回调都返回 false，则最终返回 false（不取消事件）
                return false;
            });

    /**
     * 当一个抛射物命中时调用的方法。
     *
     * @param projectile 命中的抛射物实体 (ProjectileEntity)。
     * @param hitResult  命中结果 (HitResult)，包含了命中位置和被命中的目标（可能是方块或实体）的信息。
     *                   可以将其强制转换为 BlockHitResult 或 EntityHitResult 来获取更具体的数据。
     * @return 返回 {@code true} 来取消默认的命中逻辑，返回 {@code false} 则允许其继续。
     */
    boolean onImpact(ProjectileEntity projectile, HitResult hitResult);
}