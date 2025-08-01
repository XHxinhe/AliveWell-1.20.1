package com.XHxinhe.aliveandwell.flintcoppertool.item;

import net.minecraft.item.Item;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ToolMaterial;

/**
 * 一个基础的锄头物品类，用于创建自定义材质的锄头。
 * <p>
 * 它继承自原版的 HoeItem，简化了创建新锄头物品的过程。
 * 构造函数接收一个工具材质（ToolMaterial）和物品设置（Item.Settings），
 * 允许快速地为燧石、铜等自定义材质创建对应的锄头。
 */
public class HoeBase extends HoeItem {

    /**
     * 构造一个锄头物品。
     *
     * @param material 锄头所使用的工具材质，例如 FlintToolBase 或 CopperToolBase。
     * @param settings 物品的基本属性，如所在的物品组。
     */
    public HoeBase(ToolMaterial material, Item.Settings settings) {
        // 调用父类构造函数来创建锄头。
        // 参数-1和-2.0F分别代表攻击伤害和攻击速度的修正值。
        // 在1.19+版本，锄头的攻击伤害和速度通常由材质的挖掘等级决定，
        // 此处使用硬编码值可能是为了实现特定的攻击属性。
        super(material, -1, -2.0F, settings);
    }
}