package com.XHxinhe.aliveandwell.mixin.aliveandwell.item;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.CaveVines;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.sound.BlockSoundGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Blocks.class)
public class BlocksMixin {

    @ModifyArg(
            method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=grass")),
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/block/FernBlock;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V",ordinal = 0))
    private static AbstractBlock.Settings grass(AbstractBlock.Settings settings){
        return AbstractBlock.Settings.create().mapColor(MapColor.DARK_GREEN).replaceable().noCollision().strength(0.1f).sounds(BlockSoundGroup.GRASS).offset(AbstractBlock.OffsetType.XYZ).burnable().pistonBehavior(PistonBehavior.DESTROY);
    }

    @ModifyArg(
            method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=seagrass")),
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/block/SeagrassBlock;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V",ordinal = 0))
    private static AbstractBlock.Settings seagrass(AbstractBlock.Settings settings){
        return AbstractBlock.Settings.create().mapColor(MapColor.WATER_BLUE).replaceable().noCollision().strength(0.1f).sounds(BlockSoundGroup.WET_GRASS).pistonBehavior(PistonBehavior.DESTROY);
    }

    @ModifyArg(
            method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=tall_seagrass")),
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/block/TallSeagrassBlock;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V",ordinal = 0))
    private static AbstractBlock.Settings tall_seagrass(AbstractBlock.Settings settings){
        return AbstractBlock.Settings.create().mapColor(MapColor.WATER_BLUE).replaceable().noCollision().strength(0.1f).sounds(BlockSoundGroup.WET_GRASS).offset(AbstractBlock.OffsetType.XZ).pistonBehavior(PistonBehavior.DESTROY);
    }

    @ModifyArg(
            method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=tall_grass")),
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/block/TallPlantBlock;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V",ordinal = 0))
    private static AbstractBlock.Settings tall_grass(AbstractBlock.Settings settings){
        return AbstractBlock.Settings.create().mapColor(MapColor.DARK_GREEN).replaceable().noCollision().strength(0.1f).sounds(BlockSoundGroup.GRASS).offset(AbstractBlock.OffsetType.XZ).burnable().pistonBehavior(PistonBehavior.DESTROY);
    }

    @ModifyArg(
            method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=large_fern")),
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/block/TallPlantBlock;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V",ordinal = 0))
    private static AbstractBlock.Settings large_fern(AbstractBlock.Settings settings){
        return AbstractBlock.Settings.create().mapColor(MapColor.DARK_GREEN).replaceable().noCollision().strength(0.1f).sounds(BlockSoundGroup.GRASS).offset(AbstractBlock.OffsetType.XZ).burnable().pistonBehavior(PistonBehavior.DESTROY);
    }

    @ModifyArg(
            method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=fern")),
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/block/TallPlantBlock;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V",ordinal = 0))
    private static AbstractBlock.Settings fern(AbstractBlock.Settings settings){
        return AbstractBlock.Settings.create().mapColor(MapColor.DARK_GREEN).replaceable().noCollision().strength(0.1f).sounds(BlockSoundGroup.GRASS).offset(AbstractBlock.OffsetType.XYZ).burnable().pistonBehavior(PistonBehavior.DESTROY);
    }

    @ModifyArg(
            method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=kelp")),
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/block/KelpBlock;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V",ordinal = 0))
    private static AbstractBlock.Settings kelp(AbstractBlock.Settings settings){
        return AbstractBlock.Settings.create().mapColor(MapColor.WATER_BLUE).noCollision().ticksRandomly().strength(0.1f).sounds(BlockSoundGroup.WET_GRASS).pistonBehavior(PistonBehavior.DESTROY);
    }

    @ModifyArg(
            method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=sweet_berry_bush",ordinal = 0)),
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/block/SweetBerryBushBlock;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V",ordinal = 0))
    private static AbstractBlock.Settings sweet_berry_bush(AbstractBlock.Settings settings){
        return AbstractBlock.Settings.create().mapColor(MapColor.DARK_GREEN).ticksRandomly().noCollision().strength(0.1f).sounds(BlockSoundGroup.SWEET_BERRY_BUSH).pistonBehavior(PistonBehavior.DESTROY);
    }

    @ModifyArg(
            method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=sugar_cane",ordinal = 0)),
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/block/SugarCaneBlock;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V",ordinal = 0))
    private static AbstractBlock.Settings sugar_cane(AbstractBlock.Settings settings){
        return AbstractBlock.Settings.create().mapColor(MapColor.DARK_GREEN).noCollision().strength(0.1f).ticksRandomly().sounds(BlockSoundGroup.GRASS).pistonBehavior(PistonBehavior.DESTROY);
    }

    @ModifyArg(
            method = "<clinit>",
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=dead_bush",ordinal = 0)),
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/block/DeadBushBlock;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V",ordinal = 0))
    private static AbstractBlock.Settings dead_bush(AbstractBlock.Settings settings){
        return AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).replaceable().noCollision().strength(0.1f).sounds(BlockSoundGroup.GRASS).burnable().pistonBehavior(PistonBehavior.DESTROY);
    }
}
