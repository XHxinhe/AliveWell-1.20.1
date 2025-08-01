package com.XHxinhe.aliveandwell.block.portal;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.dimensions.DimsRegistry;
import com.XHxinhe.aliveandwell.util.TeleporterPortalHelper;
import com.XHxinhe.aliveandwell.util.config.CommonConfig;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.kyrptonaught.customportalapi.interfaces.EntityInCustomPortal;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NetherworldPortalBlock extends CustomPortalBlock {
    // 构造函数
    public NetherworldPortalBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    // 当实体在方块内时，每一刻(tick)都会调用此方法
    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        EntityInCustomPortal entityInPortal = (EntityInCustomPortal) entity;
        entityInPortal.tickInPortal(pos.toImmutable()); // 更新实体在传送门内的计时器和位置

        // 检查实体是否已传送，以及在传送门内停留时间是否足够
        if (!entityInPortal.didTeleport() && entityInPortal.getTimeInPortal() >= entity.getMaxNetherPortalTime()) {
            entityInPortal.setDidTP(true); // 标记为已传送，防止重复触发

            // 检查：1. 是否在服务器端；2. 实体当前是否在主世界(Overworld)或自定义的UNDER_WORLD维度
            // 注意：原始代码的逻辑是 entity.getWorld().getRegistryKey() != World.NETHER，这意味着“只要不是地狱”就可以。
            // 这与你提供的比较版本中的逻辑 (== World.NETHER) 是相反的。
            if (!world.isClient && entity.getWorld().getRegistryKey() != World.NETHER && entity.getWorld().getRegistryKey() != DimsRegistry.UNDER_WORLD_KEY) {

                // 如果游戏天数达到了配置中允许进入地狱的天数
                if (AliveAndWellMain.day >= CommonConfig.netherDay) {
                    // 执行传送
                    TeleporterPortalHelper.TPToDim(world, entity, this.getPortalBase(world, pos), pos);
                } else if (entity instanceof PlayerEntity player) {
                    // 如果天数未到，且实体是玩家，则发送提示消息
                    player.sendMessage(
                            Text.literal(String.valueOf(CommonConfig.netherDay)) // 创建包含天数的文本
                                    .append(Text.translatable("aliveandewell.to_netherworld")) // 追加翻译文本
                                    .formatted(Formatting.YELLOW) // 设置文本颜色为黄色
                    );
                }
            }
        }
    }
}