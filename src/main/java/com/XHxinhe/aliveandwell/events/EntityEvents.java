package com.XHxinhe.aliveandwell.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

/**
 * 模组的自定义实体事件中心。
 * <p>
 * 这个类使用 Fabric API 的事件系统来定义和管理一系列与实体相关的自定义事件。
 * 其他代码可以通过此类中定义的静态 Event 字段来注册监听器，从而在特定游戏逻辑点执行自定义代码。
 * <p>
 * 每个事件的创建都伴随着一个 "invoker" lambda，它定义了当事件被触发时，
 * 如何遍历所有注册的回调（listeners）并处理它们的返回值。
 */
public final class EntityEvents { // 'final' is a good practice for utility classes with private constructors

    // --- Event Definitions ---

    /**
     * 在每个生物的 tick() 方法中触发。
     * Invoker 逻辑: 遍历所有监听器并依次调用。这是一个通知性事件。
     */
    public static final Event<Living_Tick> LIVING_TICK = EventFactory.createArrayBacked(Living_Tick.class,
            (callbacks) -> (world, entity) -> {
                for (Living_Tick callback : callbacks) {
                    callback.onTick(world, entity);
                }
            });

    /**
     * 在生物死亡时触发。
     * Invoker 逻辑: 遍历所有监听器并依次调用。这是一个通知性事件。
     */
    public static final Event<Living_Entity_Death> LIVING_ENTITY_DEATH = EventFactory.createArrayBacked(Living_Entity_Death.class,
            (callbacks) -> (world, entity, source) -> {
                for (Living_Entity_Death callback : callbacks) {
                    callback.onDeath(world, entity, source);
                }
            });

    /**
     * 在计算生物受到的最终伤害时触发。
     * Invoker 逻辑: 遍历监听器，只要有一个监听器返回了与原始伤害不同的值，就立即返回该新值。
     * 如果所有监听器都未修改伤害，则返回一个特殊值 (-1.0F) 表示未作修改。
     * 这是一个可修改的事件。
     */
    public static final Event<Entity_Living_Damage> ON_LIVING_DAMAGE_CALC = EventFactory.createArrayBacked(Entity_Living_Damage.class,
            (callbacks) -> (world, entity, damageSource, damageAmount) -> {
                for (Entity_Living_Damage callback : callbacks) {
                    float newDamage = callback.onLivingDamageCalc(world, entity, damageSource, damageAmount);
                    // 如果伤害被修改，则立即中断并返回新值
                    if (newDamage != damageAmount) {
                        return newDamage;
                    }
                }
                // -1.0F 是一个哨兵值，表示没有监听器修改伤害
                return -1.0F;
            });

    /**
     * 在生物即将攻击另一个实体时触发。
     * Invoker 逻辑: 遍历监听器，只要有一个监听器返回 false (允许攻击)，就立即返回 false。
     * 只有当所有监听器都返回 true (尝试取消攻击) 时，最终才会返回 true (攻击被取消)。
     * 注意：这个逻辑似乎是反的。通常是 "只要有一个返回true就取消"。原代码逻辑是 "所有人都同意才能取消"。
     * 这是一个可取消的事件。
     */
    public static final Event<Entity_Living_Attack> ON_LIVING_ATTACK = EventFactory.createArrayBacked(Entity_Living_Attack.class,
            (callbacks) -> (world, entity, damageSource, damageAmount) -> {
                for (Entity_Living_Attack callback : callbacks) {
                    // 如果回调返回 false (允许攻击)，则整个事件链的结果就是 false (允许)。
                    if (!callback.onLivingAttack(world, entity, damageSource, damageAmount)) {
                        return false;
                    }
                }
                // 只有所有回调都返回 true (请求取消)，最终结果才是 true (取消)。
                return true;
            });

    /**
     * 在实体即将在服务器世界生成时触发。
     * Invoker 逻辑: 与 ON_LIVING_ATTACK 类似，只要有一个监听器返回 false (允许生成)，就立即返回 false。
     * 只有所有监听器都同意取消 (返回 true)，生成才会被取消。
     * 这是一个可取消的事件。
     */
    public static final Event<Entity_Spawn> ON_ENTITY_SPAWN = EventFactory.createArrayBacked(Entity_Spawn.class, // Corrected typo from onEnitySpawn
            (callbacks) -> (world, entity) -> {
                for (Entity_Spawn callback : callbacks) {
                    if (!callback.onEntitySpawn(world, entity)) { // Corrected typo
                        return false;
                    }
                }
                return true;
            });

