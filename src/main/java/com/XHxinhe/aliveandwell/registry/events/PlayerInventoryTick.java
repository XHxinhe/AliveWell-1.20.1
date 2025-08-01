package com.XHxinhe.aliveandwell.registry.events; // 包声明

import com.XHxinhe.aliveandwell.events.PlayerEvents; // 导入自定义事件
import net.minecraft.enchantment.EnchantmentHelper; // 导入附魔工具类
import net.minecraft.entity.EquipmentSlot; // 导入装备槽枚举
import net.minecraft.entity.player.PlayerEntity; // 导入玩家实体
import net.minecraft.item.*; // 导入物品相关类
import net.minecraft.nbt.NbtCompound; // 导入NBT复合标签
import net.minecraft.nbt.NbtList; // 导入NBT列表标签
import net.minecraft.registry.Registries; // 导入注册表
import net.minecraft.util.Identifier; // 导入标识符

import java.util.*; // 导入集合工具

public class PlayerInventoryTick { // 玩家背包物品Tick事件处理类

    private static final NbtCompound nbt = new NbtCompound(); // 定义一个空的NBT复合标签，用于后续标记

    // 需要禁用的物品ID集合
    private static final Set<String> BANNED_ITEMS = new HashSet<>(Arrays.asList(
            // mcdw (Minecraft Dungeons Weapons) - 禁用原因: 武器特效过于强大，破坏战斗平衡
            "mcdw:hammer_suns_grace", // 太阳恩典之锤: 范围群体治疗，过于强力
            "mcdw:bow_sabrewing",     // 利爪之翼: 箭矢分裂，清场能力过强
            "mcdw:bow_love_spell_bo", // 爱之咒语弓: 魅惑敌人，控场能力逆天

            // doom (毁灭战士) - 禁用原因: 道具效果过于逆天，完全破坏生存平衡
            "doom:daisy",             // 黛西: 特殊实体，可能导致问题
            "doom:soulcube",          // 灵魂方块: 秒杀非Boss并回血
            "doom:megasphere",        // 超级神球: 大量生命/护甲
            "doom:powersphere",       // 力量神球: 四倍伤害
            "doom:inmortalsphere",    // 无敌神球: 无敌
            "doom:invisiblesphere",   // 隐形神球: 隐身

            // twilightforest (暮色森林)
            "twilightforest:ore_magnet", // 矿石磁铁: 严重影响矿物获取平衡，跳过挖矿过程

            // Spectrum (光谱)
            "spectrum:crafting_tablet", // 合成平板: 可能是为了限制便携合成，或存在漏洞


            // MCDAR (Minecraft Dungeons Artifacts) - 禁用原因: 神器提供过于强大的主动技能，影响游戏难度曲线
            "mcdar:blast_fungus", "mcdar:harvester", "mcdar:lighting_rod", "mcdar:updraft_tome",
            "mcdar:corrupted_seeds", "mcdar:gong_of_weakening", "mcdar:satchel_of_elements",
            "mcdar:shock_powder", "mcdar:buzzy_nest", "mcdar:enchanted_grass", "mcdar:tasty_bone",
            "mcdar:wonderful_wheat", "mcdar:boots_of_swiftness", "mcdar:death_cap_mushroom",
            "mcdar:light_feather", "mcdar:enchanters_tome", "mcdar:iron_hide_amulet",
            "mcdar:powershaker", "mcdar:soul_healer", "mcdar:totem_of_regeneration",
            "mcdar:totem_of_shielding", "mcdar:totem_of_soul_protection", "mcdar:wind_horn",

            // byg (Oh The Biomes You'll Go)
            "byg:soapstone", // 皂石: 可能是装饰性方块，因特定原因（如材质问题、不符合整合包风格）被禁用

            // Minecraft (原版) - 禁用原因: 调整游戏节奏或避免潜在漏洞
            "minecraft:bundle",             // 收纳袋: 可能存在刷物品漏洞或破坏物品栏管理挑战
            "minecraft:wooden_sword",       // 木剑
            "minecraft:wooden_pickaxe",     // 木镐
            "minecraft:wooden_axe",         // 木斧
            "minecraft:wooden_hoe",         // 木锄
            "minecraft:stone_sword",        // 石剑
            "minecraft:stone_shovel",       // 石锹
            "minecraft:stone_pickaxe",      // 石镐
            "minecraft:stone_axe",          // 石斧
            "minecraft:stone_hoe",          // 石锄 (禁用低级工具以加快游戏前期节奏)

            // MobZ - 禁用原因: 该模组的武器装备数值过高，破坏整合包的成长曲线
            "mobz:axe", "mobz:v_sword", "mobz:hardenedmetal_ingot", "mobz:stone_tomahawk",
            "mobz:pillager_staff", "mobz:hardened_metal_ingot", "mobz:armored_sword", "mobz:white_bag",
            "mobz:immunity_orb", "mobz:levitation_orb", "mobz:rainbow_sword", "mobz:eragons_axe",
            "mobz:lilith_bow", "mobz:poison_sword", "mobz:frozen_sword", "mobz:wither_sword",
            "mobz:boss_sword", "mobz:shield",

            // Botania (植物魔法) - 禁用原因: 功能过于强大或与整合包其他机制重叠/冲突
            "botania:travel_belt",          // 旅行腰带: 提供过高的移动速度
            "botania:super_travel_belt",    // 超级旅行腰带: 同上
            "botania:speed_up_belt",        // 加速腰带: 同上
            "botania:holy_cloak",           // 神圣斗篷: 强大的防御特效
            "botania:unholy_cloak",         // 渎神斗篷: 强大的攻击特效
            "botania:crafting_halo",        // 合成光环: 便携合成
            "botania:auto_crafting_halo",   // 自动合成光环: 便携自动合成
            "botania:alchemy_catalyst",     // 炼金催化器: 改变交易，可能被滥用
            "botania:enchanter",            // 植物魔法附魔台: 引导玩家使用其他附魔途径

            // Souls Weapons (魂之武器) - 禁用原因: 武器伤害和攻击模式过于突出，破坏武器平衡
            "soulsweapons:sting", "soulsweapons:guts_sword", "soulsweapons:kirkhammer",
            "soulsweapons:holy_greatsword", "soulsweapons:moonstone_axe", "soulsweapons:moonstone_hoe",
            "soulsweapons:moonstone_pickaxe", "soulsweapons:moonstone_shovel",

            // Create (机械动力) - 禁用原因: 可能存在漏洞或为了引导玩家使用特定自动化流程
            "create:crafting_blueprint",    // 合成蓝图
            "create:cart_assembler",        // 矿车装配器
            "create:schematicannon",       //蓝图加农炮

            // invade - 禁用原因: 功能过于强大，破坏核心游戏循环
            "invade:enchantment_extract_table" // 附魔提取桌: 使顶级附魔获取过于简单，破坏附魔稀有性
    ));

