package com.XHxinhe.aliveandwell.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapIcon;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.util.Util;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;

import java.util.Map;

public class TradeOfferSelf {
    public static final Map PROFESSION_TO_LEVELED_TRADE =
            (Map) Util.make(Maps.newHashMap(), (map) -> {
                map.put(VillagerProfession.FARMER, copyToFastUtilMap(ImmutableMap.of(
                        1, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.WHEAT, 18, 16, 6),
                                new TradeOffers.BuyForOneEmeraldFactory(Items.POTATO, 18, 16, 6),
                                new TradeOffers.BuyForOneEmeraldFactory(Items.CARROT, 18, 16, 6),
                                new TradeOffers.BuyForOneEmeraldFactory(Items.BEETROOT, 18, 16, 6),
                                new TradeOffers.SellItemFactory(Items.BREAD, 1, 5, 16, 3)},
                        2, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Blocks.PUMPKIN, 5, 12, 6),
                                new TradeOffers.SellItemFactory(Items.PUMPKIN_PIE, 1, 4, 3),
                                new TradeOffers.SellItemFactory(Items.APPLE, 1, 4, 16, 6)},
                        3, new TradeOffers.Factory[]{
                                new TradeOffers.SellItemFactory(Items.COOKIE, 3, 18, 6),
                                new TradeOffers.BuyForOneEmeraldFactory(Blocks.MELON, 4, 12, 6)},
                        4, new TradeOffers.Factory[]{
                                new TradeOffers.SellItemFactory(Blocks.CAKE, 1, 1, 12, 6),
//                                new TradeOffers.SellSuspiciousStewFactory(StatusEffects.NIGHT_VISION, 100, 6),
//                                new TradeOffers.SellSuspiciousStewFactory(StatusEffects.JUMP_BOOST, 160, 6),
//                                new TradeOffers.SellSuspiciousStewFactory(StatusEffects.WEAKNESS, 140, 6),
//                                new TradeOffers.SellSuspiciousStewFactory(StatusEffects.BLINDNESS, 120, 6),
//                                new TradeOffers.SellSuspiciousStewFactory(StatusEffects.POISON, 280, 9),
//                                new TradeOffers.SellSuspiciousStewFactory(StatusEffects.SATURATION, 7, 15)
                        },
                        5, new TradeOffers.Factory[]{
                                new TradeOffers.SellItemFactory(Items.GOLDEN_CARROT, 3, 3, 18),
                                new TradeOffers.SellItemFactory(Items.GLISTERING_MELON_SLICE, 4, 3, 18)})));

                map.put(VillagerProfession.FISHERMAN, copyToFastUtilMap(ImmutableMap.of(
                        1, new TradeOffers.Factory[]{
//                                new TradeOffers.BuyForOneEmeraldFactory(Items.STRING, 20, 16, 6),
                                new TradeOffers.BuyForOneEmeraldFactory(Items.COAL, 16, 16, 6),
                                new TradeOffers.ProcessItemFactory(Items.COD, 6, Items.COOKED_COD, 6, 16, 3),
                                new TradeOffers.SellItemFactory(Items.COD_BUCKET, 3, 1, 16, 3)},
                        2, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.COD, 10, 16, 6),
                                new TradeOffers.ProcessItemFactory(Items.SALMON, 6, Items.COOKED_SALMON, 6, 16, 3),
                                new TradeOffers.SellItemFactory(Items.CAMPFIRE, 2, 1, 3)},
                        3, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.SALMON, 10, 16, 12),
                                new TradeOffers.SellEnchantedToolFactory(Items.FISHING_ROD, 3, 3, 2, 0.2F)},
                        4, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.TROPICAL_FISH, 10, 12, 18)},
                        5, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.PUFFERFISH, 6, 12, 18)})));

                map.put(VillagerProfession.SHEPHERD, copyToFastUtilMap(ImmutableMap.of(
                        1, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.LEATHER, 18, 16, 6),
                                new TradeOffers.BuyForOneEmeraldFactory(Items.FEATHER, 18, 16, 6),
//                                new TradeOffers.BuyForOneEmeraldFactory(Blocks.BLACK_WOOL, 18, 16, 6),
//                                new TradeOffers.BuyForOneEmeraldFactory(Blocks.GRAY_WOOL, 18, 16, 6),
                                new TradeOffers.SellItemFactory(Items.SHEARS, 2, 1, 3)},
                        2, new TradeOffers.Factory[]{
//                                new TradeOffers.BuyForOneEmeraldFactory(Items.WHITE_DYE, 12, 16, 6),
//                                new TradeOffers.BuyForOneEmeraldFactory(Items.GRAY_DYE, 12, 16, 6),
//                                new TradeOffers.BuyForOneEmeraldFactory(Items.BLACK_DYE, 12, 16, 6),
//                                new TradeOffers.BuyForOneEmeraldFactory(Items.LIGHT_BLUE_DYE, 12, 16, 6),
//                                new TradeOffers.BuyForOneEmeraldFactory(Items.LIME_DYE, 12, 16, 6),
                                new TradeOffers.SellItemFactory(Blocks.WHITE_WOOL, 1, 1, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.ORANGE_WOOL, 1, 1, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.MAGENTA_WOOL, 1, 1, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.LIGHT_BLUE_WOOL, 1, 1, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.YELLOW_WOOL, 1, 1, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.LIME_WOOL, 1, 1, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.PINK_WOOL, 1, 1, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.GRAY_WOOL, 1, 1, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.LIGHT_GRAY_WOOL, 1, 1, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.CYAN_WOOL, 1, 1, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.PURPLE_WOOL, 1, 1, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.BLUE_WOOL, 1, 1, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.BROWN_WOOL, 1, 1, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.GREEN_WOOL, 1, 1, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.RED_WOOL, 1, 1, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.BLACK_WOOL, 1, 1, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.WHITE_CARPET, 1, 4, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.ORANGE_CARPET, 1, 4, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.MAGENTA_CARPET, 1, 4, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.LIGHT_BLUE_CARPET, 1, 4, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.YELLOW_CARPET, 1, 4, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.LIME_CARPET, 1, 4, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.PINK_CARPET, 1, 4, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.GRAY_CARPET, 1, 4, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.LIGHT_GRAY_CARPET, 1, 4, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.CYAN_CARPET, 1, 4, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.PURPLE_CARPET, 1, 4, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.BLUE_CARPET, 1, 4, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.BROWN_CARPET, 1, 4, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.GREEN_CARPET, 1, 4, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.RED_CARPET, 1, 4, 16, 3),
                                new TradeOffers.SellItemFactory(Blocks.BLACK_CARPET, 1, 4, 16, 5)},
                        3, new TradeOffers.Factory[]{
//                                new TradeOffers.BuyForOneEmeraldFactory(Items.YELLOW_DYE, 12, 16, 20),
//                                new TradeOffers.BuyForOneEmeraldFactory(Items.LIGHT_GRAY_DYE, 12, 16, 20),
//                                new TradeOffers.BuyForOneEmeraldFactory(Items.ORANGE_DYE, 12, 16, 20),
//                                new TradeOffers.BuyForOneEmeraldFactory(Items.RED_DYE, 12, 16, 20),
//                                new TradeOffers.BuyForOneEmeraldFactory(Items.PINK_DYE, 12, 16, 20),
                                new TradeOffers.SellItemFactory(Blocks.WHITE_BED, 3, 1, 12, 6),
                                new TradeOffers.SellItemFactory(Blocks.YELLOW_BED, 3, 1, 12, 6),
                                new TradeOffers.SellItemFactory(Blocks.RED_BED, 3, 1, 12, 6),
                                new TradeOffers.SellItemFactory(Blocks.BLACK_BED, 3, 1, 12, 6),
                                new TradeOffers.SellItemFactory(Blocks.BLUE_BED, 3, 1, 12, 6),
                                new TradeOffers.SellItemFactory(Blocks.BROWN_BED, 3, 1, 12, 6),
                                new TradeOffers.SellItemFactory(Blocks.CYAN_BED, 3, 1, 12, 6),
                                new TradeOffers.SellItemFactory(Blocks.GRAY_BED, 3, 1, 12, 6),
                                new TradeOffers.SellItemFactory(Blocks.GREEN_BED, 3, 1, 12, 6),
                                new TradeOffers.SellItemFactory(Blocks.LIGHT_BLUE_BED, 3, 1, 12, 6),
                                new TradeOffers.SellItemFactory(Blocks.LIGHT_GRAY_BED, 3, 1, 12, 6),
                                new TradeOffers.SellItemFactory(Blocks.LIME_BED, 3, 1, 12, 6),
                                new TradeOffers.SellItemFactory(Blocks.MAGENTA_BED, 3, 1, 12, 6),
                                new TradeOffers.SellItemFactory(Blocks.ORANGE_BED, 3, 1, 12, 6),
                                new TradeOffers.SellItemFactory(Blocks.PINK_BED, 3, 1, 12, 6),
                                new TradeOffers.SellItemFactory(Blocks.PURPLE_BED, 3, 1, 12, 10)},
                        4, new TradeOffers.Factory[]{
//                                new TradeOffers.BuyForOneEmeraldFactory(Items.BROWN_DYE, 12, 16, 30),
//                                new TradeOffers.BuyForOneEmeraldFactory(Items.PURPLE_DYE, 12, 16, 30),
//                                new TradeOffers.BuyForOneEmeraldFactory(Items.BLUE_DYE, 12, 16, 30),
//                                new TradeOffers.BuyForOneEmeraldFactory(Items.GREEN_DYE, 12, 16, 30),
//                                new TradeOffers.BuyForOneEmeraldFactory(Items.MAGENTA_DYE, 12, 16, 30),
//                                new TradeOffers.BuyForOneEmeraldFactory(Items.CYAN_DYE, 12, 16, 30),
                                new TradeOffers.SellItemFactory(Items.WHITE_BANNER, 3, 1, 12, 9),
                                new TradeOffers.SellItemFactory(Items.BLUE_BANNER, 3, 1, 12, 9),
                                new TradeOffers.SellItemFactory(Items.LIGHT_BLUE_BANNER, 3, 1, 12, 9),
                                new TradeOffers.SellItemFactory(Items.RED_BANNER, 3, 1, 12, 9),
                                new TradeOffers.SellItemFactory(Items.PINK_BANNER, 3, 1, 12, 9),
                                new TradeOffers.SellItemFactory(Items.GREEN_BANNER, 3, 1, 12, 9),
                                new TradeOffers.SellItemFactory(Items.LIME_BANNER, 3, 1, 12, 9),
                                new TradeOffers.SellItemFactory(Items.GRAY_BANNER, 3, 1, 12, 9),
                                new TradeOffers.SellItemFactory(Items.BLACK_BANNER, 3, 1, 12, 9),
                                new TradeOffers.SellItemFactory(Items.PURPLE_BANNER, 3, 1, 12, 9),
                                new TradeOffers.SellItemFactory(Items.MAGENTA_BANNER, 3, 1, 12, 9),
                                new TradeOffers.SellItemFactory(Items.CYAN_BANNER, 3, 1, 12, 9),
                                new TradeOffers.SellItemFactory(Items.BROWN_BANNER, 3, 1, 12, 9),
                                new TradeOffers.SellItemFactory(Items.YELLOW_BANNER, 3, 1, 12, 9),
                                new TradeOffers.SellItemFactory(Items.ORANGE_BANNER, 3, 1, 12, 9),
                                new TradeOffers.SellItemFactory(Items.LIGHT_GRAY_BANNER, 3, 1, 12, 9)},
                        5, new TradeOffers.Factory[]{
                                new TradeOffers.SellItemFactory(Items.PAINTING, 2, 3, 18)})));

                map.put(VillagerProfession.FLETCHER, copyToFastUtilMap(ImmutableMap.of(
                        1, new TradeOffers.Factory[]{
//                                new TradeOffers.BuyForOneEmeraldFactory(Items.STICK, 64, 16, 6),
                                new TradeOffers.SellItemFactory(Items.ARROW, 1, 16, 3),
                                new TradeOffers.ProcessItemFactory(Blocks.GRAVEL, 2, Items.FLINT, 2, 12, 3)},
                        2, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.FLINT, 10, 12, 6),
                                new TradeOffers.SellItemFactory(Items.BOW, 2, 1, 5)},
                        3, new TradeOffers.Factory[]{
//                                new TradeOffers.BuyForOneEmeraldFactory(Items.STRING, 64, 16, 12),
                                new TradeOffers.SellItemFactory(Items.CROSSBOW, 3, 1, 6)},
                        4, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.FEATHER, 24, 16, 18),
                                new TradeOffers.SellEnchantedToolFactory(Items.BOW, 2, 3, 9)},
                        5, new TradeOffers.Factory[]{
                                new TradeOffers.SellEnchantedToolFactory(Items.CROSSBOW, 3, 3, 9),
                                new TradeOffers.SellPotionHoldingItemFactory(Items.ARROW, 5, Items.TIPPED_ARROW, 5, 2, 12, 18)})));

                map.put(VillagerProfession.LIBRARIAN, copyToFastUtilMap(ImmutableMap.builder()
                        .put(1, new TradeOffers.Factory[]{
                                new TradeOffers.SellItemFactory(Items.NAME_TAG, 2, 1, 18),
                                new TradeOffers.BuyForOneEmeraldFactory(Items.BONE, 20, 16, 6)})
                        .put(2, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.PAPER, 24, 16, 6),
                                new TradeOffers.EnchantBookFactory(1),
//                                new TradeOffers.SellItemFactory(Blocks.BOOKSHELF, 4, 1, 12, 3)
                        })
                        .put(3,
                                new TradeOffers.Factory[]{
                                        new TradeOffers.BuyForOneEmeraldFactory(Items.BOOK, 6, 12, 6),
                                        new TradeOffers.EnchantBookFactory(5),
                                        new TradeOffers.SellItemFactory(Items.LANTERN, 1, 1, 3)})
                        .put(4, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.INK_SAC, 5, 12, 12),
                                new TradeOffers.EnchantBookFactory(10),
                                new TradeOffers.SellItemFactory(Items.GLASS, 1, 4, 6)})
                        .put(5, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.WRITABLE_BOOK, 2, 12, 18),
                                new TradeOffers.EnchantBookFactory(15),
                                new TradeOffers.SellItemFactory(Items.CLOCK, 5, 1, 9),
                                new TradeOffers.SellItemFactory(Items.COMPASS, 4, 1, 9)})
                        .build()));

                map.put(VillagerProfession.CARTOGRAPHER, copyToFastUtilMap(ImmutableMap.of(
                        1, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.PAPER, 24, 16, 6),
                                new TradeOffers.SellItemFactory(Items.MAP, 7, 1, 3)},
                        2, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.GLASS_PANE, 24, 16, 6),
                                new TradeOffers.SellMapFactory(13, StructureTags.ON_OCEAN_EXPLORER_MAPS, "filled_map.monument", MapIcon.Type.MONUMENT, 12, 3)},
                        3, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.COMPASS, 1, 12, 12),
                                new TradeOffers.SellMapFactory(14, StructureTags.ON_WOODLAND_EXPLORER_MAPS, "filled_map.mansion", MapIcon.Type.MANSION, 12, 6)},
                        4, new TradeOffers.Factory[]{
                                new TradeOffers.SellItemFactory(Items.ITEM_FRAME, 7, 1, 9),
                                new TradeOffers.SellItemFactory(Items.WHITE_BANNER, 3, 1, 9),
                                new TradeOffers.SellItemFactory(Items.BLUE_BANNER, 3, 1, 9),
                                new TradeOffers.SellItemFactory(Items.LIGHT_BLUE_BANNER, 3, 1, 9),
                                new TradeOffers.SellItemFactory(Items.RED_BANNER, 3, 1, 9),
                                new TradeOffers.SellItemFactory(Items.PINK_BANNER, 3, 1, 9),
                                new TradeOffers.SellItemFactory(Items.GREEN_BANNER, 3, 1, 9),
                                new TradeOffers.SellItemFactory(Items.LIME_BANNER, 3, 1, 9),
                                new TradeOffers.SellItemFactory(Items.GRAY_BANNER, 3, 1, 9),
                                new TradeOffers.SellItemFactory(Items.BLACK_BANNER, 3, 1, 9),
                                new TradeOffers.SellItemFactory(Items.PURPLE_BANNER, 3, 1, 9),
                                new TradeOffers.SellItemFactory(Items.MAGENTA_BANNER, 3, 1, 9),
                                new TradeOffers.SellItemFactory(Items.CYAN_BANNER, 3, 1, 9),
                                new TradeOffers.SellItemFactory(Items.BROWN_BANNER, 3, 1, 9),
                                new TradeOffers.SellItemFactory(Items.YELLOW_BANNER, 3, 1, 9),
                                new TradeOffers.SellItemFactory(Items.ORANGE_BANNER, 3, 1, 9),
                                new TradeOffers.SellItemFactory(Items.LIGHT_GRAY_BANNER, 3, 1, 9)},
                        5, new TradeOffers.Factory[]{
                                new TradeOffers.SellItemFactory(Items.GLOBE_BANNER_PATTERN, 8, 1, 18)})));

                map.put(VillagerProfession.CLERIC, copyToFastUtilMap(ImmutableMap.of(
                        1, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.ROTTEN_FLESH, 20, 16, 6),
                                new TradeOffers.SellItemFactory(Items.REDSTONE, 1, 2, 3)},
                        2, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.GOLD_INGOT, 6, 12, 6),
                                new TradeOffers.SellItemFactory(Items.LAPIS_LAZULI, 1, 1, 3)},
                        3, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.RABBIT_FOOT, 3, 12, 12),
                                new TradeOffers.SellItemFactory(Blocks.GLOWSTONE, 4, 1, 12, 6)},
                        4, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.SCUTE, 4, 12, 18),
                                new TradeOffers.BuyForOneEmeraldFactory(Items.GLASS_BOTTLE, 18, 12, 18),
                                new TradeOffers.SellItemFactory(Items.ENDER_PEARL, 5, 1, 9)},
                        5, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.NETHER_WART, 22, 12, 18),
                                new TradeOffers.SellItemFactory(Items.EXPERIENCE_BOTTLE, 3, 1, 18)})));

                map.put(VillagerProfession.ARMORER, copyToFastUtilMap(ImmutableMap.of(
                        1, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.COAL, 16, 16, 6),
                                new TradeOffers.SellItemFactory(
                                        new ItemStack(Items.IRON_LEGGINGS), 7, 1, 12, 1, 0.2F),
                                new TradeOffers.SellItemFactory(
                                        new ItemStack(Items.IRON_BOOTS), 4, 1, 12, 1, 0.2F),
                                new TradeOffers.SellItemFactory(
                                        new ItemStack(Items.IRON_HELMET), 5, 1, 12, 1, 0.2F),
                                new TradeOffers.SellItemFactory(
                                        new ItemStack(Items.IRON_CHESTPLATE), 9, 1, 12, 1, 0.2F)},
                        2, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.IRON_INGOT, 8, 12, 6),
                                new TradeOffers.SellItemFactory(
                                        new ItemStack(Items.BELL), 36, 1, 12, 1, 0.2F),
                                new TradeOffers.SellItemFactory(
                                        new ItemStack(Items.CHAINMAIL_BOOTS), 1, 1, 12, 1, 0.2F),
                                new TradeOffers.SellItemFactory(
                                        new ItemStack(Items.CHAINMAIL_LEGGINGS), 3, 1, 12, 1, 0.2F)},
                        3, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.LAVA_BUCKET, 1, 12, 12),
