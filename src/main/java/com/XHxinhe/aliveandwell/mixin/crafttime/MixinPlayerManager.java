package com.XHxinhe.aliveandwell.mixin.crafttime;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.crafttime.Constants;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {

    public MixinPlayerManager() {
    }



	@Inject(method = "onPlayerConnect", at = @At("TAIL"))
	private void onPlayerJoin(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
		AliveAndWellMain.CRAFT_DIFFICULTY.difficultyMap.forEach((k, v) -> {
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeVarInt(k);
			buf.writeFloat(v);
			ServerPlayNetworking.send(player, Constants.DIFFICULTY_TABLE_PACKET_ID, buf);
		});
	}
}
