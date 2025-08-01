package com.XHxinhe.aliveandwell.miningsblock.network;

import com.XHxinhe.aliveandwell.miningsblock.MiningPlayers;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.MinecraftServer;

public class MiningNetwork {
    public static final Identifier SEND_STATE = new Identifier("aliveandwell", "state");

    public MiningNetwork() {
    }

    public static void sendState(boolean state) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(state);
        ClientPlayNetworking.send(SEND_STATE, buf);
    }

    public static void handleState(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        boolean flag = packetByteBuf.readBoolean();
        minecraftServer.execute(() -> {
            if (flag) {
                MiningPlayers.activateMining(serverPlayerEntity, serverPlayerEntity.getWorld().getTime());
            } else {
                MiningPlayers.deactivateMining(serverPlayerEntity);
            }

        });
    }
}