    /**
     * 在实体加入世界之前触发。
     * Invoker 逻辑: 与 ON_ENTITY_SPAWN 相同。
     * 这是一个可取消的事件。
     */
    public static final Event<Pre_Entity_Join_World> PRE_ENTITY_JOIN_WORLD = EventFactory.createArrayBacked(Pre_Entity_Join_World.class,
            (callbacks) -> (world, entity) -> {
                for (Pre_Entity_Join_World callback : callbacks) {
                    if (!callback.onPreSpawn(world, entity)) {
                        return false;
                    }
                }
                return true;
            });

    /**
     * 在计算实体坠落伤害时触发。
     * Invoker 逻辑: 遍历监听器，只要有一个返回 0 (取消伤害)，就立即返回 0。
     * 如果所有监听器都返回非零值，则返回 1 (表示伤害应按原逻辑计算)。
     * 这是一个可修改/可取消的事件。
     */
    public static final Event<Entity_Fall_Damage_Calc> ON_FALL_DAMAGE_CALC = EventFactory.createArrayBacked(Entity_Fall_Damage_Calc.class,
            (callbacks) -> (world, entity, fallDistance, damageMultiplier) -> {
                for (Entity_Fall_Damage_Calc callback : callbacks) {
                    if (callback.onFallDamageCalc(world, entity, fallDistance, damageMultiplier) == 0) {
                        return 0; // 只要有一个回调取消伤害，就立即取消
                    }
                }
                return 1; // 1 是一个哨兵值，表示伤害继续计算
            });

    /**
     * 在实体即将掉落战利品时触发。
     * Invoker 逻辑: 遍历所有监听器并依次调用。这是一个通知性事件。
     */
    public static final Event<Entity_Is_Dropping_Loot> ON_ENTITY_IS_DROPPING_LOOT = EventFactory.createArrayBacked(Entity_Is_Dropping_Loot.class,
            (callbacks) -> (world, entity, damageSource) -> {
                for (Entity_Is_Dropping_Loot callback : callbacks) {
                    callback.onDroppingLoot(world, entity, damageSource);
                }
            });

    /**
     * 在生物跳跃时触发。
     * Invoker 逻辑: 遍历所有监听器并依次调用。这是一个通知性事件。
     */
    public static final Event<Entity_Is_Jumping> ON_ENTITY_IS_JUMPING = EventFactory.createArrayBacked(Entity_Is_Jumping.class,
            (callbacks) -> (world, entity) -> {
                for (Entity_Is_Jumping callback : callbacks) {
                    callback.onLivingJump(world, entity);
                }
            });

    /**
     * 在实体攻击距离相关逻辑点触发。
     * Invoker 逻辑: 遍历所有监听器并依次调用。这是一个通知性事件。
     */
    public static final Event<ENTITY_ATTACK_DISTANCE> ON_ENTITY_ATTACK_DISTANCE = EventFactory.createArrayBacked(ENTITY_ATTACK_DISTANCE.class,
            (callbacks) -> (world, entity) -> {
                for (ENTITY_ATTACK_DISTANCE callback : callbacks) {
                    callback.onEntityAttackDistance(world, entity);
                }
            });

    /**
     * 私有构造函数，防止该工具类被实例化。
     */
    private EntityEvents() {
    }

    // --- Callback Interface Definitions ---

    @FunctionalInterface
    public interface Living_Tick {
        void onTick(World world, Entity entity);
    }

    @FunctionalInterface
    public interface Living_Entity_Death {
        void onDeath(World world, Entity entity, DamageSource source);
    }

    @FunctionalInterface
    public interface Entity_Living_Damage {
        float onLivingDamageCalc(World world, Entity entity, DamageSource source, float amount);
    }

    @FunctionalInterface
    public interface Entity_Living_Attack {
        boolean onLivingAttack(World world, Entity target, DamageSource source, float amount);
    }

    @FunctionalInterface
    public interface Entity_Spawn {
        boolean onEntitySpawn(ServerWorld serverWorld, Entity entity); // Corrected typo
    }

    @FunctionalInterface
    public interface Pre_Entity_Join_World {
        boolean onPreSpawn(World world, Entity entity);
    }

    @FunctionalInterface
    public interface Entity_Fall_Damage_Calc {
        int onFallDamageCalc(World world, Entity entity, float fallDistance, float damageMultiplier);
    }

    @FunctionalInterface
    public interface Entity_Is_Dropping_Loot {
        void onDroppingLoot(World world, Entity entity, DamageSource source);
    }

    @FunctionalInterface
    public interface Entity_Is_Jumping {
        void onLivingJump(World world, Entity entity);
    }

    @FunctionalInterface
    public interface ENTITY_ATTACK_DISTANCE {
        void onEntityAttackDistance(World world, Entity entity);
    }
}