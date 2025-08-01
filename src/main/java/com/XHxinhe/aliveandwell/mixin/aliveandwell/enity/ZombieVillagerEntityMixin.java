package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;
import java.util.UUID;

@Mixin(ZombieVillagerEntity.class)
public abstract class ZombieVillagerEntityMixin extends ZombieEntity implements VillagerDataContainer {
    public ZombieVillagerEntityMixin(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if(player instanceof ServerPlayerEntity serverPlayerEntity){
            PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker(serverPlayerEntity);
            AdvancementProgress progress = null;
            Advancement advancement = serverPlayerEntity.server.getAdvancementLoader().get(Identifier.tryParse("minecraft:nether/find_fortress"));
            if (advancement != null) {
                progress = tracker.getProgress(advancement);
            }
            if (!(progress != null && progress.isDone())) {
                player.sendMessage(Text.translatable("aliveandwell.zombie_villager").formatted(Formatting.YELLOW));
                return ActionResult.SUCCESS;
            }
        }

        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Items.GOLDEN_APPLE)) {
            if (this.hasStatusEffect(StatusEffects.WEAKNESS)) {
                if (!player.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }
                if (!this.getWorld().isClient) {
                    this.setConverting(player.getUuid(), this.random.nextInt(2401) + 3600);
                }
                return ActionResult.SUCCESS;
            }
            return ActionResult.CONSUME;
        }

        if (itemStack.isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
            if (this.hasStatusEffect(StatusEffects.WEAKNESS)) {
                if (!player.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }
                if (!this.getWorld().isClient) {
                    this.setConverting(player.getUuid(), 20);
                }
                return ActionResult.SUCCESS;
            }
            return ActionResult.CONSUME;
        }

        return super.interactMob(player, hand);
    }

    @Shadow
    protected abstract void setConverting(@Nullable UUID uuid, int delay);
}
