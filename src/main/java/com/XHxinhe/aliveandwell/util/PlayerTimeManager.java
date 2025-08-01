package com.XHxinhe.aliveandwell.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// 假设这是在 Fabric/Forge Mod 环境中，可以获取游戏目录
// import net.fabricmc.loader.api.FabricLoader;

public class PlayerTimeManager {

    // 建议：将文件路径指定到 config 文件夹下，更规范
    // private static final Path PLAYER_TIMES_FILE = FabricLoader.getInstance().getConfigDir().resolve("player_times.txt");
    private static final File PLAYER_TIMES_FILE = new File("config/player_times.txt");

    // 使用 ConcurrentHashMap 可以在很多场景下避免使用 synchronized，性能更好
    private static final Map<UUID, Long> playerTimes = new ConcurrentHashMap<>();

    // 脏标记，只有数据变动时才为 true
    private static volatile boolean isDirty = false;

    // 静态初始化块，在类加载时执行
    static {
        // 确保父目录存在
        File parentDir = PLAYER_TIMES_FILE.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        loadPlayerTimes();

        // TODO: 在服务器关闭时调用 saveToDisk()
        // 例如，注册一个服务器关闭事件监听器来调用 saveToDisk(true)
    }

    /**
     * 保存或更新玩家的时间数据。
     * 数据仅在内存中更新，并标记为需要保存。
     */
    public static void savePlayerTime(UUID playerId, long time) {
        playerTimes.put(playerId, time);
        isDirty = true;
    }

    /**
     * 获取玩家的时间数据。
     */
    public static Long getPlayerTime(UUID playerId) {
        return playerTimes.get(playerId);
    }

    /**
     * 移除玩家的时间数据。
     */
    public static void removePlayerTime(UUID playerId) {
        if (playerTimes.remove(playerId) != null) {
            isDirty = true;
        }
    }

    /**
     * 从磁盘加载所有玩家的时间数据到内存。
     * 这个操作应该是线程安全的，因为它在静态块中被调用或手动调用。
     */
    public static synchronized void loadPlayerTimes() {
        if (!PLAYER_TIMES_FILE.exists()) {
            return; // 文件不存在，无需加载
        }

        playerTimes.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(PLAYER_TIMES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    try {
                        UUID playerId = UUID.fromString(parts[0]);
                        long time = Long.parseLong(parts[1]);
                        playerTimes.put(playerId, time);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Skipping malformed line in player_times.txt: " + line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        isDirty = false; // 加载后，数据是干净的
    }

    /**
     * 将内存中的数据写入磁盘。
     * @param forceSave 如果为 true，则无论数据是否“脏”都强制保存。
     */
    public static synchronized void saveToDisk(boolean forceSave) {
        if (!isDirty && !forceSave) {
            return; // 如果数据没有变化且不强制保存，则不执行任何操作
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PLAYER_TIMES_FILE))) {
            for (Map.Entry<UUID, Long> entry : playerTimes.entrySet()) {
                writer.write(entry.getKey().toString() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        isDirty = false; // 保存后，数据变干净
    }

    /**
     * 重置所有玩家的时间数据。
     */
    public static synchronized void resetAllPlayerTimes() {
        playerTimes.clear();
        isDirty = true;
        saveToDisk(true); // 清空后立即强制保存
    }
}