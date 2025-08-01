package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import com.XHxinhe.aliveandwell.registry.ItemInit;
import com.XHxinhe.aliveandwell.registry.events.EatOreAddExperience;
import com.XHxinhe.aliveandwell.util.GetIsDestroyingBlock;
import com.XHxinhe.aliveandwell.util.ReachDistance;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Unique
    private int lavaTime = 0;
    @Unique
    private int damageTime = 0;
    @Unique
    private int damageTime1 = 0;
    @Unique
    private int hungerTime = 0;
    @Unique
    private float cold_lerp = 0;
    @Unique
    boolean hot = false;
    @Unique
    boolean cold = false;
    @Shadow
    protected HungerManager hungerManager;

    @Shadow
    public int experienceLevel;
    @Shadow protected int enchantmentTableSeed;

//    private final NbtCompound nbt = new NbtCompound();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    public void baseTick() {
        ReachDistance.setReachDistance((PlayerEntity) (Object)this);
//        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue((int)Math.floor((double) Math.min(experienceLevel, 200) / 5) * 2 + 6);
//        this.setHealth(Math.min(120, (int)Math.floor(experienceLevel / 5) * 2 + 6));
        super.baseTick();

        int i = this.experienceLevel;
        this.totalExperience = (int) (5*i*i+5*i+this.experienceProgress*10*(this.experienceLevel+1));

        if (getWorld().hasRain(getBlockPos())) {
            this.addExhaustion(0.004f);
        }
        if (this.isTouchingWater() || this.isSubmergedInWater) {
            this.addExhaustion(0.006f);
        }

        if (getWorld().getDimension().ultrawarm()) {
            hot = true;
            cold = false;
        } else
        {
            float temperature = getWorld().getBiome(getBlockPos()).value().getTemperature();
            hot = temperature > 1.5f;
            cold = temperature < 0.5f;
        }

        if (cold) {
            if (cold_lerp < 1) {
                cold_lerp += 0.01f;
            }
        } else {
            if (cold_lerp > 0) {
                cold_lerp -= 0.01f;
            }
        }

        float hunger_loss = 0.001f;

        if (cold) {
            hunger_loss = 0.003f;
        }
        if (hot) {
            hunger_loss = 0.004f;
        }
        this.addExhaustion(hunger_loss);
    }

    @Inject(at = @At("HEAD"), method = "tickMovement")
    public void tickMovement(CallbackInfo info) {
        if (this.getHealth() < this.getMaxHealth()) {
            this.heal(0.00015F);
        }

        if (this.isSprinting()) {
            this.addExhaustion(0.003f);
        }

    }
    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo info) throws IllegalAccessException {
        if(this.isInLava()) {
            lavaTime++;
        }

        boolean digging = this.isDigBlock();
        int i = this.experienceLevel;
        this.totalExperience = (int) (5*i*i+5*i+this.experienceProgress*10*(this.experienceLevel+1));

        double slow_speed = 0.8d;
        double MOVEMENT_SPEED = 0.10000000149011612D;
        if(this.hungerManager.getFoodLevel()< 1){
            this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(MOVEMENT_SPEED * slow_speed);
        }

        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue((hungerManager.getFoodLevel() + hungerManager.getSaturationLevel()) > 0 ? 1 : 0);

        //每10秒减1个血
        if(this.hungerManager.getFoodLevel() <= 0){
            damageTime++;
            if(this.damageTime >=200){
                this.damage(this.getDamageSources().starve(),1.0f);
                damageTime = 0;
            }
        }

        //中毒每10秒减1个血
        if(this.hasStatusEffect(StatusEffects.POISON) && this.getHealth() <= 1){
            damageTime1++;
            if(this.damageTime1 >=100){
                this.damage(this.getDamageSources().magic(),1.0f);
                damageTime1 = 0;
            }
        }

        //每3秒减1个血
        if(this.hasStatusEffect(StatusEffects.HUNGER)){
            hungerTime++;
            if(this.hungerTime >=120 ){
                this.addExhaustion(2.0f);
                hungerTime = 0;
            }
        }

        if(this.isInLava()){
            lavaTime++;
            if(lavaTime>=40){
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE,1,0));
                this.getEquippedStack(EquipmentSlot.HEAD).damage(2,this.random,null);
                this.getEquippedStack(EquipmentSlot.CHEST).damage(2,this.random,null);
                this.getEquippedStack(EquipmentSlot.LEGS).damage(2,this.random,null);
                this.getEquippedStack(EquipmentSlot.LEGS).damage(2,this.random,null);
                lavaTime = 0;
            }
        }

        //20秒回复1血
        if(this.isSleeping()){
            this.heal(0.01f);
            if(digging){
                this.wakeUp();
                this.sendMessage(Text.translatable("aliveandwell.playerentity.info1").formatted(Formatting.RED));
            }
        }

