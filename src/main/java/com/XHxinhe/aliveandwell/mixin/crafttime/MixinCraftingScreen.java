package com.XHxinhe.aliveandwell.mixin.crafttime;

import com.XHxinhe.aliveandwell.crafttime.ITimeCraftPlayer;
import com.XHxinhe.aliveandwell.crafttime.util.CraftingDifficultyHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

/**
 * 这是一个用于 CraftingScreen (工作台界面) 的 Mixin。
 * 它实现了计时合成的UI（进度条）和交互逻辑，并且根据工作台的类型提供不同的合成速度加成。
 */
@Mixin(CraftingScreen.class)
public abstract class MixinCraftingScreen extends HandledScreen<CraftingScreenHandler> {
	@Unique
	private ITimeCraftPlayer player;
	@Unique
	private static final Identifier CRAFT_OVERLAY_TEXTURE = new Identifier("aliveandwell:textures/gui/crafting_table.png");

	public MixinCraftingScreen(CraftingScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

    /**
     * 注入代码到 drawBackground 方法的末尾，用于绘制合成进度条。
     */
	@Inject(method = "drawBackground", at = @At("TAIL"))
	protected void timecraft$drawBackground(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
		assert this.client != null;
		this.player = (ITimeCraftPlayer) this.client.player;

		RenderSystem.setShaderTexture(0,CRAFT_OVERLAY_TEXTURE);
		int i = this.x;
		int j = (this.height - this.backgroundHeight) / 2;
		if (player.isCrafting() && player.getCraftPeriod() > 0) {
			int l = (int) ((player.getCraftTime() * 24.0F / player.getCraftPeriod()));
			context.drawTexture(CRAFT_OVERLAY_TEXTURE, i + 89, j + 35, 0, 0, l + 1, 16, 24, 17);
		}
	}

    /**
     * 注入代码到 handledScreenTick 方法的末尾，用于每 tick 更新合成逻辑。
     */
	@Inject(method = "handledScreenTick", at = @At("TAIL"))
	public void timecraft$tick(CallbackInfo info) {
		assert this.client != null;
		this.player = (ITimeCraftPlayer) this.client.player;
		ItemStack resultStack = this.handler.getSlot(0).getStack();
		boolean finished = player.tick(resultStack);

		Text text = this.title;
		float p = 1.0f;
		if(text.equals(Text.translatable("container.copper_crafting"))){
			p -= 0.1f;//铜工作台减10%
		}
		if(text.equals(Text.translatable("container.iron_crafting"))){
			p -= 0.2f;//铁工作台减20%
		}
		if(text.equals(Text.translatable("container.diamond_crafting"))){
			p -= 0.3f;//钻石工作台减30%
		}
		if(text.equals(Text.translatable("container.netherite_crafting"))){
			p -= 0.9f;//下界合金工作台减90%
		}

		if (finished) {
			ArrayList<Item> old_recipe = CraftingDifficultyHelper.getItemFromMatrix(this.handler, true);
			super.onMouseClick(this.handler.getSlot(0), 0, 0, SlotActionType.THROW);

			ArrayList<Item> new_recipe = CraftingDifficultyHelper.getItemFromMatrix(this.handler, true);

			if (old_recipe.equals(new_recipe) )
				this.player.setCraftPeriod(CraftingDifficultyHelper.getCraftingDifficultyFromMatrix(this.handler, true) *p);
			else
				this.player.stopCraft();
		}
	}

    /**
     * 注入代码到 onMouseClick 方法的开头，用于拦截点击事件。
     */
	@Inject(method = "onMouseClick", at = @At("HEAD"), cancellable = true)
	public void timecraft$onMouseClick(Slot slot, int invSlot, int clickData, SlotActionType actionType,
									   CallbackInfo info) {

		Text text = this.title;
		float p = 1.0f;
		if(text.equals(Text.translatable("container.copper_crafting"))){
			p -= 0.1f;//铜工作台减10%
		}
		if(text.equals(Text.translatable("container.iron_crafting"))){
			p -= 0.2f;//铁工作台减20%
		}
		if(text.equals(Text.translatable("container.diamond_crafting"))){
			p -= 0.3f;//钻石工作台减30%
		}
		if(text.equals(Text.translatable("container.netherite_crafting"))){
			p -= 0.6f;//下界合金工作台减60%
		}

		if (slot != null) {
			invSlot = slot.id;
		}
		if (invSlot > 0 && invSlot < 10) {
			this.player.setCraftTime(0);
			this.player.setCrafting(false);
		}
		if (invSlot == 0) {
			if (! this.player.isCrafting() ) {
				this.player.startCraftWithNewPeriod(CraftingDifficultyHelper.getCraftingDifficultyFromMatrix(this.handler, true) * p);
			}
			info.cancel();
		}
	}
}
