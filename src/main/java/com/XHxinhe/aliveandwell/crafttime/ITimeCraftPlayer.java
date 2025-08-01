package com.XHxinhe.aliveandwell.crafttime;

import net.minecraft.item.ItemStack;

/**
 * 一个接口，定义了“能够进行计时合成的玩家”所需具备的功能和数据。
 * <p>
 * 这通常通过 Mixin 应用于 PlayerEntity 类，为其附加额外的状态和行为，
 * 从而实现计时合成的功能，而无需修改原版玩家类。
 */
public interface ITimeCraftPlayer {

	/**
	 * 设置玩家是否正在进行合成。
	 *
	 * @param isCrafting true 表示开始或正在合成，false 表示没有在合成。
	 */
	void setCrafting(boolean isCrafting);

	/**
	 * 检查玩家当前是否正在进行合成。
	 *
	 * @return 如果正在合成，则返回 true。
	 */
	boolean isCrafting();

	/**
	 * 设置当前的合成进度时间。
	 *
	 * @param time 当前的合成进度，通常是一个从0开始累加的计时器。
	 */
	void setCraftTime(float time);

	/**
	 * 获取当前的合成进度时间。
	 *
	 * @return 当前的合成进度。
	 */
	float getCraftTime();

	/**
	 * 设置完成本次合成所需的总时间。
	 *
	 * @param period 总时长。这个值通常由合成配方的难度决定。
	 */
	void setCraftPeriod(float period);

	/**
	 * 获取完成本次合成所需的总时间。
	 *
	 * @return 总时长。
	 */
	float getCraftPeriod();

	/**
	 * 立即停止当前的合成过程。
	 * 通常会重置合成状态和进度。
	 */
	void stopCraft();

	/**
	 * 开始一个新的合成过程，并设定其总时长。
	 *
	 * @param newPeriod 新合成所需的总时长。
	 */
	void startCraftWithNewPeriod(float newPeriod);

	/**
	 * 在每个游戏刻（tick）执行的更新逻辑。
	 * <p>
	 * 这个方法负责推进合成进度、检查完成条件等。
	 *
	 * @param resultStack 合成结果槽中的物品堆栈，用于检查合成是否仍然有效。
	 * @return 如果合成在当前 tick 完成，则返回 true，否则返回 false。
	 */
	boolean tick(ItemStack resultStack);
}