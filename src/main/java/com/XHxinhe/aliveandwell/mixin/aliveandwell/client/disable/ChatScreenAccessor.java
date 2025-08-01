package com.XHxinhe.aliveandwell.mixin.aliveandwell.client.disable;

import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChatScreen.class)
public interface ChatScreenAccessor {
    @Accessor("chatField")
    TextFieldWidget getChatField();
    @Accessor("chatInputSuggestor")
    ChatInputSuggestor getChatInputSuggestor();

}
