package com.XHxinhe.aliveandwell.mixin.aliveandwell;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.StringIdentifiable;
import org.spongepowered.asm.mixin.*;

import java.util.Random;

@Mixin(SpawnGroup.class)
public abstract class SpawnGroupMixin  implements StringIdentifiable {
    protected SpawnGroupMixin(int capacity, int immediateDespawnRange, boolean peaceful) {
        this.capacity = capacity;
        this.immediateDespawnRange = immediateDespawnRange;
        this.peaceful = peaceful;
    }

    @Shadow public abstract String getName();
    @Mutable
    @Shadow @Final private final int capacity;
    @Mutable
    @Shadow @Final private final int immediateDespawnRange;
    @Mutable
    @Shadow @Final private final boolean peaceful;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getCapacity() {
        if (this.getName().equals("monster")) {
            if(AliveAndWellMain.ca >= 100){
                return 100;
            }else {
                return AliveAndWellMain.ca;
            }
        }
        if (this.getName().equals("creature")) {
            return 3;
        }
        return this.capacity;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean isPeaceful() {
        //water_creature墨鱼， water_ambient 鱼
        if (this.getName().equals("water_creature") || this.getName().equals("water_ambient") || this.getName().equals("misc")) {
            int ran = new Random().nextInt(100);
            return AliveAndWellMain.day % 7 != 1 && ran <= 10;
        }
        return this.peaceful;
    }
}
