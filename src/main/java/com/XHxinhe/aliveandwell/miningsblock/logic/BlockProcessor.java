package com.XHxinhe.aliveandwell.miningsblock.logic;

import com.XHxinhe.aliveandwell.miningsblock.BlockGroups;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 方块处理器：用于判断方块是否为有效目标、分组、标签等逻辑
 */
public class BlockProcessor {
    // 缓存：已检查过的方块ID及其有效性
    private static final Map<String, Boolean> checkedBlocks = new HashMap<>();
    // 缓存：已检查过的方块对及其匹配性（目前未启用）
    private static final Map<String, Map<String, Boolean>> checkedPairs = new HashMap<>();
    // 缓存：已检查过的方块ID及其所有标签
    private static final Map<String, Set<String>> checkedTags = new HashMap<>();

    /**
     * 清空所有缓存（通常在配置或分组变更时调用）
     */
    public static void rebuild() {
        checkedBlocks.clear();
        checkedPairs.clear();
        checkedTags.clear();
    }

    /**
     * 判断某个方块是否为有效目标
     * @param state 目标方块状态
     * @param world 世界对象
     * @param pos 目标方块坐标
     * @param source 源方块
     * @return 是否为有效目标
     */
    public static boolean isValidTarget(BlockState state, ServerWorld world, BlockPos pos, Block source) {
        Block block = world.getBlockState(pos).getBlock(); // 获取目标方块
        // 不是空气 && 检查缓存或新检查为有效 && 源方块与目标方块匹配
        return !state.isAir()
                && checkedBlocks.computeIfAbsent(
                Registries.BLOCK.getId(block).toString(),
                (name) -> BlockProcessor.checkBlock(state)
        )
                && matches(source, block);
    }

    /**
     * 判断两个方块是否为同一个方块（可扩展为分组匹配）
     * @param origin 源方块
     * @param target 目标方块
     * @return 是否匹配
     */
    private static boolean matches(Block origin, Block target) {
        if (origin == target) {
            return true; // 完全相同
        }
        // 下面是分组匹配的缓存逻辑（如需启用可取消注释）
        /*
        String originName = Registries.BLOCK.getId(origin).toString();
        String targetName = Registries.BLOCK.getId(target).toString();
        boolean useOriginKey = originName.compareTo(targetName) >= 0;

        if (useOriginKey) {
            return checkedPairs.computeIfAbsent(originName, (name) -> new HashMap<>())
                    .computeIfAbsent(targetName, (name) -> checkMatch(origin, target));
        } else {
            return checkedPairs.computeIfAbsent(targetName, (name) -> new HashMap<>())
                    .computeIfAbsent(originName, (name) -> checkMatch(origin, target));
        }
        */
        return false; // 目前只允许完全相同
    }

    /**
     * 检查方块是否属于有效分组或标签
     * @param blockState 方块状态
     * @return 是否有效
     */
    private static boolean checkBlock(BlockState blockState) {
        Set<String> ids = new HashSet<>();
        String blockId = Registries.BLOCK.getId(blockState.getBlock()).toString();
        ids.add(blockId);

        // 获取并缓存该方块的所有标签
        Set<String> tags = checkedTags.computeIfAbsent(blockId, (name) -> getTagsFor(blockState));
        tags.forEach(tag -> ids.add("#" + tag));

        // 获取该方块所属的自定义分组
        Set<String> configs = BlockGroups.getGroup(blockId);

        // 如果是圆石，直接返回true
        if (blockState.getBlock() == Blocks.COBBLESTONE) {
            return true;
        }

        // 检查分组中是否有与ids匹配的
        for (String id : configs) {
            if (ids.contains(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取方块的所有标签
     * @param blockState 方块状态
     * @return 标签集合
     */
    private static Set<String> getTagsFor(BlockState blockState) {
        Set<String> tags = new HashSet<>();
        Registries.BLOCK.streamTags().forEach(blockTagKey -> {
            if (blockState.isIn(blockTagKey)) {
                tags.add(blockTagKey.id().toString());
            }
        });
        return tags;
    }

    /**
     * 检查两个方块是否属于同一分组
     * @param origin 源方块
     * @param target 目标方块
     * @return 是否属于同一分组
     */
    private static boolean checkMatch(Block origin, Block target) {
        Set<String> group = BlockGroups.getGroup(Registries.BLOCK.getId(origin).toString());
        if (group != null) {
            return group.contains(Registries.BLOCK.getId(target).toString());
        }
        return false;
    }
}