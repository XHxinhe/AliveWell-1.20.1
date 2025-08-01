package com.XHxinhe.aliveandwell.mixin.aliveandwell.block;

import net.minecraft.fluid.LavaFluid;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LavaFluid.class)
public class LavaFluidMixin {


    /**
     * @author
     * @reason
     */

    //是否可以无限水
    @Overwrite
    public boolean isInfinite(World world) {
        return true;
    }
}
