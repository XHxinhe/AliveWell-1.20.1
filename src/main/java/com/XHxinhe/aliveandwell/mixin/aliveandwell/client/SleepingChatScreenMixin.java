package com.XHxinhe.aliveandwell.mixin.aliveandwell.client;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.SleepingChatScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SleepingChatScreen.class)
public abstract class SleepingChatScreenMixin {
    @Shadow private ButtonWidget stopSleepingButton;
    private int width;
    private int height;

    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/ButtonWidget;builder(Lnet/minecraft/text/Text;Lnet/minecraft/client/gui/widget/ButtonWidget$PressAction;)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;",
                    ordinal = 0
            ),
            cancellable = true
    )
    private void modifySleepButton(CallbackInfo ci) {
        // 移除原按钮
        if (this.stopSleepingButton != null) {
            this.stopSleepingButton = null;
        }

        // 创建自定义按钮
        this.stopSleepingButton = ButtonWidget.builder(
                Text.translatable("multiplayer.stopSleeping"),
                button -> this.stopSleeping()
        ).dimensions(this.width / 2 - 12, this.height - 57, 24, 20).build();

        this.addDrawableChild(this.stopSleepingButton);
        ci.cancel(); // 阻止原按钮创建
    }

    private void addDrawableChild(ButtonWidget stopSleepingButton) {

    }

    @Shadow
    private void stopSleeping() {}
}