package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import com.XHxinhe.aliveandwell.block.randompos.RandomManager;
import com.XHxinhe.aliveandwell.util.config.CommonConfig;
import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.registry.ItemInit;
import com.XHxinhe.aliveandwell.util.ReachDistance;
import com.mojang.authlib.GameProfile;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    @Shadow @Final public MinecraftServer server;
    @Shadow @Final private ServerStatHandler statHandler;
    //    @Unique
//    private final int xpCostTime=CommonConfig.xptime;
    @Unique
    public boolean clearCheat ;
    @Unique
    public boolean can_end ;
    @Unique
    public boolean underworld;
    @Unique
    public boolean obtain_ancient_debris;
    @Unique
    public boolean can_end1;
    @Unique
    public boolean can_end2;
    @Unique
    public boolean can_end3;
    @Unique
    public boolean can_end4;
    @Unique
    public boolean can_end5;
    @Unique
    public boolean can_end6;
    @Unique
    public boolean can_end7;
    @Unique
    public boolean can_end8;
    @Unique
    public boolean can_end9;
    @Unique
    public boolean can_end10;
    @Unique
    public boolean can_end11;
    @Unique
    public boolean can_end12;
    @Unique
    public boolean can_end13;
    @Unique
    public boolean can_end14;
    @Unique
    public boolean can_end15;
    @Unique
    public boolean can_end16;
    @Unique
    public boolean can_end17;
    @Unique
    public boolean can_end18;
    @Unique
    public boolean can_end19;
    @Unique
    public boolean can_end20;
    @Unique
    public boolean ingot_adamantium;
    @Unique
    public boolean adamantium_core;
    @Unique
    public boolean argent_pickaxe;
    @Unique
    public boolean quantum_upgrade;
    @Unique
    public boolean argent_core;
    @Unique
    public boolean isCheated;
    @Unique
    public boolean flag;
    @Unique
    public int timeSpectator;

    @Unique
    public int timeCheat;
    @Unique
    public boolean cheatItem;
    @Unique
    public boolean ingot_mithril;
    @Unique
    public boolean mithril_core;
    @Unique
    public boolean quantum_sword;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, ClientConnection connection) {
        super(world, pos, yaw, gameProfile);
    }

    @Shadow public abstract void sendMessage(Text message, boolean actionBar);

    @Shadow public abstract boolean changeGameMode(GameMode gameMode);

    @Shadow public abstract void sendMessage(Text message);

    @Shadow public ServerPlayNetworkHandler networkHandler;

//    @Shadow public abstract boolean isSpectator();

    @Shadow public abstract ServerStatHandler getStatHandler();

//    @Inject(method = "<init>", at = @At(value = "TAIL"))
//    private void initMixin(MinecraftServer server, ServerWorld world, GameProfile profile, CallbackInfo ci) {
//        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) (Object) this;
//        serverPlayerEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)
//                .setBaseValue(Math.min(100, (int)Math.floor(experienceLevel / 5) * 2 + 6));
//         //Init stats - Can't send to client cause network hander is null -> onSpawn packet
//    }

    public void baseTick() {
        ReachDistance.setReachDistance((PlayerEntity) (Object)this);
//        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(Math.min(100, (int)Math.floor(experienceLevel / 5) * 2 + 6));
        double slow_speed = 0.8F;
        if(this.hungerManager.getFoodLevel()< 1){
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)).setBaseValue(0.10000000149011612D * slow_speed);
        }

        if(this.statHandler.getStat(Stats.CUSTOM.getOrCreateStat(Stats.DEATHS)) >= CommonConfig.deathCount){
            this.changeGameMode(GameMode.SPECTATOR);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION,60*20));
            if(!this.changeGameMode(GameMode.SPECTATOR)){
                this.changeGameMode(GameMode.SPECTATOR);
            }
        }

        super.baseTick();
//        changeSurvival();
        checkAdvancement0();
        checkAdvancement01();
        checkAdvancement1();
        checkAdvancement2();
        checkAdvancement3();
        checkAdvancement4();
        checkAdvancement5();
        checkAdvancement6();
        checkAdvancement7();
        checkAdvancement8();
        checkAdvancement9();
        checkAdvancement10();
        checkAdvancement11();
        checkAdvancement12();
        checkAdvancement13();
        checkAdvancement14();
        checkAdvancement15();
        checkAdvancement16();
        checkAdvancement17();
        checkAdvancement18();
        checkAdvancement19();
        checkAdvancement20();
        checkAdvancemendquantum_sword();
        checkAdvancemendQidian();
        checkAdvancementargent_core();
        checkAdvancementargent_pickaxe();
        checkAdvancementadamantium_core();
        checkAdvancementingot_adamantium();
        checkAdvancementmithril_core();
        checkAdvancementingot_mithril();
        addEffects();
