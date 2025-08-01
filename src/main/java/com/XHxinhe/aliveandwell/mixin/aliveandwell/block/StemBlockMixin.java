package com.XHxinhe.aliveandwell.mixin.aliveandwell.block;

import net.minecraft.block.Fertilizable;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.StemBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * 这是一个用于 StemBlock (瓜/南瓜茎方块) 的 Mixin。
 * 它的核心功能是通过一种非常精巧的方式来修改瓜茎的生长速度。
 * 它没有重写整个生长方法，而是像外科手术一样，精确地找到了原版生长概率计算公式中的一个常量值，并将其替换为了一个更大的值。
 * 这样做的结果是显著降低了瓜/南瓜茎在每次接收到随机刻（randomTick）时成功生长的概率，从而延长了它们的成熟时间。
 */
@Mixin(StemBlock.class) // @Mixin 注解，告诉处理器我们要修改原版的 StemBlock 类。
public abstract class StemBlockMixin extends PlantBlock implements Fertilizable {

    // Mixin 类的标准构造函数。
    public StemBlockMixin(Settings settings) {
        super(settings);
    }

    // @ModifyConstant 注解，这是一种高级的注入方式，用于修改方法中的一个常量值。
    // method = "randomTick": 指定我们要修改的目标方法是 `randomTick`，这是处理瓜茎生长逻辑的地方。
    // constant = @Constant(floatValue = 25.0f): 精确地指定我们要修改的目标是该方法中的一个浮点型常量，其原始值为 25.0f。
    // 在原版代码中，这个 25.0f 是计算生长几率的一个关键因子。
    @ModifyConstant(method = "randomTick", constant = @Constant(floatValue = 25.0f))
    public float randomTick(float constant) {
        // --- 核心逻辑 ---

        // 这个方法会在原版 `randomTick` 方法执行到那个常量时被调用。
        // 参数 `constant` 会接收到原始值，即 25.0f。
        // 我们返回一个新的值，这个新值会替换掉原来的常量。

        // 原版生长几率大致正比于 1/25。
        // 我们将这个值修改为 55。
        // 生长几率现在将大致正比于 1/55。
        // 因为 55 > 25，所以生长几率被降低了，瓜茎的生长变得更慢了。
        return 55;
    }
}
