package com.XHxinhe.aliveandwell.crafttime.util;

import net.minecraft.entity.player.PlayerEntity;

/**
 * 一个工具类，用于根据玩家的属性计算其合成速度。
 * 这个速度值是一个乘数，用于缩短合成所需的时间。
 */
public final class CraftingSpeedHelper {

	/**
	 * 每一级经验提供的速度加成值 (0.02 = 2%)。
	 * 使用常量可以提高代码的可读性和可维护性。
	 */
	private static final float SPEED_BONUS_PER_LEVEL = 0.02F;

	/**
	 * 计算速度加成的最大有效等级。
	 * 这可以防止高等级玩家的合成速度过快，影响游戏平衡。
	 */
	private static final int MAX_EFFECTIVE_LEVEL = 200;

	/**
	 * 私有构造函数，防止该工具类被外部实例化。
	 */
	private CraftingSpeedHelper() {
	}

	/**
	 * 获取指定玩家的当前合成速度乘数。
	 * 速度 = 基础速度(1.0) + 经验等级加成。
	 *
	 * @param player 要计算速度的玩家实体。
	 * @return 代表合成速度的浮点数乘数。
	 */
	public static float getCraftingSpeed(PlayerEntity player) {
		// 所有玩家的基础速度为 1.0 (100%)。
		float baseSpeed = 1.0F;

		// 获取玩家的经验等级，并将其限制在最大有效等级之内。
		int effectiveLevel = Math.min(MAX_EFFECTIVE_LEVEL, player.experienceLevel);

		// 计算由经验等级带来的速度加成。
		float levelBonus = SPEED_BONUS_PER_LEVEL * (float)effectiveLevel;

		// 返回最终速度。
		return baseSpeed + levelBonus;
	}
}