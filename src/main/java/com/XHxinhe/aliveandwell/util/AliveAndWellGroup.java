package com.XHxinhe.aliveandwell.util;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.registry.BlockInit;
import com.XHxinhe.aliveandwell.registry.ItemInit;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AliveAndWellGroup {
    public AliveAndWellGroup() {
    }
    public static void addGroup() {
        Registry.register(Registries.ITEM_GROUP, id("aliveandwell.group"), FabricItemGroup.builder()
                .icon(() -> new ItemStack(ItemInit.REBORN_STONE))
                .displayName(Text.translatable("aliveandwell.group")).entries((context, entries) -> {
                    entries.add(BlockInit.ORE_MITHRIL);
                    entries.add(BlockInit.ORE_ADAMANTIUM);
                    entries.add(BlockInit.ORE_ADAMANTIUM_NETHER);
                    entries.add(BlockInit.ORE_MITHRIL_DEEPSLATE);
                    entries.add(BlockInit.ORE_EX);
                    entries.add(BlockInit.ORE_EX_DEEPSLATE);
                    entries.add(BlockInit.RANDOM_A_PORTAL);
                    entries.add(BlockInit.RANDOM_B_PORTAL);
                    entries.add(BlockInit.SPAWNPOINT_PORTAL);
                    entries.add(BlockInit.UNDERWORLD_PORTAL);
                    entries.add(BlockInit.NETHERWORLD_PORTAL);
                    entries.add(BlockInit.ADAMANTIUM_BLOCK);
                    entries.add(BlockInit.MITHRIL_BLOCK);
                    entries.add(BlockInit.FRAME_MITHRIL);
                    entries.add(BlockInit.FRAME_ADAMANTIUM);
                    entries.add(BlockInit.FRAME_SPAWNPOINT);
                    entries.add(BlockInit.random_mithril_jia);
                    entries.add(BlockInit.random_mithril_yi);
                    entries.add(BlockInit.random_mithril_bing);
                    entries.add(BlockInit.random_mithril_ding);
                    entries.add(BlockInit.random_mithril_wu);
                    entries.add(BlockInit.random_mithril_ji);
                    entries.add(BlockInit.random_mithril_geng);
                    entries.add(BlockInit.random_mithril_xin);
                    entries.add(BlockInit.random_mithril_ren);
                    entries.add(BlockInit.random_mithril_gui);
                    entries.add(BlockInit.random_adamantium_jia);
                    entries.add(BlockInit.random_adamantium_yi);
                    entries.add(BlockInit.random_adamantium_bing);
                    entries.add(BlockInit.random_adamantium_ding);
                    entries.add(BlockInit.random_adamantium_wu);
                    entries.add(BlockInit.random_adamantium_ji);
                    entries.add(BlockInit.random_adamantium_geng);
                    entries.add(BlockInit.random_adamantium_xin);
                    entries.add(BlockInit.random_adamantium_ren);
                    entries.add(BlockInit.random_adamantium_gui);
                    entries.add(BlockInit.FLINT_CRAFTING_TABLE);
                    entries.add(BlockInit.COPPER_CRAFTING_TABLE);
                    entries.add(BlockInit.IRON_CRAFTING_TABLE);
                    entries.add(BlockInit.DIAMOND_CRAFTING_TABLE);
                    entries.add(BlockInit.NETHERITE_CRAFTING_TABLE);
                    entries.add(BlockInit.CLAY_FURNACE);
                    entries.add(BlockInit.OBSIDIAN_FURNACE);
                    entries.add(BlockInit.NETHERRACK_FURNACE);
                    entries.add(BlockInit.WUJIN_ORE);
                    entries.add(BlockInit.WUJIN_ORE_DEEPSLATE);
                    entries.add(BlockInit.NITER_ORE);
                    entries.add(BlockInit.NITER_ORE_DEEPSLATE);
                    entries.add(ItemInit.XP_ITEM);
                    entries.add(ItemInit.XP_ITEN1);
                    entries.add(ItemInit.WATER_BOWL);
                    entries.add(ItemInit.ITEM_EN_GENSTONE);
                    entries.add(ItemInit.INGOT_WUJIN);
                    entries.add(ItemInit.INGOT_MITHRIL);
                    entries.add(ItemInit.ROW_MITHRIL);
                    entries.add(ItemInit.ROW_ADAMANTIUM);
                    entries.add(ItemInit.INGOT_ADAMANTIUM);
                    entries.add(ItemInit.ELYTRA_CORE);
                    entries.add(ItemInit.MITHRIL_CORE);
                    entries.add(ItemInit.ADAMANTIUM_CORE);
                    entries.add(ItemInit.ARGENT_CORE);
                    entries.add(ItemInit.SKELETON_CORE);
                    entries.add(ItemInit.FS);
                    entries.add(ItemInit.nugget_mithril);
                    entries.add(ItemInit.nugget_adamantium);
                    entries.add(ItemInit.EX_COPPER);
                    entries.add(ItemInit.EX_GOLD);
                    entries.add(ItemInit.EX_DIAMOND);
                    entries.add(ItemInit.EX_MITHRIL);
                    entries.add(ItemInit.EX_ADAMAN);
                    entries.add(ItemInit.REBORN_STONE);
                    entries.add(ItemInit.JUHUAGAO);
                    entries.add(ItemInit.ENCHANTED_GOLDEN_CARROT);
                    entries.add(ItemInit.MIANTUAN);
                    entries.add(ItemInit.QUQI_MIANTUAN);
                    entries.add(ItemInit.SALA);
                    entries.add(ItemInit.EX_PICKAXE);
                    entries.add(ItemInit.WUJIN_SWORD);
                    entries.add(ItemInit.MITHRIL_SWORD);
                    entries.add(ItemInit.ADAMANTIUM_SWORD);
                    entries.add(ItemInit.WUJIN_HELMET);
                    entries.add(ItemInit.WUJIN_CHESTPLATE);
                    entries.add(ItemInit.WUJIN_LEGGINGS);
                    entries.add(ItemInit.WUJIN_BOOTS);
                    entries.add(ItemInit.WUJIN_PICKAXE);
                    entries.add(ItemInit.WUJIN_AXE);
                    entries.add(ItemInit.WUJIN_SHOVEL);
                    entries.add(ItemInit.WUJIN_HOE);
                    entries.add(ItemInit.WUJIN_COAL);
                    entries.add(ItemInit.RAW_WUJIN);
                    entries.add(ItemInit.MITHRIL_HELMET);
                    entries.add(ItemInit.MITHRIL_CHESTPLATE);
                    entries.add(ItemInit.MITHRIL_LEGGINGS);
                    entries.add(ItemInit.MITHRIL_BOOTS);
                    entries.add(ItemInit.ADAMANTIUM_HELMET);
                    entries.add(ItemInit.ADAMANTIUM_CHESTPLATE);
                    entries.add(ItemInit.ADAMANTIUM_LEGGINGS);
                    entries.add(ItemInit.ADAMANTIUM_BOOTS);
                    entries.add(ItemInit.copper_helmet);
                    entries.add(ItemInit.copper_chestplate);
                    entries.add(ItemInit.copper_leggings);
                    entries.add(ItemInit.copper_boots);
                    entries.add(ItemInit.DEEP_RAW_COPPER);
                    entries.add(ItemInit.DEEP_RAW_IRON);
                    entries.add(ItemInit.DEEP_RAW_GOLD);
                    entries.add(ItemInit.copper_shears);
                    entries.add(ItemInit.FLINT_SHARD);
                    entries.add(ItemInit.FLINT_PICKAXE);
                    entries.add(ItemInit.FLINT_AXE);
                    entries.add(ItemInit.FLINT_SWORD);
                    entries.add(ItemInit.FLINT_INGOT);
                    entries.add(ItemInit.STRINGS);
                    entries.add(ItemInit.COPPER_NUGGET);
                    entries.add(ItemInit.COPPER_SWORD);
                    entries.add(ItemInit.COPPER_SHOVEL);
                    entries.add(ItemInit.COPPER_PICKAXE);
                    entries.add(ItemInit.COPPER_AXE);
                    entries.add(ItemInit.COPPER_HOE);
                    entries.add(ItemInit.nugget_diamond);
                    entries.add(ItemInit.nugget_emerald);
                    entries.add(ItemInit.nugget_wujin);
                    entries.add(ItemInit.lich_spawn);
                    entries.add(ItemInit.void_blossom_spawn);
                    entries.add(ItemInit.draugr_boss_spawn);
                    entries.add(ItemInit.FLINT_AND_STEEL);
                    entries.add(ItemInit.COMPASS_ANCIENT_CITY);
                    entries.add(ItemInit.COMPASS_MANSION);
                    entries.add(ItemInit.ANCIENT_SWORD);
                    entries.add(ItemInit.DROSS_JERKY);
                    entries.add(ItemInit.BONE_STICK);
                })
                .build());
    }

    public static Identifier id(String path) {
        return new Identifier(AliveAndWellMain.MOD_ID, path);
    }
}
