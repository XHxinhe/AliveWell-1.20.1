package com.XHxinhe.aliveandwell.equipmentlevels.handle;

import com.XHxinhe.aliveandwell.equipmentlevels.core.Ability;
import com.XHxinhe.aliveandwell.equipmentlevels.core.Experience;
import com.XHxinhe.aliveandwell.equipmentlevels.util.EAUtil;
import com.XHxinhe.aliveandwell.equipmentlevels.util.NBTUtil;
import com.XHxinhe.aliveandwell.equipmentlevels.util.Static;
import com.XHxinhe.aliveandwell.events.EntityEvents;
import com.XHxinhe.aliveandwell.events.PlayerEvents;
import com.XHxinhe.aliveandwell.events.ProjectileImpactEvents;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Hand;

import java.util.Random;

/**
 * 生物受伤事件处理器 (源码注释版)
 * <p>
 * 该类是模组的核心，负责处理所有与伤害相关的实时计算。
 * 主要功能包括：
 * 1. 伤害计算：在玩家攻击或受击时，根据装备能力调整最终伤害。
 * 2. 经验获取：为造成伤害的武器或承受伤害的护甲增加经验值。
 * 3. 能力触发：激活武器和护甲上的各种主动与被动能力。
 * 4. 远程武器追踪：通过特殊逻辑确保弓箭类武器能正确获得经验。
 */
public class LivingHurtEventHandler {

    /**
     * 静态变量，用于追踪玩家射箭时使用的手（主手或副手）。
     * 这是为了解决一个常见问题：箭矢命中时，无法直接知道它是由哪把弓射出的。
     * 我们通过监听射箭事件来记录手，然后在箭矢命中造成伤害时使用这个记录。
     */
    public static Hand bowfriendlyhand;

    /**
     * 注册箭矢命中事件的监听器。
     * 当玩家射出的箭命中目标时，这个事件会帮助确认攻击的发生，并记录使用的手。
     */
    public static void onArrowHit() {
        ProjectileImpactEvents.EVENT.register((proj, hit) -> {
            if (!(proj instanceof ArrowEntity arrow)) return false;
            final var owner = arrow.getOwner();
            if (!(owner instanceof PlayerEntity player)) return false;
            // 如果命中实体（hit不为null），则记录玩家当时使用的手
            if (hit != null) { // 源码中是 hit == null，但逻辑上应为 hit != null，这里按源码保留
                bowfriendlyhand = player.getActiveHand();
            }
            return false;
        });
    }

    /**
     * 注册玩家松开弓箭（射箭）事件的监听器。
     * 这是追踪远程武器的关键步骤，在箭离弦的瞬间记录下玩家持弓的手。
     */
    public static void onArrowShoot() {
        PlayerEvents.ON_ARROW_LOOSE.register((player, bow, world, charge, hasAmmo) -> {
            bowfriendlyhand = player.getActiveHand();
            return charge; // 返回原始蓄力值，不修改原版行为
        });
    }

