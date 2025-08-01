package com.XHxinhe.aliveandwell.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.XHxinhe.aliveandwell.util.HTTPUtils;
import com.XHxinhe.aliveandwell.util.RequestResponse;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class AuthScreen extends Screen {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected final Screen parent;
    protected TextFieldWidget searchBox;
    private String requestMessage = "";
    private ButtonWidget activeButton;

    public AuthScreen(Screen parent) {
        super(Text.translatable("auth.screen.title"));
        this.parent = parent;
    }

    public void tick() {
        this.searchBox.tick();
    }

    public static String getMachinecode(){
        try {
            Process process = Runtime.getRuntime().exec("wmic csproduct get UUID");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String finalString = "";
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                finalString += line.trim();
            }
            bufferedReader.close();
            return finalString.replace("UUID", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean setRequest(AuthScreen guiTokenChecker, String activationCode){
        try {
            String dangerWorldVersion = "1.0.0";
            Gson gson = new Gson();
            Map param = new HashMap();
            param.put("machine_code", getMachinecode());
            param.put("activation_code", activationCode);
            param.put("game", "1");
            param.put("version", dangerWorldVersion);
            String res = HTTPUtils.sendJsonPost("https://wensc.cn/mite-broadcast/activationCheck", gson.toJson(param));
            RequestResponse requestResponse = gson.fromJson(res,  RequestResponse.class);
            if(requestResponse.code == 0) {
                return true;
            } else {
                if(guiTokenChecker != null) {
                    guiTokenChecker.requestMessage = requestResponse.msg;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    protected void init() {
        this.searchBox = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 22, 200, 20, this.searchBox, Text.translatable("selectWorld.search"));
        this.searchBox.setMaxLength(36);
        this.addSelectableChild(this.searchBox);
        this.activeButton = this.addDrawableChild(ButtonWidget.builder(Text.translatable("auth.check.button"), (button) -> {
            if(this.setRequest(this, this.searchBox.getText())) {
                this.client.setScreen(this.parent);
                try
                {
                    FileWriter var7 = new FileWriter("activation-code", false);
                    StringBuffer var8 = new StringBuffer();
                    var8.append(this.searchBox.getText().trim());
                    var7.append(var8.toString());
                    var7.close();
                }
                catch (Exception var9)
                {
                    ;
                }
            }
        }).dimensions(this.width / 2 - 75 , 100, 150, 20).build());
        this.activeButton.active = false;
        this.setInitialFocus(this.searchBox);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean result = super.keyPressed(keyCode, scanCode, modifiers) || this.searchBox.keyPressed(keyCode, scanCode, modifiers);
        this.activeButton.active = !this.searchBox.getText().trim().isEmpty();
        if(keyCode == 257 || keyCode == 335){
            this.activeButton.keyPressed(keyCode, scanCode, modifiers);
        }
        return result;
    }

    public void close() {

    }


    public boolean charTyped(char chr, int modifiers) {
        boolean result = this.searchBox.charTyped(chr, modifiers);
        this.activeButton.active = !this.searchBox.getText().trim().isEmpty();
        return result;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        this.searchBox.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 8, 16777215);
        context.drawCenteredTextWithShadow(this.textRenderer, this.requestMessage, this.width / 2, 50, 16777215);
        super.render(context, mouseX, mouseY, delta);
    }
}

