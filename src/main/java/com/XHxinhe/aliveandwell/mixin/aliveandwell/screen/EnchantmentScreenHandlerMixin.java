package com.XHxinhe.aliveandwell.mixin.aliveandwell.screen;

import com.XHxinhe.aliveandwell.registry.ItemInit;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.screen.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantmentScreenHandlerMixin extends ScreenHandler implements EnchantmentScreenHandlerAccessor{
    @Shadow @Final private Inventory inventory;
    @Final
    @Shadow public final int[] enchantmentPower = new int[3];

    @Shadow @Final private ScreenHandlerContext context;
    @Final
    @Shadow private final Property seed = Property.create();

    protected EnchantmentScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

//    @Inject(at=@At("INVOKE"), method="onButtonClick", cancellable = true)
//    private void onButtonClick(PlayerEntity player, int id, CallbackInfoReturnable<Boolean> ca) {
//        this.exLevel = player.experienceLevel;
//        this.progress = player.experienceProgress;
//    }

    @Shadow
    private List<EnchantmentLevelEntry> generateEnchantments(ItemStack stack, int slot, int level) {
        return null;
    }

    @Inject(at=@At("HEAD"), method="onButtonClick", cancellable = true)
    public void clickMenuButton(PlayerEntity player, int id, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemstack = ((EnchantmentScreenHandlerAccessor) (Object) this).getEnchantSlots().getStack(0);
        ItemStack itemstack1 = ((EnchantmentScreenHandlerAccessor) (Object) this).getEnchantSlots().getStack(1);
        int i = id + 1;
        if ((itemstack1.isEmpty() || itemstack1.getCount() < i) && !player.getAbilities().creativeMode) {
            cir.setReturnValue(false);
        } else if (this.enchantmentPower[id] <= 0 || itemstack.isEmpty() || (player.experienceLevel < i || player.experienceLevel < this.enchantmentPower[id]) && !player.getAbilities().creativeMode) {
            cir.setReturnValue(false);
        } else {
            this.context.run((world, pos) -> {
                if (itemstack.isOf(Items.GOLDEN_APPLE)) {
                    ItemStack itemStackFalse = Items.WOODEN_SWORD.getDefaultStack();
                    ItemStack itemStack3 = itemstack;
                    List<EnchantmentLevelEntry> list = this.generateEnchantments(itemStackFalse, id, this.enchantmentPower[id]);
                    assert list != null;
                    if (!list.isEmpty()) {
                        //消耗经验等级
                        player.applyEnchantmentCosts(itemStack3, this.enchantmentPower[id]);
                        itemStack3 = new ItemStack(Items.ENCHANTED_GOLDEN_APPLE);

                        if (!player.getAbilities().creativeMode) {
                            //消耗青金石
//                            itemStack1.shrink(i);
                            itemstack1.decrement(0);//不消耗
                            if (itemstack1.isEmpty()) {
                                ((EnchantmentScreenHandlerAccessor) (Object) this).getEnchantSlots().setStack(1, ItemStack.EMPTY);
                            }
                        }

                        ((EnchantmentScreenHandlerAccessor) (Object) this).getEnchantSlots().setStack(0, itemStack3);
                        player.incrementStat(Stats.ENCHANT_ITEM);
                        if (player instanceof ServerPlayerEntity) {
                            Criteria.ENCHANTED_ITEM.trigger((ServerPlayerEntity) player, itemStack3, i);
                        }
                        ((EnchantmentScreenHandlerAccessor) (Object) this).getEnchantSlots().markDirty();
                        this.seed.set(player.getEnchantmentTableSeed());
                        this.onContentChanged(((EnchantmentScreenHandlerAccessor) (Object) this).getEnchantSlots());
                        world.playSound(null, (BlockPos) pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0f, world.random.nextFloat() * 0.1f + 0.9f);
                    }
                }

                if (itemstack.isOf(Items.GOLDEN_CARROT)) {
                    ItemStack itemStackFalse = Items.WOODEN_SWORD.getDefaultStack();
                    ItemStack itemStack3 = itemstack;
                    List<EnchantmentLevelEntry> list = this.generateEnchantments(itemStackFalse, id, this.enchantmentPower[id]);
                    assert list != null;
                    if (!list.isEmpty()) {
                        //消耗经验等级
                        player.applyEnchantmentCosts(itemStack3, this.enchantmentPower[id]);
                        itemStack3 = new ItemStack(ItemInit.ENCHANTED_GOLDEN_CARROT);

                        if (!player.getAbilities().creativeMode) {
                            //消耗青金石
//                            itemStack1.shrink(i);
                            itemstack1.decrement(0);//不消耗
                            if (itemstack1.isEmpty()) {
                                ((EnchantmentScreenHandlerAccessor) (Object) this).getEnchantSlots().setStack(1, ItemStack.EMPTY);
                            }
                        }

                        ((EnchantmentScreenHandlerAccessor) (Object) this).getEnchantSlots().setStack(0, itemStack3);
                        player.incrementStat(Stats.ENCHANT_ITEM);
                        if (player instanceof ServerPlayerEntity) {
                            Criteria.ENCHANTED_ITEM.trigger((ServerPlayerEntity) player, itemStack3, i);
                        }
                        ((EnchantmentScreenHandlerAccessor) (Object) this).getEnchantSlots().markDirty();
                        this.seed.set(player.getEnchantmentTableSeed());
                        this.onContentChanged(((EnchantmentScreenHandlerAccessor) (Object) this).getEnchantSlots());
                        world.playSound(null, (BlockPos) pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0f, world.random.nextFloat() * 0.1f + 0.9f);
                    }
                }

                ItemStack itemstack2 = itemstack;
                List<EnchantmentLevelEntry> list = this.generateEnchantments(itemstack, id, this.enchantmentPower[id]);
                assert list != null;
                if (!list.isEmpty()) {
                    player.applyEnchantmentCosts(itemstack, this.enchantmentPower[id]);
                    boolean flag = itemstack.isOf(Items.BOOK);
                    if (flag) {
                        itemstack2 = new ItemStack(Items.ENCHANTED_BOOK);
                        NbtCompound compoundtag = itemstack.getNbt();
                        if (compoundtag != null) {
                            itemstack2.setNbt(compoundtag.copy());
                        }

                        ((EnchantmentScreenHandlerAccessor) (Object) this).getEnchantSlots().setStack(0, itemstack2);
                    }

                    for (EnchantmentLevelEntry enchantmentinstance : list) {
                        if (flag) {
                            EnchantedBookItem.addEnchantment(itemstack2, enchantmentinstance);
                        } else {
                            itemstack2.addEnchantment(enchantmentinstance.enchantment, enchantmentinstance.level);
                        }
                    }

                    if (!player.getAbilities().creativeMode) {
                        itemstack1.decrement(i);
                        if (itemstack1.isEmpty()) {
                            ((EnchantmentScreenHandlerAccessor) (Object) this).getEnchantSlots().setStack(1, ItemStack.EMPTY);
                        }
                    }

                    player.incrementStat(Stats.ENCHANT_ITEM);
                    if (player instanceof ServerPlayerEntity) {
                        Criteria.ENCHANTED_ITEM.trigger((ServerPlayerEntity)player, itemstack2, i);
                    }

                    ((EnchantmentScreenHandlerAccessor) (Object) this).getEnchantSlots().markDirty();
                    this.seed.set(player.getEnchantmentTableSeed());
                    this.onContentChanged(((EnchantmentScreenHandlerAccessor) (Object) this).getEnchantSlots());
                    world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
                }

            });
            cir.setReturnValue(true);
        }
    }

    @Inject(at=@At("HEAD"), method="onButtonClick", cancellable = true)
    public void onButtonClick21(PlayerEntity player, int id, CallbackInfoReturnable<Boolean> ca) {
        ItemStack itemStack = this.inventory.getStack(0);
        if(itemStack.getName().toString().contains("未获取到任何附魔，请关闭gui后再次附魔")){
            ca.setReturnValue(false);
        }
    }
    
    @Inject(at=@At("RETURN"), method="generateEnchantments", cancellable = true)
    private void generateEnchantments(ItemStack stack, int slot, int level,CallbackInfoReturnable<List<EnchantmentLevelEntry>> ca) {
        ((EnchantmentScreenHandlerAccessor)(Object)(this)).getRandom().setSeed((long)(((EnchantmentScreenHandlerAccessor)(Object)(this)).getSeed().get()+ slot));
        List<EnchantmentLevelEntry> list = EnchantmentHelper.generateEnchantments(((EnchantmentScreenHandlerAccessor)(Object)(this)).getRandom(), stack, level, false);
        if(!list.isEmpty()){
            for(int i=0;i<list.size();i++){
                if(Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:radiance")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:radiance_shot")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:prospector")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:heal_allies")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:lucky_explorer")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:leeching")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:reckless")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:anima_conduit")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:death_barter")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:rushdown")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:tempo_theft")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_boss")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_burst")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_surge")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:swiftfooted")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:refreshment")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:void_shot")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:void_strike")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:cowardice")
