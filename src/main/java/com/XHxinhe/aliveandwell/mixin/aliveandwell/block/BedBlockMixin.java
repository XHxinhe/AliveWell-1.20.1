package com.XHxinhe.aliveandwell.mixin.aliveandwell.block;

import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public abstract class BedBlockMixin {

    /**
     * 注入到 BedBlock 的 onUse 方法中。
     * @param state 方块状态
     * @param world 世界
     * @param pos 方块位置
     * @param player 交互的玩家
     * @param hand 交互时使用的手
     * @param hit 命中结果
     * @param cir 回调信息，用于取消原方法并设置返回值
     */
    @Inject(
            method = "onUse",
            // 注入点：在原方法即将调用 player.trySleep() 的地方拦截
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;trySleep(Lnet/minecraft/util/math/BlockPos;)Lcom/mojang/datafixers/util/Either;"
            ),
            // 允许我们取消原方法的后续执行
            cancellable = true
    )
    private void forceSleepMixin(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {

        // 手动调用原版的 trySleep 方法，并获取其返回结果
        player.trySleep(pos).ifLeft((sleepFailureReason) -> {
            // ifLeft 表示 trySleep 失败了，我们在这里处理失败的逻辑

            // 条件1: 如果失败原因不是“现在不能睡觉”（即不是因为白天）
            // 那么就把失败原因消息发给玩家，例如“附近有怪物”。
            if (sleepFailureReason != PlayerEntity.SleepFailureReason.NOT_POSSIBLE_NOW) {
                player.sendMessage(sleepFailureReason.getMessage(), true); // true 表示消息显示在快捷栏上方
            }

            // 条件2: 这是此Mod的核心逻辑！
            // 如果失败原因是因为“现在不能睡觉”（白天）或者“不安全”（附近有怪物）
            if (sleepFailureReason == PlayerEntity.SleepFailureReason.NOT_POSSIBLE_NOW ||
                    sleepFailureReason == PlayerEntity.SleepFailureReason.NOT_SAFE) {

                // 强制玩家入睡，并设置重生点。
                // player.sleep() 是一个更底层的调用，它不进行安全检查，直接让玩家躺下。
                player.sleep(pos);
            }
        });

        // 无论成功还是失败，都取消原版 onUse 方法的后续执行，并返回 SUCCESS。
        // 这可以防止游戏在我们的代码执行后，再次调用 trySleep 或执行其他逻辑。
        cir.setReturnValue(ActionResult.SUCCESS);
    }
}