package com.XHxinhe.aliveandwell.equipmentlevels.util;

/**
 * 全局静态常量类
 * <p>
 * 这个类不执行任何操作，仅作为整个模组的配置中心。
 * 它定义了所有核心的、全局共享的常量值，如模组ID、等级上限、经验值计算公式参数以及各种能力的触发几率。
 * 将这些值集中在此处，便于统一管理和调整游戏平衡。
 */
public class Static {

    /**
     * 模组的唯一标识符 (Mod ID)。
     * 用于注册物品、方块、网络频道等。
     */
    public static final String MY_ID = "equipmentlevels";

    /**
     * 装备可以达到的最高等级。
     */
    public static final int MAX_LEVEL = 25;

    /**
     * 升到1级所需的基础经验值。
     * 这是经验值计算的起点。
     */
    public static final int level1Experience = 100;

    /**
     * 经验值乘数。
     * 用于计算下一级所需经验，公式通常为：下一级经验 = 当前级经验 * experienceMultiplier。
     * 1.3 表示每升一级，所需经验会增加30%。
     */
    public static final double experienceMultiplier = 1.3;

    /**
     * "Innate" (天赋/本能) 能力的触发几率 (百分比)。
     */
    public static final double innatechance = 3.0;

    /**
     * "Critical Point" (暴击点) 能力的触发几率 (百分比)。
     */
    public static final double criticalpointchance = 3.0;

    /**
     * "Illuminated" (光照/启迪) 能力的触发几率 (百分比)。
     */
    public static final double illumchance = 3.0;

    /**
     * "Bloodthirst" (血渴) 能力的触发几率 (百分比)。
     */
    public static final double bloodthirstchance = 3.5;

    /**
     * "Toxic" (剧毒) 能力的触发几率 (百分比)。
     */
    public static final double toxicchance = 3.0;

    /**
     * "Hardened" (硬化) 能力的触发几率 (百分比)。
     */
    public static final double hardenedchance = 3.0;

    /**
     * 私有构造函数，防止该工具类被实例化。
     * (原代码为 public，但 private 更符合工具类的设计模式)。
     */
    private Static() {
        // This class is not meant to be instantiated.
    }
}