package com.XHxinhe.aliveandwell.mixin.aliveandwell.villager;

import com.XHxinhe.aliveandwell.util.config.CommonConfig;
import com.XHxinhe.aliveandwell.util.TradeOfferSelf;
import com.XHxinhe.aliveandwell.util.VillagerNbt;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.InteractionObserver;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends MerchantEntity implements InteractionObserver, VillagerDataContainer, VillagerNbt {
//    @Unique
//    @Final
//    private static final String ALIVEANDWELL_NBT = "aliveandwell_nbt_villager";//总的nbt
//    @Mutable
//    @Unique
//    private NbtCompound customVillagerNbt = new NbtCompound();//分支nbt
//    @Unique
//    private boolean canSpawn ;
//    private int canSpawnTime ;
//    @Unique
//    private final RandomManager serverState = RandomManager.getServerState(Objects.requireNonNull(this.getWorld().getServer()));

    public VillagerEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
    }


//    @Inject(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;Lnet/minecraft/village/VillagerType;)V", at = @At(value = "TAIL"))
//    private void initMixin(CallbackInfo info) {
//
//    }

    @Inject(at=@At("HEAD"), method="interactMob",cancellable = true)
    public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> ca) {
        //地狱堡垒
        if(player instanceof ServerPlayerEntity player1){
            if(CommonConfig.c){
                Advancement advancement15 = player1.server.getAdvancementLoader().get(Identifier.tryParse("minecraft:nether/obtain_ancient_debris"));
                if(advancement15 != null){
                    AdvancementProgress advancementProgress = player1.getAdvancementTracker().getProgress(advancement15);
                    if(!advancementProgress.isDone()){
                        this.sayNo();
                        player.sendMessage(Text.translatable("aliveandwell.villager.info1").formatted(Formatting.YELLOW));
                        ca.setReturnValue(ActionResult.success(this.getWorld().isClient));
                    }
                }
            }else {
                this.sayNo();
                player.sendMessage(Text.translatable("aliveandwell.villager.info2").formatted(Formatting.YELLOW));
                ca.setReturnValue(ActionResult.success(this.getWorld().isClient));
            }
        }
    }

    @Shadow
    private void sayNo() {
        this.setHeadRollingTimeLeft(40);
        if (!this.getWorld().isClient()) {
            this.playSound(SoundEvents.ENTITY_VILLAGER_NO, this.getSoundVolume(), this.getSoundPitch());
        }

    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void fillRecipes() {
        VillagerData villagerData = this.getVillagerData();
        Int2ObjectMap int2ObjectMap = (Int2ObjectMap) TradeOfferSelf.PROFESSION_TO_LEVELED_TRADE.get(villagerData.getProfession());
        if (int2ObjectMap != null && !int2ObjectMap.isEmpty()) {
            TradeOffers.Factory[] factorys = (TradeOffers.Factory[])int2ObjectMap.get(villagerData.getLevel());
            if (factorys != null) {
                TradeOfferList tradeOfferList = this.getOffers();
                this.fillRecipesFromPool(tradeOfferList, factorys, 2);
            }
        }
    }

//    //写入nbt
//    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
//    private void writeToNbt(NbtCompound nbt, CallbackInfo ci) {
//        nbt.put(ALIVEANDWELL_NBT, customVillagerNbt);
//    }
//
//    //读取nbt
//    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
//    private void readFromNbt(NbtCompound tag, CallbackInfo ci) {
//        customVillagerNbt = tag.getCompound(ALIVEANDWELL_NBT);
//    }
//
//    //判断分支nbt的键
//    @Override
//    public boolean villager$contains(String key) {
//        return customVillagerNbt.contains(key);
//    }
//    //获取分支nbt的键
//    @Override
//    public boolean villager$getBoolean(String key) {
//        return customVillagerNbt.getBoolean(key);
//    }
//    //存取一个分支nbt
//    @Override
//    public void villager$putBoolean() {
//        customVillagerNbt.putBoolean("canSpawnVillager", canSpawn);
//    }
//
//    @Override
//    public int villager$getInt(String key) {
//        return customVillagerNbt.getInt(key);
//    }
//    //存取一个分支nbt
//    @Override
//    public void villager$putInt() {
//        customVillagerNbt.putInt("canSpawnTime", canSpawnTime);
//    }
//    @Override
//    public void villager$removeString(String key) {
//        customVillagerNbt.remove(key);
//    }

}
