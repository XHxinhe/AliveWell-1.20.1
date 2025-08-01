package com.XHxinhe.aliveandwell.mixin.aliveandwell.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.GiveCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * 这是一个用于 GiveCommand (处理/give指令的类) 的 Mixin。
 * 它的功能与 ExecuteCommandMixin 类似，都是通过完全重写（Overwrite）其注册方法，来彻底“阉割”掉整个 `/give` 指令。
 * 在原版游戏中，`/give` 指令是管理员和地图制作者的核心工具，允许将任何物品给予任何玩家。其完整的语法是 `/give <targets> <item> [count]`。
 * 这个 Mixin 做了什么？
 * 1.  它使用 `@Overwrite` 完全替换了原版的 `register` 方法。
 * 2.  在新的 `register` 方法中，它只注册了一个最基础的 `/give` 根命令，并设置了权限要求。
 * 3.  关键在于，它完全省略了原版中用于定义 `<targets>`, `<item>`, `[count]` 等后续参数的 `.then(...)` 逻辑。
 * 最终效果是，服务器上虽然还存在 `/give` 这个指令（输入时会有提示），但它变成了一个“空壳”。任何尝试在 `/give` 后面添加玩家名或物品名的行为都会导致“参数错误”的提示，因为这个指令的定义里已经不存在这些参数了。因此，`/give` 指令在功能上被完全禁用了。
 * 为什么要这样做？
 * `/give` 是最直接的“作弊”指令。在一个强调生存、探索和资源收集的整合包中，禁用 `/give` 是维持游戏挑战性和经济平衡的根本性措施。通过移除这个指令，作者可以确保：
 * -   **杜绝物品作弊**：所有玩家（包括OP）都无法通过指令凭空创造物品，必须通过游戏内机制（如合成、交易、探索）来获取。
 * -   **强化生存体验**：强制玩家依赖游戏世界本身来获取资源，这正是硬核生存整合包的核心乐趣所在。
 * -   **统一游戏规则**：确保了服务器的规则对于所有人都一样，没有人可以利用OP权限来绕过资源限制。
 * 这是一个非常直接且有效的修改，旨在从根本上巩固整合包的硬核生存主题。
 */
@Mixin(GiveCommand.class)
public class GiveCommandMixin {

    /**
     * @author XHxinhe(欣訸) (推断)
     * @reason 彻底禁用 /give 指令，以防止玩家或管理员通过指令获取物品，从而保证整合包的生存挑战性。
     */
    @Overwrite
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
        // 注册一个名为 "give" 的字面量参数（即指令本身）。
        dispatcher.register(
                CommandManager.literal("give")
                        // 要求执行者必须拥有2级权限（通常是服务器操作员OP）。
                        .requires((source) -> source.hasPermissionLevel(2))
                // 关键点：这里没有链接任何 .then() 方法来定义后续的参数（如玩家、物品、数量）。
                // 这意味着这个指令的定义到此为止，它是一个无法接收任何参数的“空壳”指令。
        );
    }
}