package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.util.SkeletonEquipDiamondUtil;
import com.XHxinhe.aliveandwell.util.SkeletonEquipIronUtil;
import com.XHxinhe.aliveandwell.util.SkeletonEquipNetherUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.BowAttackGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/**
 * 这个 Mixin 对骷髅进行了全方位的增强，实现了随游戏时间推进而逐步升级的进化系统。
 * 主要改动包括：近战能力增强、远程攻击强化、装备进化系统和基础属性调整。
 * **核心功能增强**：
 * 1. **基础属性调整**：
 *    - 移动速度设为 0.26（比原版稍快）
 *    - 生命值降至 6 点（增加挑战性）
 *    - 添加了召唤增援的能力
 * 2. **视觉感知增强**：
 *    - 玩家检测范围增加到 48 格
 *    - 添加了透过缝隙攻击的能力（类似僵尸改动）
 * 3. **远程攻击增强**：
 *    - 箭矢伤害随游戏天数增加（最高1.5倍）
 *    - 箭矢击退效果随天数增强（最高5级）
 *    - 困难模式下射击间隔缩短（60刻度vs80刻度）
 * 4. **装备进化系统**：
 *    基于游戏天数(AliveAndWellMain.day)的分级：
 *    - 0-16天：原版装备（弓）
 *    - 17-32天：
 *      * 主世界：10%概率获得铁装备
 *      * 其他维度：必定获得铁装备
 *    - 33-71天：全面铁装备
 *    - 72-128天：
 *      * 主世界：铁装备
 *      * 其他维度：钻石装备
 *    - 129-160天：
 *      * 主世界：强化铁装备
 *      * 其他维度：钻石装备
 *    - 161-196天：下界装备第一阶段
 *    - 197天以后：下界装备第二阶段
 * **战斗机制改进**：
 * 1. **近战能力**：
 *    - 攻击距离增加到4.1格+目标宽度
 *    - 添加了透过障碍物攻击的能力
 *    - 保持了较快的移动速度（0.25）
 * 2. **远程作战**：
 *    - 箭矢伤害和击退随游戏进程提升
 *    - 射击精度提高（抛物线优化）
 *    - 攻击频率根据难度动态调整
 * **游戏平衡性影响**：
 * 1. **早期游戏（0-32天）**：
 *    - 相对温和的提升
 *    - 给玩家适应和发展的时间
 *    - 维度差异化设计增加探索难度
 * 2. **中期游戏（33-128天）**：
 *    - 装备质量显著提升
 *    - 需要玩家具备相应装备才能应对
 *    - 维度难度差异进一步扩大
 * 3. **后期游戏（129天以后）**：
 *    - 极具挑战性的装备配置
 *    - 需要顶级装备和战术才能应对
 *    - 下界装备带来的特殊威胁
 * **设计理念**：
 * 1. 渐进式难度提升
 * 2. 维度差异化设计
 * 3. 鼓励玩家装备发展
 * 4. 保持持续挑战性
 */
