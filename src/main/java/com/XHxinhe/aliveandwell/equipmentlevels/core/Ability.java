package com.XHxinhe.aliveandwell.equipmentlevels.core;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;

/**
 * 装备能力枚举（混淆名替换版）
 * <p>
 * 定义了所有可应用于武器和护甲的特殊能力。
 * 每个能力都有类别、类型、等级、颜色等属性，并包含处理NBT数据和玩家经验等级的逻辑。
 */
public enum Ability {
    // 定义各种能力及其属性
    FROST("weapon", "passive", true, Formatting.AQUA, 5636095, 1, 5),
    INNATE("weapon", "passive", true, Formatting.GOLD, 11141120, 2, 5),
    CRITICAL_POINT("weapon", "passive", true, Formatting.RED, 5592405, 3, 5),
    ILLUMINATION("weapon", "passive", true, Formatting.YELLOW, 16777045, 2, 5),
    BLOODTHIRST("weapon", "passive", true, Formatting.DARK_RED, 11141290, 3, 5),
    PROTECT("armor", "active", true, Formatting.BLUE, 5635925, 2, 5),
    FIRM("armor", "active", true, Formatting.LIGHT_PURPLE, 16733695, 2, 5),
    TOXIC("armor", "active", true, Formatting.GREEN, 43520, 2, 5),
    BEASTIAL("armor", "passive", true, Formatting.GOLD, 11141120, 2, 5),
    HARDENED("armor", "passive", true, Formatting.DARK_GRAY, 11184810, 3, 5);

    // 静态列表和计数器，用于分类和管理能力
    public static final ArrayList<Ability> WEAPON_ABILITIES = new ArrayList<>();
    public static final ArrayList<Ability> ARMOR_ABILITIES = new ArrayList<>();
    public static final ArrayList<Ability> ALL_ABILITIES = new ArrayList<>();
    public static int WEAPON_ABILITIES_COUNT = 0;
    public static int ARMOR_ABILITIES_COUNT = 0;

    // 能力的私有属性
    private final String category; // "weapon" 或 "armor"
    private final String type;     // "passive" 或 "active"
    private final boolean enabled; // 是否启用
    private final String color;    // 格式化颜色代码字符串
    private final int hex;         // 颜色的十六进制值
    private final int tier;        // 等阶
    private final int maxlevel;    // 最高等级

    /**
     * 构造函数
     */
    Ability(String category, String type, boolean enabled, Formatting color, int hex, int tier, int maxlevel) {
        this.category = category;
        this.type = type;
        this.enabled = enabled;
        this.color = color.toString(); // 将Formatting枚举转为字符串
        this.hex = hex;
        this.tier = tier;
        this.maxlevel = maxlevel;
    }

    /**
     * 检查给定的NBT是否包含此能力。
     */
    public boolean hasAbility(NbtCompound nbt) {
        return nbt != null && nbt.getInt(this.toString()) > 0;
    }

    /**
     * 向NBT添加此能力，并增加总能力计数。
     */
    public void addAbility(NbtCompound nbt) {
        nbt.putInt(this.toString(), 1); // 添加能力，初始等级为1
        if (nbt.contains("ABILITIES")) {
            nbt.putInt("ABILITIES", nbt.getInt("ABILITIES") + 1);
        } else {
            nbt.putInt("ABILITIES", 1);
        }
    }

    /**
     * 从NBT移除此能力，并减少总能力计数。
     */
    public void removeAbility(NbtCompound nbt) {
        nbt.remove(this.toString());
        if (nbt.contains("ABILITIES") && nbt.getInt("ABILITIES") > 0) {
            nbt.putInt("ABILITIES", nbt.getInt("ABILITIES") - 1);
        }
    }

    /**
     * 检查玩家是否有足够的经验等级来添加或升级此能力。
     */
    public boolean hasEnoughExp(PlayerEntity player, NbtCompound nbt) {
        return this.getExpLevel(nbt) <= player.experienceLevel || player.isCreative();
    }

    /**
     * 计算添加或升级此能力所需的经验等级。
     */
    public int getExpLevel(NbtCompound nbt) {
        int requiredExpLevel;
        if (nbt.contains("ABILITIES")) {
            // 所需等级 = (阶级 + 最高等级) * (已有能力数 + 1) - 1
            requiredExpLevel = (this.getTier() + this.getMaxLevel()) * (nbt.getInt("ABILITIES") + 1) - 1;
        } else {
            requiredExpLevel = this.getTier() + this.getMaxLevel();
        }
        return requiredExpLevel;
    }

    /**
     * 设置此能力在NBT中的等级。
     */
    public void setLevel(NbtCompound nbt, int level) {
        nbt.putInt(this.toString(), level);
    }

    /**
     * 获取此能力在NBT中的等级。
     */
    public int getLevel(NbtCompound nbt) {
        return nbt != null ? nbt.getInt(this.toString()) : 0;
    }

    /**
     * 检查此能力是否可以升级。
     */
    public boolean canUpgradeLevel(NbtCompound nbt) {
        return this.getLevel(nbt) < this.maxlevel;
    }

    public int getTier() {
        return this.tier;
    }

    public int getMaxLevel() {
        return this.maxlevel;
    }

    public String getColor() {
        return this.color;
    }

    public int getHex() {
        return this.hex;
    }

    public String getName() {
        return this.toString();
    }

    /**
     * 获取能力的显示名称，包含罗马数字等级。
     */
    public String getName(NbtCompound nbt) {
        String baseName = Text.translatable("enhancedarmaments.ability." + this.toString()).getString();
        int level = this.getLevel(nbt);
        switch (level) {
            case 2: return baseName + " Ⅱ";
            case 3: return baseName + " Ⅲ";
            case 4: return baseName + " Ⅳ";
            case 5: return baseName + " Ⅴ";
            default: return baseName + " Ⅰ";
        }
    }

    public String getType() {
        return this.type;
    }

    /**
     * 获取能力类型的可翻译名称（例如 "被动"）。
     */
    public String getTypeName() {
        return Text.translatable("enhancedarmaments.ability.type." + this.type).getString();
    }



    public String getCategory() {
        return this.category;
    }

    // 静态初始化块，在类加载时执行
    static {
        // 遍历所有枚举实例，将它们分类到不同的列表中
        for (Ability ability : values()) {
            ALL_ABILITIES.add(ability);
            if (ability.getCategory().equals("weapon") && ability.enabled) {
                WEAPON_ABILITIES.add(ability);
                WEAPON_ABILITIES_COUNT++;
            } else if (ability.getCategory().equals("armor") && ability.enabled) {
                ARMOR_ABILITIES.add(ability);
                ARMOR_ABILITIES_COUNT++;
            }
        }
    }
}