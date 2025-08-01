package com.XHxinhe.aliveandwell.mixin.aliveandwell.world;

import net.minecraft.world.level.LevelInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LevelInfo.class)
public class LevelInfoMixin {

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean areCommandsAllowed() {
        return false;
    }
}
