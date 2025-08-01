package com.XHxinhe.aliveandwell.mixin.xpgui;

import com.XHxinhe.aliveandwell.xpgui.common.PlayerStatsManagerAccess;
import com.XHxinhe.aliveandwell.xpgui.common.PlayerSyncAccess;
import com.XHxinhe.aliveandwell.xpgui.network.PlayerStatsServerPacket;
import com.XHxinhe.aliveandwell.xpgui.common.XPStates;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements PlayerSyncAccess {
    @Unique
    private final XPStates playerStatsManager = ((PlayerStatsManagerAccess) this).getPlayerStatsManager();

    @Unique
    private int syncedLevelExperience = -99999999;
    @Unique
    private boolean syncTeleportStats = false;
    @Unique
    private int tinySyncTicker = 0;

    @Shadow
    @Final
    public MinecraftServer server;

    @Shadow
    public abstract void sendMessage(Text message);

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }


    @Inject(method = "playerTick",
            at = @At(value = "FIELD",
                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;totalExperience:I",
                    ordinal = 0,
                    shift = At.Shift.BEFORE
            )
    )
    private void playerTickMixin(CallbackInfo info) {
        if (playerStatsManager.getXp() != this.syncedLevelExperience) {
            this.syncedLevelExperience = playerStatsManager.getXp();
            PlayerStatsServerPacket.writeS2CXPPacket(playerStatsManager, ((ServerPlayerEntity) (Object) this));
            if (this.syncTeleportStats) {
                PlayerStatsServerPacket.writeS2CXPPacket(playerStatsManager, (ServerPlayerEntity) (Object) this);
                this.syncTeleportStats = false;
            }
        }

        if (this.tinySyncTicker > 0) {
            --this.tinySyncTicker;
            if (this.tinySyncTicker % 20 == 0) {
                this.syncStats(false);
            }
        }
    }

    @Inject(
        method = {"onSpawn"},
        at = {@At("TAIL")}
    )
    private void onSpawnMixin(CallbackInfo info) {
        PlayerStatsServerPacket.writeS2CXPPacket(playerStatsManager, (ServerPlayerEntity) (Object) this);
    }

    @Inject(
        method = {"copyFrom"},
        at = {@At(
                    value = "FIELD",
                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;syncedExperience:I",
                    ordinal = 0
)}
            )
    private void copyFromMixin(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo info) {
        XPStates oldPlayerStatsManager = ((PlayerStatsManagerAccess) oldPlayer).getPlayerStatsManager();
        XPStates newPlayerStatsManager = ((PlayerStatsManagerAccess) this).getPlayerStatsManager();
        newPlayerStatsManager.SetMaxXp(oldPlayerStatsManager.GetMaxXp());
        this.syncStats(false);
    }

    @Override
    public void syncStats(boolean syncDelay) {
        this.syncTeleportStats = true;
        this.syncedLevelExperience = -1;
        if (syncDelay)
            this.tinySyncTicker = 40;
    }

    @Override
    public void addXPBox(int add) {
        ServerPlayerEntity playerEntity = (ServerPlayerEntity) (Object) this;
        playerEntity.addExperience(-add);
        playerStatsManager.xp += add;
        PlayerStatsServerPacket.writeS2CXPPacket(playerStatsManager, playerEntity);
        playerEntity.server.getPlayerManager().sendToAll(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_GAME_MODE, playerEntity));
    }

    @Override
    public void setXpBox(int add) {
        this.playerStatsManager.xp = add;
    }

    @Override
    public boolean canPlus(int plus) {
        if (plus <= 0) {
            if (plus < 0) {
                // 检查玩家是否有足够的经验值来减少（当plus为负数时）
                return this.playerStatsManager.xp >= -plus;
            } else {
                // plus为0时总是返回false
                return false;
            }
        } else {
            // 检查玩家是否满足两个条件：
            // 1. 玩家等级 >= 要增加的经验值
            // 2. 当前经验值 + 要增加的经验值 <= 最大经验值
            return this.experienceLevel >= plus &&
                    this.playerStatsManager.xp <= this.playerStatsManager.GetMaxXp() - plus;
        }
    }
}
