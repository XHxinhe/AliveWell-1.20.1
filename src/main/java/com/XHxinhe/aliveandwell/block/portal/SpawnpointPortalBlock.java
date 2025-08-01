package com.XHxinhe.aliveandwell.block.portal;

import com.XHxinhe.aliveandwell.util.TpUtil;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.kyrptonaught.customportalapi.interfaces.EntityInCustomPortal;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 一个自定义传送门方块，可以将进入的实体传送回其设置的重生点。
 */
public class SpawnpointPortalBlock extends CustomPortalBlock {

    public SpawnpointPortalBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    /**
     * 当实体与方块碰撞时调用。
     * 这是传送逻辑的核心。
     * @param state 方块状态
     * @param world 实体所在的世界
     * @param pos 方块的位置
     * @param entity 与方块碰撞的实体
     */
    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        // 将实体转换为 CustomPortal API 的接口类型，以追踪其在传送门内的时间
        EntityInCustomPortal entityInPortal = (EntityInCustomPortal) entity;
        entityInPortal.tickInPortal(pos.toImmutable());

        // 检查实体是否已经传送过，以及在传送门内停留的时间是否达到最大值（与下界传送门相同）
        if (!entityInPortal.didTeleport() && entityInPortal.getTimeInPortal() >= entity.getMaxNetherPortalTime()) {
            // 标记实体为已传送，防止在同一行程中重复传送
            entityInPortal.setDidTP(true);

            // 确保逻辑只在服务器端执行，以防止客户端和服务端出现不一致
            if (!world.isClient()) {
                // 调用工具类中的方法，将实体传送回其重生点
                TpUtil.backSpawnpoint(world, entity);
            }
        }
    }
}