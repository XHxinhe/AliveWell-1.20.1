package com.XHxinhe.aliveandwell.mixin.aliveandwell.client.disable;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * 这是一个用于 ClientWorld.Properties (客户端世界属性) 的 Mixin。
 * 它的核心功能是强制将客户端的游戏难度锁定为“困难”（Hard），并且不允许玩家进行任何更改。
 * 它通过完全重写两个关键方法来实现这一目的：一个用于检查难度是否被锁定，另一个用于设置难度。
 * 最终效果是，无论玩家在游戏设置中选择什么难度，游戏将始终以困难模式运行，并且难度选项会被锁定。
 * 这是一种在“硬核”或“生存挑战”类Mod中常见的做法，旨在为玩家提供一个固定且充满挑战的游戏环境。
 */
@Mixin(ClientWorld.Properties.class) // @Mixin 注解，告诉处理器我们要修改原版的 ClientWorld.Properties 内部类。
public class ClientWorldPropertiesMixin {

    // @Shadow 注解，让我们能直接访问原版类中的 `difficulty` 私有字段。
    // 这个字段存储了当前客户端实际的难度设置。
    @Shadow private Difficulty difficulty;

    /**
     * @author [作者名]
     * @reason [重写原因]
     */
    // @Overwrite 注解，表示我们要用下面的方法，完全替换掉原有的 isDifficultyLocked 方法。
    // 这个方法在游戏中用于判断难度是否被锁定（例如在极限模式下）。
    @Overwrite
    public boolean isDifficultyLocked() {
        // --- 核心修改逻辑 ---
        // 无论原始状态如何，这个方法现在被硬编码为总是返回 true。
        // 这会告诉游戏UI：“难度已经被锁定了，不要让玩家更改它。”
        return true;
    }

    /**
     * @author [作者名]
     * @reason [重写原因]
     */
    // @Overwrite 注解，表示我们要用下面的方法，完全替换掉原有的 setDifficulty 方法。
    // 这个方法在玩家尝试更改游戏难度时被调用。
    @Overwrite
    public void setDifficulty(Difficulty difficulty) {
        // --- 核心修改逻辑 ---
        // 这个方法现在完全忽略了传入的 `difficulty` 参数（即玩家选择的难度）。
        // 相反，它直接将内部存储的难度字段强制设置为 Difficulty.HARD（困难）。
        // 因此，任何设置难度的尝试最终都会导致难度变为“困难”。
        this.difficulty = Difficulty.HARD;
    }

}