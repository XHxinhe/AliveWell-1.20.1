package com.XHxinhe.aliveandwell.item.xpitem; // 包声明：定义此类所在的包

import com.XHxinhe.aliveandwell.xpgui.common.XPStates; // 导入XP状态管理类
import com.XHxinhe.aliveandwell.xpgui.network.PlayerStatsServerPacket; // 导入用于同步玩家数据的数据包类
import java.util.List; // 导入列表类

import net.minecraft.client.item.TooltipContext; // 导入物品提示上下文
import net.minecraft.entity.player.PlayerEntity; // 导入玩家实体类
import net.minecraft.item.Item; // 导入物品基类
import net.minecraft.item.ItemStack; // 导入物品堆类
import net.minecraft.server.network.ServerPlayerEntity; // 导入服务端玩家实体类
import net.minecraft.text.Text; // 导入文本类
import net.minecraft.util.Formatting; // 导入文本格式化类
import net.minecraft.util.Hand; // 导入手部枚举
import net.minecraft.util.TypedActionResult; // 导入带返回值的操作结果
import net.minecraft.world.World; // 导入世界类

/**
 * 大型经验上限提升道具
 * Large XP Max Increase Item
 */
public class XpItem1 extends Item { // 声明一个继承自Item的类 XpItem1
    public XpItem1(Settings settings) { // 构造方法，传入物品设置
        super(settings); // 调用父类构造方法
    }

    /**
     * 右键使用道具时触发
     * Called when the item is used (right-click)
     */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand); // 获取玩家手中的该物品

        // 只在服务端执行
        if (!world.isClient) { // 判断是否为服务端
            XPStates xpStates = XPStates.getForPlayer(player); // 获取玩家的XP状态对象
            int newMaxXp = xpStates.GetMaxXp() + 500; // 新的最大经验值为当前+500

            if (newMaxXp <= 20000) { // 如果新的最大经验值不超过20000
                xpStates.SetMaxXp(newMaxXp); // 设置玩家新的最大经验值

                // 同步数据包到客户端
                if (player instanceof ServerPlayerEntity serverPlayer) { // 如果是服务端玩家
                    PlayerStatsServerPacket.writeS2CXPPacket(xpStates, serverPlayer); // 发送同步数据包
                }

                // 消耗道具
                itemStack.decrement(1); // 物品数量减1

                // 发送提示消息
                player.sendMessage(
                        Text.translatable("aliveandwell.xpgui.info2") // 可翻译的文本（前缀）
                                .append(Text.literal(xpStates.getXp() + "/" + xpStates.GetMaxXp())) // 拼接当前经验/最大经验
                                .formatted(Formatting.YELLOW), // 设置文本颜色为黄色
                        true // 消息显示在操作栏
                );
            } else { // 如果超过最大上限
                // 达到上限提示
                player.sendMessage(
                        Text.translatable("aliveandwell.xpgui.info5").formatted(Formatting.YELLOW), // 显示上限提示，黄色
                        true // 消息显示在操作栏
                );
            }
        }

        return TypedActionResult.success(itemStack); // 返回操作成功和物品堆
    }

    /**
     * 物品是否有附魔光效
     * Whether the item has an enchanted glint
     */
    @Override
    public boolean hasGlint(ItemStack stack) {
        return true; // 始终显示附魔光效
    }

    /**
     * 添加物品提示文本
     * Add item tooltip
     */
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable("item.aliveandwell.xp_item1.info1")); // 添加第一行提示
        tooltip.add(Text.translatable("item.aliveandwell.xp_item1.info2")); // 添加第二行提示
    }
}