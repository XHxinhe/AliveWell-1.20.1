package com.XHxinhe.aliveandwell.mixin.aliveandwell.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.SpreadPlayersCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * 这是一个用于 SpreadPlayersCommand (处理/spreadplayers指令的类) 的 Mixin。
 * `/spreadplayers` 指令用于将一个或多个实体随机传送分散到一个区域内，是服务器管理和举办活动（如UHC）的重要工具。
 * 这个 Mixin 的功能与 PublishCommandMixin 类似，都是通过完全重写（Overwrite）其注册方法，来有效地禁用 `/spreadplayers` 指令。
 * 这个 Mixin 做了什么？
 * 1.  它使用 `@Overwrite` 完全替换了原版的 `register` 方法。
 * 2.  在新的 `register` 方法中，它虽然重新注册了 `/spreadplayers` 指令，但做了两个关键改动：
 *     a. **移除了所有功能和参数**：原版的 `/spreadplayers` 指令需要多个参数（中心点、分散距离、最大范围、目标等）。这个 Mixin 完全移除了这些参数定义，使其变成了一个无法执行任何操作的“空壳”。
 *     b. **权限等级提升至最高**：它将执行该指令所需的权限等级从原版的2级（服务器操作员OP）提升到了4级。在原版中，4级权限默认只保留给服务器控制台，任何游戏内的玩家（包括OP）都无法达到。
 * **综合效果**：
 * 这个 Mixin 使得 `/spreadplayers` 指令对于游戏中的任何玩家都完全无法使用。尝试执行该指令会直接提示“你没有权限执行此命令”。这从根本上阻止了玩家或管理员使用该指令来传送实体。
 */
@Mixin(SpreadPlayersCommand.class)
public class SpreadPlayersCommandMixin {

    /**
     * @author XHxinhe(欣訸) (推断)
     * @reason 将 /spreadplayers 指令的权限提升至玩家无法达到的等级，并移除其所有功能，以禁用这个与核心生存玩法无关的管理指令。
     */
    @Overwrite
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        // 注册一个名为 "spreadplayers" 的字面量参数（即指令本身）。
        dispatcher.register(
                CommandManager.literal("spreadplayers")
                        // 关键修改：要求执行者必须拥有4级权限。
                        // 在游戏中，玩家（即使是OP）最高只能达到3级权限，4级权限通常为服务器控制台专属。
                        // 这使得任何玩家都无法执行此命令。
                        .requires(source -> source.hasPermissionLevel(4))
                // 注意：这里同样没有 .then() 或 .executes()，移除了指令的所有参数和功能。
        );
    }
}