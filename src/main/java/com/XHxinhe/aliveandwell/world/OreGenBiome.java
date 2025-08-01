package com.XHxinhe.aliveandwell.world;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;
import java.util.function.Predicate;

public class OreGenBiome {
    public static void addOres() {
        //生成矿物
        BiomeModifications.addFeature(overworldSelector(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(AliveAndWellMain.MOD_ID, "ore_mithril_overworld")));
        BiomeModifications.addFeature(overworldSelector(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(AliveAndWellMain.MOD_ID, "ore_mithril_deepslate_overworld")));
//        BiomeModifications.addFeature(overworldSelector(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(AliveAndWellMain.MOD_ID, "ore_emerald_overworld")));
        BiomeModifications.addFeature(overworldSelector(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(AliveAndWellMain.MOD_ID, "ore_ex_overworld")));
//        BiomeModifications.addFeature(overworldSelector(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(AliveAndWellMain.MOD_ID, "ore_emerald_deepslate_overworld")));
        BiomeModifications.addFeature(overworldSelector(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(AliveAndWellMain.MOD_ID, "ore_ex_deepslate_overworld")));
        BiomeModifications.addFeature(netherSelector(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(AliveAndWellMain.MOD_ID, "ore_adamantium_nether")));

        BiomeModifications.addFeature(overworldSelector(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(AliveAndWellMain.MOD_ID, "wujin_ore_overworld")));
        BiomeModifications.addFeature(overworldSelector(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(AliveAndWellMain.MOD_ID, "wujin_ore_deepslate_overworld")));

        BiomeModifications.addFeature(overworldSelector(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(AliveAndWellMain.MOD_ID, "niter_ore_overworld")));
        BiomeModifications.addFeature(overworldSelector(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(AliveAndWellMain.MOD_ID, "niter_ore_deepslate_overworld")));
    }

    //主世界矿
    public static Predicate<BiomeSelectionContext> overworldSelector() {
        return context -> context.getBiomeRegistryEntry().isIn(BiomeTags.IS_OVERWORLD);
    }

    //地狱矿
    public static Predicate<BiomeSelectionContext> netherSelector() {
        return context -> context.getBiomeRegistryEntry().isIn(BiomeTags.IS_NETHER);
    }

    //末地矿
    public static Predicate<BiomeSelectionContext> endSelector() {
        return context -> context.getBiomeRegistryEntry().isIn(BiomeTags.IS_END);
    }

    //数量
    private static List<PlacementModifier> modifiersWithCount(int count, PlacementModifier heightModifier) {
        return modifiers(CountPlacementModifier.of(count), heightModifier);
    }

    //权重
    private static List<PlacementModifier> modifiersWithRarity(int chance, PlacementModifier heightModifier) {
        return modifiers(RarityFilterPlacementModifier.of(chance), heightModifier);
    }

    private static List<PlacementModifier> modifiers(PlacementModifier countModifier, PlacementModifier heightModifier) {
        return List.of(countModifier, SquarePlacementModifier.of(), heightModifier, BiomePlacementModifier.of());
    }
}
