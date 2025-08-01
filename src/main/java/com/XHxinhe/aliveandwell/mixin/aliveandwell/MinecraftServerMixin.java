package com.XHxinhe.aliveandwell.mixin.aliveandwell;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin extends ReentrantThreadExecutor<ServerTask> implements CommandOutput, AutoCloseable {

    public MinecraftServerMixin(String string) {
        super(string);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public GameMode getDefaultGameMode() {
        return GameMode.SURVIVAL;
    }
}
