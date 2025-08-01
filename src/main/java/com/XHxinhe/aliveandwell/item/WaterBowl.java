package com.XHxinhe.aliveandwell.item; // 声明包名

import net.minecraft.item.Item; // 导入物品基类
import net.minecraft.item.ItemStack; // 导入物品堆类
import net.minecraft.util.UseAction; // 导入使用动作枚举

/**
 * 水碗物品类
 * 让物品在使用时显示“喝水”动画
 */
public class WaterBowl extends Item { // 定义WaterBowl类，继承自Item

    // 构造方法，传入物品设置参数
    public WaterBowl(Settings settings) {
        super(settings); // 调用父类构造方法
    }

    /**
     * 重写getUseAction方法，使物品使用时显示“喝水”动画
     * @param stack 当前物品堆
     * @return UseAction.DRINK，表示喝水动作
     */
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK; // 返回喝水动作
    }
}