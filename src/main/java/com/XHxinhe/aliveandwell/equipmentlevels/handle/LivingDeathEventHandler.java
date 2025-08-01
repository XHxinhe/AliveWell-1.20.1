package com.XHxinhe.aliveandwell.equipmentlevels.handle;

import com.XHxinhe.aliveandwell.equipmentlevels.core.Experience;
import com.XHxinhe.aliveandwell.equipmentlevels.util.EAUtil;
import com.XHxinhe.aliveandwell.equipmentlevels.util.NBTUtil;
import com.XHxinhe.aliveandwell.events.EntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.mob.HostileEntity; // 或者更通用的 MobEntity
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

/**
 * 生物死亡事件处理器（混淆名替换版）
 * <p>
 * 监听生物死亡事件，当玩家造成击杀时，为对应的装备（近战或远程）增加经验并处理升级。
 */
public class LivingDeathEventHandler {
    public LivingDeathEventHandler() {
    }

    /**
     * 初始化事件监听器。
     */
    public static void init() {
        EntityEvents.LIVING_ENTITY_DEATH.register((world, entity, source) -> {
            // 检查伤害来源是否是玩家直接造成的
            Entity attacker = source.getAttacker();
            if (attacker instanceof PlayerEntity player) {
                ItemStack stack;
                // 检查是否是远程武器击杀（通过另一个事件处理器记录的手）
                if (LivingHurtEventHandler.bowfriendlyhand == null) {
                    // 如果是近战，获取当前活动手上的物品
                    stack = player.getStackInHand(player.getActiveHand());
                } else {
                    // 如果是远程，获取之前记录的持弓手上的物品
                    stack = player.getStackInHand(LivingHurtEventHandler.bowfriendlyhand);
                }

                NbtCompound nbt;
                // 处理近战武器
                if (stack != ItemStack.EMPTY && EAUtil.canEnhanceMelee(stack.getItem())) {
                    nbt = NBTUtil.loadStackNBT(stack);
                    if (nbt.contains("EA_ENABLED")) {
                        updateLevel(player, stack, nbt);
                        NBTUtil.saveStackNBT(stack, nbt); // 近战武器的NBT需要立即保存
                    }
                }
                // 处理远程武器
                else if (stack != ItemStack.EMPTY && EAUtil.canEnhanceRanged(stack.getItem())) {
                    nbt = NBTUtil.loadStackNBT(stack);
                    if (nbt.contains("EA_ENABLED")) {
                        updateLevel(player, stack, nbt);
                        // 远程武器的NBT可能在其他地方保存，这里不调用save
                    }
                }
            }
            // 检查伤害来源是否是玩家驯服的生物（如狼）
            else if (source.getAttacker() instanceof HostileEntity) { // 这里的类型检查可能需要根据实际情况调整
                Entity trueSource = source.getSource();
                if (trueSource instanceof PlayerEntity player) {
                    // 检查玩家主手物品（通常是剑）
                    if (source.getSource() != null) {
                        ItemStack mainHandStack = player.getInventory().getStack(player.getInventory().selectedSlot);
                        if (mainHandStack != ItemStack.EMPTY) {
                            NbtCompound nbt = NBTUtil.loadStackNBT(mainHandStack);
                            updateLevel(player, mainHandStack, nbt);
                        }
                    }
                }
            }
        });
    }

    /**
     * 辅助方法，用于更新物品的经验和等级。
     *
     * @param player 玩家
     * @param stack  要更新的物品
     * @param nbt    物品的NBT
     */
    private static void updateLevel(PlayerEntity player, ItemStack stack, NbtCompound nbt) {
        // 调用核心经验系统来计算新等级
        int newLevel = Experience.getNextLevel(player, stack, nbt, Experience.getLevel(nbt), Experience.getExperience(nbt));
        // 将新等级设置回NBT
        Experience.setLevel(nbt, newLevel);
    }
}