package com.XHxinhe.aliveandwell.mixin.aliveandwell.command;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.WeatherCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 这是一个用于 WeatherCommand (处理/weather指令的类) 的 Mixin。
 * 它的功能与 TeleportCommandMixin、KillCommandMixin 等类似，都是根据一个全局配置，有条件地禁用游戏中的 `/weather` 指令。
 * 实现方式非常简洁高效：
 * 1.  它使用 `@Inject` 将代码注入到 `WeatherCommand` 类的 `register` 方法的开头 (`@At("HEAD")`)。这个 `register` 方法负责在服务器启动时将 `/weather` 指令注册到游戏中。
 * 2.  注入的代码会检查来自Mod主类 `AliveAndWellMain` 的一个名为 `canCreative` 的静态布尔值。
 * 3.  如果 `canCreative` 的值为 `false`，它会立即调用 `ci.cancel()`。
 * `ci.cancel()` 的作用是取消原 `register` 方法的后续执行。这意味着，如果 `canCreative` 为 `false`，`/weather` 指令的注册过程就会在刚开始时被直接中断，该指令将永远不会被注册到游戏中。因此，任何玩家或系统都将无法使用该指令来改变天气。
 * 为什么要这样做？
 * 在一个强调真实感和生存挑战的整合包中，天气是一个重要的、不可预测的环境因素。禁用 `/weather` 指令是为了：
 * -   **强化环境挑战**：天气（如下雨、雷暴）会直接影响游戏玩法，例如扑灭火焰、影响光照、生成更危险的生物（闪电苦力怕）等。移除手动控制天气的能力，强迫玩家必须适应和应对自然发生的天气变化，而不是简单地将其关闭。
 * -   **增加沉浸感**：一个无法被玩家意志所左右的自然天气系统，能极大地增强游戏世界的真实感和沉浸感。
 * -   **防止滥用**：防止管理员为了便利而频繁使用 `/weather clear`，从而削弱了为所有玩家设计的生存挑战。
 * -   **统一的设计哲学**：通过 `canCreative` 这个全局开关，作者可以一键切换整合包的整体难度。在“硬核生存”模式下，天气是自然的一部分；在“创造/管理”模式下，管理员则可以自由控制它。
 */
@Mixin(WeatherCommand.class)
public class WeatherCommandMixin {
    /**
     * @Inject: 注入代码到目标方法。
     * at = @At("HEAD"): 注入点在 `register` 方法的开头。
     * method = "register": 目标方法是负责注册 /weather 指令的静态方法。
     * cancellable = true: 允许我们通过 CallbackInfo (ci) 来取消原方法的执行。
     */
    @Inject(
            at = @At("HEAD"),
            method = "register",
            cancellable = true
    )
    private static void register(CommandDispatcher<ServerCommandSource> dispatcher, CallbackInfo ci) {
        // 检查来自Mod主类的全局配置开关 `canCreative`。
        if (!AliveAndWellMain.canCreative) {
            // 如果 `canCreative` 为 false，则取消 /weather 指令的注册过程。
            // 这将导致该指令在游戏中完全不可用。
            ci.cancel();
        }
        // 如果 `canCreative` 为 true，则不执行任何操作，原版的注册方法会正常继续执行。
    }
}