//        banItem();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getXpToDrop() {
        if (this.getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY) || this.isSpectator()) {
            return 0;
        }
        int i = this.experienceLevel;
        return (int)((5*i*i+5*i+this.experienceProgress*10*(this.experienceLevel+1))*3/5);
    }

    @Inject(at = @At("RETURN"), method = "getBlockBreakingSpeed", cancellable = true)
    private void getBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> ca) {
        float speed = this.getInventory().getBlockBreakingSpeed(block);

        ItemStack itemStack = this.getMainHandStack();
        if((block.getBlock() instanceof PillarBlock) && (block.isIn(BlockTags.LOGS)) && (Registries.ITEM.getId(itemStack.getItem()).toString().equals("mcdw:staff_battlestaff"))){
            speed = -1 ;
            ca.setReturnValue(speed);
        }

        if((block.getBlock() instanceof PillarBlock) && (block.isIn(BlockTags.LOGS)) && !(itemStack.getItem() instanceof AxeItem) && (!(Registries.ITEM.getId(itemStack.getItem()).toString().equals("doom:argent_paxel")|| Registries.ITEM.getId(itemStack.getItem()).toString().equals("doom:argent_axe")))){
            speed = -1 ;
            ca.setReturnValue(speed);
        }

        if(itemStack.getItem() instanceof MiningToolItem){
            MiningToolItem miningToolItem = (MiningToolItem) (itemStack.getItem());
            int i = miningToolItem.getMaterial().getMiningLevel();
            if (i < 3 && block.isIn(BlockTags.NEEDS_DIAMOND_TOOL) && !(block.getBlock() == Blocks.OBSIDIAN || block.getBlock() == Blocks.CRYING_OBSIDIAN)) {
                speed = -1;
            } else if (i < 2 && block.isIn(BlockTags.NEEDS_IRON_TOOL) && !(block.getBlock() instanceof EnchantingTableBlock
                    || block.getBlock() instanceof AnvilBlock || block.getBlock() instanceof FurnaceBlock
            )) {
                speed = -1;
            } else if(i < 1 && block.isIn(BlockTags.NEEDS_STONE_TOOL)//熔炉、铁砧、附魔台不受影响
                    && !(block.getBlock() instanceof EnchantingTableBlock || block.getBlock() instanceof AnvilBlock || block.getBlock() instanceof FurnaceBlock
            )){
                speed = -1 ;
            }
            ca.setReturnValue(speed);
        }

        if( itemStack.getItem() == ItemInit.FLINT_PICKAXE && block.isIn(BlockTags.COPPER_ORES)){
            speed = -1 ;
            ca.setReturnValue(speed);
        }
        if(this.hungerManager.getFoodLevel()==0){
            if(block.getBlock() != Blocks.GRASS){
                speed = speed/8;
            }
        }

        if (speed > 1.0F) {
            int i = EnchantmentHelper.getEfficiency(this);
            speed += (float)(i * i + 1);
            if (i > 0 && !itemStack.isEmpty()) {
                speed = speed/8 + ((float)(i * i + 1))/15;
            }else {
                speed = speed/8;
            }
        }else {
            speed = speed/8.0F;
        }

        double j = (this.experienceLevel+0.000001)/100;
        if (j>=1.5){
            speed =speed*1.5F;
        }else {
            speed  =(float) (speed*(1+j));
        }

        if(Registries.ITEM.getId(itemStack.getItem()).toString().equals("doom:argent_pickaxe")
                || Registries.ITEM.getId(itemStack.getItem()).toString().equals("doom:argent_paxel")){
            if (speed > 1.0F) {
                int i = EnchantmentHelper.getEfficiency(this);
                speed += (float)(i * i + 1);
                if (i > 0 && !itemStack.isEmpty()) {
                    speed = speed/8 + ((float)(i * i + 1))/15;
                }else {
                    speed = speed/8;
                }
            }else {
                speed = speed/8.0F;
            }
            double m = (this.experienceLevel+0.000001)/100;
            if (m>=1.5){
                speed =speed*1.5F;
            }else {
                speed  =(float) (speed*(1+m));
            }
        }
        if(Registries.ITEM.getId(itemStack.getItem()).toString().equals("modern_industrialization:steam_mining_drill")){
            if(this.experienceLevel < 45){
                speed = speed/2;
            }
        }

        if(this.getMainHandStack().getItem() instanceof PickaxeItem){
            if(Registries.BLOCK.getId(block.getBlock()).toString().contains("coal")){
                speed = speed*2;
            }
        }

        if(this.getMainHandStack().getItem() instanceof PickaxeItem pickaxeItem){
            if(block.getBlock() == Blocks.OBSIDIAN || block.getBlock() == Blocks.CRYING_OBSIDIAN
            ){
                speed = speed*60;
            }

            if(block.getBlock() == Blocks.NETHERRACK){
                if(pickaxeItem.getMaterial().getMiningLevel() < 4){
                    speed = -1 ;
                }else {
                    speed = speed/4 ;
                }
            }
        }
        if(!(this.getMainHandStack().getItem() instanceof ToolItem)){
            if( block.getBlock() instanceof EnchantingTableBlock || block.getBlock() instanceof AnvilBlock || block.getBlock() instanceof FurnaceBlock){
                speed = speed*15;
            }
        }

        if(this.isTouchingWater() || this.isSubmergedInWater()){
            speed = speed/6;
        }

        if(block.isIn(BlockTags.LOGS) || block.isIn(BlockTags.PLANKS)){
            if(itemStack.getItem() instanceof MiningToolItem toolItem){
                if(toolItem instanceof AxeItem){
                    speed = speed*2;
                }
            }
        }
        if(block.isIn(ConventionalBlockTags.BUDDING_BLOCKS)){
            if(itemStack.getItem() instanceof MiningToolItem toolItem){
                if(toolItem instanceof ShovelItem){
                    speed = speed*1.2f;
                }
            }
        }

        if(block.getBlock() instanceof ChestBlock){
            speed = speed*1.2f;
        }

        if(block.getBlock() == Blocks.SUGAR_CANE){
            if(itemStack.getItem() instanceof SwordItem){
                speed = speed*1.2f;
            }
        }
        ca.setReturnValue(speed);
    }

    @Shadow
    public PlayerInventory getInventory() {
        return null;
    }

    @Shadow
    public abstract void addExhaustion(float exhaustion);

    @Shadow
    public abstract void wakeUp();

    @Shadow
    public abstract EntityDimensions getDimensions(EntityPose pose);

    @Shadow
    public abstract Text getName();

    @Shadow
    public float experienceProgress;

    @Shadow
    public int totalExperience;

    @Shadow
    public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Shadow
    public abstract void sendMessage(Text message, boolean overlay);

    @Shadow
    public abstract boolean isInvulnerableTo(DamageSource damageSource);

    @Shadow
    public abstract void tick();

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow
    public abstract void remove(RemovalReason reason);

    @Shadow
    public abstract void addExperience(int experience);

    @Shadow
    protected boolean isSubmergedInWater;

    @Shadow @Final private PlayerInventory inventory;

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @Shadow public abstract float getAbsorptionAmount();

    @Inject(at=@At("HEAD"), method="jump", cancellable = true)
    public void jump(CallbackInfo info) {
        super.jump();

        if (this.isSprinting()) {
            this.addExhaustion(0.006F);
        } else {
            this.addExhaustion(0.004F);
        }
        info.cancel();
    }
    protected void dropXp( ) {
        if (!(this.getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) && this.getWorld() instanceof ServerWorld && !this.isExperienceDroppingDisabled() && (this.shouldAlwaysDropXp() || this.playerHitTimer > 0 && this.shouldDropXp() && this.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_LOOT))) {
            if(!this.getWorld().isClient){
                List<ServerPlayerEntity> serverPlayers = Objects.requireNonNull(this.getWorld().getServer()).getPlayerManager().getPlayerList();
                int dropXP = this.getXpToDrop();
                int i = dropXP/ EatOreAddExperience.ITEM_EN_GENSTONE_XP;//经验石数量
                ItemStack itemStackEmerald = new ItemStack(ItemInit.ITEM_EN_GENSTONE,i);
                getWorld().spawnEntity(new ItemEntity((ServerWorld) this.getWorld(),this.getX(),this.getY(),this.getZ(),itemStackEmerald));
                for (ServerPlayerEntity player : serverPlayers) {
                    player.sendMessage(((MutableText)(this.getName())).append(Text.translatable("aliveandwell.playerentity.info2")).append(Text.of(String.valueOf(i))).append(Text.translatable("aliveandwell.playerentity.info3")).formatted(Formatting.LIGHT_PURPLE));
                }
            }
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    @Nullable
    public ItemEntity dropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership) {
        if (stack.isEmpty()) {
            return null;
        }
        if (this.getWorld().isClient) {
            this.swingHand(Hand.MAIN_HAND);
        }
        double d = this.getEyeY() - (double)0.3f;
        ItemEntity itemEntity = new ItemEntity(this.getWorld(), this.getX(), d, this.getZ(), stack);
        itemEntity.setPickupDelay(40);
        if (retainOwnership) {
            itemEntity.setThrower(this.getUuid());
        }
        if (throwRandomly) {
            float f = this.random.nextFloat() * 0.2f;//0.5
            float g = this.random.nextFloat() * ((float)(Math.PI * 2));//2
            itemEntity.setVelocity(-MathHelper.sin(g) * f, 0.2f, MathHelper.cos(g) * f);
        } else {
            float f = 0.3f;//0.3
            float g = MathHelper.sin(this.getPitch() * ((float)Math.PI / 180));
            float h = MathHelper.cos(this.getPitch() * ((float)Math.PI / 180));
            float i = MathHelper.sin(this.getYaw() * ((float)Math.PI / 180));
            float j = MathHelper.cos(this.getYaw() * ((float)Math.PI / 180));
            float k = this.random.nextFloat() * ((float)Math.PI * 2);
            float l = 0.02f * this.random.nextFloat();
            itemEntity.setVelocity((double)(-i * h * 0.3f) + Math.cos(k) * (double)l, -g * 0.3f + 0.1f + (this.random.nextFloat() - this.random.nextFloat()) * 0.1f, (double)(j * h * 0.3f) + Math.sin(k) * (double)l);
        }
        return itemEntity;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getNextLevelExperience() {
        return 10*(this.experienceLevel+1);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getEnchantmentTableSeed() {
        return this.random.nextInt();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void applyEnchantmentCosts(ItemStack itemStack, int enchantLevel) {
        if(enchantLevel>=44){
            addExperience(-10000);
        }else if(itemStack.getItem() == Items.GOLDEN_APPLE || itemStack.getItem() == Items.GOLDEN_CARROT) {
            addExperience(-400);
        }else {
            addExperience(- Math.max((int) (5*enchantLevel*enchantLevel+5*enchantLevel), 400));
        }

        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experienceProgress = 0.0F;
            this.totalExperience = 0;
        }
        this.enchantmentTableSeed = this.random.nextInt();
    }
    //僵尸正在挖方块
    @Unique
    public boolean isDigBlock() throws IllegalAccessException {
        LivingEntity entity = this.getWorld().getClosestEntity(HostileEntity.class, TargetPredicate.DEFAULT,this,this.getX(),this.getY(),this.getZ(),new Box(this.getX()-4,this.getY()-4,this.getZ()-4,this.getX()+4,this.getY()+4,this.getZ()+4));
        if(entity instanceof ZombieEntity zombieEntity){
            if(GetIsDestroyingBlock.getIsDestroyingBlock(zombieEntity)){
                return true;
            }
        }
        return false;
    }

    @Inject(at = @At("RETURN"), method = "getEquippedStack", cancellable = true)
    public void getEquippedStack(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> cir) {
        if (slot.getType() == EquipmentSlot.Type.ARMOR) {
            String key = Registries.ITEM.getId(inventory.armor.get(slot.getEntitySlotId()).getItem()).toString();
            int damage = this.inventory.armor.get(slot.getEntitySlotId()).getMaxDamage() - this.inventory.armor.get(slot.getEntitySlotId()).getDamage();
            cir.setReturnValue(damage <=1 && !key.contains("modern_industrialization:quantum_")? ItemStack.EMPTY : this.inventory.armor.get(slot.getEntitySlotId()));
        }
    }

    @Inject(at = @At("TAIL"), method = "addExperienceLevels")
    public void addExperienceLevels(int levels, CallbackInfo ci) {
        if(this.experienceLevel > 0 && levels != 0){
            if(levels>0){
                if ( this.experienceLevel % 5 == 0 && this.experienceLevel <= 200) {
//                    Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)).setBaseValue(this.getMaxHealth()+2);
//                    this.heal(2.0f);//先治愈，livingenity后加血量，所以会少

                    if(this.getHealth() == this.getMaxHealth()-2){//修复利用经验gui刷等级进行血量回复
                        this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION,6*20));
                    }

                }
            }else {
                int lose = (this.experienceLevel-levels) % 5 + levels;
                int m = -lose / 5;
                int healthLose = (m+1)*2;
                if(lose < 0){
//                    Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)).setBaseValue(this.getMaxHealth()- healthLose);
                    //有黄心，且红色血量也是满的
                    if(this.getHealth() >= this.getMaxHealth()){
                        this.heal(- healthLose);
                    }
                }
            }

        }
    }

}
