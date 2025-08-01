package com.XHxinhe.aliveandwell.mixin.aliveandwell.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.SpawnPointCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * 这是一个用于 SpawnPointCommand (处理/spawnpoint指令的类) 的 Mixin。
 * 它的功能与 GiveCommandMixin 和 ExecuteCommandMixin 类似，都是通过完全重写（Overwrite）其注册方法，来彻底“阉割”掉整个 `/spawnpoint` 指令。
 * 在原版游戏中，`/spawnpoint` 指令允许拥有权限的玩家为自己或他人设置重生点，其完整语法通常是 `/spawnpoint [targets] [pos] [angle]`。
 * 这个 Mixin 做了什么？
 * 1.  它使用 `@Overwrite` 完全替换了原版的 `register` 方法。
 * 2.  在新的 `register` 方法中，它只注册了一个最基础的 `/spawnpoint` 根命令，并设置了权限要求。
 * 3.  它完全省略了原版中用于定义 `[targets]`, `[pos]`, `[angle]` 等后续参数以及执行逻辑的 `.then(...)` 和 `.executes(...)` 部分。
 * 最终效果是，服务器上虽然还存在 `/spawnpoint` 这个指令（输入时会有提示），但它变成了一个“空壳”。任何尝试执行该指令（无论是否带参数）的行为都会导致“参数错误”或“未知指令”的提示，因为这个指令的定义里已经没有任何有效的用法了。因此，`/spawnpoint` 指令在功能上被完全禁用了。
 * 为什么要这样做？
 * 禁用 `/spawnpoint` 是一个强化生存挑战的重要手段。
 * -   **增加死亡惩罚**：玩家无法再随意设置重生点来降低探索风险。死亡后必须从世界重生点或上一次睡过的床重生，这大大增加了远距离探索的风险和挑战性。
 * -   **强化核心机制**：此举强迫玩家必须依赖游戏内的核心机制——床——来管理自己的重生位置，而不是使用便利的指令。
 * -   **防止滥用**：防止玩家在攻略高难度区域（如要塞、末地城）时，通过在门口设置重生点来无限次地“磨死”怪物，从而破坏了游戏的设计。
 * 这一修改与该整合包中其他被禁用的指令一脉相承，共同构建了一个规则严格、挑战性强的硬核生存环境。
 */
@Mixin(SpawnPointCommand.class)
public class SpawnPointCommandMixin {
    /**
     * @author XHxinhe(欣訸) (推断)
     * @reason 彻底禁用 /spawnpoint 指令，以增加死亡惩罚，强迫玩家依赖游戏内机制（床）来设置重生点。
     */
    @Overwrite
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        // 注册一个名为 "spawnpoint" 的字面量参数（即指令本身）。
        dispatcher.register(
                CommandManager.literal("spawnpoint")
                        // 要求执行者必须拥有2级权限（通常是服务器操作员OP）。
                        .requires(source -> source.hasPermissionLevel(2))
                // 关键点：这里没有链接任何 .then() 或 .executes() 方法来定义指令的参数和功能。
                // 这使得该指令虽然存在，但没有任何有效的语法，从而在功能上被完全禁用。
        );
    }
}