//        cheatCreativeDisconnect();
        cheat();
    }

    //    @Unique
//    public void cheatCreativeDisconnect(){
//        if(cheatCreative()){
//            this.server.close();
//        }
//    }
    @Unique
    public void cheat(){
        if(!Objects.requireNonNull(this.getServer()).getPlayerManager().isOperator(this.getGameProfile())) {
            if(cheatItem && !clearCheat){
                cheatItem = false;
                clearCheat = true;
            }

            if (cheatItem() || cheatItem) {
                timeCheat++;
                if (timeCheat == 20) {
                    this.sendMessage(Text.translatable("aliveandwell.cheat.info1"));
                }
                if (timeCheat == 2 * 20) {
                    this.sendMessage(Text.translatable("aliveandwell.cheat.info2"));
                }
                if (timeCheat == 3 * 20) {
                    this.sendMessage(Text.translatable("aliveandwell.cheat.info3"));
                }
                if (timeCheat == 4 * 20) {
                    this.sendMessage(Text.translatable("aliveandwell.cheat.info4"));
                }
                if (timeCheat == 5 * 20) {
                    this.sendMessage(Text.translatable("aliveandwell.cheat.info5"));
                }
                if (timeCheat >= 6 * 20) {
                    timeCheat = 6 * 20;
                    this.networkHandler.disconnect(Text.translatable("aliveandwell.cheat.info"));
                }
            }
        }
    }

    @Unique
    public void addEffects(){
        if (this.getWorld().getDimensionKey().getValue().toString().contains("minecell")) {
            PlayerAdvancementTracker tracker = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker((ServerPlayerEntity) (Object)this);
            AdvancementProgress progress = null;

            //成就10：死亡细胞boss
            Advancement advancement = this.server.getAdvancementLoader().get(Identifier.tryParse("doom:argent"));
            if (advancement != null) {
                progress = tracker.getProgress(advancement);
            }
            if (progress != null) {
                if(!progress.isDone()){
                    if(this.hasStatusEffect(StatusEffects.NIGHT_VISION)){
                        this.removeStatusEffect(StatusEffects.NIGHT_VISION);
                    }
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS,20*5,5));
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS,20*5,3));
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER,20*5,2));
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA,20*5,2));
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE,20*5,2));
                }
            }
        }
    }

    @Inject(at = @At("TAIL"), method = "increaseStat")
    public void increaseStat(Stat<?> stat, int amount, CallbackInfo ca) {
        ItemStack stack = this.getOffHandStack();
        if(stack.getItem() == ItemInit.REBORN_STONE){
            //重生石有指定玩家，减少指定玩家死亡数
            if(stack.hasCustomName()) {
                if (this.server != null) {
                    List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
                    for (ServerPlayerEntity player : serverPlayers) {
                        if (player.getName().equals(stack.getName())) {
                            if (player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.DEATHS)) > 0) {
                                player.getStatHandler().increaseStat(player, Stats.CUSTOM.getOrCreateStat(Stats.DEATHS), -1);
                                stack.split(1);
                                player.sendMessage(((MutableText) (this.getName())).append(Text.translatable("aliveandwell.deathcount.for").append(player.getName()).append(Text.translatable("aliveandwell.deathcount.total")).append(Text.of(String.valueOf((CommonConfig.deathCount - ((ServerPlayerEntity) player).getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.DEATHS)))))).append(Text.translatable("aliveandwell.deathcount.count"))).formatted(Formatting.DARK_PURPLE));
                                this.sendMessage(((MutableText) (this.getName())).append(Text.translatable("aliveandwell.deathcount.for").append(player.getName()).append(Text.translatable("aliveandwell.deathcount.total")).append(Text.of(String.valueOf((CommonConfig.deathCount - ((ServerPlayerEntity) player).getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.DEATHS)))))).append(Text.translatable("aliveandwell.deathcount.count"))).formatted(Formatting.DARK_PURPLE));
                            }
                        } else {
                            player.getStatHandler().increaseStat(player, Stats.CUSTOM.getOrCreateStat(Stats.DEATHS), 0);
                        }
                    }
                }
            }else {
                if (this.statHandler.getStat(Stats.CUSTOM.getOrCreateStat(Stats.DEATHS)) > 0) {
                    this.statHandler.increaseStat(this, Stats.CUSTOM.getOrCreateStat(Stats.DEATHS), -1);
                    stack.split(1);
                    this.sendMessage(((MutableText) (this.getName())).append(Text.translatable("aliveandwell.deathcount.total")).append(Text.of(String.valueOf((CommonConfig.deathCount - this.statHandler.getStat(Stats.CUSTOM.getOrCreateStat(Stats.DEATHS)))))).append(Text.translatable("aliveandwell.deathcount.count")).formatted(Formatting.DARK_PURPLE));
                }
            }
        }
    }

