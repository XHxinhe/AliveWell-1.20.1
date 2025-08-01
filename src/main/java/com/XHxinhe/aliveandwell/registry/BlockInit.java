package com.XHxinhe.aliveandwell.registry;

import com.XHxinhe.aliveandwell.block.portal.*;
import com.XHxinhe.aliveandwell.block.randompos.BaseAdamantiumRandomBlock;
import com.XHxinhe.aliveandwell.block.randompos.BaseMithrilRandomBlock;
import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.tablesandfurnaces.blocks.TheCraftingTableBlock;
import com.XHxinhe.aliveandwell.tablesandfurnaces.blocks.TheFurnace;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

public class BlockInit {
    public static final Block ORE_MITHRIL = new ExperienceDroppingBlock(FabricBlockSettings.copy(Blocks.DIAMOND_ORE));
    public static final Block ORE_ADAMANTIUM = new ExperienceDroppingBlock(FabricBlockSettings.copy(Blocks.DIAMOND_ORE));
    public static final Block ORE_ADAMANTIUM_NETHER = new ExperienceDroppingBlock(FabricBlockSettings.copy(Blocks.DIAMOND_ORE));
    public static final Block ORE_MITHRIL_DEEPSLATE = new ExperienceDroppingBlock(FabricBlockSettings.copy(Blocks.DIAMOND_ORE));
    public static final Block ORE_EX = new ExperienceDroppingBlock(FabricBlockSettings.copy(Blocks.DIAMOND_ORE));
    public static final Block WUJIN_ORE = new ExperienceDroppingBlock(FabricBlockSettings.copy(Blocks.DIAMOND_ORE).luminance(createLightLevelFromBlockState2(10)));
    public static final Block WUJIN_ORE_DEEPSLATE = new ExperienceDroppingBlock(FabricBlockSettings.copy(Blocks.DEEPSLATE_DIAMOND_ORE).luminance(createLightLevelFromBlockState2(9)));

    public static final Block NITER_ORE = new ExperienceDroppingBlock(FabricBlockSettings.copy(Blocks.DIAMOND_ORE));
    public static final Block NITER_ORE_DEEPSLATE = new ExperienceDroppingBlock(FabricBlockSettings.copy(Blocks.DEEPSLATE_DIAMOND_ORE));
    public static final Block ORE_EX_DEEPSLATE = new ExperienceDroppingBlock(FabricBlockSettings.copy(Blocks.DEEPSLATE_DIAMOND_ORE).luminance(createLightLevelFromBlockState2(10)));

    //portals
    public static final CustomPortalBlock RANDOM_A_PORTAL = new RandomAPortalBlock(FabricBlockSettings.copy(Blocks.NETHER_PORTAL).strength(-1.0F));
    public static final CustomPortalBlock RANDOM_B_PORTAL = new RandomBPortalBlock(FabricBlockSettings.copy(Blocks.NETHER_PORTAL).strength(-1.0F));
    public static final CustomPortalBlock SPAWNPOINT_PORTAL = new SpawnpointPortalBlock(FabricBlockSettings.copy(Blocks.NETHER_PORTAL).strength(-1.0F));
    public static final CustomPortalBlock UNDERWORLD_PORTAL = new UnderworldPortalBlock(FabricBlockSettings.copy(Blocks.NETHER_PORTAL).strength(-1.0F));
    public static final CustomPortalBlock NETHERWORLD_PORTAL = new NetherworldPortalBlock(FabricBlockSettings.copy(Blocks.NETHER_PORTAL).strength(-1.0F));
    public static final Block ADAMANTIUM_BLOCK = new Block(FabricBlockSettings.copy(Blocks.IRON_BLOCK));
    public static final Block MITHRIL_BLOCK = new Block(FabricBlockSettings.copy(Blocks.IRON_BLOCK));
    public static final Block FRAME_MITHRIL = new Block(FabricBlockSettings.copy(Blocks.STONE));
    public static final Block FRAME_ADAMANTIUM= new Block(FabricBlockSettings.copy(Blocks.STONE));
    public static final Block FRAME_SPAWNPOINT = new Block(FabricBlockSettings.copy(Blocks.STONE).strength(3.0F, 3.0F));