//                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("minecraft:protection")//test=================
                ){
                    list.remove(i);
                }
            }
        }

        if(!list.isEmpty()){
            for(int i=0;i<list.size();i++){
                if(Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:radiance")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:radiance_shot")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:prospector")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:heal_allies")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:lucky_explorer")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:leeching")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:reckless")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:anima_conduit")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:death_barter")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:rushdown")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:tempo_theft")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_boss")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_burst")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_surge")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:swiftfooted")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:refreshment")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:void_shot")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:void_strike")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:cowardice")
//                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("minecraft:protection")//test=================
                ){
                    list.remove(i);
                }
            }
        }
        if(!list.isEmpty()){
            for(int i=0;i<list.size();i++){
                if(Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:radiance")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:radiance_shot")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:prospector")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:heal_allies")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:lucky_explorer")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:leeching")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:reckless")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:anima_conduit")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:death_barter")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:rushdown")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:tempo_theft")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_boss")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_burst")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_surge")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:swiftfooted")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:refreshment")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:void_shot")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:void_strike")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:cowardice")
//                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("minecraft:protection")//test=================
                ){
                    list.remove(i);
                }
            }
        }
        if(!list.isEmpty()){
            for(int i=0;i<list.size();i++){
                if(Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:radiance")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:radiance_shot")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:prospector")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:heal_allies")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:lucky_explorer")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:leeching")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:reckless")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:anima_conduit")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:death_barter")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:rushdown")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:tempo_theft")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_boss")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_burst")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_surge")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:swiftfooted")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:refreshment")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:void_shot")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:void_strike")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:cowardice")
