package com.XHxinhe.aliveandwell.dig;

import net.minecraft.block.Block;

/**
 * 射线检测碰撞结果（混淆名替换版）
 * <p>
 * 这是一个简单的数据结构，用于存储射线检测与方块发生碰撞时的信息，
 * 包括碰撞方块的坐标和方块类型。
 */
public class RaycastCollision {
    public int block_hit_x;
    public int block_hit_y;
    public int block_hit_z;
    public Block blockHit;

    public RaycastCollision() {
    }

    /**
     * 检查碰撞是否是方块。
     * 在这个实现中，它总是返回true，意味着这个类的实例只在与方块碰撞时创建。
     */
    public boolean isBlock() {
        return true;
    }

    /**
     * 获取被碰撞的方块类型。
     */
    public Block getBlockHit() {
        return this.blockHit;
    }
}