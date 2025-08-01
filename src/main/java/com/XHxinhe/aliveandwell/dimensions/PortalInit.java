package com.XHxinhe.aliveandwell.dimensions;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.registry.BlockInit;
import com.XHxinhe.aliveandwell.registry.ItemInit;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;

/**
 * 传送门初始化（混淆名替换版）
 * <p>
 * 这个类使用 CustomPortalAPI 库来注册模组中的各种自定义传送门。
 */
public class PortalInit {
    public PortalInit() {
    }

    /**
     * 注册所有自定义传送门。
     */
    public static void registerPortal() {
        // 注册一个前往 "地下世界" (Underworld) 的传送门
        CustomPortalBuilder.beginPortal()
                .frameBlock(Blocks.CRYING_OBSIDIAN) // 传送门框架方块：哭泣的黑曜石
                .lightWithItem(ItemInit.FLINT_AND_STEEL) // 点燃物品：自定义的打火石
                .forcedSize(2, 3) // 强制传送门内部空间为 2x3
                .destDimID(AliveAndWellMain.MOD_DIMENSION_ID) // 目标维度：模组的自定义维度
                .customPortalBlock(BlockInit.UNDERWORLD_PORTAL) // 传送门内部的方块
                .registerPortal(); // 完成注册

        // 注册一个前往 "下界" (Nether) 的传送门
        CustomPortalBuilder.beginPortal()
                .frameBlock(Blocks.RESPAWN_ANCHOR) // 传送门框架方块：重生锚
                .lightWithItem(ItemInit.FLINT_AND_STEEL)
                .forcedSize(2, 3)
                .destDimID(new Identifier("the_nether")) // 目标维度：原版下界
                .customPortalBlock(BlockInit.NETHERWORLD_PORTAL)
                .registerPortal();

        // 注册一个由秘银框架构成的，前往 "主世界" (Overworld) 的传送门
        CustomPortalBuilder.beginPortal()
                .frameBlock(BlockInit.FRAME_MITHRIL) // 传送门框架方块：自定义的秘银框架
                .lightWithItem(ItemInit.FLINT_AND_STEEL)
                .forcedSize(2, 3)
                .destDimID(new Identifier("overworld")) // 目标维度：原版主世界
                .customPortalBlock(BlockInit.RANDOM_A_PORTAL)
                .registerPortal();

        // 注册一个由精金框架构成的，前往 "主世界" 的传送门
        CustomPortalBuilder.beginPortal()
                .frameBlock(BlockInit.FRAME_ADAMANTIUM) // 传送门框架方块：自定义的精金框架
                .lightWithItem(ItemInit.FLINT_AND_STEEL)
                .forcedSize(2, 3)
                .destDimID(new Identifier("overworld"))
                .customPortalBlock(BlockInit.RANDOM_B_PORTAL)
                .registerPortal();

        // 注册一个由重生点框架构成的，前往 "主世界" 的传送门
        CustomPortalBuilder.beginPortal()
                .frameBlock(BlockInit.FRAME_SPAWNPOINT) // 传送门框架方块：自定义的重生点框架
                .lightWithItem(ItemInit.FLINT_AND_STEEL)
                .forcedSize(2, 3)
                .destDimID(new Identifier("overworld"))
                .customPortalBlock(BlockInit.SPAWNPOINT_PORTAL)
                .registerPortal();
    }
}