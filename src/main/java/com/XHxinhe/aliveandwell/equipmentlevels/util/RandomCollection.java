package com.XHxinhe.aliveandwell.equipmentlevels.util;

import net.minecraft.util.math.random.Random; // 替换混淆名 class_5819

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * 加权随机选择集合 (混淆名替换版)
 * <p>
 * 这是一个泛型类，用于从一个集合中根据每个元素设定的“权重”来随机抽取一个元素。
 * 权重越高的元素，被选中的概率越大。
 *
 * @param <E> 集合中存储的元素类型。
 */
public class RandomCollection<E> {

    // 使用 NavigableMap (具体实现为 TreeMap) 来存储数据。
    // 键 (Double): 累积的权重值。
    // 值 (E): 对应的元素。
    // TreeMap 会自动根据键（累积权重）进行排序，这是实现高效查找的关键。
    private final NavigableMap<Double, E> map = new TreeMap<>();

    // 所有已添加元素权重的总和。
    private double total = 0.0;

    public RandomCollection() {
    }

    /**
     * 向集合中添加一个带权重的元素。
     *
     * @param weight 元素的权重。必须是正数，权重越高，被选中的概率越大。
     * @param result 要添加的元素。
     */
    public void add(double weight, E result) {
        // 忽略所有非正数的权重。
        if (weight > 0.0) {
            // 将当前权重累加到总权重中。
            this.total += weight;
            // 将新的总权重作为键，元素作为值，存入 map。
            // 例如：
            // 1. add(10, "A") -> total=10, map={10:"A"}
            // 2. add(30, "B") -> total=40, map={10:"A", 40:"B"}
            // 3. add(5, "C")  -> total=45, map={10:"A", 40:"B", 45:"C"}
            // 这相当于创建了三个区间：(0, 10], (10, 40], (40, 45]
            this.map.put(this.total, result);
        }
    }

    /**
     * 从集合中根据权重随机抽取一个元素。
     *
     * @param random Minecraft 的随机数生成器实例。
     * @return 随机选中的元素。
     */
    public E next(Random random) {
        // 1. 生成一个 [0.0, 1.0) 之间的随机双精度浮点数。
        // 2. 将其乘以总权重，得到一个在 [0, total) 范围内的随机值。
        double value = random.nextDouble() * this.total;

        // 3. 使用 map.ceilingEntry(value) 查找。
        //    这个方法会返回 map 中键大于或等于 `value` 的最小的一个键值对。
        //    例如，如果 total=45，随机生成的 value=25：
        //    - map 中大于等于25的最小键是40。
        //    - ceilingEntry(25) 返回 (40, "B") 这个键值对。
        //    - .getValue() 就返回了 "B"。
        //    这恰好实现了加权随机选择。
        return this.map.ceilingEntry(value).getValue();
    }
}