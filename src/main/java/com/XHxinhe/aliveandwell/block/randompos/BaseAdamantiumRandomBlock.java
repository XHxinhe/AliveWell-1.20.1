package com.XHxinhe.aliveandwell.block.randompos;

import com.XHxinhe.aliveandwell.block.portal.RandomBPortalBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;

/**
 * 艾德曼合金随机方块的基础类 (Base Adamantium Random Block)。
 * <p>
 * 这个类本身没有添加新的功能，但它作为一个共同的父类存在。
 * 这使得在其他代码（例如 {@link RandomBPortalBlock} 的传送门逻辑）中，
 * 可以通过 {@code instanceof BaseAdamantiumRandomBlock} 来方便地检查一个方块是否属于这一系列的特殊方块。
 */
public class BaseAdamantiumRandomBlock extends Block {

    /**
     * 构造一个基础的艾德曼合金随机方块。
     * @param settings 方块的属性设置
     */
    public BaseAdamantiumRandomBlock(AbstractBlock.Settings settings) {
        super(settings);
    }
}