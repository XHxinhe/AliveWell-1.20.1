package com.XHxinhe.aliveandwell.mixin.time;

import com.XHxinhe.aliveandwell.util.timeutil.WorldTimeHelper;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LevelProperties.class)
public class LevelPropertiesMixin implements WorldTimeHelper {

    private double timeOfDayDouble;

    @Override
    public double GetDoubleTime() {
        return timeOfDayDouble;
    }

    @Override
    public void SetDoubleTime(double time) {
        timeOfDayDouble = time;
    }
}
