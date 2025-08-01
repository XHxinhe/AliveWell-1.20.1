package com.XHxinhe.aliveandwell.mixin.aliveandwell.client;

import com.XHxinhe.aliveandwell.miningsblock.MiningPlayers;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 这是一个用于 ClientPlayerEntity (客户端玩家实体) 的 Mixin，它实现了两个非常不同的功能。
 *
 * 1.  **死亡倒计时冗余**：它将死亡惩罚的倒计时逻辑添加到了玩家实体的 `tick` 方法中。这是继 `DeathScreenMixin` 和 `ChatScreenMixin` 之后的第三层保障。无论玩家处于哪个界面（死亡、聊天、甚至在某些特殊情况下没有界面），只要玩家实体本身在更新，倒计时就会继续，这使得通过任何方式暂停倒计时都变得几乎不可能。
 *
 * 2.  **饥饿消耗锁定**：这是一个非常巧妙的机制，其目的是“冻结”玩家的饥饿值，使其不会因为任何活动（如奔跑、跳跃、攻击）而下降。它的工作原理如下：
 *     a. 在游戏处理玩家移动和饥饿消耗的逻辑（`tickMovement`）开始之前，它先记下玩家当前的饥饿值。
 *     b. 然后，它立即给玩家增加大量的饥饿值（+7）。
 *     c. 接下来，原版的 `tickMovement` 方法会执行，它会根据玩家的活动计算出应该消耗多少饥饿值，并从那个被临时增加过的、非常高的饥饿值中扣除。
 *     d. 最后，在 `tickMovement` 方法执行完毕后，它会强制将玩家的饥饿值设置回第一步中记录的原始值。
 *
 *     最终效果是，无论玩家在这一刻做了什么，他们的饥饿值在这一刻结束时都会被重置回原来的水平，看起来就像是完全没有消耗一样。这实际上禁用了饥饿系统。
 */
@Mixin(ClientPlayerEntity.class) // @Mixin 注解，目标是客户端玩家实体类。
public abstract class ClientPlayerEntityMixin extends PlayerEntity {
    @Final
    @Shadow
    protected MinecraftClient client;

    // @Unique: 添加一个此类独有的新字段，用于临时存储玩家的饥e饿值。
    @Unique
    private int tick_hunger = 0;

    public ClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    // 注入到玩家实体的 tick 方法开头。
    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo ci) {
        // 这是第三层死亡倒计时逻辑，确保在任何情况下计时器都能运行。
        if(this.isDead()){
            MiningPlayers.timeDead--;
        }
    }

    // --- 饥饿锁定机制 开始 ---

    // 注入到 tickMovement 方法的开头（HEAD）。
    @Inject(at = @At("HEAD"), method = "tickMovement")
    public void tickMovement(CallbackInfo info) {
        // 步骤 a: 记录下当前真实的饥饿值。
        tick_hunger = this.hungerManager.getFoodLevel();

        // 步骤 b: 如果玩家有饥饿值，就临时增加它。
        // 这样一来，即使原版代码要消耗1点饥饿，也是从一个虚高的值（例如 20 -> 27）上扣除，变成26。
        if (tick_hunger > 0) {
            this.hungerManager.setFoodLevel(tick_hunger + 7);
        }
    }

    // 注入到 tickMovement 方法的末尾（TAIL）。
    @Inject(at = @At("TAIL"), method = "tickMovement")
    public void tickMovementReturn(CallbackInfo info) {
        // 步骤 d: 无论中间发生了什么，都将饥饿值强制恢复到我们之前记录的值。
        // 接着上面的例子，无论饥饿值变成了26还是别的什么，这里都会把它强制设回20。
        this.hungerManager.setFoodLevel(tick_hunger);
    }
    // --- 饥饿锁定机制 结束 ---
}