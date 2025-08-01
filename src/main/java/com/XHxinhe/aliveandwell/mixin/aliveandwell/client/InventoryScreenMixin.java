package com.XHxinhe.aliveandwell.mixin.aliveandwell.client;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 这是一个用于 InventoryScreen (玩家物品栏屏幕) 的 Mixin。
 * 它的功能非常直观：在玩家打开物品栏时，在界面的顶部额外显示一些自定义的信息。
 *
 * 具体来说，它实现了以下功能：
 * 1.  **显示总经验值**：在屏幕顶部绘制玩家当前拥有的总经验值（`totalExperience`），而不仅仅是经验等级。这对于玩家来说是一个非常重要的信息，因为本Mod中的附魔系统是基于总经验值来计算成本的。
 * 2.  **显示游戏天数**：在总经验值的下方，绘制一个从Mod主类（`AliveAndWellMain`）获取的游戏天数计数器。这可以帮助玩家追踪游戏进程，或者与某些按天数触发的事件相关联。
 *
 * 这个Mixin通过在玩家最常打开的界面之一（物品栏）上提供关键的游戏数据，极大地提升了玩家的便利性。
 */
@Mixin(InventoryScreen.class)
@Environment(EnvType.CLIENT) // 明确指出这个Mixin只在客户端环境加载。
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> implements RecipeBookProvider {
    // @Unique: 添加一个此类独有的新字段，用于存储玩家的总经验值。
    @Unique
    private int totalEx;

    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    /**
     * @Inject: 注入到 `handledScreenTick` 方法的开头。
     * 这个方法在物品栏屏幕打开时每游戏刻都会被调用。
     */
    @Inject(method = "handledScreenTick", at = @At("HEAD"))
    public void handledScreenTick(CallbackInfo ca) {
        // 每一刻都从玩家实体获取最新的总经验值，并更新到我们的 `totalEx` 字段中。
        // 这样做可以确保屏幕上显示的数据永远是实时的。
        if (this.client != null && this.client.player != null) {
            this.totalEx = this.client.player.totalExperience;
        }
    }

    /**
     * @Inject: 注入到 `drawForeground` 方法的开头。
     * 这个方法用于在GUI背景贴图之上绘制文本等内容。
     */
    @Inject(method = "drawForeground", at = @At("HEAD"))
    public void drawForeground(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        // 绘制总经验值文本。
        // Text.translatable("XP:"+...): 创建一个包含 "XP:" 和总经验值的文本对象。
        // .formatted(Formatting.GREEN): 将文本颜色设置为绿色。
        // this.backgroundWidth/2+12, -10: 文本的坐标。Y坐标为负数，意味着它会被绘制在GUI主窗口的上方区域。
        context.drawText(this.textRenderer, Text.translatable("XP:"+String.valueOf(this.totalEx)).formatted(Formatting.GREEN), this.backgroundWidth/2+12, -10, 175752,false);

        // 这是一行被注释掉的代码，它似乎想根据复杂的日期条件显示不同的后缀（(1)或(2)），可能用于调试或某个未完成的功能。
        // context.drawText(this.textRenderer, Text.translatable("Day:"+AliveAndWellMain.day + (( AliveAndWellMain.day == 1 || AliveAndWellMain.day % AliveAndWellMain.dayTime == 0 && AliveAndWellMain.day<=48 || AliveAndWellMain.day % AliveAndWellMain.dayTimeEnd == 0 && AliveAndWellMain.day>48) ? "(2)":"(1)")).formatted(Formatting.YELLOW), this.backgroundWidth/2+12 , -20, 175752,false);

        // 绘制游戏天数文本。
        // 从 `AliveAndWellMain.day` 静态变量中获取天数。
        // .formatted(Formatting.YELLOW): 将文本颜色设置为黄色。
        // Y坐标为-20，位于经验值文本的下方。
        context.drawText(this.textRenderer, Text.translatable("Day:"+ AliveAndWellMain.day).formatted(Formatting.YELLOW), this.backgroundWidth/2+12 , -20, 175752,false);
    }
}