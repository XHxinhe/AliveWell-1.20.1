package com.XHxinhe.aliveandwell.crafttime.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

/**
 * Mod自定义声音事件的注册与存储类。
 * <p>
 * 这个类用于定义和初始化Mod中需要用到的声音事件。
 */
public class SoundEventRegistry {

    /**
     * “完成”声音事件的资源标识符 (ID)。
     * 它的值 "aliveandwell:finish" 指向了资源包中 `assets/aliveandwell/sounds.json`
     * 文件里名为 "finish" 的声音定义。
     * `public static final` 确保它是一个全局可访问的常量。
     */
    public static final Identifier finishSoundID = new Identifier("aliveandwell:finish");

    /**
     * “完成”声音事件的 SoundEvent 对象。
     * 这个对象将在代码中被实际调用以播放声音。
     * 它被声明为 `public static` 以便在Mod的任何地方都能轻松访问。
     */
    public static SoundEvent finishSound;

    /**
     * 公共构造函数。
     * (最佳实践建议：对于纯静态工具类，可以将其设为 private，以防止被意外实例化。)
     */
    public SoundEventRegistry() {
    }

    // 静态初始化块 (Static Initializer Block)
    // 这段代码在JVM加载 SoundEventRegistry 类时执行，且仅执行一次。
    // 这是初始化静态字段的最佳位置。
    static {
        // 调用 SoundEvent.of() 工厂方法，根据上面定义的ID创建一个 SoundEvent 实例，
        // 并将其赋值给 finishSound 字段。
        finishSound = SoundEvent.of(finishSoundID);
    }
}