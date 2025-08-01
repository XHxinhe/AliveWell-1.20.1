package com.XHxinhe.aliveandwell.mixin.aliveandwell.client.disable;

import com.XHxinhe.aliveandwell.miningsblock.MiningPlayers;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin extends Screen{
    @Unique
    private Text timeText;
    @Shadow private int ticksSinceDeath;
    @Mutable
    @Final
    @Shadow private final Text message;
    @Mutable
    @Final
    @Shadow private final boolean isHardcore;
    @Shadow private Text scoreText;

    @Final
    @Shadow private final List<ButtonWidget> buttons = Lists.newArrayList();
    @Shadow
    @Nullable
    private ButtonWidget titleScreenButton;

    protected DeathScreenMixin(Text title, Text message, boolean isHardcore) {
        super(title);
        this.message = message;
        this.isHardcore = isHardcore;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void init() {
        this.timeText = Text.translatable("aliveandwell.deathscreen.info1").append(": ").append(Text.literal(Integer.toString(this.client.player.getScore())).formatted(Formatting.YELLOW));

        this.ticksSinceDeath = 0;
        this.buttons.clear();
        MutableText text = this.isHardcore ? Text.translatable("deathScreen.spectate") : Text.translatable("deathScreen.respawn");
        this.buttons.add(this.addDrawableChild(ButtonWidget.builder(text, button -> {
            this.client.player.requestRespawn();
            MiningPlayers.timeDead = 2400;//++++++++++++++++
            button.active = false;
        }).dimensions(this.width / 2 - 100, this.height / 4 + 72, 200, 20).build()));
        this.titleScreenButton = this.addDrawableChild(ButtonWidget.builder(Text.translatable("deathScreen.titleScreen"), button -> this.client.getAbuseReportContext().tryShowDraftScreen(this.client, this, this::onTitleScreenButtonClicked, true)).dimensions(this.width / 2 - 100, this.height / 4 + 96, 200, 20).build());
        this.buttons.add(this.titleScreenButton);
        this.setButtonsActive(false);
        this.scoreText = Text.translatable("deathScreen.score").append(": ").append(Text.literal(Integer.toString(this.client.player.getScore())).formatted(Formatting.YELLOW));
    }

    @Inject(at = @At("HEAD"), method = "render")
    public void render(DrawContext matrices, int mouseX, int mouseY, float delta, CallbackInfo info) {
        matrices.drawCenteredTextWithShadow(this.textRenderer, this.timeText, this.width / 2, 110, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo info) {
        if (this.client != null && this.client.player != null && this.client.player.isDead()) {
            MiningPlayers.timeDead--;
        }

        this.timeText = Text.translatable("aliveandwell.deathscreen.info1").append(": ").append(Text.literal(Integer.toString(MiningPlayers.timeDead/20)).append(Text.translatable("s")).formatted(Formatting.YELLOW));
        if(MiningPlayers.timeDead /20 == 0){
            if (this.client != null) {
                if (this.client.player != null) {
                    this.client.player.requestRespawn();
                }
            }
            MiningPlayers.timeDead = 2400;
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if ( keyCode == GLFW.GLFW_KEY_T || keyCode == GLFW.GLFW_KEY_SLASH) {
            if (client != null) {
                client.setScreen(new ChatScreen(""));
            }
        }
        return true;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void onTitleScreenButtonClicked() {
        this.quitLevel();
    }
    @Shadow
    private void quitLevel() {
        if (this.client.world != null) {
            this.client.world.disconnect();
        }
        this.client.disconnect(new MessageScreen(Text.translatable("menu.savingLevel")));
        this.client.setScreen(new TitleScreen());
    }

    @Shadow
    private void setButtonsActive(boolean active) {
        for (ButtonWidget buttonWidget : this.buttons) {
            buttonWidget.active = active;
        }
    }
}

