package com.XHxinhe.aliveandwell.mixin.aliveandwell.item;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.events.PlayerEvents;
import com.XHxinhe.aliveandwell.registry.ItemInit;
import com.XHxinhe.aliveandwell.registry.events.EatOreAddExperience;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.block.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Item.class)
public abstract class ItemMixin implements ItemConvertible, FabricItem{

//    @Unique
//    private static final FoodComponent FOOD_WHEAT_SEEDS = new FoodComponent.Builder().hunger(0).saturationModifier(0.6f).build();
    @Unique
    private static final FoodComponent FOOD_PUMPKIN_SEEDS = new FoodComponent.Builder().hunger(1).saturationModifier(1.0f).build();
    @Unique
    private static final FoodComponent MELON_SEEDS = new FoodComponent.Builder().hunger(1).saturationModifier(1.0f).build();
    @Unique
    private static final FoodComponent NETHER_WART = new FoodComponent.Builder().hunger(1).saturationModifier(1.0f).build();
    @Unique
    private static final FoodComponent BEETROOT_SEEDS = new FoodComponent.Builder().hunger(1).saturationModifier(1.0f).build();
    @Unique
    private static final FoodComponent FOOD_RED_MUSHROOM = new FoodComponent.Builder().hunger(1).saturationModifier(1.0f).statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 10 * 20), 1).statusEffect(new StatusEffectInstance(StatusEffects.POISON, 30 * 20), 1).build();
    @Unique
    private static final FoodComponent FOOD_BROWN_MUSHROOM = new FoodComponent.Builder().hunger(1).saturationModifier(1.0f).build();
    @Unique
    private static final FoodComponent FOOD_EGG = new FoodComponent.Builder().hunger(2).saturationModifier(1.0f).meat().build();
    @Unique
    private static final FoodComponent FOOD_HONEYCOMB = new FoodComponent.Builder().hunger(3).saturationModifier(1.0f).build();
    @Unique
    private static final FoodComponent FOOD_SUGAR = new FoodComponent.Builder().hunger(1).saturationModifier(1.0f).build();
    @Unique
    private static final FoodComponent FOOD_ROTTEN_FLESH = new FoodComponent.Builder().hunger(4).saturationModifier(2.0f).statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 15), 1).statusEffect(new StatusEffectInstance(StatusEffects.POISON, 7 * 20), 1).statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 30 * 20), 1).build();

    @Unique
    private static final FoodComponent APPLE = new FoodComponent.Builder().hunger(2).saturationModifier(0.3f).build();
    @Unique
    private static final FoodComponent BAKED_POTATO = new FoodComponent.Builder().hunger(2).saturationModifier(0.6f).build();
    @Unique
    private static final FoodComponent BEEF = new FoodComponent.Builder().hunger(3).saturationModifier(0.3f).meat().build();
    @Unique
    private static final FoodComponent BEETROOT = new FoodComponent.Builder().hunger(1).saturationModifier(0.6f).build();
    @Unique
    private static final FoodComponent BREAD = new FoodComponent.Builder().hunger(4).saturationModifier(0.6f).build();
    @Unique
    private static final FoodComponent COOKIE = new FoodComponent.Builder().hunger(3).saturationModifier(0.6f).build();
    @Unique
    private static final FoodComponent CARROT = new FoodComponent.Builder().hunger(1).saturationModifier(0.6f).build();

    //GOLDEN_APPLE = new FoodComponent.Builder().hunger(4).saturationModifier(1.2f)
    // .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 1), 1.0f)//生命回复2
    // .statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 0), 1.0f).alwaysEdible().build();//吸收伤害

    //ENCHANTED_GOLDEN_APPLE = new FoodComponent.Builder().hunger(4).saturationModifier(1.2f)
    // .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400, 1), 1.0f)//生命回复3
    // .statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 6000, 0), 1.0f)//抗性提升3
    // .statusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 6000, 0), 1.0f)//火焰免疫
    // .statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 3), 1.0f).alwaysEdible().build();//吸收伤害4

    @Unique
    private static final FoodComponent ENCHANTED_GOLDEN_APPLE = new FoodComponent.Builder().hunger(2)
            .saturationModifier(1.2f).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 800, 2), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 6000, 1), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 6000, 0), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 3), 1.0f).alwaysEdible().build();
    @Unique
    private static final FoodComponent GOLDEN_APPLE = new FoodComponent.Builder().hunger(2).saturationModifier(1.2f)
            .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 600, 1), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 0), 1.0f).alwaysEdible().build();
    @Unique
    private static final FoodComponent GOLDEN_CARROT = new FoodComponent.Builder().hunger(2).saturationModifier(1.2f).alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 1), 1.0f).build();
    @Unique
    private static final FoodComponent PUMPKIN_PIE = new FoodComponent.Builder().hunger(3).saturationModifier(0.3f).build();//========
    @Unique
    private static final FoodComponent EGG_PIE = new FoodComponent.Builder().hunger(2).saturationModifier(0.3f).build();//========
    @Unique
    private static final FoodComponent EGG_PIE1 = new FoodComponent.Builder().hunger(2).saturationModifier(0.3f).build();//========
    @Unique
    private static final FoodComponent EGG_PIE2 = new FoodComponent.Builder().hunger(2).saturationModifier(0.3f).build();//========
    @Unique
    private static final FoodComponent EGG_PIE3 = new FoodComponent.Builder().hunger(2).saturationModifier(0.3f).build();//========
    @Unique
    private static final FoodComponent GREEN_APPLE = new FoodComponent.Builder().hunger(1).saturationModifier(0.3f).statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 10 * 20), 1).build();//========
    @Unique
    private static final FoodComponent GREEN_APPLE1 = new FoodComponent.Builder().hunger(2).saturationModifier(0.3f).build();//========
    @Unique
    private static final FoodComponent GREEN_APPLE2 = new FoodComponent.Builder().hunger(1).saturationModifier(0.3f).build();//========
    @Unique
    private static final FoodComponent GREEN_APPLE3 = new FoodComponent.Builder().hunger(3).saturationModifier(0.3f).build();//========
    @Unique
    private static final FoodComponent GREEN_APPLE4 = new FoodComponent.Builder().hunger(2).saturationModifier(0.3f).build();//========
    @Unique
    private static final FoodComponent RABBIT_STEW = createStew(8).build();
    @Unique
    private static final FoodComponent MUSHROOM_STEW = createStew(2).build();
    @Unique
    private static final FoodComponent HONEY_BOTTLE = new FoodComponent.Builder().hunger(3).saturationModifier(0.1f).build();
    @Unique
    private static final FoodComponent COOKED_BEEF = new FoodComponent.Builder().hunger(10).saturationModifier(0.8f).meat().build();

    @Unique
    private static final FoodComponent DRIED_KELP = new FoodComponent.Builder().hunger(0).saturationModifier(0.3f).snack().build();

    @Unique
    private static final FoodComponent FOOD_COCOA_BEANS = new FoodComponent.Builder().hunger(1).saturationModifier(0.3f).build();;

    @Inject(at=@At("HEAD"), method="getMaxUseTime", cancellable = true)
    public void getMaxUseTime(ItemStack stack, CallbackInfoReturnable<Integer> ca) {
        if (stack.getItem() == Items.ENCHANTED_GOLDEN_APPLE || stack.getItem() == Items.GOLDEN_APPLE || stack.getItem() == Items.GOLDEN_CARROT) {
            ca.setReturnValue(16);
        }
    }
    @Shadow
    public FoodComponent getFoodComponent() {
        return null;
    }

    @Inject(at=@At("RETURN"), method="getFoodComponent", cancellable = true)
    public void getFoodComponent(CallbackInfoReturnable<FoodComponent> info) {
        Item item = ((Item)(Object)this);
        String name = Registries.ITEM.getId(item).toString();
//        if (item == Items.WHEAT_SEEDS) {
//            info.setReturnValue(FOOD_WHEAT_SEEDS);
//            return;
//        } else
        if (item == Items.PUMPKIN_SEEDS) {
            info.setReturnValue(FOOD_PUMPKIN_SEEDS);
            return;
        } else if (item == Items.RED_MUSHROOM) {
            info.setReturnValue(FOOD_RED_MUSHROOM);
            return;
        } else if (item == Items.BROWN_MUSHROOM) {
            info.setReturnValue(FOOD_BROWN_MUSHROOM);
            return;
        } else if (item == Items.EGG) {
            info.setReturnValue(FOOD_EGG);
            return;
        } else if (item == Items.HONEYCOMB) {
            info.setReturnValue(FOOD_HONEYCOMB);
            return;
        } else if (item == Items.SUGAR) {
            info.setReturnValue(FOOD_SUGAR);
            return;
        }else if (item == Items.BEETROOT_SEEDS) {
            info.setReturnValue(BEETROOT_SEEDS);
            return;
        } else if (item == Items.NETHER_WART) {
            info.setReturnValue(NETHER_WART);
            return;
        } else if (item == Items.ROTTEN_FLESH) {
            info.setReturnValue(FOOD_ROTTEN_FLESH);
            return;
        } else if (item == Items.CARROT) {//=============
            info.setReturnValue(CARROT);
            return;
        }else if (item == Items.APPLE) {
            info.setReturnValue(APPLE);
            return;
        }else if (item == Items.BAKED_POTATO) {
            info.setReturnValue(BAKED_POTATO);
            return;
        }else if (item == Items.BEEF) {
            info.setReturnValue(BEEF);
            return;
        }else if (item == Items.BEETROOT) {
            info.setReturnValue(BEETROOT);
            return;
        }else if (item == Items.BREAD) {
            info.setReturnValue(BREAD);
            return;
        }else if (item == Items.ENCHANTED_GOLDEN_APPLE) {
            info.setReturnValue(ENCHANTED_GOLDEN_APPLE);
            return;
        }else if (item == Items.GOLDEN_APPLE) {
            info.setReturnValue(GOLDEN_APPLE);
            return;
        }else if (item == Items.GOLDEN_CARROT) {
            info.setReturnValue(GOLDEN_CARROT);
            return;
        }else if (item == Items.PUMPKIN_PIE) {
            info.setReturnValue(PUMPKIN_PIE);
            return;
        }else if (item == Items.RABBIT_STEW) {
            info.setReturnValue(RABBIT_STEW);
            return;
        }else if (item == Items.MUSHROOM_STEW) {
            info.setReturnValue(MUSHROOM_STEW);
            return;
        }else if (item == Items.HONEY_BOTTLE) {
            info.setReturnValue(HONEY_BOTTLE);
            return;
        }else if (item == Items.COOKED_BEEF) {
            info.setReturnValue(COOKED_BEEF);
            return;
        } else if (item == Items.MELON_SEEDS) {
            info.setReturnValue(MELON_SEEDS);
            return;
        } else if (item == Items.COOKIE) {
            info.setReturnValue(COOKIE);
            return;
        }else if (name.equals("byg:nightshade_berry_pie")) {
            info.setReturnValue(EGG_PIE);
            return;
        }else if (name.equals("byg:green_apple_pie")) {
            info.setReturnValue(EGG_PIE1);
            return;
        }else if (name.equals("byg:crimson_berry_pie")) {
            info.setReturnValue(EGG_PIE2);
            return;
        }else if (name.equals("byg:blueberry_pie")) {
            info.setReturnValue(EGG_PIE3);
            return;
        }else if (name.equals("byg:green_apple")) {
            info.setReturnValue(GREEN_APPLE);
            return;
        }else if (name.equals("byg:cooked_joshua_fruit")) {
            info.setReturnValue(GREEN_APPLE1);
            return;
        }else if (name.equals("byg:cooked_white_puffball_cap")) {//2
            info.setReturnValue(GREEN_APPLE2);
            return;
        }else if (name.equals("byg:white_puffball_stew")) {//4
            info.setReturnValue(GREEN_APPLE3);
            return;
        }else if (name.equals("byg:aloe_vera_juice")) {//2
            info.setReturnValue(GREEN_APPLE4);
            return;
        }else if (item == Items.DRIED_KELP) {//2
            info.setReturnValue(DRIED_KELP);
            return;
        }else if (item == Items.GLOW_BERRIES) {//2
            info.setReturnValue(null);
            return;
        }else if (item == Items.COCOA_BEANS) {
            info.setReturnValue(FOOD_COCOA_BEANS);
            return;
        }
    }

    @Inject(at = @At("RETURN"), method="isFood", cancellable = true)
    public void isFood(CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(getFoodComponent() != null);
    }
    private static FoodComponent.Builder createStew(int hunger) {
        return new FoodComponent.Builder().hunger(hunger).saturationModifier(0.6f);
    }

    @Inject(at=@At("HEAD"), method="appendTooltip")
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ca) {
        if(stack.getItem() == Items.DIAMOND){
            tooltip.add(Text.translatable("item.aliveandwell.diamond.tooltip").append(Text.translatable(String.valueOf(EatOreAddExperience.DIAMOND_XP))).formatted(Formatting.LIGHT_PURPLE));
            tooltip.add(Text.translatable("item.aliveandwell.diamond.tooltip1").formatted(Formatting.YELLOW));
        }
//        if(stack.getItem() == Items.EMERALD){
//            tooltip.add(Text.translatable("item.aliveandwell.emerald.tooltip").append(Text.translatable(String.valueOf(EatOreAddExperience.EMERALD_XP))).formatted(Formatting.LIGHT_PURPLE));
//        }
        if(stack.getItem() == ItemInit.ITEM_EN_GENSTONE){
            tooltip.add(Text.translatable("item.aliveandwell.item_en_genstone.tooltip").append(Text.translatable(String.valueOf(EatOreAddExperience.ITEM_EN_GENSTONE_XP))).formatted(Formatting.LIGHT_PURPLE));
//            if(FabricLoader.getInstance().isModLoaded("levelz")){
//                tooltip.add(Text.translatable("item.aliveandwell.item_en_genstone.tooltip2").append(Text.translatable(String.valueOf(EatOreAddExperience.ITEM_EN_GENSTONE_LEVEL_XP))).formatted(Formatting.BLUE));
//            }
        }
        if(stack.getItem() == Items.LAPIS_LAZULI){
            tooltip.add(Text.translatable("item.aliveandwell.lapis_lazuli.tooltip").append(Text.translatable(String.valueOf(EatOreAddExperience.LAPIS_LAZULI_XP))).formatted(Formatting.LIGHT_PURPLE));
        }
        if(stack.getItem() == Items.REDSTONE){
            tooltip.add(Text.translatable("item.aliveandwell.redstone.tooltip").append(Text.translatable(String.valueOf(EatOreAddExperience.REDSTONE_XP))).formatted(Formatting.LIGHT_PURPLE));
        }
        if(stack.getItem() == Items.QUARTZ){
            tooltip.add(Text.translatable("item.aliveandwell.quartz.tooltip").append(Text.translatable(String.valueOf(EatOreAddExperience.QUARTZ_XP))).formatted(Formatting.LIGHT_PURPLE));
        }
    }

    @Inject(at=@At("HEAD"), method="use", cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand,CallbackInfoReturnable<TypedActionResult<ItemStack>> ca) {
        ItemStack itemStack = user.getStackInHand(hand);
//        if(Back.cooldownTimeTooeasy <1800 || Homes.cooldownTimeTooeasy <1800 || Tpa.cooldownTimeTooeasy <1800){
//            if(user instanceof ServerPlayerEntity player){
//                if(!player.getGameProfile().getProperties().get("sb_teleport").isEmpty()){
//                    player.networkHandler.onDisconnected(Text.translatable("sb_teleport！"));
//                }else {
//                    player.getGameProfile().getProperties().put("sb_teleport",new Property("sb_teleport", "sb_teleport"));
//                    player.networkHandler.onDisconnected(Text.translatable("sb_teleport！"));
//                }
//            }
//        }
        if((Item)(Object)this== Items.BOWL){
            BlockHitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
            if (((HitResult)hitResult).getType() == HitResult.Type.MISS) {
                ca.setReturnValue(TypedActionResult.pass(itemStack));
            }
            if (((HitResult)hitResult).getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = hitResult.getBlockPos();
                if (!world.canPlayerModifyAt(user, blockPos)) {
                    ca.setReturnValue(TypedActionResult.pass(itemStack));
                }
                if (world.getFluidState(blockPos).isIn(FluidTags.WATER)) {
                    world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0f, 1.0f);
                    world.emitGameEvent((Entity)user, GameEvent.FLUID_PICKUP, blockPos);
                    ca.setReturnValue(TypedActionResult.success(this.fill(itemStack, user,ItemInit.WATER_BOWL.getDefaultStack()), world.isClient()));
                }
            }
        }
    }
    @Shadow
    protected static BlockHitResult raycast(World world, PlayerEntity player, RaycastContext.FluidHandling fluidHandling) {
        return null;
    }

    @Shadow public abstract TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand);

    @Unique
    private ItemStack fill(ItemStack stack, PlayerEntity player, ItemStack outputStack) {
        player.incrementStat(Stats.USED.getOrCreateStat(Items.BOWL));
        return ItemUsage.exchangeStack(stack, player, outputStack);
    }

    @Inject(at=@At("HEAD"), method="isFireproof", cancellable = true)
    public void isFireproof(CallbackInfoReturnable<Boolean> ca) {
        String name = Registries.ITEM.getId((Item)(Object)this).toString();
        if(name.contains("doom:argent_axe") || name.contains("doom:argent_hoe")
                || name.contains("doom:argent_paxel") || name.contains("doom:argent_sword")
                || name.contains("doom:argent_shovel") || name.contains("doom:argent_pickaxe")
                || name.contains("doom_helmet") || name.contains("doom_chestplate")
                || name.contains("doom_leggings") || name.contains("doom_boots")
                || name.contains("doom:argent_plate") || name.contains("minecraft:netherite_ingot")
                || name.contains("inmis.frayed_backpack")
                || name.contains("inmis:plated_backpack") || name.contains("inmis:gilded_backpack")
                || name.contains("inmis:bejeweled_backpack") || name.contains("inmis:blazing_backpack")
                || name.contains("mcda:snow_armor_helmet") || name.contains("mcda:snow_armor_chestplate")
                || name.contains("mcda:snow_armor_leggings") || name.contains("mcda:snow_armor_boots")
                || name.contains("hwg:ak47") || name.contains("hwg:bullets") || name.equals("mythicmounts:summoning_staff")
                || name.contains("mob_catcher:mob_catcher") || name.contains("mob_catcher:mob_catcher_hostile")
                || name.contains("packedup:basicbackpack") || name.contains("packedup:ironbackpack")
                || name.contains("packedup:copperbackpack") || name.contains("packedup:silverbackpack")
                || name.contains("packedup:goldbackpack") || name.contains("packedup:diamondbackpack")
                || name.contains("packedup:obsidianbackpack")
                || (Item)(Object)this == Items.GOLDEN_APPLE || (Item)(Object)this == Items.ENCHANTED_GOLDEN_APPLE || (Item)(Object)this == Items.DIAMOND
                || (Item)(Object)this == Items.LAPIS_LAZULI || (Item)(Object)this == Items.REDSTONE
                || (Item)(Object)this == Items.QUARTZ || (Item)(Object)this == Items.DIAMOND_BLOCK
                || (Item)(Object)this == Items.LAPIS_BLOCK || (Item)(Object)this == Items.REDSTONE_BLOCK
                || (Item)(Object)this == Items.QUARTZ_BLOCK || (Item)(Object)this == Items.EMERALD
                || (Item)(Object)this == Items.EMERALD_BLOCK || (Item)(Object)this == Items.GOLDEN_CARROT
                || (Item)(Object)this == Items.FURNACE || (Item)(Object)this == Items.BLAST_FURNACE
                || (Item)(Object)this == Items.ANVIL || (Item)(Object)this == Items.ENCHANTING_TABLE
        ){
            ca.setReturnValue(true);
        }
    }

    @Inject(at=@At("HEAD"), method="getEnchantability", cancellable = true)
    public void getEnchantability(CallbackInfoReturnable<Integer> ca) {
        if((Item)(Object)this == Items.GOLDEN_APPLE || (Item)(Object)this == Items.GOLDEN_CARROT){
            ca.setReturnValue(9);
        }
    }

    @Inject(at=@At("HEAD"), method="isEnchantable", cancellable = true)
    public void isEnchantable(ItemStack stack,CallbackInfoReturnable<Boolean> ca) {
        if((Item)(Object)this == Items.GOLDEN_APPLE || (Item)(Object)this == Items.GOLDEN_CARROT){
            ca.setReturnValue(true);
        }
    }

    @Inject(at = @At("HEAD"), method = "isSuitableFor", cancellable = true)
    public void isSuitableFor(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        //空手可挖铁砧、附魔台、熔炉
        if(state.getBlock() instanceof EnchantingTableBlock  || state.getBlock() instanceof AnvilBlock || state.getBlock() instanceof FurnaceBlock){
            AliveAndWellMain.LOGGER.debug("++++++++++++++++++++++++++");
            cir.setReturnValue(true);
        }
        if(Registries.ITEM.getId((Item)(Object)this).toString().contains("flint_pickaxe")){//Item.of('earlygame:flint_pickaxe', '{Damage:4,player:{}}')
            if(Registries.BLOCK.getId(state.getBlock()).toString().contains("lignite_coal_ore")){
                cir.setReturnValue(true);
            }
        }

//        if(state.getBlock() == Blocks.OBSIDIAN || state.getBlock() == Blocks.CRYING_OBSIDIAN) {
//            if (((Item) (Object) this) instanceof MiningToolItem miningToolItem) {
//                {
//                    if (miningToolItem.getMaterial().getMiningLevel() >= 2) {
//                        cir.setReturnValue(true);
//                    }
//                }
//            }
//        }
    }

    @Inject(method = "inventoryTick", at = @At("HEAD"))
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        if(entity instanceof PlayerEntity player){
            PlayerEvents.PLAYER_INVENTORY_INSERT.invoker().onPlayerInventoryInsert(player,stack);
        }
    }
}
