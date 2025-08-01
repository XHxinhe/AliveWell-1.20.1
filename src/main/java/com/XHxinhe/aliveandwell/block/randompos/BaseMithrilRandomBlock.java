package com.XHxinhe.aliveandwell.block.randompos;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;

/**
 * 秘银随机方块的基础类 (Base Mithril Random Block)。
 * <p>
 * 与 {@link BaseAdamantiumRandomBlock} 类似，这个类本身不添加任何新功能，
 * 而是作为所有“秘银随机方块”的共同父类。
 * 这使得在其他代码中（例如在自定义传送门或其他多方块结构的逻辑中），
 * 可以通过 {@code instanceof BaseMithrilRandomBlock} 来方便地识别和处理这一类方块。
 */
public class BaseMithrilRandomBlock extends Block {

    /**
     * 构造一个基础的秘银随机方块。
     * @param settings 方块的属性设置
     */
    public BaseMithrilRandomBlock(AbstractBlock.Settings settings) {
        super(settings);
    }
}