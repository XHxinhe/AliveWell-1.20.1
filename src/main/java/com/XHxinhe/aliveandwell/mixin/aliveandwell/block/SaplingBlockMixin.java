package com.XHxinhe.aliveandwell.mixin.aliveandwell.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

/**
 * 这是一个用于 SaplingBlock (树苗方块) 的 Mixin。
 * 它的核心功能是彻底改变树苗的自然生长机制。
 * 原版游戏中，树苗在每次接收到随机刻（randomTick）时，都有一个较小的概率尝试生长。
 * 这个 Mixin 修改了这一行为，引入了一个计时器，使得树苗必须累计接收到5次随机刻后，才会进行一次生长判定。
 * 这会显著减慢树苗的平均生长速度，让植树造林的过程变得更漫长。
 */
@Mixin(SaplingBlock.class) // @Mixin 注解，告诉处理器我们要修改原版的 SaplingBlock 类。
public abstract class SaplingBlockMixin extends PlantBlock implements Fertilizable {

    // @Unique 注解，表示这是我们为 SaplingBlock 新增的一个独有字段。
    // 这个 'time' 变量将作为一个计时器，记录随机刻发生的次数。
    @Unique
    private int time;

    // Mixin 类的标准构造函数。
    public SaplingBlockMixin(Settings settings) {
        super(settings);
    }

    /**
     * @author
     * @reason
     */
    // @Overwrite 注解，表示我们要用下面的方法，完全替换掉 SaplingBlock 类中原有的 randomTick 方法。
    // randomTick 是游戏用来让方块随时间发生随机变化（如植物生长）的机制。
    @Overwrite
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // --- 核心逻辑 ---

        // 每次这个方法被调用（即树苗接收到一个随机刻），计时器 'time' 就加 1。
        time++;

        // 检查计时器是否已经累计到 5 或以上。
        if(time >= 5){
            // 当累计满 5 次随机刻后，才开始进行生长判定。
            // 检查树苗上方的光照等级是否大于等于9，并且有 1/4 (25%) 的概率成功。
            if (world.getLightLevel(pos.up()) >= 9 && random.nextInt(4) == 0) {
                // 如果条件满足，就调用原版的 generate 方法，让树苗长成一棵树。
                this.generate(world, pos, state, random);
            }
            // 不论生长成功与否，只要进行过一次判定，就将计时器重置为 0，开始下一轮的累计。
            time=0;
        }
        // 如果 time < 5，这个方法将什么也不做，直接结束，等待下一次随机刻。
    }

    // @Shadow 注解，让我们能直接“引用”并调用原版类中的 generate 方法。
    // 我们不需要知道这个方法的具体实现，Mixin 会帮我们链接到原版的功能。
    // 这个方法负责将树苗方块替换为完整的树木结构（树干和树叶）。
    @Shadow
    public void generate(ServerWorld world, BlockPos pos, BlockState state, Random random) {
    }
}