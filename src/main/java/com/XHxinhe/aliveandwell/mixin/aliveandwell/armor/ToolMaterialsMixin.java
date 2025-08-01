package com.XHxinhe.aliveandwell.mixin.aliveandwell.armor;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;
import org.spongepowered.asm.mixin.*;

@Mixin(ToolMaterials.class)
public abstract class ToolMaterialsMixin implements ToolMaterial {
    @Mutable
    @Final
    @Shadow
    private final Lazy<Ingredient> repairIngredient;

    @Shadow
    public static ToolMaterials valueOf(String name) throws IllegalArgumentException {
        return null;
    }

    @Final
    @Shadow
    public static ToolMaterials IRON;

    protected ToolMaterialsMixin(Lazy<Ingredient> repairIngredient) {
        this.repairIngredient = repairIngredient;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public Ingredient getRepairIngredient() {
//        System.out.println("工具材料："+this.IRON.name());
        if(this.IRON.name().equals("IRON")){
            return Ingredient.ofItems(new ItemConvertible[]{Items.IRON_NUGGET});
        }
        return this.repairIngredient.get();
    }
}
