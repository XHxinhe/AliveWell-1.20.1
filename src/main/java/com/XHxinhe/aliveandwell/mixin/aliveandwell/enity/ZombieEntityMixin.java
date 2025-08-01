package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import com.XHxinhe.aliveandwell.dig.AnimalWatcherEntity;
import com.XHxinhe.aliveandwell.dig.EntityAIWatchAnimal;
import com.XHxinhe.aliveandwell.dig.FoodTargetGoal;
import com.XHxinhe.aliveandwell.dig.Resources;
import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.dimensions.DimsRegistry;
import com.XHxinhe.aliveandwell.util.ZombieEquipDiamondUtil;
import com.XHxinhe.aliveandwell.util.ZombieEquipIronUtil;
import com.XHxinhe.aliveandwell.util.ZombieEquipNetherUtil;
import net.minecraft.block.*;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin extends HostileEntity  implements AnimalWatcherEntity {

    @Unique
    protected boolean is_destroying_block;

    @Unique
    protected int destroy_block_x;
    @Unique
    protected int destroy_block_y;
    @Unique
    protected int destroy_block_z;
    @Unique
    protected int destroy_block_progress;
    @Unique
    protected int destroy_block_cooloff = 100;
    @Unique
    protected int destroy_pause_ticks;
    @Unique
    protected int build_pause_ticks;


    @Unique
    public int RECENTLY_HIT;
    @Unique
    public Random RAND;
    @Unique
    public boolean b;
    @Unique
    public int clearBlockTime;

    protected ZombieEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at=@At("HEAD"), method="initGoals")
    protected void initGoals(CallbackInfo info) {
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 32.0F));
        this.goalSelector.add(9, new LookAroundGoal(this));
        //=============================
        this.goalSelector.add(1, new EntityAIWatchAnimal((ZombieEntity)(Object)this));
        FoodTargetGoal foodTargetGoal = new FoodTargetGoal(this, true);
        foodTargetGoal.onlyEatsMeat = true;
        this.targetSelector.add(4, foodTargetGoal);
        this.RAND = random;
    }

    @Inject(at=@At("HEAD"), method="initCustomGoals")
    protected void initCustomGoals(CallbackInfo info) {
        this.goalSelector.add(9, new WanderAroundFarGoal(this, 1.0));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
//        this.targetSelector.add(3, new ActiveTargetGoal<>(this, AnimalEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PigEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, FoxEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, SheepEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, RabbitEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, CowEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, GoatEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PandaEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, FrogEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, AxolotlEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, ChickenEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PolarBearEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, TurtleEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, StriderEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, OcelotEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, HoglinEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, TameableEntity.class, true));
        this.RAND = random;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void initAttributes() {
        double i = 0;
        double j = 0;
        double i1 = 0;
        double j1 = 0;
        if(AliveAndWellMain.day >= 24) {
            i =1.0* (AliveAndWellMain.day-24) /8;//主世界health
            j = 1.0* (AliveAndWellMain.day-24) / 10;//主世界damage
            i1 = 1.0*(AliveAndWellMain.day-32) /8;//地下地狱health
            j1 =1.0* (AliveAndWellMain.day-48) / 10;//地下地狱damage
        }
        if(i<0)i=0;
        if(j<0)j=0;
        if(i1<0)i1=0;
        if(j1<0)j1=0;

        if(j>=10){
            j=10;
        }
        if(j1>=10){
            j1=10;
        }

        if (getWorld().getRegistryKey() == DimsRegistry.UNDER_WORLD_KEY  || getWorld().getRegistryKey() == World.NETHER) {
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)).setBaseValue(50.0+1.5*i1);
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE)).setBaseValue(3.0+j1);
        }else {
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)).setBaseValue(20.0+5*i);
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE)).setBaseValue(1.0+j);
        }
        Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS)).setBaseValue((this.random.nextDouble() * 0.10000000149011612)*1.2);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static DefaultAttributeContainer.Builder createZombieAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23333f).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0).add(EntityAttributes.GENERIC_ARMOR, 2.0).add(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS,0.65d);
