package com.XHxinhe.aliveandwell.mixin.time;

import com.XHxinhe.aliveandwell.util.timeutil.WorldTimeHelper;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientWorld.Properties.class)
public class ClientWorldPropertiesMixin implements WorldTimeHelper {
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