    public static final Block random_mithril_jia = new BaseMithrilRandomBlock(FabricBlockSettings.copy(Blocks.STONE));
    public static final Block random_mithril_yi = new BaseMithrilRandomBlock(FabricBlockSettings.copy(Blocks.STONE));
    public static final Block random_mithril_bing = new BaseMithrilRandomBlock(FabricBlockSettings.copy(Blocks.STONE));
    public static final Block random_mithril_ding = new BaseMithrilRandomBlock(FabricBlockSettings.copy(Blocks.STONE));
    public static final Block random_mithril_wu = new BaseMithrilRandomBlock(FabricBlockSettings.copy(Blocks.STONE));
    public static final Block random_mithril_ji = new BaseMithrilRandomBlock(FabricBlockSettings.copy(Blocks.STONE));
    public static final Block random_mithril_geng = new BaseMithrilRandomBlock(FabricBlockSettings.copy(Blocks.STONE));
    public static final Block random_mithril_xin = new BaseMithrilRandomBlock(FabricBlockSettings.copy(Blocks.STONE));
    public static final Block random_mithril_ren = new BaseMithrilRandomBlock(FabricBlockSettings.copy(Blocks.STONE));
    public static final Block random_mithril_gui = new BaseMithrilRandomBlock(FabricBlockSettings.copy(Blocks.STONE));

    public static final Block random_adamantium_jia = new BaseAdamantiumRandomBlock(FabricBlockSettings.copy(Blocks.STONE));
    public static final Block random_adamantium_yi = new BaseAdamantiumRandomBlock(FabricBlockSettings.copy(Blocks.STONE));
    public static final Block random_adamantium_bing = new BaseAdamantiumRandomBlock(FabricBlockSettings.copy(Blocks.STONE));
    public static final Block random_adamantium_ding = new BaseAdamantiumRandomBlock(FabricBlockSettings.copy(Blocks.STONE));
    public static final Block random_adamantium_wu = new BaseAdamantiumRandomBlock(FabricBlockSettings.copy(Blocks.STONE));
    public static final Block random_adamantium_ji = new BaseAdamantiumRandomBlock(FabricBlockSettings.copy(Blocks.STONE));
    public static final Block random_adamantium_geng = new BaseAdamantiumRandomBlock(FabricBlockSettings.copy(Blocks.STONE));
    public static final Block random_adamantium_xin = new BaseAdamantiumRandomBlock(FabricBlockSettings.copy(Blocks.STONE));
    public static final Block random_adamantium_ren = new BaseAdamantiumRandomBlock(FabricBlockSettings.copy(Blocks.STONE));
    public static final Block random_adamantium_gui = new BaseAdamantiumRandomBlock(FabricBlockSettings.copy(Blocks.STONE));

    //tables
    public static final List<Block> allFurnaces = new ArrayList<>();
    public static Block FLINT_CRAFTING_TABLE = new TheCraftingTableBlock((AbstractBlock.Settings.create().strength(0.2F).sounds(BlockSoundGroup.WOOD)));//燧石工作台
    public static Block COPPER_CRAFTING_TABLE = new TheCraftingTableBlock((AbstractBlock.Settings.create().strength(0.2F).sounds(BlockSoundGroup.WOOD)));//铜工作台
    public static Block IRON_CRAFTING_TABLE = new TheCraftingTableBlock((AbstractBlock.Settings.create().strength(0.2F).sounds(BlockSoundGroup.WOOD)));//铁工作台
    public static Block DIAMOND_CRAFTING_TABLE = new TheCraftingTableBlock((AbstractBlock.Settings.create().strength(0.2F).sounds(BlockSoundGroup.WOOD)));//秘银工作台
    public static Block NETHERITE_CRAFTING_TABLE = new TheCraftingTableBlock((AbstractBlock.Settings.create().strength(0.2F).sounds(BlockSoundGroup.WOOD)));//下界合金工作台

