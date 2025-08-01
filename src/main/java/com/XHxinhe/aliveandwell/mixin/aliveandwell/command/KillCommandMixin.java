package com.XHxinhe.aliveandwell.mixin.aliveandwell.command;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.KillCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 这是一个用于 KillCommand (处理/kill指令的类) 的 Mixin。
 * 它的功能与 ClearCommandMixin 类似，都是根据一个全局配置，有条件地禁用游戏中的 `/kill` 指令。
 * 实现方式非常简洁高效：
 * 1.  它使用 `@Inject` 将代码注入到 `KillCommand` 类的 `register` 方法的开头 (`@At("HEAD")`)。这个 `register` 方法负责在服务器启动时将 `/kill` 指令注册到游戏中。
 * 2.  注入的代码会检查来自Mod主类 `AliveAndWellMain` 的一个名为 `canCreative` 的静态布尔值。
 * 3.  如果 `canCreative` 的值为 `false`，它会立即调用 `ci.cancel()`。
 * `ci.cancel()` 的作用是取消原 `register` 方法的后续执行。这意味着，如果 `canCreative` 为 `false`，`/kill` 指令的注册过程就会在刚开始时被直接中断，该指令将永远不会被注册到游戏中。因此，任何玩家或系统都将无法使用 `/kill` 指令。
 * 为什么要这样做？
 * `/kill` 指令虽然是原版生存的一部分，但在某些硬核整合包中，作者可能希望移除这种“自杀”捷径。禁用的目的可能包括：
 * -   **增加挑战性**：防止玩家在陷入绝境（如被困、饥饿）时通过自杀来快速“重置”状态或返回重生点，强迫玩家必须通过智慧和努力来解决困境。
 * -   **防止滥用**：在某些PVP或团队合作场景中，防止玩家利用 `/kill` 来逃避战斗或恶意影响游戏。
 * -   **统一规则**：通过 `canCreative` 这个全局开关，作者可以轻松地控制一系列管理/便利性指令的可用性，从而一键切换整合包的整体难度和风格。
 */
@Mixin(KillCommand.class)
public class KillCommandMixin {
    /**
     * @Inject: 注入代码到目标方法。
     * at = @At("HEAD"): 注入点在 `register` 方法的开头。
     * method = "register": 目标方法是负责注册 /kill 指令的静态方法。
     * cancellable = true: 允许我们通过 CallbackInfo (ci) 来取消原方法的执行。
     */
    @Inject(at = @At("HEAD"), method = "register", cancellable = true)
    private static void register(CommandDispatcher<ServerCommandSource> dispatcher, CallbackInfo ci) {
        // 检查来自Mod主类的全局配置开关 `canCreative`。
        if(!AliveAndWellMain.canCreative){
            // 如果 `canCreative` 为 false，则取消 /kill 指令的注册过程。
            // 这将导致该指令在游戏中完全不可用。
            ci.cancel();
        }
        // 如果 `canCreative` 为 true，则不执行任何操作，原版的注册方法会正常继续执行。
    }
}