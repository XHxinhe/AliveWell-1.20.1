package com.XHxinhe.aliveandwell.registry.events; // 包声明

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class UseItem { // 事件注册类
    public static void init() { // 初始化方法
        // 注册实体交互事件
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            Item item = player.getStackInHand(hand).getItem(); // 获取玩家手中物品
            // 判断：手持灵魂之星，且交互对象为指定生物，且天数<=32
            if (item == Items.NETHER_STAR &&
                    (entity instanceof VillagerEntity || // 村民
                            entity instanceof IronGolemEntity || // 铁傀儡
                            entity instanceof WanderingTraderEntity || // 流浪商人
                            entity instanceof CatEntity || // 猫
                            entity instanceof HorseEntity || // 马
                            entity instanceof WolfEntity) && // 狼
                    AliveAndWellMain.day <= 32) { // 天数限制
                player.sendMessage(Text.translatable("aliveandwell.soul_star"), true); // 发送提示消息
                return ActionResult.FAIL; // 阻止交互
            } else {
                return ActionResult.PASS; // 允许交互
            }
        });

        // 注册物品使用事件
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack itemStack = player.getStackInHand(hand); // 获取玩家手中物品堆
            Item item = itemStack.getItem(); // 获取物品

            if (!world.isClient) { // 只在服务端执行
                Identifier itemId = Registries.ITEM.getId(item); // 获取物品ID

                // 判断是否为torchbow
                if (itemId.getPath().equals("torchbow")) {
                    // 仅主世界可用，且天数<=7
                    if (world.getRegistryKey() == World.OVERWORLD) { // 判断是否主世界
                        if (AliveAndWellMain.day <= 7) { // 天数限制
                            player.sendMessage(Text.translatable("aliveandwell.useitem.info1").formatted(net.minecraft.util.Formatting.RED), true); // 发送红色提示
                            return TypedActionResult.fail(itemStack); // 阻止使用
                        }
                    } else if (!player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) { // 非主世界且无火焰抗性
                        player.sendMessage(Text.translatable("aliveandwell.useitem.info2").formatted(net.minecraft.util.Formatting.RED), true); // 发送红色提示
                        return TypedActionResult.fail(itemStack); // 阻止使用
                    }
                }

                // 岩浆桶限制
                if (itemId.getPath().equals("lava_bucket") && // 是岩浆桶
                        world.getRegistryKey() != World.OVERWORLD && // 非主世界
                        !player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) { // 无火焰抗性
                    player.sendMessage(Text.translatable("aliveandwell.useitem.info2").formatted(net.minecraft.util.Formatting.RED), true); // 发送红色提示
                    return TypedActionResult.fail(itemStack); // 阻止使用
                }

                // 特定mod物品限制
                String idStr = itemId.toString(); // 获取物品完整ID字符串
                if (((idStr.startsWith("bosses_of_mass_destruction:") && idStr.contains("soul_star")) || // BOSS MOD的soul_star
                        (idStr.startsWith("endrem:") && idStr.endsWith("_eye"))) && // End Remastered的_eye
                        AliveAndWellMain.day <= 32) { // 天数限制
                    player.sendMessage(Text.translatable("aliveandwell.soul_star"), true); // 发送提示
                    return TypedActionResult.fail(itemStack); // 阻止使用
                }
            }

            return TypedActionResult.pass(itemStack); // 允许使用
        });
    }
}