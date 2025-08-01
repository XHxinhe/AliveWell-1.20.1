package com.XHxinhe.aliveandwell.mixin.aliveandwell.item;

import com.XHxinhe.aliveandwell.registry.ItemInit;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Final
    @Shadow  public static final String DAMAGE_KEY = "Damage";
    @Shadow
    @Nullable
    private NbtCompound nbt;


    @Shadow public abstract Item getItem();
    @Final
    @Shadow private static final String UNBREAKABLE_KEY = "Unbreakable";

    @Inject(at = @At("HEAD"), method = "isEnchantable", cancellable = true)
    public void isEnchantable(CallbackInfoReturnable<Boolean> ca) {
        if (((ItemStack) (Object) this).isOf(Items.GOLDEN_APPLE)) {
            ca.setReturnValue(true);
        }
    }

    @Inject(method = "getTooltip", at = @At(value = "TAIL", target = "Lnet/minecraft/item/ItemStack;isDamaged()Z"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void getTooltipMixin(@Nullable PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> info, List<Text> list) {
        if(((ItemStack)(Object)this).isOf(ItemInit.EX_ADAMAN) || ((ItemStack)(Object)this).isOf(ItemInit.EX_COPPER)
                || ((ItemStack)(Object)this).isOf(ItemInit.EX_DIAMOND) || ((ItemStack)(Object)this).isOf(ItemInit.EX_GOLD)
                || ((ItemStack)(Object)this).isOf(ItemInit.EX_MITHRIL)
        ){
            list.remove(Text.translatable("item.durability", this.getMaxDamage() - this.getDamage(), this.getMaxDamage()));
        }
    }

    @Shadow public abstract int getMaxDamage();
    @Shadow public abstract int getDamage();

    @Inject(at = @At("HEAD"), method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z", cancellable = true)
    public void damage(int amount, Random random, ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if(this.getItem() instanceof ArmorItem){
            //装备耐久为1或者伤害数量-耐久>1
           if(this.getMaxDamage() - this.getDamage() <= 1){
               cir.setReturnValue(false);
           }
           else if( amount - (this.getMaxDamage() - this.getDamage()) >= 1){
               int i = this.getMaxDamage() - this.getDamage()- 1 ;
               ((ItemStack)(Object)this).setDamage(this.getMaxDamage()-1);
               cir.setReturnValue(false);
           }
        }
    }
}