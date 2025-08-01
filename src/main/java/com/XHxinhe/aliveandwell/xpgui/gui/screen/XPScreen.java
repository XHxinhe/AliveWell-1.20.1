//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

// 定义包名
package com.XHxinhe.aliveandwell.xpgui.gui.screen;

// 导入相关类
import com.XHxinhe.aliveandwell.xpgui.common.PlayerStatsManagerAccess;
import com.XHxinhe.aliveandwell.xpgui.common.XPStates;
import com.XHxinhe.aliveandwell.xpgui.network.PlayerStatsClientPacket;
import com.XHxinhe.aliveandwell.xpgui.network.PlayerStatsServerPacket;
import java.util.List;

import net.minecraft.util.Formatting;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.text.MutableText;

public class XPScreen extends Screen {
    // 定义各种经验值消耗量
    private int xpCost1 = 10;
    private int xpCost2 = 100;
    private int xpCost3 = 500;
    private int xpCost4 = 1000;
    private int xpCost5 = 5000;
    private int xpCost6 = 10000;
    // 屏幕高度
    int scaledHeight;
    private ButtonWidget buttonsPlus10;
    private ButtonWidget buttonsPlus100;
    private ButtonWidget buttonsPlus500;
    private ButtonWidget buttonsPlus1000;
    private ButtonWidget buttonsPlus5000;
    private ButtonWidget buttonsPlus10000;
    private ButtonWidget buttonsOut10;
    private ButtonWidget buttonsOut100;
    private ButtonWidget buttonsOut500;
    private ButtonWidget buttonsOut1000;
    private ButtonWidget buttonsOut5000;
    private ButtonWidget buttonsOut10000;
    private int xpBox;
    private int playerEx;
    private final XPStates xpStates;

    public XPScreen(MinecraftClient client, Text title) {
        super(title);
        this.xpStates = ((PlayerStatsManagerAccess)client.player).getPlayerStatsManager();
        this.scaledHeight = client.getWindow().getScaledHeight();
    }

    public void render(DrawContext matrices, int mouseX, int mouseY, float partialTicks) {
        super.render(matrices, mouseX, mouseY, partialTicks);

        assert this.client != null;

        int scaledWidth = 0;
        if (this.client.player != null) {
            scaledWidth = this.client.getWindow().getScaledWidth();
            int scaledHeight = this.client.getWindow().getScaledHeight();
            InventoryScreen.drawEntity(matrices, scaledWidth / 2 - 70, scaledHeight / 2 + 20, 40, (float)(-mouseX), (float)(-mouseY), this.client.player);
            matrices.drawCenteredTextWithShadow(this.textRenderer, this.xpStates.getPlayerEntity().getDisplayName().copy().formatted(Formatting.BOLD).append(Text.translatable("aliveandwell.xpgui.info1")), this.width / 2 + 30, this.scaledHeight / 2 - 90, 16777215);
            TextRenderer var10001 = this.textRenderer;
            MutableText var10002 = Text.translatable("aliveandwell.xpgui.info2");
            int var10003 = this.xpBox;
            matrices.drawTextWithShadow(var10001, var10002.append(Text.literal(var10003 + "/" + this.xpStates.GetMaxXp())).formatted(Formatting.GREEN), this.width / 2 - 20, this.scaledHeight / 2 - 75, 16777215);
            matrices.drawTextWithShadow(this.textRenderer, Text.translatable("aliveandwell.xpgui.info3").formatted(Formatting.YELLOW), this.width / 2 - 25, this.scaledHeight / 2 - 60, 16777215);
            matrices.drawTextWithShadow(this.textRenderer, Text.translatable("aliveandwell.xpgui.info4").append(Text.literal(String.valueOf(this.playerEx))).formatted(Formatting.LIGHT_PURPLE), this.width / 2 - 17, this.scaledHeight / 2 + 50, 16777215);
        }

        this.client.getTextureManager().bindTexture(new Identifier("textures/xpgui/background.png"));
        matrices.drawTexture(new Identifier("textures/xpgui/background.png"), this.width / 5 + 25, 20, 0.0F, 0.0F, scaledWidth, this.scaledHeight, scaledWidth, this.scaledHeight);
        this.buttonsPlus10.render(matrices, mouseX, mouseY, 1.0F);
        this.buttonsOut10.render(matrices, mouseX, mouseY, 1.0F);
        this.buttonsPlus100.render(matrices, mouseX, mouseY, 1.0F);
        this.buttonsOut100.render(matrices, mouseX, mouseY, 1.0F);
        this.buttonsPlus500.render(matrices, mouseX, mouseY, 1.0F);
        this.buttonsOut500.render(matrices, mouseX, mouseY, 1.0F);
        this.buttonsPlus1000.render(matrices, mouseX, mouseY, 1.0F);
        this.buttonsOut1000.render(matrices, mouseX, mouseY, 1.0F);
        this.buttonsPlus5000.render(matrices, mouseX, mouseY, 1.0F);
        this.buttonsOut5000.render(matrices, mouseX, mouseY, 1.0F);
        this.buttonsPlus10000.render(matrices, mouseX, mouseY, 1.0F);
        this.buttonsOut10000.render(matrices, mouseX, mouseY, 1.0F);
    }