//        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23f).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0).add(EntityAttributes.GENERIC_ARMOR, 2.0).add(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static boolean shouldBeBaby(Random random) {
        return false;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean isBaby() {
        return false;
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean shouldBreakDoors() {
        return true;
    }

    @Shadow protected abstract void playStepSound(BlockPos pos, BlockState state);

    @Shadow public abstract boolean damage(DamageSource source, float amount);

//    public ItemEntity attackFood;
    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo info) {
        build_pause_ticks++;
        clearBlockTime++;
        if (this.getTarget() == null) {
            PlayerEntity player1 = getWorld().getClosestPlayer(getX(), getY(), getZ(), 48, false);
            PlayerEntity player2= this.getWorld().getClosestPlayer(getX(), getY(), getZ(), 32, false);
            PlayerEntity player3= this.getWorld().getClosestPlayer(getX(), getY(), getZ(), 32, false);
            PlayerEntity player4= this.getWorld().getClosestPlayer(getX(), getY(), getZ(), 16, false);
            if (getWorld().getRegistryKey() == DimsRegistry.UNDER_WORLD_KEY){
                if(this.getAttacker() != null){
                    this.setTarget(this.getAttacker());
                }else {
                    if (player3 != null) {
                        if (this.canSee(player3)) {
                            this.setTarget(player3);
                        }else {
                            if (player4 != null) {
                                this.setTarget(player4);
                            }
                        }
                    }
                }
            }else {
                if(this.getAttacker() != null){
                    this.setTarget(this.getAttacker());
                }else {
                    if (player1 != null) {
                        if (this.canSee(player1)) {
                            this.setTarget(player1);
                        } else {
                            if (player2 != null) {
                                this.setTarget(player2);
                            }
                        }
                    }
                }
            }

            //目标为玩家
            if(this.getTarget() != null && this.getTarget() instanceof PlayerEntity player){
                buildBlock(player);
            }
        }

        if(this.getEquippedStack(EquipmentSlot.MAINHAND).getItem() instanceof ToolItem){
            this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.24000000417232513D * (Math.min((1+AliveAndWellMain.day*0.0002)*1.07,1.2d)));
        }else {
            this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.24000000417232513D * (Math.min(1+AliveAndWellMain.day*0.0002,1.2d)));
        }

        this.RECENTLY_HIT = this.getLastAttackedTime() - this.age;
        if (this.is_destroying_block) {
            if (this.destroy_pause_ticks == 0) {
                this.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, new Vec3d((float)this.destroy_block_x + 0.5F, (float)this.destroy_block_y + 0.5F, (float)this.destroy_block_z + 0.5F));
                if (!this.canDestroyBlock(this.destroy_block_x, this.destroy_block_y, this.destroy_block_z, true)) {
                    this.cancelBlockDestruction();
                } else {
                    isBlockClaimedByAnother(destroy_block_x, destroy_block_y, destroy_block_z);
                }
            }
        } else {
            this.destroy_block_cooloff = 100;
            this.destroy_block_progress = -1;
        }
    }

    public void buildBlock(PlayerEntity player){
        if(this.getNavigation().findPathTo(player,1) == null){
            if (build_pause_ticks >= 5 * 20 ) {
                //水平距离10格之内
                if ((Math.abs(player.getBlockPos().getZ() - this.getBlockPos().getZ()) >= 0 && Math.abs(player.getBlockPos().getZ() - this.getBlockPos().getZ()) < 10 && this.distanceTo(player) <= 64) || (Math.abs(player.getBlockPos().getX() - this.getBlockPos().getX()) > 0 && Math.abs(player.getBlockPos().getX() - this.getBlockPos().getX()) < 10 && this.distanceTo(player) <= 64)) {
                    BlockPos lastPos = new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ());
                    //僵尸头顶是空气
                    if (getWorld().getBlockState(this.getBlockPos().up(2)).isAir() && player.getBlockPos().getY() > this.getBlockPos().getY()) {
                        this.setPosition(Math.floor(this.getPos().getX()) + 0.5d, this.getPos().getY() + 1.0d, Math.floor(this.getPos().getZ()) + 0.5d);
                        if(!this.getWorld().getBlockState(lastPos).hasBlockEntity()){
                            this.b = this.getWorld().setBlockState(lastPos, Blocks.COBBLESTONE.getDefaultState());
                        }
                    }
                    if (!b || !(getWorld().getBlockState(this.getBlockPos().up(2)).isAir()) && player.getBlockPos().getY() >= this.getBlockPos().getY()) {
                        if (getWorld().getBlockState(new BlockPos(this.getBlockPos().getX() + 1, this.getBlockPos().getY() - 1, this.getBlockPos().getZ())).isAir()) {
                            if(!this.getWorld().getBlockState(new BlockPos(this.getBlockPos().getX() + 1, this.getBlockPos().getY() - 1, this.getBlockPos().getZ())).hasBlockEntity()){
                                this.getWorld().setBlockState(new BlockPos(this.getBlockPos().getX() + 1, this.getBlockPos().getY() - 1, this.getBlockPos().getZ()), Blocks.COBBLESTONE.getDefaultState());
                            }

                        } else if (getWorld().getBlockState(new BlockPos(this.getBlockPos().getX() - 1, this.getBlockPos().getY() - 1, this.getBlockPos().getZ())).isAir()) {
                            if(!this.getWorld().getBlockState(new BlockPos(this.getBlockPos().getX() - 1, this.getBlockPos().getY() - 1, this.getBlockPos().getZ())).hasBlockEntity()){
                                this.getWorld().setBlockState(new BlockPos(this.getBlockPos().getX() - 1, this.getBlockPos().getY() - 1, this.getBlockPos().getZ()), Blocks.COBBLESTONE.getDefaultState());
                            }
                        } else if (getWorld().getBlockState(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() - 1, this.getBlockPos().getZ() - 1)).isAir()) {
                            if(!this.getWorld().getBlockState(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() - 1, this.getBlockPos().getZ() - 1)).hasBlockEntity()){
                                this.getWorld().setBlockState(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() - 1, this.getBlockPos().getZ() - 1), Blocks.COBBLESTONE.getDefaultState());
                            }
                        } else if (getWorld().getBlockState(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() - 1, this.getBlockPos().getZ() + 1)).isAir()) {
                            if(!this.getWorld().getBlockState(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() - 1, this.getBlockPos().getZ() + 1)).hasBlockEntity()){
                                this.getWorld().setBlockState(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() - 1, this.getBlockPos().getZ() + 1), Blocks.COBBLESTONE.getDefaultState());
                            }
                        }
                    }
                    build_pause_ticks = 0;
                    if (!(getWorld().getBlockState(this.getBlockPos().up(2)).isAir())
                            && this.distanceTo(player) < 32) {
                        BlockPos pos = this.getBlockPos().up(2);
                        Block block = getWorld().getBlockState(pos).getBlock();
                        if (this.clearBlockTime >= 10 * 20) {
                            if (block != Blocks.OBSIDIAN || block != Blocks.BEDROCK) {
//                                this.getWorld().removeBlock(this.getBlockPos().up(2), true);
                                this.getWorld().breakBlock(this.getBlockPos().up(2), true);
                                Block.dropStack(this.getEntityWorld(), this.getBlockPos().up(2), new ItemStack(block.asItem()));
                                this.clearBlockTime = 0;
                            }
                        }
                    }
                }
            }
        }
    }

    public float getMovementSpeed() {
        return is_destroying_block ? 0 : super.getMovementSpeed();
    }

    @Inject(at=@At("HEAD"), method="writeCustomDataToNbt")
    public void writeCustomDataToNbt(NbtCompound par1NBTTagCompound, CallbackInfo info) {
        if (this.is_destroying_block) {
            par1NBTTagCompound.putBoolean("is_destroying_block", this.is_destroying_block);
            par1NBTTagCompound.putInt("destroy_block_x", this.destroy_block_x);
            par1NBTTagCompound.putInt("destroy_block_y", this.destroy_block_y);
            par1NBTTagCompound.putInt("destroy_block_z", this.destroy_block_z);
            par1NBTTagCompound.putInt("destroy_block_progress", this.destroy_block_progress);
            par1NBTTagCompound.putInt("destroy_block_cooloff", this.destroy_block_cooloff);
            par1NBTTagCompound.putInt("build_pause_ticks", this.build_pause_ticks);
            par1NBTTagCompound.putInt("clearBlockTime", this.clearBlockTime);
        }
    }

    @Inject(at=@At("HEAD"), method="readCustomDataFromNbt")
    public void readCustomDataFromNBT(NbtCompound par1NBTTagCompound, CallbackInfo info) {
        if (par1NBTTagCompound.contains("is_destroying_block")) {
            this.is_destroying_block = par1NBTTagCompound.getBoolean("is_destroying_block");
            this.destroy_block_x = par1NBTTagCompound.getInt("destroy_block_x");
            this.destroy_block_y = par1NBTTagCompound.getInt("destroy_block_y");
            this.destroy_block_z = par1NBTTagCompound.getInt("destroy_block_z");
            this.destroy_block_progress = par1NBTTagCompound.getInt("destroy_block_progress");
            this.destroy_block_cooloff = par1NBTTagCompound.getInt("destroy_block_cooloff");
            this.build_pause_ticks = par1NBTTagCompound.getInt("build_pause_ticks");
            this.clearBlockTime = par1NBTTagCompound.getInt("clearBlockTime");
        }
    }

    @Inject(at=@At("HEAD"), method="initEquipment")
    public void initEquipment(Random random, LocalDifficulty localDifficulty, CallbackInfo info) {
        super.initEquipment(random, localDifficulty);

        if( AliveAndWellMain.day <= 16) {
            super.initEquipment(random, localDifficulty);
        }else if(AliveAndWellMain.day <= 32) {
            if (this.getWorld().getRegistryKey() != World.OVERWORLD) {
                ZombieEquipIronUtil.equip((ZombieEntity) (Object) this);
            }else {
                if(new java.util.Random().nextInt(10)+1 == 5){
                    ZombieEquipIronUtil.equip3((ZombieEntity) (Object) this);
                }else {
                    super.initEquipment(random, localDifficulty);
                }
            }
        }else if(AliveAndWellMain.day < 96) {
            ZombieEquipIronUtil.equip((ZombieEntity)(Object)this);
        }else if( AliveAndWellMain.day <= 128) {
            if (this.getWorld().getRegistryKey() == World.OVERWORLD) {
                ZombieEquipIronUtil.equip((ZombieEntity) (Object) this);
            } else {
                ZombieEquipDiamondUtil.equip((ZombieEntity) (Object) this);
            }
        } else if (AliveAndWellMain.day <= 160) {
            if (this.getWorld().getRegistryKey() == World.OVERWORLD) {
                ZombieEquipIronUtil.equip2((ZombieEntity) (Object) this);
            } else {
                ZombieEquipDiamondUtil.equip((ZombieEntity) (Object) this);
            }
        }else if (AliveAndWellMain.day <= 196){
            ZombieEquipNetherUtil.equip1((ZombieEntity) (Object) this);
        }else {
            ZombieEquipNetherUtil.equip2((ZombieEntity) (Object) this);
        }
    }

    //===========================================
    public void onDeath(DamageSource par1DamageSource) {
        this.cancelBlockDestruction();
        super.onDeath(par1DamageSource);
    }

    public boolean isHoldingItemThatPreventsDigging() {
        if (this.getMainHandStack() == null) return false;
        Item held_item = this.getMainHandStack().getItem();
        return held_item instanceof SwordItem;
    }

    public boolean isDiggingEnabled() {
        return !this.isHoldingItemThatPreventsDigging();
    }


    public int recentlyHit() {
        return RECENTLY_HIT;
    }

    public int getDestroyBlockZ() {
        return destroy_block_z;
    }
    public int getDestroyBlockX() {
        return destroy_block_x;
    }
    public int getDestroyBlockY() {
        return destroy_block_y;
    }

    public int getDestroyBlockProgress() {
        return destroy_block_progress;
    }

    public boolean isBlockClaimedByAnother(int x, int y, int z) {
        Box bb = new Box(this.getX() - 4.0D, this.getY() - 4.0D, this.getZ() - 4.0D, this.getX() + 4.0D, this.getY() + 4.0D, this.getZ() + 4.0D);
        List<Entity> entities = this.getWorld().getOtherEntities(this, bb);

        for (Entity entity : entities) {
            if (entity instanceof AnimalWatcherEntity) {
                AnimalWatcherEntity digger = (AnimalWatcherEntity) entity;

                if (digger.isDestroyingBlock() && digger.getDestroyBlockX() == x && digger.getDestroyBlockY() == y && digger.getDestroyBlockZ() == z) {
                    if (digger.getDestroyBlockX() == destroy_block_x &&
                            digger.getDestroyBlockY() == destroy_block_y &&
                            digger.getDestroyBlockZ() == destroy_block_z &&
                            isDestroyingBlock()) {
                        if (destroy_block_progress < digger.getDestroyBlockProgress()) {
                            destroy_block_progress = digger.getDestroyBlockProgress();
                        }
                    }
                    return true;
                }
            }
        }

        return false;
    }

    private static double getDistanceSqFromDeltas(double dx, double dy, double dz) {
        return dx * dx + dy * dy + dz * dz;
    }
    public boolean hasDownwardsDiggingTool() {
        ItemStack held_item = this.getMainHandStack();
        return held_item != null && held_item.getItem() instanceof ShovelItem;
    }
    public double getCenterPosYForBlockDestroying() {
        return this.getY() + (double)(this.getHeight() * 0.5F);
    }

    public boolean canDestroyBlock(int x, int y, int z, boolean check_clipping) {
        if (this.getTarget() != null) {
            boolean canBeSeen;
//            if (canBeSeen == false) {
//                Vec3d vector3d = new Vec3d(this.getX(), this.getEyeY(), this.getZ());
//                Vec3d vector3d1 = new Vec3d(getTarget().getX(), getTarget().getEyeY(), getTarget().getZ());
//                canBeSeen = this.getWorld().raycast(new RaycastContext(vector3d, vector3d1, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this)).getType() == HitResult.Type.MISS;
//            }
            Vec3d vector3d = new Vec3d(this.getX(), this.getEyeY(), this.getZ());
            Vec3d vector3d1 = new Vec3d(getTarget().getX(), getTarget().getY(), getTarget().getZ());
            canBeSeen = this.getWorld().raycast(new RaycastContext(vector3d, vector3d1, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this)).getType() == HitResult.Type.MISS;
            if (canBeSeen) {
                this.is_destroying_block = false;
                return false;
            }
        }

        if (this.isHoldingItemThatPreventsDigging()) {
            return false;
        } else {
            int foot_y = this.getBlockPos().getY();

            if (y < foot_y && !this.hasDownwardsDiggingTool()) {
                return false;
            } else if (y > foot_y + 1) {
                return false;
            } else {
                if (getDistanceSqFromDeltas(this.getX() - (double)((float)x + 0.5F), this.getCenterPosYForBlockDestroying() - (double)((float)y + 0.5F), this.getZ() - (double)((float)z + 0.5F)) > 4.5D) {
                    return false;
                } else {
                    Block block1 = getWorld().getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block1 == null) {
                        return false;
                    }else if(block1 == Blocks.OBSIDIAN || block1 == Blocks.AMETHYST_BLOCK
                            || block1 == Blocks.BEDROCK
                            || Registries.BLOCK.getId(block1).toString().contains("portal")
                            || Registries.BLOCK.getId(block1).toString().contains("frame")){
                        return false;//|| block1 == Blocks.COBBLESTONE_WALL || block1 == Blocks.COBBLED_DEEPSLATE_WALL || block1 == Blocks.MOSSY_COBBLESTONE_WALL || block1  == Blocks.COBBLESTONE_WALL
                    } else if (block1.getDefaultState() == Blocks.AIR.getDefaultState()) {
                        return false;
                    } else if (!block1.getDefaultState().getFluidState().isEmpty()) {
                        return false;
                    } else {
                        if (!(block1.getDefaultState().isIn(BlockTags.PICKAXE_MINEABLE))) {
                            return true;
                        } else {
                            if (this.getMainHandStack() != null) {
                                Item held_item1 = this.getMainHandStack().getItem();
                                if (held_item1 instanceof PickaxeItem) {
                                    if (block1.getDefaultState().isIn(BlockTags.PICKAXE_MINEABLE))
                                        return true;
                                }
                                if (held_item1 instanceof AxeItem) {
                                    return block1.getDefaultState().getBlock() instanceof PillarBlock;
                                }
                            }
                        }
                        return false;
                    }
                }
            }
        }
    }

    public boolean isDestroyingBlock() {
        return is_destroying_block;
    }

    public boolean setBlockToDig(int x, int y, int z, boolean check_clipping) {
        if (!this.canDestroyBlock(x, y, z, check_clipping)) {
            return false;
        } else {
            this.is_destroying_block = true;
            if (x == this.destroy_block_x && y == this.destroy_block_y && z == this.destroy_block_z) {
                return true;
            } else {
                if (y == this.getBlockPos().getY() + 1 && this.getWorld().getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.CACTUS && this.canDestroyBlock(x, y - 1, z, check_clipping)) {
                    --y;
                }

                this.destroy_block_progress = -1;
                this.destroy_block_x = x;
                this.destroy_block_y = y;
                this.destroy_block_z = z;
                return true;
            }
        }
    }

    public void setDestroyingBlock(boolean b) {
        is_destroying_block = b;
    }

    public Vec3d getEyePosForBlockDestroying() {
        return this.getPrimaryPointOfAttack();
    }

    public Vec3d getPrimaryPointOfAttack()
    {
        return this.getPos().add(0, this.getHeight() * 0.75F, 0);
    }

    public boolean isFrenzied() {
        return  getWorld().isNight();
    }

    public boolean hasLineOfStrike(Vec3d target_pos) {
        List<Vec3d> target_points = new ArrayList<>();
        target_points.add(getPos());
        target_points.add(new Vec3d(getPos().x, getPos().y + getHeight() * 0.5, getPos().z));
        target_points.add(new Vec3d(getPos().x, getPos().y + getHeight() * 0.75, getPos().z));

        Iterator<Vec3d> i = target_points.iterator();
        do {
            if (!i.hasNext()) {
                return false;
            }
        } while (!Resources.getBlockCollisionForPhysicalReach(i.next(), target_pos, this.getEntityWorld(), this).isBlock());
        return true;
    }
    public boolean hasLineOfStrike(Entity target) {
        List<Vec3d> target_points = new ArrayList<>();
        target_points.add(target.getPos());
        target_points.add(new Vec3d(target.getPos().x, target.getPos().y + target.getHeight() * 0.5, target.getPos().z));
        target_points.add(new Vec3d(target.getPos().x, target.getPos().y + target.getHeight() * 0.75, target.getPos().z));

        for (Vec3d target_point : target_points) {
            if (this.hasLineOfStrike(target_point)) {
                return true;
            }
        }
        return false;
    }

    public boolean isTargetWithinStrikingDistance(LivingEntity target) {
        if (this.isAiDisabled()) {
            return false;
        } else {
            return this.getPos().distanceTo(new Vec3d(target.getX(), target.getBoundingBox().minY, target.getZ())) <= 1.5;
        }
    }

    public boolean hasLineOfStrikeAndTargetIsWithinStrikingDistance(LivingEntity target) {
        return this.isTargetWithinStrikingDistance(target) && this.hasLineOfStrike(target);
    }

    public Vec3d getTargetEntityCenterPosForBlockDestroying(LivingEntity entity_living_base) {
        return new Vec3d(entity_living_base.getPos().getX(), entity_living_base.getPos().getY() + (double)(entity_living_base.getHeight() / 2.0F), entity_living_base.getPos().getZ());
    }

    public boolean canSeeTarget(boolean b) {
        return this.canSee(getTarget());
    }

    public boolean isHoldingAnEffectiveTool(Block blockHit) {
        BlockState mat = blockHit.getDefaultState();
        if (this.getMainHandStack() != null) {
            Item i = this.getMainHandStack().getItem();

            if (mat.isIn(BlockTags.SHOVEL_MINEABLE)) {
                if (i instanceof ShovelItem) return true;
            }
            if (mat.isIn(BlockTags.PICKAXE_MINEABLE)) {
                if (!(i instanceof PickaxeItem)) return false;
            }
            if (mat.isIn(BlockTags.AXE_MINEABLE)) {
                return i instanceof AxeItem;
            }
        }
        return true;
    }

    public Vec3d getAttackerLegPosForBlockDestroying() {
        return new Vec3d(this.getX(), this.getY() + (double)(this.getHeight() * 0.25F), this.getZ());
    }

    public boolean blockWillFall(int x, int y, int z) {
        Block block = this.getWorld().getBlockState(new BlockPos(x, y, z)).getBlock();
        return block instanceof FallingBlock || block == Blocks.CACTUS || block instanceof TorchBlock || block instanceof WallTorchBlock || block == Blocks.SNOW;
    }

    public int getDestroyPauseTicks() {
        return this.destroy_pause_ticks;
    }

    public int getTicksExistedWithOffset() {
        return this.age + this.getId() * 47;
    }

    public GoalSelector getGoalSelector() {
        return this.goalSelector;
    }

    public void decrementDestroyPauseTicks() {
        destroy_pause_ticks--;
    }

    public int getDestroyBlockCooloff() {
        return destroy_block_cooloff;
    }

    public int getCooloffForBlock() {
        Block block = this.getWorld().getBlockState(new BlockPos(this.destroy_block_x, this.destroy_block_y, this.destroy_block_z)).getBlock();

        if (block == null) {
            return 40;
        } else {
            int cooloff = (int)(100.0F * this.getWorld().getBlockState(new BlockPos(this.destroy_block_x, this.destroy_block_y, this.destroy_block_z)).getHardness(this.getWorld(), new BlockPos(this.destroy_block_x, this.destroy_block_y, this.destroy_block_z)));
            if (this.isFrenzied()) {
                cooloff /= 4;
            }

            if (this.getMainHandStack() == null) {
                return cooloff;
            } else {
                Item held_item1 = this.getMainHandStack().getItem();
                if (held_item1 instanceof ToolItem) {
                    cooloff /= 10;//挖方块速度
                }
                return cooloff;
            }
        }
    }

    public void setDestroyBlockCooloff(int cooloffForBlock) {
        destroy_block_cooloff = cooloffForBlock;
    }

    public void partiallyDestroyBlock() {
        int x = this.destroy_block_x;
        int y = this.destroy_block_y;
        int z = this.destroy_block_z;

        if (!this.canDestroyBlock(x, y, z, true)) {
            this.cancelBlockDestruction();
            if (getTarget() != null)
                getNavigation().startMovingTo(getTarget(), 1.0);
        } else {
            Block block = getWorld().getBlockState(new BlockPos(x, y, z)).getBlock();
            Item it = null;
            if (this.getMainHandStack() != null) {
                it = this.getMainHandStack().getItem();
            }
            if (block == Blocks.CACTUS && !(it instanceof ToolItem)) {
                this.damage(this.getDamageSources().cactus(), 1.0F);
            }

            if (++this.destroy_block_progress < 10) {
                this.is_destroying_block = true;
            } else {
                this.destroy_block_progress = -1;
                if (block.getDefaultState().isIn(BlockTags.SMELTS_TO_GLASS)) {
                    getWorld().syncGlobalEvent(2001, new BlockPos(x, y, z), Block.getRawIdFromState(getWorld().getBlockState(new BlockPos(x, y, z))));
                }
                getWorld().removeBlock(new BlockPos(x, y, z), false);
                if (getTarget() != null)
                    getNavigation().startMovingTo(getTarget(), 1.0);
                if (this.blockWillFall(x, y + 1, z)) {
                    List<LivingEntity> item_stack = getWorld().getEntitiesByClass(LivingEntity.class, this.getBoundingBox().expand(3.0D, 1.0D, 3.0D),
                            (entity) -> {
                                return entity instanceof LivingEntity;
                            });
                }

                ItemStack var11 = this.getMainHandStack();
                if (var11 != null) {
                    var11.getItem().postMine(var11, this.getWorld(), getWorld().getBlockState(new BlockPos(x, y, z)), new BlockPos(x, y, z), this);
                }

                this.is_destroying_block = false;
                Block var12 = getWorld().getBlockState(new BlockPos(x, y + 1, z)).getBlock();

                if (var12 instanceof FallingBlock) {
                    this.is_destroying_block = true;
                    this.destroy_pause_ticks = 10;
                } else if (var12 != null && !this.blockWillFall(x, y + 1, z)) {
                    if (y == this.getBlockPos().getY() && this.canDestroyBlock(x, y + 1, z, true)) {
                        ++this.destroy_block_y;
                    } else {
                        --this.destroy_block_y;
                    }

                    this.is_destroying_block = true;
                    this.destroy_pause_ticks = 10;
                } else if (y == this.getBlockPos().getY() + 1 && !getWorld().getBlockState(BlockPos.ofFloored(this.getPos().getX(), this.getPos().getY() + 2, this.getPos().getZ())).blocksMovement() && this.canDestroyBlock(x, y - 1, z, true)) {
                    this.is_destroying_block = true;
                    this.destroy_pause_ticks = 10;
                    --this.destroy_block_y;
                }

                this.cancelBlockDestruction();
                this.getWorld().breakBlock(new BlockPos(x, y, z), true);
                Block.dropStack(this.getEntityWorld(),new BlockPos(x, y, z),new ItemStack(block.asItem()));
            }

            destroyBlock(this.getId(), x, y, z, this.destroy_block_progress, this.getEntityWorld());


            if (block.getDefaultState().isIn(BlockTags.SMELTS_TO_GLASS)) {
                getWorld().playSound((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, Blocks.GLASS.getDefaultState().getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, Blocks.GLASS.getDefaultState().getSoundGroup().getVolume() + 2.0F, Blocks.GLASS.getDefaultState().getSoundGroup().getPitch() * 1.0F, false);
            } else {
                getWorld().syncGlobalEvent(2001, new BlockPos(x, y, z), Block.getRawIdFromState(getWorld().getBlockState(new BlockPos(x, y, z))));
            }
        }
    }

    public void destroyBlock(int id, int x, int y, int z, int progress, World world) {
        getWorld().setBlockBreakingInfo(id, new BlockPos(x, y, z), progress);
    }

    public void decrementDestroyBlockCooloff() {
        destroy_block_cooloff--;
    }

    public void cancelBlockDestruction() {
        if (this.is_destroying_block) {
            destroyBlock(this.getId(), this.destroy_block_x, this.destroy_block_y, this.destroy_block_z, -1, this.getWorld());
            this.is_destroying_block = false;
            this.destroy_block_progress = -1;
            this.destroy_block_cooloff = 40;
        }
    }
}
