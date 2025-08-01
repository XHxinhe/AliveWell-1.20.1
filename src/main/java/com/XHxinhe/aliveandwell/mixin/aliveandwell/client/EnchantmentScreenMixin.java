package com.XHxinhe.aliveandwell.mixin.aliveandwell.client;

import com.XHxinhe.aliveandwell.mixin.aliveandwell.screen.EnchantmentScreenHandlerAccessor;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.EnchantingPhrases;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin extends HandledScreen<EnchantmentScreenHandler> {
    @Final
    @Shadow private static final Identifier TEXTURE = new Identifier("textures/gui/container/enchanting_table.png");
    @Final
    @Shadow private static final Identifier BOOK_TEXTURE = new Identifier("textures/entity/enchanting_table_book.png");
    @Shadow public float nextPageTurningSpeed;
    @Shadow public float pageTurningSpeed;
    @Shadow  public float nextPageAngle;
    @Shadow public float pageAngle;
    @Shadow private BookModel BOOK_MODEL;

    public EnchantmentScreenMixin(EnchantmentScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Redirect(
            method = {"render"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;II)V"
            )
    )
    private void render(DrawContext instance, TextRenderer textRenderer, List<Text> text, int x, int y) {
        text.clear();
        instance.drawTooltip(textRenderer, text, x, y);
    }

    @Inject(at=@At("HEAD"), method="drawBackground", cancellable = true)
    private void drawBackground(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        this.drawBook(context, i, j, delta);
        EnchantingPhrases.getInstance().setSeed((long) ((EnchantmentScreenHandler) this.handler).getSeed());
        int k = ((EnchantmentScreenHandler) this.handler).getLapisCount();

        for (int l = 0; l < 3; ++l) {
            int m = i + 60;
            int n = m + 20;

            //显示的经验等级=====================
            int l1aa = (this.handler).enchantmentPower[l];
            int rr;
            ItemStack itemStackApple = ((EnchantmentScreenHandlerAccessor) (Object) this.handler).getEnchantSlots().getStack(0);

            if(l1aa >= 44){
                rr = (int) (5*l1aa*l1aa+5*l1aa)+100;
            }else if (itemStackApple.isOf(Items.GOLDEN_APPLE) || itemStackApple.isOf(Items.GOLDEN_CARROT)) {
                rr = 400;
            }else {
                rr = Math.max((int) (5*l1aa*l1aa+5*l1aa), 400);
            }
            //显示的经验等级=====================

            if (rr == 0) {
                context.drawTexture(TEXTURE, m, j + 14 + 19 * l, 0, 185, 108, 19);
            } else {
                String string = "" + rr;//显示的经验等级=====================
                int p = 86 - this.textRenderer.getWidth(string);
                StringVisitable stringVisitable = EnchantingPhrases.getInstance().generatePhrase(this.textRenderer, p);
                int q = 6839882;

                //金苹果和金胡萝卜在附魔台附魔高亮=====================================================
                int enClue = this.handler.enchantmentId[l];
                ItemStack itemStack = ((EnchantmentScreenHandlerAccessor) (Object) this.handler).getEnchantSlots().getStack(0);
                if (itemStack.isOf(Items.GOLDEN_APPLE) || itemStack.isOf(Items.GOLDEN_CARROT)) {
                    if(n > 0){
                        enClue = 0;
                    }
                }

                //================渲染是否高亮的条件：由experienceLevel改为totalExperience============================
                if (!(k >= l + 1 && this.client.player.totalExperience >= rr || this.client.player.getAbilities().creativeMode || enClue == -1)) {
                    context.drawTexture(TEXTURE, m, j + 14 + 19 * l, 0, 185, 108, 19);
                    context.drawTexture(TEXTURE, m + 1, j + 15 + 19 * l, 16 * l, 239, 16, 16);
                    context.drawTextWrapped(this.textRenderer, stringVisitable, n, j + 16 + 19 * l, p, (q & 16711422) >> 1);
                    q = 4226832;
                }else {
                    int r = mouseX - (i + 60);
                    int s = mouseY - (j + 14 + 19 * l);
                    if (r >= 0 && s >= 0 && r < 108 && s < 19) {
                        context.drawTexture(TEXTURE, m, j + 14 + 19 * l, 0, 204, 108, 19);
                        q = 0xFFFF80;
                    } else {
                        context.drawTexture(TEXTURE, m, j + 14 + 19 * l, 0, 166, 108, 19);
                    }
                    context.drawTexture(TEXTURE, m + 1, j + 15 + 19 * l, 16 * l, 223, 16, 16);
                    context.drawTextWrapped(this.textRenderer, stringVisitable, n, j + 16 + 19 * l, p, q);
                    q = 8453920;
                }
                context.drawTextWithShadow(this.textRenderer, string, n + 86 - this.textRenderer.getWidth(string), j + 16 + 19 * l + 7, q);
            }
            ci.cancel();
        }
    }

    @Shadow
    private void drawBook(DrawContext context, int x, int y, float delta) {
        float f = MathHelper.lerp(delta, this.pageTurningSpeed, this.nextPageTurningSpeed);
        float g = MathHelper.lerp(delta, this.pageAngle, this.nextPageAngle);
        DiffuseLighting.method_34742();
        context.getMatrices().push();
        context.getMatrices().translate((float)x + 33.0F, (float)y + 31.0F, 100.0F);
        float h = 40.0F;
        context.getMatrices().scale(-40.0F, 40.0F, 40.0F);
        context.getMatrices().multiply(RotationAxis.POSITIVE_X.rotationDegrees(25.0F));
        context.getMatrices().translate((1.0F - f) * 0.2F, (1.0F - f) * 0.1F, (1.0F - f) * 0.25F);
        float i = -(1.0F - f) * 90.0F - 90.0F;
        context.getMatrices().multiply(RotationAxis.POSITIVE_Y.rotationDegrees(i));
        context.getMatrices().multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
        float j = MathHelper.clamp(MathHelper.fractionalPart(g + 0.25F) * 1.6F - 0.3F, 0.0F, 1.0F);
        float k = MathHelper.clamp(MathHelper.fractionalPart(g + 0.75F) * 1.6F - 0.3F, 0.0F, 1.0F);
        this.BOOK_MODEL.setPageAngles(0.0F, j, k, f);
        VertexConsumer vertexConsumer = context.getVertexConsumers().getBuffer(this.BOOK_MODEL.getLayer(BOOK_TEXTURE));
        this.BOOK_MODEL.render(context.getMatrices(), vertexConsumer, 15728880, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        context.draw();
        context.getMatrices().pop();
        DiffuseLighting.enableGuiDepthLighting();
    }
}
