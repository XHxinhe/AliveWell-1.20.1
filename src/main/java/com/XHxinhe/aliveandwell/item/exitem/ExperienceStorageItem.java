package com.XHxinhe.aliveandwell.item.exitem;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * 经验存储物品的通用父类
 * 封装了所有经验存取、物品属性和提示信息的通用逻辑。
 * 子类只需在构造函数中提供最大存储量和单次操作量即可。
 */
public abstract class ExperienceStorageItem extends Item {

    protected final int MAX_STORAGE;
    protected final int giveXP;
    private final Random random = new Random();

    /**
     * 构造一个经验存储物品。
     * @param maxStorage 最大经验存储量
     * @param giveXpAmount 每次存取的经验量
     * @param settings 物品的基本设置
     */
    public ExperienceStorageItem(int maxStorage, int giveXpAmount, Settings settings) {
        super(settings.maxCount(1).maxDamage(maxStorage));
        this.MAX_STORAGE = maxStorage;
        this.giveXP = giveXpAmount;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        // Shift+右键：存储经验
        if (player.isSneaking() && getXPStored(stack) < MAX_STORAGE) {
            if (player.totalExperience < giveXP) {
                player.sendMessage(
                        Text.translatable("aliveandwell.xpcard.noxp")
                                .append(Text.of(String.valueOf(giveXP)))
                                .append(Text.translatable("aliveandwell.xpcard.noxpsave"))
                                .formatted(Formatting.RED),
                        true
                );
                return TypedActionResult.fail(stack);
            }

            int storedXP = addXP(stack, giveXP);
            player.addExperience(-storedXP);

            if (!world.isClient) {
                world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.1F, (random.nextFloat() - random.nextFloat()) * 0.35F + 0.9F);
            }
            return TypedActionResult.success(stack, world.isClient());
        }

        // 右键：返还经验
        if (!player.isSneaking() && getXPStored(stack) > 0) {
            int xpToGive = Math.min(giveXP, getXPStored(stack));

            player.addExperience(xpToGive);
            setStoredXP(stack, getXPStored(stack) - xpToGive);

            world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.7F, 1.0F);
            return TypedActionResult.success(stack, world.isClient());
        }

        return TypedActionResult.pass(stack);
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        // 初始化为满耐久（即0经验）
        stack.setDamage(MAX_STORAGE);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return getXPStored(stack) > 0;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canRepair(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    public int addXP(ItemStack stack, int amount) {
        int stored = getXPStored(stack);
        int toAdd = Math.min(amount, MAX_STORAGE - stored);
        setStoredXP(stack, stored + toAdd);
        return toAdd;
    }

    public void setStoredXP(ItemStack stack, int amount) {
        stack.setDamage(MAX_STORAGE - amount);
    }

    public int getXPStored(ItemStack stack) {
        return MAX_STORAGE - stack.getDamage();
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("aliveandwell.xpcard.tooltip1").formatted(Formatting.GREEN));
        tooltip.add(Text.translatable("aliveandwell.xpcard.tooltip2")
                .append(Text.literal(String.valueOf(giveXP)))
                .append(Text.translatable("aliveandwell.xpcard.tooltip3"))
                .formatted(Formatting.GREEN));
        tooltip.add(Text.translatable("aliveandwell.xpcard.tooltip5")
                .append(Text.literal(String.valueOf(giveXP)))
                .append(Text.translatable("aliveandwell.xpcard.tooltip3"))
                .formatted(Formatting.GREEN));
        tooltip.add(Text.empty()); // 添加一个空行以分隔
        tooltip.add(Text.translatable("aliveandwell.xpcard.tooltip6")
                .append(Text.literal(String.format("%,d", getXPStored(stack)))) // 使用千位分隔符
                .append(Text.literal(" / "))
                .append(Text.literal(String.format("%,d", MAX_STORAGE)))
                .formatted(Formatting.YELLOW));
    }
}