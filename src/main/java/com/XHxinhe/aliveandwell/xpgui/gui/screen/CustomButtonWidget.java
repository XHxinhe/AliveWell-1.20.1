package com.XHxinhe.aliveandwell.xpgui.gui.screen;

import java.util.List;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;

public class CustomButtonWidget extends ButtonWidget {
    private final List<Text> tooltip;
    private static final Identifier BUTTON_TEXTURE = new Identifier("textures/xpgui/custom_button.png");

    public CustomButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, List<Text> tooltip) {
        super(x, y, width, height, message, onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.tooltip = tooltip;
    }

    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();
        int textureY = this.isHovered() ? this.height : 0;
        context.drawTexture(BUTTON_TEXTURE, this.getX(), this.getY(), 0, textureY, this.width, this.height);
        context.drawText(client.textRenderer, this.getMessage(), this.getX() + this.width / 2 - client.textRenderer.getWidth(this.getMessage()) / 2 - 2, this.getY() + (this.height - 8) / 2, -1, false);
        if (this.isHovered()) {
            this.renderTooltip(context, this.tooltip, mouseX, mouseY);
        }

    }

    private void renderTooltip(DrawContext context, List<Text> tooltip, int mouseX, int mouseY) {
        context.drawTooltip(MinecraftClient.getInstance().textRenderer, tooltip, mouseX, mouseY);
    }
}
