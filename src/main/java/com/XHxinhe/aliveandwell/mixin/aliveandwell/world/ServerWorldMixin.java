package com.XHxinhe.aliveandwell.mixin.aliveandwell.world;

import com.XHxinhe.aliveandwell.block.randompos.RandomManager;
import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.dimensions.DimsRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {

    @Unique
    private static final String[] enityName = {"screecher", "imp", "nightmare_imp", "iconofsin", "painelemental", "shotgunguy", "chaingunner", "marauder", "imp2016", "cyberdemon", "cyberdemon2016", "possessed_scientist", "possessed_worker", "possessed_soldier", "arachnotron", "zombieman", "mechazombie", "gore_nest", "cueball", "stone_imp", "maykr_drone", "blood_maykr", "arch_maykr", "tyrant", "tentacle", "turret", "gladiator", "spawn_bowman"};
    @Shadow @Final private ServerChunkManager chunkManager;
    @Shadow @Final private MinecraftServer server;
    @Shadow public abstract MinecraftServer getServer();
    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }
    @Inject(at = @At("TAIL"), method = "tickEntity")
    public void tickEntity(Entity entity, CallbackInfo ca) {
        // 如果游戏天数大于等于48天
        if (AliveAndWellMain.day >= 48) {
            // 允许随机生成结构
            RandomManager.canSpawnStructure = true;
        }
        if (entity instanceof ItemEntity itemEntity) {
            if (((ItemEntity) entity).getWorld().getRegistryKey() != World.OVERWORLD && entity.getName().toString().contains("argent_energy") && entity.getName().toString().contains("doom.")) {
                if (itemEntity.getStack().hasNbt()) {
                    if (!Objects.requireNonNull(itemEntity.getStack().getNbt()).contains("aliveandwell")) {
                        this.setNbt(itemEntity.getStack());
                    }
                } else {
                    this.setNbt(itemEntity.getStack());
                }
            }

            // 检查物品实体是否在下界，并且其名称包含 "netherite_scrap" 或 "blaze_rod"，以及 "minecraft."
            if (((ItemEntity) entity).getWorld().getRegistryKey() == World.NETHER && (entity.getName().toString().contains("netherite_scrap") || entity.getName().toString().contains("blaze_rod")) && entity.getName().toString().contains("minecraft.")) {
                // 如果物品有NBT数据
                if (itemEntity.getStack().hasNbt()) {
                    // 并且NBT数据中不包含 "aliveandwell" 标签
                    if (!Objects.requireNonNull(itemEntity.getStack().getNbt()).contains("aliveandwell")) {
                        // 为该物品添加 "aliveandwell" 标签
                        this.setNbt(itemEntity.getStack());
                    }
                } else { // 如果物品没有NBT数据
                    // 直接为该物品添加 "aliveandwell" 标签
                    this.setNbt(itemEntity.getStack());
                }
            }
        }
    }

    @Inject(at = @At("TAIL"), method = "tickChunk")
    public void tickChunk(WorldChunk chunk, int randomTickSpeed, CallbackInfo ca) {
        // 获取当前区块的位置
        ChunkPos chunkPos = chunk.getPos();
        // 检查该区块是否已加载
        if (this.chunkManager.isChunkLoaded(chunkPos.x, chunkPos.z)) {
            // 使用 Mixin Accessor 来设置是否生成动物
            // 仅在第1天或每128天时允许生成动物
            ((ServerChunkManagerAccessor)this.chunkManager).setSpawnAnimals(AliveAndWellMain.day == 1 || AliveAndWellMain.day % 128 == 0);
        }

        // 根据游戏天数计算一个基础难度值 i
        int i = AliveAndWellMain.day / 5;
        // 获取当前服务器的玩家数量
        int players = this.server.getCurrentPlayerCount();
        // 限制 i 的最大值为 30
        if (i >= 30) {
            i = 30;
        }
        if (this.getRegistryKey() == World.END) { // 如果在末地
            AliveAndWellMain.ca = 15;
        } else if (AliveAndWellMain.day == 1) { // 如果是第一天
            AliveAndWellMain.ca = 15;
        } else if (players <= 2) { // 玩家数小于等于2
            AliveAndWellMain.ca = 19 + players + i;
        } else if (players <= 4) { // 玩家数小于等于4
            AliveAndWellMain.ca = 20 + players + i;
        } else if (players <= 6) { // 玩家数小于等于6
            AliveAndWellMain.ca = 22 + players + i;
        } else if (players <= 8) { // 玩家数小于等于8
            AliveAndWellMain.ca = 25 + players + i;
        } else if (players <= 10) { // 玩家数小于等于10
            AliveAndWellMain.ca = 29 + players + i;
        } else if (players <= 12) { // 玩家数小于等于12
            AliveAndWellMain.ca = 34 + players + i;
        } else { // 更多玩家
            AliveAndWellMain.ca = 40 + players + i;
        }
    }
    // 这个方法决定了一个实体是否应该被阻止生成
    @Inject(at = @At("HEAD"), method = "shouldCancelSpawn", cancellable = true)
    public void shouldCancelSpawnEvent(Entity entity, CallbackInfoReturnable<Boolean> ca) {
        // if (entity instanceof class_1309) -> if (entity instanceof LivingEntity)
        // 检查实体是否为生物实体
        if (entity instanceof LivingEntity) {
            // !(entity instanceof class_1510) && ((class_1309)entity).method_6063() >= 20000.0F -> !(entity instanceof EnderDragonEntity) && ((LivingEntity) entity).getMaxHealth() >= 20000.0f
            // 如果一个生物不是末影龙，但其最大生命值大于等于20000，则取消其生成
            if (!(entity instanceof EnderDragonEntity) && ((LivingEntity) entity).getMaxHealth() >= 20000.0F) {
                ca.setReturnValue(true); // 设置返回值为true，表示取消生成
            }

            // 如果不允许村民生成，且实体是村民（非僵尸村民），则取消生成
            if (!RandomManager.canSpawnVillager && entity.getName().toString().contains("villager") && !entity.getName().toString().contains("zombie")) {
                ca.setReturnValue(true);
            }

            // 如果实体是来自 "earthtojavamobs" 模组的热带史莱姆，则取消生成
            if (entity.getName().toString().contains("earthtojavamobs.") && entity.getName().toString().contains("tropical_slime")) {
                ca.setReturnValue(true);
            }

            // 以下为反编译代码中新增的逻辑，予以保留和翻译
            // 如果实体是洞穴蜘蛛，并且在自定义的 "地下世界" 维度中，则取消生成
            if (entity.getName().toString().contains("minecraft.") && entity.getName().toString().contains("cave_spider") && entity.getEntityWorld().getRegistryKey() == DimsRegistry.UNDER_WORLD_KEY) {
                ca.setReturnValue(true);
            }

            // 如果实体是铁傀儡，则取消生成
            if (entity.getName().toString().contains("minecraft.") && entity.getName().toString().contains("iron_golem")) {
                ca.setReturnValue(true);
            }

            // 如果实体是哞菇，则取消生成
            if (entity.getName().toString().contains("mooshroom")) {
                ca.setReturnValue(true);
            }

            // 如果实体是来自 "mobz" 模组的特定生物，则取消生成
            if (entity.getName().toString().contains("mobz.") && (entity.getName().toString().contains("small_zombie") || entity.getName().toString().contains("boss_zombie") || entity.getName().toString().contains("cookie_creeper") || entity.getName().toString().contains("katherine") || entity.getName().toString().contains("fiora") || entity.getName().toString().contains("charles") || entity.getName().toString().contains("golem"))) {
                ca.setReturnValue(true);
            }

            // 如果实体是来自 "doom" 模组的血肉梅克，则取消生成
            if (entity.getName().toString().contains("doom.") && entity.getName().toString().contains("blood_maykr")) {
                ca.setReturnValue(true);
            }

            // 如果实体是来自 "deeperdarker" 模组的 Sculk Leech，并且在主世界，则取消生成
            if (entity.getName().toString().contains("deeperdarker.") && entity.getName().toString().contains("sculk_leech") && entity.getWorld().getRegistryKey() == World.OVERWORLD) {
                ca.setReturnValue(true);
            }

            // 如果实体是来自 "ad_astra" 模组的月球村民（非腐化版本），则取消生成
            if (entity.getName().toString().contains("ad_astra.") && entity.getName().toString().contains("lunarian") && !entity.getName().toString().contains("corrupted_")) {
                ca.setReturnValue(true);
            }

            // 如果实体来自 "doom" 模组，在主世界，并且生成高度在 Y=120 或以上，则取消生成
            if (entity.getName().toString().contains("doom.") && entity.getWorld().getRegistryKey() == World.OVERWORLD && entity.getBlockPos().getY() >= 120) {
                ca.setReturnValue(true);
            }

            // 如果实体是蝙蝠，则取消生成
            if (entity.getName().toString().contains("minecraft.") && entity.getName().toString().contains("bat")) {
                ca.setReturnValue(true);
            }


            // 如果游戏天数小于等于16天
            if (AliveAndWellMain.day <= 16) {
                // 如果实体是爬行者（来自原版或特定模组），并且不在地下世界或暮色森林，则取消生成
                if ((entity.getName().toString().contains("minecraft.") || entity.getName().toString().contains("creeperoverhaul.") || entity.getName().toString().contains("mobz.")) && entity.getName().toString().contains("creeper") && entity.getWorld().getRegistryKey() != DimsRegistry.UNDER_WORLD_KEY && !entity.getWorld().getRegistryKey().getValue().toString().contains("twilightforest")) {
                    ca.setReturnValue(true);
                }

                // 如果实体是流浪者，并且不在地下世界或暮色森林，则取消生成
                if (entity.getName().toString().contains("minecraft.") && entity.getName().toString().contains("stray") && entity.getWorld().getRegistryKey() != DimsRegistry.UNDER_WORLD_KEY && !entity.getWorld().getRegistryKey().getValue().toString().contains("twilightforest")) {
                    ca.setReturnValue(true);
                }

                // 如果实体是幻翼，则取消生成
                if (entity.getName().toString().contains("minecraft.") && entity.getName().toString().contains("phantom")) {
                    ca.setReturnValue(true);
                }

                // 如果实体是女巫，则取消生成
                if (entity.getName().toString().contains("minecraft.") && entity.getName().toString().contains("witch")) {
                    ca.setReturnValue(true);
                }

                // 如果实体是来自 "mobz" 模组的 skeli2，并且不在地下世界或暮色森林，则取消生成
                if (entity.getName().toString().contains("mobz.") && entity.getName().toString().contains("skeli2") && entity.getWorld().getRegistryKey() != DimsRegistry.UNDER_WORLD_KEY && !entity.getWorld().getRegistryKey().getValue().toString().contains("twilightforest")) {
                    ca.setReturnValue(true);
                }
            }

            // 如果游戏天数小于等于20天，并且实体是来自 "betteranimalsplus" 模组的特定敌对动物，则取消生成
            if (AliveAndWellMain.day <= 20 && !entity.getName().toString().contains("minecraft.") && (entity.getName().toString().contains("goose") || entity.getName().toString().contains("boar") || entity.getName().toString().contains("coyote") || entity.getName().toString().contains("bear") || entity.getName().toString().contains("wolf")) && entity.getName().toString().contains("betteranimalsplus.")) {
                ca.setReturnValue(true);
            }

            // 如果游戏天数小于等于32天，并且实体是来自 "mobz" 模组的弓箭手或矮人，则取消生成
            if (AliveAndWellMain.day <= 32 && entity.getName().toString().contains("mobz.") && (entity.getName().toString().contains("bowman") || entity.getName().toString().contains("dwarf"))) {
                ca.setReturnValue(true);
            }

            // 如果游戏天数小于等于32天，在主世界，并且是特定原版怪物且带有自定义名称（通常来自刷怪笼或命名牌），则取消生成
            if (AliveAndWellMain.day <= 32 && entity.getWorld().getRegistryKey() == World.OVERWORLD && (entity instanceof ZombieEntity || entity instanceof AbstractSkeletonEntity || entity instanceof SpiderEntity || entity instanceof CreeperEntity || entity instanceof SilverfishEntity || entity instanceof EndermanEntity || entity instanceof WitchEntity) && entity.hasCustomName()) {
                ca.setReturnValue(true);
            }

            // 如果游戏天数小于等于48天，在主世界
            if (AliveAndWellMain.day <= 48 && entity.getWorld().getRegistryKey() == World.OVERWORLD) {
                for (String name : enityName) {
                    if (entity.getName().toString().contains(name)) {
                        ca.setReturnValue(true);
                    }
                }
            }

            // 如果实体是来自 "doom" 模组的 cacodemon 或 lost_soul，并且不在下界，则取消生成
            if (entity.getName().toString().contains("doom.") && (entity.getName().toString().contains("cacodemon") || entity.getName().toString().contains("lost_soul")) && entity.getWorld().getRegistryKey() != World.NETHER) {
                ca.setReturnValue(true);
            }

            // 如果游戏天数小于等于64天，并且是 "doom" 模组的特定怪物，且不在特定维度，则取消生成
            if (AliveAndWellMain.day <= 64 && entity.getName().toString().contains("doom.") && (entity.getName().toString().contains("prowler") || entity.getName().toString().contains("gargoyle") || entity.getName().toString().contains("revenant") || entity.getName().toString().contains("spidermastermind") || entity.getName().toString().contains("stone_imp")) && !entity.getWorld().getRegistryKey().getValue().toString().contains("paradise_lost") && !entity.getWorld().getRegistryKey().getValue().toString().contains("twilightforest")) {
                ca.setReturnValue(true);
            }

            // 如果游戏天数小于等于65天
            if (AliveAndWellMain.day <= 65) {
                // 如果是 "mobz" 模组的弓箭手或骑士2，则取消生成
                if ((entity.getName().toString().contains("archer") || entity.getName().toString().contains("knight2")) && entity.getName().toString().contains("mobz.")) {
                    ca.setReturnValue(true);
                }

                // 如果是 "mobz" 模组的法师，则取消生成
                if (entity.getName().toString().contains("mobz.") && entity.getName().toString().contains("mage")) {
                    ca.setReturnValue(true);
                }
            }

            // 如果游戏天数小于等于80天
            if (AliveAndWellMain.day <= 80) {
                // 如果是 "doom" 模组的特定怪物，且不在特定维度，则取消生成
                if (entity.getName().toString().contains("doom.") && (entity.getName().toString().contains("whiplash") || entity.getName().toString().contains("doom_hunter")) && !entity.getWorld().getRegistryKey().getValue().toString().contains("paradise_lost") && !entity.getWorld().getRegistryKey().getValue().toString().contains("twilightforest")) {
                    ca.setReturnValue(true);
                }

                // 如果是 "hwg" 模组的特定怪物，且不在特定维度，则取消生成
                if (entity.getName().toString().contains("hwg.") && (entity.getName().toString().contains("spy") || entity.getName().toString().contains("merc") || entity.getName().toString().contains("lesser") || entity.getName().toString().contains("greater")) && !entity.getWorld().getRegistryKey().getValue().toString().contains("paradise_lost") && !entity.getWorld().getRegistryKey().getValue().toString().contains("twilightforest")) {
                    ca.setReturnValue(true);
                }

                // 如果实体来自 "gigeresque" 模组，则取消生成
                if (entity.getName().toString().contains("gigeresque.")) {
                    ca.setReturnValue(true);
                }
            }

            // 如果游戏天数小于等于100天，并且是 "hwg" 模组的特定怪物，且不在特定维度，则取消生成
            if (AliveAndWellMain.day <= 100 && entity.getName().toString().contains("hwg.") && (entity.getName().toString().contains("spy") || entity.getName().toString().contains("merc") || entity.getName().toString().contains("lesser") || entity.getName().toString().contains("greater") || entity.getName().toString().contains("motherdemon")) && !entity.getWorld().getRegistryKey().getValue().toString().contains("paradise_lost") && !entity.getWorld().getRegistryKey().getValue().toString().contains("twilightforest")) {
                ca.setReturnValue(true);
            }

            // 如果游戏天数小于等于128天
            if (AliveAndWellMain.day <= 128) {
                // 如果是 "doom" 模组的特定怪物，且不在特定维度，则取消生成
                if (entity.getName().toString().contains("doom.") && (entity.getName().toString().contains("baron") || entity.getName().toString().contains("dreadknight") || entity.getName().toString().contains("archvile")) && !entity.getWorld().getRegistryKey().getValue().toString().contains("paradise_lost") && !entity.getWorld().getRegistryKey().getValue().toString().contains("twilightforest")) {
                    ca.setReturnValue(true);
                }

                // 如果是 "doom" 模组的另外一些特定怪物，且不在特定维度，则取消生成
                if (entity.getName().toString().contains("doom.") && (entity.getName().toString().contains("hellknight") || entity.getName().toString().contains("mancubus") || entity.getName().toString().contains("arachnotroneternal")) && !entity.getWorld().getRegistryKey().getValue().toString().contains("paradise_lost") && !entity.getWorld().getRegistryKey().getValue().toString().contains("twilightforest")) {
                    ca.setReturnValue(true);
                }

                // 如果是 "doom" 模组的角斗士，并且在地下世界，则取消生成
                if (entity.getName().toString().contains("doom.") && entity.getName().toString().contains("gladiator") && entity.getWorld().getRegistryKey() == DimsRegistry.UNDER_WORLD_KEY) {
                    ca.setReturnValue(true);
                }
            }

            // 如果游戏天数小于等于144天
            if (AliveAndWellMain.day <= 144) {
                // 如果是 "doom" 模组的特定怪物，且不在特定维度，则取消生成
                if (entity.getName().toString().contains("doom.") && (entity.getName().toString().contains("spectre") || entity.getName().toString().contains("unwilling") || entity.getName().toString().contains("summoner") || entity.getName().toString().contains("pinky")) && !entity.getWorld().getRegistryKey().getValue().toString().contains("paradise_lost") && !entity.getWorld().getRegistryKey().getValue().toString().contains("twilightforest")) {
                    ca.setReturnValue(true);
                }

                // 如果是 "doom" 模组的梅克，并且在地下世界，则取消生成
                if (entity.getName().toString().contains("doom.") && entity.getName().toString().contains("maykr") && entity.getWorld().getRegistryKey() == DimsRegistry.UNDER_WORLD_KEY) {
                    ca.setReturnValue(true);
                }
            }

            // 如果在下界，并且是 "doom" 模组的特定高级怪物，且不在特定维度，则取消生成
            if (entity.getWorld().getRegistryKey() == World.NETHER && entity.getName().toString().contains("doom.") && (entity.getName().toString().contains("baron") || entity.getName().toString().contains("pinky") || entity.getName().toString().contains("whiplash") || entity.getName().toString().contains("dreadknight") || entity.getName().toString().contains("archvile") || entity.getName().toString().contains("motherdemon") || entity.getName().toString().contains("summoner")) && !entity.getWorld().getRegistryKey().getValue().toString().contains("paradise_lost")) {
                ca.setReturnValue(true);
            }
        }


        // 检查实体是否为物品实体
        if (entity instanceof ItemEntity itemEntity) {
            // 如果游戏天数小于等于32天，并且物品是来自 "bosses_of_mass_destruction" 模组的魂星，则取消生成（通常是掉落）
            if (AliveAndWellMain.day <= 32 && entity.getName().toString().contains("bosses_of_mass_destruction.") && entity.getName().toString().contains("soul_star")) {
                ca.setReturnValue(true);
            }

            // 如果物品是下界合金碎片或烈焰棒，但不在下界
            if (entity.getWorld().getRegistryKey() != World.NETHER && (entity.getName().toString().contains("netherite_scrap") || entity.getName().toString().contains("blaze_rod")) && entity.getName().toString().contains("minecraft.")) {
                if (itemEntity.getStack().hasNbt()) {
                    if (Objects.requireNonNull(itemEntity.getStack().getNbt()).contains("aliveandwell")) {
                        ca.setReturnValue(false);
                    } else {
                        ca.setReturnValue(true);
                    }
                } else {
                    ca.setReturnValue(true);
                }
            }

            // 如果物品是 "doom" 模组的 argent_block
            if (entity.getName().toString().contains("doom.") && entity.getName().toString().contains("argent_block")) {
                if (itemEntity.getStack().hasNbt()) {
                    if (Objects.requireNonNull(itemEntity.getStack().getNbt()).contains("aliveandwell")) {
                        ca.setReturnValue(false);
                    } else {
                        ca.setReturnValue(true);
                    }
                } else {
                    ca.setReturnValue(true);
                }
            }

            // 如果物品是 "doom" 模组的 argent_energy，但不在地下世界或下界
            if (entity.getWorld().getRegistryKey() != DimsRegistry.UNDER_WORLD_KEY && entity.getWorld().getRegistryKey() != World.NETHER && entity.getName().toString().contains("doom.") && entity.getName().toString().contains("argent_energy")) {
                if (itemEntity.getStack().hasNbt()) {
                    if (Objects.requireNonNull(itemEntity.getStack().getNbt()).contains("aliveandwell")) {
                        ca.setReturnValue(false);
                    } else {
                        ca.setReturnValue(true);
                    }
                } else {
                    ca.setReturnValue(true);
                }
            }

            // 如果物品是鞘翅、下界合金碎片或烈焰棒，但不在下界
            if (entity.getWorld().getRegistryKey() != World.NETHER && (entity.getName().toString().contains("elytra") || entity.getName().toString().contains("netherite_scrap") || entity.getName().toString().contains("blaze_rod")) && entity.getName().toString().contains("minecraft.")) {
                if (itemEntity.getStack().hasNbt()) {
                    if (Objects.requireNonNull(itemEntity.getStack().getNbt()).contains("aliveandwell")) {
                        ca.setReturnValue(false);
                    } else {
                        ca.setReturnValue(true);
                    }
                } else {
                    ca.setReturnValue(true);
                }
            }

            // 如果物品是 "mobz" 模组的 boss_ingot
            if (entity.getName().toString().contains("boss_ingot") && entity.getName().toString().contains("mobz.")) {
                if (itemEntity.getStack().hasNbt()) {
                    if (Objects.requireNonNull(itemEntity.getStack().getNbt()).contains("aliveandwell")) {
                        ca.setReturnValue(false);
                    } else {
                        ca.setReturnValue(true);
                    }
                } else {
                    ca.setReturnValue(true);
                }
            }
        }
    }
    @Unique
    public void setNbt(ItemStack itemStack) {
        // 声明一个NBT化合物标签变量
        NbtCompound nbt;
        if (!itemStack.hasNbt()) {
            nbt = new NbtCompound();
        } else {
            nbt = itemStack.getNbt();
        }
        itemStack.setSubNbt("aliveandwell", nbt);
    }
}