    // 需要禁用的原版工具
    private static final Set<String> BANNED_VANILLA_TOOLS = new HashSet<>(Arrays.asList( // 定义一个HashSet存储所有需要禁用的原版工具ID
            "minecraft:wooden_sword", "minecraft:wooden_pickaxe", "minecraft:wooden_axe", "minecraft:wooden_hoe",
            "minecraft:stone_sword", "minecraft:stone_shovel", "minecraft:stone_pickaxe", "minecraft:stone_axe", "minecraft:stone_hoe"
    ));

    // 需要特殊NBT标记的物品
    private static final Set<String> NEED_MARK_ITEMS = new HashSet<>(Arrays.asList( // 定义一个HashSet存储所有需要打特殊NBT标记的物品ID
            "minecraft:netherite_scrap", "minecraft:blaze_rod", "minecraft:elytra", "doom:argent_block", "doom:argent_energy", "mobz:boss_ingot"
    ));

    // 需要移除的附魔前缀
    private static final List<String> BANNED_ENCHANT_PREFIX = Arrays.asList( // 定义一个List存储所有需要移除的附魔前缀
            "mcdw:radiance", "mcdw:radiance_shot", "mcdw:prospector", "mcda:heal_allies",
            "mcda:lucky_explorer", "mcdw:leeching", "mcda:reckless", "mcdw:anima_conduit",
            "mcda:death_barter", "mcdw:rushdown", "mcdw:tempo_theft", "mcdar:beast_boss",
            "mcdar:beast_burst", "mcdar:beast_surge", "mcda:swiftfooted", "mcdw:refreshment",
            "mcdw:void_shot", "mcdw:void_strike", "mcda:cowardice"
    );

    public static void onPlayerInventoryItemStackTick() { // 注册事件方法
        PlayerEvents.PLAYER_INVENTORY_INSERT.register(PlayerInventoryTick::banItem); // 注册banItem方法到玩家物品插入事件
    }