@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonEntityMixin extends HostileEntity implements RangedAttackMob {
    @Final
    @Mutable
    @Shadow
    private BowAttackGoal<AbstractSkeletonEntity> bowAttackGoal;
    @Final
    @Shadow
    private final MeleeAttackGoal meleeAttackGoal = new MeleeAttackGoal(this, 1.2, false){
        @Override
        public void stop() {
            super.stop();
            AbstractSkeletonEntityMixin.this.setAttacking(false);
        }

        @Override
        public void start() {
            super.start();
            AbstractSkeletonEntityMixin.this.setAttacking(true);
        }

        protected void attack(LivingEntity target, double squaredDistance) {
            double d = this.getSquaredMaxAttackDistance(target);
            if (squaredDistance <= d && this.cooldown <= 0 && (this.mob.canSee(target) || canSeeLeggings(target))) {
                this.resetCooldown();
                this.mob.swingHand(Hand.MAIN_HAND);
                this.mob.tryAttack(target);
            }
        }

        public boolean canSeeLeggings(Entity target) {
            if (target.getWorld() != this.mob.getWorld()) {
                return false;
            }
            Vec3d vec3d = new Vec3d(this.mob.getX(), this.mob.getEyeY()-1.14d, this.mob.getZ());
            Vec3d vec3d2 = new Vec3d(target.getX(), target.getEyeY(), target.getZ());
            if (vec3d2.distanceTo(vec3d) > 128.0) {
                return false;
            }
            return this.mob.getWorld().raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this.mob)).getType() == HitResult.Type.MISS;
        }
        protected double getSquaredMaxAttackDistance(LivingEntity entity) {
            return 4.1f + entity.getWidth();
        }
    };

    protected AbstractSkeletonEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            at=@At("HEAD"),
            method="initGoals"
    )
    public void initGoals(CallbackInfo info) {
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 48.0F));
    }

    @Inject(
            at=@At("HEAD"),
            method="createAbstractSkeletonAttributes",
            cancellable = true
    )
    private static void createAbstractSkeletonAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        info.setReturnValue(HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.26D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 6)
                .add(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS,4));
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void updateAttackType() {
        if (this.getWorld() != null && !this.getWorld().isClient) {
            ItemStack itemStack = this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW));
            if (itemStack.isOf(Items.BOW)) {
                this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25D);
            } else {
                this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25D);
            }
        }

        if (this.getWorld() == null || this.getWorld().isClient) {
            return;
        }
        this.goalSelector.remove(this.meleeAttackGoal);
        this.goalSelector.remove(this.bowAttackGoal);
        ItemStack itemStack = this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW));
        if (itemStack.isOf(Items.BOW)) {
            int i = 3*20;
            if (this.getWorld().getDifficulty() != Difficulty.HARD) {
                this.getEntityWorld().getServer().sendMessage(Text.translatable("11111111111111"));
                i = 4*20;
            }
            this.bowAttackGoal.setAttackInterval(i);
            this.goalSelector.add(4, this.bowAttackGoal);
        } else {
            this.goalSelector.add(4, this.meleeAttackGoal);
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void attack(LivingEntity target, float pullProgress) {
        ItemStack itemStack = this.getProjectileType(this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW)));
        float m = (float) AliveAndWellMain.day /100;
        if(m>=1.5f){
            m = 1.5f;
        }

        PersistentProjectileEntity persistentProjectileEntity = this.createArrowProjectile(itemStack, m);
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333) - persistentProjectileEntity.getY();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        persistentProjectileEntity.setVelocity(d, e + g * 0.20000000298023224, f, 1.6F, (float)(14 - this.getWorld().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        int i = AliveAndWellMain.day<=48 ? 0 : 1+(int)(AliveAndWellMain.day/40) ;
        if(i >= 5){
            i=5;
        }

        persistentProjectileEntity.setPunch(i);//++++++++++
        this.getWorld().spawnEntity(persistentProjectileEntity);
    }
    @Shadow
    protected PersistentProjectileEntity createArrowProjectile(ItemStack arrow, float damageModifier) {
        return ProjectileUtil.createArrowProjectile(this, arrow, damageModifier);
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void initEquipment(Random random, LocalDifficulty localDifficulty) {
        if( AliveAndWellMain.day <= 16) {
            super.initEquipment(random, localDifficulty);
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        }else if(AliveAndWellMain.day <= 32) {
            if (this.getWorld().getRegistryKey() != World.OVERWORLD) {
                SkeletonEquipIronUtil.equip((AbstractSkeletonEntity) (Object) this);
            }else {
                if(new java.util.Random().nextInt(10)+1 == 5){
                    SkeletonEquipIronUtil.equip3((AbstractSkeletonEntity) (Object) this);
                }else {
                    super.initEquipment(random, localDifficulty);
                    this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
                }

            }
        }else if(AliveAndWellMain.day < 72) {
            SkeletonEquipIronUtil.equip((AbstractSkeletonEntity)(Object)this);
        }else if( AliveAndWellMain.day <= 128) {
            if (this.getWorld().getRegistryKey() == World.OVERWORLD) {
                SkeletonEquipIronUtil.equip((AbstractSkeletonEntity) (Object) this);
            } else {
                SkeletonEquipDiamondUtil.equip((AbstractSkeletonEntity) (Object) this);
            }
        } else if (AliveAndWellMain.day <= 160) {
            if (this.getWorld().getRegistryKey() == World.OVERWORLD) {
                SkeletonEquipIronUtil.equip2((AbstractSkeletonEntity) (Object) this);
            } else {
                SkeletonEquipDiamondUtil.equip((AbstractSkeletonEntity) (Object) this);
            }
        }else if (AliveAndWellMain.day <= 196){
            SkeletonEquipNetherUtil.equip1((AbstractSkeletonEntity) (Object) this);
        }else {
            SkeletonEquipNetherUtil.equip2((AbstractSkeletonEntity) (Object) this);
        }
    }
}
