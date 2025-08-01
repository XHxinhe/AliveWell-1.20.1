package com.XHxinhe.aliveandwell.miningsblock;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.*;

/**
 * BlockGroups 用于管理方块分组与标签映射关系。
 * 支持通过标签（如#minecraft:logs）或直接ID（如minecraft:stone）定义分组。
 */
public class BlockGroups {
    // 记录每个方块ID属于哪些组（组是方块ID集合）
    private static final Map<String, Set<String>> blockToGroup = new HashMap<>();
    // 所有分组的原始定义（每个元素是逗号分隔的标签或ID字符串）
    public static List<String> groups = new ArrayList<>();

    /**
     * 初始化分组映射。应在数据加载后调用。
     */
    public static void init() {
        blockToGroup.clear(); // 清空旧映射
        groups.clear();       // 清空旧分组定义
        validatePostLoad();   // 加载默认分组定义

        // 遍历每个分组定义
        for (String group : groups) {
            // 拆分为标签或ID数组
            String[] ids = group.split(",");
            // 解析为实际的方块ID集合
            Set<String> blockGroup = createGroup(ids);

            // 将每个方块ID映射到其所属的分组（可能有多个分组）
            for (String blockId : blockGroup) {
                // 合并分组，保证同一个方块可以属于多个分组
                blockToGroup.merge(blockId, blockGroup, (existingSet, newSet) -> {
                    existingSet.addAll(newSet);
                    return existingSet;
                });
            }
        }
    }

    /**
     * 获取指定方块ID所属的分组（返回所有相关方块ID的集合）。
     * @param id 方块ID字符串（如"minecraft:stone"）
     * @return 该方块所属的分组集合（可能为空）
     */
    public static Set<String> getGroup(String id) {
        // 返回所属分组集合，如果没有则返回空集合
        return blockToGroup.getOrDefault(id, Collections.emptySet());
    }

    /**
     * 根据标签或ID数组，解析出实际的方块ID集合。
     * 支持标签（如#minecraft:logs）和直接ID（如minecraft:stone）。
     * @param ids 标签或ID数组
     * @return 解析出的方块ID集合
     */
    private static Set<String> createGroup(String[] ids) {
        Set<String> newGroup = new HashSet<>();

        for (String id : ids) {
            boolean isTag = id.charAt(0) == '#'; // 判断是否为标签

            if (isTag) {
                // 解析标签名（去掉#）
                Identifier identifier = Identifier.tryParse(id.substring(1));
                if (identifier != null) {
                    // 获取标签下所有方块
                    Registries.BLOCK.getEntryList(TagKey.of(RegistryKeys.BLOCK, identifier))
                            .ifPresent(registryEntries -> {
                                for (RegistryEntry<Block> registryEntry : registryEntries) {
                                    // 获取方块注册键（即方块ID），加入集合
                                    registryEntry.getKey().ifPresent(
                                            blockRegistryKey -> newGroup.add(blockRegistryKey.getValue().toString()));
                                }
                            });
                }
            } else {
                // 直接是方块ID
                Identifier identifier = Identifier.tryParse(id);
                if (identifier != null && Registries.BLOCK.containsId(identifier)) {
                    newGroup.add(id);
                }
            }
        }
        return newGroup;
    }

