package com.XHxinhe.aliveandwell.mixin.aliveandwell.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ExecuteCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * 这是一个用于 ExecuteCommand (处理/execute指令的类) 的 Mixin。
 * 它的目的非常激进和明确：通过完全重写（Overwrite）其注册方法，来“阉割”掉整个 `/execute` 指令。
 * 在原版游戏中，`/execute` 是一个功能极其强大的指令，它允许指令执行者以其他实体、在其他位置、或其他条件下执行几乎任何其他指令。它拥有非常复杂的子命令结构（如 `as`, `at`, `if`, `run` 等）。
 * 这个 Mixin 做了什么？
 * 1.  它使用 `@Overwrite` 完全替换了原版的 `register` 方法。
 * 2.  在新的 `register` 方法中，它只注册了一个最基础的 `/execute` 根命令。
 * 3.  这个根命令没有任何子命令（没有 `as`, `at`, `if`, `run` 等等），因此它什么也做不了。
 * 最终效果是，服务器上虽然还存在 `/execute` 这个指令（输入时会有提示），但它变成了一个“空壳”，无法接受任何有效的参数，从而在功能上被完全禁用了。
 * 为什么要这样做？
 * `/execute` 指令是服务器管理和地图制作的核心工具，但同时也可能被滥用，或者作者希望创造一个无法使用这种高级指令的、更纯粹的生存环境。通过禁用 `/execute`，作者可以：
 * -   **大幅提升游戏难度**：防止玩家或OP通过复杂的指令组合来绕过游戏机制或作弊。
 * -   **简化游戏体验**：对于不希望玩家接触复杂指令系统的整合包，这是一种彻底的解决方案。
 * -   **增强服务器安全性**：在某些情况下，限制 `/execute` 的使用可以防止潜在的服务器滥用行为。
 * 这是一个非常强硬地修改，它从根本上改变了Minecraft指令系统的一个核心功能。
 */
@Mixin(ExecuteCommand.class)
public class ExecuteCommandMixin {
    /**
     * @author XHxinhe(欣訸) (推断)
     * @reason 禁用功能过于强大的 /execute 指令，以增加整合包的挑战性或防止滥用。
     */
    @Overwrite
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
        // 注册一个名为 "execute" 的字面量参数（即指令本身）。
        dispatcher.register(CommandManager.literal("execute")
                        // 要求执行者必须拥有2级权限（通常是服务器操作员OP）。
                        .requires(source -> source.hasPermissionLevel(2))
                // 关键点：这里没有链接任何 .then() 或 .executes() 方法。
                // 这意味着这个指令的定义到此为止，它后面不能跟任何子命令或执行任何操作。
                // 因此，该指令在功能上被完全禁用了。
        );
    }
}