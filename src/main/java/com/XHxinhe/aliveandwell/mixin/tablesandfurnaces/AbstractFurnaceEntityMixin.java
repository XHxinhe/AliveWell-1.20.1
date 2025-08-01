package com.XHxinhe.aliveandwell.mixin.tablesandfurnaces;

import com.XHxinhe.aliveandwell.registry.BlockInit;
import com.XHxinhe.aliveandwell.tablesandfurnaces.worklevel.FurnaceIngredient;
import com.XHxinhe.aliveandwell.tablesandfurnaces.worklevel.FurnaceIngredients;
import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.Map;

import net.minecraft.SharedConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Util;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 这是一个用于 AbstractFurnaceBlockEntity (所有熔炉的基类) 的 Mixin。
 * 它引入了一套复杂的“工作等级”系统，限制了不同等级的熔炉可以烧炼的物品和使用的燃料，
 * 并根据熔炉类型和物品动态调整烧炼速度。
 */
@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceEntityMixin extends BlockEntity {

    @Unique
    int level0 = 0;//物品
    @Unique
    int level = 0;//燃料
    @Shadow
    int burnTime;

    @Shadow
    protected abstract boolean isBurning();

    @Shadow
    protected DefaultedList<ItemStack> inventory;

    private AbstractFurnaceEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(
            at = {@At("RETURN")},
            method = {"<init>"}
    )
    public void Constructor(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, RecipeType<? extends AbstractCookingRecipe> recipeType, CallbackInfo info) {

    }
    /**
     * 注入点一：在 getFuelTime 方法的开头。
     * 作用：这是整个系统的核心。它计算燃料和待烧炼物品的等级，
     * 然后根据熔炉类型判断是否允许本次烧炼。如果不允许，则返回0燃料时间。
     */
    @Inject(at = @At("HEAD"), method = "getFuelTime", cancellable = true)
    public void getFuelTime(ItemStack fuel, CallbackInfoReturnable<Integer> ca) {
        //此处world必须判断是否为null，否则熔炉数据无法保存。
        if (this.getWorld() != null) {
            Block block = this.world.getBlockState(this.pos).getBlock();

            //物品
            ItemStack itemStack = (ItemStack) this.inventory.get(0);
            Item item0 = itemStack.getItem();
            String name0 = Registries.ITEM.getId(item0).toString();
            //燃料
            Item item = fuel.getItem();
            String name = Registries.ITEM.getId(item).toString();
            level0 = 0;//物品
            level = 0;//燃料

            if(FurnaceIngredients.modItem_ingredients.keySet().contains(name0)){
                this.level0 = ((FurnaceIngredient)FurnaceIngredients.modItem_ingredients.get(name0)).workLevel;
            }else {
                //不是原版的矿石为一级，可能不准确
                if (name0.contains("ore") && !name0.contains("deepslate") && !name0.contains("minecraft")) {
                    this.level0 = 1;
                }
                //不是原版的矿石为一级，可能不准确
                if (name0.contains("aliveandwell") && name0.contains("deep_raw") && !name0.contains("minecraft:")) {
                    this.level0 = 2;
                }

                //不是原版的矿石为一级，可能不准确
                if (name0.contains("raw_copper") && name0.contains("minecraft:")) {
                    this.level0 = 1;
                }
                if (name0.contains("raw_iron") && name0.contains("minecraft:")) {
                    this.level0 = 1;
                }
                if (name0.contains("raw_gold") && name0.contains("minecraft:")) {
                    this.level0 = 1;
                }

                //铜块和铁块
                if (name0.contains("iron_block") || name0.contains("copper_block")) {
                    this.level0 = 2;
                }

                //不是原版的矿石深层矿石为二级，可能不准确
                if (name0.contains("deepslate") && name0.contains("ore") && !name0.contains("minecraft")) {
                    this.level0 = 2;
                }
                //戈伯矿
                if (name0.equals("gobber2:gobber2_ore") ) {
                    this.level0 = 2;
                }
                if (name0.equals("minecraft:iron_ingot") ) {
                    this.level0 = 2;
                }
                //戈伯矿
                if (name0.equals("gobber2:gobber2_ore_nether") ) {
                    this.level0 = 3;
                }

                //秘银矿
                if (name0.contains("_mithril") && name0.contains("aliveandwell:")) {
                    this.level0 = 2;
                }
                //艾德曼矿
                if (name0.contains("_adamantium") && name0.contains("aliveandwell:")) {
                    this.level0 = 3;
                }


            }


            if(FurnaceIngredients.modFuel_ingredients.keySet().contains(name)){
                this.level = ((FurnaceIngredient)FurnaceIngredients.modFuel_ingredients.get(name)).workLevel;
            }else if(name.contains("coal") && !name.contains("charcoal")){
                this.level = 1;
            }else {
                this.level = 0;
            }


            //设置燃料时间
            if (name.contains("coal") && !name.contains("charcoal")) {
                ca.setReturnValue(801);
            }else if(!(item==Items.LAVA_BUCKET || item==Items.BLAZE_ROD) && !name.contains("minecraft")){
                ca.setReturnValue(150);//其他一切不是原版的燃料为150.
            }

            //燃料等级大于该熔炉等级不可燃烧:原版熔炉和高炉为一级熔炉
            if (block == BlockInit.CLAY_FURNACE) {
                //燃料等级不够，或者燃料等级小于物品等级
                if (level > 0 || level0 > level) {
                    if (this.isBurning()) {
                        this.burnTime = 0;
                    }
                    ca.setReturnValue(0);
                }
            }

            if (block == Blocks.FURNACE || block==Blocks.BLAST_FURNACE) {
                //燃料等级不够，或者燃料等级小于物品等级
                if (level > 1 || level0 > level) {
                    if (this.isBurning()) {
                        this.burnTime = 0;
                    }
                    ca.setReturnValue(0);
                }
            }
            //燃料等级《小于该熔炉等级》或《大于该熔炉等级》不可燃烧
            if (block == BlockInit.OBSIDIAN_FURNACE) {
                if (level > 2 || level0 > level) {
                    if (this.isBurning()) {
                        this.burnTime = 0;
                    }
                    ca.setReturnValue(0);
                }
            }
            //燃料等级小于该熔炉等级不可燃烧
            if (block == BlockInit.NETHERRACK_FURNACE) {
                if (level0 > level) {
                    if (this.isBurning()) {
                        this.burnTime = 0;
                    }
                    ca.setReturnValue(0);
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getCookTime", cancellable = true)
    private static void getCookTime(World world, AbstractFurnaceBlockEntity furnace, CallbackInfoReturnable<Integer> ca)  {
        Item item;
        if (world != null) {
            Block block = world.getBlockState(furnace.getPos()).getBlock();
            AbstractFurnaceEntityMixin mixin = (AbstractFurnaceEntityMixin) (Object) furnace;
            //物品
            ItemStack itemStack0 = (ItemStack) mixin.inventory.get(0);
            Item item0 = itemStack0.getItem();
            String name0 = Registries.ITEM.getId(item0).toString();

            //燃料
            ItemStack itemStack = mixin.inventory.get(1);
            item = itemStack.getItem();
            String name = Registries.ITEM.getId(item).toString();

            int time =160;
            if(item0.isFood()){
                time = 99;
            }
            //不是原版的矿石为一级，可能不准确
            if (name0.contains("_ore") && !name0.contains("deepslate")) {
                time=200;//煤801
            }
            //深层矿
            if (name0.contains("aliveandwell") && name0.contains("deep_raw") && !name0.contains("minecraft:")) {
                time=802; //煤801
            }

            if (name0.contains("iron_block") || name0.contains("copper_block")) {
                time=802; //煤801
            }

            //所有深层矿石
            if (name0.contains("deepslate") && name0.contains("_ore")) {
                time=802; //煤801
            }
            if (name0.equals("minecraft:iron_ingot")) {
                time=802; //煤801
            }
            //远古残骸
            if (name0.contains("minecraft:ancient_debris")) {
                time=8010; //烈焰棒10000
            }
            //戈伯矿
            if (name0.contains("gobber2_ore") && name0.contains("gobber2:")) {
                time=8022;//岩浆桶1800
            }
            //秘银矿
            if (name0.contains("_mithril") && name0.contains("aliveandwell:")) {
                time=802;//煤801
            }
            //艾德曼矿
            if (name0.contains("_adamantium") && name0.contains("aliveandwell:")) {
                time=8022;//岩浆桶1800
            }

            //燃料等级大于该熔炉等级不可燃烧
            if (block == Blocks.FURNACE || block == BlockInit.CLAY_FURNACE) {
                ca.setReturnValue(time);
            }
            //燃料等级《小于该熔炉等级》或《大于该熔炉等级》不可燃烧
            if (block == BlockInit.OBSIDIAN_FURNACE) {
                ca.setReturnValue(time/5);
            }
            //燃料等级小于该熔炉等级不可燃烧
            if (block == BlockInit.NETHERRACK_FURNACE) {
                ca.setReturnValue(time/10);
            }

//            //工业电炉无法燃烧戈伯矿
//            if(Registry.BLOCK.getId(block).toString().equals("modern_industrialization:bronze_furnace")
//                    || Registry.BLOCK.getId(block).toString().equals("modern_industrialization:steel_furnace")
//                    || Registry.BLOCK.getId(block).toString().equals("modern_industrialization:electric_furnace")){
//                if (name.equals("gobber2:gobber2_ore") || name.equals("gobber2:gobber2_ore_nether") || name.equals("minecraft:ancient_debris")) {
//                    ca.setReturnValue(0);
//                }
//            }
        }
    }

    /**
     * @author hctian
     * @reason Custom fuel times
     * 使用 @Overwrite 完全重写原版的燃料表。
     * 这会定义游戏中所有物品作为燃料的燃烧时间。
     * 注意：这种方式兼容性较差，可能与其他修改燃料的Mod冲突。
     */
    @Overwrite
    public static Map<Item, Integer> createFuelTimeMap() {
        Map<Item, Integer> map = Maps.newLinkedHashMap();
        // 注意：这里的燃烧时间值被大幅修改了
        addFuel(map, Items.LAVA_BUCKET, 8021);
        addFuel(map, Items.COAL_BLOCK, 80221); // 这个值非常大，可能是笔误
        addFuel(map, Items.BLAZE_ROD, 801);
        addFuel(map, Items.COAL, 199);
        addFuel(map, Items.CHARCOAL, 198);
        addFuel(map, ItemTags.PLANKS, 150);
        addFuel(map, ItemTags.WOODEN_STAIRS, 150);
        addFuel(map, ItemTags.WOODEN_SLABS, 150);
        addFuel(map, ItemTags.WOODEN_FENCES, 100);
        addFuel(map, ItemTags.WOODEN_PRESSURE_PLATES, 150);
        addFuel(map, ItemTags.WOODEN_BUTTONS, 150);
        addFuel(map, Blocks.CRAFTING_TABLE, 150);
        addFuel(map, Blocks.BOOKSHELF, 150);
        addFuel(map, Blocks.LECTERN, 150);
        addFuel(map, Blocks.JUKEBOX, 150);
        addFuel(map, Blocks.CHEST, 150);
        addFuel(map, Blocks.TRAPPED_CHEST, 150);
        addFuel(map, Blocks.NOTE_BLOCK, 150);
        addFuel(map, Blocks.DAYLIGHT_DETECTOR, 150);
        addFuel(map, ItemTags.BANNERS, 150);
        addFuel(map, Items.BOW, 150);
        addFuel(map, Items.FISHING_ROD, 150);
        addFuel(map, Blocks.LADDER, 150);
        addFuel(map, ItemTags.SIGNS, 80);
        addFuel(map, Items.WOODEN_SHOVEL, 80);
        addFuel(map, Items.WOODEN_SWORD, 80);
        addFuel(map, Items.WOODEN_HOE, 80);
        addFuel(map, Items.WOODEN_AXE, 80);
        addFuel(map, Items.WOODEN_PICKAXE, 80);
        addFuel(map, ItemTags.SAPLINGS, 80);
        addFuel(map, ItemTags.WOOL_CARPETS, 180);
        addFuel(map, ItemTags.WOOL, 150);
        addFuel(map, Items.SCAFFOLDING, 150);
        addFuel(map, ItemTags.BOATS, 150);
        addFuel(map, Blocks.DRIED_KELP_BLOCK, 1001);
        addFuel(map, Items.CROSSBOW, 150);
        addFuel(map, Blocks.BAMBOO, 50);
        addFuel(map, Blocks.DEAD_BUSH, 150);
        addFuel(map, Blocks.POTTED_BAMBOO, 50);
        addFuel(map, Blocks.COMPOSTER, 150);
        addFuel(map, Blocks.AZALEA, 150);
        addFuel(map, Blocks.FLOWERING_AZALEA, 150);
        addFuel(map, Blocks.MANGROVE_ROOTS, 150);
        addFuel(map, Blocks.MANGROVE_PROPAGULE, 150);
        return map;
    }

    @Shadow
    private static boolean isNonFlammableWood(Item item) {
        return item.getRegistryEntry().isIn(ItemTags.NON_FLAMMABLE_WOOD);
    }

    @Shadow
    private static void addFuel(Map<Item, Integer> fuelTimes, TagKey<Item> tag, int fuelTime) {
        Iterator var3 = Registries.ITEM.iterateEntries(tag).iterator();

        while (var3.hasNext()) {
            RegistryEntry<Item> registryEntry = (RegistryEntry) var3.next();
            if (!isNonFlammableWood((Item) registryEntry.value())) {
                fuelTimes.put((Item) registryEntry.value(), fuelTime);
            }
        }

    }

    @Shadow
    private static void addFuel(Map<Item, Integer> fuelTimes, ItemConvertible item, int fuelTime) {
        Item item2 = item.asItem();
        if (isNonFlammableWood(item2)) {
            if (SharedConstants.isDevelopment) {
                throw (IllegalStateException) Util.throwOrPause(new IllegalStateException("A developer tried to explicitly make fire resistant item " + item2.getName((ItemStack) null).getString() + " a furnace fuel. That will not work!"));
            }
        } else {
            fuelTimes.put(item2, fuelTime);
        }
    }
}

