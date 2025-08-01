package com.XHxinhe.aliveandwell.mixin.aliveandwell.world;

import com.XHxinhe.aliveandwell.util.config.CommonConfig;
import com.XHxinhe.aliveandwell.AliveAndWellMain;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(World.class)
public abstract class WorldMixin implements WorldAccess, AutoCloseable{
    @Unique
    private final int xpCostTime= CommonConfig.xptime;

    @Shadow public abstract RegistryKey<World> getRegistryKey();

    @Shadow @Final protected MutableWorldProperties properties;

    @Inject(at=@At("HEAD"), method = "tickEntity")
    public <T extends Entity> void tickEntity(Consumer<T> tickConsumer, T entity,CallbackInfo info) {
        long time = this.properties.getTimeOfDay();
        AliveAndWellMain.day = (int)(time / 24000L)+1;

        if(entity instanceof ServerPlayerEntity player){
            sendMessage(player);
        }
    }

    @Unique
    public void sendMessage(ServerPlayerEntity player){
        int i = (int) (player.getWorld().getTime() - (AliveAndWellMain.day - 1) * 24000L);
        if (i == 15000) {
            //第十天10点提醒玩家即将生成苦力怕
            player.sendMessage(Text.translatable("aliveandwell.sleep.info0").append(Text.translatable("aliveandwell.tip.info0")).formatted(Formatting.YELLOW));
        }

        if (AliveAndWellMain.day % 23 == 0 && i == 15020) {
            player.sendMessage(Text.translatable("aliveandwell.tip.info1").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day % 23 == 1 && i == 15020) {
            player.sendMessage(Text.translatable("aliveandwell.tip.info2").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day % 23 == 2 && i == 15020) {
            player.sendMessage(Text.translatable("aliveandwell.tip.info3").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day % 23 == 3 && i == 15020) {
            player.sendMessage(Text.translatable("aliveandwell.tip.info4").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day % 23 == 4 && i == 15020) {
            player.sendMessage(Text.translatable("aliveandwell.tip.info5").formatted(Formatting.YELLOW));
        }

        if (AliveAndWellMain.day % 23 == 6 && i == 15020) {
            player.sendMessage(Text.translatable("aliveandwell.tip.info7").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day % 23 == 7 && i == 15020) {
            player.sendMessage(Text.translatable("aliveandwell.tip.info8").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day % 23 == 8 && i == 15020) {
            player.sendMessage(Text.translatable("aliveandwell.tip.info9").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day % 23 == 9 && i == 15020) {
            player.sendMessage(Text.translatable("aliveandwell.tip.info10").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day % 23 == 10 && i == 15020) {
            player.sendMessage(Text.translatable("aliveandwell.tip.info11").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day % 23 == 11 && i == 15020) {
            player.sendMessage(Text.translatable("aliveandwell.tip.info12").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day % 23 == 12 && i == 15020) {
            player.sendMessage(Text.translatable("aliveandwell.tip.info13").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day % 23 == 13 && i == 15020) {
            player.sendMessage(Text.translatable("aliveandwell.tip.info14").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day % 23 == 14 && i == 15020) {
            player.sendMessage(Text.translatable("aliveandwell.tip.info15").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day % 23 == 15 && i == 15020) {
            player.sendMessage(Text.translatable("aliveandwell.tip.info16").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day % 23 == 16 && i == 15020) {
            player.sendMessage(Text.translatable("aliveandwell.tip.info17").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day % 23 == 17 && i == 15020) {
            player.sendMessage(Text.translatable("aliveandwell.tip.info18").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day % 23 == 18 && i == 15020) {
            player.sendMessage(Text.translatable("aliveandwell.tip.info19").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day % 23 == 19 && i == 15020) {
            player.sendMessage(Text.translatable("aliveandwell.tip.info20").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day % 23 == 20 && i == 15020) {
            player.sendMessage(Text.translatable("aliveandwell.tip.info21").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day % 23 == 21 && i == 15020) {
            player.sendMessage(Text.translatable("aliveandwell.tip.info22").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day % 23 == 22 && i == 15020) {
            player.sendMessage(Text.translatable("aliveandwell.tip.info23").formatted(Formatting.YELLOW));
        }

        if (AliveAndWellMain.day > 4) {
            if (i == 10000) {
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip1").formatted(Formatting.YELLOW));
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip2").formatted(Formatting.YELLOW));
            }
            if (i == 11000) {
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip3").formatted(Formatting.YELLOW));
            }

            if (i == 12000) {
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip4").formatted(Formatting.YELLOW));
            }

            if (i == 12500) {
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip22").formatted(Formatting.YELLOW));
            }

            if (i == 13500) {
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip19").formatted(Formatting.YELLOW));
            }
            if (i == 14500) {
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip20").formatted(Formatting.YELLOW));
            }
        }

        if (AliveAndWellMain.day == 2) {
            if (i == 2000) {
                //第15天10点提醒玩家即将生成
                player.sendMessage(Text.translatable("aliveandwell.tpxp").append(Text.of(String.valueOf(xpCostTime))).append(Text.translatable("aliveandwell.tpxp1")).formatted(Formatting.YELLOW));
            }
            if (i == 3000) {
                //第十天10点提醒玩家即将生成苦力怕
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip14").formatted(Formatting.YELLOW));
            }
            if (i == 4000) {
                //第十天10点提醒玩家即将生成苦力怕
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip10").formatted(Formatting.YELLOW));
            }
            if (i == 5000) {
                //第十天10点提醒玩家即将生成苦力怕
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip10").formatted(Formatting.YELLOW));
            }
            if (i == 10000) {
                player.sendMessage(Text.translatable("aliveandwell.tip.nametag").formatted(Formatting.YELLOW));
            }
            if (i == 11000) {
                player.sendMessage(Text.translatable("aliveandwell.tip.nametag1").formatted(Formatting.YELLOW));
            }
            if (i == 12000) {
                player.sendMessage(Text.translatable("aliveandwell.tip.nametag2").formatted(Formatting.YELLOW));
            }
            if (i == 13000) {
                //第十天10点提醒玩家即将生成苦力怕
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip13").formatted(Formatting.YELLOW));
            }
            if (i == 14000) {
                //第十天10点提醒玩家即将生成苦力怕
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip5").formatted(Formatting.YELLOW));
            }
        }
        if (AliveAndWellMain.day == 3) {
            if (i == 2000) {
                //第15天10点提醒玩家即将生成
                player.sendMessage(Text.translatable("aliveandwell.tpxp").append(Text.of(String.valueOf(xpCostTime))).append(Text.translatable("aliveandwell.tpxp1")).formatted(Formatting.YELLOW));
            }
            if (i == 3000) {
                //第十天10点提醒玩家即将生成苦力怕
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip18").formatted(Formatting.YELLOW));
            }
            if (i == 4000) {
                //第十天10点提醒玩家即将生成苦力怕
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip11").formatted(Formatting.YELLOW));
            }
            if (i == 5000) {
                //第15天10点提醒玩家即将生成
                player.sendMessage(Text.translatable("aliveandwell.tpxp").append(Text.of(String.valueOf(xpCostTime))).append(Text.translatable("aliveandwell.tpxp1")).formatted(Formatting.YELLOW));
            }
            if (i == 10000) {
                player.sendMessage(Text.translatable("aliveandwell.tip.nametag").formatted(Formatting.YELLOW));
            }
            if (i == 11000) {
                player.sendMessage(Text.translatable("aliveandwell.tip.nametag1").formatted(Formatting.YELLOW));
            }
            if (i == 12000) {
                player.sendMessage(Text.translatable("aliveandwell.tip.nametag2").formatted(Formatting.YELLOW));
            }

            if (i == 12300) {
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip23").formatted(Formatting.YELLOW));
            }

            if (i == 13000) {
                //第十天10点提醒玩家即将生成苦力怕
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip13").formatted(Formatting.YELLOW));
            }
            if (i == 14000) {
                //第十天10点提醒玩家即将生成苦力怕
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip5").formatted(Formatting.YELLOW));
            }
            if (i == 14500) {
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip21").formatted(Formatting.YELLOW));
            }
        }

        if (AliveAndWellMain.day == 4) {
            if (i == 2000) {
                //第十天10点提醒玩家即将生成苦力怕
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip10").formatted(Formatting.YELLOW));
            }
            if (i == 3000) {
                //第十天10点提醒玩家即将生成苦力怕
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip11").formatted(Formatting.YELLOW));
            }
            if (i == 4000) {
                //第十天10点提醒玩家即将生成苦力怕
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip13").formatted(Formatting.YELLOW));
            }
            if (i == 5000) {
                //第十天10点提醒玩家即将生成苦力怕
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip16").formatted(Formatting.YELLOW));
            }
            if (i == 13000) {
                //第15天10点提醒玩家即将生成
                player.sendMessage(Text.translatable("aliveandwell.tpxp").append(Text.of(String.valueOf(xpCostTime))).append(Text.translatable("aliveandwell.tpxp1")).formatted(Formatting.YELLOW));
            }

            if (i == 14000) {
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip7").formatted(Formatting.YELLOW));
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip8").formatted(Formatting.YELLOW));
            }

            if (i == 14500) {
                player.sendMessage(Text.translatable("aliveandwell.tip.infotip21").formatted(Formatting.YELLOW));
            }
        }
        if (AliveAndWellMain.day == 5 && i == 5000) {
            //第十天10点提醒玩家即将生成苦力怕
            player.sendMessage(Text.translatable("aliveandwell.tip.infotip14").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day == 6 && i == 5000) {
            //第十天10点提醒玩家即将生成苦力怕
            player.sendMessage(Text.translatable("aliveandwell.tip.infotip14").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day == 7 && i == 5000) {
            //第十天10点提醒玩家即将生成苦力怕
            player.sendMessage(Text.translatable("aliveandwell.tip.infotip11").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day == 8 && i == 5000) {
            //第十天10点提醒玩家即将生成苦力怕
            player.sendMessage(Text.translatable("aliveandwell.tip.infotip11").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day == 9 && i == 5000) {
            //第十天10点提醒玩家即将生成苦力怕
            player.sendMessage(Text.translatable("aliveandwell.tip.infotip12").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day == 15 && i == 5000) {
            //第十天10点提醒玩家即将生成苦力怕
            player.sendMessage(Text.translatable("aliveandwell.tip.infotip17").formatted(Formatting.YELLOW));
        }
        if (AliveAndWellMain.day == 21 && i == 5000) {
            //第十天10点提醒玩家即将生成苦力怕
            player.sendMessage(Text.translatable("aliveandwell.tip.infotip1").formatted(Formatting.YELLOW));
        }
    }
}
