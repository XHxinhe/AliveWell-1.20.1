package com.XHxinhe.aliveandwell.equipmentlevels.handle;

import com.XHxinhe.aliveandwell.equipmentlevels.core.Ability;
import com.XHxinhe.aliveandwell.equipmentlevels.core.Experience;
import com.XHxinhe.aliveandwell.equipmentlevels.util.EAUtil;
import com.XHxinhe.aliveandwell.equipmentlevels.util.NBTUtil;
import com.XHxinhe.aliveandwell.events.ItemTooltipEvents;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Iterator;

/**
 * 物品提示信息事件处理器（混淆名替换版）
 * <p>
 * 负责向可强化装备的Tooltip中添加等级、经验和能力信息。
 */
public class ItemTooltipEventHandler {
    public ItemTooltipEventHandler() {
    }

    /**
     * 注册并定义添加物品提示信息的逻辑。
     */
    public static void addInformation() {
        // 注册一个事件监听器，当构建物品Tooltip时触发
        ItemTooltipEvents.LIVING_TICK.register((itemStack, entityPlayer, list, flags) -> {
            Item item = itemStack.getItem();

            // 检查物品是否是可强化的
            if (EAUtil.canEnhance(item)) {
                NbtCompound nbt = NBTUtil.loadStackNBT(itemStack);

                // 检查该物品的强化系统是否已启用
                if (Experience.isEnabled(nbt)) {
                    int level = Experience.getLevel(nbt);
                    int experience = Experience.getExperience(nbt);
                    int maxExperience = Experience.getMaxLevelExp(level);

                    // 添加等级信息
                    if (level >= 25) {
                        // 已达最高等级
                        String levelText = I18n.translate("enhancedarmaments.misc.level");
                        list.add(Text.literal(levelText + ": " + Formatting.GOLD + I18n.translate("enhancedarmaments.misc.max")));
                    } else {
                        String levelText = I18n.translate("enhancedarmaments.misc.level");
                        list.add(Text.literal(levelText + ": " + Formatting.AQUA + (level - 1)));
                    }

                    // 添加经验值信息
                    if (level >= 25) {
                        String expText = I18n.translate("enhancedarmaments.misc.experience");
                        list.add(Text.literal(expText + ": " + I18n.translate("enhancedarmaments.misc.max")));
                    } else {
                        String expText = I18n.translate("enhancedarmaments.misc.experience");
                        list.add(Text.literal(expText + ": " + experience + " / " + maxExperience));
                    }

                    // 添加能力列表信息
                    if (Screen.hasShiftDown()) {
                        // 如果玩家按下了Shift键，显示详细能力列表
                        list.add(Text.literal("" + Formatting.GREEN + Formatting.BOLD + Formatting.AQUA + I18n.translate("enhancedarmaments.misc.abilities")));

                        Iterator<Ability> abilityIterator;
                        if (EAUtil.canEnhanceWeapon(item)) {
                            abilityIterator = Ability.WEAPON_ABILITIES.iterator();
                        } else if (EAUtil.canEnhanceArmor(item)) {
                            abilityIterator = Ability.ARMOR_ABILITIES.iterator();
                        } else {
                            return; // 如果都不是，则不继续
                        }

                        while (abilityIterator.hasNext()) {
                            Ability ability = abilityIterator.next();
                            if (ability.hasAbility(nbt)) {
                                // 使用能力自身的颜色和名称
                                String colorCode = ability.getColor();
                                list.add(Text.literal("-" + colorCode + ability.getName(nbt)));
                            }
                        }
                    } else {
                        // 如果没有按下Shift键，显示提示信息
                        list.add(Text.literal("" + Formatting.GREEN + Formatting.BOLD + Formatting.AQUA + I18n.translate("enhancedarmaments.misc.abilities.shift")));
                    }
                }
            }
        });
    }
}