    public static Block CLAY_FURNACE = new TheFurnace((AbstractBlock.Settings.create().sounds(BlockSoundGroup.STONE).strength(0.2F).luminance(createLightLevelFromBlockState(12)).sounds(BlockSoundGroup.STONE)));//粘土熔炉
    public static Block OBSIDIAN_FURNACE = new TheFurnace((AbstractBlock.Settings.create().sounds(BlockSoundGroup.STONE).strength(0.2F).luminance(createLightLevelFromBlockState(11)).sounds(BlockSoundGroup.STONE)));//黑曜石熔炉
    public static Block NETHERRACK_FURNACE = new TheFurnace((AbstractBlock.Settings.create().sounds(BlockSoundGroup.STONE).strength(0.2F).luminance(createLightLevelFromBlockState(13)).sounds(BlockSoundGroup.STONE)));//地狱岩熔炉


//    public static final Block HARD_GLASS = new HardGlass(MapColor.CLEAR);
//    public static final Block HARD_GLASS_WHITE = new HardGlass(MapColor.WHITE);//白色
//    public static final Block HARD_GLASS_ORANGE = new HardGlass(MapColor.ORANGE);//橘色
//    public static final Block HARD_GLASS_MAGENTA = new HardGlass(MapColor.MAGENTA);//品红
//    public static final Block HARD_GLASS_LIGHT_BLUE = new HardGlass(MapColor.LIGHT_BLUE);//亮蓝色
//    public static final Block HARD_GLASS_YELLOW = new HardGlass(MapColor.YELLOW);//黄色
//    public static final Block HARD_GLASS_LIME = new HardGlass(MapColor.LIME);//绿黄色
//    public static final Block HARD_GLASS_PINK = new HardGlass(MapColor.PINK);//粉红色
//    public static final Block HARD_GLASS_GRAY = new HardGlass(MapColor.GRAY);//灰色
//    public static final Block HARD_GLASS_LIGHT_GRAY = new HardGlass(MapColor.LIGHT_GRAY);//亮灰色
//    public static final Block HARD_GLASS_CYAN = new HardGlass(MapColor.CYAN);//青色
//    public static final Block HARD_GLASS_PURPLE = new HardGlass(MapColor.PURPLE);//紫色
//    public static final Block HARD_GLASS_BLUE = new HardGlass(MapColor.BLUE);//蓝色
//    public static final Block HARD_GLASS_BROWN = new HardGlass(MapColor.BROWN);//棕色
//    public static final Block HARD_GLASS_GREEN = new HardGlass(MapColor.GREEN);//绿色
//    public static final Block HARD_GLASS_RED = new HardGlass(MapColor.RED);//红色
//    public static final Block HARD_GLASS_BLACK = new HardGlass(MapColor.BLACK);//黑色
    public static void registerBlocks() {
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "ore_mithril"), ORE_MITHRIL);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "ore_mithril_deepslate"), ORE_MITHRIL_DEEPSLATE);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "ore_adamantium"), ORE_ADAMANTIUM);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "ore_adamantium_nether"), ORE_ADAMANTIUM_NETHER);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "ore_ex"), ORE_EX);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "ore_ex_deepslate"), ORE_EX_DEEPSLATE);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "adamantium_block"), ADAMANTIUM_BLOCK);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "mithril_block"), MITHRIL_BLOCK);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "wujin_ore"), WUJIN_ORE);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "wujin_ore_deepslate"), WUJIN_ORE_DEEPSLATE);

        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "niter_ore"), NITER_ORE);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "niter_ore_deepslate"), NITER_ORE_DEEPSLATE);

        //portals
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "random_a_portal"), RANDOM_A_PORTAL);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "random_b_portal"), RANDOM_B_PORTAL);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "spawnpoint_portal"), SPAWNPOINT_PORTAL);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "underworld_portal"), UNDERWORLD_PORTAL);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "netherworld_portal"), NETHERWORLD_PORTAL);

        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "frame_mithril"), FRAME_MITHRIL);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "frame_adamantium"), FRAME_ADAMANTIUM);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "frame_spawnpoint"), FRAME_SPAWNPOINT);

        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "random_mithril_jia"), random_mithril_jia);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "random_mithril_yi"), random_mithril_yi);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "random_mithril_bing"), random_mithril_bing);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "random_mithril_ding"), random_mithril_ding);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "random_mithril_wu"), random_mithril_wu);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "random_mithril_ji"), random_mithril_ji);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "random_mithril_geng"), random_mithril_geng);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "random_mithril_xin"), random_mithril_xin);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "random_mithril_ren"), random_mithril_ren);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "random_mithril_gui"), random_mithril_gui);

        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "random_adamantium_jia"), random_adamantium_jia);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "random_adamantium_yi"), random_adamantium_yi);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "random_adamantium_bing"), random_adamantium_bing);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "random_adamantium_ding"), random_adamantium_ding);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "random_adamantium_wu"), random_adamantium_wu);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "random_adamantium_ji"), random_adamantium_ji);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "random_adamantium_geng"), random_adamantium_geng);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "random_adamantium_xin"), random_adamantium_xin);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "random_adamantium_ren"), random_adamantium_ren);
        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "random_adamantium_gui"), random_adamantium_gui);

        //tables
        Registry.register(Registries.BLOCK, new Identifier("aliveandwell", "flint_crafting_table"), FLINT_CRAFTING_TABLE);
        Registry.register(Registries.BLOCK, new Identifier("aliveandwell", "copper_crafting_table"), COPPER_CRAFTING_TABLE);
        Registry.register(Registries.BLOCK, new Identifier("aliveandwell", "iron_crafting_table"), IRON_CRAFTING_TABLE);
        Registry.register(Registries.BLOCK, new Identifier("aliveandwell", "diamond_crafting_table"),DIAMOND_CRAFTING_TABLE);
        Registry.register(Registries.BLOCK, new Identifier("aliveandwell", "netherite_crafting_table"), NETHERITE_CRAFTING_TABLE);

        Registry.register(Registries.BLOCK, new Identifier("aliveandwell", "clay_furnace"), CLAY_FURNACE);
        Registry.register(Registries.BLOCK, new Identifier("aliveandwell", "obsidian_furnace"), OBSIDIAN_FURNACE);
        Registry.register(Registries.BLOCK, new Identifier("aliveandwell", "netherrack_furnace"), NETHERRACK_FURNACE);

        allFurnaces.add(CLAY_FURNACE);
        allFurnaces.add(OBSIDIAN_FURNACE);
        allFurnaces.add(NETHERRACK_FURNACE);

