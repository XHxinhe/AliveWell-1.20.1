package com.XHxinhe.aliveandwell.block.randompos;

import net.minecraft.util.math.ChunkPos;
import java.util.Objects;

/**
 * 一个数据类，用于唯一标识一个随机传送门实例。
 * <p>
 * 它通过组合以下信息来创建一个唯一的“指纹”：
 * <ul>
 *     <li>实体所在的世界 ({@code world})</li>
 *     <li>传送门所在的区块坐标 ({@code chunkPos})</li>
 *     <li>构成传送门框架的四种方块类型 ({@code topA, topB, bomA, bomB})</li>
 * </ul>
 * 这个类的实例被用作 {@link RandomManager} 中 ConcurrentHashMap 的键，
 * 以便将每个独一无二的传送门映射到一个固定的传送目标点。
 * 正确实现的 {@code equals()} 和 {@code hashCode()} 方法是其作为键的关键。
 */
public final class RandomPos { // Made final as it's an immutable data class

    private final String world;
    private final ChunkPos chunkPos;
    private final String topA;
    private final String topB;
    private final String bomA;
    private final String bomB;

    public RandomPos(String world, ChunkPos chunkPos, String topA, String topB, String bomA, String bomB) {
        this.world = world;
        this.chunkPos = chunkPos;
        this.topA = topA;
        this.topB = topB;
        this.bomA = bomA;
        this.bomB = bomB;
    }

    public String getWorld() {
        return this.world;
    }

    public ChunkPos getChunkPos() {
        return this.chunkPos;
    }

    public String getTopA() {
        return this.topA;
    }

    public String getTopB() {
        return this.topB;
    }

    public String getBomA() {
        return this.bomA;
    }

    public String getBomB() {
        return this.bomB;
    }

    /**
     * 比较两个 RandomPos 对象是否相等。
     * 当且仅当所有字段都相同时，两个对象才被视为相等。
     * @param o 要比较的对象。
     * @return 如果相等则返回 true，否则返回 false。
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RandomPos randomPos = (RandomPos) o;
        return world.equals(randomPos.world) &&
                chunkPos.equals(randomPos.chunkPos) &&
                topA.equals(randomPos.topA) &&
                topB.equals(randomPos.topB) &&
                bomA.equals(randomPos.bomA) &&
                bomB.equals(randomPos.bomB);
    }

    /**
     * 根据所有字段计算哈希码。
     * 这是为了确保相等的对象具有相同的哈希码，满足作为 HashMap 键的条件。
     * @return 该对象的哈希码。
     */
    @Override
    public int hashCode() {
        return Objects.hash(world, chunkPos, topA, topB, bomA, bomB);
    }
}