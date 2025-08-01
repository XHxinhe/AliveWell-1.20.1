package com.XHxinhe.aliveandwell.mixin.aliveandwell.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * 这是一个用于 NetherPortalBlock (下界传送门方块) 的 Mixin。
 * 它的核心功能是彻底改变下界传送门的行为，使其对不同类型的实体有不同的反应。
 * 具体来说，它完全禁用了玩家通过下界传送门进行传送的功能，取而代之的是向玩家发送一条提示信息。
 * 然而，它保留了非玩家实体（如怪物、动物、掉落物）通过传送门的能力。
 * 这通常用于整合包或Mod中，强制玩家通过其他方式进入下界，例如完成某个任务或使用特定的传送物品。
 */
@Mixin(NetherPortalBlock.class) // @Mixin 注解，告诉处理器我们要修改原版的 NetherPortalBlock 类。
public class NetherPortalBlockMixin {

    /**
     * @author [作者名]
     * @reason [重写原因]
     */
    // @Overwrite 注解，表示我们要用下面的方法，完全替换掉 NetherPortalBlock 类中原有的 onEntityCollision 方法。
    // 原版方法的作用是让实体进入传送门后开始传送。
    @Overwrite
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        // --- 核心逻辑 ---
        // 检查接触到传送门的实体是不是一个玩家。
        if(entity instanceof PlayerEntity){
            // 如果是玩家，就执行这里的代码：

            // 将实体对象强制转换为玩家实体对象。
            // (Object) 是一种有时为了让编译器通过而使用的转换技巧。
            PlayerEntity player = (PlayerEntity)(Object)entity;

            // 向该玩家发送一条消息。
            // Text.translatable(...) 会从语言文件中查找键为 "aliveandwell.netherportal.collision" 的文本并显示给玩家。
            // 这条消息很可能会提示玩家“此传送门已失效”或“你需要通过其他方式前往下界”。
            // 注意：这里没有执行任何传送逻辑，所以玩家无法通过这个门。
            player.sendMessage(Text.translatable("aliveandwell.netherportal.collision"));

            // 如果接触传送门的实体不是玩家...
        } else if(!entity.hasVehicle() && !entity.hasPassengers() && entity.canUsePortals()) {
            // ...并且它没有骑乘其他实体、没有被其他实体骑乘、并且它本身可以传送（例如，猪、僵尸、掉落的物品等），
            // 那么就执行这里的代码：

            // 调用原版的一个方法，让实体进入“传送门内”的状态。
            // 这会触发屏幕晃动效果和传送倒计时，最终将这个非玩家实体传送到下界。
            entity.setInNetherPortal(pos);
        }
    }
}