package com.XHxinhe.aliveandwell.registry;

import com.google.common.collect.UnmodifiableIterator;
import java.lang.reflect.Field;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.Items;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.AbstractBlock;
import net.minecraft.registry.Registries;


public class VanillaTweaks {
    // 这是一个空的构造函数。当创建这个类的实例时会被调用。
    public VanillaTweaks() {
    }

    // 定义一个私有的静态方法，用于修改方块的硬度。
    // 参数: block (要修改的方块), i (新的硬度值)
    private static void ChangeStrength(Block block, float i) {
        // 使用 try-catch 结构来捕获并处理可能发生的异常（主要来自反射操作）。
        try {
            // 【警告】使用反射按索引获取字段，这种方式非常脆弱，游戏更新可能导致其失效。
            // 获取 AbstractBlock 类中声明的第11个字段（索引为10），这通常是 "settings" 字段。
            Field PROPERTIES = AbstractBlock.class.getDeclaredFields()[10];
            // 设置该字段为可访问，即使它是私有的。
            PROPERTIES.setAccessible(true);
            // 从传入的 block 对象中获取其 "settings" 实例。
            AbstractBlock.Settings properties = (AbstractBlock.Settings)PROPERTIES.get(block);
            // 调用 settings 对象的 strength 方法，将硬度和爆炸抗性都设置为新的值 i。
            properties.strength(i, i);

            // 同样使用反射，获取 AbstractBlock.AbstractBlockState 类中的第9个字段（索引为8），这通常是 "hardness" 字段。
            Field STATE_HARDNESS = AbstractBlock.AbstractBlockState.class.getDeclaredFields()[8];
            // 设置该字段为可访问。
            STATE_HARDNESS.setAccessible(true);
            // 获取该方块所有可能状态的迭代器。
            UnmodifiableIterator var5 = block.getStateManager().getStates().iterator();

            // 循环遍历该方块的每一个状态。
            while(var5.hasNext()) {
                // 获取下一个方块状态。
                BlockState state = (BlockState)var5.next();
                // 直接修改这个状态的硬度字段值为 i。
                STATE_HARDNESS.set(state, i);
            }
            // 如果在 try 块中发生任何错误。
        } catch (Exception var7) {
            // 打印错误的堆栈信息到控制台，方便调试。
            var7.printStackTrace();
        }
    }

    // 定义一个私有的静态方法，用于修改物品的最大堆叠数量。
    // 参数: item (要修改的物品), size (新的堆叠数量)
    private static void ChangeStackSize(Item item, int size) {
        // 使用反射获取 Item 类中的第10个字段（索引为9），这通常是 "maxCount" 字段。
        Field MAX_COUNT = Item.class.getDeclaredFields()[9];
        // 设置该字段为可访问。
        MAX_COUNT.setAccessible(true);

        // 使用 try-catch 结构处理可能的异常。
        try {
            // 将传入的 item 对象的 "maxCount" 字段值修改为新的 size。
            MAX_COUNT.set(item, size);
        } catch (Exception var4) {
            // 如果出错，打印错误信息。
            var4.printStackTrace();
        }
    }

    // 定义一个公开的静态方法，用于批量调整所有物品的堆叠大小。
    public static void ChangeStackSizes() {
        // 遍历游戏物品注册表中的每一个物品。
        Registries.ITEM.iterator().forEachRemaining((item) -> {
            // 判断当前物品是否是 BlockItem (方块物品)。
            if (item instanceof BlockItem blockItem) {
                // 【逻辑分析】这个 if 条件非常复杂且有缺陷。
                // 它检查物品是否 *不是* 火把、*不是* 植物、也*不是* 刷怪蛋。
                if (!(blockItem.getBlock() instanceof TorchBlock) && !(blockItem.getBlock() instanceof PlantBlock) && !(item instanceof SpawnEggItem)) {
                    // 【逻辑缺陷】这个分支永远不会被执行，因为它要求物品是植物，但外层条件已经排除了植物。
                    if (blockItem.getBlock() instanceof PlantBlock) {
                        if (item.getMaxCount() >= 64) {
                            ChangeStackSize(item, 64);
                        }
                        // 如果物品是食物。
                    } else if (item.isFood()) {
                        if (item.getMaxCount() >= 32) {
                            ChangeStackSize(item, 32);
                        }
                        // 其他情况的方块物品。
                    } else if (item.getMaxCount() >= 16) {
                        ChangeStackSize(item, 16);
                    }
                    // 如果是火把、植物或刷怪蛋（根据外层if的else逻辑）。
                } else if (item.getMaxCount() >= 64) {
                    ChangeStackSize(item, 64);
                }
                // 如果物品是箭。
            } else if (item instanceof ArrowItem) {
                if (item.getMaxCount() >= 64) {
                    ChangeStackSize(item, 64);
                }
                // 如果是其他所有类型的物品。
            } else if (!Registries.ITEM.getId(item).toString().contains("bullet") && !Registries.ITEM.getId(item).toString().contains("nugget") && item.getMaxCount() >= 32) {
                // 并且其注册名不包含 "bullet" 和 "nugget"，且当前堆叠上限大于等于32。
                ChangeStackSize(item, 32);
            }
        });
    }

