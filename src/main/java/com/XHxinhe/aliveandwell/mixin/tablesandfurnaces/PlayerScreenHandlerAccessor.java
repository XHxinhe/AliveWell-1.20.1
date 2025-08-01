package com.XHxinhe.aliveandwell.mixin.tablesandfurnaces;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.screen.PlayerScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerScreenHandler.class)
public interface PlayerScreenHandlerAccessor {
    @Accessor("craftingInput")
    RecipeInputInventory getCraftingInput();

}
