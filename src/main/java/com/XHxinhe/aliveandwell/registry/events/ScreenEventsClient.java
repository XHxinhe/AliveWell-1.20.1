package com.XHxinhe.aliveandwell.registry.events; // 声明包名

import net.fabricmc.api.EnvType; // 导入环境类型枚举
import net.fabricmc.api.Environment; // 导入环境注解
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents; // 导入Fabric屏幕事件API
import net.fabricmc.fabric.api.client.screen.v1.Screens; // 导入Fabric屏幕工具类
import net.minecraft.client.gui.screen.OpenToLanScreen; // 导入局域网开启界面类


@Environment(EnvType.CLIENT) // 声明该类只在客户端环境下生效
public class ScreenEventsClient { // 定义ScreenEventsClient类
    public static void init() { // 静态初始化方法
        //=================================Client=====================================================================
        // 局域网作弊：生存模式按钮禁用
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> { // 注册屏幕初始化后的事件监听器
            if(screen instanceof OpenToLanScreen){ // 如果当前屏幕是局域网开启界面
                Screens.getButtons(screen).get(0).active = false; // 禁用第0个按钮（通常是“生存模式”按钮）
            }
        });
        // 局域网作弊：作弊按钮禁用
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> { // 再次注册屏幕初始化后的事件监听器
            if(screen instanceof OpenToLanScreen){ // 如果当前屏幕是局域网开启界面
                Screens.getButtons(screen).get(1).active = false; // 禁用第1个按钮（通常是“允许作弊”按钮）
            }
        });
    }
}