package com.XHxinhe.aliveandwell.mixin.aliveandwell.command;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TeleportCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 这是一个用于 TeleportCommand (处理/teleport和/tp指令的类) 的 Mixin。
 * 它的功能与 KillCommandMixin 和 ClearCommandMixin 类似，都是根据一个全局配置，有条件地禁用游戏中的传送指令。
 * 实现方式非常简洁高效：
 * 1.  它使用 `@Inject` 将代码注入到 `TeleportCommand` 类的 `register` 方法的开头 (`@At("HEAD")`)。这个 `register` 方法负责在服务器启动时将 `/teleport` 和 `/tp` 指令注册到游戏中。
 * 2.  注入的代码会检查来自Mod主类 `AliveAndWellMain` 的一个名为 `canCreative` 的静态布尔值。
 * 3.  如果 `canCreative` 的值为 `false`，它会立即调用 `ci.cancel()`。
 * `ci.cancel()` 的作用是取消原 `register` 方法的后续执行。这意味着，如果 `canCreative` 为 `false`，`/teleport` 和 `/tp` 指令的注册过程就会在刚开始时被直接中断，这两个指令将永远不会被注册到游戏中。因此，任何玩家或系统都将无法使用传送功能。
 */
@Mixin(TeleportCommand.class)
public class TeleportCommandMixin {
    /**
     * @Inject: 注入代码到目标方法。
     * at = @At("HEAD"): 注入点在 `register` 方法的开头。
     * method = "register": 目标方法是负责注册 /teleport 和 /tp 指令的静态方法。
     * cancellable = true: 允许我们通过 CallbackInfo (ci) 来取消原方法的执行。
     */
    @Inject(at = @At("HEAD"), method = "register", cancellable = true)
    private static void register(CommandDispatcher<ServerCommandSource> dispatcher, CallbackInfo ci) {
        // 检查来自Mod主类的全局配置开关 `canCreative`。
        if(!AliveAndWellMain.canCreative){
            // 如果 `canCreative` 为 false，则取消 /teleport 和 /tp 指令的注册过程。
            // 这将导致这两个指令在游戏中完全不可用。
            ci.cancel();
        }
        // 如果 `canCreative` 为 true，则不执行任何操作，原版的注册方法会正常继续执行。
    }
}