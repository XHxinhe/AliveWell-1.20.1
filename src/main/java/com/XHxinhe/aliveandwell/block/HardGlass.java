package com.XHxinhe.aliveandwell.block;

import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior; // 虽然未直接使用，但行为类似
import net.minecraft.sound.BlockSoundGroup;
import java.util.function.ToIntFunction;

/**
 * 硬化玻璃方块 (HardGlass)。
 * <p>
 * 这是一种特殊的玻璃，继承自 AbstractGlassBlock，具有极高的硬度和爆炸抗性，
 * 并且自身会发光。它保留了玻璃的透明特性，但比黑曜石还要坚固。
 */
public class HardGlass extends AbstractGlassBlock {

    /**
     * 构造一个硬化玻璃方块。
     * @param mapColor 在地图上显示的颜色。
     */
    public HardGlass(MapColor mapColor) {
        // 调用父类构造函数，并传入一系列自定义的方块属性
        super(AbstractBlock.Settings.create()
                // 在地图上显示的颜色
                .mapColor(mapColor)
                // 设置音符盒乐器：当放在音符盒下方时，发出“帽子”声效（类似沙子）
                .instrument(Instrument.HAT)
                // 设置需要合适的工具才能有效挖掘并掉落物品
                .requiresTool()
                // 设置硬度和爆炸抗性。硬度50.0F（黑曜石级别），爆炸抗性1200.0F（黑曜石级别）
                // 这使得它极难被破坏，并且能抵抗凋灵和末影龙的攻击。
                .strength(50.0F, 1200.0F)
                // 设置方块的音效组为玻璃
                .sounds(BlockSoundGroup.GLASS)
                // 设置为非不透明方块，光线可以穿过，对于玻璃是必须的
                .nonOpaque()
                // 禁止任何生物在此方块上生成
                .allowsSpawning((state, world, pos, type) -> false) // Blocks::never 的等效写法
                // --- 以下是关于视觉剔除（Culling）和物理交互的设置 ---
                // 使用 Blocks::never (等效于 (state, world, pos) -> false) 来定义行为
                .solidBlock((state, world, pos) -> false)      // 不是一个完整的固体方块
                .suffocates((state, world, pos) -> false)      // 不会使实体窒息
                .blockVision((state, world, pos) -> false)     // 不会阻挡视线
                // 设置方块的发光等级
                .luminance(createLightLevelProvider())
        );
    }

    /**
     * 创建一个提供光照等级的函数。
     * (原名: createLightLevelFromBlockState2)
     * @return 一个函数，它接收 BlockState 并返回一个固定的光照等级。
     */
    private static ToIntFunction<BlockState> createLightLevelProvider() {
        // 无论方块处于何种状态，都返回 10 的光照等级。
        // (光照等级范围是 0-15，10 是一个相当明亮的级别，类似于红石火把)
        return (blockState) -> 10;
    }
}