package com.XHxinhe.aliveandwell.mixin.aliveandwell.command;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ClearCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 这是一个用于 ClearCommand (处理/clear指令的类) 的 Mixin。
 * 它的功能是根据一个全局配置，有条件地禁用游戏中的 `/clear` 指令。
 * 核心逻辑非常直接：
 * 1.  它注入到 `ClearCommand` 类的 `register` 方法的开头。这个 `register` 方法负责在服务器启动时将 `/clear` 指令注册到指令分发器中，从而让玩家和命令方块可以使用它。
 * 2.  在注入的代码中，它会检查来自Mod主类 `AliveAndWellMain` 的一个名为 `canCreative` 的静态布尔值。
 * 3.  如果 `canCreative` 的值为 `false`，它会立即调用 `ci.cancel()`。
 * `ci.cancel()` 的作用是取消原 `register` 方法的后续执行。这意味着，如果 `canCreative` 为 `false`，`/clear` 指令的注册过程就会在开始时被直接中断，该指令将永远不会被注册到游戏中。因此，任何玩家或系统都将无法使用 `/clear` 指令，尝试执行会提示“未知指令”。
 * 这种设计的目的通常是为了增强服务器的管理或提升特定游戏模式的挑战性。例如，在一个严格的生存整合包中，作者可能不希望管理员或玩家（即使是OP）能够轻易地使用 `/clear` 指令来清空物品栏，以防止作弊或滥用。通过一个全局的 `canCreative` 开关，作者可以轻松地控制包括 `/clear` 在内的一系列“创造模式”或“管理员”相关的指令是否可用，从而动态调整整合包的整体难度和规则。
 */
@Mixin(ClearCommand.class)
public class ClearCommandMixin {
    /**
     * @Inject: 注入代码到目标方法。
     * at = @At("HEAD"): 注入点在 `register` 方法的开头。
     * method = "register": 目标方法是负责注册 /clear 指令的静态方法。
     * cancellable = true: 允许我们取消原方法的执行。
     */
    @Inject(at = @At("HEAD"), method = "register", cancellable = true)
    private static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CallbackInfo ci) {
        // 检查来自Mod主类的全局配置开关 `canCreative`。
        if(!AliveAndWellMain.canCreative){
            // 如果 `canCreative` 为 false，则取消 /clear 指令的注册过程。
            ci.cancel();
        }
        // 如果 `canCreative` 为 true，则不执行任何操作，原版的注册方法会正常继续执行。
    }
}