package com.XHxinhe.aliveandwell.client;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.crafttime.Constants;
import com.XHxinhe.aliveandwell.item.CompassAncientCity;
import com.XHxinhe.aliveandwell.item.CompassMansion;
import com.XHxinhe.aliveandwell.miningsblock.network.MiningNetwork;
import com.XHxinhe.aliveandwell.registry.ItemInit;
import com.XHxinhe.aliveandwell.registry.events.ScreenEventsClient;
import com.XHxinhe.aliveandwell.util.config.CommonConfig;
import com.XHxinhe.aliveandwell.xpgui.network.PlayerStatsClientPacket;
import com.XHxinhe.aliveandwell.util.ModsChect;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.item.CompassAnglePredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AliveAndWellClient implements ClientModInitializer {
    public boolean a;

    // 构造方法，初始化变量a为配置中的b
    public AliveAndWellClient() {
        this.a = CommonConfig.b;
    }

    // 客户端初始化方法
    public void onInitializeClient() {
        // 初始化自定义界面事件
        ScreenEventsClient.init();

        // 检查依赖模组（已注释）

        ClientLifecycleEvents.CLIENT_STARTED.register((client)-> {
            boolean b =(new ModsChect()).chectMods();
            if (this.a && !b) {
                client.close();
            }
        });


        // 初始化经验值GUI相关的网络包
        PlayerStatsClientPacket.init();

        // 注册合成难度同步数据包的接收器
        ClientPlayNetworking.registerGlobalReceiver(Constants.DIFFICULTY_TABLE_PACKET_ID, (client, handler, packetBuf, responseSender) -> {
            int item = packetBuf.readVarInt();
            float value = packetBuf.readFloat();
            AliveAndWellMain.map.setDifficulty(item, value);
        });

        // 连锁挖矿功能：每5tick同步一次玩家状态
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            ClientWorld world = client.world;
            ClientPlayerEntity player = client.player;
            if (world != null && player != null && world.getTime() % 5L == 0L) {
                boolean enabled = !player.isSneaking(); // 潜行时禁用连锁挖矿
                MiningNetwork.sendState(enabled);
            }
        });

        //古城指南针
        ModelPredicateProviderRegistry.register(ItemInit.COMPASS_ANCIENT_CITY, new Identifier("angle"), new CompassAnglePredicateProvider((world, stack, entity) -> {
            if (stack.isOf(ItemInit.COMPASS_ANCIENT_CITY)) {
                CompassAncientCity item = (CompassAncientCity) stack.getItem();
                return item.getStructurePos(world, stack);
            } else {
                return null;
            }
        }));
        ModelPredicateProviderRegistry.register(ItemInit.COMPASS_MANSION, new Identifier("angle"), new CompassAnglePredicateProvider((world, stack, entity) -> {
            if (stack.isOf(ItemInit.COMPASS_MANSION)) {
                CompassMansion item = (CompassMansion)stack.getItem();
                return item.getStructurePos(world, stack);
            } else {
                return null;
            }
        }));
    }
}
