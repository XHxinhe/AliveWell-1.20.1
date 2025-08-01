package com.XHxinhe.aliveandwell.flintcoppertool.init;

import com.XHxinhe.aliveandwell.flintcoppertool.utils.DisableWoodStoneTools;
import com.XHxinhe.aliveandwell.flintcoppertool.utils.FlintKnapEvent;

/**
 * "燧石与铜工具" 模块的事件初始化器。
 * <p>
 * 这个类的主要职责是在模组加载时，调用其他工具类的方法，
 * 以注册和激活本模块所需的所有事件监听器。
 * 它充当了功能激活的中央枢纽。
 */
public final class EventsInit { // 将其设为 final 并添加私有构造函数是良好实践

    /**
     * 私有构造函数，防止该工具类被实例化。
     */
    private EventsInit() {
    }

    /**
     * 初始化本模块的所有事件。
     * 这个方法应该在模组的主初始化阶段被调用。
     */
    public static void init() {
        // 注册用于禁用木制和石制工具等级的事件。
        // 这会改变游戏的早期进程，强迫玩家使用模组添加的工具。
        DisableWoodStoneTools.noStoneWoodTier();

        // 注册用于处理“燧石打制”机制的事件。
        // 这会为游戏添加一个新的核心玩法，用于在早期制作工具。
        FlintKnapEvent.knapEvent();
    }
}