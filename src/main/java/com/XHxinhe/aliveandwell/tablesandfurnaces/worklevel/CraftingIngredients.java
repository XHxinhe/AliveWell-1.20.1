package com.XHxinhe.aliveandwell.tablesandfurnaces.worklevel;

import java.util.HashMap;

public class CraftingIngredients {
    public static HashMap<String, CraftingIngredient> mod_ingredients = new HashMap();

    public CraftingIngredients() {
    }

    public static void init() {
        register("aliveandwell:copper_crafting_table", 1);
        register("aliveandwell:strings", 1);
        register("aliveandwell:flint_ingot", 1);
        register("minecraft:leather", 1);
        register("minecraft:stick", 1);
        register("aliveandwell:iron_crafting_table", 2);
        register("aliveandwell:diamond_crafting_table", 3);
        register("aliveandwell:netherite_crafting_table", 4);
    }

    public static void register(String name, int workbench) {
        mod_ingredients.put(name, new CraftingIngredient(workbench));
    }
}
