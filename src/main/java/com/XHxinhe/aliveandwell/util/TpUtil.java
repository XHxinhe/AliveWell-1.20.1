package com.XHxinhe.aliveandwell.util;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.*;

public class TpUtil {

    static int minA = 6000;
    static int maxA = 9000;

    static int minB = 10000;
    static int maxB = 20000;

    private static final Dynamic2CommandExceptionType INVALID_HEIGHT_EXCEPTION = new Dynamic2CommandExceptionType((maxY, worldBottomY) -> {
        return Text.translatable("commands.spreadplayers.failed.invalid.height", new Object[]{maxY, worldBottomY});
    });

    public TpUtil() {
    }

    public static void RandomTpA(World world, Entity entity) {
        if(entity instanceof ServerPlayerEntity serverPlayer){
            BlockPos startingPos;
            startingPos = serverPlayer.getBlockPos();
            java.util.Random rand = new java.util.Random();
            int x;
            int y = 100;
            int z;

            if(world.getDimension().hasCeiling()){
                if(world.getRegistryKey() == World.NETHER){
                    y = 80;
                }
            }else {
                y = world.getTopY();
            }

            x = Math.round(startingPos.getX()) + rand.nextInt(maxA + minA) - minA;
            z = Math.round(startingPos.getZ()) + rand.nextInt(maxA + minA) - minA;
            Vec2f center = new Vec2f(x,z);
            Collection<ServerPlayerEntity> collection = new ArrayList<>();
            collection.add(serverPlayer);
                //参数：目标玩家，目的地x和z，扩散距离，扩散范围，世界的最高y，是否团队，玩家集合
            execute(serverPlayer, center, 1,1,y,false, collection);
        }
    }

    public static void RandomTpB(World world, Entity entity){
        if(entity instanceof ServerPlayerEntity serverPlayer){
            BlockPos startingPos;
            startingPos = serverPlayer.getBlockPos();
            java.util.Random rand = new java.util.Random();
            int x;
            int y = 100;
            int z;
            if(world.getDimension().hasCeiling()){
                if(world.getRegistryKey() == World.NETHER){
                    y = 80;
                }
            }else {
                y = world.getTopY();
            }
            x = Math.round(startingPos.getX()) + rand.nextInt(maxB + minB) - minB;
            z = Math.round(startingPos.getZ()) + rand.nextInt(maxB + minB) - minB;
            Vec2f center = new Vec2f(x,z);
            Collection<ServerPlayerEntity> collection = new ArrayList<>();
            collection.add(serverPlayer);
                //参数：目标玩家，目的地x和z，扩散距离，扩散范围，世界的最高y，是否团队，玩家集合
            execute(serverPlayer, center, 1,1,y,false, collection);
        }
    }

    public static void backSpawnpoint(World world, Entity entity) {
        if(entity instanceof ServerPlayerEntity serverPlayer){
            BlockPos startingPos;
            startingPos = serverPlayer.getWorld().getSpawnPos();
            int x;
            int y;
            int z;
            if(world.getRegistryKey() == World.OVERWORLD){
                y = 150;
            }else {
                y = 100;
            }
            x = startingPos.getX();
            z = startingPos.getZ();
            Vec2f center = new Vec2f(x,z);
            Collection<ServerPlayerEntity> collection = new ArrayList<>();
            collection.add(serverPlayer);
            //参数：目标玩家，目的地x和z，扩散距离，扩散范围，世界的最高y，是否团队，玩家集合
            execute(serverPlayer, center, 1,1,y,false, collection);
        }
    }