    /**
     * 加载默认分组定义（标签和ID）。
     * 可根据需要扩展或修改。
     */
    public static void validatePostLoad() {
        // 用Set去重
        Set<String> validatedGroups = new HashSet<>();

        // 默认分组定义（标签或ID，支持逗号分隔多个）
        List<String> defaults = Lists.newArrayList(
                // 这里是标签和ID的列表，支持自定义扩展
                "#minecraft:mineable/pickaxe",
                "#minecraft:mineable/shovel",
                "#minecraft:mineable/axe",
                "#minecraft:acacia_logs",
                "#minecraft:all_hanging_signs",
                "#minecraft:all_signs",
                "#minecraft:ancient_city_replaceable",
                "#minecraft:animals_spawnable_on",
                "#minecraft:anvil",
                "#minecraft:axolotls_spawnable_on",
                "#minecraft:azalea_grows_on",
                "#minecraft:azalea_root_replaceable",
                "#minecraft:bamboo_blocks",
                "#minecraft:bamboo_plantable_on",
                "#minecraft:banners",
                "#minecraft:base_stone_nether",
                "#minecraft:base_stone_overworld",
                "#minecraft:beacon_base_blocks",
                "#minecraft:beds",
                "#minecraft:bee_growables",
                "#minecraft:beehives",
                "#minecraft:big_dripleaf_placeable",
                "#minecraft:birch_logs",
                "#minecraft:buttons",
                "#minecraft:campfires",
                "#minecraft:candle_cakes",
                "#minecraft:candles",
                "#minecraft:cauldrons",
                "#minecraft:cave_vines",
                "#minecraft:ceiling_hanging_signs",
                "#minecraft:cherry_logs",
                "#minecraft:climbable",
                "#minecraft:coal_ores",
                "#minecraft:combination_step_sound_blocks",
                "#minecraft:completes_find_tree_tutorial",
                "#minecraft:convertable_to_mud",
                "#minecraft:copper_ores",
                "#minecraft:coral_blocks",
                "#minecraft:coral_plants",
                "#minecraft:corals",
                "#minecraft:crimson_stems",
                "#minecraft:crops",
                "#minecraft:crystal_sound_blocks",
                "#minecraft:dampens_vibrations",
                "#minecraft:dark_oak_logs",
                "#minecraft:dead_bush_may_place_on",
                "#minecraft:deepslate_ore_replaceables",
                "#minecraft:diamond_ores",
                "#minecraft:dirt",
                "#minecraft:doors",
                "#minecraft:dragon_immune",
                "#minecraft:dragon_transparent",
                "#minecraft:dripstone_replaceable_blocks",
                "#minecraft:emerald_ores",
                "#minecraft:enchantment_power_provider",
                "#minecraft:enchantment_power_transmitter",
                "#minecraft:enderman_holdable",
                "#minecraft:fall_damage_resetting",
                "#minecraft:features_cannot_replace",
                "#minecraft:fence_gates",
                "#minecraft:fences",
                "#minecraft:fire",
                "#minecraft:flower_pots",
                "#minecraft:flowers",
                "#minecraft:foxes_spawnable_on",
                "#minecraft:frog_prefer_jump_to",
                "#minecraft:frogs_spawnable_on",
                "#minecraft:geode_invalid_blocks",
                "#minecraft:goats_spawnable_on",
                "#minecraft:gold_ores",
                "#minecraft:guarded_by_piglins",
                "#minecraft:hoglin_repellents",
                "#minecraft:ice",
                "#minecraft:impermeable",
                "#minecraft:infiniburn_end",
                "#minecraft:infiniburn_nether",
                "#minecraft:infiniburn_overworld",
                "#minecraft:inside_step_sound_blocks",
                "#minecraft:invalid_spawn_inside",
                "#minecraft:iron_ores",
                "#minecraft:jungle_logs",
                "#minecraft:lapis_ores",
                "#minecraft:lava_pool_stone_cannot_replace",
                "#minecraft:leaves",
                "#minecraft:logs",
                "#minecraft:logs_that_burn",
                "#minecraft:lush_ground_replaceable",
                "#minecraft:maintains_farmland",
                "#minecraft:mangrove_logs",
                "#minecraft:mangrove_logs_can_grow_through",
                "#minecraft:mangrove_roots_can_grow_through",
                "#minecraft:mooshrooms_spawnable_on",
                "#minecraft:moss_replaceable",
                "#minecraft:mushroom_grow_block",
                "#minecraft:needs_diamond_tool",
                "#minecraft:needs_iron_tool",
                "#minecraft:needs_stone_tool",
                "#minecraft:nether_carver_replaceables",
                "#minecraft:nylium",
                "#minecraft:oak_logs",
                "#minecraft:occludes_vibration_signals",
                "#minecraft:overworld_carver_replaceables",
                "#minecraft:overworld_natural_logs",
                "#minecraft:parrots_spawnable_on",
                "#minecraft:piglin_repellents",
                "#minecraft:planks",
                "#minecraft:polar_bears_spawnable_on_alternate",
                "#minecraft:pressure_plates",
                "#minecraft:prevent_mob_spawning_inside",
                "#minecraft:rabbits_spawnable_on",
                "#minecraft:rails",
                "#minecraft:redstone_ores",
                "#minecraft:replaceable",
                "#minecraft:replaceable_by_trees",
                "#minecraft:sand",
                "#minecraft:saplings",
                "#minecraft:sculk_replaceable",
                "#minecraft:sculk_replaceable_world_gen",
                "#minecraft:shulker_boxes",
                "#minecraft:signs",
                "#minecraft:slabs",
                "#minecraft:small_dripleaf_placeable",
                "#minecraft:small_flowers",
                "#minecraft:smelts_to_glass",
                "#minecraft:snaps_goat_horn",
                "#minecraft:sniffer_diggable_block",
                "#minecraft:sniffer_egg_hatch_boost",
                "#minecraft:snow",
                "#minecraft:snow_layer_can_survive_on",
                "#minecraft:snow_layer_cannot_survive_on",
                "#minecraft:soul_fire_base_blocks",
                "#minecraft:soul_speed_blocks",
                "#minecraft:spruce_logs",
                "#minecraft:stairs",
                "#minecraft:standing_signs",
                "#minecraft:stone_bricks",
                "#minecraft:stone_buttons",
                "#minecraft:stone_ore_replaceables",
                "#minecraft:stone_pressure_plates",
                "#minecraft:strider_warm_blocks",
                "#minecraft:sword_efficient",
                "#minecraft:tall_flowers",
                "#minecraft:terracotta",
                "#minecraft:trail_ruins_replaceable",
                "#minecraft:trapdoors",
                "#minecraft:underwater_bonemeals",
                "#minecraft:unstable_bottom_center",
                "#minecraft:valid_spawn",
                "#minecraft:vibration_resonators",
                "#minecraft:wall_corals",
                "#minecraft:wall_hanging_signs",
                "#minecraft:wall_post_override",
                "#minecraft:wall_signs",
                "#minecraft:walls",
                "#minecraft:warped_stems",
                "#minecraft:wart_blocks",
                "#minecraft:wither_immune",
                "#minecraft:wither_summon_base_blocks",
                "#minecraft:wolves_spawnable_on",
                "#minecraft:wooden_buttons",
                "#minecraft:wooden_doors",
                "#minecraft:wooden_fences",
                "#minecraft:wooden_pressure_plates",
                "#minecraft:wooden_slabs",
                "#minecraft:wooden_stairs",
                "#minecraft:wooden_trapdoors",
                "#minecraft:wool",
                "#minecraft:wool_carpets",
                "#c:adamantite_ores",
                "#c:aetherium_ores",
                "#c:aluminum_ores",
                "#c:amethyst_ores",
                "#c:antimony_ores",
                "#c:aquarium_ores",
                "#c:asterite_ores",
                "#c:banglum_ores",
                "#c:bauxite_ores",
                "#c:carmot_ores",
                "#c:certus_quartz_ores",
                "#c:cinnabar_ores",
                "#c:coal_ores",
                "#c:cobalt_ores",
                "#c:copper_ores",
                "#c:diamond_ores",
                "#c:emerald_ores",
                "#c:galaxium_ores",
                "#c:galena_ores",
                "#c:gold_ores",
                "#c:iridium_ores",
                "#c:iron_ores",
                "#c:kyber_ores",
                "#c:lapis_ores",
                "#c:lead_ores",
                "#c:lunum_ores",
                "#c:lutetium_ores",
                "#c:manganese_ores",
                "#c:metite_ores",
                "#c:mythril_ores",
                "#c:nickel_ores",
                "#c:orichalcum_ores",
                "#c:osmium_ores",
                "#c:palladium_ores",
                "#c:peridot_ores",
                "#c:platinum_ores",
                "#c:prometheum_ores",
                "#c:pyrite_ores",
                "#c:quadrillum_ores",
                "#c:quartz_ores",
                "#c:redstone_ores",
                "#c:ruby_ores",
                "#c:runite_ores",
                "#c:salt_ores",
                "#c:sapphire_ores",
                "#c:sheldonite_ores",
                "#c:silver_ores",
                "#c:sodalite_ores",
                "#c:sphalerite_ores",
                "#c:starrite_ores",
                "#c:stellum_ores",
                "#c:stormyx_ores",
                "#c:sulfur_ores",
                "#c:tantalite_ores",
                "#c:tin_ores",
                "#c:titanium_ores",
                "#c:topaz_ores",
                "#c:truesilver_ores",
                "#c:tungsten_ores",
                "#c:unobtainium_ores",
                "#c:ur_ores",
                "#c:uranium_ores",
                "#c:vermiculite_ores",
                "#c:zinc_ores"
        );
        validatedGroups.addAll(defaults); // 去重
        groups = Lists.newArrayList(validatedGroups); // 赋值给groups
    }
}