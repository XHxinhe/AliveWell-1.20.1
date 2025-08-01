package com.XHxinhe.aliveandwell.mixin.aliveandwell.enchantment;

import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(
        targets = "net.minecraft.loot.function.ApplyBonusLootFunction$OreDrops"
)
public class OreDropsMixin {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getValue(Random random, int initialCount, int enchantmentLevel) {
        if (enchantmentLevel > 0) {
            int i = random.nextInt(enchantmentLevel) - 1;
            if (i < 0) {
                i = 0;
            }
            return initialCount + i;
        }
        return initialCount;
    }
}
