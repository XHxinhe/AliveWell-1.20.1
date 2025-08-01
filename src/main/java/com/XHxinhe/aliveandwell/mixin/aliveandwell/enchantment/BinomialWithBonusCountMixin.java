package com.XHxinhe.aliveandwell.mixin.aliveandwell.enchantment;

import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.*;

@Mixin(
        targets = "net.minecraft.loot.function.ApplyBonusLootFunction$BinomialWithBonusCount"
)
public class BinomialWithBonusCountMixin {
    @Mutable
    @Final
    @Shadow
    private final int extra;
    @Mutable
    @Final
    @Shadow
    private final float probability;

    public BinomialWithBonusCountMixin(int extra, float probability) {
        this.extra = extra;
        this.probability = probability;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getValue(Random random, int initialCount, int enchantmentLevel) {
        for (int i = 0; i < 2; ++i) {
            if (!(random.nextFloat() < this.probability)) continue;
            ++initialCount;
        }
        return initialCount;
    }
}
