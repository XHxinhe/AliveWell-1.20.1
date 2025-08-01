package com.XHxinhe.aliveandwell.mixin.aliveandwell.command;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.util.timeutil.WorldTimeHelper;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.argument.TimeArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TimeCommand;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * 这是一个用于 TimeCommand (处理/time指令的类) 的 Mixin。
 * 这个 Mixin 极其重要且复杂，它完全重写了原版的 `/time` 指令，并将其与一个自定义的、更高精度的时间系统深度绑定。
 * 核心改动分析：
 * 1.  **自定义时间系统**：
 *     -   代码中反复出现一个名为 `WorldTimeHelper` 的接口，并从 `world.getLevelProperties()` 中获取其实例。这表明作者在其他地方（很可能是对 `ServerWorldProperties` 的 Mixin）实现了一个自定义的时间存储机制。
 *     -   这个 `WorldTimeHelper` 接口提供了 `GetDoubleTime()` 和 `SetDoubleTime()` 方法。使用 `double` 类型来存储时间，而不是原版的 `long`，意味着作者实现了一个可以存储远超原版时间上限（约2.48亿年）的、精度可能更高的时间系统。这对于需要模拟极其漫长时间流逝的整合包来说是革命性的。
 * 2.  **完全重写的指令**：
 *     -   **条件性注册**：整个指令的注册逻辑被包裹在 `if(AliveAndWellMain.canCreative)` 中，遵循了该整合包一贯的设计哲学：在“硬核生存”模式下禁用此管理指令，仅在“创造/管理”模式下启用。
 *     -   **全新的子命令**：它废弃了原版的 `/time set day`, `/time set night`, `/time query daytime` 等子命令，替换为四个全新的、功能更具体的子命令：
 *         -   `/time setday <天数>`: 设置为指定天数的清晨（0刻）。
 *         -   `/time setdayNight <天数>`: 设置为指定天数的夜晚（13000刻）。
 *         -   `/time setdaySleeping <天数>`: 设置为指定天数的“可睡觉”时间（14900刻，推测）。
 *         -   `/time setdayMorning <天数>`: 设置为指定天数的黎明（23900刻，推测）。
 *     -   这些新指令允许管理员精确地将时间跳转到未来或过去的**某一天**的特定时刻，这比原版只能在当前24000刻循环内设置时间要强大得多。
 * 3.  **重写核心逻辑**：
 *     -   `executeSet` 方法被重写，它不再直接操作原版的时间，而是通过 `WorldTimeHelper.SetDoubleTime()` 来设置自定义的 `double` 类型时间。
 *     -   设置完自定义时间后，它会进行一次“同步”：将这个 `double` 时间转换为 `long`，再更新到原版的世界时间（`setTime` 和 `setTimeOfDay`），确保游戏内的光照、生物生成等依赖原版时间的系统能正确响应。
 *     -   `getDayTime` 方法也被重写，用于从自定义的 `double` 时间中计算出当前处于一天中的哪个时刻（对24000取模）。
 * **目的与影响**：
 * 这个 Mixin 是整合包“长时标生存”核心机制的基石。通过引入一个几乎没有上限的时间系统，作者可以实现：
 * -   **真实的季节更替与气候变化**：漫长的时间流逝是实现有意义的季节系统的先决条件。
 * -   **宏大的历史叙事**：游戏世界可以记录下极其漫长的历史，可能用于实现文明演进、遗迹风化等宏大概念。
 * -   **更强的管理能力**：新的 `/time` 指令给予了管理员前所未有的时间控制能力，可以精确地在漫长的时间线上跳转。
 * 总之，这不仅仅是对一个指令的修改，而是整个游戏时间维度的重塑，是实现整合包宏大构想的关键技术支撑。
 */
@Mixin(TimeCommand.class)
public class TimeCommandMixin {

    /**
     * @author XHxinhe(欣訸) (推断)
     * @reason 完全重写 /time 指令，使其与自定义的、基于 double 的高精度时间系统集成，并提供更强大的、可以按天数设置时间的子命令。
     */
    @Overwrite
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        // 仅在 `canCreative` 模式下注册该指令
        if(AliveAndWellMain.canCreative){
            dispatcher.register(
                    CommandManager.literal("time").requires((source) -> source.hasPermissionLevel(2))
                            .then(CommandManager.literal("setday")
                                    .then(CommandManager.argument("time", TimeArgumentType.time())
                                            // 执行逻辑：(天数-1) * 24000，即设置到第N天的开始
                                            .executes((context) -> executeSet(context.getSource(), (IntegerArgumentType.getInteger(context, "time") - 1) * 24000))
                                    )
                            )
                            .then(CommandManager.literal("setdayNight")
                                    .then(CommandManager.argument("time", TimeArgumentType.time())
                                            // 执行逻辑：(天数-1) * 24000 + 13000，即设置到第N天的夜晚
                                            .executes((context) -> executeSet(context.getSource(), (IntegerArgumentType.getInteger(context, "time") - 1) * 24000 + 13000))
                                    )
                            )
                            .then(CommandManager.literal("setdaySleeping")
                                    .then(CommandManager.argument("time", TimeArgumentType.time())
                                            // 执行逻辑：(天数-1) * 24000 + 14900，即设置到第N天的可睡觉时间
                                            .executes((context) -> executeSet(context.getSource(), (IntegerArgumentType.getInteger(context, "time") - 1) * 24000 + 14900))
                                    )
                            )
                            .then(CommandManager.literal("setdayMorning")
                                    .then(CommandManager.argument("time", TimeArgumentType.time())
                                            // 执行逻辑：(天数-1) * 24000 + 23900，即设置到第N天的黎明
                                            .executes((context) -> executeSet(context.getSource(), (IntegerArgumentType.getInteger(context, "time") - 1) * 24000 + 23900))
                                    )
                            )
            );
        }
    }

    /**
     * @author XHxinhe(欣訸) (推断)
     * @reason 重写此方法以从自定义的 `double` 时间系统中获取一天内的时刻。
     */
    @Overwrite
    private static int getDayTime(ServerWorld world) {
        // 从实现了 WorldTimeHelper 接口的世界属性中，获取 double 类型的时间，并计算一天内的时刻
        return (int)(((WorldTimeHelper)world.getLevelProperties()).GetDoubleTime() % 24000L);
    }

    /**
     * @author XHxinhe(欣訸) (推断)
     * @reason 重写核心设置逻辑，使其能够操作自定义的 `double` 时间，并同步回原版时间系统。
     */
    @Overwrite
    public static int executeSet(ServerCommandSource source, int time) {
        // 遍历服务器上的所有世界（主世界、下界、末地等）
        for (ServerWorld serverWorld : source.getServer().getWorlds()) {
            // 获取自定义时间助手的实例
            WorldTimeHelper timeHelper = ((WorldTimeHelper) serverWorld.getLevelProperties());
            // 1. 设置自定义的 double 时间
            timeHelper.SetDoubleTime(time);

            // 2. 同步到原版时间系统
            long l = (long) timeHelper.GetDoubleTime();
            ServerWorldProperties properties = (ServerWorldProperties) serverWorld.getLevelProperties();
            properties.setTime(l); // 设置总时间
            properties.getScheduledEvents().processEvents(serverWorld.getServer(), l); // 处理计划事件
            serverWorld.setTimeOfDay(l); // 设置一天中的时间（影响光照等）
        }

        // 返回设置后的一天内时刻作为指令结果
        return getDayTime(source.getWorld());
    }
}