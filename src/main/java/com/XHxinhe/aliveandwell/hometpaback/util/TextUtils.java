package com.XHxinhe.aliveandwell.hometpaback.util;

import java.util.List;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * 提供文本格式化和操作的工具类。
 * <p>
 * 这个类封装了一些常用的文本处理功能，例如创建“键: 值”格式的文本，
 * 或者将一个文本列表用指定的分隔符连接起来，简化了在游戏中发送格式化消息的代码。
 */
public class TextUtils {

    public TextUtils() {
    }

    /**
     * 创建一个 "名称: 值" 格式的可变文本（MutableText）。
     * 名称为灰色，值为绿色。
     *
     * @param name  要显示的名称字符串。
     * @param value 要显示的值，为 Text 对象。
     * @return 格式化后的 MutableText 对象。
     */
    public static MutableText valueRepr(String name, Text value) {
        // 如果值本身没有样式，则给它一个默认的绿色样式
        return value.getStyle().isEmpty() ?
                Text.literal(name + ": ").formatted(Formatting.GRAY).append(value.copy().formatted(Formatting.GREEN)) :
                Text.literal(name + ": ").formatted(Formatting.GRAY).append(value);
    }

    /**
     * 创建一个 "名称: 值" 格式的可变文本的重载方法。
     *
     * @param name  要显示的名称字符串。
     * @param value 要显示的值，为 String 对象。
     * @return 格式化后的 MutableText 对象。
     */
    public static MutableText valueRepr(String name, String value) {
        return valueRepr(name, Text.literal(value).formatted(Formatting.GREEN));
    }

    /**
     * 创建一个 "名称: 值" 格式的可变文本的重载方法，用于 double 类型的值。
     * 值会被格式化为保留两位小数。
     *
     * @param name  要显示的名称字符串。
     * @param value 要显示的 double 值。
     * @return 格式化后的 MutableText 对象。
     */
    public static MutableText valueRepr(String name, double value) {
        return valueRepr(name, String.format("%.2f", value));
    }

    /**
     * 创建一个 "名称: 值" 格式的可变文本的重载方法，用于 float 类型的值。
     * 值会被格式化为保留两位小数。
     *
     * @param name  要显示的名称字符串。
     * @param value 要显示的 float 值。
     * @return 格式化后的 MutableText 对象。
     */
    public static MutableText valueRepr(String name, float value) {
        return valueRepr(name, String.format("%.2f", value));
    }

    /**
     * 将一个 Text 列表用指定的分隔符连接成一个单一的 MutableText。
     *
     * @param values  要连接的 Text 对象列表。
     * @param joiner  用于连接各项之间的分隔符 Text 对象。
     * @return 连接后的 MutableText 对象。
     */
    public static MutableText join(List<Text> values, Text joiner) {
        MutableText out = Text.empty();

        for (int i = 0; i < values.size(); ++i) {
            out.append(values.get(i));
            // 不在最后一个元素后添加分隔符
            if (i < values.size() - 1) {
                out.append(joiner);
            }
        }

        return out;
    }
}