package com.XHxinhe.aliveandwell.events;

import net.minecraft.entity.Entity;                   // 替换混淆名 class_1297
import net.minecraft.entity.damage.DamageSource;      // 替换混淆名 class_1282
import net.minecraft.world.World;                     // 替换混淆名 class_1937

/**
 * 实体掉落战利品事件的回调接口 (混淆名替换版)
 * <p>
 * 这是一个功能性接口，作为 EntityEvents 中定义的自定义事件的类型。
 * 它定义了监听 "实体掉落战利品" 事件时，回调函数所必须遵循的方法签名。
 * <p>
 * 此事件在实体死亡、即将掉落物品时触发，允许开发者介入并添加额外的逻辑。
 *
 * <b>使用示例:</b>
 * <pre>{@code
 * EntityEvents.ENTITY_IS_DROPPING_LOOT.EVENT.register((world, entity, source) -> {
 *     // 检查是否是僵尸被玩家杀死
 *     if (entity instanceof ZombieEntity && source.getAttacker() instanceof PlayerEntity) {
 *         // 有 10% 的几率额外掉落一个钻石
 *         if (world.random.nextFloat() < 0.1f) {
 *             ItemEntity diamondDrop = new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(Items.DIAMOND));
 *             world.spawnEntity(diamondDrop);
 *         }
 *     }
 * });
 * }</pre>
 */
@FunctionalInterface
public interface Entity_Is_Dropping_Loot { // 在实际代码中，这通常写作内部接口: public static interface Entity_Is_Dropping_Loot

    /**
     * 当实体即将掉落战利品时调用的方法。
     *
     * @param world  事件发生的所在世界 (World)。
     * @param entity 即将掉落战利品的实体 (Entity)。
     * @param source 导致该实体死亡的伤害来源 (DamageSource)，可用于判断击杀者。
     */
    void onDroppingLoot(World world, Entity entity, DamageSource source);
}