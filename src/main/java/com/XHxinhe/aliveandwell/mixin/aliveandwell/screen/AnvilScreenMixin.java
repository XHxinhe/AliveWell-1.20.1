package com.XHxinhe.aliveandwell.mixin.aliveandwell.screen;

import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin {
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/AnvilScreenHandler;getLevelCost()I"), method = "drawForeground")
    private int drawForeground(AnvilScreenHandler anvilScreenHandler) {
//        if (anvilScreenHandler.getLevelCost() <= 5) {
//            return 0;
//        }
//        if (anvilScreenHandler.getLevelCost() > 5 && anvilScreenHandler.getLevelCost() < 20) {
//            return 6;
//        }
//        if (anvilScreenHandler.getLevelCost() >= 20 && anvilScreenHandler.getLevelCost() < 40) {
//            return 20;
//        }
        if (anvilScreenHandler.getLevelCost() >= 40) {
            return 35;
        }
        return anvilScreenHandler.getLevelCost();
    }
}