//                                new TradeOffers.BuyForOneEmeraldFactory(Items.IRON, 1, 12, 20),
                                new TradeOffers.SellItemFactory(
                                        new ItemStack(Items.CHAINMAIL_HELMET), 1, 1, 12, 2, 0.2F),
                                new TradeOffers.SellItemFactory(
                                        new ItemStack(Items.CHAINMAIL_CHESTPLATE), 4, 1, 12, 2, 0.2F),
                                new TradeOffers.SellItemFactory(
                                        new ItemStack(Items.SHIELD), 5, 1, 12, 2, 0.2F)},
                        4, new TradeOffers.Factory[]{
                                new TradeOffers.SellEnchantedToolFactory(Items.IRON_LEGGINGS, 14, 3, 3, 0.2F),
                                new TradeOffers.SellEnchantedToolFactory(Items.IRON_BOOTS, 8, 3, 3, 0.2F)},
                        5, new TradeOffers.Factory[]{
                                new TradeOffers.SellEnchantedToolFactory(Items.IRON_HELMET, 8, 3, 6, 0.2F),
                                new TradeOffers.SellEnchantedToolFactory(Items.IRON_CHESTPLATE, 16, 3, 6, 0.2F)})));

                map.put(VillagerProfession.WEAPONSMITH, copyToFastUtilMap(ImmutableMap.of(
                        1, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.COAL, 16, 16, 6),
                                new TradeOffers.SellItemFactory(
                                        new ItemStack(Items.IRON_AXE), 3, 1, 12, 1, 0.2F),
                                new TradeOffers.SellEnchantedToolFactory(Items.IRON_SWORD, 2, 3, 3)},
                        2, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.IRON_INGOT, 8, 12, 6),
                                new TradeOffers.SellItemFactory(
                                        new ItemStack(Items.BELL), 36, 1, 12, 1, 0.2F)},
                        3, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.FLINT, 10, 12, 12)},
                        4, new TradeOffers.Factory[]{
//                                new TradeOffers.BuyForOneEmeraldFactory(Items.IRON, 1, 12, 30),
                                new TradeOffers.SellEnchantedToolFactory(Items.IRON_AXE, 12, 3, 3, 0.2F)},
                        5, new TradeOffers.Factory[]{
                                new TradeOffers.SellEnchantedToolFactory(Items.IRON_SWORD, 8, 3, 6, 0.2F)})));

                map.put(VillagerProfession.TOOLSMITH, copyToFastUtilMap(ImmutableMap.of(
                        1, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.COAL, 16, 16, 6)
//                                ,
//                            new TradeOffers.SellItemFactory(
//                                    new ItemStack(Items.STONE_AXE), 1, 1, 12, 1, 0.2F),
//                            new TradeOffers.SellItemFactory(
//                                    new ItemStack(Items.STONE_SHOVEL), 1, 1, 12, 1, 0.2F),
//                            new TradeOffers.SellItemFactory(
//                                    new ItemStack(Items.STONE_PICKAXE), 1, 1, 12, 1, 0.2F),
//                            new TradeOffers.SellItemFactory(
//                                    new ItemStack(Items.STONE_HOE), 1, 1, 12, 1, 0.2F)
                        },
                        2, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.IRON_INGOT, 8, 12, 6),
                                new TradeOffers.SellItemFactory(
                                        new ItemStack(Items.BELL), 36, 1, 12, 1, 0.2F)},
                        3, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.FLINT, 15, 12, 12),
                                new TradeOffers.SellEnchantedToolFactory(Items.IRON_AXE, 1, 3, 2, 0.2F),
                                new TradeOffers.SellEnchantedToolFactory(Items.IRON_SHOVEL, 2, 3, 2, 0.2F),
                                new TradeOffers.SellEnchantedToolFactory(Items.IRON_PICKAXE, 3, 3, 2, 0.2F),
                                new TradeOffers.SellItemFactory(
                                        new ItemStack(Items.IRON_HOE), 4, 1, 3, 2, 0.2F)},
                        4, new TradeOffers.Factory[]{
//                                new TradeOffers.BuyForOneEmeraldFactory(Items.IRON, 1, 12, 30),
                                new TradeOffers.SellEnchantedToolFactory(Items.IRON_AXE, 12, 3, 3, 0.2F),
                                new TradeOffers.SellEnchantedToolFactory(Items.IRON_SHOVEL, 5, 3, 3, 0.2F)},
                        5, new TradeOffers.Factory[]{
                                new TradeOffers.SellEnchantedToolFactory(Items.IRON_PICKAXE, 13, 3, 30, 0.2F)})));

                map.put(VillagerProfession.BUTCHER, copyToFastUtilMap(ImmutableMap.of(
                        1, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.CHICKEN, 14, 16, 6),
                                new TradeOffers.BuyForOneEmeraldFactory(Items.PORKCHOP, 12, 16, 6),
                                new TradeOffers.BuyForOneEmeraldFactory(Items.RABBIT, 6, 16, 6),
                                new TradeOffers.SellItemFactory(Items.RABBIT_STEW, 5, 1, 3)},
                        2, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.COAL, 16, 16, 6),
                                new TradeOffers.SellItemFactory(Items.COOKED_PORKCHOP, 1, 5, 16, 3),
                                new TradeOffers.SellItemFactory(Items.COOKED_CHICKEN, 1, 8, 16, 3)},
                        3, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.MUTTON, 14, 16, 12),
                                new TradeOffers.BuyForOneEmeraldFactory(Items.BEEF, 6, 16, 12)},
                        4, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.DRIED_KELP_BLOCK, 4, 12, 18)},
                        5, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.SWEET_BERRIES, 22, 12, 18)})));

                map.put(VillagerProfession.LEATHERWORKER, copyToFastUtilMap(ImmutableMap.of(
                        1, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.LEATHER, 16, 16, 6),
                                new TradeOffers.SellDyedArmorFactory(Items.LEATHER_LEGGINGS, 9),
                                new TradeOffers.SellDyedArmorFactory(Items.LEATHER_CHESTPLATE, 7)},
                        2, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.FLINT, 13, 12, 6),
                                new TradeOffers.SellDyedArmorFactory(Items.LEATHER_HELMET, 5, 12, 3),
                                new TradeOffers.SellDyedArmorFactory(Items.LEATHER_BOOTS, 4, 12, 3)},
                        3, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.RABBIT_HIDE, 6, 12, 12),
                                new TradeOffers.SellDyedArmorFactory(Items.LEATHER_CHESTPLATE, 7)},
                        4, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.SCUTE, 8, 12, 18),
                                new TradeOffers.SellDyedArmorFactory(Items.LEATHER_HORSE_ARMOR, 6, 12, 9)},
                        5, new TradeOffers.Factory[]{
                                new TradeOffers.SellItemFactory(
                                        new ItemStack(Items.SADDLE), 6, 1, 12, 6, 0.2F),
                                new TradeOffers.SellDyedArmorFactory(Items.LEATHER_HELMET, 5, 12, 18)})));

                map.put(VillagerProfession.MASON, copyToFastUtilMap(ImmutableMap.of(
                        1, new TradeOffers.Factory[]{
//                                new TradeOffers.BuyForOneEmeraldFactory(Items.CLAY_BALL, 2, 16, 6),
                                new TradeOffers.SellItemFactory(Items.BRICK, 1, 2, 16, 3)},
                        2, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Blocks.COPPER_BLOCK, 10, 16, 6),
//                                new TradeOffers.SellItemFactory(Blocks.CHISELED_STONE_BRICKS, 1, 4, 16, 5)
                        },
                        3, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Blocks.COPPER_BLOCK, 16, 16, 12),
