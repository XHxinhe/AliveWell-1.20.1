package com.XHxinhe.aliveandwell.mixin.aliveandwell.enity;

import com.XHxinhe.aliveandwell.registry.ItemInit;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import java.util.List;

@Mixin(LightningEntity.class)
public abstract class LightningEntityMixin extends Entity {
    @Shadow private boolean cosmetic;
    @Shadow private int blocksSetOnFire;
    public LightningEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void spawnFire(int spreadAttempts) {
        if (this.cosmetic || this.getWorld().isClient || !this.getWorld().getGameRules().getBoolean(GameRules.DO_FIRE_TICK)) {
            return;
        }
        BlockPos blockPos = this.getBlockPos();
        BlockState blockState = AbstractFireBlock.getState(this.getWorld(), blockPos);

        List<PlayerEntity> players = this.getWorld().<PlayerEntity>getEntitiesByClass(
                PlayerEntity.class,
                new Box(this.getX()-8,this.getY()-8,this.getZ()-8, this.getX()+8,this.getY()+8,this.getZ()+8),
                (e) -> e instanceof PlayerEntity
        );
        boolean hasPlayer = false;

        for (PlayerEntity player : players){
            if (player != null && player.getStackInHand(player.getActiveHand()).getItem() == ItemInit.ANCIENT_SWORD) {
                hasPlayer=true;
                break;
            }
        }

        if (!hasPlayer) {
            if (this.getWorld().getBlockState(blockPos).isAir() && blockState.canPlaceAt(this.getWorld(), blockPos)) {
                this.getWorld().setBlockState(blockPos, blockState);
                ++this.blocksSetOnFire;
            }
            for (int i = 0; i < spreadAttempts; ++i) {
                BlockPos blockPos2 = blockPos.add(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);
                blockState = AbstractFireBlock.getState(this.getWorld(), blockPos2);
                if (!this.getWorld().getBlockState(blockPos2).isAir() || !blockState.canPlaceAt(this.getWorld(), blockPos2)) continue;
                this.getWorld().setBlockState(blockPos2, blockState);
                ++this.blocksSetOnFire;
            }
        }
    }
}
