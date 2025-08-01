package com.XHxinhe.aliveandwell.registry.events;

import com.XHxinhe.aliveandwell.registry.ItemInit;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypedActionResult;

import java.util.HashMap;
import java.util.Map;

public class EatOreAddExperience {
    // 定义每种矿物获得的经验值
    public static final int DIAMOND_XP = 500;
    public static final int ITEM_EN_GENSTONE_XP = 200;
    // public static final int EMERALD_XP = 100; // 绿宝石暂未启用
    public static final int LAPIS_LAZULI_XP = 60;
    public static final int QUARTZ_XP = 60;
    public static final int REDSTONE_XP = 60;

    // 用于存储矿物与经验值的映射关系
    private static final Map<Item, Integer> ORE_XP_MAP = new HashMap<>();

    static {
        // 初始化矿物与经验值的映射
        ORE_XP_MAP.put(Items.DIAMOND, DIAMOND_XP);
        // ORE_XP_MAP.put(Items.EMERALD, EMERALD_XP); // 绿宝石暂未启用
        ORE_XP_MAP.put(ItemInit.ITEM_EN_GENSTONE, ITEM_EN_GENSTONE_XP);
        ORE_XP_MAP.put(Items.LAPIS_LAZULI, LAPIS_LAZULI_XP);
        ORE_XP_MAP.put(Items.REDSTONE, REDSTONE_XP);
        ORE_XP_MAP.put(Items.QUARTZ, QUARTZ_XP);
    }

    /**
     * 注册矿物食用事件
     */
    public static void init() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            // 获取玩家当前手中的物品（支持主手和副手）
            ItemStack itemStack = player.getStackInHand(hand);
            // 获取物品类型
            Item item = itemStack.getItem();

            // 检查该物品是否在可食用矿物列表中
            if (ORE_XP_MAP.containsKey(item)) {
                // 仅在服务端执行经验和物品减少操作
                if (!world.isClient) {
                    int xp = ORE_XP_MAP.get(item); // 获取该矿物对应的经验值
                    int count = itemStack.getCount(); // 获取物品堆数量

                    // 判断玩家是否在潜行状态
                    if (player.isSneaking()) {
                        // 潜行时一次性吃掉全部，获得总经验
                        player.addExperience(xp * count);
                        itemStack.decrement(count); // 减少全部物品
                    } else {
                        // 非潜行时只吃一个，获得单个经验
                        player.addExperience(xp);
                        itemStack.decrement(1); // 减少一个物品
                    }
                }
                // 播放玻璃破碎音效
                world.playSound(
                        player, // 声音来源实体
                        player.getX(), player.getY(), player.getZ(), // 声音坐标
                        SoundEvents.BLOCK_GLASS_BREAK, // 声音类型
                        SoundCategory.PLAYERS, // 声音类别
                        1.0F, // 音量
                        1.0F  // 音调
                );
                // 返回操作成功，物品堆会被正确更新
                return TypedActionResult.success(itemStack);
            }

            // 其它物品不处理，交给后续事件
            return TypedActionResult.pass(itemStack);
        });
    }
}