//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
// Deobfuscated, analyzed, and corrected by AI聊天机器人
// Current time: Mon Jul 14 10:02:24 CST 2025
//

package com.XHxinhe.aliveandwell.block.randompos;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 负责管理和持久化随机传送门目标位置的服务器端状态管理器。
 * 这个类继承了 PersistentState，使其数据能够随世界存档一起保存和加载。
 * 注意：此版本已修正原始代码中存在的NBT读写不一致的严重BUG。
 */
public class RandomManager extends PersistentState {

    // 使用 ConcurrentHashMap 存储传送门唯一标识 (RandomPos) 与其目标位置 (BlockPos) 的映射。
    public final ConcurrentHashMap<RandomPos, BlockPos> randomPosMap = new ConcurrentHashMap<>();

    // 这两个静态变量用于控制某些游戏内事件的生成，它们的值也会被持久化。
    public static boolean canSpawnVillager = false;
    public static boolean canSpawnStructure = false;

    // 用于在文件中标识此数据的名称，忠于原作
    private static final String DATA_NAME = "aliveandwell";
    private static final String NBT_KEY_POSITIONS = "aliveandewell_randompos";
    private static final String NBT_KEY_SPAWN_VILLAGER = "aliveandewell_canSpawnVillager";
    private static final String NBT_KEY_SPAWN_STRUCTURE = "aliveandewell_canSpawnStructure";


    /**
     * 默认构造函数，用于首次创建该状态。
     */
    public RandomManager() {
    }

    /**
     * 从 NBT 数据加载状态的构造函数。
     * @param nbt 从 world/data/aliveandwell.dat 文件中读取的 NBT 数据。
     */
    public RandomManager(NbtCompound nbt) {
        // 从 NBT 加载传送门位置映射
        NbtList posList = nbt.getList(NBT_KEY_POSITIONS, NbtElement.COMPOUND_TYPE);
        for (NbtElement element : posList) {
            NbtCompound randomNbt = (NbtCompound) element;
            RandomPos randomPos = new RandomPos(
                    randomNbt.getString("world"),
                    new ChunkPos(randomNbt.getInt("x"), randomNbt.getInt("z")),
                    randomNbt.getString("topA"),
                    randomNbt.getString("topB"),
                    randomNbt.getString("bomA"),
                    randomNbt.getString("bomB")
            );
            BlockPos pos = new BlockPos(
                    randomNbt.getInt("posX"),
                    randomNbt.getInt("posY"),
                    randomNbt.getInt("posZ")
            );
            this.randomPosMap.put(randomPos, pos);
        }

        // **修正后的加载逻辑**：直接从主NBT读取布尔值。
        // 这样可以正确加载，并且能兼容使用修正后代码保存的存档。
        if (nbt.contains(NBT_KEY_SPAWN_VILLAGER, NbtElement.BYTE_TYPE)) {
            canSpawnVillager = nbt.getBoolean(NBT_KEY_SPAWN_VILLAGER);
        }
        if (nbt.contains(NBT_KEY_SPAWN_STRUCTURE, NbtElement.BYTE_TYPE)) {
            canSpawnStructure = nbt.getBoolean(NBT_KEY_SPAWN_STRUCTURE);
        }
    }

    /**
     * 获取服务器范围内的 RandomManager 实例。
     * @param server Minecraft 服务器实例。
     * @return RandomManager 的单例。
     */
    public static RandomManager getServerState(MinecraftServer server) {
        ServerWorld overworld = server.getWorld(World.OVERWORLD);
        // 必须在服务器端调用，且主世界已加载
        Objects.requireNonNull(overworld, "Cannot get server state when overworld is not loaded!");

        PersistentStateManager persistentStateManager = overworld.getPersistentStateManager();

        // 获取或创建名为 "aliveandwell" 的持久化状态
        RandomManager state = persistentStateManager.getOrCreate(
                RandomManager::new, // 用于从 NBT 加载
                RandomManager::new, // 用于首次创建
                DATA_NAME
        );

        // state.markDirty(); // 调用 markDirty() 是个好习惯，但因为 isDirty() 恒为 true，所以这里不是必须的。
        return state;
    }

    /**
     * 根据传送门的唯一标识获取其目标位置。
     * @param randomPos 传送门的唯一标识。
     * @param server Minecraft 服务器实例。
     * @return 如果存在，则返回目标 BlockPos；否则返回 null。
     */
    public BlockPos getDestination(RandomPos randomPos, MinecraftServer server) {
        RandomManager serverState = getServerState(server);
        return serverState.randomPosMap.get(randomPos);
    }

    /**
     * 将当前状态写入 NBT 数据，以便保存。
     * @param nbt 要写入的目标 NbtCompound。
     * @return 写入数据后的 NbtCompound。
     */
    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        // 保存传送门位置映射
        NbtList posList = new NbtList();
        randomPosMap.forEach((randomPos, pos) -> {
            if (randomPos != null && pos != null) {
                NbtCompound randomNbt = new NbtCompound();
                randomNbt.putString("world", randomPos.getWorld());
                randomNbt.putInt("x", randomPos.getChunkPos().x);
                randomNbt.putInt("z", randomPos.getChunkPos().z);
                randomNbt.putString("topA", randomPos.getTopA());
                randomNbt.putString("topB", randomPos.getTopB());
                randomNbt.putString("bomA", randomPos.getBomA());
                randomNbt.putString("bomB", randomPos.getBomB());
                randomNbt.putInt("posX", pos.getX());
                randomNbt.putInt("posY", pos.getY());
                randomNbt.putInt("posZ", pos.getZ());
                posList.add(randomNbt);
            }
        });
        nbt.put(NBT_KEY_POSITIONS, posList);

        // **修正后的保存逻辑**：直接将布尔值写入主NBT，而不是嵌套在另一个Compound或List中。
        nbt.putBoolean(NBT_KEY_SPAWN_VILLAGER, canSpawnVillager);
        nbt.putBoolean(NBT_KEY_SPAWN_STRUCTURE, canSpawnStructure);

        return nbt;
    }

    /**
     * 强制状态始终被保存。
     * @return 总是 true。
     */
    @Override
    public boolean isDirty() {
        return true;
    }
}