//                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("minecraft:protection")//test=================
                ){
                    list.remove(i);
                }
            }
        }
        if(!list.isEmpty()){
            for(int i=0;i<list.size();i++){
                if(Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:radiance")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:radiance_shot")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:prospector")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:heal_allies")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:lucky_explorer")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:leeching")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:reckless")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:anima_conduit")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:death_barter")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:rushdown")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:tempo_theft")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_boss")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_burst")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_surge")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:swiftfooted")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:refreshment")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:void_shot")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:void_strike")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:cowardice")
//                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("minecraft:protection")//test=================
                ){
                    list.remove(i);
                }
            }
        }
        if(!list.isEmpty()){
            for(int i=0;i<list.size();i++){
                if(Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:radiance")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:radiance_shot")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:prospector")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:heal_allies")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:lucky_explorer")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:leeching")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:reckless")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:anima_conduit")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:death_barter")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:rushdown")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:tempo_theft")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_boss")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_burst")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_surge")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:swiftfooted")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:refreshment")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:void_shot")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:void_strike")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:cowardice")
//                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("minecraft:protection")//test=================
                ){
                    list.remove(i);
                }
            }
        }
        if(!list.isEmpty()){
            for(int i=0;i<list.size();i++){
                if(Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:radiance")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:radiance_shot")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:prospector")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:heal_allies")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:lucky_explorer")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:leeching")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:reckless")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:anima_conduit")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:death_barter")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:rushdown")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:tempo_theft")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_boss")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_burst")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_surge")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:swiftfooted")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:refreshment")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:void_shot")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:void_strike")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:cowardice")
//                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("minecraft:protection")//test=================
                ){
                    list.remove(i);
                }
            }
        }
        if(!list.isEmpty()){
            for(int i=0;i<list.size();i++){
                if(Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:radiance")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:radiance_shot")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:prospector")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:heal_allies")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:lucky_explorer")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:leeching")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:reckless")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:anima_conduit")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:death_barter")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:rushdown")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:tempo_theft")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_boss")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_burst")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_surge")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:swiftfooted")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:refreshment")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:void_shot")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:void_strike")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:cowardice")
//                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("minecraft:protection")//test=================
                ){
                    list.remove(i);
                }
            }
        }
        if(!list.isEmpty()){
            for(int i=0;i<list.size();i++){
                if(Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:radiance")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:radiance_shot")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:prospector")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:heal_allies")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:lucky_explorer")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:leeching")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:reckless")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:anima_conduit")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:death_barter")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:rushdown")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:tempo_theft")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_boss")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_burst")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdar:beast_surge")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:swiftfooted")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:refreshment")//==
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:void_shot")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcdw:void_strike")
                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("mcda:cowardice")
//                        || Registries.ENCHANTMENT.getId(list.get(i).enchantment).toString().contains("minecraft:protection")//test=================
                ){
                    list.remove(i);
                }
            }
        }

        if (stack.isOf(Items.BOOK) && list.size() > 1) {
            list.remove(((EnchantmentScreenHandlerAccessor)(Object)(this)).getRandom().nextInt(list.size()));
        }


        if(list.size() == 0){
            if(!(stack.isOf(Items.GOLDEN_APPLE) || stack.isOf(Items.GOLDEN_CARROT))){
                stack.setCustomName(Text.translatable("未获取到任何附魔，请关闭gui后再次附魔").formatted(Formatting.DARK_RED));
            }
        }

        if(!list.isEmpty()){
            if(!(stack.isOf(Items.GOLDEN_APPLE) || stack.isOf(Items.GOLDEN_CARROT))){
                if(stack.hasCustomName()){
                    if(stack.getName().toString().contains("未获取到任何附魔，请关闭gui后再次附魔")){
                        stack.removeCustomName();
                    }
                }
            }
        }
        ca.setReturnValue(list);
    }

}
