package com.XHxinhe.aliveandwell.mixin.crafttime;

import com.XHxinhe.aliveandwell.crafttime.ITimeCraftPlayer;
import com.XHxinhe.aliveandwell.crafttime.util.CraftingDifficultyHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
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
 * 这是一个用于 InventoryScreen (玩家物品栏界面) 的 Mixin，
 * 目的是添加基于时间的合成UI和逻辑。这个类负责绘制进度条，
 * 并处理点击事件来开始/停止合成过程。
 */
@Mixin(InventoryScreen.class)
public abstract class MixinInventoryScreen extends AbstractInventoryScreen<PlayerScreenHandler> {

	@Unique
	private ITimeCraftPlayer player;
	@Unique
	private static final Identifier CRAFT_OVERLAY_TEXTURE = new Identifier("aliveandwell:textures/gui/inventory.png");

	public MixinInventoryScreen(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, text);
	}

    /**
     * 注入代码到 drawBackground 方法的末尾，用于绘制合成进度条。
     */
	@Inject(method = "drawBackground", at = @At("TAIL"))
	protected void timecraft$drawBackground(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
		this.player = (ITimeCraftPlayer) this.client.player;

		RenderSystem.setShaderTexture(0,CRAFT_OVERLAY_TEXTURE);
		int i = this.x;// 界面左上角的X坐标
		int j = this.y;// 界面左上角的Y坐标
		// 只有当玩家正在合成且合成周期大于0时，才绘制进度条
		if (player.isCrafting() && player.getCraftPeriod() > 0) {
			// 根据当前的合成时间相对于总时间的比例，计算进度条的宽度
			int l = (int) ((player.getCraftTime() * 17.0F / player.getCraftPeriod()));
			// 绘制进度条纹理
			context.drawTexture(CRAFT_OVERLAY_TEXTURE, i + 134, j + 29, 0, 0, l + 1, 14, 18, 15);
		}
	}

    /**
     * 注入代码到 handledScreenTick 方法的末尾，用于每 tick 更新合成进度。
     */
	@Inject(method = "handledScreenTick", at = @At("TAIL"))
	public void timecraft$tick(CallbackInfo info) {
		this.player = (ITimeCraftPlayer) this.client.player;
		// 获取结果格子中的当前物品
		ItemStack resultStack = this.handler.getSlot(0).getStack();
		// 更新玩家的合成逻辑。如果本次tick完成了一次合成，'finished'会变为true。
		boolean finished = player.tick(resultStack);
		if (finished) {
			ArrayList<Item> old_recipe = CraftingDifficultyHelper.getItemFromMatrix(this.handler, false);
			super.onMouseClick(this.handler.getSlot(0), 0, 0, SlotActionType.THROW);
			ArrayList<Item> new_recipe = CraftingDifficultyHelper.getItemFromMatrix(this.handler, false);
			if (old_recipe.equals(new_recipe) )
				player.setCraftPeriod(CraftingDifficultyHelper.getCraftingDifficultyFromMatrix(this.handler, false));
			else
				player.stopCraft();
		}
	}

    /**
     * 注入代码到 onMouseClick 方法的开头，用于拦截对合成网格和结果格子的点击。
     * cancellable = true 表示我们可以取消原始的点击事件。
     */
	@Inject(method = "onMouseClick", at = @At("HEAD"), cancellable = true)
	public void timecraft$onMouseClick(Slot slot, int invSlot, int clickData, SlotActionType actionType,
			CallbackInfo info) {
		if (slot != null) {
			invSlot = slot.id;
		}
		if (invSlot > 0 && invSlot < 5) {
			player.stopCraft();
		}
		if (invSlot == 0) {
			if (!player.isCrafting()) {
				player.startCraftWithNewPeriod(CraftingDifficultyHelper.getCraftingDifficultyFromMatrix(this.handler, false));
			}
			info.cancel();
		}
	}
}
