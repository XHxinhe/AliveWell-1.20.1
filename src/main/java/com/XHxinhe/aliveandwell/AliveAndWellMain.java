package com.XHxinhe.aliveandwell;

import com.XHxinhe.aliveandwell.block.randompos.RandomManager;
import com.XHxinhe.aliveandwell.crafttime.Constants;
import com.XHxinhe.aliveandwell.crafttime.sound.SoundEventRegistry;
import com.XHxinhe.aliveandwell.dimensions.DimsRegistry;
import com.XHxinhe.aliveandwell.dimensions.PortalInit;
import com.XHxinhe.aliveandwell.equipmentlevels.handle.ItemTooltipEventHandler;
import com.XHxinhe.aliveandwell.equipmentlevels.handle.LivingDeathEventHandler;
import com.XHxinhe.aliveandwell.equipmentlevels.handle.LivingHurtEventHandler;
import com.XHxinhe.aliveandwell.equipmentlevels.handle.LivingUpdateEventHandler;
import com.XHxinhe.aliveandwell.hometpaback.Back;
import com.XHxinhe.aliveandwell.hometpaback.Homes;
import com.XHxinhe.aliveandwell.hometpaback.Tpa;
import com.XHxinhe.aliveandwell.miningsblock.BlockGroups;
import com.XHxinhe.aliveandwell.miningsblock.MiningEnchantment;
import com.XHxinhe.aliveandwell.miningsblock.MiningPlayers;
import com.XHxinhe.aliveandwell.miningsblock.logic.BlockProcessor;
import com.XHxinhe.aliveandwell.miningsblock.network.MiningNetwork;
import com.XHxinhe.aliveandwell.registry.BlockInit;
import com.XHxinhe.aliveandwell.registry.EnityRegistry;
import com.XHxinhe.aliveandwell.registry.ItemInit;
import com.XHxinhe.aliveandwell.registry.VanillaTweaks;
import com.XHxinhe.aliveandwell.registry.events.*;
import com.XHxinhe.aliveandwell.tablesandfurnaces.worklevel.CraftingIngredients;
import com.XHxinhe.aliveandwell.tablesandfurnaces.worklevel.FurnaceIngredients;
import com.XHxinhe.aliveandwell.util.config.CommonConfig;
import com.XHxinhe.aliveandwell.crafttime.config.ConfigLoader;
import com.XHxinhe.aliveandwell.equipmentlevels.network.NetWorkHandler;
import com.XHxinhe.aliveandwell.flintcoppertool.init.EventsInit;
import com.XHxinhe.aliveandwell.util.AliveAndWellGroup;
import com.XHxinhe.aliveandwell.util.ConfigLock;
import com.XHxinhe.aliveandwell.util.ModsChect;
import com.XHxinhe.aliveandwell.util.config.Config;
import com.XHxinhe.aliveandwell.world.OreGenBiome;
import com.XHxinhe.aliveandwell.xpgui.network.PlayerStatsServerPacket;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class AliveAndWellMain implements ModInitializer {
    public static final String VERSION = "v5.1.0";//
    public static boolean canEnd = false;//仅测试使用
//   public static boolean canEnd = false;//仅测试使用====打包改false
    public static boolean canCreative = true;//仅测试使用====打包改false
//    public static boolean canCreative = false;//仅测试使用====打包改false

    public static final String MOD_ID = "aliveandwell";

    public static final Identifier MOD_DIMENSION_ID = new Identifier(AliveAndWellMain.MOD_ID, "underworld_dim");  // 384 -64
    public static final org.slf4j.Logger LOGGER = LogUtils.getLogger();
    public static Config config;

    public static int ca;
    public static int structureUnderDay = 32;

    public static double time_increment = 20.0D / 40.0D;//白天20分钟

    public static int day ;

    //crafttime
    public static ConfigLoader map = new ConfigLoader();
    public static ConfigLoader CRAFT_DIFFICULTY = new ConfigLoader();

    public RandomManager randomManagerStorage;

    //连锁附魔
    public static final Enchantment MINING_BLOCK = new MiningEnchantment();

    @Override
    public void onInitialize() {
        config = new Config();
        config.load();

        DimsRegistry.setupDimension();

        ItemInit.init();
        BlockInit.registerBlocks();
        BlockInit.registerBlockItems();
        BlockInit.registerFuels();
        PortalInit.registerPortal();
        PlayerHurtEventHandler.onHurt();

        //flintcoppertool
        EventsInit.init();

        //xpgui
        PlayerStatsServerPacket.init();

        FuelRegistry.INSTANCE.add(ItemInit.BONE_STICK,150);

        //tables=============================
        EnityRegistry.init();
        CraftingIngredients.init();
        FurnaceIngredients.initFuel();
        FurnaceIngredients.initItem();

        AliveAndWellGroup.addGroup();

        //events
        OreGenBiome.addOres();
        EatOreAddExperience.init();
//        EatFood.init();
        UseBlock.init();
        UseItem.init();
        AddExhaustion.init();
        AllowSleep.init();
        PlayerInventoryTick.onPlayerInventoryItemStackTick();

        //homes ,tpa
        new Homes().register();
        new Back().register();
        new Tpa().register();

        //crafttime
        ConfigLoader.genSampleConfig();
        try {
            File cfgFile = FabricLoader.getInstance().getConfigDir().resolve(Constants.CONFIG_FILENAME).toFile();
            FileInputStream inputFile = new FileInputStream(cfgFile);
            byte[] buf = new byte[inputFile.available()];
            inputFile.read(buf);
            inputFile.close();
            String json = new String(buf);
            CRAFT_DIFFICULTY.parserFrom(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Registry.register(Registries.SOUND_EVENT, SoundEventRegistry.finishSoundID, SoundEventRegistry.finishSound);
        //附魔：连锁
        Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, "mining_block"), MINING_BLOCK);

        //equipmentlevels
        ItemTooltipEventHandler.addInformation();
        LivingDeathEventHandler.init();
        LivingHurtEventHandler.onHurt();
        LivingHurtEventHandler.onArrowShoot();
        LivingHurtEventHandler.onArrowHit();
        LivingUpdateEventHandler.onUpdate();
        NetWorkHandler.onRun();

        VanillaTweaks.ApplyChanges();

        //连锁======================================================================
        ServerLifecycleEvents.END_DATA_PACK_RELOAD
                .register((minecraftServer, serverResourceManager, b) -> BlockProcessor.rebuild());
        ServerTickEvents.END_WORLD_TICK
                .register((world) -> MiningPlayers.validate(world.getTime()));
        ServerPlayNetworking
                .registerGlobalReceiver(MiningNetwork.SEND_STATE, MiningNetwork::handleState);
        //连锁======================================================================

//        ServerLoginConnectionEvents.INIT.register( (handler, server) -> {
//            if(!AliveAndWellMain.canCreative){
//                try {
//                    if(!(new ConfigLock().isDefaultConfigConnect())){
//                        server.close();
//                    }
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            BlockGroups.init();//连锁==============================================================
            randomManagerStorage = Objects.requireNonNull(server.getWorld(World.OVERWORLD)).getPersistentStateManager().getOrCreate(RandomManager::new, RandomManager::new, MOD_ID);

            //***************************************************************
            if(!canCreative){
                if(server.isUsingNativeTransport()){//服务器，不包括本地开局域网
                    boolean b = new ModsChect().chectModsServer();
                    if(CommonConfig.b){
                        if(!b ){
                            System.out.println("Server chect mods DONE!");
                            server.close();
                        }
                    }
                }
            }else {
                if(server.isUsingNativeTransport()){//服务器，不包括本地开局域网
                    boolean b = new ModsChect().chectModsServer();
                    System.out.println("Server chect mods DONE!"+ b);
                }
            }

            if(!AliveAndWellMain.canCreative){
                try {
                    if(!(new ConfigLock().isDefaultConfig())){
                        server.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            ServerCommandSource serverCommandSource = server.getCommandSource();
            //关闭游戏规则作弊选项
            if(server.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)//死亡不掉落true1===================================
                    || !server.getGameRules().getBoolean(GameRules.DROWNING_DAMAGE)//溺水伤害false2
                    || !server.getGameRules().getBoolean(GameRules.FALL_DAMAGE)//掉落伤害false3
                    || !server.getGameRules().getBoolean(GameRules.FREEZE_DAMAGE)//冰冻伤害false4
                    || !server.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)//禁用怪物破坏方块false5==================
                    || !server.getGameRules().getBoolean(GameRules.DO_FIRE_TICK)//火焰蔓延false7================================
                    || !server.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)//时间循环false8
                    || !server.getGameRules().getBoolean(GameRules.DO_WEATHER_CYCLE)//天气更替false9
                    || !server.getGameRules().getBoolean(GameRules.DO_INSOMNIA)//生成幻翼false10================================
                    || !server.getGameRules().getBoolean(GameRules.DO_PATROL_SPAWNING)//生成灾厄巡逻队false11
                    || !server.getGameRules().getBoolean(GameRules.DO_WARDEN_SPAWNING)//生成监守者false12
            ){
                GameRules.BooleanRule rule1 = serverCommandSource.getServer().getGameRules().get(GameRules.KEEP_INVENTORY);
                GameRules.BooleanRule rule2 = serverCommandSource.getServer().getGameRules().get(GameRules.DROWNING_DAMAGE);
                GameRules.BooleanRule rule3 = serverCommandSource.getServer().getGameRules().get(GameRules.FALL_DAMAGE);
                GameRules.BooleanRule rule4 = serverCommandSource.getServer().getGameRules().get(GameRules.FREEZE_DAMAGE);
                GameRules.BooleanRule rule5 = serverCommandSource.getServer().getGameRules().get(GameRules.DO_MOB_GRIEFING);
                GameRules.BooleanRule rule7 = serverCommandSource.getServer().getGameRules().get(GameRules.DO_FIRE_TICK);
                GameRules.BooleanRule rule8 = serverCommandSource.getServer().getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE);
                GameRules.BooleanRule rule9 = serverCommandSource.getServer().getGameRules().get(GameRules.DO_WEATHER_CYCLE);
                GameRules.BooleanRule rule10 = serverCommandSource.getServer().getGameRules().get(GameRules.DO_INSOMNIA);
                GameRules.BooleanRule rule11 = serverCommandSource.getServer().getGameRules().get(GameRules.DO_PATROL_SPAWNING);
                GameRules.BooleanRule rule12 = serverCommandSource.getServer().getGameRules().get(GameRules.DO_WARDEN_SPAWNING);
                rule1.set(false,server);
                rule2.set(true,server);
                rule3.set(true,server);
                rule4.set(true,server);
                rule5.set(true,server);
                rule7.set(true,server);
                rule8.set(true,server);
                rule9.set(true,server);
                rule10.set(true,server);
                rule11.set(true,server);
                rule12.set(true,server);
            }
            //***************************************************************

        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            RandomManager.canSpawnStructure = false;//清除缓存，避免新建世界读取
            RandomManager.canSpawnVillager = false;
        });
    }

}
