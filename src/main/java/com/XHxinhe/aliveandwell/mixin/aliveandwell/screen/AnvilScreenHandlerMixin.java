package com.XHxinhe.aliveandwell.mixin.aliveandwell.screen;

import net.minecraft.enchantment.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.screen.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {
    @Final
    @Shadow private  Property levelCost;

    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Inject(at = @At("HEAD"), method = "onTakeOutput")
    public void onTakeOutput(PlayerEntity player, ItemStack stack, CallbackInfo ca) {
        if (!player.getAbilities().creativeMode) {
            if(this.levelCost.get() >= 5 && this.levelCost.get()  < 10){
                if(player instanceof ServerPlayerEntity){
                    player.sendMessage(Text.translatable("此次修复消耗50经验").formatted(Formatting.LIGHT_PURPLE));
                }
                player.addExperience(-50);
            }
            if(this.levelCost.get() >= 10 && this.levelCost.get()  < 15){
                if(player instanceof ServerPlayerEntity){
                    player.sendMessage(Text.translatable("此次修复消耗200经验").formatted(Formatting.LIGHT_PURPLE));
                }
                player.addExperience(-200);
            }
            if(this.levelCost.get() >= 15 && this.levelCost.get()  < 20){
                if(player instanceof ServerPlayerEntity){
                    player.sendMessage(Text.translatable("此次修复消耗400经验").formatted(Formatting.LIGHT_PURPLE));
                }
                player.addExperience(-400);
            }
            if(this.levelCost.get() >= 20 && this.levelCost.get()  < 25){
                if(player instanceof ServerPlayerEntity){
                    player.sendMessage(Text.translatable("此次修复消耗600经验").formatted(Formatting.LIGHT_PURPLE));
                }
                player.addExperience(-600);
            }
            if(this.levelCost.get() >= 25 && this.levelCost.get()  < 30){
                if(player instanceof ServerPlayerEntity){
                    player.sendMessage(Text.translatable("此次修复消耗800经验").formatted(Formatting.LIGHT_PURPLE));
                }
                player.addExperience(-800);
            }
            if(this.levelCost.get() >= 30 ){
                if(player instanceof ServerPlayerEntity){
                    player.sendMessage(Text.translatable("此次修复消耗1000经验").formatted(Formatting.LIGHT_PURPLE));
                }
                player.addExperience(-1000);
            }
            player.addExperienceLevels(this.levelCost.get());
        }
    }

//    @Inject(at = @At("HEAD"), method = "getLevelCost",cancellable = true)
//    public void getLevelCost(CallbackInfoReturnable<Integer> ca) {
//        if(this.levelCost.get() >= 0 ){
//            ca.setReturnValue(1);
//        }
//    }

    @Inject(at = @At("HEAD"), method = "updateResult", cancellable = true)
    public void updateResult(CallbackInfo ci) {
        ItemStack itemStack = this.input.getStack(0);
        ItemStack itemStack1 = this.input.getStack(1);
        if(itemStack.getItem() == Items.GOLDEN_APPLE || itemStack1.getItem() == Items.GOLDEN_APPLE
                || itemStack.getItem() == Items.GOLDEN_CARROT || itemStack1.getItem() == Items.GOLDEN_CARROT){
            ci.cancel();
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/Property;get()I"), method = "updateResult")
    private int get(Property property) {
        ItemStack itemStack1 = this.input.getStack(0);
        ItemStack itemStack2 = this.input.getStack(1);
        boolean b1 = true;
        boolean b2 = true;
        if(itemStack1.hasEnchantments()){
            NbtList ens = itemStack1.getEnchantments();
            for(int i =0;i<ens.size();i++){
                if(ens.getString(i).contains("radiance")
                        || ens.getString(i).contains("radiance_shot")
                        || ens.getString(i).contains("prospector")
                        || ens.getString(i).contains("heal_allies")
                        || ens.getString(i).contains("lucky_explorer")
                        || ens.getString(i).contains("leeching")
                        || ens.getString(i).contains("reckless")
                        || ens.getString(i).contains("anima_conduit")
                        || ens.getString(i).contains("death_barter")
                        || ens.getString(i).contains("rushdown")//==
                        || ens.getString(i).contains("tempo_theft")
                        || ens.getString(i).contains("beast_boss")
                        || ens.getString(i).contains("beast_burst")
                        || ens.getString(i).contains("beast_surge")
                        || ens.getString(i).contains("mcda:swiftfooted")
                        || ens.getString(i).contains("mcdw:refreshment")
                        || ens.getString(i).contains("mcdw:void_shot")
                        || ens.getString(i).contains("mcdw:void_strike")
                        || ens.getString(i).contains("mcda:cowardice")
//                        || ens.getString(i).contains("minecraft:protection")//test=================
                ){
                    b1=false;
                }
//                this.player.sendMessage(Text.translatable(ens.getString(i)));
//                this.player.sendMessage(Text.translatable(String.valueOf(b1)));
            }
        }
        if(itemStack2.hasEnchantments() ){
            NbtList ens = itemStack2.getEnchantments();
            for(int i =0;i<ens.size();i++){
                if(ens.getString(i).contains("radiance")
                        || ens.getString(i).contains("radiance_shot")
                        || ens.getString(i).contains("prospector")
                        || ens.getString(i).contains("heal_allies")
                        || ens.getString(i).contains("lucky_explorer")
                        || ens.getString(i).contains("leeching")
                        || ens.getString(i).contains("reckless")
                        || ens.getString(i).contains("anima_conduit")
                        || ens.getString(i).contains("death_barter")
                        || ens.getString(i).contains("rushdown")//==
                        || ens.getString(i).contains("tempo_theft")
                        || ens.getString(i).contains("beast_boss")
                        || ens.getString(i).contains("beast_burst")
                        || ens.getString(i).contains("beast_surge")
                        || ens.getString(i).contains("mcda:swiftfooted")
                        || ens.getString(i).contains("mcdw:refreshment")
                        || ens.getString(i).contains("mcdw:void_shot")
                        || ens.getString(i).contains("mcdw:void_strike")
                        || ens.getString(i).contains("mcda:cowardice")
//                        || ens.getString(i).contains("minecraft:protection")//test=================
                ){
                    b2=false;
                }
            }
        }

        if(itemStack2.hasEnchantments() ){
            NbtList ens = itemStack2.getEnchantments();
            for(int i =0;i<ens.size();i++){
                if(ens.getString(i).contains("radiance")
                        || ens.getString(i).contains("radiance_shot")
                        || ens.getString(i).contains("prospector")
                        || ens.getString(i).contains("heal_allies")
                        || ens.getString(i).contains("lucky_explorer")
                        || ens.getString(i).contains("leeching")
                        || ens.getString(i).contains("reckless")
                        || ens.getString(i).contains("anima_conduit")
                        || ens.getString(i).contains("death_barter")
                        || ens.getString(i).contains("rushdown")//==
                        || ens.getString(i).contains("tempo_theft")
                        || ens.getString(i).contains("beast_boss")
                        || ens.getString(i).contains("beast_burst")
                        || ens.getString(i).contains("beast_surge")
                        || ens.getString(i).contains("mcda:swiftfooted")
                        || ens.getString(i).contains("mcdw:refreshment")
                        || ens.getString(i).contains("mcdw:void_shot")
                        || ens.getString(i).contains("mcdw:void_strike")
                        || ens.getString(i).contains("mcda:cowardice")
//                        || ens.getString(i).contains("minecraft:protection")//test=================
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.hasEnchantments() ){
            NbtList ens = itemStack2.getEnchantments();
            for(int i =0;i<ens.size();i++){
                if(ens.getString(i).contains("radiance")
                        || ens.getString(i).contains("radiance_shot")
                        || ens.getString(i).contains("prospector")
                        || ens.getString(i).contains("heal_allies")
                        || ens.getString(i).contains("lucky_explorer")
                        || ens.getString(i).contains("leeching")
                        || ens.getString(i).contains("reckless")
                        || ens.getString(i).contains("anima_conduit")
                        || ens.getString(i).contains("death_barter")
                        || ens.getString(i).contains("rushdown")//==
                        || ens.getString(i).contains("tempo_theft")
                        || ens.getString(i).contains("beast_boss")
                        || ens.getString(i).contains("beast_burst")
                        || ens.getString(i).contains("beast_surge")
                        || ens.getString(i).contains("mcda:swiftfooted")
                        || ens.getString(i).contains("mcdw:refreshment")
                        || ens.getString(i).contains("mcdw:void_shot")
                        || ens.getString(i).contains("mcdw:void_strike")
                        || ens.getString(i).contains("mcda:cowardice")
//                        || ens.getString(i).contains("minecraft:protection")//test=================
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.hasEnchantments() ){
            NbtList ens = itemStack2.getEnchantments();
            for(int i =0;i<ens.size();i++){
                if(ens.getString(i).contains("radiance")
                        || ens.getString(i).contains("radiance_shot")
                        || ens.getString(i).contains("prospector")
                        || ens.getString(i).contains("heal_allies")
                        || ens.getString(i).contains("lucky_explorer")
                        || ens.getString(i).contains("leeching")
                        || ens.getString(i).contains("reckless")
                        || ens.getString(i).contains("anima_conduit")
                        || ens.getString(i).contains("death_barter")
                        || ens.getString(i).contains("rushdown")//==
                        || ens.getString(i).contains("tempo_theft")
                        || ens.getString(i).contains("beast_boss")
                        || ens.getString(i).contains("beast_burst")
                        || ens.getString(i).contains("beast_surge")
                        || ens.getString(i).contains("mcda:swiftfooted")
                        || ens.getString(i).contains("mcdw:refreshment")
                        || ens.getString(i).contains("mcdw:void_shot")
                        || ens.getString(i).contains("mcdw:void_strike")
                        || ens.getString(i).contains("mcda:cowardice")
//                        || ens.getString(i).contains("minecraft:protection")//test=================
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.hasEnchantments() ){
            NbtList ens = itemStack2.getEnchantments();
            for(int i =0;i<ens.size();i++){
                if(ens.getString(i).contains("radiance")
                        || ens.getString(i).contains("radiance_shot")
                        || ens.getString(i).contains("prospector")
                        || ens.getString(i).contains("heal_allies")
                        || ens.getString(i).contains("lucky_explorer")
                        || ens.getString(i).contains("leeching")
                        || ens.getString(i).contains("reckless")
                        || ens.getString(i).contains("anima_conduit")
                        || ens.getString(i).contains("death_barter")
                        || ens.getString(i).contains("rushdown")//==
                        || ens.getString(i).contains("tempo_theft")
                        || ens.getString(i).contains("beast_boss")
                        || ens.getString(i).contains("beast_burst")
                        || ens.getString(i).contains("beast_surge")
                        || ens.getString(i).contains("mcda:swiftfooted")
                        || ens.getString(i).contains("mcdw:refreshment")
                        || ens.getString(i).contains("mcdw:void_shot")
                        || ens.getString(i).contains("mcdw:void_strike")
                        || ens.getString(i).contains("mcda:cowardice")
//                        || ens.getString(i).contains("minecraft:protection")//test=================
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.hasEnchantments() ){
            NbtList ens = itemStack2.getEnchantments();
            for(int i =0;i<ens.size();i++){
                if(ens.getString(i).contains("radiance")
                        || ens.getString(i).contains("radiance_shot")
                        || ens.getString(i).contains("prospector")
                        || ens.getString(i).contains("heal_allies")
                        || ens.getString(i).contains("lucky_explorer")
                        || ens.getString(i).contains("leeching")
                        || ens.getString(i).contains("reckless")
                        || ens.getString(i).contains("anima_conduit")
                        || ens.getString(i).contains("death_barter")
                        || ens.getString(i).contains("rushdown")//==
                        || ens.getString(i).contains("tempo_theft")
                        || ens.getString(i).contains("beast_boss")
                        || ens.getString(i).contains("beast_burst")
                        || ens.getString(i).contains("beast_surge")
                        || ens.getString(i).contains("mcda:swiftfooted")
                        || ens.getString(i).contains("mcdw:refreshment")
                        || ens.getString(i).contains("mcdw:void_shot")
                        || ens.getString(i).contains("mcdw:void_strike")
                        || ens.getString(i).contains("mcda:cowardice")
//                        || ens.getString(i).contains("minecraft:protection")//test=================
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.hasEnchantments() ){
            NbtList ens = itemStack2.getEnchantments();
            for(int i =0;i<ens.size();i++){
                if(ens.getString(i).contains("radiance")
                        || ens.getString(i).contains("radiance_shot")
                        || ens.getString(i).contains("prospector")
                        || ens.getString(i).contains("heal_allies")
                        || ens.getString(i).contains("lucky_explorer")
                        || ens.getString(i).contains("leeching")
                        || ens.getString(i).contains("reckless")
                        || ens.getString(i).contains("anima_conduit")
                        || ens.getString(i).contains("death_barter")
                        || ens.getString(i).contains("rushdown")//==
                        || ens.getString(i).contains("tempo_theft")
                        || ens.getString(i).contains("beast_boss")
                        || ens.getString(i).contains("beast_burst")
                        || ens.getString(i).contains("beast_surge")
                        || ens.getString(i).contains("mcda:swiftfooted")
                        || ens.getString(i).contains("mcdw:refreshment")
                        || ens.getString(i).contains("mcdw:void_shot")
                        || ens.getString(i).contains("mcdw:void_strike")
                        || ens.getString(i).contains("mcda:cowardice")
//                        || ens.getString(i).contains("minecraft:protection")//test=================
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.hasEnchantments() ){
            NbtList ens = itemStack2.getEnchantments();
            for(int i =0;i<ens.size();i++){
                if(ens.getString(i).contains("radiance")
                        || ens.getString(i).contains("radiance_shot")
                        || ens.getString(i).contains("prospector")
                        || ens.getString(i).contains("heal_allies")
                        || ens.getString(i).contains("lucky_explorer")
                        || ens.getString(i).contains("leeching")
                        || ens.getString(i).contains("reckless")
                        || ens.getString(i).contains("anima_conduit")
                        || ens.getString(i).contains("death_barter")
                        || ens.getString(i).contains("rushdown")//==
                        || ens.getString(i).contains("tempo_theft")
                        || ens.getString(i).contains("beast_boss")
                        || ens.getString(i).contains("beast_burst")
                        || ens.getString(i).contains("beast_surge")
                        || ens.getString(i).contains("mcda:swiftfooted")
                        || ens.getString(i).contains("mcdw:refreshment")
                        || ens.getString(i).contains("mcdw:void_shot")
                        || ens.getString(i).contains("mcdw:void_strike")
                        || ens.getString(i).contains("mcda:cowardice")
//                        || ens.getString(i).contains("minecraft:protection")//test=================
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.hasEnchantments() ){
            NbtList ens = itemStack2.getEnchantments();
            for(int i =0;i<ens.size();i++){
                if(ens.getString(i).contains("radiance")
                        || ens.getString(i).contains("radiance_shot")
                        || ens.getString(i).contains("prospector")
                        || ens.getString(i).contains("heal_allies")
                        || ens.getString(i).contains("lucky_explorer")
                        || ens.getString(i).contains("leeching")
                        || ens.getString(i).contains("reckless")
                        || ens.getString(i).contains("anima_conduit")
                        || ens.getString(i).contains("death_barter")
                        || ens.getString(i).contains("rushdown")//==
                        || ens.getString(i).contains("tempo_theft")
                        || ens.getString(i).contains("beast_boss")
                        || ens.getString(i).contains("beast_burst")
                        || ens.getString(i).contains("beast_surge")
                        || ens.getString(i).contains("mcda:swiftfooted")
                        || ens.getString(i).contains("mcdw:refreshment")
                        || ens.getString(i).contains("mcdw:void_shot")
                        || ens.getString(i).contains("mcdw:void_strike")
                        || ens.getString(i).contains("mcda:cowardice")
//                        || ens.getString(i).contains("minecraft:protection")//test=================
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.hasEnchantments() ){
            NbtList ens = itemStack2.getEnchantments();
            for(int i =0;i<ens.size();i++){
                if(ens.getString(i).contains("radiance")
                        || ens.getString(i).contains("radiance_shot")
                        || ens.getString(i).contains("prospector")
                        || ens.getString(i).contains("heal_allies")
                        || ens.getString(i).contains("lucky_explorer")
                        || ens.getString(i).contains("leeching")
                        || ens.getString(i).contains("reckless")
                        || ens.getString(i).contains("anima_conduit")
                        || ens.getString(i).contains("death_barter")
                        || ens.getString(i).contains("rushdown")//==
                        || ens.getString(i).contains("tempo_theft")
                        || ens.getString(i).contains("beast_boss")
                        || ens.getString(i).contains("beast_burst")
                        || ens.getString(i).contains("beast_surge")
                        || ens.getString(i).contains("mcda:swiftfooted")
                        || ens.getString(i).contains("mcdw:refreshment")
                        || ens.getString(i).contains("mcdw:void_shot")
                        || ens.getString(i).contains("mcdw:void_strike")
                        || ens.getString(i).contains("mcda:cowardice")
//                        || ens.getString(i).contains("minecraft:protection")//test=================
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.hasEnchantments() ){
            NbtList ens = itemStack2.getEnchantments();
            for(int i =0;i<ens.size();i++){
                if(ens.getString(i).contains("radiance")
                        || ens.getString(i).contains("radiance_shot")
                        || ens.getString(i).contains("prospector")
                        || ens.getString(i).contains("heal_allies")
                        || ens.getString(i).contains("lucky_explorer")
                        || ens.getString(i).contains("leeching")
                        || ens.getString(i).contains("reckless")
                        || ens.getString(i).contains("anima_conduit")
                        || ens.getString(i).contains("death_barter")
                        || ens.getString(i).contains("rushdown")//==
                        || ens.getString(i).contains("tempo_theft")
                        || ens.getString(i).contains("beast_boss")
                        || ens.getString(i).contains("beast_burst")
                        || ens.getString(i).contains("beast_surge")
                        || ens.getString(i).contains("mcda:swiftfooted")
                        || ens.getString(i).contains("mcdw:refreshment")
                        || ens.getString(i).contains("mcdw:void_shot")
                        || ens.getString(i).contains("mcdw:void_strike")
                        || ens.getString(i).contains("mcda:cowardice")
//                        || ens.getString(i).contains("minecraft:protection")//test=================
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.hasEnchantments() ){
            NbtList ens = itemStack2.getEnchantments();
            for(int i =0;i<ens.size();i++){
                if(ens.getString(i).contains("radiance")
                        || ens.getString(i).contains("radiance_shot")
                        || ens.getString(i).contains("prospector")
                        || ens.getString(i).contains("heal_allies")
                        || ens.getString(i).contains("lucky_explorer")
                        || ens.getString(i).contains("leeching")
                        || ens.getString(i).contains("reckless")
                        || ens.getString(i).contains("anima_conduit")
                        || ens.getString(i).contains("death_barter")
                        || ens.getString(i).contains("rushdown")//==
                        || ens.getString(i).contains("tempo_theft")
                        || ens.getString(i).contains("beast_boss")
                        || ens.getString(i).contains("beast_burst")
                        || ens.getString(i).contains("beast_surge")
                        || ens.getString(i).contains("mcda:swiftfooted")
                        || ens.getString(i).contains("mcdw:refreshment")
                        || ens.getString(i).contains("mcdw:void_shot")
                        || ens.getString(i).contains("mcdw:void_strike")
                        || ens.getString(i).contains("mcda:cowardice")
//                        || ens.getString(i).contains("minecraft:protection")//test=================
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.hasEnchantments() ){
            NbtList ens = itemStack2.getEnchantments();
            for(int i =0;i<ens.size();i++){
                if(ens.getString(i).contains("radiance")
                        || ens.getString(i).contains("radiance_shot")
                        || ens.getString(i).contains("prospector")
                        || ens.getString(i).contains("heal_allies")
                        || ens.getString(i).contains("lucky_explorer")
                        || ens.getString(i).contains("leeching")
                        || ens.getString(i).contains("reckless")
                        || ens.getString(i).contains("anima_conduit")
                        || ens.getString(i).contains("death_barter")
                        || ens.getString(i).contains("rushdown")//==
                        || ens.getString(i).contains("tempo_theft")
                        || ens.getString(i).contains("beast_boss")
                        || ens.getString(i).contains("beast_burst")
                        || ens.getString(i).contains("beast_surge")
                        || ens.getString(i).contains("mcda:swiftfooted")
                        || ens.getString(i).contains("mcdw:refreshment")
                        || ens.getString(i).contains("mcdw:void_shot")
                        || ens.getString(i).contains("mcdw:void_strike")
                        || ens.getString(i).contains("mcda:cowardice")
//                        || ens.getString(i).contains("minecraft:protection")//test=================
                ){
                    b2=false;
                }
            }
        }

        if(itemStack2.getItem() instanceof EnchantedBookItem){
            NbtList nbtList = EnchantedBookItem.getEnchantmentNbt(itemStack2);
            for(int i =0;i<nbtList.size();i++){
                NbtCompound nbtCompound = nbtList.getCompound(i);
                Identifier identifier2 = EnchantmentHelper.getIdFromNbt(nbtCompound);
                assert identifier2 != null;
                if(identifier2.toString().contains("radiance")
                || identifier2.toString().contains("radiance_shot")
                || identifier2.toString().contains("prospector")
                || identifier2.toString().contains("heal_allies")
                || identifier2.toString().contains("lucky_explorer")
                || identifier2.toString().contains("leeching")
                || identifier2.toString().contains("reckless")
                || identifier2.toString().contains("anima_conduit")
                || identifier2.toString().contains("death_barter")
                || identifier2.toString().contains("rushdown")//==
                || identifier2.toString().contains("tempo_theft")
                || identifier2.toString().contains("beast_boss")
                || identifier2.toString().contains("beast_burst")
                || identifier2.toString().contains("beast_surge")
                || identifier2.toString().contains("mcda:swiftfooted")
                || identifier2.toString().contains("mcdw:refreshment")
                || identifier2.toString().contains("mcdw:void_shot")
                || identifier2.toString().contains("mcdw:void_strike")
                || identifier2.toString().contains("mcda:cowardice")
//                || identifier2.toString().contains("minecraft:protection")//test===============
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.getItem() instanceof EnchantedBookItem){
            NbtList nbtList = EnchantedBookItem.getEnchantmentNbt(itemStack2);
            for(int i =0;i<nbtList.size();i++){
                NbtCompound nbtCompound = nbtList.getCompound(i);
                Identifier identifier2 = EnchantmentHelper.getIdFromNbt(nbtCompound);
                assert identifier2 != null;
                if(identifier2.toString().contains("radiance")
                        || identifier2.toString().contains("radiance_shot")
                        || identifier2.toString().contains("prospector")
                        || identifier2.toString().contains("heal_allies")
                        || identifier2.toString().contains("lucky_explorer")
                        || identifier2.toString().contains("leeching")
                        || identifier2.toString().contains("reckless")
                        || identifier2.toString().contains("anima_conduit")
                        || identifier2.toString().contains("death_barter")
                        || identifier2.toString().contains("rushdown")//==
                        || identifier2.toString().contains("tempo_theft")
                        || identifier2.toString().contains("beast_boss")
                        || identifier2.toString().contains("beast_burst")
                        || identifier2.toString().contains("beast_surge")
                        || identifier2.toString().contains("mcda:swiftfooted")
                        || identifier2.toString().contains("mcdw:refreshment")
                        || identifier2.toString().contains("mcdw:void_shot")
                        || identifier2.toString().contains("mcdw:void_strike")
                        || identifier2.toString().contains("mcda:cowardice")
//                        || identifier2.toString().contains("minecraft:protection")//test===============
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.getItem() instanceof EnchantedBookItem){
            NbtList nbtList = EnchantedBookItem.getEnchantmentNbt(itemStack2);
            for(int i =0;i<nbtList.size();i++){
                NbtCompound nbtCompound = nbtList.getCompound(i);
                Identifier identifier2 = EnchantmentHelper.getIdFromNbt(nbtCompound);
                assert identifier2 != null;
                if(identifier2.toString().contains("radiance")
                        || identifier2.toString().contains("radiance_shot")
                        || identifier2.toString().contains("prospector")
                        || identifier2.toString().contains("heal_allies")
                        || identifier2.toString().contains("lucky_explorer")
                        || identifier2.toString().contains("leeching")
                        || identifier2.toString().contains("reckless")
                        || identifier2.toString().contains("anima_conduit")
                        || identifier2.toString().contains("death_barter")
                        || identifier2.toString().contains("rushdown")//==
                        || identifier2.toString().contains("tempo_theft")
                        || identifier2.toString().contains("beast_boss")
                        || identifier2.toString().contains("beast_burst")
                        || identifier2.toString().contains("beast_surge")
                        || identifier2.toString().contains("mcda:swiftfooted")
                        || identifier2.toString().contains("mcdw:refreshment")
                        || identifier2.toString().contains("mcdw:void_shot")
                        || identifier2.toString().contains("mcdw:void_strike")
                        || identifier2.toString().contains("mcda:cowardice")
//                        || identifier2.toString().contains("minecraft:protection")//test===============
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.getItem() instanceof EnchantedBookItem){
            NbtList nbtList = EnchantedBookItem.getEnchantmentNbt(itemStack2);
            for(int i =0;i<nbtList.size();i++){
                NbtCompound nbtCompound = nbtList.getCompound(i);
                Identifier identifier2 = EnchantmentHelper.getIdFromNbt(nbtCompound);
                assert identifier2 != null;
                if(identifier2.toString().contains("radiance")
                        || identifier2.toString().contains("radiance_shot")
                        || identifier2.toString().contains("prospector")
                        || identifier2.toString().contains("heal_allies")
                        || identifier2.toString().contains("lucky_explorer")
                        || identifier2.toString().contains("leeching")
                        || identifier2.toString().contains("reckless")
                        || identifier2.toString().contains("anima_conduit")
                        || identifier2.toString().contains("death_barter")
                        || identifier2.toString().contains("rushdown")//==
                        || identifier2.toString().contains("tempo_theft")
                        || identifier2.toString().contains("beast_boss")
                        || identifier2.toString().contains("beast_burst")
                        || identifier2.toString().contains("beast_surge")
                        || identifier2.toString().contains("mcda:swiftfooted")
                        || identifier2.toString().contains("mcdw:refreshment")
                        || identifier2.toString().contains("mcdw:void_shot")
                        || identifier2.toString().contains("mcdw:void_strike")
                        || identifier2.toString().contains("mcda:cowardice")
//                        || identifier2.toString().contains("minecraft:protection")//test===============
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.getItem() instanceof EnchantedBookItem){
            NbtList nbtList = EnchantedBookItem.getEnchantmentNbt(itemStack2);
            for(int i =0;i<nbtList.size();i++){
                NbtCompound nbtCompound = nbtList.getCompound(i);
                Identifier identifier2 = EnchantmentHelper.getIdFromNbt(nbtCompound);
                assert identifier2 != null;
                if(identifier2.toString().contains("radiance")
                        || identifier2.toString().contains("radiance_shot")
                        || identifier2.toString().contains("prospector")
                        || identifier2.toString().contains("heal_allies")
                        || identifier2.toString().contains("lucky_explorer")
                        || identifier2.toString().contains("leeching")
                        || identifier2.toString().contains("reckless")
                        || identifier2.toString().contains("anima_conduit")
                        || identifier2.toString().contains("death_barter")
                        || identifier2.toString().contains("rushdown")//==
                        || identifier2.toString().contains("tempo_theft")
                        || identifier2.toString().contains("beast_boss")
                        || identifier2.toString().contains("beast_burst")
                        || identifier2.toString().contains("beast_surge")
                        || identifier2.toString().contains("mcda:swiftfooted")
                        || identifier2.toString().contains("mcdw:refreshment")
                        || identifier2.toString().contains("mcdw:void_shot")
                        || identifier2.toString().contains("mcdw:void_strike")
                        || identifier2.toString().contains("mcda:cowardice")
//                        || identifier2.toString().contains("minecraft:protection")//test===============
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.getItem() instanceof EnchantedBookItem){
            NbtList nbtList = EnchantedBookItem.getEnchantmentNbt(itemStack2);
            for(int i =0;i<nbtList.size();i++){
                NbtCompound nbtCompound = nbtList.getCompound(i);
                Identifier identifier2 = EnchantmentHelper.getIdFromNbt(nbtCompound);
                assert identifier2 != null;
                if(identifier2.toString().contains("radiance")
                        || identifier2.toString().contains("radiance_shot")
                        || identifier2.toString().contains("prospector")
                        || identifier2.toString().contains("heal_allies")
                        || identifier2.toString().contains("lucky_explorer")
                        || identifier2.toString().contains("leeching")
                        || identifier2.toString().contains("reckless")
                        || identifier2.toString().contains("anima_conduit")
                        || identifier2.toString().contains("death_barter")
                        || identifier2.toString().contains("rushdown")//==
                        || identifier2.toString().contains("tempo_theft")
                        || identifier2.toString().contains("beast_boss")
                        || identifier2.toString().contains("beast_burst")
                        || identifier2.toString().contains("beast_surge")
                        || identifier2.toString().contains("mcda:swiftfooted")
                        || identifier2.toString().contains("mcdw:refreshment")
                        || identifier2.toString().contains("mcdw:void_shot")
                        || identifier2.toString().contains("mcdw:void_strike")
                        || identifier2.toString().contains("mcda:cowardice")
//                        || identifier2.toString().contains("minecraft:protection")//test===============
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.getItem() instanceof EnchantedBookItem){
            NbtList nbtList = EnchantedBookItem.getEnchantmentNbt(itemStack2);
            for(int i =0;i<nbtList.size();i++){
                NbtCompound nbtCompound = nbtList.getCompound(i);
                Identifier identifier2 = EnchantmentHelper.getIdFromNbt(nbtCompound);
                assert identifier2 != null;
                if(identifier2.toString().contains("radiance")
                        || identifier2.toString().contains("radiance_shot")
                        || identifier2.toString().contains("prospector")
                        || identifier2.toString().contains("heal_allies")
                        || identifier2.toString().contains("lucky_explorer")
                        || identifier2.toString().contains("leeching")
                        || identifier2.toString().contains("reckless")
                        || identifier2.toString().contains("anima_conduit")
                        || identifier2.toString().contains("death_barter")
                        || identifier2.toString().contains("rushdown")//==
                        || identifier2.toString().contains("tempo_theft")
                        || identifier2.toString().contains("beast_boss")
                        || identifier2.toString().contains("beast_burst")
                        || identifier2.toString().contains("beast_surge")
                        || identifier2.toString().contains("mcda:swiftfooted")
                        || identifier2.toString().contains("mcdw:refreshment")
                        || identifier2.toString().contains("mcdw:void_shot")
                        || identifier2.toString().contains("mcdw:void_strike")
                        || identifier2.toString().contains("mcda:cowardice")
//                        || identifier2.toString().contains("minecraft:protection")//test===============
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.getItem() instanceof EnchantedBookItem){
            NbtList nbtList = EnchantedBookItem.getEnchantmentNbt(itemStack2);
            for(int i =0;i<nbtList.size();i++){
                NbtCompound nbtCompound = nbtList.getCompound(i);
                Identifier identifier2 = EnchantmentHelper.getIdFromNbt(nbtCompound);
                assert identifier2 != null;
                if(identifier2.toString().contains("radiance")
                        || identifier2.toString().contains("radiance_shot")
                        || identifier2.toString().contains("prospector")
                        || identifier2.toString().contains("heal_allies")
                        || identifier2.toString().contains("lucky_explorer")
                        || identifier2.toString().contains("leeching")
                        || identifier2.toString().contains("reckless")
                        || identifier2.toString().contains("anima_conduit")
                        || identifier2.toString().contains("death_barter")
                        || identifier2.toString().contains("rushdown")//==
                        || identifier2.toString().contains("tempo_theft")
                        || identifier2.toString().contains("beast_boss")
                        || identifier2.toString().contains("beast_burst")
                        || identifier2.toString().contains("beast_surge")
                        || identifier2.toString().contains("mcda:swiftfooted")
                        || identifier2.toString().contains("mcdw:refreshment")
                        || identifier2.toString().contains("mcdw:void_shot")
                        || identifier2.toString().contains("mcdw:void_strike")
                        || identifier2.toString().contains("mcda:cowardice")
//                        || identifier2.toString().contains("minecraft:protection")//test===============
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.getItem() instanceof EnchantedBookItem){
            NbtList nbtList = EnchantedBookItem.getEnchantmentNbt(itemStack2);
            for(int i =0;i<nbtList.size();i++){
                NbtCompound nbtCompound = nbtList.getCompound(i);
                Identifier identifier2 = EnchantmentHelper.getIdFromNbt(nbtCompound);
                assert identifier2 != null;
                if(identifier2.toString().contains("radiance")
                        || identifier2.toString().contains("radiance_shot")
                        || identifier2.toString().contains("prospector")
                        || identifier2.toString().contains("heal_allies")
                        || identifier2.toString().contains("lucky_explorer")
                        || identifier2.toString().contains("leeching")
                        || identifier2.toString().contains("reckless")
                        || identifier2.toString().contains("anima_conduit")
                        || identifier2.toString().contains("death_barter")
                        || identifier2.toString().contains("rushdown")//==
                        || identifier2.toString().contains("tempo_theft")
                        || identifier2.toString().contains("beast_boss")
                        || identifier2.toString().contains("beast_burst")
                        || identifier2.toString().contains("beast_surge")
                        || identifier2.toString().contains("mcda:swiftfooted")
                        || identifier2.toString().contains("mcdw:refreshment")
                        || identifier2.toString().contains("mcdw:void_shot")
                        || identifier2.toString().contains("mcdw:void_strike")
                        || identifier2.toString().contains("mcda:cowardice")
//                        || identifier2.toString().contains("minecraft:protection")//test===============
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.getItem() instanceof EnchantedBookItem){
            NbtList nbtList = EnchantedBookItem.getEnchantmentNbt(itemStack2);
            for(int i =0;i<nbtList.size();i++){
                NbtCompound nbtCompound = nbtList.getCompound(i);
                Identifier identifier2 = EnchantmentHelper.getIdFromNbt(nbtCompound);
                assert identifier2 != null;
                if(identifier2.toString().contains("radiance")
                        || identifier2.toString().contains("radiance_shot")
                        || identifier2.toString().contains("prospector")
                        || identifier2.toString().contains("heal_allies")
                        || identifier2.toString().contains("lucky_explorer")
                        || identifier2.toString().contains("leeching")
                        || identifier2.toString().contains("reckless")
                        || identifier2.toString().contains("anima_conduit")
                        || identifier2.toString().contains("death_barter")
                        || identifier2.toString().contains("rushdown")//==
                        || identifier2.toString().contains("tempo_theft")
                        || identifier2.toString().contains("beast_boss")
                        || identifier2.toString().contains("beast_burst")
                        || identifier2.toString().contains("beast_surge")
                        || identifier2.toString().contains("mcda:swiftfooted")
                        || identifier2.toString().contains("mcdw:refreshment")
                        || identifier2.toString().contains("mcdw:void_shot")
                        || identifier2.toString().contains("mcdw:void_strike")
                        || identifier2.toString().contains("mcda:cowardice")
//                        || identifier2.toString().contains("minecraft:protection")//test===============
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.getItem() instanceof EnchantedBookItem){
            NbtList nbtList = EnchantedBookItem.getEnchantmentNbt(itemStack2);
            for(int i =0;i<nbtList.size();i++){
                NbtCompound nbtCompound = nbtList.getCompound(i);
                Identifier identifier2 = EnchantmentHelper.getIdFromNbt(nbtCompound);
                assert identifier2 != null;
                if(identifier2.toString().contains("radiance")
                        || identifier2.toString().contains("radiance_shot")
                        || identifier2.toString().contains("prospector")
                        || identifier2.toString().contains("heal_allies")
                        || identifier2.toString().contains("lucky_explorer")
                        || identifier2.toString().contains("leeching")
                        || identifier2.toString().contains("reckless")
                        || identifier2.toString().contains("anima_conduit")
                        || identifier2.toString().contains("death_barter")
                        || identifier2.toString().contains("rushdown")//==
                        || identifier2.toString().contains("tempo_theft")
                        || identifier2.toString().contains("beast_boss")
                        || identifier2.toString().contains("beast_burst")
                        || identifier2.toString().contains("beast_surge")
                        || identifier2.toString().contains("mcda:swiftfooted")
                        || identifier2.toString().contains("mcdw:refreshment")
                        || identifier2.toString().contains("mcdw:void_shot")
                        || identifier2.toString().contains("mcdw:void_strike")
                        || identifier2.toString().contains("mcda:cowardice")
//                        || identifier2.toString().contains("minecraft:protection")//test===============
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.getItem() instanceof EnchantedBookItem){
            NbtList nbtList = EnchantedBookItem.getEnchantmentNbt(itemStack2);
            for(int i =0;i<nbtList.size();i++){
                NbtCompound nbtCompound = nbtList.getCompound(i);
                Identifier identifier2 = EnchantmentHelper.getIdFromNbt(nbtCompound);
                assert identifier2 != null;
                if(identifier2.toString().contains("radiance")
                        || identifier2.toString().contains("radiance_shot")
                        || identifier2.toString().contains("prospector")
                        || identifier2.toString().contains("heal_allies")
                        || identifier2.toString().contains("lucky_explorer")
                        || identifier2.toString().contains("leeching")
                        || identifier2.toString().contains("reckless")
                        || identifier2.toString().contains("anima_conduit")
                        || identifier2.toString().contains("death_barter")
                        || identifier2.toString().contains("rushdown")//==
                        || identifier2.toString().contains("tempo_theft")
                        || identifier2.toString().contains("beast_boss")
                        || identifier2.toString().contains("beast_burst")
                        || identifier2.toString().contains("beast_surge")
                        || identifier2.toString().contains("mcda:swiftfooted")
                        || identifier2.toString().contains("mcdw:refreshment")
                        || identifier2.toString().contains("mcdw:void_shot")
                        || identifier2.toString().contains("mcdw:void_strike")
                        || identifier2.toString().contains("mcda:cowardice")
//                        || identifier2.toString().contains("minecraft:protection")//test===============
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.getItem() instanceof EnchantedBookItem){
            NbtList nbtList = EnchantedBookItem.getEnchantmentNbt(itemStack2);
            for(int i =0;i<nbtList.size();i++){
                NbtCompound nbtCompound = nbtList.getCompound(i);
                Identifier identifier2 = EnchantmentHelper.getIdFromNbt(nbtCompound);
                assert identifier2 != null;
                if(identifier2.toString().contains("radiance")
                        || identifier2.toString().contains("radiance_shot")
                        || identifier2.toString().contains("prospector")
                        || identifier2.toString().contains("heal_allies")
                        || identifier2.toString().contains("lucky_explorer")
                        || identifier2.toString().contains("leeching")
                        || identifier2.toString().contains("reckless")
                        || identifier2.toString().contains("anima_conduit")
                        || identifier2.toString().contains("death_barter")
                        || identifier2.toString().contains("rushdown")//==
                        || identifier2.toString().contains("tempo_theft")
                        || identifier2.toString().contains("beast_boss")
                        || identifier2.toString().contains("beast_burst")
                        || identifier2.toString().contains("beast_surge")
                        || identifier2.toString().contains("mcda:swiftfooted")
                        || identifier2.toString().contains("mcdw:refreshment")
                        || identifier2.toString().contains("mcdw:void_shot")
                        || identifier2.toString().contains("mcdw:void_strike")
                        || identifier2.toString().contains("mcda:cowardice")
//                        || identifier2.toString().contains("minecraft:protection")//test===============
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.getItem() instanceof EnchantedBookItem){
            NbtList nbtList = EnchantedBookItem.getEnchantmentNbt(itemStack2);
            for(int i =0;i<nbtList.size();i++){
                NbtCompound nbtCompound = nbtList.getCompound(i);
                Identifier identifier2 = EnchantmentHelper.getIdFromNbt(nbtCompound);
                assert identifier2 != null;
                if(identifier2.toString().contains("radiance")
                        || identifier2.toString().contains("radiance_shot")
                        || identifier2.toString().contains("prospector")
                        || identifier2.toString().contains("heal_allies")
                        || identifier2.toString().contains("lucky_explorer")
                        || identifier2.toString().contains("leeching")
                        || identifier2.toString().contains("reckless")
                        || identifier2.toString().contains("anima_conduit")
                        || identifier2.toString().contains("death_barter")
                        || identifier2.toString().contains("rushdown")//==
                        || identifier2.toString().contains("tempo_theft")
                        || identifier2.toString().contains("beast_boss")
                        || identifier2.toString().contains("beast_burst")
                        || identifier2.toString().contains("beast_surge")
                        || identifier2.toString().contains("mcda:swiftfooted")
                        || identifier2.toString().contains("mcdw:refreshment")
                        || identifier2.toString().contains("mcdw:void_shot")
                        || identifier2.toString().contains("mcdw:void_strike")
                        || identifier2.toString().contains("mcda:cowardice")
//                        || identifier2.toString().contains("minecraft:protection")//test===============
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.getItem() instanceof EnchantedBookItem){
            NbtList nbtList = EnchantedBookItem.getEnchantmentNbt(itemStack2);
            for(int i =0;i<nbtList.size();i++){
                NbtCompound nbtCompound = nbtList.getCompound(i);
                Identifier identifier2 = EnchantmentHelper.getIdFromNbt(nbtCompound);
                assert identifier2 != null;
                if(identifier2.toString().contains("radiance")
                        || identifier2.toString().contains("radiance_shot")
                        || identifier2.toString().contains("prospector")
                        || identifier2.toString().contains("heal_allies")
                        || identifier2.toString().contains("lucky_explorer")
                        || identifier2.toString().contains("leeching")
                        || identifier2.toString().contains("reckless")
                        || identifier2.toString().contains("anima_conduit")
                        || identifier2.toString().contains("death_barter")
                        || identifier2.toString().contains("rushdown")//==
                        || identifier2.toString().contains("tempo_theft")
                        || identifier2.toString().contains("beast_boss")
                        || identifier2.toString().contains("beast_burst")
                        || identifier2.toString().contains("beast_surge")
                        || identifier2.toString().contains("mcda:swiftfooted")
                        || identifier2.toString().contains("mcdw:refreshment")
                        || identifier2.toString().contains("mcdw:void_shot")
                        || identifier2.toString().contains("mcdw:void_strike")
                        || identifier2.toString().contains("mcda:cowardice")
//                        || identifier2.toString().contains("minecraft:protection")//test===============
                ){
                    b2=false;
                }
            }
        }
        if(itemStack2.getItem() instanceof EnchantedBookItem){
            NbtList nbtList = EnchantedBookItem.getEnchantmentNbt(itemStack2);
            for(int i =0;i<nbtList.size();i++){
                NbtCompound nbtCompound = nbtList.getCompound(i);
                Identifier identifier2 = EnchantmentHelper.getIdFromNbt(nbtCompound);
                assert identifier2 != null;
                if(identifier2.toString().contains("radiance")
                        || identifier2.toString().contains("radiance_shot")
                        || identifier2.toString().contains("prospector")
                        || identifier2.toString().contains("heal_allies")
                        || identifier2.toString().contains("lucky_explorer")
                        || identifier2.toString().contains("leeching")
                        || identifier2.toString().contains("reckless")
                        || identifier2.toString().contains("anima_conduit")
                        || identifier2.toString().contains("death_barter")
                        || identifier2.toString().contains("rushdown")//==
                        || identifier2.toString().contains("tempo_theft")
                        || identifier2.toString().contains("beast_boss")
                        || identifier2.toString().contains("beast_burst")
                        || identifier2.toString().contains("beast_surge")
                        || identifier2.toString().contains("mcda:swiftfooted")
                        || identifier2.toString().contains("mcdw:refreshment")
                        || identifier2.toString().contains("mcdw:void_shot")
                        || identifier2.toString().contains("mcdw:void_strike")
                        || identifier2.toString().contains("mcda:cowardice")
//                        || identifier2.toString().contains("minecraft:protection")//test===============
                ){
                    b2=false;
                }
            }
        }
//        this.player.sendMessage(Text.translatable("b1========"+String.valueOf(b1)));
//        this.player.sendMessage(Text.translatable("b2========"+String.valueOf(b2)));
//        this.player.sendMessage(Text.translatable("item1========"+itemStack1.getName().toString()));
//        this.player.sendMessage(Text.translatable("item2========"+itemStack2.getName().toString()));

        if(!b1 || !b2){
//            this.player.sendMessage(Text.translatable("333333333========"+String.valueOf(b1)));
//            this.player.sendMessage(Text.translatable("666666666666========"+String.valueOf(b2)));
            property.set(Integer.MAX_VALUE);
            return Integer.MAX_VALUE;
        }else{
            if (property.get() >= 20 ) {//&& property.get() < Integer.MAX_VALUE
                property.set(20);
                return 20;
            }
        }



//        if(!b1 || !b2){
////            this.player.sendMessage(Text.translatable("333333333========"+String.valueOf(b1)));
////            this.player.sendMessage(Text.translatable("666666666666========"+String.valueOf(b2)));
//            property.set(99999999);
//            return 99999999;
//        }else {
//            if (property.get() >= 40 ) {
//                property.set(35);
//                return 35;
//            }
//        }
        return property.get();
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;canCombine(Lnet/minecraft/enchantment/Enchantment;)Z"), method = "updateResult")
    private boolean canCombine(Enchantment enchantment, Enchantment other) {
        return canCombine0(enchantment, other);
    }

    @Unique
    private boolean canCombine0(Enchantment enchantment1, Enchantment enchantment2) {
        if ((enchantment1 instanceof InfinityEnchantment && enchantment2 instanceof MendingEnchantment) || (enchantment2 instanceof InfinityEnchantment && enchantment1 instanceof MendingEnchantment)) {
            return true;
        } else if ((enchantment1 instanceof MultishotEnchantment && enchantment2 instanceof PiercingEnchantment) || (enchantment2 instanceof MultishotEnchantment && enchantment1 instanceof PiercingEnchantment)) {
            return true;
        }
        //禁用附魔无法结合。radiance治愈光辉，radiance_shot治愈光辉射击，prospector探矿者,heal_allies舍己为人，lucky_explorer幸运探险家
        else if( Registries.ENCHANTMENT.getId(enchantment1).toString().contains("radiance") || Registries.ENCHANTMENT.getId(enchantment2).toString().contains("radiance")){
            return false;
        } else if (Registries.ENCHANTMENT.getId(enchantment1).toString().contains("radiance_shot") || Registries.ENCHANTMENT.getId(enchantment2).toString().contains("radiance_shot")){
            return false;
        } else if (Registries.ENCHANTMENT.getId(enchantment1).toString().contains("prospector") || Registries.ENCHANTMENT.getId(enchantment2).toString().contains("prospector")){
            return false;
        } else if (Registries.ENCHANTMENT.getId(enchantment1).toString().contains("heal_allies") || Registries.ENCHANTMENT.getId(enchantment2).toString().contains("heal_allies")){
            return false;
        } else if (Registries.ENCHANTMENT.getId(enchantment1).toString().contains("lucky_explorer") || Registries.ENCHANTMENT.getId(enchantment2).toString().contains("lucky_explorer")){
            return false;
        }else if (Registries.ENCHANTMENT.getId(enchantment1).toString().contains("leeching") || Registries.ENCHANTMENT.getId(enchantment2).toString().contains("leeching")){
            return false;
        }else if (Registries.ENCHANTMENT.getId(enchantment1).toString().contains("reckless") || Registries.ENCHANTMENT.getId(enchantment2).toString().contains("reckless")){
            return false;
        }else if (Registries.ENCHANTMENT.getId(enchantment1).toString().contains("anima_conduit") || Registries.ENCHANTMENT.getId(enchantment2).toString().contains("anima_conduit")){
            return false;
        }else if (Registries.ENCHANTMENT.getId(enchantment1).toString().contains("death_barter") || Registries.ENCHANTMENT.getId(enchantment2).toString().contains("death_barter")){
            return false;
        }else if (Registries.ENCHANTMENT.getId(enchantment1).toString().contains("rushdown") || Registries.ENCHANTMENT.getId(enchantment2).toString().contains("rushdown")){
            return false;
        }else if (Registries.ENCHANTMENT.getId(enchantment1).toString().contains("tempo_theft") || Registries.ENCHANTMENT.getId(enchantment2).toString().contains("tempo_theft")){
            return false;
        }else if (Registries.ENCHANTMENT.getId(enchantment1).toString().contains("beast_boss") || Registries.ENCHANTMENT.getId(enchantment2).toString().contains("beast_boss")){
            return false;
        }else if (Registries.ENCHANTMENT.getId(enchantment1).toString().contains("beast_burst") || Registries.ENCHANTMENT.getId(enchantment2).toString().contains("beast_burst")){
            return false;
        }else if (Registries.ENCHANTMENT.getId(enchantment1).toString().contains("beast_surge") || Registries.ENCHANTMENT.getId(enchantment2).toString().contains("beast_surge")){
            return false;
        }else if (Registries.ENCHANTMENT.getId(enchantment1).toString().contains("mcda:swiftfooted") || Registries.ENCHANTMENT.getId(enchantment2).toString().contains("mcda:swiftfooted")){
            return false;
        }else if (Registries.ENCHANTMENT.getId(enchantment1).toString().contains("mcdw:refreshment") || Registries.ENCHANTMENT.getId(enchantment2).toString().contains("mcdw:refreshment")){
            return false;
        }else if (Registries.ENCHANTMENT.getId(enchantment1).toString().contains("mcdw:void_shot") || Registries.ENCHANTMENT.getId(enchantment2).toString().contains("mcdw:void_shot")){
            return false;
        }else if (Registries.ENCHANTMENT.getId(enchantment1).toString().contains("mcdw:void_strike") || Registries.ENCHANTMENT.getId(enchantment2).toString().contains("mcdw:void_strike")){
            return false;
        }else if (Registries.ENCHANTMENT.getId(enchantment1).toString().contains("mcda:cowardice") || Registries.ENCHANTMENT.getId(enchantment2).toString().contains("mcda:cowardice")){
            return false;
        }
//        else if (Registries.ENCHANTMENT.getId(enchantment1).toString().contains("minecraft:protection") || Registries.ENCHANTMENT.getId(enchantment2).toString().contains("minecraft:protection")){
////            this.player.sendMessage(Text.translatable("第二个物品"+Registries.ENCHANTMENT.getId(enchantment1).toString()));
////            this.player.sendMessage(Text.translatable("第一个物品"+Registries.ENCHANTMENT.getId(enchantment2).toString()));
//            return false;
//        }
        else {
            return enchantment1.canCombine(enchantment2);
        }
    }
}
