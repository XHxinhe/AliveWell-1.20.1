package com.XHxinhe.aliveandwell.mixin.aliveandwell.client;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 这是一个用于 TitleScreen (游戏主菜单屏幕) 的 Mixin。
 * 它的功能是在主菜单的左下角添加一行自定义文本，用于显示整合包的名称、版本和作者信息。
 *
 * 这个 Mixin 的实现非常精巧，体现在以下几点：
 * 1.  **动态文本内容**：它会检查从主类 `AliveAndWellMain` 获取的版本号字符串。如果版本号包含 "-lite" 标识，它会显示一个带有“（精简版）”后缀的文本。这允许作者为不同构建版本（例如，一个功能完整的版本和一个核心功能版）提供不同的信息。
 * 2.  **与原版动画同步**：它通过 `@Shadow` 引用了原版标题屏幕的 `doBackgroundFade` 和 `backgroundFadeStart` 字段。这两个字段控制着主菜单刚出现时的淡入动画效果。该 Mixin 使用这些值来计算一个同步的 alpha (透明度) 值，并将其应用到自定义文本上。
 * 3.  **无缝的视觉效果**：由于与原版动画同步，这行自定义文本不会突兀地瞬间出现，而是会和游戏Logo、按钮等其他UI元素一起平滑地淡入，提供了极佳的视觉整合度。
 *
 * 最终效果是，在游戏主菜单的左下角，玩家可以看到一行清晰的、带有淡入效果的整合包信息文本，这极大地提升了整合包的品牌辨识度和专业感。
 */
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    // @Shadow: 引用原版类中的字段，用于同步淡入动画。
    @Final
    @Shadow
    private  boolean doBackgroundFade; // 是否执行淡入动画
    @Shadow
    private long backgroundFadeStart; // 淡入动画开始的时间戳

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    /**
     * @Inject: 注入代码到 `render` 方法的末尾。
     * at = @At("TAIL"): 确保我们的代码在原版主菜单所有元素都绘制完毕后才执行。
     */
    @Inject(
            at = @At("TAIL"),
            method = "render"
    )
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        String string1;
        // 根据版本号中是否包含 "-lite" 来决定显示的文本内容。
        if(AliveAndWellMain.VERSION.contains("-lite")){
            string1 = "危险序曲 Dangerous Overture (Alive Well-"+ AliveAndWellMain.VERSION+")（精简版）by XHxinhe(欣訸)";
        } else {
            string1 = "危险序曲 Dangerous Overture (Alive Well-"+ AliveAndWellMain.VERSION+") by XHxinhe(欣訸)";
        }

        // --- 计算与主菜单同步的淡入透明度 ---
        // 计算自淡入开始以来经过的时间（秒）。
        float f = this.doBackgroundFade ? (float)(Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 1000.0F : 1.0F;
        // 将时间转换为一个在 [0.0, 1.0] 区间内的透明度值。
        float g = this.doBackgroundFade ? MathHelper.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;
        // 将 [0.0, 1.0] 的透明度值转换为一个 8位的 alpha 值，并将其移到32位颜色整数的最高位。
        int l = MathHelper.ceil(g * 255.0F) << 24;

        // 绘制带有阴影的文本。
        // 坐标 (2, this.height - 20) 将文本定位在屏幕的左下角。
        // 颜色 `16777215 | l` 是将白色 (0xFFFFFF) 与我们计算出的动态 alpha 值进行按位或运算，
        // 从而使文本颜色带有正确的透明度，实现同步淡入效果。
        context.drawTextWithShadow(this.textRenderer, string1, 2, this.height - 20, 16777215 | l);
    }
}