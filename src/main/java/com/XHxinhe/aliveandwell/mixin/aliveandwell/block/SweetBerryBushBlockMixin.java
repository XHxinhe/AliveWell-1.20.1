package com.XHxinhe.aliveandwell.mixin.aliveandwell.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

/**
 * 这是一个用于 SweetBerryBushBlock (甜浆果丛方块) 的 Mixin。
 * 它的核心功能是彻底重写甜浆果丛的自然生长机制，使其生长速度大幅减慢。
 * 原版游戏中，甜浆果丛在每次接收到随机刻（randomTick）时，都有一定的概率尝试生长。
 * 这个 Mixin 引入了一个计时器，强制甜浆果丛必须累计接收到 60 次随机刻后，才能进行一次生长判定。
 * 这极大地延长了甜浆果丛的生长周期，让其成熟变得更加耗时。
 */
@Mixin(SweetBerryBushBlock.class) // @Mixin 注解，告诉处理器我们要修改原版的 SweetBerryBushBlock 类。
public abstract class SweetBerryBushBlockMixin extends PlantBlock implements Fertilizable {

    // @Unique 注解，表示这是我们为甜浆果丛新增的一个独有字段。
    // 这个 'time' 变量将作为一个计时器，记录随机刻发生的次数。
    @Unique
    public int time;

    // @Final 和 @Shadow 注解，用于“引用”原版类中定义的 AGE 属性。
    // AGE 是一个整数属性，代表甜浆果丛的生长阶段（0-3）。
    @Final
    @Shadow
    public static IntProperty AGE;

    // Mixin 类的标准构造函数。
    public SweetBerryBushBlockMixin(Settings settings) {
        super(settings);
    }

    /**
     * @author [作者名]
     * @reason [重写原因]
     */
    // @Overwrite 注解，表示我们要用下面的方法，完全替换掉 SweetBerryBushBlock 类中原有的 randomTick 方法。
    @Overwrite
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // --- 核心修改逻辑 ---

        // 每次这个方法被调用（即甜浆果丛接收到一个随机刻），计时器 'time' 就加 1。
        time++;

        // 获取当前甜浆果丛的年龄。
        int i = (Integer) state.get(AGE);

        // 检查计时器是否已经累计到 60 (3*20) 或以上。
        if (time >= 3 * 20) {
            // 当累计满 60 次随机刻后，才开始进行生长判定。
            // 检查条件：年龄小于3、有 1/5 的概率、且上方光照等级足够。
            if (i < 3 && random.nextInt(5) == 0 && world.getBaseLightLevel(pos.up(), 0) >= 9) {
                // 如果条件满足，就将年龄加1，让其生长。
                BlockState blockState = (BlockState) state.with(AGE, i + 1);
                world.setBlockState(pos, blockState, 2);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(blockState));
            }
            // 不论生长成功与否，只要进行过一次判定，就将计时器重置为 0，开始下一轮的累计。
            time = 0;
        }
        // 如果 time < 60，这个方法将什么也不做，直接结束，等待下一次随机刻。
    }
}
