package com.XHxinhe.aliveandwell.flintcoppertool.utils;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
/**
 * 定义模组自定义的物品标签（Item Tags）。
 * <p>
 * 物品标签是Minecraft中对物品进行分类和分组的强大方式。
 * 这个类定义了“木制工具”和“石制工具”的标签，用于在其他地方（如事件处理）
 * 方便地识别这些类型的工具，而无需逐个检查物品ID。
 */
public final class ModItemTags {
	/**
	 * 包含所有原版木制工具的标签。
	 * 通过数据包（data pack）中的 a/tags/items/wooden_tools.json 文件定义具体内容。
	 */
	public static final TagKey<Item> WOODEN_TOOLS = register("wooden_tools");

	/**
	 * 包含所有原版石制工具的标签。
	 * 通过数据包（data pack）中的 a/tags/items/stone_tools.json 文件定义具体内容。
	 */
	public static final TagKey<Item> STONE_TOOLS = register("stone_tools");

	private ModItemTags() {
		// 私有构造函数，防止实例化
	}

	/**
	 * 一个辅助方法，用于创建和注册物品标签。
	 *
	 * @param id 标签的路径名称（例如 "wooden_tools"）
	 * @return 一个注册好的 TagKey<Item> 实例
	 */
	private static TagKey<Item> register(String id) {
		// 使用模组的命名空间 "aliveandwell" 创建一个完整的标识符
		return TagKey.of(RegistryKeys.ITEM, new Identifier("aliveandwell", id));
	}
}