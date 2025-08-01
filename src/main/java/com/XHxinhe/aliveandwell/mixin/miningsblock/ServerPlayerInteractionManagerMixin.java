package com.XHxinhe.aliveandwell.mixin.miningsblock;

import com.XHxinhe.aliveandwell.miningsblock.MiningMixinHooks;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 这是一个用于 ServerPlayerInteractionManager 的 Mixin。
 * 它是“连锁挖矿”功能的触发器和总指挥。
 * 它负责在玩家成功破坏一个方块后，启动连锁破坏的逻辑。
 */
@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {

    @Final
    @Shadow
    protected ServerPlayerEntity player;

    @Unique
    private BlockState source;

    public ServerPlayerInteractionManagerMixin() {
    }

    /**
     * 注入点一：在 tryBreakBlock 方法的开头。
     * 作用：在方块被破坏之前，记录下它的 BlockState。
     */
    @Inject(
            at = @At(value = "HEAD"),
            method = "tryBreakBlock(Lnet/minecraft/util/math/BlockPos;)Z")
    private void veinmining$preHarvest(BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
        source = player.getServerWorld().getBlockState(pos);
    }

    /**
     * 注入点二：在 tryBreakBlock 方法内部，就在工具的 postMine 方法被调用之前。
     * 作用：此时原版方块已被成功破坏，是触发连锁反应的最佳时机。
     */
    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/item/ItemStack.postMine(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;)V"),
            method = "tryBreakBlock(Lnet/minecraft/util/math/BlockPos;)Z")
    private void veinmining$tryHarvest(BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
        MiningMixinHooks.tryHarvest(player, pos, source);
    }
}
