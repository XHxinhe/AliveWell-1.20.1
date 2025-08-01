package com.XHxinhe.aliveandwell.registry.events;

import com.XHxinhe.aliveandwell.dimensions.DimsRegistry;
import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.flintcoppertool.utils.FlintKnapEvent;
import com.XHxinhe.aliveandwell.registry.ItemInit;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;

import java.util.Objects;

import static net.minecraft.block.Block.dropStack;

public class UseBlock {


    public static void init(){
        UseEntityCallback.EVENT.register( (player, world, hand, entity, hitResult) -> {
            String name = Registries.ENTITY_TYPE.getId(entity.getType()).toString();
            if(name.contains("pswg:")){
                if(name.contains("xwing_t65b") || name.contains("landspeeder_x34")  || name.contains("zephyr_j")){
                    return ActionResult.FAIL;
                }
            }
            if(player.getStackInHand(hand).getItem() == Items.BUCKET){
                if(entity instanceof HostileEntity){
                    return ActionResult.FAIL;
                }

                if(!(name.contains("mythicmounts:") || name.contains("uselessreptile:")
                        || name.contains("horse") || name.contains("rlovelyr:")
                        || name.contains("minecraft:") && name.contains("camel"))){
                    if(!player.isSneaking()){
                        return ActionResult.FAIL;
                    }
                }
            }

            return ActionResult.PASS;
        });

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockPos pos = player.getBlockPos();
            BlockPos pos1 = hitResult.getBlockPos();
            int y = pos.getY();
            ItemStack itemStack = player.getStackInHand(hand);
            Item item = itemStack.getItem();
            Block block = world.getBlockState(hitResult.getBlockPos()).getBlock();

            //levelZ
//            PlayerStatsManager playerStatsManager = ((PlayerStatsManagerAccess)player).getPlayerStatsManager();

            if (!player.getWorld().isClient){
//                if(block == Blocks.STONE){
//                    long i = player.getWorld().getTime();
//                    long i1 = player.getWorld().getTime()-(AliveAndWellMain.day-1)*24000L;
//                    player.sendMessage(Text.translatable("当前时间为："+i).formatted(Formatting.YELLOW));
//                    player.sendMessage(Text.translatable("当前时间1为："+i1).formatted(Formatting.YELLOW));
//                    player.sendMessage(Text.translatable("当前时间1为："+i1).formatted(Formatting.YELLOW));
//                }

                if(block instanceof CampfireBlock){
                    world.removeBlock(hitResult.getBlockPos(),true);
                }

                if(block == Blocks.BREWING_STAND){
                    PlayerAdvancementTracker tracker = Objects.requireNonNull(player.getServer()).getPlayerManager().getAdvancementTracker((ServerPlayerEntity) player);
                    AdvancementProgress progress = null;

                    //成就：获取烈焰棒
                    Advancement advancement = player.getServer().getAdvancementLoader().get(Identifier.tryParse("minecraft:nether/obtain_blaze_rod"));
                    if (advancement != null) {
                        progress = tracker.getProgress(advancement);
                    }

                    if (progress != null) {
                        if (!progress.isDone()) {
                            player.sendMessage(Text.translatable("aliveandwell.useblock.info1").formatted(Formatting.YELLOW));
                            return ActionResult.FAIL;
                        }
                    }
                }

                if(item instanceof BlockItem blockItem){
//                    if(blockItem.getBlock() instanceof TorchBlock || blockItem.getBlock() instanceof LanternBlock || blockItem.getBlock() instanceof CampfireBlock){
//                        if (player.getWorld().getRegistryKey() != World.OVERWORLD) {
//                            if(!player.hasStatusEffect(StatusEffects.NIGHT_VISION)){
//                                player.sendMessage(Text.translatable("aliveandwell.useitem.info2").formatted(Formatting.YELLOW));
//                                return ActionResult.FAIL;
//                            }
//                        }
//                    }

                    if(blockItem.getBlock().getDefaultState().getLuminance() > 5 ){
                        if (player.getWorld().getRegistryKey() != World.OVERWORLD) {
                            if(!player.hasStatusEffect(StatusEffects.NIGHT_VISION)){
                                player.sendMessage(Text.translatable("aliveandwell.useitem.info2").formatted(Formatting.YELLOW));
                                return ActionResult.FAIL;
                            }
                        }
                    }
                }

                if(!player.isCreative()){
//                    if(item instanceof BlockItem blockItem){
//                        if(FabricLoader.getInstance().isModLoaded("levelz")){
//                            if (PlayerStatsManager.listContainsItemOrBlock(player, Registry.BLOCK.getRawId(blockItem.getBlock()), 1)){
//                                player.sendMessage(Text.translatable("未达到该方块挖掘等级，无法放置").formatted(Formatting.LIGHT_PURPLE));
//                                return ActionResult.FAIL;
//                            }
//                        }
//                    }
                    if(Registries.BLOCK.getId(block).toString().contains("command_block") ){
                        world.removeBlock(hitResult.getBlockPos(),true);
                    }

                    //无法使用拆解台
                    if(Registries.BLOCK.getId(block).toString().contains("twilightforest:") && Registries.BLOCK.getId(block).toString().contains("uncrafting_table")){
                        world.removeBlock(hitResult.getBlockPos(),true);
                    }

                    //植物魔法附魔台
                    if(Registries.BLOCK.getId(block).toString().contains("botania:") && Registries.BLOCK.getId(block).toString().contains("alchemy_catalyst")){
                        world.removeBlock(hitResult.getBlockPos(),true);
                    }
                    if(Registries.BLOCK.getId(block).toString().contains("botania:") && Registries.BLOCK.getId(block).toString().contains("enchanter")){
                        world.removeBlock(hitResult.getBlockPos(),true);
                    }

                    //放置刷怪蛋空间限制
                    if(Registries.ITEM.getId(item).toString().contains("spawn_egg")){
                        if(!Registries.ITEM.getId(item).toString().contains("minecraft")) {
                            int xbox = hitResult.getBlockPos().getX();
                            int ybox = hitResult.getBlockPos().getY();
                            int zbox = hitResult.getBlockPos().getZ();
                            int ix;
                            int iy;
                            int iz;
                            Vec3d vec3d;
                            Vec3d vec3d1 = new Vec3d(xbox, ybox + 1, zbox);
                            int hasBlock = 0;
                            for (int i = -5; i <= 5; i++) {
                                ix = xbox + i;
                                for (int j = 0; j <= 15; j++) {
                                    iy = ybox + i;
                                    for (int k = -5; k <= 5; k++) {
                                        iz = zbox + k;
                                        vec3d = new Vec3d(ix, iy, iz);
                                        if (player.getWorld().raycast(new RaycastContext(vec3d1, vec3d, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player)).getType() == HitResult.Type.MISS) {
                                            hasBlock++;
                                        }
                                    }
                                }
                            }
                            if (hasBlock < 800) {
                                player.sendMessage(Text.translatable("aliveandwell.useblock.info2").formatted(Formatting.RED));
                                return ActionResult.FAIL;
                            }
                        }
                    }


                    if(player.isSubmergedInWater()){
                        if(item instanceof BlockItem blockItem){
                            if(blockItem.getBlock() instanceof DoorBlock || blockItem.getBlock() instanceof TrapdoorBlock
                                    || blockItem.getBlock() == Blocks.MAGMA_BLOCK
                                    || blockItem.getBlock() instanceof FenceGateBlock
                            ){
                                return ActionResult.FAIL;//栅栏门
                            }
                        }

                    }

                    if(item == Items.FLINT_AND_STEEL){
                        if(block == Blocks.OBSIDIAN){
                            return ActionResult.FAIL;
                        }
                    }

                    if(item == ItemInit.FLINT_AND_STEEL){
                        world.playSound(player, pos1, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f, world.getRandom().nextFloat() * 0.4f + 0.8f);
                        itemStack.damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
                    }

                    if(Registries.BLOCK.getId(block).toString().contains("byg:") && Registries.BLOCK.getId(block).toString().contains("crafting_table")){
                        world.removeBlock(hitResult.getBlockPos(),true);
                    }

                    //合成蓝图
                    if(Registries.BLOCK.getId(block).toString().contains("create:") && Registries.BLOCK.getId(block).toString().contains("crafting_blueprint")){
                        world.removeBlock(hitResult.getBlockPos(),true);
                    }

                    //镐子右击黑曜石掉落铜矿
                    if(item instanceof PickaxeItem pickaxeItem && pickaxeItem.getMaterial().getMiningLevel() >= 2 && (block==Blocks.OBSIDIAN || block==Blocks.CRYING_OBSIDIAN)){
//                        if(FabricLoader.getInstance().isModLoaded("levelz")){
//                            if (PlayerStatsManager.listContainsItemOrBlock(player, Registry.BLOCK.getRawId(block), 1)){
//                                player.sendMessage(Text.translatable("未达到该方块挖掘等级，无法挖掘").formatted(Formatting.LIGHT_PURPLE));
//                                return ActionResult.FAIL;
//                            }else {
                        double r1 = FlintKnapEvent.RANDOM.nextDouble();
                        double r2 = FlintKnapEvent.RANDOM.nextDouble();
                        if (r1 <= 0.1) {
                            if (r2 <= 0.8) {
                                world.removeBlock(pos1,true);
                                dropStack(world, pos1, new ItemStack(Items.OBSIDIAN,1));
                            }
                            world.playSound(player,pos1, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);

                        }else{
                            world.playSound(null, pos, SoundEvents.BLOCK_STONE_HIT,
                                    SoundCategory.PLAYERS, 1.0F, 1.0F);
                        }

                        player.addExhaustion(0.1f);
                        ItemStack itemStackHand = player.getStackInHand(hand);
                        itemStackHand.damage(1,player, (player1) -> player.sendToolBreakStatus(hand));

                        return ActionResult.SUCCESS;
//                            }
//                        }
                    }

                    //传送门高度限制*************************************************************************
                    if(item == Items.AMETHYST_BLOCK  &&  y >= -35  && (player.getWorld().getDimensionKey() == DimensionTypes.OVERWORLD ||  player.getWorld().getDimensionKey() == DimensionTypes.OVERWORLD_CAVES )){
                        player.sendMessage(Text.translatable("aliveandwell.useblock.info3"));
                        return ActionResult.FAIL;
                    }

                    if(item == Items.BUDDING_AMETHYST  &&  ((y >= -35  && player.getWorld().getRegistryKey() == DimsRegistry.UNDER_WORLD_KEY)
                            || !( player.getWorld().getRegistryKey() == DimsRegistry.UNDER_WORLD_KEY || player.getWorld().getRegistryKey() == World.NETHER))){
                        player.sendMessage(Text.translatable("aliveandwell.useblock.info4"));
                        return ActionResult.FAIL;
                    }

                    if(item instanceof BedItem && ( player.getWorld().getRegistryKey() == DimsRegistry.UNDER_WORLD_KEY)){
                        player.sendMessage(Text.translatable("aliveandwell.useblock.info5"));
                        return ActionResult.FAIL;
                    }

                    if(Registries.ITEM.getId(item).toString().contains("bosses_of_mass_destruction:") && Registries.ITEM.getId(item).toString().contains("soul_star")
                            || Registries.ITEM.getId(item).toString().contains("endrem:") && Registries.ITEM.getId(item).toString().contains("_eye")){
                        if(AliveAndWellMain.day <= 32) {
                            player.sendMessage(Text.translatable("aliveandwell.soul_star"));
                            return ActionResult.FAIL;
                        }
                    }

                    if(player.getHungerManager().getFoodLevel() == 0){
                        if(block.getDefaultState().hasBlockEntity()){
                            return ActionResult.PASS;
                        }else if(block instanceof DoorBlock){
                            return ActionResult.PASS;
                        }else if(block instanceof TrapdoorBlock){
                            return ActionResult.PASS;
                        }else if(block instanceof ButtonBlock){
                            return ActionResult.PASS;
                        }else if(block instanceof FenceGateBlock){
                            return ActionResult.PASS;
                        }else if(block instanceof LeverBlock){
                            return ActionResult.PASS;
                        }else if(player.getMainHandStack().isFood()){
                            return ActionResult.PASS;
                        }else if(item==Items.CAKE || block instanceof CakeBlock || block instanceof CandleCakeBlock || block instanceof SweetBerryBushBlock ){
                            return ActionResult.PASS;
                        }

                        player.sendMessage(Text.translatable("aliveandwell.useblock.info6"));
                        return ActionResult.FAIL;
                    }
                    if(player.getHungerManager().getFoodLevel() >= 6 + ((int)Math.floor(player.experienceLevel / 5.0)*2) ) {
                        if (block instanceof CakeBlock || block instanceof CandleCakeBlock) {
                            return ActionResult.FAIL;
                        }
                    }
                }
            }

            return ActionResult.PASS;
        });

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockState blockState = world.getBlockState(hitResult.getBlockPos());
            Block block = blockState.getBlock();
            ItemStack itemStack = player.getStackInHand(hand);
            if (!player.getWorld().isClient) {
                //无法使用原版熔炉、工作台、高炉
                if(block == Blocks.CRAFTING_TABLE){
                    world.removeBlock(hitResult.getBlockPos(),true);
                }

                //禁用“你要去的生物群落”工作台
                if(Registries.BLOCK.getId(block).toString().contains("byg:") && Registries.BLOCK.getId(block).toString().contains("crafting_table")){
                    world.removeBlock(hitResult.getBlockPos(),true);
                }

                //生成夜巫妖bosses_of_mass_destruction:lich
                if(itemStack.getItem() == ItemInit.lich_spawn){
                    if(FabricLoader.getInstance().isModLoaded("bosses_of_mass_destruction")) {
                        try {
                            execute((ServerPlayerEntity)player, hitResult.getPos(), new NbtCompound(),false,"bosses_of_mass_destruction:lich");
                            itemStack.split(1);
                        } catch (CommandSyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                if(itemStack.getItem() == ItemInit.void_blossom_spawn){
                    if(FabricLoader.getInstance().isModLoaded("bosses_of_mass_destruction")) {
                        try {
                            execute((ServerPlayerEntity)player, hitResult.getPos(), new NbtCompound(),false,"bosses_of_mass_destruction:void_blossom");
                            itemStack.split(1);
                        } catch (CommandSyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                if(itemStack.getItem() == ItemInit.draugr_boss_spawn){
                    if(FabricLoader.getInstance().isModLoaded("soulsweapons")) {
                        try {
                            execute((ServerPlayerEntity)player, hitResult.getPos(), new NbtCompound(),false,"soulsweapons:draugr_boss");
                            itemStack.split(1);
                        } catch (CommandSyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            return ActionResult.PASS;
        });
    }

    private static final SimpleCommandExceptionType FAILED_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.summon.failed"));
    private static final SimpleCommandExceptionType FAILED_UUID_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.summon.failed.uuid"));
    private static final SimpleCommandExceptionType INVALID_POSITION_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.summon.invalidPosition"));

    public static Entity summon(ServerPlayerEntity source, Vec3d pos, NbtCompound nbt, boolean initialize,String name) throws CommandSyntaxException {
        BlockPos blockPos = BlockPos.ofFloored(pos);
        if (!World.isValid(blockPos)) {
            throw INVALID_POSITION_EXCEPTION.create();
        }
        NbtCompound nbtCompound = nbt.copy();
        nbtCompound.putString("id", name);
        ServerWorld serverWorld = (ServerWorld) source.getWorld();
        Entity entity2 = EntityType.loadEntityWithPassengers(nbtCompound, serverWorld, entity -> {
            entity.refreshPositionAndAngles(pos.x, pos.y, pos.z, entity.getYaw(), entity.getPitch());
            return entity;
        });
        if (entity2 == null) {
            throw FAILED_EXCEPTION.create();
        }
        if (initialize && entity2 instanceof MobEntity) {
            ((MobEntity)entity2).initialize((ServerWorldAccess) source.getWorld(), source.getWorld().getLocalDifficulty(entity2.getBlockPos()), SpawnReason.COMMAND, null, null);
        }
        if (!serverWorld.spawnNewEntityAndPassengers(entity2)) {
            throw FAILED_UUID_EXCEPTION.create();
        }
        return entity2;
    }

    private static void execute(ServerPlayerEntity source, Vec3d pos, NbtCompound nbt, boolean initialize ,String name) throws CommandSyntaxException {
        Entity entity = summon(source,pos, nbt, initialize,name);
        source.sendMessage(Text.translatable("commands.summon.success", entity.getDisplayName()), true);
    }
}
