package com.XHxinhe.aliveandwell.mixin.aliveandwell.armor;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;
import org.spongepowered.asm.mixin.*;

import java.util.EnumMap;

@Mixin(ArmorMaterials.class)
public abstract class ArmorMaterialsMixin {

    @Mutable
    @Final
    @Shadow
    private final EnumMap<ArmorItem.Type, Integer> protectionAmounts;
    @Mutable
    @Final
    @Shadow
    private final Lazy<Ingredient> repairIngredientSupplier;

    protected ArmorMaterialsMixin(EnumMap<ArmorItem.Type, Integer> protectionAmounts, Lazy<Ingredient> repairIngredientSupplier) {
        this.protectionAmounts = protectionAmounts;
        this.repairIngredientSupplier = repairIngredientSupplier;
    }

    @Shadow
    public abstract String getName();


    //copper 2,4,6,2
    //iron 4,6,8,4
    //wujin 8,10,12,8
    //miyin 10, 12, 16, 10
    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getProtection(ArmorItem.Type type) {
        if(this.getName().equals("iron")){
            if(type == ArmorItem.Type.BOOTS){
                return 4;
            }else if(type == ArmorItem.Type.LEGGINGS){
                return 6;
            }else if(type == ArmorItem.Type.CHESTPLATE){
                return 8;
            }else if(type == ArmorItem.Type.HELMET){
                return 4;
            }
        }
        return this.protectionAmounts.get((Object)type);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public Ingredient getRepairIngredient() {
        if(this.getName().equals("iron")){
            return Ingredient.ofItems(new ItemConvertible[]{Items.IRON_NUGGET});
        }
        return this.repairIngredientSupplier.get();
    }
}