    private static void banItem(PlayerEntity player, ItemStack stack) { // 处理玩家物品的方法
        String name = Registries.ITEM.getId(stack.getItem()).toString(); // 获取物品的注册ID字符串

        // 1. 移除特定自定义名称
        if (stack.getName().getString().contains("未获取到任何附魔，请关闭gui后再次附魔")) { // 如果物品名称包含指定字符串
            stack.removeCustomName(); // 移除自定义名称
        }

        // 2. 禁用特定附魔（非附魔书）
        if (stack.getItem() != Items.ENCHANTED_BOOK && stack.hasEnchantments()) { // 如果不是附魔书且有附魔
            removeBannedEnchantments(stack); // 移除指定附魔
        }

        // 3. 禁用特定物品（直接删除）
        if (BANNED_ITEMS.contains(name) || BANNED_VANILLA_TOOLS.contains(name)) { // 如果物品ID在禁用列表中
            stack.split(1); // 删除该物品
        }

        // 4. 禁用入侵装备（invade:xxx_helmet等）
        if (name.startsWith("invade:") && ( // 如果物品ID以invade:开头且结尾为指定装备类型
                name.endsWith("_helmet") || name.endsWith("_chestplate") ||
                        name.endsWith("_leggings") || name.endsWith("_boots"))) {
            stack.split(1); // 删除该物品
        }

        // 5. 特定物品打上aliveandwell NBT标记
        if (NEED_MARK_ITEMS.contains(name)) { // 如果物品ID在需要打标记的列表中
            markItemWithNBT(stack, "aliveandwell"); // 打上aliveandwell标记
        }

        // 6. 给玩家装备打上equip_player NBT标记
        for (EquipmentSlot slot : EquipmentSlot.values()) { // 遍历所有装备槽
            ItemStack equip = player.getEquippedStack(slot); // 获取该槽位的装备
            if (!equip.isEmpty()) { // 如果装备不为空
                markItemWithNBT(equip, "equip_player"); // 打上equip_player标记
            }
        }

        // 7. 主手为工具/弩/三叉戟时打上equip_player NBT
        ItemStack mainHand = player.getEquippedStack(EquipmentSlot.MAINHAND); // 获取主手物品
        if (mainHand.getItem() instanceof ToolItem || // 如果主手是工具
                mainHand.getItem() instanceof CrossbowItem || // 或者是弩
                mainHand.getItem() instanceof TridentItem) { // 或者是三叉戟
            markItemWithNBT(mainHand, "equip_player"); // 打上equip_player标记
        }

        // 8. 背包内工具/护甲/弓/弩/三叉戟打上equip_player NBT
        if (stack.getItem() instanceof ToolItem || // 如果物品是工具
                stack.getItem() instanceof ArmorItem || // 或者是护甲
                stack.getItem() instanceof BowItem || // 或者是弓
                stack.getItem() instanceof CrossbowItem || // 或者是弩
                stack.getItem() instanceof TridentItem) { // 或者是三叉戟
            markItemWithNBT(stack, "equip_player"); // 打上equip_player标记
        }
    }

    // 移除指定附魔
    private static void removeBannedEnchantments(ItemStack stack) { // 移除物品上指定附魔的方法
        NbtList enchantments = stack.getEnchantments(); // 获取物品的附魔NBT列表
        for (int i = enchantments.size() - 1; i >= 0; i--) { // 逆序遍历附魔列表，防止remove时下标错乱
            NbtCompound ench = enchantments.getCompound(i); // 获取第i个附魔的NBT
            Identifier enchId = EnchantmentHelper.getIdFromNbt(ench); // 获取附魔ID
            if (enchId != null) { // 如果附魔ID不为空
                String enchStr = enchId.toString(); // 转为字符串
                for (String prefix : BANNED_ENCHANT_PREFIX) { // 遍历所有禁用附魔前缀
                    if (enchStr.contains(prefix)) { // 如果附魔ID包含禁用前缀
                        enchantments.remove(i); // 移除该附魔
                        break; // 跳出前缀循环
                    }
                }
            }
        }
    }

    // 给物品打上指定NBT标记
    private static void markItemWithNBT(ItemStack stack, String key) { // 给物品打上指定NBT标记的方法
        if (!stack.hasNbt() || !Objects.requireNonNull(stack.getNbt()).contains(key)) { // 如果物品没有NBT或没有该key
            stack.setSubNbt(key, nbt); // 设置该key的NBT为预定义的空NBT
        }
    }
}