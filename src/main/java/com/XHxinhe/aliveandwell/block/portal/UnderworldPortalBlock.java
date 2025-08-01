package com.XHxinhe.aliveandwell.block.portal;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.dimensions.DimsRegistry;
import com.XHxinhe.aliveandwell.util.TeleporterPortalHelper;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.kyrptonaught.customportalapi.interfaces.EntityInCustomPortal;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 一个自定义传送门方块，用于在主世界和自定义的“地下世界”维度之间传送。
 * 这个传送门有一个时间限制：只有当游戏进行到特定天数后才能激活。
 */
public class UnderworldPortalBlock extends CustomPortalBlock {

    public UnderworldPortalBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    /**
     * 当实体与方块碰撞时调用。
     * @param state 方块状态
     * @param world 实体所在的世界
     * @param pos 方块的位置
     * @param entity 与方块碰撞的实体
     */
    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        EntityInCustomPortal entityInPortal = (EntityInCustomPortal) entity;
        entityInPortal.tickInPortal(pos.toImmutable());

        // 检查实体是否已传送以及在传送门内的时间是否足够
        if (!entityInPortal.didTeleport() && entityInPortal.getTimeInPortal() >= entity.getMaxNetherPortalTime()) {
            entityInPortal.setDidTP(true);

            // 确保逻辑只在服务器端执行
            if (!world.isClient()) {
                // 检查实体是否在主世界或地下世界，使传送门可以双向工作
                if (entity.getEntityWorld().getRegistryKey() == World.OVERWORLD || entity.getEntityWorld().getRegistryKey() == DimsRegistry.UNDER_WORLD_KEY) {

                    // 检查当前游戏天数是否满足进入地下世界的条件
                    if (AliveAndWellMain.day >= AliveAndWellMain.structureUnderDay) {
                        // 条件满足，执行传送
                        TeleporterPortalHelper.TPToDim(world, entity, this.getPortalBase(world, pos), pos);
                    } else if (entity instanceof PlayerEntity) {
                        // 条件不满足，向玩家发送提示消息
                        PlayerEntity player = (PlayerEntity) entity;

                        // 构建并发送消息，告诉玩家还需要等待多少天
                        Text message = Text.translatable("aliveandwell.to_underworld.fail", AliveAndWellMain.structureUnderDay)
                                .formatted(Formatting.RED);
                        player.sendMessage(message, false);
                    }
                }
            }
        }
    }
}