package com.XHxinhe.aliveandwell.mixin.aliveandwell.client.disable; // 声明包名

import net.fabricmc.fabric.api.client.screen.v1.Screens; // 导入 Fabric 客户端屏幕 API
import net.minecraft.client.MinecraftClient; // 导入 Minecraft 客户端类
import net.minecraft.client.gui.screen.Screen; // 导入屏幕基类
import net.minecraft.client.gui.screen.world.CreateWorldScreen; // 导入创建世界界面类
import net.minecraft.client.gui.screen.world.WorldCreator; // 导入世界创建器
import net.minecraft.client.world.GeneratorOptionsHolder; // 导入世界生成选项持有者
import net.minecraft.text.Text; // 导入文本类
import net.minecraft.world.Difficulty; // 导入难度枚举
import net.minecraft.world.gen.WorldPresets; // 导入世界预设
import net.minecraft.world.level.LevelInfo; // 导入世界信息
import org.jetbrains.annotations.Nullable; // 导入可空注解
import org.spongepowered.asm.mixin.Mixin; // 导入 Mixin 注解
import org.spongepowered.asm.mixin.Overwrite; // 导入 Overwrite 注解
import org.spongepowered.asm.mixin.injection.At; // 导入注入点注解
import org.spongepowered.asm.mixin.injection.Inject; // 导入 Inject 注解
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo; // 导入回调信息

import java.nio.file.Path; // 导入路径类
import java.util.OptionalLong; // 导入可选长整型

@Mixin(CreateWorldScreen.class) // 声明这是对 CreateWorldScreen 类的 Mixin
public abstract class CreateWorldScreenMixin extends Screen { // 定义一个抽象类，继承自 Screen
    protected CreateWorldScreenMixin(Text title) {
        super(title); // 调用父类构造方法
    }

    /**
     * @author
     * @reason
     */
    @Overwrite // 覆盖原版的 create 方法
    public static CreateWorldScreen create(MinecraftClient client, @Nullable Screen parent, LevelInfo levelInfo, GeneratorOptionsHolder generatorOptionsHolder, @Nullable Path dataPackTempDir) {
        // 创建一个新的 CreateWorldScreen 实例，传入客户端、父界面、生成器选项、世界预设和种子
        CreateWorldScreen createWorldScreen = new CreateWorldScreen(
                client,
                parent,
                generatorOptionsHolder,
                WorldPresets.getWorldPreset(generatorOptionsHolder.selectedDimensions().dimensions()),
                OptionalLong.of(generatorOptionsHolder.generatorOptions().getSeed())
        );
        // 设置世界名称
        createWorldScreen.worldCreator.setWorldName(levelInfo.getLevelName());
        // 设置是否允许作弊
        createWorldScreen.worldCreator.setCheatsEnabled(levelInfo.areCommandsAllowed());
        // 设置世界难度
        createWorldScreen.worldCreator.setDifficulty(levelInfo.getDifficulty());
        // 设置游戏规则
        createWorldScreen.worldCreator.getGameRules().setAllValues(levelInfo.getGameRules(), null);

        // 根据世界模式设置游戏模式
        if (levelInfo.isHardcore()) {
            // 如果是极限模式，设置为生存模式
            createWorldScreen.worldCreator.setGameMode(WorldCreator.Mode.SURVIVAL);
        } else if (levelInfo.getGameMode().isSurvivalLike()) {
            // 如果是生存类模式，设置为极限模式
            createWorldScreen.worldCreator.setGameMode(WorldCreator.Mode.HARDCORE);
        } else if (levelInfo.getGameMode().isCreative()) {
            // 如果是创造模式，设置为生存模式
            createWorldScreen.worldCreator.setGameMode(WorldCreator.Mode.SURVIVAL);
        }
        // 设置数据包临时目录
        createWorldScreen.dataPackTempDir = dataPackTempDir;
        // 返回创建好的界面
        return createWorldScreen;
    }
}