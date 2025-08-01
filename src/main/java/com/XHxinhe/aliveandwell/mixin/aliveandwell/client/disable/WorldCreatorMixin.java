package com.XHxinhe.aliveandwell.mixin.aliveandwell.client.disable;

import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.registry.tag.WorldPresetTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.WorldPresets;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(WorldCreator.class)
public class WorldCreatorMixin {
    @Mutable
    @Final
    @Shadow private final Path savesDirectory;
    @Shadow private WorldCreator.Mode gameMode = WorldCreator.Mode.SURVIVAL;
    @Shadow private Difficulty difficulty = Difficulty.HARD;
    @Shadow @Nullable
    private Boolean cheatsEnabled;

    @Shadow private boolean generateStructures;
    @Shadow private boolean bonusChestEnabled;
    @Shadow private GeneratorOptionsHolder generatorOptionsHolder;

    @Final
    @Shadow private final List<WorldCreator.WorldType> normalWorldTypes = new ArrayList<WorldCreator.WorldType>();
    @Shadow private WorldCreator.WorldType worldType;

    public WorldCreatorMixin(Path savesDirectory) {
        this.savesDirectory = savesDirectory;
    }

    @Shadow
    public void update() {
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void setGameMode(WorldCreator.Mode gameMode) {
        this.gameMode = WorldCreator.Mode.SURVIVAL;
        this.update();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public WorldCreator.Mode getGameMode() {
//        if (this.isDebug()) {
//            return WorldCreator.Mode.DEBUG;
//        }
        return WorldCreator.Mode.SURVIVAL;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = Difficulty.HARD; // 忽略输入，强制设为困难。
        this.update();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public Difficulty getDifficulty() {
        return Difficulty.HARD; // 总是返回困难。
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void setCheatsEnabled(boolean cheatsEnabled) {
        this.cheatsEnabled = false; // 忽略输入，强制禁用作弊。
        this.update();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void setGenerateStructures(boolean generateStructures) {
        this.generateStructures = true; // 忽略输入，强制开启结构。
        this.update();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void setBonusChestEnabled(boolean bonusChestEnabled) {
        this.bonusChestEnabled = false; // 忽略输入，强制禁用奖励箱。
        this.update();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static Optional<RegistryEntry<WorldPreset>> getWorldPreset(GeneratorOptionsHolder generatorOptionsHolder, Optional<RegistryKey<WorldPreset>> key) {
        return key.flatMap(key2 -> generatorOptionsHolder.getCombinedRegistryManager().get(RegistryKeys.WORLD_PRESET).getEntry((RegistryKey<WorldPreset>)(WorldPresets.LARGE_BIOMES)));
    }

    @Inject(at = @At("TAIL"), method = "updateWorldTypeLists")
    private void updateWorldTypeLists(CallbackInfo ca) {
        this.normalWorldTypes.remove(0);
        this.normalWorldTypes.remove(0);
        this.normalWorldTypes.remove(1);
        this.normalWorldTypes.remove(1);
    }
}