//                                new TradeOffers.BuyForOneEmeraldFactory(Blocks.ANDESITE, 16, 16, 20),
//                                new TradeOffers.BuyForOneEmeraldFactory(Blocks.DIORITE, 16, 16, 20),
//                                new TradeOffers.SellItemFactory(Blocks.DRIPSTONE_BLOCK, 1, 4, 16, 6),
//                                new TradeOffers.SellItemFactory(Blocks.POLISHED_ANDESITE, 1, 4, 16, 6),
//                                new TradeOffers.SellItemFactory(Blocks.POLISHED_DIORITE, 1, 4, 16, 6),
//                                new TradeOffers.SellItemFactory(Blocks.POLISHED_GRANITE, 1, 4, 16, 10)
                        },
                        4, new TradeOffers.Factory[]{
                                new TradeOffers.BuyForOneEmeraldFactory(Items.QUARTZ, 8, 12, 18),
//                                new TradeOffers.SellItemFactory(Blocks.ORANGE_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.WHITE_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.BLUE_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.LIGHT_BLUE_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.GRAY_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.LIGHT_GRAY_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.BLACK_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.RED_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.PINK_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.MAGENTA_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.LIME_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.GREEN_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.CYAN_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.PURPLE_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.YELLOW_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.BROWN_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.ORANGE_GLAZED_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.WHITE_GLAZED_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.BLUE_GLAZED_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.GRAY_GLAZED_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.BLACK_GLAZED_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.RED_GLAZED_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.PINK_GLAZED_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.MAGENTA_GLAZED_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.LIME_GLAZED_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.GREEN_GLAZED_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.CYAN_GLAZED_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.PURPLE_GLAZED_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.YELLOW_GLAZED_TERRACOTTA, 1, 1, 12, 9),
//                                new TradeOffers.SellItemFactory(Blocks.BROWN_GLAZED_TERRACOTTA, 1, 1, 12, 15)
                        },
                        5, new TradeOffers.Factory[]{
                                new TradeOffers.SellItemFactory(Blocks.QUARTZ_PILLAR, 1, 1, 12, 18),
                                new TradeOffers.SellItemFactory(Blocks.QUARTZ_BLOCK, 1, 1, 12, 18)})));
            });
    private static Int2ObjectMap<TradeOffers.Factory[]> copyToFastUtilMap(ImmutableMap<Object, Object> map) {
        return new Int2ObjectOpenHashMap(map);
    }
}
