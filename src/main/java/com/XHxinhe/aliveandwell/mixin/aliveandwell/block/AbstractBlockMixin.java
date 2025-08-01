package com.XHxinhe.aliveandwell.mixin.aliveandwell.block;

import com.XHxinhe.aliveandwell.block.FallingBlockHelper;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {

    // --- 功能 1: 自定义方块下落 (已更新) ---
    @Inject(at = @At("HEAD"), method = "scheduledTick")
    public void onScheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (world.isClient()) {
            return;
        }

        Block self = state.getBlock();
        // 新版本的目标方块列表：增加了灵魂沙、灵魂土和干草块
        boolean isCustomFallingBlock = self == Blocks.DIRT || self == Blocks.COARSE_DIRT || self == Blocks.ROOTED_DIRT ||
                self == Blocks.SOUL_SAND || self == Blocks.SOUL_SOIL || self == Blocks.HAY_BLOCK;

        if (isCustomFallingBlock) {
            // 检查下方方块是否为固体方块。如果不是，则使其下落。
            // 注意: isSolidBlock() 比 FallingBlock.canFallThrough() 更严格。
            if (!world.getBlockState(pos.down()).isSolidBlock(world, pos.down())) {
                FallingBlockHelper.tryToFall(world, pos);
            }
        }
    }

    // --- 功能 2: 修改作物掉落 (无变化) ---
    @Inject(at = @At("HEAD"), method = "getDroppedStacks", cancellable = true)
    public void onGetDroppedStacks(BlockState state, LootContextParameterSet.Builder builder, CallbackInfoReturnable<List<ItemStack>> cir) {
        // 注意: 下面这行代码获取了方块ID，但变量`name`从未被使用，属于无效代码。
        // String name = Registries.BLOCK.getId((Block)(Object)this).toString();

        if (state.getBlock() instanceof CropBlock cropBlock) {
            if (cropBlock.getAge(state) < cropBlock.getMaxAge()) {
                cir.setReturnValue(Collections.emptyList());
            }
        }
    }

    // --- 功能 3: 连锁生成蠹虫 (已更新) ---
    @Inject(at = @At("HEAD"), method = "onStacksDropped")
    public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack, boolean dropExperience, CallbackInfo ci) {
        if (world.isClient()) {
            return;
        }

        // 检查被破坏的方块是否为石质类型
        if (isStoneType(state.getBlock())) {
            // 遍历所有6个方向
            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = pos.offset(direction);
                Block neighborBlock = world.getBlockState(neighborPos).getBlock();

                // 如果邻居方块也是石质类型
                if (isStoneType(neighborBlock)) {
                    // 暮色森林维度有更高的生成概率
                    boolean isTwilightForest = world.getRegistryKey().getValue().getNamespace().equals("twilightforest");
                    int chanceBound = isTwilightForest ? 30 : 1000;

                    // 使用世界自带的随机数生成器，避免每次都 new Random()
                    if (world.getRandom().nextInt(chanceBound) < 5) {
                        // 调用修正后的生成方法
                        spawnSingleSilverfish(world, neighborPos);
                    }
                }
            }
        }
    }

    /**
     * 辅助方法，判断方块是否为“石质”类型。
     * 新版本增加了方解石(Calcite)，并将虫蚀方块的判断改为了更通用的 ExperienceDroppingBlock。
     */
    @Unique
    private boolean isStoneType(Block block) {
        return block == Blocks.STONE || block == Blocks.DEEPSLATE || block == Blocks.GRANITE ||
                block == Blocks.DIORITE || block == Blocks.ANDESITE || block == Blocks.CALCITE ||
                block == Blocks.TUFF || block instanceof ExperienceDroppingBlock; // 虫蚀方块会掉落经验
    }

    /**
     * 辅助方法，在指定位置生成一只蠹虫。
     * 修正了原始代码中严重的逻辑错误。
     * @param world 服务器世界
     * @param pos   生成位置
     */
    @Unique
    private void spawnSingleSilverfish(ServerWorld world, BlockPos pos) {
        SilverfishEntity silverfish = EntityType.SILVERFISH.create(world);
        if (silverfish != null) {
            world.breakBlock(pos, true); // 破坏原方块
            silverfish.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0f, 0.0f);
            world.spawnEntity(silverfish); // 将蠹虫实体添加到世界
            silverfish.playSpawnEffects(); // 播放生成动画和音效
        }
    }
}