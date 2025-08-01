package com.XHxinhe.aliveandwell.mixin.aliveandwell.command;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.GameModeCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Collections;

/**
 * 这是一个用于 GameModeCommand (处理/gamemode指令的类) 的 Mixin。
 * 它的功能是根据一个全局配置，有条件地“阉割”或启用 `/gamemode` 指令。
 *
 * 这个 Mixin 的实现方式非常巧妙：
 * 1.  它使用 `@Overwrite` 完全重写了 `GameModeCommand` 的 `register` 方法。
 * 2.  它首先无条件地创建了一个基础的 `/gamemode` 指令构建器。
 * 3.  然后，它检查来自Mod主类 `AliveAndWellMain` 的 `canCreative` 静态布尔值。
 * 4.  **如果 `canCreative` 为 `false`**：它会跳过添加所有子命令（如 `survival`, `creative` 等）的逻辑，直接注册一个“空”的 `/gamemode` 指令。这意味着玩家输入 `/gamemode` 会被识别，但输入 `/gamemode survival` 则会提示参数错误，从而在功能上禁用了该指令。
 * 5.  **如果 `canCreative` 为 `true`**：它会循环遍历所有的游戏模式（生存、创造、冒险、观察者），并为每一个模式重新构建和添加对应的子命令，包括为自己或为其他玩家设置游戏模式的完整功能。这部分代码实际上是原版注册逻辑的完整复现。
 *
 * 这种设计的目的与之前的指令 Mixin 一脉相承，都是为了通过一个全局开关来控制核心游戏指令的可用性，从而塑造整合包的特定玩法和难度。禁用 `/gamemode` 是防止作弊、维持纯粹生存体验的最直接手段之一。
 *
 * 通过这种方式，作者可以轻松地发布一个“严格生存版”（`canCreative = false`）和一个“自由/管理版”（`canCreative = true`）的整合包，而无需维护两套不同的代码。
 */
@Mixin(GameModeCommand.class)
public class GameModeCommandMixin {

    /**
     * @author XHxinhe(欣訸) (推断)
     * @reason 根据全局配置 `canCreative` 来决定是否启用 /gamemode 指令的完整功能。
     */
    @Overwrite
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        // 1. 创建一个基础的 /gamemode 指令构建器，并设置权限等级为2 (OP)。
        // 这一步是无条件的，所以 /gamemode 这个根指令总是存在的。
        LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = CommandManager.literal("gamemode").requires(source -> source.hasPermissionLevel(2));

        // 2. 检查全局配置开关。
        if(AliveAndWellMain.canCreative){
            // 3. 如果 `canCreative` 为 true，则为 /gamemode 添加所有子命令。
            for (GameMode gameMode : GameMode.values()) {
                // 为每个游戏模式（如 "survival", "creative"）创建一个子命令。
                literalArgumentBuilder.then(
                        // e.g., /gamemode survival
                        CommandManager.literal(gameMode.getName())
                                // 处理对自身执行的情况
                                .executes(context -> execute(context, Collections.singleton(((ServerCommandSource)context.getSource()).getPlayerOrThrow()), gameMode))
                                // 添加 "target" 参数，处理对其他玩家执行的情况
                                // e.g., /gamemode survival @a
                                .then(CommandManager.argument("target", EntityArgumentType.players())
                                        .executes(context -> execute(context, EntityArgumentType.getPlayers(context, "target"), gameMode))
                                )
                );
            }
        }

        // 4. 注册最终构建好的指令。
        // 如果 `canCreative` 为 false，这里注册的是一个没有子命令的“空壳”指令。
        // 如果为 true，这里注册的是功能完整的 /gamemode 指令。
        dispatcher.register(literalArgumentBuilder);
    }

    // @Shadow: 引用原版类中负责执行切换游戏模式的核心逻辑的私有静态方法。
    // 因为 @Overwrite 的方法需要调用它，所以必须用 @Shadow 引用。
    @Shadow
    private static int execute(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> targets, GameMode gameMode) {
        int i = 0;
        for (ServerPlayerEntity serverPlayerEntity : targets) {
            if (!serverPlayerEntity.changeGameMode(gameMode)) continue;
            sendFeedback(context.getSource(), serverPlayerEntity, gameMode);
            ++i;
        }
        return i;
    }

    // @Shadow: 引用原版类中负责向玩家发送反馈消息的私有静态方法。
    @Shadow
    private static void sendFeedback(ServerCommandSource source, ServerPlayerEntity player, GameMode gameMode) {
        MutableText text = Text.translatable("gameMode." + gameMode.getName());
        if (source.getEntity() == player) {
            source.sendFeedback(() -> Text.translatable("commands.gamemode.success.self", text), true);
        } else {
            if (source.getWorld().getGameRules().getBoolean(GameRules.SEND_COMMAND_FEEDBACK)) {
                player.sendMessage(Text.translatable("gameMode.changed", text));
            }
            source.sendFeedback(() -> Text.translatable("commands.gamemode.success.other", player.getDisplayName(), text), true);
        }
    }
}