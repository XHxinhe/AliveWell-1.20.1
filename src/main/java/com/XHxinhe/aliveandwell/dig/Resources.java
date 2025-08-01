package com.XHxinhe.aliveandwell.dig;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

/**
 * 资源与工具类（混淆名替换版）
 * <p>
 * 包含一些静态的辅助方法，例如计算距离和执行射线检测。
 */
public class Resources {
    public Resources() {
    }

    /**
     * 根据三维坐标的差值计算欧几里得距离。
     */
    public static float getDistanceFromDeltas(double dx, double dy, double dz) {
        return MathHelper.sqrt((float) (dx * dx + dy * dy + dz * dz));
    }

    /**
     * 线性插值。
     */
    public static float lerp(float a, float b, float lerp) {
        return a + lerp * (b - a);
    }

    /**
     * 执行一次射线检测，以确定实体是否能“物理上”触及某个点。
     * 如果射线与方块碰撞，则返回碰撞信息。
     *
     * @param start  射线起点
     * @param end    射线终点
     * @param world  所在世界
     * @param entity 发起射线的实体（用于碰撞排除）
     * @return 返回一个 RaycastCollision 对象，包含碰撞方块的信息。如果未碰撞，则字段为空。
     */
    public static RaycastCollision getBlockCollisionForPhysicalReach(Vec3d start, Vec3d end, World world, Entity entity) {
        RaycastCollision rc = new RaycastCollision();

        // 创建射线检测上下文，定义了射线的起点、终点、碰撞箱形状、流体处理方式和要忽略的实体
        RaycastContext context = new RaycastContext(
                start,
                end,
                RaycastContext.ShapeType.COLLIDER, // 使用方块的完整碰撞箱进行检测
                RaycastContext.FluidHandling.NONE, // 忽略流体
                entity
        );

        // 执行射线检测
        BlockHitResult result = world.raycast(context);

        // 如果有碰撞结果 (result.getType() == HitResult.Type.BLOCK)
        if (result != null && result.getType() == BlockHitResult.Type.BLOCK) {
            // 将碰撞结果填充到自定义的 RaycastCollision 对象中
            rc.block_hit_x = result.getBlockPos().getX();
            rc.block_hit_y = result.getBlockPos().getY();
            rc.block_hit_z = result.getBlockPos().getZ();
            BlockState blockState = world.getBlockState(result.getBlockPos());
            rc.blockHit = blockState.getBlock();
        }

        return rc;
    }
}