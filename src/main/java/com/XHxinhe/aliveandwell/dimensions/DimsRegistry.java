package com.XHxinhe.aliveandwell.dimensions;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

/**
 * 维度注册表（混淆名替换版）
 * <p>
 * 这个类用于定义和注册模组自定义维度的键（Keys）。
 * 这些键是模组中其他部分引用该维度的唯一标识符。
 */
public class DimsRegistry {

    /**
     * 自定义维度的世界键 (World Key)。
     * 用于在代码中获取此维度的 World 实例。
     */
    public static final RegistryKey<World> UNDER_WORLD_KEY;

    /**
     * 自定义维度的类型键 (Dimension Type Key)。
     * 用于引用该维度的具体属性，如光照、天空效果等。
     */
    public static final RegistryKey<DimensionType> UNDER_WORLD_KEY_TYPE;

    public DimsRegistry() {
    }

    /**
     * 一个空的设置方法，可能用于在主类中调用以确保静态代码块被执行。
     */
    public static void setupDimension() {
        // 这个方法的调用会触发静态初始化块的执行。
    }

    // 静态初始化块，在类加载时执行一次，用于创建和赋值注册键。
    static {
        // 创建一个指向 World 注册表的键，使用模组的维度ID作为标识符。
        UNDER_WORLD_KEY = RegistryKey.of(RegistryKeys.WORLD, AliveAndWellMain.MOD_DIMENSION_ID);

        // 创建一个指向 DimensionType 注册表的键，同样使用模组的维度ID。
        UNDER_WORLD_KEY_TYPE = RegistryKey.of(RegistryKeys.DIMENSION_TYPE, AliveAndWellMain.MOD_DIMENSION_ID);
    }
}