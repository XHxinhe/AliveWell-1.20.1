package com.XHxinhe.aliveandwell.crafttime.config;

import com.XHxinhe.aliveandwell.crafttime.Constants;
import com.XHxinhe.aliveandwell.registry.ItemInit;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置文件加载器，用于管理物品的“合成难度”。
 * <p>
 * 这个类负责生成、解析和查询一个JSON配置文件，该文件定义了游戏中每个物品的难度值。
 * 这个难度值可能用于调整合成所需的时间或其他游戏机制。
 */
public class ConfigLoader {
	// 使用 Map<Item, Float> 更为健壮和清晰，但为了保持与原逻辑一致，仍使用整数ID
	public final Map<Integer, Float> difficultyMap = new HashMap<>();
	private static final Path configPath = FabricLoader.getInstance().getConfigDir();

	// JSON 配置文件中的键名常量
	public static final String GLOBAL = "global_multiplier";
	public static final String MODS = "mods";
	public static final String MOD_MULTIPLIER = "mod_multiplier";
	public static final String ITEMS = "items";

	public ConfigLoader() {
	}

	/**
	 * 生成一个示例配置文件。
	 * 这个文件会包含游戏中所有已注册的物品，并为它们设置一个默认值。
	 * 这极大地帮助了用户理解如何配置这个Mod。
	 */
	public static void genSampleConfig() {
		Map<String, JsonObject> nameSpaceMap = new HashMap<>();

		// 遍历所有已注册的物品
		Registries.ITEM.getIds().forEach(itemId -> {
			String namespace = itemId.getNamespace();
			String path = itemId.getPath();

			// 按命名空间（即Mod ID）对物品进行分组
			nameSpaceMap.computeIfAbsent(namespace, k -> new JsonObject());
			nameSpaceMap.get(namespace).addProperty(path, 20.0F); // 默认难度值
		});

		// 构建顶层JSON对象
		JsonObject root = new JsonObject();
		root.addProperty(GLOBAL, 1.0F); // 全局乘数

		JsonObject modsList = new JsonObject();
		root.add(MODS, modsList);

		// 将每个Mod的物品列表添加到JSON中
		nameSpaceMap.forEach((namespace, itemArray) -> {
			JsonObject modObject = new JsonObject();
			modObject.addProperty(MOD_MULTIPLIER, 1.0F); // 每个Mod的独立乘数
			modObject.add(ITEMS, itemArray);
			modsList.add(namespace, modObject);
		});

		try {
			// 如果主配置文件已存在，则生成一个带 ".sample" 后缀的示例文件，避免覆盖用户配置
			File configFile = configPath.resolve(Constants.CONFIG_FILENAME).toFile();
			File fileToWrite = configFile.exists()
					? configPath.resolve(Constants.SAMPLE_CONFIG_FILENAME).toFile()
					: configFile;

			try (FileWriter writer = new FileWriter(fileToWrite)) {
				writer.write(root.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从一个JSON字符串中解析配置数据，并填充 difficultyMap。
	 * @param jsonString 包含配置的JSON字符串。
	 */
	public void parserFrom(String jsonString) {
		JsonParser parser = new JsonParser();
		JsonObject root = parser.parse(jsonString).getAsJsonObject();

		float globalMultiplier = root.getAsJsonPrimitive(GLOBAL).getAsFloat();
		JsonObject modsList = root.getAsJsonObject(MODS);

		// 遍历 "mods" 对象中的每一个条目（每个条目代表一个Mod）
		modsList.entrySet().forEach(modEntry -> {
			String namespace = modEntry.getKey();
			JsonObject modObject = modEntry.getValue().getAsJsonObject();

			// 计算最终的乘数：全局乘数 * Mod独立乘数
			float finalMultiplier = modObject.getAsJsonPrimitive(MOD_MULTIPLIER).getAsFloat() * globalMultiplier;
			JsonObject items = modObject.getAsJsonObject(ITEMS);

			// 遍历该Mod下的所有物品
			items.entrySet().forEach(itemEntry -> {
				// 构建完整的物品ID，例如 "minecraft:stone"
				Identifier itemId = new Identifier(namespace, itemEntry.getKey());
				Item item = Registries.ITEM.get(itemId);

				// 获取物品的原始整数ID
				int rawId = Item.getRawId(item);
				if (rawId != 0) { // 确保物品存在
					float value = itemEntry.getValue().getAsFloat() * finalMultiplier;
					this.setDifficulty(rawId, value);
				}
			});
		});
	}

	/**
	 * 获取指定物品的难度值。
	 * @param item 要查询的物品。
	 * @return 难度值。
	 */
	public float getDifficulty(Item item) {
		// 首先处理硬编码的特殊物品值
		if (item == Items.NETHERITE_INGOT) return 360.0F;
		if (item == Items.DIAMOND) return 40.0F;
		if (item == Items.GOLD_INGOT) return 30.0F;
		if (item == Items.IRON_INGOT) return 60.0F;
		if (item == ItemInit.INGOT_WUJIN) return 60.0F;
		if (item == Items.ANCIENT_DEBRIS) return 100.0F;
		if (item == ItemInit.INGOT_MITHRIL) return 120.0F;
		if (item == ItemInit.INGOT_ADAMANTIUM) return 150.0F;

		// 如果没有硬编码值，则从配置文件加载的Map中查找
		int rawId = Item.getRawId(item);
		return this.difficultyMap.getOrDefault(rawId, 20.0F); // 如果Map中也没有，返回默认值20.0F
	}

	/**
	 * 设置一个物品的难度值。
	 * @param id 物品的原始整数ID。
	 * @param difficulty 难度值。
	 */
	public void setDifficulty(int id, float difficulty) {
		this.difficultyMap.put(id, difficulty);
	}
}