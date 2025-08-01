package com.XHxinhe.aliveandwell.mixin.aliveandwell.client.disable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * 这是一个用于 GameOptions (游戏选项) 的 Mixin。
 * 它的核心功能是彻底禁用并锁定游戏内的“亮度”（Gamma）设置。
 * 它通过完全重写获取亮度选项的方法（getGamma）来实现这一目的。新的方法返回一个被“破坏”了的选项对象：
 * 1.  **值被锁定**：无论玩家如何拖动亮度滑块，设置的值都不会被应用，因为更新值的回调函数是空的。
 * 2.  **显示被锁定**：滑块上显示的文本被硬编码为始终显示“最低亮度”（Moody），无论其实际位置如何。
 * 3.  **默认值被锁定**：选项的默认值被强制设为最低。
 *
 * 最终效果是，玩家在设置菜单中看到的亮度滑块将形同虚设，游戏将始终以最低亮度运行，强制玩家在黑暗环境中依赖火把等光源。
 * 这是一种极大地增加游戏难度和恐怖氛围的常用手段。
 */
@Mixin(GameOptions.class) // @Mixin 注解，告诉处理器我们要修改原版的 GameOptions 类。
@Environment(EnvType.CLIENT) // @Environment 注解，明确指出这个 Mixin 只在客户端生效。
public class GameOptionsMixin {

    /**
     * @author [作者名]
     * @reason [重写原因]
     */
    // @Overwrite 注解，表示我们要用下面的方法，完全替换掉原有的 getGamma 方法。
    // 这个方法负责提供游戏设置中的“亮度”选项对象。
    @Overwrite
    public SimpleOption<Double> getGamma() {
        // --- 核心修改逻辑 ---
        // 返回一个全新的、被我们完全控制的 SimpleOption 对象来代替原版的亮度选项。
        return new SimpleOption(
                "options.gamma", // 选项的内部名称，保持不变。
                SimpleOption.emptyTooltip(), // 选项的提示文本，这里为空。

                // 这个部分是“文本显示”逻辑，它决定了滑块上显示的文字。
                (optionText, value) -> {
                    // 关键：这里定义了一个局部变量 i 并硬编码为 0，完全忽略了滑块的实际值 value。
                    int i = 0;
                    // 因为 i 永远是 0，所以这个条件永远为真。
                    if (i == 0) {
                        // 因此，滑块将永远显示“最低亮度”的文本（即 "options.gamma.min"，在游戏中是“亮度：昏暗”）。
                        return Text.translatable("options.gamma.min");
                    } else if (i == 50) {
                        return Text.translatable("options.gamma.default");
                    } else {
                        return i == 100 ? Text.translatable("options.gamma.max") : Text.literal(Integer.toString(i));
                    }
                },

                SimpleOption.DoubleSliderCallbacks.INSTANCE, // 定义这是一个标准的双精度浮点数滑块。

                0.0, // 设置该选项的默认/初始值为 0.0（最低亮度）。

                // 这个部分是“值设置”逻辑，当玩家拖动滑块时，这个回调函数会被调用。
                (value) -> {
                    // 关键：这个回调函数是空的。
                    // 这意味着当玩家更改滑块的值时，什么都不会发生。新的值被直接丢弃，游戏亮度不会有任何改变。
                }
        );
    }
}