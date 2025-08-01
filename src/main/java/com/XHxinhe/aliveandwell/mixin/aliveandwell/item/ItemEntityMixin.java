package com.XHxinhe.aliveandwell.mixin.aliveandwell.item;

import com.XHxinhe.aliveandwell.registry.BlockInit;
import com.XHxinhe.aliveandwell.registry.ItemInit;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Shadow private int pickupDelay;
    @Final
    @Shadow private static final int DESPAWN_AGE = 15*60*20;
    @Mutable
    @Final
    @Shadow private static final TrackedData<ItemStack> STACK;

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 6000))
    public int tick(int constant) {
        return 15*60*20;
    }

    @Shadow
    public ItemStack getStack() {
        return (ItemStack)this.getDataTracker().get(STACK);
    }

    @Shadow public abstract Text getName();

    @Shadow public abstract boolean isFireImmune();

    @Inject(at=@At("HEAD"), method="onPlayerCollision")
    public void onPlayerCollision(PlayerEntity player, CallbackInfo ca) {
        super.onPlayerCollision(player);
        if (!this.getWorld().isClient) {
            ItemStack itemStack = ((ItemEntity)(Object)this).getStack();
            if(!itemStack.isEmpty()){
                assert itemStack.getNbt() != null;
                if(itemStack.hasNbt()){
                    if(itemStack.getNbt().contains("equip_mob")){
                        this.pickupDelay = 32767;
                        this.kill();
                    }
                }
            }
        }
    }

    @Inject(at=@At("HEAD"), method="damage", cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        String name = this.getName().toString();//minecraft.stone

        Item item = this.getStack().getItem();
        if(item == Items.GOLDEN_APPLE || item == Items.ENCHANTED_GOLDEN_APPLE
                || item == Items.DIAMOND || item == Items.LAPIS_LAZULI
                || item == Items.REDSTONE || item == Items.QUARTZ
                || item == Items.DIAMOND_BLOCK || item == Items.LAPIS_BLOCK
                || item == Items.REDSTONE_BLOCK || item == ItemInit.ARGENT_CORE
                || item == Items.QUARTZ_BLOCK || item == Items.EMERALD
                || item == Items.EMERALD_BLOCK || item == Items.GOLDEN_CARROT
                || item == ItemInit.ADAMANTIUM_LEGGINGS || item == ItemInit.ADAMANTIUM_BOOTS
                || item == ItemInit.ADAMANTIUM_CHESTPLATE || item == ItemInit.ADAMANTIUM_HELMET
                || item == ItemInit.ITEM_EN_GENSTONE || item == ItemInit.ADAMANTIUM_CORE
                || item == ItemInit.ADAMANTIUM_SWORD
                || item == ItemInit.ELYTRA_CORE || item == ItemInit.ENCHANTED_GOLDEN_CARROT
                || item == ItemInit.EX_ADAMAN || item == ItemInit.EX_COPPER
                || item == ItemInit.EX_DIAMOND || item == ItemInit.EX_GOLD
                || item == ItemInit.EX_MITHRIL || item == ItemInit.EX_PICKAXE
                || item == ItemInit.FS || item == ItemInit.INGOT_ADAMANTIUM
                || item == ItemInit.INGOT_MITHRIL || item == ItemInit.INGOT_WUJIN
                || item == ItemInit.MITHRIL_BOOTS || item == ItemInit.MITHRIL_CHESTPLATE
                || item == ItemInit.MITHRIL_HELMET || item == ItemInit.MITHRIL_LEGGINGS
                || item == ItemInit.MITHRIL_SWORD || item == ItemInit.REBORN_STONE
                || item == ItemInit.MITHRIL_CORE || item == ItemInit.SKELETON_CORE
                || item == ItemInit.WUJIN_BOOTS || item == ItemInit.WUJIN_CHESTPLATE
                || item == ItemInit.WUJIN_HELMET || item == ItemInit.WUJIN_LEGGINGS
                || item == ItemInit.WUJIN_SWORD || item == ItemInit.ANCIENT_SWORD
                || item == BlockInit.FLINT_CRAFTING_TABLE.asItem()
                || item == BlockInit.COPPER_CRAFTING_TABLE.asItem()
                || item == BlockInit.IRON_CRAFTING_TABLE.asItem()
                || item == BlockInit.DIAMOND_CRAFTING_TABLE.asItem()
                || item == BlockInit.NETHERITE_CRAFTING_TABLE.asItem()
                || item == BlockInit.CLAY_FURNACE.asItem()
                || item == BlockInit.OBSIDIAN_FURNACE.asItem()
                || item == BlockInit.NETHERRACK_FURNACE.asItem()
                || item == Blocks.FURNACE.asItem()
                || item == Blocks.BLAST_FURNACE.asItem()
                || item == Blocks.ANVIL.asItem()
                || item == Blocks.ENCHANTING_TABLE.asItem()
                || name.contains("doom.argent_axe") || name.contains("doom.argent_hoe")
                || name.contains("doom.argent_paxel") || name.contains("doom.argent_sword")
                || name.contains("doom.argent_shovel") || name.contains("doom.argent_pickaxe")
                || name.contains("doom_helmet") || name.contains("doom_chestplate")
                || name.contains("doom_leggings") || name.contains("doom_boots")
                || name.contains("doom.argent_plate") || name.contains("minecraft.netherite_ingot")
                || name.contains("inmis.frayed_backpack")
                || name.contains("inmis.plated_backpack") || name.contains("inmis.gilded_backpack")
                || name.contains("inmis.bejeweled_backpack") || name.contains("inmis.blazing_backpack")
                || name.contains("mcda.snow_armor_helmet") || name.contains("mcda.snow_armor_chestplate")
                || name.contains("mcda.snow_armor_leggings") || name.contains("mcda.snow_armor_boots")
                || name.contains("hwg.ak47") || name.contains("hwg.bullets")
                || name.contains("mythicmounts.summoning_staff")
                || name.contains("mob_catcher.mob_catcher") || name.contains("mob_catcher.mob_catcher_hostile")
                || name.contains("pswg.spawn_xwing_t65b") || name.contains("pswg.spawn_landspeeder_x34")
                || name.contains("pswg.spawn_zephyr_j") || name.contains("pswg.small_power_pack")
                || name.contains("pswg.blaster_ee3") || name.contains("pswg.blaster_t21")
                || name.contains("pswg.blaster_dc15a") || name.contains("pswg.blaster_ca87")
                || name.contains("pswg.blaster_rk3") || name.contains("pswg.blaster_rt97c")
                || name.contains("pswg_addon_clonewars.blaster_dc17") || name.contains("pswg.blaster_e11")
                || name.contains("pswg.blaster_bowcaster") || name.contains("pswg.blaster_dl18")
                || name.contains("pswg.blaster_dh17") || name.contains("pswg.blaster_dc15")
                || name.contains("pswg.blaster_cycler") || name.contains("pswg.blaster_a280")
                || name.contains("pswg.blaster_jawa_ion") || name.contains("pswg.blaster_dl44")
                || name.contains("pswg.blaster_se14c") || name.contains("pswg.blaster_dlt19")
                || name.contains("pswg.lightsaber") || (name.contains("soulsweapons.")&& name.contains("sword"))
                || name.contains("soulsweapons.bloodthirster") || name.contains("soulsweapons.darkin_blade")
                || name.contains("soulsweapons.dragon_staff") || name.contains("soulsweapons.withered_wabbajack")
                || name.contains("soulsweapons.whirligig_sawblade") || name.contains("soulsweapons.rageblade")
                || name.contains("soulsweapons.nightfall") || name.contains("soulsweapons.comet_spear")
                || name.contains("soulsweapons.lich_bane") || name.contains("soulsweapons.galeforce")
                || name.contains("soulsweapons.translucent_glaive") || name.contains("soulsweapons.draugr")
                || name.contains("soulsweapons.dawnbreaker") || name.contains("soulsweapons.soul_reaper")
                || name.contains("soulsweapons.forlorn_scythe") || name.contains("soulsweapons.leviathan_axe")
                || name.contains("soulsweapons.skofnung") || name.contains("soulsweapons.mjolnir")
                || name.contains("soulsweapons.sting") || name.contains("soulsweapons.featherlight")
                || name.contains("soulsweapons.darkin_scythe_pre") || name.contains("soulsweapons.darkin_scythe")
                || name.contains("soulsweapons.shdow_assassin_scythe") || name.contains("soulsweapons.kirkhammer")
                || name.contains("soulsweapons.draupnir_spear")
                || name.contains("packedup.basicbackpack") || name.contains("packedup.ironbackpack")
                || name.contains("packedup.copperbackpack") || name.contains("packedup.silverbackpack")
                || name.contains("packedup.goldbackpack") || name.contains("packedup.diamondbackpack")
                || name.contains("packedup.obsidianbackpack") || name.contains("mob_catcher.mob_catcher")
                || name.contains("enderchests.ender_pouch")  || name.contains("enderchests.ender_bag")
                || name.contains("mobz.shield") || name.contains("gitbm.bucket_of_entity")
                || name.contains("rlovelyr.")
        ){
            cir.setReturnValue(false);
        }
    }

    static {
        STACK = DataTracker.registerData(ItemEntityMixin.class, TrackedDataHandlerRegistry.ITEM_STACK);
    }
}
