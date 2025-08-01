package com.XHxinhe.aliveandwell.dig;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.util.math.Vec3d;

/**
 * 一个接口，定义了拥有高级“观察与挖掘”AI的生物实体所需的功能。
 * <p>
 * 实现此接口的实体将能够智能地决定何时、何地以及如何破坏方块，
 * 可能是为了攻击目标、开辟路径或与环境进行复杂的交互。
 * 这通常通过Mixin附加到原版或自定义实体上。
 */
public interface AnimalWatcherEntity {

    // --- 状态与条件检查 ---

    /**
     * 检查实体是否手持会阻止其进行挖掘的物品（例如，食物）。
     */
    boolean isHoldingItemThatPreventsDigging();

    /**
     * 检查该实体的挖掘功能是否已启用。一个总开关。
     */
    boolean isDiggingEnabled();

    /**
     * 获取实体最近被攻击的时间（ticks）。可用于中断挖掘行为。
     */
    int recentlyHit();

    /**
     * 检查实体是否处于“狂暴”状态，这可能会改变其挖掘行为。
     */
    boolean isFrenzied();

    /**
     * 检查实体是否能看到其目标。
     * @param canSeeThroughBlocks 是否允许“透视”检查。
     */
    boolean canSeeTarget(boolean canSeeThroughBlocks);

    /**
     * 检查实体是否手持对于特定方块有效的工具。
     * @param block 目标方块。
     */
    boolean isHoldingAnEffectiveTool(Block block);

    // --- 目标方块管理 ---

    /**
     * 获取当前正在破坏的方块的X坐标。
     */
    int getDestroyBlockX();

    /**
     * 获取当前正在破坏的方块的Y坐标。
     */
    int getDestroyBlockY();

    /**
     * 获取当前正在破坏的方块的Z坐标。
     */
    int getDestroyBlockZ();

    /**
     * 检查实体是否可以破坏指定坐标的方块。
     * @param x 方块X坐标。
     * @param y 方块Y坐标。
     * @param z 方块Z坐标。
     * @param isPlayerInitiated 行为是否由玩家触发（可能影响规则）。
     */
    boolean canDestroyBlock(int x, int y, int z, boolean isPlayerInitiated);

    /**
     * 检查破坏指定坐标的方块是否会导致重力方块（沙子、沙砾）下落。
     */
    boolean blockWillFall(int x, int y, int z);

    /**
     * 设置要挖掘的目标方块。
     * @return 如果设置成功，返回true。
     */
    boolean setBlockToDig(int x, int y, int z, boolean isPlayerInitiated);

    // --- 挖掘过程控制 ---

    /**
     * 检查实体当前是否正在破坏方块。
     */
    boolean isDestroyingBlock();

    /**
     * 设置实体是否正在破坏方块的状态。
     * @param destroying true表示开始破坏，false表示停止。
     */
    void setDestroyingBlock(boolean destroying);

    /**
     * 执行一次方块破坏进度的推进（例如，在每个tick调用）。
     */
    void partiallyDestroyBlock();

    /**
     * 取消当前的方块破坏过程。
     */
    void cancelBlockDestruction();

    /**
     * 获取当前方块的破坏进度（通常为0-9）。
     */
    int getDestroyBlockProgress();

    // --- 攻击与寻路相关 ---

    /**
     * 检查实体与目标之间是否有直线攻击路径，并且目标在攻击距离内。
     * @param target 目标生物实体。
     */
    boolean hasLineOfStrikeAndTargetIsWithinStrikingDistance(LivingEntity target);

    /**
     * 获取用于破坏方块时实体的眼睛位置。
     */
    Vec3d getEyePosForBlockDestroying();

    /**
     * 获取用于破坏方块时，目标实体的中心位置。
     * @param target 目标生物实体。
     */
    Vec3d getTargetEntityCenterPosForBlockDestroying(LivingEntity target);

    /**
     * 获取用于破坏方块时，攻击者（自身）的腿部位置。
     */
    Vec3d getAttackerLegPosForBlockDestroying();

    // --- 计时器与冷却 ---

    /**
     * 获取两次破坏方块之间的冷却时间。
     */
    int getDestroyBlockCooloff();

    /**
     * 设置破坏方块后的冷却时间。
     * @param cooloff 冷却时间（ticks）。
     */
    void setDestroyBlockCooloff(int cooloff);

    /**
     * 将破坏方块的冷却时间减少1 tick。
     */
    void decrementDestroyBlockCooloff();

    /**
     * 获取特定方块所需的冷却时间。
     */
    int getCooloffForBlock();

    /**
     * 获取破坏过程中的暂停时间。
     */
    int getDestroyPauseTicks();

    /**
     * 将破坏过程中的暂停时间减少1 tick。
     */
    void decrementDestroyPauseTicks();

    // --- AI与实体内部访问 ---

    /**
     * 获取实体的AI目标选择器，用于动态添加或移除AI任务。
     */
    GoalSelector getGoalSelector();

    /**
     * 获取带有偏移量的实体存在时间，常用于同步AI或动画。
     */
    int getTicksExistedWithOffset();
}