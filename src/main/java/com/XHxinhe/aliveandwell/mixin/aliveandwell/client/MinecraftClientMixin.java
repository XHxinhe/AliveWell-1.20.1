package com.XHxinhe.aliveandwell.mixin.aliveandwell.client;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
/**
 * 这是一个用于 MinecraftClient (Minecraft 客户端主类) 的 Mixin。
 * 它的功能非常简单且常见：修改游戏窗口的标题栏文本。
 */
@Mixin(MinecraftClient.class)
@Environment(EnvType.CLIENT)
public class MinecraftClientMixin {

    @Inject(
            method = "getWindowTitle",
            at = @At(value = "TAIL"),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private void injectedGetWindowTitle(CallbackInfoReturnable<String> cir, StringBuilder stringBuilder) {
        if(!AliveAndWellMain.VERSION.contains("-modrinth")){
            stringBuilder.append(" -危险序曲 (Dangerous Overture) "+ AliveAndWellMain.VERSION+" by XHxinhe(欣訸）");
        }
        cir.setReturnValue(stringBuilder.toString());
    }

}
