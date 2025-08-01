package com.XHxinhe.aliveandwell.hometpaback;

import com.XHxinhe.aliveandwell.hometpaback.util.IStoreHome;
import com.XHxinhe.aliveandwell.hometpaback.util.TeleportUtils;
import com.XHxinhe.aliveandwell.util.config.CommonConfig;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

/**
 * 处理 `/back` 和 `/setback` 命令的逻辑。
 * <p>
 * 该类负责注册和实现返回上一个位置的功能。
 * - `/back`: 将玩家传送回他们上次传送前的位置。
 * - `/setback`: 手动设置当前的返回点。
 * 功能包括：传送冷却、经验消耗（基于游戏天数动态计算）、跨维度传送限制等。
 */
public class Back {
    private final Map<UUID, Long> commandCooldowns = new HashMap<>();
    private final int xpCostPerDay;
    private static int teleportCooldown;

    public Back() {
        this.xpCostPerDay = CommonConfig.xptime;
        teleportCooldown = CommonConfig.tptime;
    }

    /**
     * 注册 `/back` 和 `/setback` 命令。
     */
    public void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LiteralArgumentBuilder<ServerCommandSource> backCommand = CommandManager.literal("back")
                    .requires(ServerCommandSource::isExecutedByPlayer)
                    .executes(this::executeBack);
            dispatcher.register(backCommand);

            LiteralArgumentBuilder<ServerCommandSource> setbackCommand = CommandManager.literal("setback")
                    .requires(ServerCommandSource::isExecutedByPlayer)
                    .executes(this::executeSetBack);
            dispatcher.register(setbackCommand);
        });
    }

    /**
     * 检查玩家是否处于命令冷却中。
     * @param player 要检查的玩家。
     * @return 如果处于冷却中，则返回 true 并向玩家发送提示消息；否则返回 false。
     */
    private boolean isOnCooldown(ServerPlayerEntity player) {
        if (commandCooldowns.containsKey(player.getUuid())) {
            long secondsSinceLastUse = Instant.now().getEpochSecond() - commandCooldowns.get(player.getUuid());
            if (secondsSinceLastUse < teleportCooldown) {
                long remaining = teleportCooldown - secondsSinceLastUse;
                player.sendMessage(Text.translatable("aliveandwell.hometpaback.cooldowntime",
                        Text.literal(String.valueOf(remaining)).append(Text.translatable("aliveandwell.hometpaback.second"))
                ).formatted(Formatting.RED), false);
                return true;
            }
        }
        return false;
    }

    /**
     * 计算传送所需的经验值。
     * 经验值基于游戏内的天数动态增长，但有上限。
     * @param world 玩家所在的世界。
     * @return 所需的经验点数。
     */
    private int calculateXpCost(World world) {
        int cost = ((int)(world.getTimeOfDay() / 24000L) + 1) * Math.max(0, this.xpCostPerDay);
        return Math.min(cost, 1000); // 经验消耗上限为1000
    }

    /**
     * 执行 `/back` 命令的核心逻辑。
     */
    private int executeBack(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
        IStoreHome homeData = (IStoreHome) player;
        Pair<Vec3d, RegistryKey<World>> backLocation = homeData.getBack();

        if (backLocation == null || backLocation.getLeft() == null || backLocation.getRight() == null) {
            player.sendMessage(Text.translatable("aliveandwell.hometpaback.nosetback").formatted(Formatting.RED), false);
            return 0;
        }

        int requiredXp = calculateXpCost(player.getWorld());
        if (player.totalExperience < requiredXp) {
            player.sendMessage(Text.translatable("aliveandwell.hometpaback.xpno",
                    Text.translatable("aliveandwell.hometpaback.xpneed"),
                    Text.literal(String.valueOf(requiredXp)),
                    Text.translatable("aliveandwell.hometpaback.xpcount")
            ).formatted(Formatting.RED), false);
            return 0;
        }

        // 传送限制：通常只允许在同维度内传送，但对特定模组维度（如暮色森林）放开限制
        RegistryKey<World> currentDim = player.getWorld().getRegistryKey();
        RegistryKey<World> targetDim = backLocation.getRight();
        String currentDimNs = currentDim.getValue().getNamespace();
        String targetDimNs = targetDim.getValue().getNamespace();

        boolean isAllowedCrossDim = currentDimNs.equals(targetDimNs) && (currentDimNs.equals("minecells") || currentDimNs.equals("twilightforest"));
        if (!currentDim.equals(targetDim) && !isAllowedCrossDim) {
            ctx.getSource().sendError(Text.translatable("aliveandwell.hometpaback.nosameworld"));
            return 0;
        }

        if (isOnCooldown(player)) {
            return 1;
        }

        // 执行带延迟的传送
        TeleportUtils.genericTeleport(true, 3.0, player, () -> {
            // 在传送前再次检查条件
            int finalRequiredXp = calculateXpCost(player.getWorld());
            if (player.totalExperience < finalRequiredXp) {
                ctx.getSource().sendError(Text.translatable("aliveandwell.hometpaback.xpno1",
                        Text.literal(String.valueOf(finalRequiredXp)),
                        Text.translatable("aliveandwell.hometpaback.notp")
                ));
                return;
            }

            ServerWorld targetWorld = Objects.requireNonNull(ctx.getSource().getServer().getWorld(targetDim));
            Vec3d pos = backLocation.getLeft();
            player.teleport(targetWorld, pos.x, pos.y, pos.z, player.getYaw(), player.getPitch());

            player.addExperience(-finalRequiredXp);
            commandCooldowns.put(player.getUuid(), Instant.now().getEpochSecond());
            player.sendMessage(Text.translatable("aliveandwell.hometpaback.xpcost",
                    Text.literal(String.valueOf(finalRequiredXp)),
                    Text.translatable("aliveandwell.hometpaback.xpcount")
            ).formatted(Formatting.GOLD), false);
        });
        return 1;
    }

    /**
     * 执行 `/setback` 命令的核心逻辑。
     */
    private int executeSetBack(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
        IStoreHome homeData = (IStoreHome) player;

        homeData.addBack(player.getPos(), player.getWorld().getRegistryKey());

        String posString = String.format("(%d, %d, %d)", player.getBlockPos().getX(), player.getBlockPos().getY(), player.getBlockPos().getZ());
        String dimString = player.getWorld().getRegistryKey().getValue().getPath();
        player.sendMessage(Text.translatable("aliveandwell.hometpaback.setback")
                .append(Text.literal(String.format(" %s [%s]", posString, dimString)))
                .formatted(Formatting.GRAY), false);
        return 1;
    }
}