//        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass"), HARD_GLASS);
//        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_white"), HARD_GLASS_WHITE);
//        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_orange"), HARD_GLASS_ORANGE);
//        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_magenta"), HARD_GLASS_MAGENTA);
//        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_light_blue"), HARD_GLASS_LIGHT_BLUE);
//        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_yellow"), HARD_GLASS_YELLOW);
//        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_lime"), HARD_GLASS_LIME);
//        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_pink"), HARD_GLASS_PINK);
//        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_gray"), HARD_GLASS_GRAY);
//        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_light_gray"), HARD_GLASS_LIGHT_GRAY);
//        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_cyan"), HARD_GLASS_CYAN);
//        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_purple"), HARD_GLASS_PURPLE);
//        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_blue"), HARD_GLASS_BLUE);
//        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_brown"), HARD_GLASS_BROWN);
//        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_green"), HARD_GLASS_GREEN);
//        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_red"), HARD_GLASS_RED);
//        Registry.register(Registries.BLOCK, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_black"), HARD_GLASS_BLACK);

    }

    public static void registerBlockItems() {

        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "ore_mithril"), new BlockItem(ORE_MITHRIL, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "ore_adamantium"), new BlockItem(ORE_ADAMANTIUM, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "ore_adamantium_nether"), new BlockItem(ORE_ADAMANTIUM_NETHER, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "ore_mithril_deepslate"), new BlockItem(ORE_MITHRIL_DEEPSLATE, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "ore_ex"), new BlockItem(ORE_EX, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "ore_ex_deepslate"), new BlockItem(ORE_EX_DEEPSLATE, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "adamantium_block"), new BlockItem(ADAMANTIUM_BLOCK, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "mithril_block"), new BlockItem(MITHRIL_BLOCK, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "wujin_ore"), new BlockItem(WUJIN_ORE, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "wujin_ore_deepslate"), new BlockItem(WUJIN_ORE_DEEPSLATE, new Item.Settings().fireproof()));

        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "niter_ore"), new BlockItem(NITER_ORE, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "niter_ore_deepslate"), new BlockItem(NITER_ORE_DEEPSLATE, new Item.Settings().fireproof()));

        //portals
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "random_a_portal"), new BlockItem(RANDOM_A_PORTAL, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "random_b_portal"), new BlockItem(RANDOM_B_PORTAL, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "spawnpoint_portal"), new BlockItem(SPAWNPOINT_PORTAL, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "underworld_portal"), new BlockItem(UNDERWORLD_PORTAL, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "netherworld_portal"), new BlockItem(NETHERWORLD_PORTAL, new Item.Settings().fireproof()));

        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "frame_mithril"), new BlockItem(FRAME_MITHRIL, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "frame_adamantium"), new BlockItem(FRAME_ADAMANTIUM, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "frame_spawnpoint"), new BlockItem(FRAME_SPAWNPOINT, new Item.Settings().fireproof()));

        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "random_mithril_jia"), new BlockItem(random_mithril_jia, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "random_mithril_yi"), new BlockItem(random_mithril_yi, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "random_mithril_bing"), new BlockItem(random_mithril_bing, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "random_mithril_ding"), new BlockItem(random_mithril_ding, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "random_mithril_wu"), new BlockItem(random_mithril_wu, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "random_mithril_ji"), new BlockItem(random_mithril_ji, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "random_mithril_geng"), new BlockItem(random_mithril_geng, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "random_mithril_xin"), new BlockItem(random_mithril_xin, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "random_mithril_ren"), new BlockItem(random_mithril_ren, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "random_mithril_gui"), new BlockItem(random_mithril_gui, new Item.Settings().fireproof()));

        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "random_adamantium_jia"), new BlockItem(random_adamantium_jia, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "random_adamantium_yi"), new BlockItem(random_adamantium_yi, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "random_adamantium_bing"), new BlockItem(random_adamantium_bing, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "random_adamantium_ding"), new BlockItem(random_adamantium_ding, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "random_adamantium_wu"), new BlockItem(random_adamantium_wu, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "random_adamantium_ji"), new BlockItem(random_adamantium_ji, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "random_adamantium_geng"), new BlockItem(random_adamantium_geng, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "random_adamantium_xin"), new BlockItem(random_adamantium_xin, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "random_adamantium_ren"), new BlockItem(random_adamantium_ren, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "random_adamantium_gui"), new BlockItem(random_adamantium_gui, new Item.Settings().fireproof()));

        //tables
        Registry.register(Registries.ITEM, new Identifier("aliveandwell", "flint_crafting_table"), new BlockItem(FLINT_CRAFTING_TABLE, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier("aliveandwell", "copper_crafting_table"), new BlockItem(COPPER_CRAFTING_TABLE, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier("aliveandwell", "iron_crafting_table"), new BlockItem(IRON_CRAFTING_TABLE, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier("aliveandwell", "diamond_crafting_table"), new BlockItem(DIAMOND_CRAFTING_TABLE, new Item.Settings().fireproof()));
        Registry.register(Registries.ITEM, new Identifier("aliveandwell", "netherite_crafting_table"), new BlockItem(NETHERITE_CRAFTING_TABLE, new Item.Settings().fireproof()));

        Registry.register(Registries.ITEM, new Identifier("aliveandwell", "clay_furnace"), new BlockItem(CLAY_FURNACE, new Item.Settings().maxCount(1).fireproof()));
        Registry.register(Registries.ITEM, new Identifier("aliveandwell", "obsidian_furnace"), new BlockItem(OBSIDIAN_FURNACE, new Item.Settings().maxCount(1).fireproof()));
        Registry.register(Registries.ITEM, new Identifier("aliveandwell", "netherrack_furnace"), new BlockItem(NETHERRACK_FURNACE, new Item.Settings().maxCount(1).fireproof()));

//        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass"), new BlockItem(HARD_GLASS, new Item.Settings().fireproof()));
//        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_white"), new BlockItem(HARD_GLASS_WHITE, new Item.Settings().fireproof()));
//        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_orange"), new BlockItem(HARD_GLASS_ORANGE, new Item.Settings().fireproof()));
//        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_magenta"), new BlockItem(HARD_GLASS_MAGENTA, new Item.Settings().fireproof()));
//        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_light_blue"), new BlockItem(HARD_GLASS_LIGHT_BLUE, new Item.Settings().fireproof()));
//        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_yellow"), new BlockItem(HARD_GLASS_YELLOW, new Item.Settings().fireproof()));
//        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_lime"), new BlockItem(HARD_GLASS_LIME, new Item.Settings().fireproof()));
//        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_pink"), new BlockItem(HARD_GLASS_PINK, new Item.Settings().fireproof()));
//        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_gray"), new BlockItem(HARD_GLASS_GRAY, new Item.Settings().fireproof()));
//        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_light_gray"), new BlockItem(HARD_GLASS_LIGHT_GRAY, new Item.Settings().fireproof()));
//        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_cyan"), new BlockItem(HARD_GLASS_CYAN, new Item.Settings().fireproof()));
//        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_purple"), new BlockItem(HARD_GLASS_PURPLE, new Item.Settings().fireproof()));
//        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_blue"), new BlockItem(HARD_GLASS_BLUE, new Item.Settings().fireproof()));
//        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_brown"), new BlockItem(HARD_GLASS_BROWN, new Item.Settings().fireproof()));
//        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_green"), new BlockItem(HARD_GLASS_GREEN, new Item.Settings().fireproof()));
//        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_red"), new BlockItem(HARD_GLASS_RED, new Item.Settings().fireproof()));
//        Registry.register(Registries.ITEM, new Identifier(AliveAndWellMain.MOD_ID, "hard_glass_black"), new BlockItem(HARD_GLASS_BLACK, new Item.Settings().fireproof()));

    }

    public static void registerFuels() {
        FuelRegistry.INSTANCE.add(FLINT_CRAFTING_TABLE, 300);
        FuelRegistry.INSTANCE.add(COPPER_CRAFTING_TABLE, 300);
        FuelRegistry.INSTANCE.add(IRON_CRAFTING_TABLE, 300);
        FuelRegistry.INSTANCE.add(DIAMOND_CRAFTING_TABLE, 300);
        FuelRegistry.INSTANCE.add(NETHERITE_CRAFTING_TABLE, 300);
    }

    public static List<Block> getFurnaces() {
        return allFurnaces;
    }

    private static ToIntFunction<BlockState> createLightLevelFromBlockState(int litLevel) {
        return (blockState) -> (Boolean)blockState.get(Properties.LIT) ? litLevel : 0;
    }

    private static ToIntFunction<BlockState> createLightLevelFromBlockState2(int litLevel) {
        return (blockState) -> litLevel;
    }

}
