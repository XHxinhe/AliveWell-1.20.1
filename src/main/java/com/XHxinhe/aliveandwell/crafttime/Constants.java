package com.XHxinhe.aliveandwell.crafttime;

import net.minecraft.util.Identifier;

/**
 * 一个用于存放整个Mod全局常量的类。
 * <p>
 * 将常量集中存放在这里可以提高代码的可读性和可维护性，
 * 避免在代码库中散布“魔法值”（Magic Values）。
 */
public final class Constants {

	/**
	 * 示例配置文件的标准名称。
	 * 当主配置文件已存在时，会生成此名称的示例文件。
	 */
	public static final String SAMPLE_CONFIG_FILENAME = "aliveandwell_craft_time_sample.json";

	/**
	 * 主配置文件的标准名称。
	 * Mod将从此文件加载合成难度设置。
	 */
	public static final String CONFIG_FILENAME = "aliveandwell_craft_time.json";

	/**
	 * 用于服务器向客户端同步“合成难度表”的网络数据包通道ID。
	 * 服务器是配置的权威来源，客户端需要这些数据以正确显示信息。
	 */
	public static final Identifier DIFFICULTY_TABLE_PACKET_ID = new Identifier("aliveandwell", "crafttime");

	/**
	 * 私有构造函数，防止该工具类被外部实例化。
	 * 这是一个纯静态的常量持有类，不应该有任何实例。
	 */
	private Constants() {
	}
}