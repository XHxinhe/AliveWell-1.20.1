package com.XHxinhe.aliveandwell.mixin.client;


import com.XHxinhe.aliveandwell.gui.AuthScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.realms.RealmsClient;
import net.minecraft.resource.ResourceReload;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    private void onInitFinished(RealmsClient realms, ResourceReload reload, RunArgs.QuickPlay quickPlay){};

    @Shadow
    public void setScreen(@Nullable Screen screen) {};


    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;onInitFinished(Lnet/minecraft/client/realms/RealmsClient;Lnet/minecraft/resource/ResourceReload;Lnet/minecraft/client/RunArgs$QuickPlay;)V"), method = "<init>")
    public void injectDisplayProxy(MinecraftClient instance, RealmsClient realms, ResourceReload reload, RunArgs.QuickPlay quickPlay){
        String tokenFileName = "activation-code";
        File file = new File(tokenFileName);
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(tokenFileName));
                String total = "";
                String line;
                while ((line = reader.readLine()) != null) {
                    total+=line;
                }
                reader.close();
                if(AuthScreen.setRequest(null, total.trim())){
                    this.onInitFinished(realms, reload, quickPlay);
                } else {
                    this.setScreen(new AuthScreen(null));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.setScreen(new AuthScreen(null));
        }

    }
}