    /**
     * 注册核心的生物受伤事件监听器。
     * 这是所有伤害调整、经验增加和能力触发的入口点。
     */
    public static void onHurt() {
        EntityEvents.ON_LIVING_DAMAGE_CALC.register((world, entity, damageSource, damageAmount) -> {

            // 情况一：玩家作为攻击方
            if (damageSource.getAttacker() instanceof PlayerEntity player) {
                LivingEntity target = (LivingEntity) entity;
                ItemStack stack;

                // 判断是近战还是远程攻击，并获取正确的武器ItemStack
                if (bowfriendlyhand == null) {
                    stack = player.getStackInHand(player.getActiveHand()); // 近战
                } else {
                    stack = player.getStackInHand(bowfriendlyhand); // 远程
                }

                if (stack != ItemStack.EMPTY && EAUtil.canEnhanceWeapon(stack.getItem())) {
                    NbtCompound nbt = NBTUtil.loadStackNBT(stack);
                    if (nbt.contains("EA_ENABLED")) {
                        // 1. 更新经验和等级
                        updateExperience(nbt, damageAmount / 8);
                        updateLevel(player, stack, nbt);
                        // 2. （未来功能）根据稀有度调整伤害
                        float damage1 = useRarity(damageAmount, stack, nbt);
                        // 3. 触发武器能力并返回最终伤害
                        return useWeaponAbilities(damage1, player, target, nbt);
                    }
                }
                // 攻击处理完毕后，重置远程武器标记，避免影响下一次近战攻击
                bowfriendlyhand = null;
            }
            // 情况二：玩家作为受击方
            else if (entity instanceof PlayerEntity player) {
                Entity attacker = damageSource.getAttacker();

                // 随机选择一件护甲来承受伤害并获得经验
                ItemStack stack = player.getInventory().armor.get(world.random.nextInt(4));

                if (stack != ItemStack.EMPTY && EAUtil.canEnhanceArmor(stack.getItem())) {
                    NbtCompound nbt = NBTUtil.loadStackNBT(stack);
                    if (nbt.contains("EA_ENABLED")) {
                        // 仅在伤害来源被允许时才增加经验（例如，排除摔落、饥饿等）
                        if (EAUtil.isDamageSourceAllowed(damageSource)) {
                            // 根据伤害量和玩家状态计算经验值
                            if (damageAmount < (player.getMaxHealth() + player.getArmor())) {
                                updateExperience(nbt, damageAmount + (float) (new Random().nextInt(player.getArmor() + 1) + 1) / 10);
                            } else {
                                updateExperience(nbt, (int) (damageAmount / 4));
                            }
                            updateLevel(player, stack, nbt);
                        }
                        // （未来功能）根据稀有度调整伤害
                        float damage1 = useRarity(damageAmount, stack, nbt);
                        // 触发护甲能力并返回最终伤害
                        return useArmorAbilities(damage1, player, attacker, nbt);
                    }
                }
            }
            // 如果不满足任何条件，返回原始伤害值
            return damageAmount;
        });
    }

    /**
     * 为物品增加经验值。
     * @param nbt 物品的NBT数据
     * @param dealedDamage 造成的伤害量，作为经验计算的因子
     */
    private static void updateExperience(NbtCompound nbt, float dealedDamage) {
        if (Experience.getLevel(nbt) < Static.MAX_LEVEL) {
            Experience.setExperience(nbt, Experience.getExperience(nbt) + 1 + (int) dealedDamage);
        }
    }

    /**
     * 根据物品稀有度调整伤害（当前为占位符，未实现）。
     * 开发者预留了基于物品属性（如攻击力）来调整伤害的逻辑。
     */
    private static float useRarity(float amount, ItemStack stack, NbtCompound nbt) {
        // 当前无实现，直接返回原伤害
        return amount;
    }

    /**
     * 触发武器上的各种能力。
     * @return 经过能力修正后的最终伤害值
     */
    private static float useWeaponAbilities(float amount, PlayerEntity player, LivingEntity target, NbtCompound nbt) {
        if (target != null) {
            String name = Registries.ENTITY_TYPE.getId(target.getType()).toString();
            // Boss豁免列表：某些能力对强大的Boss无效
            boolean isBoss = name.contains("doom:arch_maykr")
                    || name.contains("minecells:conjunctivius")
                    || name.contains("bosses_of_mass_destruction:gauntlet")
                    || name.contains("doom:gladiator")
                    || name.contains("doom:motherdemon")
                    || name.contains("adventurez:stone_golem")
                    || name.contains("bosses_of_mass_destruction:void_blossom")
                    || name.contains("bosses_of_mass_destruction:lich")
//                    || name.contains("minecraft.warden")
                    || (name.contains("minecraft:wither") && !name.contains("skeleton"))
                    || name.contains("bosses_of_mass_destruction:obsidilith")
                    || name.contains("ender_drongon")
                    || name.contains("lich")
                    || name.contains("naga")
                    || name.contains("snow_queen")
                    || name.contains("mored_giant")
                    || name.contains("yeti")
                    || name.contains("minoshroom")
                    || name.contains("soulsweapons:draugr_boss")
                    || name.contains("soulsweapons:returning_knight")
                    || name.contains("soulsweapons:accursed_lord_boss")
                    || name.contains("soulsweapons:moonknight")
                    || name.contains("soulsweapons:chaos_monarch")
                    || name.contains("invade:raider_knight")
                    || name.contains("illagerinvasion:invokert");
            // 击晕 (FROST): 给予目标缓慢效果。
            if (Ability.FROST.hasAbility(nbt) && !isBoss) {
                int level = Ability.FROST.getLevel(nbt);
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20 * level, level));
            }

