package com.XHxinhe.aliveandwell.events;

import net.minecraft.entity.Entity; // 替换混淆名 class_1297
import net.minecraft.world.World;   // 替换混淆名 class_1937

/**
 * 实体坠落伤害计算事件的回调接口 (混淆名替换版)
 * <p>
 * 这是一个功能性接口，作为 EntityEvents 中定义的自定义事件的类型。
 * 它定义了监听 "实体坠落伤害计算" 事件时，回调函数所必须遵循的方法签名。
 * <p>
 * 此事件允许开发者介入并修改实体因坠落而受到的伤害值。
 *
 * <b>使用示例:</b>
 * <pre>{@code
 * EntityEvents.ENTITY_FALL_DAMAGE_CALC.EVENT.register((world, entity, fallDistance, damageMultiplier) -> {
 *     // 检查实体是否是玩家并且拥有特殊物品
 *     if (entity instanceof PlayerEntity player && player.getInventory().contains(new ItemStack(Items.FEATHER))) {
 *         // 如果有羽毛，则完全免疫坠落伤害
 *         return 0;
 *     }
 *     // 否则，执行原版伤害计算
 *     int vanillaDamage = (int)((fallDistance - 3.0F) * damageMultiplier);
 *     return Math.max(0, vanillaDamage);
 * });
 * }</pre>
 */
@FunctionalInterface
public interface Entity_Fall_Damage_Calc { // 在实际代码中，这通常写作内部接口: public static interface Entity_Fall_Damage_Calc

    /**
     * 当计算实体坠落伤害时调用的方法。
     *
     * @param world            事件发生的所在世界 (World)。
     * @param entity           正在坠落的实体 (Entity)。
     * @param fallDistance     实体坠落的距离（格数）。
     * @param damageMultiplier 伤害乘数，通常为1.0，但可能受效果等影响。
     * @return 计算后得到的最终伤害值（整数）。返回 0 可取消伤害。
     */
    int onFallDamageCalc(World world, Entity entity, float fallDistance, float damageMultiplier);
}