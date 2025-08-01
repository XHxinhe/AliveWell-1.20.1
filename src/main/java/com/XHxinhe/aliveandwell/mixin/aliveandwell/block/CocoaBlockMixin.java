package com.XHxinhe.aliveandwell.mixin.aliveandwell.block;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CocoaBlock.class)
public abstract class CocoaBlockMixin extends HorizontalFacingBlock implements Fertilizable {
    @Final
    @Shadow
    public static final IntProperty AGE = Properties.AGE_2;

    protected CocoaBlockMixin(Settings settings) {
        super(settings);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int i;
        if (AliveAndWellMain.day % 6 == 0 && world.random.nextInt(60) == 0 && (i = state.get(AGE).intValue()) < 2) {
            world.setBlockState(pos, state.with(AGE, i + 1), Block.NOTIFY_LISTENERS);
        }
    }
}
