package com.XHxinhe.aliveandwell.item.tool;

import com.XHxinhe.aliveandwell.item.AliveToolMaterial;
import com.XHxinhe.aliveandwell.registry.BlockInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

/**
 * 经验镐 (ExPickaxe)
 * 挖掘特定矿石时有特殊效果。
 * (已根据原始逻辑修正)
 */
public class ExPickaxe extends PickaxeItem {

    private static final Random RANDOM = new Random();

    public ExPickaxe(Settings settings) {
        super(AliveToolMaterial.EN_GENSTONE, 1, -2.8F, settings);
    }

    /**
     * 在方块被成功挖掘后调用。
     */
    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        // 仅在服务端执行逻辑，且方块非瞬间破坏
        if (!world.isClient && state.getHardness(world, pos) != 0.0F) {
            Block block = state.getBlock();

            // 检查工具是否适用，且方块是会掉落经验的类型
            if (isSuitableFor(state) && block instanceof ExperienceDroppingBlock) {
                // 检查是否没有精准采集附魔
                boolean hasSilkTouch = EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, stack) > 0;

                if (!hasSilkTouch && miner instanceof PlayerEntity player && world instanceof ServerWorld serverWorld) {
                    // --- 核心逻辑：精确复现原始设计 ---

                    // 1. 生成自定义数量的经验球
                    int experienceToDrop = 5 + player.experienceLevel / 5;
                    ExperienceOrbEntity.spawn(serverWorld, Vec3d.ofCenter(pos), experienceToDrop);

                    // 2. 如果是特定的 ORE_EX 方块，则额外掉落绿宝石
                    if (block == BlockInit.ORE_EX) {
                        // 掉落 0-4 个绿宝石 (nextInt(5) 的范围是 [0, 4])
                        int amount = RANDOM.nextInt(5);
                        if (amount > 0) {
                            ItemStack emeralds = new ItemStack(Items.EMERALD, amount);
                            ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, emeralds);
                            itemEntity.setToDefaultPickupDelay(); // 设置默认拾取延迟
                            world.spawnEntity(itemEntity);
                        }
                    }
                }
            }

            // 处理工具耐久度消耗 (常规消耗1点)
            stack.damage(1, miner, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    /**
     * 添加物品提示信息。
     */
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.aliveandwell.ex_pickaxe.tooltip_0").formatted(Formatting.AQUA));
    }
}