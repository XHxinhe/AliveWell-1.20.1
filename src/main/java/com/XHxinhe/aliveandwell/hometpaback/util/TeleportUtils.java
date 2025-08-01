package com.XHxinhe.aliveandwell.hometpaback.util;

import java.util.Timer;
import java.util.TimerTask;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.CommandBossBar;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

/**
 * 提供通用传送逻辑的工具类。
 * <p>
 * 这个类封装了一个带有“站立不动”倒计时的传送机制。
 * 玩家在传送前必须保持静止一段时间，如果移动，倒计时会重置。
 * 它支持通过 Boss Bar 或 Action Bar 向玩家显示倒计时进度。
 */
public class TeleportUtils {

    public TeleportUtils() {
    }

    /**
     * 执行一个通用的、需要玩家站立不动的传送。
     *
     * @param useBossBar     是否使用 Boss Bar 显示倒计时。如果为 false，则使用 Action Bar。
     * @param standStillTime 玩家需要保持静止的总时间（秒）。
     * @param player         将要被传送的玩家。
     * @param onCounterDone  倒计时成功结束后要执行的操作（通常是实际的传送逻辑）。
     */
    public static void genericTeleport(final boolean useBossBar, final double standStillTime, ServerPlayerEntity player, final Runnable onCounterDone) {
        final MinecraftServer server = player.getServer();
        if (server == null) return;

        final double[] counter = {standStillTime};
        final Vec3d[] lastPos = {player.getPos()};
        final CommandBossBar standStillBar;

        // 根据配置创建 Boss Bar
        if (useBossBar) {
            standStillBar = server.getBossBarManager().add(new Identifier("standstill-" + player.getUuidAsString()), Text.empty());
            standStillBar.addPlayer(player);
            standStillBar.setColor(BossBar.Color.GREEN);
        } else {
            standStillBar = null;
        }

        // 设置屏幕淡入效果
        player.networkHandler.sendPacket(new TitleFadeS2CPacket(0, 10, 5));
        final ServerPlayerEntity[] playerRef = {player}; // 使用数组以便在匿名类中修改
        final Timer timer = new Timer();

        // 每 250 毫秒（0.25秒）检查一次
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                // 倒计时结束
                if (counter[0] <= 0.0) {
                    if (useBossBar) {
                        standStillBar.removePlayer(playerRef[0]);
                        server.getBossBarManager().remove(standStillBar);
                    } else {
                        playerRef[0].sendMessage(Text.translatable("aliveandwell.hometpaback.teleporting").formatted(Formatting.GREEN), true);
                    }

                    // 延迟后清除标题，以提供更好的视觉效果
                    (new Timer()).schedule(new TimerTask() {
                        public void run() {
                            playerRef[0].networkHandler.sendPacket(new TitleS2CPacket(Text.empty()));
                        }
                    }, 500L);

                    timer.cancel();
                    // 在服务器主线程中执行传送操作
                    server.execute(onCounterDone);
                } else { // 倒计时进行中
                    Vec3d currPos = playerRef[0].getPos();

                    // 检查玩家是否已断线
                    if (playerRef[0].isDisconnected()) {
                        playerRef[0] = server.getPlayerManager().getPlayer(playerRef[0].getUuid());
                        if (playerRef[0] == null) { // 如果重连后仍然找不到玩家，则取消传送
                            timer.cancel();
                            if (useBossBar) server.getBossBarManager().remove(standStillBar);
                            return;
                        }
                    }

                    // 检查玩家是否移动
                    if (lastPos[0].equals(currPos)) {
                        counter[0] -= 0.25; // 未移动，倒计时继续
                    } else {
                        lastPos[0] = currPos;
                        counter[0] = standStillTime; // 移动了，重置倒计时
                    }

                    // 更新UI显示
                    if (useBossBar) {
                        standStillBar.setPercent((float) (counter[0] / standStillTime));
                    } else {
                        Text message = Text.translatable("aliveandwell.hometpaback.stilling").formatted(Formatting.YELLOW)
                                .append(Text.literal(Integer.toString((int) Math.floor(counter[0] + 1.0))).formatted(Formatting.GREEN))
                                .append(Text.translatable("aliveandwell.hometpaback.second").formatted(Formatting.GRAY));
                        playerRef[0].sendMessage(message, true);
                    }

                    // 在屏幕上显示标题和副标题
                    playerRef[0].networkHandler.sendPacket(new SubtitleS2CPacket(Text.translatable("aliveandwell.hometpaback.stilling").formatted(Formatting.BOLD, Formatting.YELLOW)));
                    playerRef[0].networkHandler.sendPacket(new TitleS2CPacket(Text.translatable("aliveandwell.hometpaback.teleporting").formatted(Formatting.GREEN, Formatting.ITALIC)));
                }
            }
        }, 0L, 250L);
    }
}