    public static void backSpawnpoint1(World world, Entity entity) {
        if (!world.isClient ) {
            if (world instanceof ServerWorld) {
                // 检测安全点的次数
                for(int i = 1; i < 6; i++) {
                    RegistryKey<World> registryKey = world.getRegistryKey();
                    //只能在主世界随机传送
//                    if(!(registryKey == World.OVERWORLD)) {
//                       return;
//                    }

                    BlockPos startingPos;
                    ServerWorld serverWorld = (ServerWorld)world;
                    startingPos = serverWorld.getSpawnPos();

                    // 使用当前世界出生点x和z作为起点
                    int x = startingPos.getX();
                    int y;
                    if(registryKey == World.OVERWORLD) {
                       y = 150;
                    }else {
                        y = 100;
                    }
                    int z = startingPos.getZ();

                    Chunk chunk = world.getChunk(x >> 4, z >> 4);
                    int y1=0;
                    //让我们避免把它们放在地下=最小为60高度
                    while (y > 60) {
                        y--;
                        BlockPos groundPos = new BlockPos(x, y-2, z);//着陆点
                        //着陆点方块不是空气、基岩、水
                        if(!chunk.getBlockState(groundPos).isAir() &&
                                (!chunk.getBlockState(groundPos).getBlock().equals(Blocks.BEDROCK) &&
                                        (!chunk.getBlockState(groundPos).getBlock().equals(Blocks.WATER) &&
                                                (y-2) > 60))) {
                            int m=0;
                            for (int k = 0 ;k<=100;k++){
                                y1=y+k;
                                BlockPos kPos = (new BlockPos(x, y1, z)).mutableCopy();
                                if(!chunk.getBlockState(kPos).isAir()){
                                    m++;
                                }
                            }
                            //检测腿部坐标方块是否为空气
                            BlockPos legPos = new BlockPos(x, y-1, z);
                            if (chunk.getBlockState(legPos).isAir()) {
                                //检测头部坐标方块是否为空气
                                BlockPos headPos = new BlockPos(x, y, z);
                                if (chunk.getBlockState(headPos).isAir()) {
                                    if(m==0) {//传送点头顶没有方块。
                                        entity.stopRiding();//不能骑乘
                                        entity.requestTeleport(x, y, z);
                                        entity.fallDistance = 0.0F;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void execute(ServerPlayerEntity player, Vec2f center, float spreadDistance, float maxRange, int maxY, boolean respectTeams, Collection<? extends Entity> players) {
        ServerWorld serverWorld = (ServerWorld) player.getWorld();
        int i = serverWorld.getBottomY();
        if (maxY >= i) {
            Random random = Random.create();
            double d = (double)(center.x - maxRange);
            double e = (double)(center.y - maxRange);
            double f = (double)(center.x + maxRange);
            double g = (double)(center.y + maxRange);
            Pile[] piles = makePiles(random, respectTeams ? getPileCountRespectingTeams(players) : players.size(), d, e, f, g);
            spread(center, (double)spreadDistance, serverWorld, random, d, e, f, g, maxY, piles, respectTeams);
            getMinDistance(players, serverWorld, piles, maxY, respectTeams);
        }
    }

    private static int getPileCountRespectingTeams(Collection<? extends Entity> entities) {
        Set<AbstractTeam> set = Sets.newHashSet();
        Iterator var2 = entities.iterator();

        while(var2.hasNext()) {
            Entity entity = (Entity)var2.next();
            if (entity instanceof PlayerEntity) {
                set.add(entity.getScoreboardTeam());
            } else {
                set.add((AbstractTeam) null);
            }
        }

        return set.size();
    }

    private static void spread(Vec2f center, double spreadDistance, ServerWorld world, Random random, double minX, double minZ, double maxX, double maxZ, int maxY, TpUtil.Pile[] piles, boolean respectTeams) {
        boolean bl = true;

        // -------------------【关键修改点】-------------------
        // 删除了原来根据 center.x 和 center.y 计算 max 变量的逻辑。
        // 将循环次数固定为 10000 次。这是Minecraft原版命令的做法，它与传送距离无关，
        // 保证了算法性能的稳定，从而修复了因坐标过大导致循环次数过多而崩溃的问题。
        for (int i = 0; i < 10000 && bl; ++i) {
            // ----------------------------------------------------
            bl = false;

            int k;
            TpUtil.Pile pile2;
            for (int j = 0; j < piles.length; ++j) {
                TpUtil.Pile pile = piles[j];
                k = 0;
                pile2 = new TpUtil.Pile();

                for (int l = 0; l < piles.length; ++l) {
                    if (j != l) {
                        TpUtil.Pile pile3 = piles[l];
                        double e = pile.getDistance(pile3);
                        if (e < spreadDistance) {
                            ++k;
                            pile2.x += pile3.x - pile.x;
                            pile2.z += pile3.z - pile.z;
                        }
                    }
                }
                if (k > 0) {
                    pile2.x /= (double)k;
                    pile2.z /= (double)k;
                    double f = pile2.absolute();
                    if (f > 0.0) {
                        pile2.normalize();
                        pile.subtract(pile2);
                    } else {
                        pile.setPileLocation(random, minX, minZ, maxX, maxZ);
                    }
                    bl = true;
                }

                if (pile.clamp(minX, minZ, maxX, maxZ)) {
                    bl = true;
                }
            }

            if (!bl) {
                TpUtil.Pile[] var28 = piles;
                int var29 = piles.length;
                for(k = 0; k < var29; ++k) {
                    pile2 = var28[k];
                    if (!pile2.isSafe(world, maxY)) {
                        pile2.setPileLocation(random, minX, minZ, maxX, maxZ);
                        bl = true;
                    }
                }
            }
        }
    }

    private static void getMinDistance(Collection<? extends Entity> entities, ServerWorld world, TpUtil.Pile[] piles, int maxY, boolean respectTeams) {
        int i = 0;
        HashMap<AbstractTeam, TpUtil.Pile> map = Maps.newHashMap();
        double e = 0;
        for (Entity entity : entities) {
            TpUtil.Pile pile;
            if (respectTeams) {
                AbstractTeam abstractTeam = entity instanceof PlayerEntity ? entity.getScoreboardTeam() : null;
                if (!map.containsKey(abstractTeam)) {
                    map.put(abstractTeam, piles[i++]);
                }
                pile = (TpUtil.Pile)map.get(abstractTeam);
            } else {
                pile = piles[i++];
            }

            entity.teleport(world, (double)MathHelper.floor(pile.x) + 0.5, (double)pile.getY(world, maxY), (double)MathHelper.floor(pile.z) + 0.5, Set.of(), entity.getYaw(), entity.getPitch());
            e = Double.MAX_VALUE;
            for (TpUtil.Pile pile2 : piles) {
                if (pile == pile2) continue;
                double f = pile.getDistance(pile2);
                e = Math.min(f, e);
            }
        }
    }

    private static TpUtil.Pile[] makePiles(Random random, int count, double minX, double minZ, double maxX, double maxZ) {
        TpUtil.Pile[] piles = new TpUtil.Pile[count];
        for (int i = 0; i < piles.length; ++i) {
            TpUtil.Pile pile = new TpUtil.Pile();
            pile.setPileLocation(random, minX, minZ, maxX, maxZ);
            piles[i] = pile;
        }
        return piles;
    }

    static class Pile {
        double x;
        double z;

        Pile() {
        }

        double getDistance(TpUtil.Pile other) {
            double d = this.x - other.x;
            double e = this.z - other.z;
            return Math.sqrt(d * d + e * e);
        }

        void normalize() {
            double d = this.absolute();
            this.x /= d;
            this.z /= d;
        }

        double absolute() {
            return Math.sqrt(this.x * this.x + this.z * this.z);
        }

        public void subtract(TpUtil.Pile other) {
            this.x -= other.x;
            this.z -= other.z;
        }

        public boolean clamp(double minX, double minZ, double maxX, double maxZ) {
            boolean bl = false;
            if (this.x < minX) {
                this.x = minX;
                bl = true;
            } else if (this.x > maxX) {
                this.x = maxX;
                bl = true;
            }
            if (this.z < minZ) {
                this.z = minZ;
                bl = true;
            } else if (this.z > maxZ) {
                this.z = maxZ;
                bl = true;
            }
            return bl;
        }

        public int getY(BlockView blockView, int maxY) {
            BlockPos.Mutable mutable = new BlockPos.Mutable(this.x, (double)(maxY + 1), this.z);
            boolean bl = blockView.getBlockState(mutable).isAir();
            mutable.move(Direction.DOWN);
            boolean bl3;
            for(boolean bl2 = blockView.getBlockState(mutable).isAir(); mutable.getY() > blockView.getBottomY(); bl2 = bl3) {
                mutable.move(Direction.DOWN);
                bl3 = blockView.getBlockState(mutable).isAir() || (blockView.getBlockState(mutable).getBlock()==Blocks.BEDROCK) || (blockView.getBlockState(mutable).getFluidState().isOf(Fluids.LAVA));
                if (!bl3 && bl2 && bl) {
                    return mutable.getY() + 1;
                }

                bl = bl2;
            }
            return maxY + 1;
        }
        public boolean isSafe(BlockView world, int maxY) {
            BlockPos blockPos = BlockPos.ofFloored(this.x, (double)(this.getY(world, maxY) - 1), this.z);
            BlockState blockState = world.getBlockState(blockPos);
            return blockPos.getY() < maxY && !blockState.isLiquid() && !blockState.isIn(BlockTags.FIRE);
        }
        public void setPileLocation(Random random, double minX, double minZ, double maxX, double maxZ) {
            this.x = MathHelper.nextDouble(random, minX, maxX);
            this.z = MathHelper.nextDouble(random, minZ, maxZ);
        }
    }
}

