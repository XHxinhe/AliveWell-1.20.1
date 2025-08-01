package com.XHxinhe.aliveandwell.xpgui.network;

import com.XHxinhe.aliveandwell.xpgui.common.PlayerSyncAccess;
import com.XHxinhe.aliveandwell.xpgui.common.XPStates;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class PlayerStatsServerPacket {
    public static final Identifier XP_PACKET = new Identifier("aliveandwell", "player_xp_box");


    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(XP_PACKET, (server, player, handler, buffer, sender) -> {
            if (player == null)
                return;
            ((PlayerSyncAccess) player).addXPBox(buffer.readInt());
            ((PlayerSyncAccess) player).setXpBox(buffer.readInt());
        });
    }

    public static void writeS2CXPPacket(XPStates playerStatsManager, ServerPlayerEntity serverPlayerEntity) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(playerStatsManager.getXp());
        buf.writeInt(playerStatsManager.GetMaxXp());
        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(XP_PACKET, buf);
        serverPlayerEntity.networkHandler.sendPacket(packet);
    }

    public static void writeS2CXPPacket2(XPStates playerStatsManager, int experience) {
        playerStatsManager.addXPBox(experience);
        playerStatsManager.setXpBox(playerStatsManager.getXp());
    }
}
