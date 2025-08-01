package com.XHxinhe.aliveandwell.miningsblock;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 挖矿玩家管理工具类
 * 负责管理玩家的挖矿状态、连锁挖矿状态以及特殊掉落位置映射。
 */
public class MiningPlayers {
    // 玩家死亡后等待时间（tick），2400 tick = 2 分钟
    public static int timeDead = 2400;

    // 激活状态校验的时间差（tick），20 tick = 1 秒
    private static final long DIFF = 20;

    // 工具耐久消耗倍率
    public static final float toolDamageMultiplier = 1.1f;

    // 玩家疲劳消耗倍率
    public static final float playerExhaustionMultiplier = 1.1f;

    // 已激活的矿工（UUID -> 最后激活时间）
    private static final Map<UUID, Long> ACTIVATED_MINERS = new ConcurrentHashMap<>();

    // 当前正在连锁挖矿的矿工（UUID集合）
    private static final Set<UUID> CURRENT_MINERS = new ConcurrentSkipListSet<>();

    // 每个世界的特殊挖矿方块映射（World -> (原始方块位置 -> 掉落物生成位置)）
    private static final Map<World, Map<BlockPos, BlockPos>> MINING_BLOCKS = new ConcurrentHashMap<>();

    /**
     * 校验激活矿工列表，移除超时未操作的玩家
     * @param worldTime 当前世界时间（tick）
     */
    public static void validate(long worldTime) {
        Iterator<Map.Entry<UUID, Long>> entries = ACTIVATED_MINERS.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<UUID, Long> entry = entries.next(); // 获取下一个玩家
            long lastTime = entry.getValue(); // 获取该玩家最后激活时间

            // 如果超时或lastTime异常，则移除该玩家
            if (worldTime - lastTime > DIFF || lastTime > worldTime) {
                entries.remove();
            }
        }
    }

    /**
     * 判断玩家是否已激活挖矿功能
     * @param player 玩家实体
     * @return 是否已激活
     */
    public static boolean canStartMining(PlayerEntity player) {
        return ACTIVATED_MINERS.containsKey(player.getUuid());
    }

    /**
     * 激活玩家的挖矿功能，并记录激活时间
     * @param player 玩家实体
     * @param time   当前世界时间
     */
    public static void activateMining(PlayerEntity player, long time) {
        ACTIVATED_MINERS.put(player.getUuid(), time);
    }

    /**
     * 取消玩家的挖矿激活状态
     * @param player 玩家实体
     */
    public static void deactivateMining(PlayerEntity player) {
        ACTIVATED_MINERS.remove(player.getUuid());
    }

    /**
     * 判断玩家是否正在连锁挖矿
     * @param player 玩家实体
     * @return 是否正在连锁挖矿
     */
    public static boolean isVeinMining(PlayerEntity player) {
        return CURRENT_MINERS.contains(player.getUuid());
    }

    /**
     * 标记玩家开始连锁挖矿
     * @param player 玩家实体
     */
    public static void startMining(PlayerEntity player) {
        CURRENT_MINERS.add(player.getUuid());
    }

    /**
     * 标记玩家结束连锁挖矿
     * @param player 玩家实体
     */
    public static void stopMining(PlayerEntity player) {
        CURRENT_MINERS.remove(player.getUuid());
    }

    /**
     * 添加特殊挖矿方块映射（用于掉落物生成位置修正）
     * @param level    世界对象
     * @param pos      原始方块位置
     * @param spawnPos 掉落物生成位置
     */
    public static void addMiningBlock(World level, BlockPos pos, BlockPos spawnPos) {
        // 若该世界无映射则新建，随后添加映射关系
        MINING_BLOCKS.computeIfAbsent(level, (k) -> new ConcurrentHashMap<>()).put(pos, spawnPos);
    }

    /**
     * 移除特殊挖矿方块映射
     * @param level 世界对象
     * @param pos   原始方块位置
     */
    public static void removeMiningBlock(World level, BlockPos pos) {
        Map<BlockPos, BlockPos> map = MINING_BLOCKS.get(level);
        if (map != null) {
            map.remove(pos);
        }
    }

    /**
     * 获取指定方块的掉落物生成位置（如无则返回空）
     * @param level 世界对象
     * @param pos   原始方块位置
     * @return 掉落物生成位置（Optional）
     */
    public static Optional<BlockPos> getNewSpawnPosForDrop(World level, BlockPos pos) {
        Map<BlockPos, BlockPos> map = MINING_BLOCKS.get(level);
        if (map != null) {
            return Optional.ofNullable(map.get(pos));
        }
        return Optional.empty();
    }
}