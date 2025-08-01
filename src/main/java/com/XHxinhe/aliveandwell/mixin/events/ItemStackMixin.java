package com.XHxinhe.aliveandwell.mixin.events;

import com.XHxinhe.aliveandwell.events.ItemTooltipEvents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * 这是一个用于 ItemStack (物品堆) 的 Mixin。
 * 它的作用是创建一个自定义事件，当游戏准备显示一个物品的提示框时触发。
 * 这允许其他代码动态地向任何物品的提示框中添加信息。
 */
@Mixin(ItemStack.class)
public class ItemStackMixin {

    public ItemStackMixin() {
    }

    /**
     * 注入代码到 getTooltip 方法的末尾。
     * 这个方法在原版代码已经生成了所有默认的提示行之后执行。
     * @param player  查看提示框的玩家。
     * @param context 提示框的上下文（例如，是否为高级模式）。
     * @param cir     回调信息对象，我们可以用它来获取返回值（即提示行列表）。
     */
    @Inject(
        method = "getTooltip",
        at = @At("RETURN") // 在方法返回前执行
    )
    public void tooltipLines(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
        // 1. 获取事件的调用器 (invoker)。
        // 2. 调用在事件接口中定义的 onItemTooltip 方法。
        // 3. 将当前物品(this)、玩家、以及已经生成的提示行列表(cir.getReturnValue())作为参数传递过去。
        // 这样，所有监听了这个事件的代码都可以接收到这些信息，并对提示行列表进行修改（比如添加新的行）。
        ((ItemTooltipEvents) ItemTooltipEvents.LIVING_TICK.invoker()).onItemTooltip((ItemStack) (Object) this, player, cir.getReturnValue(), context);
    }

}