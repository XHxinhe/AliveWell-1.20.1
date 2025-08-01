package com.XHxinhe.aliveandwell.mixin.spawnerlimit;

import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 这是一个用于 MobSpawnerLogic (刷怪笼逻辑) 的 Mixin。
 * 它的核心功能是给刷怪笼设置一个生成上限。当刷怪笼生成的怪物达到16个后，
 * 它将自我禁用并提供视觉/听觉反馈，将其变为一种消耗性资源。
 */
@Mixin(MobSpawnerLogic.class)
public abstract class MobSpawnerLogicMixin {
    @Unique
    private short spawns = 0;
    public MobSpawnerLogicMixin() {
    }

    /**
     * 注入点一：在 serverTick 方法中，就在刷怪笼成功生成怪物并同步世界事件（冒烟效果）之后。
     * 作用：每次成功刷怪后，更新我们的计数器和刷怪笼参数。
     */
    @Inject(
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V"
            )},
            method = {"serverTick(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V"}
    )
    public void entitySpawn(ServerWorld world, BlockPos pos, CallbackInfo ca) {
        // --- 修改刷怪笼参数 ---
        // 这里使用了 Mixin Accessor (通过强制类型转换调用)，来修改原版的私有字段。
        // 这使得刷怪笼的激活范围更远，刷怪速度更快。
        ((MobSpawnerLogicAccessor)this).setRequiredPlayerRange(24);
        ((MobSpawnerLogicAccessor)this).setMinSpawnDelay(100);
        ((MobSpawnerLogicAccessor)this).setMaxSpawnDelay(400);

        NbtCompound nbt = new NbtCompound();
        nbt = ((MobSpawnerLogic)(Object)this).writeNbt(nbt);
        String entity_string = nbt.get("SpawnData").toString();
        entity_string = entity_string.substring(entity_string.indexOf("\"") + 1);
        entity_string = entity_string.substring(0, entity_string.indexOf("\""));
        if (!entity_string.contains("area_effect_cloud")) {
            ++this.spawns;
        }
    }
    /**
     * 注入点二：在 serverTick 方法中，就在游戏即将从NBT数据创建实体之前。
     * 作用：检查生成数量是否已达上限。如果达到，则取消刷怪并禁用刷怪笼。
     */
    @Inject(
            at = {@At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/entity/EntityType;fromNbt(Lnet/minecraft/nbt/NbtCompound;)Ljava/util/Optional;"
            )},
            method = {"serverTick(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V"},
            cancellable = true
    )
    public void cancel(ServerWorld world, BlockPos pos, CallbackInfo ci) {

        world.getBlockEntity(pos).markDirty();
        world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 3);

        if (this.spawns >= 16) {
            NbtCompound nbt = new NbtCompound();
            nbt = ((MobSpawnerLogic)(Object)this).writeNbt(nbt);
            nbt.putShort("RequiredPlayerRange", (short) 0);
            ((MobSpawnerLogic)(Object)this).readNbt(world, pos, nbt);
//            ((MobSpawnerLogic)(Object)this).setEntityId(EntityType.AREA_EFFECT_CLOUD,world,Random.create(),pos);
            ((MobSpawnerLogic)(Object)this).setEntityId(EntityType.AREA_EFFECT_CLOUD,world,world.random,pos);
            world.syncWorldEvent(WorldEvents.LAVA_EXTINGUISHED, pos, 0);
            ci.cancel();
        }

    }

    /**
     * 注入 readNbt 方法，用于从世界存档中加载我们的 spawns 计数。
     */
    @Inject(
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/nbt/NbtCompound;getShort(Ljava/lang/String;)S"
            )},
            method = {"readNbt"}
    )
    private void readNbt(World world, BlockPos pos, NbtCompound nbt, CallbackInfo info) {
        this.spawns = nbt.getShort("spawns");
    }

    /**
     * 注入 writeNbt 方法，用于将我们的 spawns 计数保存到世界存档中。
     */
    @Inject(
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/nbt/NbtCompound;putShort(Ljava/lang/String;S)V"
            )},
            method = {"writeNbt"}
    )
    private void writeNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> info) {
        nbt.putShort("spawns", this.spawns);
    }


}