    protected void init() {
        long[] lastClickTime = new long[]{0L};
        int clickInterval = 250;
        this.buttonsPlus10 = (ButtonWidget)this.addDrawableChild(new CustomButtonWidget(this.width / 2 - 25, this.scaledHeight / 2 - 45, 50, 10, Text.translatable("+" + this.xpCost1), (button) -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime[0] >= (long)clickInterval && this.xpStates.canPlus(this.xpCost1)) {
                this.buttonsPlus10.active = true;
                PlayerStatsServerPacket.writeS2CXPPacket2(this.xpStates, this.xpCost1);
                PlayerStatsClientPacket.writeC2SIncreaseLevelPacket(this.xpStates, this.xpCost1);
                lastClickTime[0] = currentTime;
            } else {
                this.buttonsPlus10.active = false;
            }
        }, List.of()));
        this.buttonsOut10 = (ButtonWidget)this.addDrawableChild(new CustomButtonWidget(this.width / 2 + 40, this.scaledHeight / 2 - 45, 50, 10, Text.translatable("-" + this.xpCost1), (button) -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime[0] >= (long)clickInterval && this.xpStates.canPlus(-this.xpCost1)) {
                this.buttonsOut10.active = true;
                PlayerStatsServerPacket.writeS2CXPPacket2(this.xpStates, -this.xpCost1);
                PlayerStatsClientPacket.writeC2SIncreaseLevelPacket(this.xpStates, -this.xpCost1);
                lastClickTime[0] = currentTime;
            } else {
                this.buttonsOut10.active = false;
            }

        }, List.of()));
        this.buttonsPlus100 = (ButtonWidget)this.addDrawableChild(new CustomButtonWidget(this.width / 2 - 25, this.scaledHeight / 2 - 30, 50, 10, Text.translatable("+" + this.xpCost2), (button) -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime[0] >= (long)clickInterval && this.xpStates.canPlus(this.xpCost2)) {
                this.buttonsPlus100.active = true;
                PlayerStatsServerPacket.writeS2CXPPacket2(this.xpStates, this.xpCost2);
                PlayerStatsClientPacket.writeC2SIncreaseLevelPacket(this.xpStates, this.xpCost2);
                lastClickTime[0] = currentTime;
            } else {
                this.buttonsPlus100.active = false;
            }

        }, List.of()));
        this.buttonsOut100 = (ButtonWidget)this.addDrawableChild(new CustomButtonWidget(this.width / 2 + 40, this.scaledHeight / 2 - 30, 50, 10, Text.translatable("-" + this.xpCost2), (button) -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime[0] >= (long)clickInterval && this.xpStates.canPlus(-this.xpCost2)) {
                this.buttonsOut100.active = true;
                PlayerStatsServerPacket.writeS2CXPPacket2(this.xpStates, -this.xpCost2);
                PlayerStatsClientPacket.writeC2SIncreaseLevelPacket(this.xpStates, -this.xpCost2);
                lastClickTime[0] = currentTime;
            } else {
                this.buttonsOut100.active = false;
            }

        }, List.of()));
        this.buttonsPlus500 = (ButtonWidget)this.addDrawableChild(new CustomButtonWidget(this.width / 2 - 25, this.scaledHeight / 2 - 15, 50, 10, Text.translatable("+" + this.xpCost3), (button) -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime[0] >= (long)clickInterval && this.xpStates.canPlus(this.xpCost3)) {
                this.buttonsPlus500.active = true;
                PlayerStatsServerPacket.writeS2CXPPacket2(this.xpStates, this.xpCost3);
                PlayerStatsClientPacket.writeC2SIncreaseLevelPacket(this.xpStates, this.xpCost3);
                lastClickTime[0] = currentTime;
            } else {
                this.buttonsPlus500.active = false;
            }

        }, List.of()));
        this.buttonsOut500 = (ButtonWidget)this.addDrawableChild(new CustomButtonWidget(this.width / 2 + 40, this.scaledHeight / 2 - 15, 50, 10, Text.translatable("-" + this.xpCost3), (button) -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime[0] >= (long)clickInterval && this.xpStates.canPlus(-this.xpCost3)) {
                this.buttonsOut500.active = true;
                PlayerStatsServerPacket.writeS2CXPPacket2(this.xpStates, -this.xpCost3);
                PlayerStatsClientPacket.writeC2SIncreaseLevelPacket(this.xpStates, -this.xpCost3);
                lastClickTime[0] = currentTime;
            } else {
                this.buttonsOut500.active = false;
            }

        }, List.of()));
        this.buttonsPlus1000 = (ButtonWidget)this.addDrawableChild(new CustomButtonWidget(this.width / 2 - 25, this.scaledHeight / 2, 50, 10, Text.translatable("+" + this.xpCost4), (button) -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime[0] >= (long)clickInterval && this.xpStates.canPlus(this.xpCost4)) {
                this.buttonsPlus1000.active = true;
                PlayerStatsServerPacket.writeS2CXPPacket2(this.xpStates, this.xpCost4);
                PlayerStatsClientPacket.writeC2SIncreaseLevelPacket(this.xpStates, this.xpCost4);
                lastClickTime[0] = currentTime;
            } else {
                this.buttonsPlus1000.active = false;
            }

        }, List.of()));
        this.buttonsOut1000 = (ButtonWidget)this.addDrawableChild(new CustomButtonWidget(this.width / 2 + 40, this.scaledHeight / 2, 50, 10, Text.translatable("-" + this.xpCost4), (button) -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime[0] >= (long)clickInterval && this.xpStates.canPlus(-this.xpCost4)) {
                this.buttonsOut1000.active = true;
                PlayerStatsServerPacket.writeS2CXPPacket2(this.xpStates, -this.xpCost4);
                PlayerStatsClientPacket.writeC2SIncreaseLevelPacket(this.xpStates, -this.xpCost4);
                lastClickTime[0] = currentTime;
            } else {
                this.buttonsOut1000.active = false;
            }

        }, List.of()));
        this.buttonsPlus5000 = (ButtonWidget)this.addDrawableChild(new CustomButtonWidget(this.width / 2 - 25, this.scaledHeight / 2 + 15, 50, 10, Text.translatable("+" + this.xpCost5), (button) -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime[0] >= (long)clickInterval && this.xpStates.canPlus(this.xpCost5)) {
                this.buttonsPlus5000.active = true;
                PlayerStatsServerPacket.writeS2CXPPacket2(this.xpStates, this.xpCost5);
                PlayerStatsClientPacket.writeC2SIncreaseLevelPacket(this.xpStates, this.xpCost5);
                lastClickTime[0] = currentTime;
            } else {
                this.buttonsPlus5000.active = false;
            }

        }, List.of()));
        this.buttonsOut5000 = (ButtonWidget)this.addDrawableChild(new CustomButtonWidget(this.width / 2 + 40, this.scaledHeight / 2 + 15, 50, 10, Text.translatable("-" + this.xpCost5), (button) -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime[0] >= (long)clickInterval && this.xpStates.canPlus(-this.xpCost5)) {
                this.buttonsOut5000.active = true;
                PlayerStatsServerPacket.writeS2CXPPacket2(this.xpStates, -this.xpCost5);
                PlayerStatsClientPacket.writeC2SIncreaseLevelPacket(this.xpStates, -this.xpCost5);
                lastClickTime[0] = currentTime;
            } else {
                this.buttonsOut5000.active = false;
            }

        }, List.of()));
        this.buttonsPlus10000 = (ButtonWidget)this.addDrawableChild(new CustomButtonWidget(this.width / 2 - 25, this.scaledHeight / 2 + 30, 50, 10, Text.translatable("+" + this.xpCost6), (button) -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime[0] >= (long)clickInterval && this.xpStates.canPlus(this.xpCost6)) {
                this.buttonsPlus10000.active = true;
                PlayerStatsServerPacket.writeS2CXPPacket2(this.xpStates, this.xpCost6);
                PlayerStatsClientPacket.writeC2SIncreaseLevelPacket(this.xpStates, this.xpCost6);
                lastClickTime[0] = currentTime;
            } else {
                this.buttonsPlus10000.active = false;
            }

        }, List.of()));
        this.buttonsOut10000 = (ButtonWidget)this.addDrawableChild(new CustomButtonWidget(this.width / 2 + 40, this.scaledHeight / 2 + 30, 50, 10, Text.translatable("-" + this.xpCost6), (button) -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime[0] >= (long)clickInterval && this.xpStates.canPlus(-this.xpCost6)) {
                this.buttonsOut10000.active = true;
                PlayerStatsServerPacket.writeS2CXPPacket2(this.xpStates, -this.xpCost6);
                PlayerStatsClientPacket.writeC2SIncreaseLevelPacket(this.xpStates, -this.xpCost6);
                lastClickTime[0] = currentTime;
            } else {
                this.buttonsOut10000.active = false;
            }
        }, List.of()));
    }

    public void tick() {
        super.tick();
        this.xpBox = this.xpStates.getXp();
        this.playerEx = this.xpStates.getPlayerEntity().totalExperience;
        this.buttonsPlus10.active = this.xpStates.canPlus(this.xpCost1);

        this.buttonsOut10.active = this.xpStates.canPlus(-this.xpCost1);

        this.buttonsPlus100.active = this.xpStates.canPlus(this.xpCost2);

        this.buttonsOut100.active = this.xpStates.canPlus(-this.xpCost2);

        this.buttonsPlus500.active = this.xpStates.canPlus(this.xpCost3);

        this.buttonsOut500.active = this.xpStates.canPlus(-this.xpCost3);

        this.buttonsPlus1000.active = this.xpStates.canPlus(this.xpCost4);

        this.buttonsOut1000.active = this.xpStates.canPlus(-this.xpCost4);

        this.buttonsPlus5000.active = this.xpStates.canPlus(this.xpCost5);

        this.buttonsOut5000.active = this.xpStates.canPlus(-this.xpCost5);

        this.buttonsPlus10000.active = this.xpStates.canPlus(this.xpCost6);

        this.buttonsOut10000.active = this.xpStates.canPlus(-this.xpCost6);

    }

    public boolean shouldPause() {
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else {
            assert this.client != null;
            // 检查是否按下物品栏键
            if (this.client.options.inventoryKey.matchesKey(keyCode, scanCode)) {
                this.close();
                return true;
            } else {
                return super.keyPressed(keyCode, scanCode, modifiers);
            }
        }
    }
}