package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.dimensions.DimsRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    @Unique
    private int deSpawnTime;
    @Unique
    private static final NbtCompound nbt = new NbtCompound();
    @Unique
    private static final String[] enityName = {"screecher","imp","nightmare_imp","iconofsin","painelemental","shotgunguy","hellknight","hellknight2016","chaingunner","marauder","imp2016","cyberdemon","cyberdemon2016","unwilling","revenant","pinky","spectre","archvile","baron","cacodemon","mancubus","lost_soul","lost_soul_eternal","possessed_scientist","possessed_worker","possessed_soldier","arachnotron","spidermastermind","spidermastermind2016","zombieman","mechazombie","gore_nest","gargoyle","cueball","prowler","dreadknight","stone_imp","whiplash","doom_hunter","maykr_drone","arch_maykr","baron2016","firebronebaron","armoredbaron","tyrant","pinky2016","arachnotroneternal","archvileeternal","tentacle","motherdemon","turret","summoner","revenant2016","gladiator"};

    @Shadow
    @Nullable
    private LivingEntity target;

    @Shadow public abstract boolean canPickUpLoot();

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at=@At("HEAD"), method="canPickUpLoot", cancellable = true)
    public void canPickUpLoot(CallbackInfoReturnable<Boolean> ca) {
        if((MobEntity)(Object)this instanceof ZombieEntity || (MobEntity)(Object)this instanceof AbstractSkeletonEntity || (MobEntity)(Object)this instanceof PillagerEntity && (!this.getName().toString().contains("mobz.") ||  !this.getName().toString().contains("archer"))){
            ca.setReturnValue(true);
        }
    }

    @Inject(at=@At("HEAD"), method="canPickupItem", cancellable = true)
    public void canPickupItem(ItemStack stack,CallbackInfoReturnable<Boolean> ca) {
        //只能拾取武器或装备
        if(!(stack.getItem() instanceof ToolItem || stack.getItem() instanceof ArmorItem || stack.getItem() instanceof BowItem || stack.getItem() instanceof CrossbowItem
                || stack.getItem() instanceof TridentItem)){
            ca.setReturnValue(false);
        }
    }

    @Inject(at=@At("HEAD"), method="baseTick")
    public void baseTick(CallbackInfo ca) {
        deSpawnTime++;
        setMobEquipNbt((MobEntity)(Object) this);

        ItemStack stack = this.getEquippedStack(EquipmentSlot.MAINHAND);
        ItemStack stack1 = this.getEquippedStack(EquipmentSlot.OFFHAND);
        if (((MobEntity)(Object)this).getTarget() != null && ((MobEntity)(Object)this).getTarget() instanceof PlayerEntity) {
            if(this.distanceTo(((MobEntity)(Object)this).getTarget()) <= 10.f ){
                if(!((MobEntity)(Object)this instanceof PillagerEntity)){
                    if( ( stack.isOf(Items.BOW) || stack.isEmpty() || stack.isOf(Items.CROSSBOW)) && !stack1.isEmpty() && stack1.getItem() instanceof ToolItem){
                        this.equipStack(EquipmentSlot.MAINHAND,stack1);
                    }
                }
            }
        }

        if(((MobEntity)(Object)this).isAttacking()){
            if(((MobEntity)(Object)this).getAttacker() != null && ((MobEntity)(Object)this).getTarget() instanceof PlayerEntity){
                ((MobEntity)(Object) this).setTarget(((MobEntity)(Object)this).getAttacker());
            }
        }

        if((MobEntity)(Object)this instanceof HostileEntity && !((MobEntity)(Object)this instanceof EndermanEntity)){
            //毁灭战士怪物主动攻击24格范围内玩家
            if(((MobEntity)(Object) this).getTarget() == null){
                PlayerEntity player = this.getWorld().getClosestPlayer(this.getX(), this.getY(), this.getZ(), 32, false);
                PlayerEntity player1 = this.getWorld().getClosestPlayer(this.getX(), this.getY(), this.getZ(), 24, false);
                PlayerEntity player2 = this.getWorld().getClosestPlayer(this.getX(), this.getY(), this.getZ(), 16, false);

                if(this.getName().toString().contains("terrarianslimes.")) {
                    ((MobEntity)(Object) this).setTarget(player2);
                }

                if(this.getName().toString().contains("minecraft.")) {
                    if(this.getName().toString().contains("slime")){
                        ((MobEntity)(Object) this).setTarget(player2);
                    }
                }

                //地下世界
                if (this.getWorld().getRegistryKey() == DimsRegistry.UNDER_WORLD_KEY ) {
//                    if(this.getAttacker() != null){
//                        ((MobEntity)(Object) this).setTarget(this.getAttacker());
//                    }else {
//                        if (player != null) {
//                            if (this.canSee(player)) {
//                                ((MobEntity)(Object) this).setTarget(player);
//                            }else {
//                                if (player1 != null) {
//                                    ((MobEntity)(Object) this).setTarget(player1);
//                                }
//                            }
//                        }
//                    }

                    if (player != null) {
                        if (this.canSee(player)) {
                            ((MobEntity)(Object) this).setTarget(player);
                        }else {
                            if (player1 != null) {
                                ((MobEntity)(Object) this).setTarget(player1);
                            }
                        }
                    }
                }else {
                    if (player1 != null) {
                        if (this.canSee(player1)) {
                            ((MobEntity)(Object) this).setTarget(player1);
                        }else {
                            if (player2 != null) {
                                ((MobEntity)(Object) this).setTarget(player2);
                            }
                        }
                    }
                    if (player != null) {
                        if (this.canSee(player)) {
                            if (this.getName().toString().contains("ad_astra.")) {
                                ((MobEntity) (Object) this).setTarget(player1);
                            }else if (this.getName().toString().contains("twilightforest.")) {
                                ((MobEntity) (Object) this).setTarget(player);
                            }else {
                                ((MobEntity) (Object) this).setTarget(player);
                            }
                        } else {
                            if (player1 != null) {
                                ((MobEntity) (Object) this).setTarget(player1);
                            }
                        }
                    }
                }
            }
        }
    }

    @Inject(at=@At("HEAD"), method="cannotDespawn", cancellable = true)
    public void cannotDespawn(CallbackInfoReturnable<Boolean> ca) {
        ItemStack itemStack1 = null;
        ItemStack itemStack2 = null;
        ItemStack itemStack3 = null;
        ItemStack itemStack4 = null;
        ItemStack itemStack5 = null;
        ItemStack itemStack6 = null;
        if(this.hasStackEquipped(EquipmentSlot.HEAD)) {
            itemStack1= this.getEquippedStack(EquipmentSlot.HEAD);
        }
        if(this.hasStackEquipped(EquipmentSlot.CHEST)) {
            itemStack2= this.getEquippedStack(EquipmentSlot.CHEST);
        }
        if(this.hasStackEquipped(EquipmentSlot.LEGS)) {
            itemStack3= this.getEquippedStack(EquipmentSlot.LEGS);
        }
        if(this.hasStackEquipped(EquipmentSlot.FEET)) {
            itemStack4= this.getEquippedStack(EquipmentSlot.FEET);
        }
        if(this.hasStackEquipped(EquipmentSlot.MAINHAND)) {
            itemStack5= this.getEquippedStack(EquipmentSlot.MAINHAND);
        }
        if(this.hasStackEquipped(EquipmentSlot.OFFHAND)) {
            itemStack6= this.getEquippedStack(EquipmentSlot.OFFHAND);
        }

        if(itemStack1 != null ) {
            if(itemStack1.hasNbt() ){
                assert itemStack1.getNbt() != null;
                if(itemStack1.getNbt().contains("equip_player")) {
                    if(this.deSpawnTime >= 20*60*20) {
                        this.deSpawnTime  = 0;
                        ca.setReturnValue(false);
                    }
                    ca.setReturnValue(true);
                }
            }
        }
        if(itemStack2 != null ) {
            if(itemStack2.hasNbt() ){
                assert itemStack2.getNbt() != null;
                if(itemStack2.getNbt().contains("equip_player")) {
                    if(this.deSpawnTime >= 20*60*20) {
                        this.deSpawnTime  = 0;
                        ca.setReturnValue(false);
                    }
                    ca.setReturnValue(true);
                }
            }
        }
        if(itemStack3 != null ) {
            if(itemStack3.hasNbt() ){
                assert itemStack3.getNbt() != null;
                if(itemStack3.getNbt().contains("equip_player")) {
                    if(this.deSpawnTime >= 20*60*20) {
                        this.deSpawnTime  = 0;
                        ca.setReturnValue(false);
                    }
                    ca.setReturnValue(true);
                }
            }
        }
        if(itemStack4 != null ) {
            if(itemStack4.hasNbt() ){
                assert itemStack4.getNbt() != null;
                if(itemStack4.getNbt().contains("equip_player")) {
                    if(this.deSpawnTime >= 20*60*20) {
                        this.deSpawnTime  = 0;
                        ca.setReturnValue(false);
                    }
                    ca.setReturnValue(true);
                }
            }
        }
        if(itemStack5 != null ) {
            if(itemStack5.hasNbt() ){
                assert itemStack5.getNbt() != null;
                if(itemStack5.getNbt().contains("equip_player")) {
                    if(this.deSpawnTime >= 20*60*20) {
                        this.deSpawnTime  = 0;
                        ca.setReturnValue(false);
                    }
                    ca.setReturnValue(true);
                }
            }
        }
        if(itemStack6 != null ) {
            if(itemStack6.hasNbt() ){
                assert itemStack6.getNbt() != null;
                if(itemStack6.getNbt().contains("equip_player")) {
                    if(this.deSpawnTime >= 20*60*20) {
                        this.deSpawnTime  = 0;
                        ca.setReturnValue(false);
                    }
                    ca.setReturnValue(true);
                }
            }
        }

        if(this.target != null && this.target instanceof PlayerEntity ){
            if(this.canPickUpLoot()){
                ca.setReturnValue(true);
            }
        }
    }

    @Inject(at=@At("HEAD"), method= "canSpawn(Lnet/minecraft/world/WorldView;)Z", cancellable = true)
    public void canSpawn(WorldView world, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(canSpanwnDoom(world,(MobEntity)(Object)this));
    }

    @Unique
    private boolean canSpanwnDoom(WorldView world, MobEntity entity){
        if (entity.getName().toString().contains("doom.")) {
            if (entity.getName().toString().contains("blood_maykr")
            ) {
                return false;
            }
        }

        if (AliveAndWellMain.day <= 16){
            if (entity.getName().toString().contains("minecraft.") || entity.getName().toString().contains("creeperoverhaul.")) {
                if(entity.getName().toString().contains("creeper")){
                    if (!(entity.getEntityWorld().getRegistryKey() == DimsRegistry.UNDER_WORLD_KEY)) {//除了地下世界
                        return false;
                    }
                }
            }
        }
        if (AliveAndWellMain.day <= 25) {
            if (entity.getName().toString().contains("archer")
                    || entity.getName().toString().contains("knight2")) {
                if (entity.getName().toString().contains("mobz.")) {
                    return false;
                }
            }
        }

        if (AliveAndWellMain.day <= 48) {
            for (String name : enityName) {
                if (entity.getName().toString().contains(name)) {
                    if (entity.getName().toString().contains("doom.")) {
                        if (entity.getEntityWorld().getRegistryKey() == World.OVERWORLD) {//除了地下世界
                            return false;
                        }
                    }
                }
            }
        }

        if (AliveAndWellMain.day <= 96) {
            if (entity.getName().toString().contains("doom.")) {
                if (entity.getName().toString().contains("prowler")
                        || entity.getName().toString().contains("stoneipm")
                        || entity.getName().toString().contains("gargoyle")
                        || entity.getName().toString().contains("stoneimp")//石皮幼魔
                        || entity.getName().toString().contains("revenant")//亡魂
                        || entity.getName().toString().contains("doom_hunter")//毁灭战士猎人
                        || entity.getName().toString().contains("spidermastermind")//蜘蛛首脑
                ) {
                    return false;
                }
            }
        }

        if (AliveAndWellMain.day <= 128) {//64天前不生成。
            if (entity.getName().toString().contains("doom.")) {
                if (entity.getName().toString().contains("baron")
                        || entity.getName().toString().contains("pinky")
                        || entity.getName().toString().contains("whiplash")
                        || entity.getName().toString().contains("dreadknight")
                        || entity.getName().toString().contains("archvile")
                        || entity.getName().toString().contains("motherdemon")
                        || entity.getName().toString().contains("summoner")//撒拉弗
                ) {
                    return false;
                }
            }

            //hellknight,mancubus,arachnotroneternal
            if (entity.getName().toString().contains("doom.")) {
                if (entity.getName().toString().contains("hellknight")
                        || entity.getName().toString().contains("mancubus")
                        || entity.getName().toString().contains("arachnotroneternal")
                ) {
                    return false;
                }
            }
        }

        //'doom:spectre_spawn_egg'      'doom:unwilling_spawn_egg'
        if (AliveAndWellMain.day <= 156) {//64天前不生成。
            if (entity.getName().toString().contains("doom.") && (entity.getName().toString().contains("cacodemon") || entity.getName().toString().contains("lost_soul")) && entity.getEntityWorld().getRegistryKey() != World.NETHER) {
                        return false;
                    }
                }

            if (entity.getName().toString().contains("doom.")) {
                if (entity.getName().toString().contains("spectre")
                        || entity.getName().toString().contains("unwilling")
                ) {
                    return false;
                }
            }


        return !world.containsFluid(entity.getBoundingBox()) && world.doesNotIntersectEntities(entity);
    }

    @Unique
    private void setMobEquipNbt(MobEntity mob){
        if(mob.hasStackEquipped(EquipmentSlot.HEAD)){
            ItemStack head = mob.getEquippedStack(EquipmentSlot.HEAD);
            if( head.getItem() instanceof ArmorItem){
                if(head.hasNbt()){
                    assert head.getNbt() != null;
                    if(!head.getNbt().contains("equip_player")){
                        head.setSubNbt("equip_mob",nbt);
                    }
                }
            }
        }
        if(mob.hasStackEquipped(EquipmentSlot.CHEST)){
            ItemStack head = mob.getEquippedStack(EquipmentSlot.CHEST);
            if( head.getItem() instanceof ArmorItem){
                if(head.hasNbt()){
                    assert head.getNbt() != null;
                    if(!head.getNbt().contains("equip_player")){
                        head.setSubNbt("equip_mob",nbt);
                    }
                }
            }
        }
        if(mob.hasStackEquipped(EquipmentSlot.LEGS)){
            ItemStack head = mob.getEquippedStack(EquipmentSlot.LEGS);
            if( head.getItem() instanceof ArmorItem){
                if(head.hasNbt()){
                    assert head.getNbt() != null;
                    if(!head.getNbt().contains("equip_player")){
                        head.setSubNbt("equip_mob",nbt);
                    }
                }
            }
        }
        if(mob.hasStackEquipped(EquipmentSlot.FEET)){
            ItemStack head = mob.getEquippedStack(EquipmentSlot.FEET);
            if( head.getItem() instanceof ArmorItem){
                if(head.hasNbt()){
                    assert head.getNbt() != null;
                    if(!head.getNbt().contains("equip_player")){
                        head.setSubNbt("equip_mob",nbt);
                    }
                }
            }
        }
        if(mob.hasStackEquipped(EquipmentSlot.MAINHAND)){
            ItemStack head = mob.getEquippedStack(EquipmentSlot.MAINHAND);
            if( head.getItem() instanceof ToolItem || head.getItem() instanceof RangedWeaponItem){
                if(head.hasNbt()){
                    assert head.getNbt() != null;
                    if(!head.getNbt().contains("equip_player")){
                        head.setSubNbt("equip_mob",nbt);
                    }
                }
            }
        }
        if(mob.hasStackEquipped(EquipmentSlot.OFFHAND)){
            ItemStack head = mob.getEquippedStack(EquipmentSlot.OFFHAND);
            if( head.getItem() instanceof ToolItem || head.getItem() instanceof RangedWeaponItem){
                if(head.hasNbt()){
                    assert head.getNbt() != null;
                    if(!head.getNbt().contains("equip_player")){
                        head.setSubNbt("equip_mob",nbt);
                    }
                }
            }
        }
    }
}