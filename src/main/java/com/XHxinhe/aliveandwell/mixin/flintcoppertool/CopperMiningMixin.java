package com.XHxinhe.aliveandwell.mixin.flintcoppertool;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 这是一个用于 PlayerEntity (玩家实体) 的 Mixin。
 * 它的核心功能是修改游戏规则，允许挖掘等级为0的工具（如木镐、金镐）也能成功开采铜矿石。
 * 这很可能是为了配合Mod中新增的燧石工具或铜制工具，让玩家在游戏早期就能利用这些新工具获取铜。
 */
@Mixin(PlayerEntity.class)
public abstract class CopperMiningMixin extends LivingEntity {

    @Shadow @Final
    private PlayerInventory inventory;

    /**
     * 注入代码到 canHarvest 方法的开头。
     * 这个方法决定了玩家是否能用当前工具成功开采一个方块（即获得掉落物）。
     * @param state 玩家正在尝试开采的方块的状态。
     * @param cir   回调信息对象，我们可以用它来提前设置返回值并取消原方法的执行。
     */
    @Inject(
        method = "canHarvest(Lnet/minecraft/block/BlockState;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void canHarvest(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        // 获取玩家主手上的物品
        Item heldItem = this.inventory.getMainHandStack().getItem();

        // 检查玩家手上拿的是不是一个采矿工具 (镐、斧、锹、锄)
        if (heldItem instanceof PickaxeItem) {
            // 获取该工具的挖掘等级 (0=木/金, 1=石, 2=铁, 3=钻石, 4=下界合金)
            int miningLevel = ((PickaxeItem) heldItem).getMaterial().getMiningLevel();
            // 获取目标方块的注册名，例如 "minecraft:copper_ore"
            String blockName = Registries.BLOCK.getId(state.getBlock()).toString();
            // 如果工具的挖掘等级是0，并且目标方块是铜矿石...
            if (miningLevel == 0 && blockName.equals("minecraft:copper_ore"))
                // ...那么就直接设置返回值为 true，告诉游戏“可以开采！”
                cir.setReturnValue(true);
        }
    }

    protected CopperMiningMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

}