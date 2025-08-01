package com.XHxinhe.aliveandwell.registry;

// 导入所有自定义物品类
import com.XHxinhe.aliveandwell.item.*;
import com.XHxinhe.aliveandwell.item.exitem.*;
import com.XHxinhe.aliveandwell.item.food.EnchantedGoldenCarrotItem;
import com.XHxinhe.aliveandwell.item.food.JuHuaGao;
import com.XHxinhe.aliveandwell.item.food.SalaItem;
import com.XHxinhe.aliveandwell.item.tool.ExPickaxe;
import com.XHxinhe.aliveandwell.item.xpitem.XpItem;
import com.XHxinhe.aliveandwell.item.xpitem.XpItem1;
import com.XHxinhe.aliveandwell.flintcoppertool.armor.CopperArmorBase;
import com.XHxinhe.aliveandwell.flintcoppertool.item.CopperToolBase;
import com.XHxinhe.aliveandwell.flintcoppertool.item.FlintToolBase;
import com.XHxinhe.aliveandwell.flintcoppertool.item.HoeBase;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ItemInit {

    // --- 物品字段声明 ---
    public static final Item XP_ITEM;
    public static final Item XP_ITEN1;
    public static final Item WATER_BOWL;
    public static final Item ITEM_EN_GENSTONE;
    public static final Item INGOT_WUJIN;
    public static final Item INGOT_MITHRIL;
    public static final Item ROW_MITHRIL;
    public static final Item ROW_ADAMANTIUM;
    public static final Item INGOT_ADAMANTIUM;
    public static final Item FLINT_AND_STEEL;
    public static final Item ELYTRA_CORE;
    public static final Item MITHRIL_CORE;
    public static final Item ADAMANTIUM_CORE;
    public static final Item ARGENT_CORE;
    public static final Item SKELETON_CORE;
    public static final Item FS;
    public static final Item EX_COPPER;
    public static final Item EX_GOLD;
    public static final Item EX_DIAMOND;
    public static final Item EX_MITHRIL;
    public static final Item EX_ADAMAN;
    public static final Item REBORN_STONE;
    public static final Item JUHUAGAO;
    public static final Item ENCHANTED_GOLDEN_CARROT;
    public static final Item MIANTUAN;
    public static final Item QUQI_MIANTUAN;
    public static final Item SALA;
    public static final Item EX_PICKAXE;
    public static final Item WUJIN_SWORD;
    public static final Item MITHRIL_SWORD;
    public static final Item ADAMANTIUM_SWORD;
    public static final Item ANCIENT_SWORD;
    public static final Item WUJIN_HELMET;
    public static final Item WUJIN_CHESTPLATE;
    public static final Item WUJIN_LEGGINGS;
    public static final Item WUJIN_BOOTS;
    public static final Item WUJIN_PICKAXE;
    public static final Item WUJIN_AXE;
    public static final Item WUJIN_SHOVEL;
    public static final Item WUJIN_HOE;
    public static final Item WUJIN_COAL;
    public static final Item RAW_WUJIN;
    public static final Item MITHRIL_HELMET;
    public static final Item MITHRIL_CHESTPLATE;
    public static final Item MITHRIL_LEGGINGS;
    public static final Item MITHRIL_BOOTS;
    public static final Item ADAMANTIUM_HELMET;
    public static final Item ADAMANTIUM_CHESTPLATE;
    public static final Item ADAMANTIUM_LEGGINGS;
    public static final Item ADAMANTIUM_BOOTS;
    public static final ArmorMaterial COPPER_ARMOR;
    public static final Item copper_helmet;
    public static final Item copper_chestplate;
    public static final Item copper_leggings;
    public static final Item copper_boots;
    public static final Item DEEP_RAW_COPPER;
    public static final Item DEEP_RAW_IRON;
    public static final Item DEEP_RAW_GOLD;
    public static final Item copper_shears;
    public static final Item FLINT_SHARD;
    public static final Item FLINT_PICKAXE;
    public static final Item FLINT_AXE;
    public static final Item FLINT_SWORD;
    public static final Item FLINT_INGOT;
    public static final Item STRINGS;
    public static final Item nugget_mithril;
    public static final Item nugget_adamantium;
    public static final Item nugget_wujin;
    public static final Item nugget_diamond;
    public static final Item nugget_emerald;
    public static final Item lich_spawn;
    public static final Item void_blossom_spawn;
    public static final Item draugr_boss_spawn;
    public static final Item COPPER_NUGGET;
    public static final Item COPPER_SWORD;
    public static final Item COPPER_SHOVEL;
    public static final Item COPPER_PICKAXE;
    public static final Item COPPER_AXE;
    public static final Item COPPER_HOE;
    public static final Item COMPASS_ANCIENT_CITY;
    public static final Item COMPASS_MANSION;
    public static final Item DROSS_JERKY;
    public static final Item BONE_STICK;

    // 构造函数为空
    public ItemInit() {
    }

    /**
     * 注册所有物品的辅助方法
     *
     * @param name 物品的注册名 (路径)
     * @param item 要注册的物品实例
     * @return 注册后的物品实例
     */
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier("aliveandwell", name), item);
    }

    // init() 方法现在变得非常简洁，只负责调用注册辅助方法
    public static void init() {
        registerItem("xp_item", XP_ITEM);
        registerItem("xp_item1", XP_ITEN1);
        registerItem("flint_and_steel", FLINT_AND_STEEL);
        registerItem("lich_spawn", lich_spawn);
        registerItem("void_blossom_spawn", void_blossom_spawn);
        registerItem("draugr_boss_spawn", draugr_boss_spawn);
        registerItem("flint_shard", FLINT_SHARD);
        registerItem("flint_pickaxe", FLINT_PICKAXE);
        registerItem("flint_axe", FLINT_AXE);
        registerItem("flint_ingot", FLINT_INGOT);
        registerItem("flint_sword", FLINT_SWORD);
        registerItem("strings", STRINGS);
        registerItem("copper_nugget", COPPER_NUGGET);
        registerItem("copper_sword", COPPER_SWORD);
        registerItem("copper_shovel", COPPER_SHOVEL);
        registerItem("copper_pickaxe", COPPER_PICKAXE);
        registerItem("copper_axe", COPPER_AXE);
        registerItem("copper_hoe", COPPER_HOE);
        registerItem("copper_shears", copper_shears);
        registerItem("ex_copper", EX_COPPER);
        registerItem("ex_gold", EX_GOLD);
        registerItem("ex_diamond", EX_DIAMOND);
        registerItem("ex_mithril", EX_MITHRIL);
        registerItem("ex_adaman", EX_ADAMAN);
        registerItem("enchanted_golden_carrot", ENCHANTED_GOLDEN_CARROT);
        registerItem("mithril_core", MITHRIL_CORE);
        registerItem("adamantium_core", ADAMANTIUM_CORE);
        registerItem("argent_core", ARGENT_CORE);
        registerItem("skeleton_core", SKELETON_CORE);
        registerItem("water_bowl", WATER_BOWL);
        registerItem("miantuan", MIANTUAN);
        registerItem("quqi_miantuan", QUQI_MIANTUAN);
        registerItem("nugget_wujin", nugget_wujin);
        registerItem("nugget_mithril", nugget_mithril);
        registerItem("nugget_adamantium", nugget_adamantium);
        registerItem("nugget_diamond", nugget_diamond);
        registerItem("nugget_emerald", nugget_emerald);
        registerItem("item_en_gemstone", ITEM_EN_GENSTONE);
        registerItem("ingot_wujin", INGOT_WUJIN);
        registerItem("ingot_mithril", INGOT_MITHRIL);
        registerItem("row_mithril", ROW_MITHRIL);
        registerItem("row_adamantium", ROW_ADAMANTIUM);
        registerItem("ingot_adamantium", INGOT_ADAMANTIUM);
        registerItem("fortified_stone", FS);
        registerItem("reborn_stone", REBORN_STONE);
        registerItem("juhuagao", JUHUAGAO);
        registerItem("sala", SALA);
        registerItem("ex_pickaxe", EX_PICKAXE);
        registerItem("elytra_core", ELYTRA_CORE);
        registerItem("wujin_sword", WUJIN_SWORD);
        registerItem("mithril_sword", MITHRIL_SWORD);
        registerItem("adamantium_sword", ADAMANTIUM_SWORD);
        registerItem("ancient_sword", ANCIENT_SWORD);
        registerItem("copper_helmet", copper_helmet);
        registerItem("copper_chestplate", copper_chestplate);
        registerItem("copper_leggings", copper_leggings);
        registerItem("copper_boots", copper_boots);
        registerItem("wujin_helmet", WUJIN_HELMET);
        registerItem("wujin_chestplate", WUJIN_CHESTPLATE);
        registerItem("wujin_leggings", WUJIN_LEGGINGS);
        registerItem("wujin_boots", WUJIN_BOOTS);
        registerItem("wujin_pickaxe", WUJIN_PICKAXE);
        registerItem("wujin_axe", WUJIN_AXE);
        registerItem("wujin_shovel", WUJIN_SHOVEL);
        registerItem("wujin_hoe", WUJIN_HOE);
        registerItem("wujin_coal", WUJIN_COAL);
        registerItem("raw_wujin", RAW_WUJIN);
        registerItem("mithril_helmet", MITHRIL_HELMET);
        registerItem("mithril_chestplate", MITHRIL_CHESTPLATE);
        registerItem("mithril_leggings", MITHRIL_LEGGINGS);
        registerItem("mithril_boots", MITHRIL_BOOTS);
        registerItem("adamantium_helmet", ADAMANTIUM_HELMET);
        registerItem("adamantium_chestplate", ADAMANTIUM_CHESTPLATE);
        registerItem("adamantium_leggings", ADAMANTIUM_LEGGINGS);
        registerItem("adamantium_boots", ADAMANTIUM_BOOTS);
        registerItem("deep_raw_copper", DEEP_RAW_COPPER);
        registerItem("deep_raw_iron", DEEP_RAW_IRON);
        registerItem("deep_raw_gold", DEEP_RAW_GOLD);
        registerItem("compass_ancient_city", COMPASS_ANCIENT_CITY);
        registerItem("compass_mansion", COMPASS_MANSION);
        registerItem("dross_jerky", DROSS_JERKY);
        registerItem("bone_stick", BONE_STICK);
    }

    // --- 静态初始化块，实例化所有物品 ---
    static {
        // 使用 FabricItemSettings 代替 Item.Settings，这是 Fabric API 的推荐做法
        XP_ITEM = new XpItem(new FabricItemSettings().maxCount(16).rarity(Rarity.RARE));
        XP_ITEN1 = new XpItem1(new FabricItemSettings().maxCount(16).rarity(Rarity.RARE));
        WATER_BOWL = new WaterBowl(new FabricItemSettings().maxCount(16).recipeRemainder(Items.BOWL));
        ITEM_EN_GENSTONE = new Item(new FabricItemSettings().maxCount(16));
        INGOT_WUJIN = new Item(new FabricItemSettings().maxCount(16));
        INGOT_MITHRIL = new Item(new FabricItemSettings().maxCount(16));
        ROW_MITHRIL = new Item(new FabricItemSettings().maxCount(16));
        ROW_ADAMANTIUM = new Item(new FabricItemSettings().maxCount(16));
        INGOT_ADAMANTIUM = new Item(new FabricItemSettings().maxCount(16));
        FLINT_AND_STEEL = new Item(new FabricItemSettings().maxDamage(20));
        ELYTRA_CORE = new Item(new FabricItemSettings());
        MITHRIL_CORE = new Item(new FabricItemSettings());
        ADAMANTIUM_CORE = new Item(new FabricItemSettings());
        ARGENT_CORE = new Item(new FabricItemSettings());
        SKELETON_CORE = new Item(new FabricItemSettings());
        FS = new Item(new FabricItemSettings().maxCount(16));
        EX_COPPER = new ExItemCopper();
        EX_GOLD = new ExItemGold();
        EX_DIAMOND = new ExItemDiamond();
        EX_MITHRIL = new ExItemMithril();
        EX_ADAMAN = new ExItemAdaman();
        REBORN_STONE = new RebornStone(new FabricItemSettings().maxCount(16));
        JUHUAGAO = new JuHuaGao(new FabricItemSettings().maxCount(16).food(new FoodComponent.Builder().hunger(1).saturationModifier(0.3F).build()));
        ENCHANTED_GOLDEN_CARROT = new EnchantedGoldenCarrotItem(new FabricItemSettings().maxCount(16).food(
                new FoodComponent.Builder()
                        .hunger(2)
                        .saturationModifier(1.2F)
                        .alwaysEdible()
                        .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 0), 1.0F)
                        .statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 4800, 3), 1.0F)
                        .build()
        ));
        MIANTUAN = new JuHuaGao(new FabricItemSettings().maxCount(16).food(new FoodComponent.Builder().hunger(1).saturationModifier(0.3F).build()));
        QUQI_MIANTUAN = new JuHuaGao(new FabricItemSettings().maxCount(16).food(new FoodComponent.Builder().hunger(1).saturationModifier(0.3F).build()));
        SALA = new SalaItem(new FabricItemSettings().maxCount(16).food(new FoodComponent.Builder().hunger(2).saturationModifier(0.5F).build()));
        EX_PICKAXE = new ExPickaxe(new FabricItemSettings());
        WUJIN_SWORD = new SwordItem(AliveToolMaterial.WUJIN, 4, -2.0F, new FabricItemSettings());
        MITHRIL_SWORD = new SwordItem(AliveToolMaterial.MITHRIL, 8, -2.0F, new FabricItemSettings());
        ADAMANTIUM_SWORD = new SwordItem(AliveToolMaterial.ADAMANTIUM, 17, -2.0F, new FabricItemSettings());
        ANCIENT_SWORD = new SwordItem(AliveToolMaterial.ANCIENT, 34, -2.0F, new FabricItemSettings());
        WUJIN_HELMET = new ArmorItem(AliveArmorMaterial.WUJIN, ArmorItem.Type.HELMET, new FabricItemSettings());
        WUJIN_CHESTPLATE = new ArmorItem(AliveArmorMaterial.WUJIN, ArmorItem.Type.CHESTPLATE, new FabricItemSettings());
        WUJIN_LEGGINGS = new ArmorItem(AliveArmorMaterial.WUJIN, ArmorItem.Type.LEGGINGS, new FabricItemSettings());
        WUJIN_BOOTS = new ArmorItem(AliveArmorMaterial.WUJIN, ArmorItem.Type.BOOTS, new FabricItemSettings());
        WUJIN_PICKAXE = new PickaxeItem(AliveToolMaterial.WUJIN, 1, -2.8F, new FabricItemSettings());
        WUJIN_AXE = new AxeItem(AliveToolMaterial.WUJIN, 5.0F, -3.0F, new FabricItemSettings());
        WUJIN_SHOVEL = new ShovelItem(AliveToolMaterial.WUJIN, 1.5F, -3.0F, new FabricItemSettings());
        WUJIN_HOE = new HoeBase(AliveToolMaterial.WUJIN, new FabricItemSettings());
        WUJIN_COAL = new Item(new FabricItemSettings().maxCount(16));
        RAW_WUJIN = new Item(new FabricItemSettings().maxCount(16));
        MITHRIL_HELMET = new ArmorItem(AliveArmorMaterial.MITHRIL, ArmorItem.Type.HELMET, new FabricItemSettings());
        MITHRIL_CHESTPLATE = new ArmorItem(AliveArmorMaterial.MITHRIL, ArmorItem.Type.CHESTPLATE, new FabricItemSettings());
        MITHRIL_LEGGINGS = new ArmorItem(AliveArmorMaterial.MITHRIL, ArmorItem.Type.LEGGINGS, new FabricItemSettings());
        MITHRIL_BOOTS = new ArmorItem(AliveArmorMaterial.MITHRIL, ArmorItem.Type.BOOTS, new FabricItemSettings());
        ADAMANTIUM_HELMET = new ArmorItem(AliveArmorMaterial.ADAMANTIUM, ArmorItem.Type.HELMET, new FabricItemSettings());
        ADAMANTIUM_CHESTPLATE = new ArmorItem(AliveArmorMaterial.ADAMANTIUM, ArmorItem.Type.CHESTPLATE, new FabricItemSettings());
        ADAMANTIUM_LEGGINGS = new ArmorItem(AliveArmorMaterial.ADAMANTIUM, ArmorItem.Type.LEGGINGS, new FabricItemSettings());
        ADAMANTIUM_BOOTS = new ArmorItem(AliveArmorMaterial.ADAMANTIUM, ArmorItem.Type.BOOTS, new FabricItemSettings());
        COPPER_ARMOR = new CopperArmorBase();
        copper_helmet = new ArmorItem(COPPER_ARMOR, ArmorItem.Type.HELMET, new FabricItemSettings());
        copper_chestplate = new ArmorItem(COPPER_ARMOR, ArmorItem.Type.CHESTPLATE, new FabricItemSettings());
        copper_leggings = new ArmorItem(COPPER_ARMOR, ArmorItem.Type.LEGGINGS, new FabricItemSettings());
        copper_boots = new ArmorItem(COPPER_ARMOR, ArmorItem.Type.BOOTS, new FabricItemSettings());
        DEEP_RAW_COPPER = new Item(new FabricItemSettings().maxCount(16));
        DEEP_RAW_IRON = new Item(new FabricItemSettings().maxCount(16));
        DEEP_RAW_GOLD = new Item(new FabricItemSettings().maxCount(16));
        copper_shears = new ShearsItem(new FabricItemSettings().maxDamage(64));
        FLINT_SHARD = new Item(new FabricItemSettings());
        FLINT_PICKAXE = new PickaxeItem(new FlintToolBase(), 0, -1.8F, new FabricItemSettings());
        FLINT_AXE = new AxeItem(new FlintToolBase(), 1.0F, -2.8F, new FabricItemSettings());
        FLINT_SWORD = new ShovelItem(new FlintToolBase(), 2.5F, -3.0F, new FabricItemSettings().maxDamage(128));
        FLINT_INGOT = new Item(new FabricItemSettings());
        STRINGS = new Item(new FabricItemSettings());
        nugget_mithril = new Item(new FabricItemSettings().maxCount(16));
        nugget_adamantium = new Item(new FabricItemSettings().maxCount(16));
        nugget_wujin = new Item(new FabricItemSettings());
        nugget_diamond = new Item(new FabricItemSettings().maxCount(16));
        nugget_emerald = new Item(new FabricItemSettings().maxCount(16));
        lich_spawn = new Item(new FabricItemSettings());
        void_blossom_spawn = new Item(new FabricItemSettings());
        draugr_boss_spawn = new Item(new FabricItemSettings());
        COPPER_NUGGET = new Item(new FabricItemSettings());
        COPPER_SWORD = new SwordItem(new CopperToolBase(), 3, -2.4F, new FabricItemSettings());
        COPPER_SHOVEL = new ShovelItem(new CopperToolBase(), 1.5F, -3.0F, new FabricItemSettings());
        COPPER_PICKAXE = new PickaxeItem(new CopperToolBase(), 1, -2.8F, new FabricItemSettings());
        COPPER_AXE = new AxeItem(new CopperToolBase(), 7.0F, -3.2F, new FabricItemSettings());
        COPPER_HOE = new HoeBase(AliveToolMaterial.COPPER, new FabricItemSettings());
        COMPASS_ANCIENT_CITY = new CompassAncientCity(new FabricItemSettings().rarity(Rarity.UNCOMMON));
        COMPASS_MANSION = new CompassMansion(new FabricItemSettings().rarity(Rarity.UNCOMMON));
        DROSS_JERKY = new Item(new FabricItemSettings().maxCount(16).food(new FoodComponent.Builder().hunger(1).saturationModifier(0.3F).build()));
        BONE_STICK = new Item(new FabricItemSettings().maxCount(16));
    }
}
