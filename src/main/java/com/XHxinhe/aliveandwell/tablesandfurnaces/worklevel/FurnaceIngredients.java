package com.XHxinhe.aliveandwell.tablesandfurnaces.worklevel;

import java.util.HashMap;

public class FurnaceIngredients {
    public static HashMap<String, FurnaceIngredient> modFuel_ingredients = new HashMap();
    public static HashMap<String, FurnaceIngredient> modItem_ingredients = new HashMap();

    public FurnaceIngredients() {
    }

    public static void initFuel() {
        register("minecraft:coal", 1);
        register("minecraft:lava_bucket", 2);
        register("minecraft:blaze_rod", 3);
    }

    public static void initItem() {
        register1("minecraft:copper_ore", 1);
        register1("minecraft:iron_ore", 1);
        register1("minecraft:gold_ore", 1);
        register1("minecraft:raw_copper", 1);
        register1("minecraft:raw_iron", 1);
        register1("minecraft:raw_gold", 1);
        register1("minecraft:deepslate_iron_ore", 2);
        register1("minecraft:deepslate_copper_ore", 2);
        register1("minecraft:deepslate_gold_ore", 2);
        register1("minecraft:nether_gold_ore", 2);
        register1("minecraft:ancient_debris", 3);
    }

    public static void register(String name, int workbench) {
        modFuel_ingredients.put(name, new FurnaceIngredient(workbench));
    }

    public static void register1(String name, int workbench) {
        modItem_ingredients.put(name, new FurnaceIngredient(workbench));
    }
}
