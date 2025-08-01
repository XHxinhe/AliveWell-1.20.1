package com.XHxinhe.aliveandwell.mixin.aliveandwell.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.GameRuleCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * 这是一个用于 GameRuleCommand (处理/gamerule指令的类) 的 Mixin。
 * 它的功能极其激进：通过完全重写（Overwrite）其注册方法，来彻底“阉割” `/gamerule` 指令，使其几乎无法使用，并且功能被严格限制。
 *
 * 这个 Mixin 实现了两个核心的、颠覆性的改动：
 *
 * 1.  **权限等级提升至最高**：
 *     它将 `/gamerule` 指令的所需权限等级从原版的2级（服务器操作员OP）提升到了4级。在原版中，4级权限默认只保留给服务器控制台本身，任何游戏内的玩家（包括OP）都无法达到。这意味着，这个指令对于所有玩家来说都将是“权限不足”而无法使用的。
 *
 * 2.  **功能能被……限制操作“死亡不掉落”**：
 *     原版的 `/gamerule` 指令会遍历并注册游戏中所有的游戏规则（如 `doDaylightCycle`, `mobGriefing` 等）。而这个 Mixin 的代码虽然也遍历了所有规则，但在循环内部，它忽略了当前遍历到的规则，而是**硬编码**地、重复地只添加 `keepInventory` (死亡不掉落) 这一条规则。最终结果是，这个被修改后的 `/gamerule` 指令将只拥有一个子命令：`keepInventory`。所有其他的游戏规则都无法通过指令来查询或修改。
 *
 * **综合效果**：
 * 这个 Mixin 几乎完全禁用了 `/gamerule` 指令。它不仅让所有玩家都无法使用该指令，而且即便有人通过特殊手段获得了4级权限，他也只能用这个指令来控制“死亡不掉落”这一项规则，而无法影响游戏的其他任何方面。
 *
 * **目的推断**：
 * 这是作者为了极度强化其整合包“硬核生存”主题而采取的终极措施。通过废掉 `/gamerule`，作者确保了服务器的核心规则（如昼夜更替、怪物破坏方块等）是固定且无法被管理员轻易修改的，从而保证了所有玩家都能体验到作者预设的、统一且充满挑战的游戏环境。这是一种非常强硬的设计哲学，旨在根除任何可能降低游戏难度的“后门”。
 */
@Mixin(GameRuleCommand.class)
public class GameRuleCommandMixin {
    /**
     * @author XHxinhe(欣訸) (推断)
     * @reason 彻底禁用 /gamerule 指令的大部分功能，并将其权限提升至最高，以固化游戏核心规则，防止被修改。
     */
    @Overwrite
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        // 1. 创建 /gamerule 根指令，并将权限等级设置为4级（仅服务器控制台可用）。
        final LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = CommandManager.literal("gamerule").requires(source -> source.hasPermissionLevel(4));

        // 2. 使用 GameRules.Visitor 遍历所有游戏规则。
        GameRules.accept(new GameRules.Visitor(){
            @Override
            public <T extends GameRules.Rule<T>> void visit(GameRules.Key<T> key, GameRules.Type<T> type) {
                // 3. 核心修改点：无论当前遍历到哪个规则 (key)，都只添加 "keepInventory" 这一条子命令。
                // 这段代码会为每一个游戏规则都尝试添加一次 "keepInventory"，但由于名称重复，最终只会成功添加一次。
                literalArgumentBuilder.then(
                        // 硬编码使用 GameRules.KEEP_INVENTORY.getName()，即 "keepInventory"
                        CommandManager.literal(GameRules.KEEP_INVENTORY.getName())
                                // 设置查询逻辑: /gamerule keepInventory
                                .executes(context -> executeQuery(context.getSource(), GameRules.KEEP_INVENTORY))
                                // 设置修改逻辑: /gamerule keepInventory <value>
                                .then(type.argument("value")
                                        .executes(context -> executeSet(context, GameRules.KEEP_INVENTORY))
                                )
                );
            }
        });

        // 4. 注册这个被大幅修改过的指令。
        dispatcher.register(literalArgumentBuilder);
    }

    // @Shadow: 引用原版类中负责“设置”游戏规则值的私有静态方法。
    @Shadow
    static <T extends GameRules.Rule<T>> int executeSet(CommandContext<ServerCommandSource> context, GameRules.Key<T> key) {
        ServerCommandSource serverCommandSource = context.getSource();
        T rule = serverCommandSource.getServer().getGameRules().get(key);
        rule.set(context, "value");
        serverCommandSource.sendFeedback(() -> Text.translatable("commands.gamerule.set", key.getName(), rule.toString()), true);
        return rule.getCommandResult();
    }

    // @Shadow: 引用原版类中负责“查询”游戏规则值的私有静态方法。
    @Shadow
    static <T extends GameRules.Rule<T>> int executeQuery(ServerCommandSource source, GameRules.Key<T> key) {
        T rule = source.getServer().getGameRules().get(key);
        source.sendFeedback(() -> Text.translatable("commands.gamerule.query", key.getName(), rule.toString()), false);
        return rule.getCommandResult();
    }
}