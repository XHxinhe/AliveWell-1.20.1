package com.XHxinhe.aliveandwell.dig;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.EnumSet;

// 这是一个由反编译器生成的代码的直接翻译版本，保留了原始的逻辑和结构。
// 混淆名已被替换为可读的名称。
public class EntityAIWatchAnimal extends Goal {
    private AnimalWatcherEntity digger;
    private ZombieEntity zombie;
    private static boolean player_attacks_always_reset_digging = false;

    public EntityAIWatchAnimal(ZombieEntity attacker) {
        this.digger = (AnimalWatcherEntity) attacker;
        this.zombie = attacker;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (this.digger.isHoldingItemThatPreventsDigging()) {
            return false;
        } else if ((!this.digger.isDiggingEnabled() && !this.digger.canSeeTarget(false)) || (this.digger.recentlyHit() > 0 && player_attacks_always_reset_digging)) {
            // 注意：此处的原始逻辑是 `(!isDiggingEnabled && !canSeeTarget)`，与之前分析的 `(isDiggingEnabled || canSeeTarget)` 不同。
            // 这意味着只有在“挖掘被禁用”且“看不见目标”时，或者被攻击时，才会返回false并阻止AI启动。
            return false;
        } else {
            LivingEntity target = this.zombie.getTarget();
            if (target == null) {
                return false;
            } else if (this.zombie.getBlockPos().getX() == target.getBlockPos().getX() && this.zombie.getBlockPos().getY() == target.getBlockPos().getY() && this.zombie.getBlockPos().getZ() == target.getBlockPos().getZ()) {
                return false;
            } else if (this.digger.isDestroyingBlock() && this.digger.canDestroyBlock(this.digger.getDestroyBlockX(), this.digger.getDestroyBlockY(), this.digger.getDestroyBlockZ(), true)) {
                return true;
            } else {
                float distance_to_target = this.zombie.distanceTo(target);
                if (distance_to_target > 16.0F) {
                    return false;
                } else {
                    int attacker_foot_y = this.zombie.getBlockPos().getY();
                    if (distance_to_target * distance_to_target > 2.0F) {
                        int targetX = target.getBlockPos().getX();
                        int scanY = target.getBlockPos().getY() + 2;
                        int targetZ = target.getBlockPos().getZ();

                        for (; scanY >= attacker_foot_y; --scanY) {
                            if (this.digger.setBlockToDig(targetX, scanY, targetZ, true)) {
                                this.digger.setDestroyingBlock(true);
                                return true;
                            }
                        }
                    }

                    if (distance_to_target > 8.0F) {
                        return false;
                    } else {
                        boolean var11 = this.isAirOrPassableBlock((double) this.zombie.getBlockPos().getX(), this.digger.getEyePosForBlockDestroying().getY() + 1.0, (double) this.zombie.getBlockPos().getZ(), false, this.zombie.getWorld()) && this.checkForLineOfPhysicalReach(new Vec3d(this.zombie.getX(), this.digger.getEyePosForBlockDestroying().getY() + 1.0, this.zombie.getZ()), target.getPos().add(0.0, (double) (target.getHeight() * 0.75F), 0.0), this.zombie.getWorld());
                        if (distance_to_target > (var11 ? 8.0F : (this.digger.isFrenzied() ? 16.0F : 8.0F))) {
                            return false;
                        } else {
                            Path path = this.zombie.getNavigation().findPathTo(target, 32);
                            if (this.zombie.getNavigation().isFollowingPath()) {
                                return false;
                            } else if (this.digger.hasLineOfStrikeAndTargetIsWithinStrikingDistance(target)) {
                                return false;
                            } else {
                                Vec3d target_center_pos = this.digger.getTargetEntityCenterPosForBlockDestroying(target);
                                RaycastCollision rc;
                                if (this.isAirOrPassableBlock((double) target.getBlockPos().getX(), (double) target.getBlockPos().getY() + (double) target.getHeight() * 0.75 + 1.0, (double) target.getBlockPos().getZ(), false, this.zombie.getWorld())) {
                                    rc = Resources.getBlockCollisionForPhysicalReach(this.digger.getEyePosForBlockDestroying(), target_center_pos.add(new Vec3d(0.0, 1.0, 0.0)), this.zombie.getWorld(), this.zombie);
                                    if (rc != null && rc.isBlock() && (this.isNotRestrictedBlock(rc.getBlockHit()) || this.digger.isHoldingAnEffectiveTool(rc.getBlockHit()) || this.zombie.getTarget() instanceof PlayerEntity)) {
                                        ++rc.block_hit_y;

                                        while (rc.block_hit_y >= attacker_foot_y) {
                                            if (this.digger.setBlockToDig(rc.block_hit_x, rc.block_hit_y, rc.block_hit_z, true)) {
                                                return true;
                                            }
                                            --rc.block_hit_y;
                                        }
                                    }
                                }

                                rc = Resources.getBlockCollisionForPhysicalReach(this.digger.getEyePosForBlockDestroying(), target_center_pos, this.zombie.getWorld(), this.zombie);
                                if (rc != null && rc.isBlock() && (this.isNotRestrictedBlock(rc.getBlockHit()) || this.digger.isHoldingAnEffectiveTool(rc.getBlockHit()) || this.zombie.getTarget() instanceof PlayerEntity)) {
                                    ++rc.block_hit_y;

                                    while (rc.block_hit_y >= attacker_foot_y) {
                                        if (this.digger.setBlockToDig(rc.block_hit_x, rc.block_hit_y, rc.block_hit_z, true)) {
                                            return true;
                                        }
                                        --rc.block_hit_y;
                                    }
                                }

                                rc = Resources.getBlockCollisionForPhysicalReach(this.digger.getAttackerLegPosForBlockDestroying(), target_center_pos, this.zombie.getWorld(), this.zombie);
                                if (rc != null && rc.getBlockHit() != null) {
                                    boolean b = rc.isBlock() && (this.isNotRestrictedBlock(rc.getBlockHit()) || this.digger.isHoldingAnEffectiveTool(rc.getBlockHit()) || this.zombie.getTarget() instanceof PlayerEntity) && (!this.isAirOrPassableBlock((double) rc.block_hit_x, (double) (rc.block_hit_y + 1), (double) rc.block_hit_z, false, this.zombie.getWorld()) || this.digger.blockWillFall(rc.block_hit_x, rc.block_hit_y + 1, rc.block_hit_z)) && this.digger.setBlockToDig(rc.block_hit_x, rc.block_hit_y, rc.block_hit_z, false);
                                    return b;
                                } else {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public final RaycastCollision getBlockCollisionForPhysicalReach(Vec3d origin, Vec3d limit, World world) {
        return Resources.getBlockCollisionForPhysicalReach(origin, limit, world, this.zombie);
    }

    public final boolean checkForLineOfPhysicalReach(Vec3d origin, Vec3d limit, World world) {
        return !this.getBlockCollisionForPhysicalReach(origin, limit, world).isBlock();
    }

    private boolean isNotRestrictedBlock(Block blockHit) {
        return blockHit != Blocks.BEDROCK && blockHit != Blocks.BARRIER;
    }

    public final boolean isAirOrPassableBlock(double x, double y, double z, boolean include_liquid, World world) {
        if (y >= 0.0 && y <= 255.0) { // 在旧版本中，世界高度是固定的
            if (!world.isChunkLoaded(BlockPos.ofFloored(x, y, z))) {
                return false;
            } else {
                // 保留原始的 getRawIdFromState 写法
                int block_id = Block.getRawIdFromState(world.getBlockState(BlockPos.ofFloored(x, y, z)));
                if (block_id == 0) {
                    return true;
                } else {
                    // 保留原始的 getDefaultState 写法
                    Block block = world.getBlockState(BlockPos.ofFloored(x, y, z)).getBlock();
                    return block != null && (include_liquid || !block.getDefaultState().isLiquid()) && !block.getDefaultState().isSolid();
                }
            }
        } else {
            return true;
        }
    }

    @Override
    public void start() {
        this.digger.setDestroyingBlock(true);
    }

    private RaycastCollision getIntersectingBlock(Vec3d attacker_eye_pos, Vec3d target_pos, World world) {
        return this.getBlockCollisionForPhysicalReach(attacker_eye_pos, target_pos, world);
    }

    private boolean couldHitTargetByPathing() {
        LivingEntity target = this.zombie.getTarget();
        if (target == null) {
            return false;
        } else {
            Path path = this.zombie.getNavigation().findPathTo(target, 16);
            if (path == null) {
                return false;
            } else {
                PathNode final_point = path.getEnd();
                float x = (float) final_point.x + 0.5F;
                float y = (float) final_point.y;
                float z = (float) final_point.z + 0.5F;
                return !(Resources.getDistanceFromDeltas(x - target.getX(), y - target.getY(), z - target.getZ()) > 1.0F) && !this.getIntersectingBlock(new Vec3d(x, y, z), this.digger.getTargetEntityCenterPosForBlockDestroying(target), this.zombie.getWorld()).isBlock();
            }
        }
    }

    private boolean couldGetCloserByPathing() {
        LivingEntity target = this.zombie.getTarget();
        if (target == null) {
            return false;
        } else {
            double distance = Resources.getDistanceFromDeltas(this.zombie.getX() - target.getX(), this.zombie.getY() - target.getY(), this.zombie.getZ() - target.getZ());
            Path path = this.zombie.getNavigation().findPathTo(target, 16);
            if (path == null) {
                return false;
            } else {
                // 保留原始的打印输出
                System.out.println("pathing: " + path.isFinished());
                PathNode final_point = path.getEnd();
                float x = (float) final_point.x + 0.5F;
                float y = (float) final_point.y;
                float z = (float) final_point.z + 0.5F;
                return Resources.getDistanceFromDeltas(x - target.getX(), y - target.getY(), z - target.getZ()) < distance - 2.0;
            }
        }
    }

    @Override
    public boolean shouldContinue() {
        if (this.digger.isHoldingItemThatPreventsDigging()) {
            return false;
        } else if (this.zombie.getTarget() != null && this.zombie.getPos().distanceTo(this.zombie.getTarget().getPos()) <= 1.5) {
            return false;
        } else if (this.digger.getDestroyPauseTicks() > 0) {
            return this.digger.getDestroyPauseTicks() != 1 || !this.couldGetCloserByPathing();
        } else if (!this.digger.isDestroyingBlock()) {
            return false;
        } else if (!this.digger.canDestroyBlock(this.digger.getDestroyBlockX(), this.digger.getDestroyBlockY(), this.digger.getDestroyBlockZ(), true)) {
            return false;
        } else if (this.digger.recentlyHit() > 0 && player_attacks_always_reset_digging) {
            return false;
        } else {
            LivingEntity target = this.zombie.getTarget();
            return target != null && (this.zombie.getBlockPos().getX() != target.getBlockPos().getX() || this.zombie.getBlockPos().getY() != target.getBlockPos().getY() || this.zombie.getBlockPos().getZ() != target.getBlockPos().getZ()) && (this.digger.getTicksExistedWithOffset() % 10 != 0 || !this.couldHitTargetByPathing());
        }
    }

    @Override
    public void tick() {
        if (this.digger.getDestroyPauseTicks() > 0) {
            this.digger.decrementDestroyPauseTicks();
        } else {
            if (this.digger.getDestroyBlockCooloff() == 10) {
                this.zombie.swingHand(Hand.MAIN_HAND);
                this.zombie.swingHand(Hand.OFF_HAND);
                int x = this.digger.getDestroyBlockX();
                int y = this.digger.getDestroyBlockY();
                int z = this.digger.getDestroyBlockZ();
                World world = this.zombie.getWorld();
                BlockPos pos = BlockPos.ofFloored(x, y, z);
                // 保留原始的 getPlaceSound() 调用
                world.playSound(null, pos, world.getBlockState(pos).getSoundGroup().getPlaceSound(), SoundCategory.HOSTILE, 1.0F, 1.0F);
            }

            this.digger.decrementDestroyBlockCooloff();
            if (this.digger.getDestroyBlockCooloff() <= 0) {
                this.digger.setDestroyBlockCooloff(this.digger.getCooloffForBlock());
                this.digger.partiallyDestroyBlock();
            }
        }
    }

    @Override
    public void stop() {
        this.digger.cancelBlockDestruction();
    }
}