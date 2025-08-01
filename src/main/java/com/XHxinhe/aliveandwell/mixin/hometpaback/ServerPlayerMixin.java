package com.XHxinhe.aliveandwell.mixin.hometpaback;

import com.XHxinhe.aliveandwell.hometpaback.HomeComponent;
import com.XHxinhe.aliveandwell.hometpaback.util.IStoreHome;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是一个用于 ServerPlayerEntity (服务器玩家实体) 的 Mixin。
 * 它是 Home/TPA/Back 功能的核心数据层。
 * 它将家的列表和返回点位置直接附加到玩家实体上，并处理数据的保存、加载和同步。
 */
@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerMixin  extends PlayerEntity implements IStoreHome {
    @Shadow @Final public MinecraftServer server;
    @Unique
    private List<HomeComponent> homes = new ArrayList<>();
    @Unique
    private int maxHomes;

    @Unique
    public Vec3d backPos = null;
    @Unique
    public RegistryKey<World> backDimension = null;
    public ServerPlayerMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Shadow public abstract void sendMessage(Text message);



    /**
     * 注入 readCustomDataFromNbt 方法，用于从NBT中读取并加载玩家的家数据。
     * 这个方法在玩家登录服务器时被调用。
     */

    @Inject(at = @At("RETURN"), method = "readCustomDataFromNbt")
    public void readAdditionalSaveData(NbtCompound nbt, CallbackInfo ci) {
        this.homes.clear();
        NbtList list = nbt.getList("homes_homeback", 10);
        for (NbtElement t : list) {
            NbtCompound homeNbt = (NbtCompound) t;
            HomeComponent home = new HomeComponent(homeNbt.getDouble("x"),homeNbt.getDouble("y"), homeNbt.getDouble("z"),homeNbt.getFloat("pitch"),homeNbt.getFloat("yaw"), RegistryKey.of(RegistryKeys.WORLD, new Identifier(homeNbt.getString("dim"))),homeNbt.getString("name"));
            this.homes.add(home);
        }

        // 加载最大家的数量
        this.maxHomes = nbt.getInt("maxHomes_homeback");

        // 加载 /back 的位置信息
        NbtCompound data = null;
        if (nbt.contains("back_homeback")) {
            data = nbt.getCompound("back_homeback");
        }
        if (data != null && data.contains("x") && data.contains("y") && data.contains("z") && data.contains("dim")) {
            this.backPos = new Vec3d(data.getDouble("x"), data.getDouble("y"), data.getDouble("z"));
            this.backDimension = RegistryKey.of(RegistryKeys.WORLD, new Identifier(data.getString("dim")));
        }
    }

    /**
     * 注入 writeCustomDataToNbt 方法，用于将玩家的家数据写入NBT进行保存。
     * 这个方法在服务器保存玩家数据时（如玩家下线、服务器关闭）被调用。
     */
    @Inject(at = @At("RETURN"), method = "writeCustomDataToNbt")
    public void addAdditionalSaveData(NbtCompound nbt, CallbackInfo ci) {
        NbtList homeTag = new NbtList();
        this.homes.forEach((homeComponent) -> {
            if(homeComponent != null){
                NbtCompound data = new NbtCompound();
                data.putDouble("x",homeComponent.getX());
                data.putDouble("y",homeComponent.getY());
                data.putDouble("z",homeComponent.geyZ());
                data.putFloat("pitch",homeComponent.getPitch());
                data.putFloat("yaw",homeComponent.getYaw());
                data.putString("dim",homeComponent.getDimID().getValue().toString());
                data.putString("name",homeComponent.getName());
                homeTag.add(data);
            }
        });
        nbt.put("homes_homeback", homeTag);
        nbt.putInt("maxHomes_homeback", this.maxHomes);

        // 保存 /back 的位置信息
        if (this.backPos != null && this.backDimension != null) {
            NbtCompound data = new NbtCompound();
            data.putDouble("x", this.backPos.x);
            data.putDouble("y", this.backPos.y);
            data.putDouble("z", this.backPos.z);
            data.putString("dim", this.backDimension.getValue().toString());
            nbt.put("back_homeback", data);
        }
    }

    /**
     * 注入 onDeath 方法，在玩家死亡时记录其位置。
     */

    @Inject(at = @At("HEAD"), method = "onDeath")
    public void onDeath(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        // 记录死亡时的精确坐标和维度
        this.backPos = new Vec3d(player.getX(), player.getY(), player.getZ());
        this.backDimension = player.getWorld().getRegistryKey();
        player.sendMessage(Text.translatable("aliveandwell.hometpaback.setback").append(Text.of("("+player.getBlockPos().getX()+","+ player.getBlockPos().getY()+","+player.getBlockPos().getZ()+")"+"【"+backDimension.getValue().getPath()+"】")).formatted(Formatting.LIGHT_PURPLE));

//        plusHealth  = (this.experienceLevel/5) * 2;
//        this.sendMessage(Text.translatable("死亡时plusHealth=" + plusHealth));
    }

    /**
     * 注入 copyFrom 方法，用于在玩家重生或跨维度后，将数据从旧的玩家对象复制到新的对象。
     */
    @Inject(at = @At("RETURN"), method = "copyFrom")
    public void restoreFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        ServerPlayerMixin serverPlayerMixin = (ServerPlayerMixin) (Object) oldPlayer;
        homes = serverPlayerMixin.homes;
        backPos = serverPlayerMixin.backPos;
        backDimension = serverPlayerMixin.backDimension;
    }

    @Override
    public List<HomeComponent> getHomes() {
        return this.homes;
    }

    @Override
    public int getMaxHomes() {
        return this.maxHomes;
    }

    @Override
    public boolean addHome(HomeComponent home) {
        // 如果不存在同名家，则添加成功
        if (this.homes.stream().anyMatch(v -> v.getName().equalsIgnoreCase(home.getName()))) {
            return false;
        }
        return this.homes.add(home);
    }

    @Override
    public boolean removeHome(String name) {
        // 如果存在该名称的家，则移除成功
        if (this.homes.stream().noneMatch(v -> v.getName().equalsIgnoreCase(name))) {
            return false;
        }
        return this.homes.removeIf(v -> v.getName().equalsIgnoreCase(name));
    }

    @Override
    public void addBack(Vec3d pos, RegistryKey<World> dimension) {
        this.backPos = pos;
        this.backDimension = dimension;
    }
    @Override
    public Pair<Vec3d, RegistryKey<World>> getBack() {
        return Pair.of(this.backPos, this.backDimension);
    }
}
