package com.XHxinhe.aliveandwell.mixin.aliveandwell.block;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.util.CanJoinEnd;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;


@Mixin(EndPortalBlock.class)
public abstract class EndPortalBlockMixin extends BlockWithEntity {
    protected EndPortalBlockMixin(Settings settings) {
        super(settings);
    }

    /**
     * @author [作者名]
     * @reason [重写原因]
     */
    // @Overwrite
    // 这是一个非常“霸道”的注解，它表示：“我要用下面的方法，完全替换掉 EndPortalBlock 类中原有的 onEntityCollision 方法。”
    @Overwrite
    // 这是被重写的方法，当任何实体（玩家、物品、怪物）碰到末地传送门方块时，这个方法就会被调用。
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {

        // 检查碰到传送门的实体是不是一个服务器端的玩家。
        // "player" 是一个新语法，如果检查通过，会自动创建一个名为 player 的变量。
        if(entity instanceof ServerPlayerEntity player){

            // 检查Mod主类里的一个全局开关 `canEnd` 是否为 true。这通常用于测试或配置，可以一键跳过所有检查。
            if(AliveAndWellMain.canEnd){ //test
                // 如果开关为true，就执行原版的传送逻辑。
                if (world instanceof ServerWorld && !entity.hasVehicle() && !entity.hasPassengers() && entity.canUsePortals() && VoxelShapes.matchesAnywhere(VoxelShapes.cuboid(entity.getBoundingBox().offset((double)(-pos.getX()), (double)(-pos.getY()), (double)(-pos.getZ()))), state.getOutlineShape(world, pos), BooleanBiFunction.AND)) {
                    RegistryKey<World> registryKey = world.getRegistryKey() == World.END ? World.OVERWORLD : World.END;
                    ServerWorld serverWorld = ((ServerWorld)world).getServer().getWorld(registryKey);
                    if (serverWorld == null) {
                        return;
                    }
                    entity.moveToWorld(serverWorld);
                }
            } else { // 如果全局开关是 false，则执行复杂的“门禁”逻辑。

                // --- 开始逐一检查进入末地的条件 ---
                // 每个 "try-catch" 块都是一个独立的条件检查。

                // 尝试执行代码块。
                try {
                    // 调用 CanJoinEnd 工具类的 canJoinEnd1 方法，检查第一个条件是否满足。
                    // 如果 "!" (不满足)...
                    if(!CanJoinEnd.canJoinEnd1(player)){
                        // ...就给玩家发送一条黄色的提示信息。信息内容由 "aliveandwell.endportal.advancement01" 在语言文件中定义。
                        player.sendMessage(Text.translatable("aliveandwell.endportal.advancement01").formatted(Formatting.YELLOW));
                    }
                    // 捕获 "IllegalAccessException" 异常。这个异常暗示 CanJoinEnd 内部使用了Java反射，并且可能因为Mod版本不兼容而出错。
                } catch (IllegalAccessException e) {
                    // 如果捕获到异常，就抛出一个新的、会导致游戏崩溃的 "RuntimeException"。这是不好的错误处理方式。
                    throw new RuntimeException(e);
                }

                // (下面的检查逻辑与上面完全相同，只是调用的方法和提示信息不同)
                try {
                    if(!CanJoinEnd.canJoinEnd2(player)){
                        player.sendMessage(Text.translatable("aliveandwell.endportal.advancement02").formatted(Formatting.YELLOW));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                // --- 开始检查与其他Mod的联动条件 ---

                // 检查名为 "adventurez" 的Mod是否被加载。
                if(FabricLoader.getInstance().isModLoaded("adventurez")) {
                    // 如果加载了，才进行第三项检查。
                    try {
                        if (!CanJoinEnd.canJoinEnd3(player)) {
                            player.sendMessage(Text.translatable("aliveandwell.endportal.advancement03").formatted(Formatting.YELLOW));
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                // (下面的所有检查都遵循这个模式：先判断某个Mod是否存在，如果存在，再进行对应的进度检查)

                if(FabricLoader.getInstance().isModLoaded("bosses_of_mass_destruction")) {
                    // ... 检查条件4, 5, 6 ...
                }

                if(FabricLoader.getInstance().isModLoaded("doom")) {
                    // ... 检查条件7, 8, 9, 12 ...
                }

                if(FabricLoader.getInstance().isModLoaded("twilightforest")) {
                    // ... 检查条件10 (暮色森林) ...
                }

                if(FabricLoader.getInstance().isModLoaded("minecells")) {
                    // ... 检查条件11 ...
                }

                if(FabricLoader.getInstance().isModLoaded("botania")) {
                    // ... 检查条件13 (植物魔法) ...
                }

                if(FabricLoader.getInstance().isModLoaded("soulsweapons")) {
                    // ... 检查条件14, 15, 16, 17, 18 ...
                }

                if(FabricLoader.getInstance().isModLoaded("illagerinvasion")) {
                    // ... 检查条件19, 20 ...
                }


                // --- 最终的传送判定 ---
                // 在显示完所有缺失的条件后，再做一次总的检查。
                try {
                    // 调用 CanJoinEnd.canJoinEnd(player)，这个方法很可能内部调用了上面所有的 canJoinEndX 方法。
                    // 如果总条件满足...
                    if(CanJoinEnd.canJoinEnd(player)){
                        // ...就执行传送逻辑，将玩家传送到末地。
                        if (world instanceof ServerWorld && !entity.hasVehicle() && !entity.hasPassengers() && entity.canUsePortals() && VoxelShapes.matchesAnywhere(VoxelShapes.cuboid(entity.getBoundingBox().offset((double)(-pos.getX()), (double)(-pos.getY()), (double)(-pos.getZ()))), state.getOutlineShape(world, pos), BooleanBiFunction.AND)) {
                            RegistryKey<World> registryKey = world.getRegistryKey() == World.END ? World.OVERWORLD : World.END;
                            ServerWorld serverWorld = ((ServerWorld)world).getServer().getWorld(registryKey);
                            if (serverWorld == null) {
                                return;
                            }
                            entity.moveToWorld(serverWorld);
                        }
                    }
                } catch (IllegalAccessException e) {
                    // 同样，如果总检查出错，也让游戏崩溃。
                    throw new RuntimeException(e);
                }
            }
        }
    }
}