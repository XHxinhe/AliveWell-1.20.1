package com.XHxinhe.aliveandwell.hometpaback.util;

import com.XHxinhe.aliveandwell.hometpaback.HomeComponent;
import java.util.List;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

/**
 * 定义了家（Home）和返回点（Back）数据存储行为的接口。
 * <p>
 * 这个接口为玩家的家和返回点数据提供了一个统一的访问和操作规范。
 * 任何实现了此接口的类（例如，一个玩家实体的数据组件）都必须提供这些核心功能。
 * 这种设计遵循了“面向接口编程”的原则，使得数据存储的实现可以被轻松替换或扩展。
 */
public interface IStoreHome {
    /**
     * 获取该玩家设置的所有家的列表。
     * @return 一个包含 HomeComponent 对象的列表，每个对象代表一个家。
     */
    List<HomeComponent> getHomes();

    /**
     * 获取该玩家可以设置的家的最大数量。
     * @return 一个整数，代表家的数量上限。
     */
    int getMaxHomes();

    /**
     * 添加一个新的家。
     * @param home 要添加的 HomeComponent 对象，包含了家的名称、坐标和维度。
     * @return 如果添加成功，返回 true；如果家的数量已达上限或名称重复，返回 false。
     */
    boolean addHome(HomeComponent home);

    /**
     * 根据名称移除一个已设置的家。
     * @param name 要移除的家的名称。
     * @return 如果找到了对应名称的家并成功移除，返回 true；否则返回 false。
     */
    boolean removeHome(String name);

    /**
     * 记录玩家传送前的“返回点”位置。
     * @param pos 传送前的坐标 (Vec3d)。
     * @param dimension 传送前的维度 (RegistryKey<World>)。
     */
    void addBack(Vec3d pos, RegistryKey<World> dimension);

    /**
     * 获取记录的“返回点”位置。
     * @return 一个 Pair 对象，包含坐标 (Vec3d) 和维度 (RegistryKey<World>)。如果没有记录，可能返回 null。
     */
    Pair<Vec3d, RegistryKey<World>> getBack();
}