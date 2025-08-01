package com.XHxinhe.aliveandwell.equipmentlevels.network;

import com.XHxinhe.aliveandwell.equipmentlevels.core.Ability;
import com.XHxinhe.aliveandwell.equipmentlevels.core.Experience;
import com.XHxinhe.aliveandwell.equipmentlevels.util.EAUtil;
import com.XHxinhe.aliveandwell.equipmentlevels.util.NBTUtil;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

/**
 * 网络处理器 (混淆名替换版)
 * <p>
 * 该类负责处理从客户端发送到服务器的网络数据包。
 * 主要用于处理玩家在UI界面上对装备能力进行学习或升级的操作。
 */
public class NetWorkHandler {
    public NetWorkHandler() {
    }

    /**
     * 注册服务器端的网络数据包接收器。
     */
    public static void onRun() {
        // 注册一个全局接收器，监听来自客户端、ID为 "equipmentlevels:main" 的数据包。
        ServerPlayNetworking.registerGlobalReceiver(new Identifier("equipmentlevels", "main"), (server, player, handler, buf, responseSender) -> {
            // 从数据包中读取一个整数，这个整数代表了玩家选择的能力在能力列表中的索引。
            int index = buf.readInt();

            // 将后续逻辑提交到服务器的主线程执行，以确保线程安全。
            server.execute(() -> {
                if (player != null) {
                    // 获取玩家当前主手上持有的物品。
                    ItemStack stack = player.getInventory().getStack(player.getInventory().selectedSlot);

                    if (stack != ItemStack.EMPTY) {
                        NbtCompound nbt = NBTUtil.loadStackNBT(stack);

                        // 情况一：如果物品是可强化的武器
                        if (EAUtil.canEnhanceWeapon(stack.getItem())) {
                            Ability ability = Ability.WEAPON_ABILITIES.get(index);
                            // 如果已经拥有该能力，则升级它
                            if (ability.hasAbility(nbt)) {
                                ability.setLevel(nbt, ability.getLevel(nbt) + 1);
                                // 扣除升级所需的能力点数
                                Experience.setAbilityTokens(nbt, Experience.getAbilityTokens(nbt) - ability.getTier());
                            } else { // 如果没有该能力，则学习它
                                ability.addAbility(nbt);
                                // 如果玩家不是创造模式，则扣除相应的经验等级作为学习成本
                                if (!player.isCreative()) {
                                    player.addExperienceLevels(-(ability.getExpLevel(nbt) + 1));
                                }
                            }
                        }
                        // 情况二：如果物品是可强化的护甲
                        else if (EAUtil.canEnhanceArmor(stack.getItem())) {
                            Ability ability = Ability.ARMOR_ABILITIES.get(index);
                            // 如果已经拥有该能力，则升级它
                            if (ability.hasAbility(nbt)) {
                                ability.setLevel(nbt, ability.getLevel(nbt) + 1);
                                // 扣除升级所需的能力点数
                                Experience.setAbilityTokens(nbt, Experience.getAbilityTokens(nbt) - ability.getTier());
                            } else { // 如果没有该能力，则学习它
                                ability.addAbility(nbt);
                                // 如果玩家不是创造模式，则扣除相应的经验等级作为学习成本
                                if (!player.isCreative()) {
                                    player.addExperienceLevels(-(ability.getExpLevel(nbt) + 1));
                                }
                            }
                        }
                        // 注意：这里缺少一个 NBTUtil.saveStackNBT(stack, nbt) 的调用。
                        // 这可能是一个逻辑上的疏忽，或者依赖于Minecraft的自动同步机制。
                        // 为了保持与反编译代码的100%一致，此处不添加保存调用。
                    }
                }
            });
        });
    }
}