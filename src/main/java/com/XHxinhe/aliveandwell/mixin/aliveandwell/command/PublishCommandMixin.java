package com.XHxinhe.aliveandwell.mixin.aliveandwell.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.PublishCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * 这是一个用于 PublishCommand (处理/publish指令的类) 的 Mixin。
 * `/publish` 指令用于在单人游戏中将世界开放到局域网（LAN），允许其他玩家加入。
 * 这个 Mixin 的功能极其明确和强硬：通过完全重写（Overwrite）其注册方法，来有效地禁用 `/publish` 指令。
 * 这个 Mixin 做了什么？
 * 1.  它使用 `@Overwrite` 完全替换了原版的 `register` 方法。
 * 2.  在新的 `register` 方法中，它虽然重新注册了 `/publish` 指令，但做了两个关键改动：
 *     a. **移除了所有功能**：它没有添加任何执行逻辑（`.executes(...)`）或子参数，这意味着这个指令本身什么也做不了，变成了一个“空壳”。
 *     b. **权限等级提升至最高**：它将执行该指令所需的权限等级从原版的4级（默认就是4级，但这里是显式重申）设定为4级。在原版中，4级权限默认只保留给服务器控制台，任何游戏内的玩家（包括OP）都无法达到。
 * **综合效果**：
 * 这个 Mixin 使得 `/publish` 指令对于游戏中的任何玩家（包括作为房主的操作员）都完全无法使用。尝试执行该指令会直接提示“你没有权限执行此命令”。这从根本上阻止了玩家将单人游戏世界开放给局域网。
 * **为什么要这样做？**
 * 这是作者为了强制维持一个纯粹的、不受外界干扰的单人游戏体验而采取的极端措施。
 * -   **杜绝外部援助**：在一个高难度的生存整合包中，玩家可能会试图通过 `/publish` 让朋友加入来获得帮助（例如，帮忙打Boss、送物资等），这会破坏作者精心设计的挑战。禁用此指令可以杜绝这种“作弊”途径。
 * -   **保证整合包的完整性**：确保玩家体验到的是作者预设的、纯粹的单人游戏流程，不会因为其他玩家的加入而改变游戏进程或平衡。
 * -   **设计哲学的一致性**：这与该整合包中其他被禁用的指令（如 `/give`, `/gamemode`）目的一致，都是为了创造一个无法被轻易绕过规则的、硬核的生存环境。
 */
@Mixin(PublishCommand.class)
public class PublishCommandMixin {
    /**
     * @author XHxinhe(欣訸) (推断)
     * @reason 将 /publish 指令的权限提升至玩家无法达到的等级，从而彻底禁用它，以强制维持纯粹的单人游戏体验。
     */
    @Overwrite
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        // 注册一个名为 "publish" 的字面量参数（即指令本身）。
        dispatcher.register(
                CommandManager.literal("publish")
                        // 关键修改：要求执行者必须拥有4级权限。
                        // 在游戏中，玩家（即使是OP）最高只能达到3级权限，4级权限通常为服务器控制台专属。
                        // 这使得任何玩家都无法执行此命令。
                        .requires((source) -> source.hasPermissionLevel(4))
                // 注意：这里同样没有 .executes() 或 .then()，进一步确保了该指令是一个没有实际功能的空壳。
        );
    }
}