    // 定义一个私有的静态方法，用于修改物品的最大耐久度。
    // 参数: item (要修改的物品), damage (新的耐久度)
    private static void ChangeMaxDamage(Item item, int damage) {
        // 使用反射获取 Item 类中的第11个字段（索引为10），这通常是 "maxDamage" 字段。
        Field MAX_DAMAGE = Item.class.getDeclaredFields()[10];
        // 设置该字段为可访问。
        MAX_DAMAGE.setAccessible(true);

        try {
            // 将传入的 item 对象的 "maxDamage" 字段值修改为新的 damage。
            MAX_DAMAGE.set(item, damage);
        } catch (Exception var4) {
            // 如果出错，打印错误信息。
            var4.printStackTrace();
        }
    }

    // 定义一个公开的静态方法，用于修改特定方块的硬度。
    public static void ChangeBlockStrength() {
        // 【逻辑分析】这里的修改目标与您提供的“参考代码”不同。
        ChangeStrength(Blocks.CRYING_OBSIDIAN, 2.0F);   // 修改哭泣的黑曜石硬度为 2.0
        ChangeStrength(Blocks.FURNACE, 1.0F);           // 修改熔炉硬度为 1.0
        ChangeStrength(Blocks.CRAFTING_TABLE, 0.8F);    // 修改工作台硬度为 0.8
        ChangeStrength(Blocks.CRAFTING_TABLE, 0.25F);   // 【逻辑缺陷】再次修改工作台硬度为 0.25，覆盖了上面的值。
        ChangeStrength(Blocks.HAY_BLOCK, 0.1F);         // 修改干草块硬度为 0.1
        ChangeStrength(Blocks.NETHERRACK, 4.0F);        // 修改下界岩硬度为 4.0
        ChangeStrength(Blocks.CHEST, 0.3F);             // 修改箱子硬度为 0.3
        ChangeStrength(Blocks.SOUL_SAND, 0.6F);         // 修改灵魂沙硬度为 0.6
    }

    // 定义一个公开的静态方法，用于修改特定物品的耐久度。
    public static void ChangeItemDurability() {
        // 【逻辑分析】这里的修改内容与“参考代码”差异巨大，且存在多次覆盖。
        ChangeMaxDamage(Items.ANVIL, 396800);               // 修改铁砧耐久为 396800
        ChangeMaxDamage(Items.FLINT_AND_STEEL, 16);         // 修改打火石耐久为 16
        ChangeMaxDamage(Items.FISHING_ROD, 10);             // 修改钓鱼竿耐久为 10
        ChangeMaxDamage(Items.SHEARS, 20);                  // 修改剪刀耐久为 20
        ChangeMaxDamage(Items.GOLDEN_SWORD, 20);            // 修改金剑耐久为 20
        ChangeMaxDamage(Items.GOLDEN_SHOVEL, 20);           // 修改金锹耐久为 20
        ChangeMaxDamage(Items.GOLDEN_PICKAXE, 20);          // 修改金镐耐久为 20
        ChangeMaxDamage(Items.GOLDEN_AXE, 20);              // 修改金斧耐久为 20
        ChangeMaxDamage(Items.GOLDEN_HOE, 476);             // 修改金锄耐久为 476
        ChangeMaxDamage(Items.SHEARS, 32);                  // 【逻辑缺陷】再次修改剪刀耐久为 32，覆盖了上面的 20
        ChangeMaxDamage(Items.FISHING_ROD, 5);              // 【逻辑缺陷】再次修改钓鱼竿耐久为 5，覆盖了上面的 10
        ChangeMaxDamage(Items.DIAMOND_AXE, 5);              // 修改钻石斧耐久为 5
        ChangeMaxDamage(Items.DIAMOND_BOOTS, 5);            // 修改钻石靴子耐久为 5
        ChangeMaxDamage(Items.DIAMOND_SWORD, 5);            // 修改钻石剑耐久为 5
        ChangeMaxDamage(Items.DIAMOND_HOE, 5);              // 修改钻石锄耐久为 5
        ChangeMaxDamage(Items.DIAMOND_CHESTPLATE, 5);       // 修改钻石胸甲耐久为 5
        ChangeMaxDamage(Items.DIAMOND_HELMET, 5);           // 修改钻石头盔耐久为 5
        ChangeMaxDamage(Items.DIAMOND_LEGGINGS, 5);         // 修改钻石护腿耐久为 5
        ChangeMaxDamage(Items.DIAMOND_PICKAXE, 5);          // 修改钻石镐耐久为 5
    }

    // 定义一个公开的静态方法，作为所有修改的总入口。
    public static void ApplyChanges() {
        // 调用方法，修改方块硬度。
        ChangeBlockStrength();
        // 调用方法，修改物品耐久。
        ChangeItemDurability();
        // 调用方法，修改物品堆叠。
        ChangeStackSizes();
        // 在控制台打印 "DONE!"，表示所有修改已应用。
        System.out.println("DONE!");
    }
}