package com.XHxinhe.aliveandwell.mixin.aliveandwell;

import net.minecraft.world.gen.GeneratorOptions;
import org.spongepowered.asm.mixin.*;

@Mixin(GeneratorOptions.class)
public class GeneratorOptionsMixin {

    @Mutable
    @Final
    @Shadow private final boolean generateStructures;

    public GeneratorOptionsMixin(boolean generateStructures) {
        this.generateStructures = generateStructures;
    }


    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean hasBonusChest() {
        return false;
    }


//    @Inject(at = @At("HEAD"), method = "shouldGenerateStructures", cancellable = true)
//    public void shouldGenerateStructures(CallbackInfoReturnable<Boolean> ca) {
//        if(AliveAndWellMain.day <= 64 ){
//            ca.setReturnValue(false);
//        }
//    }
}
