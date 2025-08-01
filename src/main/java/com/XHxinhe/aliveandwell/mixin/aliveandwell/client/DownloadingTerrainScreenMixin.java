package com.XHxinhe.aliveandwell.mixin.aliveandwell.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 这是一个用于 DownloadingTerrainScreen (加载地形屏幕) 的 Mixin。
 * 它的核心功能非常简单：在玩家进入世界、显示“正在加载地形...”时，在屏幕上额外绘制两行自定义的文本。
 *
 * 这是一种常见的增强游戏沉浸感或提供信息的手段。当玩家等待加载时，屏幕上会显示一些与Mod主题相关的提示、警告或世界观背景介绍。
 * 从代码中可以看到，它获取了两个本地化文本键（`aliveandwell.loadgame.info1` 和 `info2`），并将它们显示在屏幕中央靠下的位置。
 * 这利用了玩家等待的“垃圾时间”，来强化Mod的氛围或提醒玩家重要的游戏机制。
 */
@Mixin(DownloadingTerrainScreen.class) // @Mixin 注解，告诉处理器我们要修改原版的 DownloadingTerrainScreen 类。
public abstract class DownloadingTerrainScreenMixin extends Screen {
    protected DownloadingTerrainScreenMixin(Text title) {
        super(title);
    }

    // @Inject 注解，表示我们要向原版方法中注入代码。
    // at = @At("TAIL"): 注入点在 `render` 方法的末尾。这意味着我们的代码会在原版屏幕的所有内容（如“正在加载地形...”文本）都绘制完毕后才执行。
    // method = "render": 目标方法是 `render`，该方法在屏幕显示时每一帧都会被调用来绘制界面。
    @Inject(at = @At("TAIL"), method = "render")
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        // --- 核心逻辑 ---

        // 从语言文件中获取键为 "aliveandwell.loadgame.info1" 的文本内容。
        String config = Text.translatable("aliveandwell.loadgame.info1").getString();
        // 从语言文件中获取键为 "aliveandwell.loadgame.info2" 的文本内容。
        String config1 = Text.translatable("aliveandwell.loadgame.info2").getString();

        // 使用 DrawContext 在屏幕上绘制第一行文本。
        // context.drawCenteredTextWithShadow: 绘制一个带阴影的居中对齐的文本。
        // this.width / 2: 水平位置在屏幕正中央。
        // this.height / 2 - this.textRenderer.fontHeight / 2 + 60: 垂直位置在屏幕中心点下方60像素处。
        // 0xFFFFFF: 文本颜色为白色。
        context.drawCenteredTextWithShadow(this.textRenderer, config, this.width / 2, this.height / 2 - this.textRenderer.fontHeight / 2 + 60, 0xFFFFFF);

        // 在第一行文本下方绘制第二行文本。
        // 垂直位置在屏幕中心点下方80像素处，即在第一行文本下方20像素。
        context.drawCenteredTextWithShadow(this.textRenderer, config1, this.width / 2, this.height / 2 - this.textRenderer.fontHeight / 2 + 80, 0xFFFFFF);
    }
}