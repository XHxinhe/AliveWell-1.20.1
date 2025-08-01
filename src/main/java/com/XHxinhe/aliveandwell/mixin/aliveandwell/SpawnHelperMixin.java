package com.XHxinhe.aliveandwell.mixin.aliveandwell;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.dimensions.DimsRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.*;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnHelper.class)
public class SpawnHelperMixin {
    @ModifyConstant(method = "isAcceptableSpawnPosition", constant = @Constant(doubleValue = 24.0))
    private static double isAcceptableSpawnPosition(double constant) {
        return 16.0d;
    }

    @ModifyConstant(method = "isAcceptableSpawnPosition", constant = @Constant(doubleValue = 576.0))
    private static double isAcceptableSpawnPositionDistance(double constant) {
        return 256;
    }

    @Inject(at = @At("RETURN"), method = "isAcceptableSpawnPosition", cancellable = true)
    private static void isAcceptableSpawnPosition(ServerWorld world, Chunk chunk, BlockPos.Mutable pos, double squaredDistance, CallbackInfoReturnable<Boolean> cir) {
        int posY = pos.getY();
        int high;
        int players = world.getServer().getCurrentPlayerCount();
        PlayerEntity playerEntity = world.getClosestPlayer(pos.getX()+0.5, (double)pos.getY(), pos.getZ()+0.5, -1.0, false);
        if(world.getRegistryKey() == DimsRegistry.UNDER_WORLD_KEY || world.getRegistryKey() == World.NETHER){
            if(AliveAndWellMain.day <= 128){
                if(players == 1){
                    if(world.random.nextInt(30)  < 5){
                        high = 15;
                    }else {
                        high = 20;
                    }
                }else if(players <= 4){
                    if(world.random.nextInt(25)  < 5){
                        high = 15;
                    }else {
                        high = 20;
                    }
                }else if(players <= 8){
                    if(world.random.nextInt(20)  < 5){
                        high = 15;
                    }else {
                        high = 20;
                    }
                }else {
                    if(world.random.nextInt(15)  < 5){
                        high = 15;
                    }else {
                        high = 20;
                    }
                }
            }else {
                if(world.random.nextInt(15)  < 5){
                    high = 15;
                }else {
                    high = 20;
                }
            }
        }else if(world.getRegistryKey() == World.OVERWORLD){
            if(AliveAndWellMain.day >= 10 && AliveAndWellMain.day <= 128){
                if(players == 1){
                    if(world.random.nextInt(30)  < 5){
                        high = 15;
                    }else {
                        high = 20;
                    }
                }else if(players <= 4){
                    if(world.random.nextInt(25)  < 5){
                        high = 15;
                    }else {
                        high = 20;
                    }

                }else if(players <= 8){
                    if(world.random.nextInt(20)  < 5){
                        high = 15;
                    }else {
                        high = 20;
                    }
                }else {
                    if(world.random.nextInt(15)  < 5){
                        high = 15;
                    }else {
                        high = 20;
                    }
                }
            }else {
                if(world.random.nextInt(15)  < 5){
                    high = 15;
                }else {
                    high = 20;
                }
            }
        }else {
            high = 20;
        }
        if(playerEntity == null || Math.abs(posY-playerEntity.getBlockPos().getY()) > high) {
            cir.setReturnValue(false);
        }


//    @Overwrite
//    private static boolean isAcceptableSpawnPosition(ServerWorld world, Chunk chunk, BlockPos.Mutable pos, double squaredDistance) {
////        if (squaredDistance <= 576.0) {
////            return false;
////        }
////        if (world.getSpawnPos().isWithinDistance(new Vec3d((double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5), 24.0)) {
////            return false;
////        }
////        return Objects.equals(new ChunkPos(pos), chunk.getPos()) || world.shouldTick(pos);
//
//        if (squaredDistance <= 225.0) {
//            return false;
//        }
//        if (world.getSpawnPos().isWithinDistance(new Vec3d((double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5), 15.0)) {
//            return false;
//        }
//
//        int posY = pos.getY();
//        int high;
//        if(world.getRegistryKey() == DimsRegistry.UNDER_WORLD_KEY){
//            high=32;
//        }else {
//            high=20;
//        }
////        int players = world.getServer().getCurrentPlayerCount();
//        PlayerEntity playerEntity = world.getClosestPlayer(pos.getX()+0.5, (double)pos.getY(), pos.getZ()+0.5, -1.0, false);
//        if(playerEntity == null || Math.abs(posY-playerEntity.getBlockPos().getY()) > high) {
//            AliveAndWellMain.LOGGER.info("]]]]]]]]]]]]]]]]]"+Math.abs(posY-playerEntity.getBlockPos().getY()));
//            return false;
//        }
//
//        return Objects.equals(new ChunkPos(pos), chunk.getPos()) || world.shouldTick(pos);
//    }

//    @Overwrite
//    private static BlockPos getRandomPosInChunkSection(World world, WorldChunk chunk)
//        ChunkPos chunkPos = chunk.getPos();
//        int i = chunkPos.getStartX() + world.random.nextInt(16);
//        int j = chunkPos.getStartZ() + world.random.nextInt(16);
//        int k = chunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE, i, j) + 1;
//
//        int l = MathHelper.nextBetween(world.random, world.getBottomY(), k);
//
//        PlayerEntity playerEntity = world.getClosestPlayer(i+0.5, l, j+0.5, -1.0, false);
//        int high;
//        int palyerPosY = playerEntity.getBlockPos().getY();
//
//        if(world.getRegistryKey() == DimsRegistry.UNDER_WORLD_KEY){
//            high=24;
//        }else {
//            high=15;
//        }
//        if(playerEntity == null || Math.abs(l-palyerPosY) > high) {
//            if(l<palyerPosY){
//                l=palyerPosY-(new Random().nextInt(high-5)+10);
//                if(world.isNight() && world.getLightLevel(new BlockPos(i, l, j)) >= 7){
//                    l=palyerPosY-(new Random().nextInt(high-5)+10);
//                }else {
//                    l=palyerPosY-(new Random().nextInt(high-5)+10);
//                }
//            }else {
//                l=palyerPosY+(new Random().nextInt(high-5)+10);
//            }
//        }
//
////        AliveAndWellMain.LOGGER.info("&&&&&&&&&&&&"+"i="+i+";l="+l+";j="+j+"player="+String.valueOf(playerEntity==null));
//        return new BlockPos(i, l, j);
    }
}
