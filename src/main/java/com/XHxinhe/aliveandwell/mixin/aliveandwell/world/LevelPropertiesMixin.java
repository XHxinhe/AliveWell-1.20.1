package com.XHxinhe.aliveandwell.mixin.aliveandwell.world;

import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.*;
@Mixin(LevelProperties.class)
public abstract class LevelPropertiesMixin implements ServerWorldProperties, SaveProperties {
    @Shadow private LevelInfo levelInfo;
    /**
     * @author
     * @reason
     */
    @Overwrite
    public GameMode getGameMode() {
        return GameMode.SURVIVAL;
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void setGameMode(GameMode gameMode) {
        this.levelInfo = this.levelInfo.withGameMode(GameMode.SURVIVAL);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean areCommandsAllowed() {
        return false;
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void setDifficulty(Difficulty difficulty) {
        this.levelInfo = this.levelInfo.withDifficulty(Difficulty.HARD);
    }
    /**
     * @author
     * @reason
     */
    @Overwrite
    public Difficulty getDifficulty() {
        return Difficulty.HARD;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean isDifficultyLocked() {
        return true;
    }
}
