package com.XHxinhe.aliveandwell.equipmentlevels.core;

import com.XHxinhe.aliveandwell.equipmentlevels.util.EAUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * 经验与升级逻辑处理（混淆名替换版）
 * <p>
 * 这个类包含了所有与装备经验值、等级、能力代币相关的静态方法。
 * 核心功能是 getNextLevel，它处理装备的升级过程。
 */
public class Experience {
    public Experience() {
    }

    /**
     * 计算并处理物品升级。如果经验足够，会提升等级、给予能力代币，并尝试添加/升级一个随机能力。
     *
     * @param player       持有物品的玩家
     * @param stack        正在处理的物品堆
     * @param nbt          物品的NBT数据
     * @param currentLevel 当前等级
     * @param experience   当前经验值
     * @return 返回升级后的新等级
     */
    public static int getNextLevel(PlayerEntity player, ItemStack stack, NbtCompound nbt, int currentLevel, int experience) {
        int newLevel = currentLevel;

        // 当等级低于25且经验值达到升级所需时，循环升级
        while (currentLevel < 25 && experience >= getMaxLevelExp(currentLevel)) {
            newLevel = currentLevel + 1;
            ++currentLevel;
            setAbilityTokens(nbt, getAbilityTokens(nbt) + 1); // 增加一个能力代币

            // 向玩家发送升级消息
            player.sendMessage(Text.translatable("aliveandwell.levelupinfo").formatted(Formatting.DARK_GRAY)
                    .append(Text.literal("【").formatted(Formatting.DARK_GRAY))
                    .append(stack.getName().copy().formatted(Formatting.DARK_GRAY))
                    .append(Text.literal("】").formatted(Formatting.DARK_GRAY))
                    .append(Text.translatable("enhancedarmaments.misc.level.leveledup").formatted(Formatting.DARK_GRAY))
                    .append(Text.literal(" " + Formatting.GREEN + (newLevel - 1))
                            .append(Text.translatable("aliveandwell.levelupinfo1").formatted(Formatting.DARK_GRAY))));

            boolean canLevel = true; // 标记本次升级是否已成功赋予/升级能力

            // 循环直到成功赋予/升级一个能力
            while (canLevel) {
                // 随机选择一个武器能力和一个护甲能力
                Ability ability_weapon = Ability.WEAPON_ABILITIES.get(player.getWorld().random.nextInt(Ability.WEAPON_ABILITIES.size()));
                Ability ability_armor = Ability.ARMOR_ABILITIES.get(player.getWorld().random.nextInt(Ability.ARMOR_ABILITIES.size()));

                // 如果是可强化的武器
                if (EAUtil.canEnhance(stack.getItem()) && EAUtil.canEnhanceWeapon(stack.getItem())) {
                    if (!ability_weapon.hasAbility(nbt)) { // 如果没有这个能力，则添加
                        ability_weapon.addAbility(nbt);
                        player.sendMessage(buildAbilityMessage(stack, ability_weapon, nbt));
                        canLevel = false;
                    } else if (ability_weapon.canUpgradeLevel(nbt)) { // 如果有且未满级，则升级
                        ability_weapon.setLevel(nbt, ability_weapon.getLevel(nbt) + 1);
                        player.sendMessage(buildAbilityMessage(stack, ability_weapon, nbt));
                        canLevel = false;
                    }
                }

                // 如果是可强化的护甲
                if (EAUtil.canEnhance(stack.getItem()) && EAUtil.canEnhanceArmor(stack.getItem())) {
                    if (!ability_armor.hasAbility(nbt)) { // 添加新能力
                        ability_armor.addAbility(nbt);
                        player.sendMessage(buildAbilityMessage(stack, ability_armor, nbt));
                        canLevel = false;
                    } else if (ability_armor.canUpgradeLevel(nbt)) { // 升级旧能力
                        ability_armor.setLevel(nbt, ability_armor.getLevel(nbt) + 1);
                        player.sendMessage(buildAbilityMessage(stack, ability_armor, nbt));
                        canLevel = false;
                    }
                }
            }
        }

        return newLevel;
    }

    // 辅助方法，用于构建获取/升级能力的消息
    private static Text buildAbilityMessage(ItemStack stack, Ability ability, NbtCompound nbt) {
        return Text.translatable("aliveandwell.levelupinfo").formatted(Formatting.AQUA)
                .append(Text.literal("【").formatted(Formatting.GREEN))
                .append(stack.getName().copy().formatted(Formatting.GREEN))
                .append(Text.literal("】").formatted(Formatting.GREEN))
                .append(Text.translatable("enhancedarmaments.misc.ability.get").formatted(Formatting.AQUA))
                .append(Text.literal(ability.getName(nbt)).copy().formatted(Formatting.GREEN))
                .append(Text.literal(" !").formatted(Formatting.DARK_GRAY));
    }

    public static int getLevel(NbtCompound nbt) {
        return nbt != null ? Math.max(nbt.getInt("LEVEL"), 1) : 1;
    }

    public static boolean canLevelUp(NbtCompound nbt) {
        return getLevel(nbt) < 25;
    }

    public static void setLevel(NbtCompound nbt, int level) {
        if (nbt != null) {
            if (level > 1) {
                nbt.putInt("LEVEL", level);
            } else {
                nbt.remove("LEVEL");
            }
        }
    }

    public static int getNeededExpForNextLevel(NbtCompound nbt) {
        return getMaxLevelExp(getLevel(nbt)) - getExperience(nbt);
    }

    public static int getExperience(NbtCompound nbt) {
        return nbt.contains("EXPERIENCE") ? nbt.getInt("EXPERIENCE") : 0;
    }

    public static void setExperience(NbtCompound nbt, int experience) {
        if (nbt != null) {
            if (experience > 0) {
                nbt.putInt("EXPERIENCE", experience);
            } else {
                nbt.remove("EXPERIENCE");
            }
        }
    }

    /**
     * 计算达到指定等级所需的总经验值。
     * 每一级的经验需求是上一级的1.3倍。
     */
    public static int getMaxLevelExp(int level) {
        int maxLevelExp = 100;
        for (int i = 1; i < level; ++i) {
            maxLevelExp = (int) ((double) maxLevelExp * 1.3);
        }
        return maxLevelExp;
    }

    public static void setAbilityTokens(NbtCompound nbt, int tokens) {
        if (nbt != null) {
            if (tokens > 0) {
                nbt.putInt("TOKENS", tokens);
            } else {
                nbt.remove("TOKENS");
            }
        }
    }

    public static int getAbilityTokens(NbtCompound nbt) {
        return nbt != null ? nbt.getInt("TOKENS") : 0;
    }

    public static void enable(NbtCompound nbt, boolean value) {
        if (nbt != null) {
            if (value) {
                nbt.putBoolean("EA_ENABLED", true);
            } else {
                nbt.remove("EA_ENABLED");
            }
        }
    }

    public static boolean isEnabled(NbtCompound nbt) {
        return nbt != null && nbt.getBoolean("EA_ENABLED");
    }
}