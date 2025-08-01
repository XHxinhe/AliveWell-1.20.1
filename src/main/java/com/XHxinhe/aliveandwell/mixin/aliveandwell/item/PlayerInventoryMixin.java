package com.XHxinhe.aliveandwell.mixin.aliveandwell.item;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @Inject(at=@At("HEAD"), method="insertStack(Lnet/minecraft/item/ItemStack;)Z", cancellable = true)
    public void insertStack(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if(stack == null){
            cir.setReturnValue(false);
        }
    }
}
