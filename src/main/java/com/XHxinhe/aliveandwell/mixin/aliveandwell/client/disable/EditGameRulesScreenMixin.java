package com.XHxinhe.aliveandwell.mixin.aliveandwell.client.disable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.EditGameRulesScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 这是一个用于 EditGameRulesScreen (编辑游戏规则屏幕) 的 Mixin。
 * 它的核心功能非常直接：禁用“完成”按钮，从而阻止玩家修改任何游戏规则。
 * 当玩家进入这个界面时，他们可以看到所有的游戏规则设置，但由于“完成”按钮是灰色且无法点击的，他们所做的任何更改都无法被保存。
 * 这是一种强制执行特定游戏规则集的有效手段，通常用于确保整合包或Mod预设的挑战性玩法不被玩家轻易绕过（例如，防止玩家开启“保留物品栏”等规则）。
 * 从包名中的 "disable" 可以看出，这正是该 Mixin 的设计意图。
 */
@Mixin(EditGameRulesScreen.class) // @Mixin 注解，告诉处理器我们要修改原版的 EditGameRulesScreen 类。
@Environment(EnvType.CLIENT) // @Environment 注解，明确指出这个 Mixin 只在客户端环境中生效，因为屏幕(Screen)是客户端独有的。
public abstract class EditGameRulesScreenMixin extends Screen {

    // @Shadow 注解，让我们能直接“引用”并访问原版类中的 `doneButton` 私有字段。
    // 这个字段代表了屏幕右下角的“完成”按钮。
    @Shadow private ButtonWidget doneButton;

    // Mixin 类的标准构造函数。
    protected EditGameRulesScreenMixin(Text title) {
        super(title);
    }

    // @Inject 注解，表示我们要向原版方法中注入我们自己的代码。
    // method = "init": 指定我们要注入的目标方法是 `init`。这个方法在屏幕被打开时调用，用于初始化所有界面元素（如按钮）。
    // at = @At("TAIL"): 指定注入点在 `init` 方法的末尾（TAIL）。这意味着我们的代码会在原版 `init` 方法的所有逻辑都执行完毕后才运行，确保 `doneButton` 此时已经被创建好了。
    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo ca) {
        // --- 核心逻辑 ---
        // 在屏幕初始化完成的最后一步，获取到“完成”按钮的引用...
        // ...并将其 `active` 属性设置为 false。
        // 在 Minecraft 的 UI 系统中，一个非 active 的按钮会变灰，并且无法被玩家点击。
        this.doneButton.active = false;
    }
}