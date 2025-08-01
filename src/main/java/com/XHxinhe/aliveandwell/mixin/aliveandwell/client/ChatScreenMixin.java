package com.XHxinhe.aliveandwell.mixin.aliveandwell.client;

import com.XHxinhe.aliveandwell.miningsblock.MiningPlayers;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 这是一个用于 ChatScreen (聊天屏幕) 的 Mixin。
 * 它的核心功能是作为一个“后备”或“补充”机制，确保在死亡惩罚期间，即使玩家打开了聊天框，死亡倒计时依然会继续，并且在倒计时结束后能正确地强制重生。
 *
 * 在之前的 `DeathScreenMixin` 中，我们看到倒计时逻辑被注入到了死亡屏幕的 `tick` 方法里。然而，当玩家从死亡屏幕打开聊天屏幕时，游戏就不再为死亡屏幕调用 `tick` 方法了，而是为聊天屏幕调用。
 * 如果没有这个 Mixin，玩家就可以通过打开聊天框来“暂停”死亡倒计时，从而绕过死亡惩罚。
 *
 * 因此，这个 Mixin 将完全相同的倒计时和强制重生逻辑复制到了聊天屏幕的 `tick` 方法中，保证了无论玩家是在死亡屏幕等待，还是在死亡时打开聊天框，死亡惩罚计时器都能持续、稳定地运作。
 */
@Mixin(ChatScreen.class) // @Mixin 注解，告诉处理器我们要修改原版的 ChatScreen 类。
public abstract class ChatScreenMixin extends Screen {
    protected ChatScreenMixin(Text title) {
        super(title);
    }

    // @Inject 注解，表示我们要向原版方法中注入代码。
    // at = @At("HEAD"): 注入点在 `tick` 方法的开头。
    // method = "tick": 目标方法是 `tick`，该方法在聊天屏幕打开时每游戏刻调用一次。
    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo ci) {
        // --- 核心逻辑 (与 DeathScreenMixin 中的逻辑完全相同) ---

        // 检查玩家是否存在且处于死亡状态。
        if (this.client != null && this.client.player != null && this.client.player.isDead()) {
            // 如果是，就将自定义的死亡计时器 `timeDead` 减 1。
            MiningPlayers.timeDead--;
        }

        // 再次检查玩家是否死亡。
        if (this.client != null && this.client.player != null && this.client.player.isDead()) {
            // 检查计时器是否已经归零（或更少）。
            if (MiningPlayers.timeDead / 20 <= 0) { // 使用 <= 0 更为稳妥
                // 如果计时器结束，就向服务器发送重生请求，强制玩家重生。
                this.client.player.requestRespawn();
                // 重置计时器，以备下次死亡时使用。
                MiningPlayers.timeDead = 2400;
            }
        }
    }
}