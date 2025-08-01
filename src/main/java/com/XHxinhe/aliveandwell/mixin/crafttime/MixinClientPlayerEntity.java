package com.XHxinhe.aliveandwell.mixin.crafttime;

import com.XHxinhe.aliveandwell.crafttime.ITimeCraftPlayer;
import com.XHxinhe.aliveandwell.crafttime.sound.SoundEventRegistry;
import com.XHxinhe.aliveandwell.crafttime.util.CraftingSpeedHelper;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity extends AbstractClientPlayerEntity implements ITimeCraftPlayer {
	@Shadow @Final protected MinecraftClient client;

    // 使用 @Unique 注解来声明这是我们新添加的字段
	@Unique
	public boolean is_crafting = false;
	@Unique
	public float craft_time = 0;
	@Unique
	public float craft_period = 0;

	public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}

	@Override
	public void setCrafting(boolean is_crafting) {
		this.is_crafting = is_crafting;
	}

	@Override
	public boolean isCrafting() {
		return this.is_crafting;
	}

	@Override
	public void setCraftTime(float craft_time) {
		this.craft_time = craft_time;
	}

	@Override
	public float getCraftTime() {
		return this.craft_time;
	}

	@Override
	public void setCraftPeriod(float craft_period) {
		this.craft_period = craft_period;
	}

	@Override
	public float getCraftPeriod() {
		return this.craft_period;
	}
	
	@Override
	public void stopCraft() {
		this.is_crafting = false;
        this.craft_time = 0.0F;
	}

	@Override
	public void startCraftWithNewPeriod(float craft_period) {
        this.craft_time = 0.0F;
		this.craft_period = craft_period;
		this.is_crafting = true;
    }

    /**
     * 每 tick 调用一次，用于更新制作进度。
     * 如果制作完成，则返回 true。
     * @param resultStack 制作配方的结果物品，用于检查是否可以继续制作。
     * @return 如果本次 tick 完成了一次制作，则返回 true，否则返回 false。
     */
	@Override
	public boolean tick(ItemStack resultStack) {

		if (this.isCrafting()) {
            // 如果配方结果为空（例如，配方失效），则停止制作
			if(resultStack.isEmpty()){
				return false;
			}

            // 如果制作时间还没到，就增加进度
			if (this.getCraftTime() < this.getCraftPeriod()) {
                // 进度增加的速度由 CraftingSpeedHelper 决定
				this.craft_time += CraftingSpeedHelper.getCraftingSpeed(this);
			}
			else if (this.getCraftTime() >= this.getCraftPeriod()) {
				this.playSound(SoundEventRegistry.finishSound, SoundCategory.PLAYERS, 0.1F, 1f);
				this.startCraftWithNewPeriod(craft_period);
				return true;
			}
		}
		return false;
	}

}
