package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.dimensions.DimsRegistry;
import com.XHxinhe.aliveandwell.registry.ItemInit;
import com.XHxinhe.aliveandwell.util.PlayerEquipUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow protected abstract void initDataTracker();

    @Shadow public abstract void readCustomDataFromNbt(NbtCompound nbt);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }


    @Inject(at=@At("HEAD"), method="getJumpVelocity", cancellable = true)
    private void getJumpVelocity(CallbackInfoReturnable<Float> ca) {
        if((LivingEntity)(Object)this instanceof PlayerEntity){
            ca.setReturnValue(0.42F * this.getJumpVelocityMultiplier()+0.045f);//0.42F
        }
    }

    @Inject(at=@At("HEAD"), method="computeFallDamage", cancellable = true)
    public void computeFallDamage(float fallDistance, float damageMultiplier,CallbackInfoReturnable<Integer> ca) {
        if (this.getType().isIn(EntityTypeTags.FALL_DAMAGE_IMMUNE)) {
            ca.setReturnValue(0);
        }
        StatusEffectInstance statusEffectInstance = this.getStatusEffect(StatusEffects.JUMP_BOOST);
        float f = statusEffectInstance == null ? 0.0f : (float)(statusEffectInstance.getAmplifier() + 1.0f);
        if(fallDistance > 6 ){
            ca.setReturnValue((int) (MathHelper.ceil((fallDistance - 3.0f - f) * damageMultiplier) * 3));
        }else {
            ca.setReturnValue((int) (MathHelper.ceil((fallDistance - 3.0f - f) * damageMultiplier) * 1.2));
        }
    }

    @Inject(method = "getMaxHealth", at = @At(value = "RETURN"), cancellable = true)
    public void setMaxHealth(CallbackInfoReturnable<Float> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (livingEntity instanceof PlayerEntity player) {
            int highestTier = (int)Math.floor((double) Math.min(player.experienceLevel, 200) / 5) * 2;
            cir.setReturnValue(cir.getReturnValue() + highestTier);
        }
    }

    @Inject(method = "getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D", at = @At(value = "TAIL"), cancellable = true)
    public void overridePlayerAttack(EntityAttribute attribute, CallbackInfoReturnable<Double> cir) {
        if (attribute.equals(EntityAttributes.GENERIC_ATTACK_DAMAGE)) {
            LivingEntity livingEntity = (LivingEntity) (Object) this;
            if (livingEntity instanceof PlayerEntity player) {
                int highestTier = (int)Math.floor((double) Math.min(player.experienceLevel, 200) / 5) * 2;
                double bonusDamage = (double) highestTier /10;
                cir.setReturnValue(cir.getReturnValue() + bonusDamage);
            }
        }
    }

    @Shadow
    @Nullable
    public StatusEffectInstance getStatusEffect(StatusEffect effect) {
        return null;
    }

    @Inject(at=@At("TAIL"), method="modifyAppliedDamage", cancellable = true)
    private void modifyAppliedDamage(DamageSource source, float amount,CallbackInfoReturnable<Float> ca) {
        //被攻击的是玩家，即玩家所受伤害
        if ((LivingEntity) (Object) this instanceof PlayerEntity player) {

            float overwoldB = (float) AliveAndWellMain.day * 0.006f;
            float otherB = (float) AliveAndWellMain.day * 0.0002f;

            float a = 1.0f;
            float overwold = overwoldB * a;
            float other = otherB * a;


            if (overwold <= 0) overwold = 0;
            if (other <= 0) other = 0;

            if (overwold >= 3) overwold = 3;
            if (other >= 3) other = 3;

            Entity entity = source.getAttacker();
            if (entity != null) {
                if (player.getWorld().getRegistryKey() == DimsRegistry.UNDER_WORLD_KEY) {
                    amount = amount * 3.8F * (1.0F + otherB);
                } else if (player.getWorld().getRegistryKey() == World.NETHER) {
                    amount *= 4.2F;
                } else if (player.getWorld().getRegistryKey() == World.OVERWORLD) {
                    amount *= 1.2F + overwold;
                } else if (!player.getWorld().getRegistryKey().getValue().toString().contains("twilightforest") && !player.getWorld().getRegistryKey().getValue().toString().contains("paradise_lost")) {
                    if (player.getWorld().getRegistryKey().getValue().toString().contains("ad_astra")) {
                        amount *= 2.2F;
                    } else if (player.getWorld().getRegistryKey().getValue().toString().contains("minecells")) {
                        amount *= 12.0F;
                    } else if (player.getWorld().getRegistryKey().getValue().toString().contains("deeperdarker") && !entity.getUuidAsString().toString().contains("minecraft.warden")) {
                        amount *= 4.0F;
                    } else {
                        amount *= 2.2F + other;
                    }
                } else {
                    amount *= 3.8F;
                    if (entity instanceof HostileEntity) {
                        amount *= 1.5F;
                    }
                }

                String name1 = Registries.ENTITY_TYPE.getId(entity.getType()).toString();
                if (name1.contains("bosses_of_mass_destruction:gauntlet") || name1.contains("bosses_of_mass_destruction:lich") || name1.contains("minecraft:wither") && !name1.contains("skeleton")) {
                    amount *= 8.0F;
                }

                if (name1.contains("adventurez:stone_golem")) {
                    amount *= 8.0F;
                }

                if (name1.contains("doom:icon_of_sin")) {
                    amount *= 8.0F;
                }

                if (name1.contains("doom:arch_maykr")) {
                    amount *= 8.0F;
                }

                if (name1.contains("doom:gladiator")) {
                    amount *= 8.0F;
                }

                if (name1.contains("doom:motherdemon")) {
                    amount *= 8.0F;
                }

                if (name1.contains("bosses_of_mass_destruction:void_blossom")) {
                    amount *= 8.0F;
                }

                if (name1.contains("bosses_of_mass_destruction:obsidilith")) {
                    amount *= 8.0F;
                }

                if (name1.contains("soulsweapons:chaos_monarch") || name1.contains("soulsweapons:draugr_boss") || name1.contains("soulsweapons:returning_knight") || name1.contains("soulsweapons:accursed_lord_boss") || name1.contains("soulsweapons:moonknight")) {
                    amount *= 8.0F;
                }

                if (name1.contains("mobz:bowman")) {
                    amount /= 2.0F;
                }
            }

            if (PlayerEquipUtil.getWearingDoomArmorCount(player) == 1) {
                amount -= amount * 0.2F;
            } else if (PlayerEquipUtil.getWearingDoomArmorCount(player) == 2) {
                amount -= amount * 0.3F;
            } else if (PlayerEquipUtil.getWearingDoomArmorCount(player) == 3) {
                amount -= amount * 0.4F;
            } else if (PlayerEquipUtil.getWearingDoomArmorCount(player) == 4) {
                amount -= amount * 0.6F;
            }

            if (PlayerEquipUtil.getWearingAamanArmorCount(player) == 1) {
                amount -= amount * 0.1F;
            } else if (PlayerEquipUtil.getWearingAamanArmorCount(player) == 2) {
                amount -= amount * 0.15F;
            } else if (PlayerEquipUtil.getWearingAamanArmorCount(player) == 3) {
                amount -= amount * 0.2F;
            } else if (PlayerEquipUtil.getWearingAamanArmorCount(player) == 4) {
                amount -= amount * 0.25F;
            }

            ca.setReturnValue(amount);
            Entity var13 = source.getAttacker();
            if (var13 instanceof PlayerEntity) {
                player = (PlayerEntity)var13;
                int count = Objects.requireNonNull(player.getServer()).getCurrentPlayerCount();
                String name = Registries.ENTITY_TYPE.getId(this.getType()).toString();
                if (count > 8) {
                    if (name.contains("doom:arch_maykr") || name.contains("minecells:conjunctivius") || name.contains("bosses_of_mass_destruction:gauntlet") || name.contains("doom:gladiator") || name.contains("doom:motherdemon") || name.contains("adventurez:stone_golem") || name.contains("bosses_of_mass_destruction:lich") || name.contains("minecraft:wither") && !name.contains("skeleton") || name.contains("lich") || name.contains("naga") || name.contains("snow_queen") || name.contains("mored_giant") || name.contains("yeti") || name.contains("minoshroom")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ADAMANTIUM_SWORD && player.getOffHandStack().getItem() != ItemInit.ADAMANTIUM_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 8.0F;
                    }

                    if (name.contains("drongon") && name.contains("minecraft:")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ANCIENT_SWORD && player.getOffHandStack().getItem() != ItemInit.ANCIENT_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 5.0F;
                    }

                    if (name.contains("doom:icon_of_sin")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ANCIENT_SWORD && player.getOffHandStack().getItem() != ItemInit.ANCIENT_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 8.0F;
                    }

                    if (name.contains("soulsweapons:chaos_monarch") || name.contains("soulsweapons:draugr_boss") || name.contains("soulsweapons:accursed_lord_boss") || name.contains("soulsweapons:moonknight")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ADAMANTIUM_SWORD && player.getOffHandStack().getItem() != ItemInit.ADAMANTIUM_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 9.0F;
                    }

                    if (name.contains("soulsweapons:returning_knight")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ADAMANTIUM_SWORD && player.getOffHandStack().getItem() != ItemInit.ADAMANTIUM_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 8.0F;
                    }

                    if (name.contains("invade:raider_knight") || name.contains("illagerinvasion:invokert")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ANCIENT_SWORD && player.getOffHandStack().getItem() != ItemInit.ANCIENT_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 8.0F;
                    }

                    if (name.contains("bosses_of_mass_destruction:obsidilith")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ANCIENT_SWORD && player.getOffHandStack().getItem() != ItemInit.ANCIENT_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 8.0F;
                    }

                    if (name.contains("bosses_of_mass_destruction:void_blossom")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ANCIENT_SWORD && player.getOffHandStack().getItem() != ItemInit.ANCIENT_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 8.0F;
                    }
                } else if (count > 6) {
                    // 相同的boss检查逻辑，但是伤害减免改为 /= 7.0F
                    if (name.contains("doom:arch_maykr") || name.contains("minecells:conjunctivius") || name.contains("bosses_of_mass_destruction:gauntlet") || name.contains("doom:gladiator") || name.contains("doom:motherdemon") || name.contains("adventurez:stone_golem") || name.contains("bosses_of_mass_destruction:lich") || name.contains("minecraft:wither") && !name.contains("skeleton") || name.contains("lich") || name.contains("naga") || name.contains("snow_queen") || name.contains("mored_giant") || name.contains("yeti") || name.contains("minoshroom")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ADAMANTIUM_SWORD && player.getOffHandStack().getItem() != ItemInit.ADAMANTIUM_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 7.0F;
                    }

                    if (name.contains("drongon") && name.contains("minecraft:")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ANCIENT_SWORD && player.getOffHandStack().getItem() != ItemInit.ANCIENT_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 5.0F;
                    }

                    if (name.contains("doom:icon_of_sin")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ANCIENT_SWORD && player.getOffHandStack().getItem() != ItemInit.ANCIENT_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 7.0F;
                    }

                    if (name.contains("soulsweapons:chaos_monarch") || name.contains("soulsweapons:draugr_boss") || name.contains("soulsweapons:accursed_lord_boss") || name.contains("soulsweapons:moonknight")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ADAMANTIUM_SWORD && player.getOffHandStack().getItem() != ItemInit.ADAMANTIUM_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 7.0F;
                    }

                    if (name.contains("soulsweapons:returning_knight")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ADAMANTIUM_SWORD && player.getOffHandStack().getItem() != ItemInit.ADAMANTIUM_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 7.0F;
                    }

                    if (name.contains("invade:raider_knight") || name.contains("illagerinvasion:invokert")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ANCIENT_SWORD && player.getOffHandStack().getItem() != ItemInit.ANCIENT_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 7.0F;
                    }

                    if (name.contains("bosses_of_mass_destruction:obsidilith")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ANCIENT_SWORD && player.getOffHandStack().getItem() != ItemInit.ANCIENT_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 7.0F;
                    }

                    if (name.contains("bosses_of_mass_destruction:void_blossom")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ANCIENT_SWORD && player.getOffHandStack().getItem() != ItemInit.ANCIENT_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 7.0F;
                    }
                } else if (count > 4) {
                    // count > 4 的情况，伤害减免为 /= 6.0F
                    if (name.contains("doom:arch_maykr") || name.contains("minecells:conjunctivius") || name.contains("bosses_of_mass_destruction:gauntlet") || name.contains("doom:gladiator") || name.contains("doom:motherdemon") || name.contains("adventurez:stone_golem") || name.contains("bosses_of_mass_destruction:lich") || name.contains("minecraft:wither") && !name.contains("skeleton") || name.contains("lich") || name.contains("naga") || name.contains("snow_queen") || name.contains("mored_giant") || name.contains("yeti") || name.contains("minoshroom")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ADAMANTIUM_SWORD && player.getOffHandStack().getItem() != ItemInit.ADAMANTIUM_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 6.0F;
                    }

                    if (name.contains("drongon") && name.contains("minecraft:")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ANCIENT_SWORD && player.getOffHandStack().getItem() != ItemInit.ANCIENT_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 4.0F;
                    }

                    if (name.contains("doom:icon_of_sin")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ANCIENT_SWORD && player.getOffHandStack().getItem() != ItemInit.ANCIENT_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 6.0F;
                    }

                    if (name.contains("soulsweapons:chaos_monarch") || name.contains("soulsweapons:draugr_boss") || name.contains("soulsweapons:accursed_lord_boss") || name.contains("soulsweapons:moonknight")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ADAMANTIUM_SWORD && player.getOffHandStack().getItem() != ItemInit.ADAMANTIUM_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 6.0F;
                    }

                    if (name.contains("soulsweapons:returning_knight")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ADAMANTIUM_SWORD && player.getOffHandStack().getItem() != ItemInit.ADAMANTIUM_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 6.0F;
                    }

                    if (name.contains("invade:raider_knight") || name.contains("illagerinvasion:invokert")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ANCIENT_SWORD && player.getOffHandStack().getItem() != ItemInit.ANCIENT_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 6.0F;
                    }

                    if (name.contains("bosses_of_mass_destruction:obsidilith")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ANCIENT_SWORD && player.getOffHandStack().getItem() != ItemInit.ANCIENT_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 6.0F;
                    }

                    if (name.contains("bosses_of_mass_destruction:void_blossom")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ANCIENT_SWORD && player.getOffHandStack().getItem() != ItemInit.ANCIENT_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 6.0F;
                    }
                } else {
                    // count <= 4 的情况，伤害减免为 /= 4.0F (除了drongon是 /= 3.0F)
                    if (name.contains("doom:arch_maykr") || name.contains("minecells:conjunctivius") || name.contains("bosses_of_mass_destruction:gauntlet") || name.contains("doom:gladiator") || name.contains("doom:motherdemon") || name.contains("adventurez:stone_golem") || name.contains("bosses_of_mass_destruction:lich") || name.contains("minecraft:wither") && !name.contains("skeleton") || name.contains("lich") || name.contains("naga") || name.contains("snow_queen") || name.contains("mored_giant") || name.contains("yeti") || name.contains("minoshroom")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ADAMANTIUM_SWORD && player.getOffHandStack().getItem() != ItemInit.ADAMANTIUM_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 4.0F;
                    }

                    if (name.contains("drongon") && name.contains("minecraft:")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ANCIENT_SWORD && player.getOffHandStack().getItem() != ItemInit.ANCIENT_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 3.0F;
                    }

                    if (name.contains("doom:icon_of_sin")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ANCIENT_SWORD && player.getOffHandStack().getItem() != ItemInit.ANCIENT_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 4.0F;
                    }

                    if (name.contains("soulsweapons:chaos_monarch") || name.contains("soulsweapons:draugr_boss") || name.contains("soulsweapons:accursed_lord_boss") || name.contains("soulsweapons:moonknight")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ANCIENT_SWORD && player.getOffHandStack().getItem() != ItemInit.ANCIENT_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 4.0F;
                    }

                    if (name.contains("soulsweapons:returning_knight")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ANCIENT_SWORD && player.getOffHandStack().getItem() != ItemInit.ANCIENT_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 4.0F;
                    }

                    if (name.contains("invade:raider_knight") || name.contains("illagerinvasion:invokert")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ANCIENT_SWORD && player.getOffHandStack().getItem() != ItemInit.ANCIENT_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 4.0F;
                    }

                    if (name.contains("bosses_of_mass_destruction:obsidilith")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ANCIENT_SWORD && player.getOffHandStack().getItem() != ItemInit.ANCIENT_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 4.0F;
                    }

                    if (name.contains("bosses_of_mass_destruction:void_blossom")) {
                        if (player.getMainHandStack().getItem() != ItemInit.ANCIENT_SWORD && player.getOffHandStack().getItem() != ItemInit.ANCIENT_SWORD) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 12000, 3));
                        }
                        amount /= 4.0F;
                    }
                }
            }
            ca.setReturnValue(amount);
        }
    }
    @Inject(at=@At("HEAD"), method="damage", cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getAttacker() instanceof PlayerEntity){
            String name = Registries.ENTITY_TYPE.getId(this.getType()).toString();
            if (name.contains("rlovelyr:")) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(at=@At("TAIL"), method="onDeath")
    public void onDeath(DamageSource damageSource,CallbackInfo ca) {
        if (this.getWorld() instanceof ServerWorld) {
            Entity entity = damageSource.getAttacker();
            if(entity instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) entity;
                if((LivingEntity) (Object)this instanceof HostileEntity){
                    if(this.onKilledOther((ServerWorld)this.getWorld(),(LivingEntity) (Object)player) ){
                        if(random.nextInt(75)+1  <= 3){
                            ItemStack stack = new ItemStack(ItemInit.REBORN_STONE);
                            setNbt(stack,(LivingEntity) (Object)this);
                            ((LivingEntity) (Object)this).dropStack(stack,1);
                        }
                    }
                }
                if((LivingEntity) (Object)this instanceof WitherSkeletonEntity){
                    if(this.onKilledOther((ServerWorld)this.getWorld(),(LivingEntity) (Object)player) ){
                        if(random.nextInt(40)+1  <= 3){
                            ((LivingEntity) (Object)this).dropStack(new ItemStack(ItemInit.SKELETON_CORE,1));
                        }
                    }
                }
            }
        }
    }
    @Unique
    public void setNbt(ItemStack itemStack,LivingEntity entity) {
        NbtCompound nbt;
        if(!itemStack.hasNbt()){
            nbt = new NbtCompound();
            nbt.putString("aliveandwell_reborn_stone",this.getUuidAsString());
        }else {
            nbt = itemStack.getNbt();
        }
        itemStack.setNbt(nbt);
    }
}
