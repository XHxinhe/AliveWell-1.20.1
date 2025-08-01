package com.XHxinhe.aliveandwell.item.food;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

/**
 * 沙拉物品
 * 玩家吃完后会返还一个碗。
 */
public class SalaItem extends Item {

    public SalaItem(Settings settings) {
        super(settings);
    }

    /**
     * 当玩家吃完食物后调用。
     * @return 返回吃完后留下的物品（这里是空碗）。
     */
    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        // 首先调用父类方法处理食物效果和消耗
        super.finishUsing(stack, world, user);

        // 如果使用者是玩家，则返还一个碗
        if (user instanceof PlayerEntity player) {
            // 创造模式下不返还
            if (player.getAbilities().creativeMode) {
                return stack;
            }

            // 给予玩家一个碗
            ItemStack bowlStack = new ItemStack(Items.BOWL);
            PlayerInventory inventory = player.getInventory();

            // 如果玩家背包满了，则在地上生成碗
            if (!inventory.insertStack(bowlStack)) {
                player.dropItem(bowlStack, false);
            }
        }

        // 如果原物品堆叠数量大于1，则返回减少数量后的堆叠；否则返回空碗。
        // 但由于食物通常不堆叠，这里直接返回空碗更符合原版逻辑。
        // 如果您的食物可以堆叠，则应返回 stack。
        // 为了安全起见，我们返回一个空碗，让原物品被消耗。
        return new ItemStack(Items.BOWL);
    }
}