//    @Redirect(method = "trySleep", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"))
//    public boolean startSleepInBed(List instance) {
//        instance.clear();
//        return true;
//    }

//    @Inject(at = @At("HEAD"), method = "sleep")
//    public void sleep(BlockPos pos, CallbackInfo ci) {
//        if(Back.cooldownTimeTooeasy <1800 || Homes.cooldownTimeTooeasy <1800 || Tpa.cooldownTimeTooeasy <1800){
//            sb_teleport_sb = true;
//        }
//    }

    @Inject(at = @At("TAIL"), method = "onDeath")
    public void onDeath(DamageSource damageSource,CallbackInfo ca) {
        if(this.server != null){
            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for(ServerPlayerEntity serverPlayer : serverPlayers){
                serverPlayer.sendMessage(((MutableText)this.getName()).append(Text.translatable("aliveandwell.deathcount.lost")).append(Text.of(String.valueOf((this.statHandler.getStat(Stats.CUSTOM.getOrCreateStat(Stats.DEATHS)))))).append(Text.of("/")).append(Text.of(String.valueOf(CommonConfig.deathCount))).append(Text.translatable("aliveandwell.deathcount.count")).formatted(Formatting.YELLOW));
            }
        }
    }

    @Unique
    public void checkAdvancement01(){
        if (server != null) {
            //成就1：凋零
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/underworld"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }

                if (progress != null && progress.isDone()) {////遍历服务器玩家：有人完成了进度
                    this.underworld = true;//记录完成进度，停止循环
                    break;
                }
            }
        }
    }

    @Unique
    public void checkAdvancement0(){
        if (server != null) {
            //成就1：凋零
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("minecraft:nether/find_fortress"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }

                if (progress != null && progress.isDone()) {////遍历服务器玩家：有人完成了进度
                    this.obtain_ancient_debris = true;//记录完成进度，停止循环
                    RandomManager.canSpawnVillager = true;
                    break;
                }
            }

            if(obtain_ancient_debris){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }

    //==================================================================================================================
//    @Unique
//    public void changeSurvival(){
//        if(!this.server.getPlayerManager().isOperator(((ServerPlayerEntity)(Object)this).getGameProfile())){
//            if(this.isSpectator() && this.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.DEATHS)) <= CommonConfig.deathCount){
//                this.changeGameMode(GameMode.SURVIVAL);
//            }
//        }
//    }

    //    @Unique
//    public boolean cheatCreative(){
//        if(this.isCreative() && (!this.server.getPlayerManager().isOperator(((ServerPlayerEntity)(Object)this).getGameProfile()) || !AliveAndWellMain.canCreative)){
//            cheatItem = true;
//            return true;
//        }
//        if(this.isSpectator() && (!this.server.getPlayerManager().isOperator(((ServerPlayerEntity)(Object)this).getGameProfile()) || !AliveAndWellMain.canCreative) && this.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.DEATHS)) <= CommonConfig.deathCount){
//            cheatItem = true;
//            return true;
//        }
//        return false;
//    }
    @Unique
    public boolean cheatItem(){
        if(!this.server.getPlayerManager().isOperator(((ServerPlayerEntity)(Object)this).getGameProfile())){
            ArrayList<String> reborn_stone_uuid = new ArrayList<>();
            for (ItemStack itemStack:this.getInventory().main){
                if(itemStack.getItem() == ItemInit.REBORN_STONE){
                    if(itemStack.hasNbt()){
                        if (itemStack.getNbt() != null && itemStack.getNbt().contains("aliveandwell_reborn_stone")) {
                            if (itemStack.getCount() > 1) {
                                cheatItem = true;
                                return true;
                            }else {
                                String stone_uuid = itemStack.getNbt().getString("aliveandwell_reborn_stone");
                                if(!reborn_stone_uuid.contains(stone_uuid)){
                                    reborn_stone_uuid.add(stone_uuid);
                                }else {
                                    cheatItem = true;
                                    return true;
                                }
                            }
                        }else {
                            cheatItem = true;
                            return true;
                        }
                    }else {
                        cheatItem = true;
                        return true;
                    }
                }

                if(AliveAndWellMain.day<32){
                    if(itemStack.getItem() == Items.DIAMOND){
                        if(itemStack.getCount() >64){
                            cheatItem = true;
                            return true;
                        }
                    }
                    if(itemStack.getItem() == Items.ENCHANTED_GOLDEN_APPLE){
                        if(itemStack.getCount() >64){
                            cheatItem = true;
                            return true;
                        }
                    }
                    if(this.getMaxHealth() >= 60){
                        cheatItem = true;
                        return true;
                    }
                }else if(AliveAndWellMain.day<65){
                    if(itemStack.getItem() == Items.DIAMOND){
                        if(itemStack.getCount() >10*64){
                            cheatItem = true;
                            return true;
                        }
                    }
                    if(itemStack.getItem() == Items.ENCHANTED_GOLDEN_APPLE){
                        if(itemStack.getCount() >10*64){
                            cheatItem = true;
                            return true;
                        }
                    }
                }
            }

            if(cheatArmorWearon()){
                cheatItem = true;
                return true;
            }
        }
        return false;
    }

    //秘银——秘银核心，秘银或秘银核心——秘银套，亚金镐——经验搞，艾德曼-艾德曼核心，艾德曼或艾德曼升级核心——艾德曼套，亚金核心——亚金套，奇点——量子剑和套装
    @Unique
    public boolean cheatArmorWearon(){
        //秘银——秘银核心
        if(!ingot_mithril){
            if(mithril_core){
                return true;
            }
        }
        //秘银或秘银核心——秘银套
        if(!ingot_mithril){
            if(wearMithrilArmor()>=1 || wearMithrilSword()){
                return true;
            }
        }
        //亚金镐——经验搞
        if(!argent_pickaxe){
            if(wearExpickaxe()){
                return true;
            }
        }
        //艾德曼-艾德曼核心
        if(!ingot_adamantium){
            if(adamantium_core){
                return true;
            }
        }
        //艾德曼或艾德曼升级核心——艾德曼套
        if(!ingot_adamantium || !adamantium_core){
            if(wearAdamanArmor()>=1 || wearAdamanSword()){
                return true;
            }
        }
        //亚金核心——亚金套
        if(!argent_core){
            if(wearDoomArmor()>=1 ){
                return true;
            }
        }
        //奇点——量子剑和套装
        if(!quantum_upgrade){
            if(wearLiangziArmor()>=1 || wearLiangziSword()){
                return true;
            }
        }
        return false;
    }
    @Unique
    public int wearMithrilArmor(){
        ItemStack head = this.getEquippedStack(EquipmentSlot.HEAD);
        ItemStack chest = this.getEquippedStack(EquipmentSlot.CHEST);
        ItemStack legs = this.getEquippedStack(EquipmentSlot.LEGS);
        ItemStack feet = this.getEquippedStack(EquipmentSlot.FEET);

        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int count4 = 0;
        if(head.getItem() == ItemInit.MITHRIL_HELMET){
            count1=1;
        }
        if(chest.getItem() == ItemInit.MITHRIL_CHESTPLATE){
            count2=1;
        }
        if(legs.getItem() == ItemInit.MITHRIL_LEGGINGS){
            count3=1;
        }
        if(feet.getItem() == ItemInit.MITHRIL_BOOTS){
            count4=1;
        }

        return count1 + count2 + count3 + count4;
    }
    @Unique
    public int wearAdamanArmor(){
        ItemStack head = this.getEquippedStack(EquipmentSlot.HEAD);
        ItemStack chest = this.getEquippedStack(EquipmentSlot.CHEST);
        ItemStack legs = this.getEquippedStack(EquipmentSlot.LEGS);
        ItemStack feet = this.getEquippedStack(EquipmentSlot.FEET);

        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int count4 = 0;
        if(head.getItem() == ItemInit.ADAMANTIUM_HELMET){
            count1=1;
        }
        if(chest.getItem() == ItemInit.ADAMANTIUM_CHESTPLATE){
            count2=1;
        }
        if(legs.getItem() == ItemInit.ADAMANTIUM_LEGGINGS){
            count3=1;
        }
        if(feet.getItem() == ItemInit.ADAMANTIUM_BOOTS){
            count4=1;
        }

        return count1 + count2 + count3 + count4;
    }
    @Unique
    public int wearDoomArmor(){
        ItemStack head = this.getEquippedStack(EquipmentSlot.HEAD);
        ItemStack chest = this.getEquippedStack(EquipmentSlot.CHEST);
        ItemStack legs = this.getEquippedStack(EquipmentSlot.LEGS);
        ItemStack feet = this.getEquippedStack(EquipmentSlot.FEET);
        String headName = Registries.ITEM.getId(head.getItem()).toString();
        String chestName = Registries.ITEM.getId(chest.getItem()).toString();
        String legsName = Registries.ITEM.getId(legs.getItem()).toString();
        String feetName = Registries.ITEM.getId(feet.getItem()).toString();

        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int count4 = 0;
        if(head.getItem() instanceof ArmorItem){
            if(	headName.contains("doom:") ){
                count1=1;
            }
        }
        if(chest.getItem() instanceof ArmorItem){
            if(	chestName.contains("doom:") ){
                count2=1;
            }
        }
        if(legs.getItem() instanceof ArmorItem){
            if(	legsName.contains("doom:") ){
                count3=1;
            }
        }
        if(feet.getItem() instanceof ArmorItem){
            if(	feetName.contains("doom:") ){
                count4=1;
            }
        }

        return count1 + count2 + count3 + count4;
    }
    @Unique
    public int wearLiangziArmor(){
        ItemStack head = this.getEquippedStack(EquipmentSlot.HEAD);
        ItemStack chest = this.getEquippedStack(EquipmentSlot.CHEST);
        ItemStack legs = this.getEquippedStack(EquipmentSlot.LEGS);
        ItemStack feet = this.getEquippedStack(EquipmentSlot.FEET);
        String headName = Registries.ITEM.getId(head.getItem()).toString();
        String chestName = Registries.ITEM.getId(chest.getItem()).toString();
        String legsName = Registries.ITEM.getId(legs.getItem()).toString();
        String feetName = Registries.ITEM.getId(feet.getItem()).toString();

        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int count4 = 0;
        if(head.getItem() instanceof ArmorItem){
            if(	headName.contains("modern_industrialization:") ){
                count1=1;
            }
        }
        if(chest.getItem() instanceof ArmorItem){
            if(	chestName.contains("modern_industrialization:") ){
                count2=1;
            }
        }
        if(legs.getItem() instanceof ArmorItem){
            if(	legsName.contains("modern_industrialization:") ){
                count3=1;
            }
        }
        if(feet.getItem() instanceof ArmorItem){
            if(	feetName.contains("modern_industrialization:") ){
                count4=1;
            }
        }

        return count1 + count2 + count3 + count4;
    }
    @Unique
    public boolean wearMithrilSword(){
        ItemStack hand = this.getMainHandStack();
        ItemStack handOff = this.getOffHandStack();

        if(hand.getItem()==ItemInit.MITHRIL_SWORD || handOff.getItem()==ItemInit.MITHRIL_SWORD){
            return true;
        }
        return false;
    }
    @Unique
    public boolean wearAdamanSword(){
        ItemStack hand = this.getMainHandStack();
        ItemStack handOff = this.getOffHandStack();

        if(hand.getItem()==ItemInit.ADAMANTIUM_SWORD || handOff.getItem()==ItemInit.ADAMANTIUM_SWORD){
            return true;
        }
        return false;
    }
    @Unique
    public boolean wearLiangziSword(){
        ItemStack hand = this.getMainHandStack();
        ItemStack handOff = this.getOffHandStack();
        String handName = Registries.ITEM.getId(hand.getItem()).toString();
        String handOffName = Registries.ITEM.getId(handOff.getItem()).toString();

        if(handName.contains("modern_industrialization:quantum_sword") || handOffName.contains("modern_industrialization:quantum_sword")){
            return true;
        }

        return false;
    }
    @Unique
    public boolean wearExpickaxe(){
        ItemStack hand = this.getMainHandStack();
        ItemStack handOff = this.getOffHandStack();

        if(hand.getItem()==ItemInit.EX_PICKAXE || handOff.getItem()==ItemInit.EX_PICKAXE){
            return true;
        }
        return false;
    }

    @Unique
    public void checkAdvancementingot_mithril(){
        if (server != null) {
            //成就1：凋零
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/ingot_mithril"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }

                if (progress != null && progress.isDone()) {////遍历服务器玩家：有人完成了进度
                    this.ingot_mithril = true;//记录完成进度，停止循环
                    break;
                }
            }

            if(ingot_mithril){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }
    @Unique
    public void checkAdvancementmithril_core(){
        if (server != null) {
            //成就1：凋零
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/mithril_core"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }

                if (progress != null && progress.isDone()) {////遍历服务器玩家：有人完成了进度
                    this.mithril_core = true;//记录完成进度，停止循环
                    break;
                }
            }

            if(mithril_core){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }
    @Unique
    public void checkAdvancementingot_adamantium(){
        if (server != null) {
            //成就1：凋零
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/ingot_adamantium"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }

                if (progress != null && progress.isDone()) {////遍历服务器玩家：有人完成了进度
                    this.ingot_adamantium = true;//记录完成进度，停止循环
                    break;
                }
            }

            if(ingot_adamantium){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }
    @Unique
    public void checkAdvancementadamantium_core(){
        if (server != null) {
            //成就1：凋零
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/adamantium_core"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }

                if (progress != null && progress.isDone()) {////遍历服务器玩家：有人完成了进度
                    this.adamantium_core = true;//记录完成进度，停止循环
                    break;
                }
            }

            if(adamantium_core){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }
    @Unique
    public void checkAdvancementargent_pickaxe(){
        if (server != null) {
            //成就1：凋零
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/argent_pickaxe"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }

                if (progress != null && progress.isDone()) {////遍历服务器玩家：有人完成了进度
                    this.argent_pickaxe = true;//记录完成进度，停止循环
                    break;
                }
            }

            if(argent_pickaxe){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }
    @Unique
    public void checkAdvancementargent_core(){
        if (server != null) {
            //成就1：凋零
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/argent_core"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }

                if (progress != null && progress.isDone()) {////遍历服务器玩家：有人完成了进度
                    this.argent_core = true;//记录完成进度，停止循环
                    break;
                }
            }

            if(argent_core){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }

    @Unique
    public void checkAdvancemendQidian(){
        if (server != null) {
            //成就1：凋零
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("modern_industrialization:quantum_upgrade"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }

                if (progress != null && progress.isDone()) {////遍历服务器玩家：有人完成了进度
                    this.quantum_upgrade = true;//记录完成进度，停止循环
                    break;
                }
            }

            if(quantum_upgrade){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }
    @Unique
    public void checkAdvancemendquantum_sword(){
        if (server != null) {
            //成就1：凋零
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("modern_industrialization:quantum_sword"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }

                if (progress != null && progress.isDone()) {////遍历服务器玩家：有人完成了进度
                    this.quantum_sword = true;//记录完成进度，停止循环
                    break;
                }
            }

            if(quantum_sword){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }
    //==================================================================================================================

    @Unique
    public void checkAdvancement1(){
        if (server != null) {
            //成就1：凋零
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/kill_a_wither"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }

                if (progress != null && progress.isDone()) {
                    this.can_end1 = true;
                    break;
                }
            }
            if(can_end1){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }

    @Unique
    public void checkAdvancement2(){
        if (server != null) {
            //成就2：循声守卫-坚守者
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/kill_a_warden"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }
                if (progress != null && progress.isDone()) {
                    this.can_end2 = true;
                    break;
                }
            }
            if(can_end2){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }

    @Unique
    public void checkAdvancement3(){
        if (server != null) {
            //成就3：黑石傀儡
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/kill_a_stone_golem"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }
                if (progress != null && progress.isDone()) {
                    this.can_end3 = true;
                    break;
                }
            }
            if(can_end3){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }

    @Unique
    public void checkAdvancement4(){
        if (server != null) {
            //成就4：地狱手套
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/kill_a_gauntlet"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }
                if (progress != null && progress.isDone()) {
                    this.can_end4 = true;
                    break;
                }
            }
            if(can_end4){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }

    @Unique
    public void checkAdvancement5(){
        if (server != null) {
            //成就5：虚空之花
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/kill_a_void_blossom"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }
                if (progress != null && progress.isDone()) {
                    this.can_end5 = true;
                    break;
                }
            }
            if(can_end5){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }

    @Unique
    public void checkAdvancement6(){
        if (server != null) {
            //成就6：夜巫妖
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/kill_a_void_lich"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }
                if (progress != null && progress.isDone()) {
                    this.can_end6 = true;
                    break;
                }
            }
            if(can_end6){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }

    @Unique
    public void checkAdvancement7(){
        if (server != null) {
            //成就7：妖娘
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/kill_a_motherdemon"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }
                if (progress != null && progress.isDone()) {
                    this.can_end7 = true;
                    break;
                }
            }
            if(can_end7){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }

    @Unique
    public void checkAdvancement8(){
        if (server != null) {
            //成就8：角斗士
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/kill_a_gladiator"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }
                if (progress != null && progress.isDone()) {
                    this.can_end8 = true;
                    break;
                }
            }
            if(can_end8){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }

    @Unique
    public void checkAdvancement9(){
        if (server != null) {
            //成就9：梅克尔
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/kill_a_arch_maykr"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }
                if (progress != null && progress.isDone()) {
                    this.can_end9 = true;
                    break;
                }
            }
            if(can_end9){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }

    @Unique
    public void checkAdvancement10(){
        if (server != null) {
            //成就10：暮色云上武装巨人
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/kill_a_armored_giant"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }
                if (progress != null && progress.isDone()) {
                    this.can_end10 = true;
                    break;
                }
            }
            if(can_end10){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }

    @Unique
    public void checkAdvancement11(){
        if (server != null) {
            //成就10：死亡细胞boss
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/kill_a_conjunctivius"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }
                if (progress != null && progress.isDone()) {
                    this.can_end11 = true;
                    break;
                }
            }
            if(can_end11){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }

    @Unique
    public void checkAdvancement12(){
        if (server != null) {
            //成就10：死亡细胞boss
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("doom:kill_icon"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }
                if (progress != null && progress.isDone()) {
                    this.can_end12 = true;
                    break;
                }
            }
            if(can_end12){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }

    @Unique
    public void checkAdvancement13(){
        if (server != null) {
            //成就10：死亡细胞boss
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("botania:challenge/gaia_guardian_hardmode"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }
                if (progress != null && progress.isDone()) {
                    this.can_end13 = true;
                    break;
                }
            }
            if(can_end13){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }

    @Unique
    public void checkAdvancement14(){
        if (server != null) {
            //成就14：魂类boss
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("soulsweapons:draugr_boss"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }
                if (progress != null && progress.isDone()) {
                    this.can_end14 = true;
                    break;
                }
            }
            if(can_end14){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }

    @Unique
    public void checkAdvancement15(){
        if (server != null) {
            //成就14：魂类boss
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/returning_knight"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }
                if (progress != null && progress.isDone()) {
                    this.can_end15 = true;
                    break;
                }
            }
            if(can_end15){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }

    @Unique
    public void checkAdvancement16(){
        if (server != null) {
            //成就14：魂类boss
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/accursed_lord_boss"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }
                if (progress != null && progress.isDone()) {
                    this.can_end16 = true;
                    break;
                }
            }
            if(can_end16){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }

    @Unique
    public void checkAdvancement17(){
        if (server != null) {
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/moonknight"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;

            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                //成就14：魂类boss
                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }
                if (progress != null && progress.isDone()) {
                    this.can_end17 = true;
                    break;
                }
            }
            if(can_end17){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }

    @Unique
    public void checkAdvancement18(){
        if (server != null) {
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/chaos_monarch"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;

            //成就1：凋零
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                //成就14：魂类boss
                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }
                if (progress != null && progress.isDone()) {
                    this.can_end18 = true;
                    break;
                }
            }
            if(can_end18){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }

    @Unique
    public void checkAdvancement19(){
        if (server != null) {
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/raider_knight"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;

            //成就1：凋零
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                //成就14：魂类boss
                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }
                if (progress != null && progress.isDone()) {
                    this.can_end19 = true;
                    break;
                }
            }
            if(can_end19){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }
    @Unique
    public void checkAdvancement20(){
        if (server != null) {
            Advancement advancement = server.getAdvancementLoader().get(Identifier.tryParse("aliveandwell:adventure/invoker"));

            PlayerAdvancementTracker trackerThe = Objects.requireNonNull(this.getServer()).getPlayerManager().getAdvancementTracker(((ServerPlayerEntity)(Object)this));
            AdvancementProgress progressThe = null;

            //成就1：凋零
            if (advancement != null) {
                progressThe = trackerThe.getProgress(advancement);
            }

            List<ServerPlayerEntity> serverPlayers = this.server.getPlayerManager().getPlayerList();
            for (ServerPlayerEntity player : serverPlayers) {
                PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(player);
                AdvancementProgress progress = null;

                //成就14：魂类boss
                if (advancement != null) {
                    progress = tracker.getProgress(advancement);
                }
                if (progress != null && progress.isDone()) {
                    this.can_end20 = true;
                    break;
                }
            }
            if(can_end20){//查询是否有人完成了进度
                if (progressThe != null) {//让没有获得成就的玩家获取
                    for (String string : progressThe.getUnobtainedCriteria()) {
                        ((ServerPlayerEntity)(Object)this).getAdvancementTracker().grantCriterion(advancement, string);
                    }
                }
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "readCustomDataFromNbt")
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        super.readCustomDataFromNbt(nbt);
        can_end = nbt.getBoolean("Can_end");
        obtain_ancient_debris = nbt.getBoolean("obtain_ancient_debris");
        can_end1 = nbt.getBoolean("Can_end1");
        can_end2 = nbt.getBoolean("Can_end2");
        can_end3 = nbt.getBoolean("Can_end3");
        can_end4 = nbt.getBoolean("Can_end4");
        can_end5 = nbt.getBoolean("Can_end5");
        can_end6 = nbt.getBoolean("Can_end6");
        can_end7 = nbt.getBoolean("Can_end7");
        can_end8 = nbt.getBoolean("Can_end8");
        can_end9 = nbt.getBoolean("Can_end9");
        can_end10 = nbt.getBoolean("Can_end10");
        can_end11 = nbt.getBoolean("Can_end11");
        can_end12 = nbt.getBoolean("Can_end12");
        can_end13 = nbt.getBoolean("Can_end13");
        can_end14 = nbt.getBoolean("Can_end14");
        can_end15 = nbt.getBoolean("Can_end15");
        can_end16 = nbt.getBoolean("Can_end16");
        can_end17 = nbt.getBoolean("Can_end17");
        can_end18 = nbt.getBoolean("Can_end18");
        can_end19 = nbt.getBoolean("Can_end19");
        can_end20 = nbt.getBoolean("Can_end20");
        ingot_adamantium = nbt.getBoolean("ingot_adamantium");
        adamantium_core = nbt.getBoolean("adamantium_core");
        argent_pickaxe = nbt.getBoolean("argent_pickaxe");
        quantum_upgrade = nbt.getBoolean("quantum_upgrade");
        argent_core = nbt.getBoolean("argent_core");
        isCheated = nbt.getBoolean("IsCheat");
        flag = nbt.getBoolean("Flag_Alive");
        timeSpectator = nbt.getInt("timeSpectator");
        cheatItem = nbt.getBoolean("cheatItem");
        ingot_mithril = nbt.getBoolean("ingot_mithril");
        mithril_core = nbt.getBoolean("mithril_core");
        quantum_sword = nbt.getBoolean("quantum_sword");
        clearCheat = nbt.getBoolean("clearCheat");
    }

    @Inject(at = @At("RETURN"), method = "writeCustomDataToNbt")
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Can_end", can_end);
        nbt.putBoolean("obtain_ancient_debris", obtain_ancient_debris);
        nbt.putBoolean("Can_end1", can_end1);
        nbt.putBoolean("Can_end2", can_end2);
        nbt.putBoolean("Can_end3", can_end3);
        nbt.putBoolean("Can_end4", can_end4);
        nbt.putBoolean("Can_end5", can_end5);
        nbt.putBoolean("Can_end6", can_end6);
        nbt.putBoolean("Can_end7", can_end7);
        nbt.putBoolean("Can_end8", can_end8);
        nbt.putBoolean("Can_end9", can_end9);
        nbt.putBoolean("Can_end10", can_end10);
        nbt.putBoolean("Can_end11", can_end11);
        nbt.putBoolean("Can_end12", can_end12);
        nbt.putBoolean("Can_end13", can_end13);
        nbt.putBoolean("Can_end14", can_end14);
        nbt.putBoolean("Can_end15", can_end15);
        nbt.putBoolean("Can_end16", can_end16);
        nbt.putBoolean("Can_end17", can_end17);
        nbt.putBoolean("Can_end18", can_end18);
        nbt.putBoolean("Can_end19", can_end19);
        nbt.putBoolean("Can_end20", can_end20);
        nbt.putBoolean("ingot_adamantium", ingot_adamantium);
        nbt.putBoolean("adamantium_core", adamantium_core);
        nbt.putBoolean("argent_pickaxe", argent_pickaxe);
        nbt.putBoolean("quantum_upgrade", quantum_upgrade);
        nbt.putBoolean("argent_core", argent_core);
        nbt.putBoolean("IsCheat", isCheated);
        nbt.putBoolean("Flag_Alive", flag);
        nbt.putInt("timeSpectator", timeSpectator);
        nbt.putBoolean("cheatItem", cheatItem);
        nbt.putBoolean("ingot_mithril", ingot_mithril);
        nbt.putBoolean("mithril_core", mithril_core);
        nbt.putBoolean("quantum_sword", quantum_sword);
        nbt.putBoolean("clearCheat", clearCheat);
    }
}