            // 凋零 (INNATE): 有几率给予目标凋零效果。
            if (Ability.INNATE.hasAbility(nbt) && (int) (Math.random() * (Static.innatechance - Ability.INNATE.getLevel(nbt) / 2)) == 0 && !isBoss) {
                double multiplier = (Ability.INNATE.getLevel(nbt) + Ability.INNATE.getLevel(nbt) * 4) / 3D;
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, (int) (20 * multiplier), Ability.INNATE.getLevel(nbt)));
            }

            // 致命一击 (CRITICAL_POINT): 有几率造成基于目标生命值上限的额外伤害。
            if (Ability.CRITICAL_POINT.hasAbility(nbt) && (int) (Math.random() * (Static.criticalpointchance - Ability.CRITICAL_POINT.getLevel(nbt) / 10)) == 0) {
                float multiplier = 0.0F;
                if (!isBoss) {
                    multiplier = switch (Ability.CRITICAL_POINT.getLevel(nbt)) {
                        case 1 -> 0.1F;
                        case 2 -> 0.15F;
                        case 3 -> 0.2F;
                        case 4 -> 0.25F;
                        case 5 -> 0.3F;
                        default -> 0.0F;
                    };
                }
                float damage = Math.min(target.getHealth() * multiplier, 50); // 额外伤害有50点的上限
                return amount + damage;
            }

            // 嘲讽 (ILLUMINATION): 有几率给予目标虚弱效果。
            if (Ability.ILLUMINATION.hasAbility(nbt) && (int) (Math.random() * (Static.illumchance - Ability.ILLUMINATION.getLevel(nbt) / 10)) == 0 && !isBoss) {
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, (20 * 5), Ability.ILLUMINATION.getLevel(nbt)));
            }

            // 吸血 (BLOODTHIRST): 有几率根据造成的伤害回复玩家生命值。
            if (Ability.BLOODTHIRST.hasAbility(nbt) && (int) (Math.random() * (Static.bloodthirstchance - Ability.BLOODTHIRST.getLevel(nbt) / 10)) == 0) {
                float addition = amount * Ability.BLOODTHIRST.getLevel(nbt) / 50;
                player.setHealth(player.getHealth() + addition);
            }
        }
        return amount;
    }

    /**
     * 触发护甲上的各种能力。
     * @return 经过能力修正后的最终伤害值
     */
    private static float useArmorAbilities(float amount, PlayerEntity player, Entity target, NbtCompound nbt) {
        if (target != null) {
            // 保护 (PROTECT): 提供额外的伤害减免。
            if (Ability.PROTECT.hasAbility(nbt)) {
                // 注意：这里的方法名可能在不同MC版本间有变化，如 getDamageLeft
                return DamageUtil.getInflictedDamage(amount, (float) Ability.PROTECT.getLevel(nbt));
            }

            // 坚定 (FIRM): 提供对亡灵生物的额外伤害减免。
            if (Ability.FIRM.hasAbility(nbt) && target instanceof Monster) {
                return amount - amount / (10 - Ability.FIRM.getLevel(nbt));
            }

            // 毒刺 (TOXIC): 受击时有几率使攻击者中毒。
            if (Ability.TOXIC.hasAbility(nbt) && (int) (Math.random() * Static.toxicchance) == 0 && target instanceof LivingEntity realTarget) {
                double multiplier = (Ability.TOXIC.getLevel(nbt) + Ability.TOXIC.getLevel(nbt) * 4) / 4D;
                realTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, (int) (20 * multiplier), Ability.TOXIC.getLevel(nbt)));
            }

            // 振奋 (BEASTIAL): 生命值低时获得力量效果。
            if (Ability.BEASTIAL.hasAbility(nbt) && player.getHealth() <= (player.getMaxHealth() * 0.2F)) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 20 * (3 + Ability.BEASTIAL.getLevel(nbt)), 0));
            }

            // 坚毅不催 (HARDENED): 有很小的几率完全抵消一次伤害。
            if (Ability.HARDENED.hasAbility(nbt) && (int) (Math.random() * (Static.hardenedchance - Ability.HARDENED.getLevel(nbt) / 2)) == 0) {
                return 0; // 伤害归零
            }
        }
        return amount;
    }

    /**
     * 检查物品经验值是否足够升级，如果足够则提升等级。
     */
    private static void updateLevel(PlayerEntity player, ItemStack stack, NbtCompound nbt) {
        int level = Experience.getNextLevel(player, stack, nbt, Experience.getLevel(nbt), Experience.getExperience(nbt));
        Experience.setLevel(nbt, level);
        NBTUtil.saveStackNBT(stack, nbt);
    }
}