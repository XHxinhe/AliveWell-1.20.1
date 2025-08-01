package com.XHxinhe.aliveandwell.mixin.aliveandwell;

import com.XHxinhe.aliveandwell.util.config.CommonConfig;
import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.XHxinhe.aliveandwell.util.ConfigLock;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Inject(at = @At("RETURN"), method = "onPlayerConnect")
    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) throws IOException {
        if(AliveAndWellMain.VERSION.contains("modrinth")){
            player.sendMessage(Text.translatable("危险序曲 ( Alive Well ) ").append(AliveAndWellMain.VERSION).append("温馨提示：").formatted(Formatting.YELLOW));
            player.sendMessage(Text.translatable("aliveandwell.playermanager.info1").formatted(Formatting.GOLD));
            player.sendMessage(Text.translatable("aliveandwell.playermanager.info2").formatted(Formatting.GOLD));
            player.sendMessage(Text.translatable("3.传送插件已加载，可使用的指令有：sethome、home、delhome、tpa、tpahere、back、setback，其中home指令为home 家的名字等。").formatted(Formatting.GOLD));
            player.sendMessage(Text.translatable("4.每日一遍【罗刹海市】，自省自悟，提神醒脑！").formatted(Formatting.YELLOW));
            player.sendMessage(Text.translatable("5.在关闭存档或服务器前务必用命名牌给村民命名（不可包含villager字样）！").formatted(Formatting.LIGHT_PURPLE));
            player.sendMessage(Text.translatable("aliveandwell.deathcount.lost1").append(Text.of(player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.DEATHS))+"/"+ CommonConfig.deathCount)).append(Text.translatable("aliveandwell.deathcount.count")).formatted(Formatting.DARK_PURPLE));
        }else {
            player.sendMessage(Text.translatable("危险序曲 ( Alive Well ) ").append(AliveAndWellMain.VERSION).append("温馨提示：").formatted(Formatting.YELLOW));
            player.sendMessage(Text.translatable("aliveandwell.playermanager.info1").formatted(Formatting.GOLD));
            player.sendMessage(Text.translatable("aliveandwell.playermanager.info2").formatted(Formatting.GOLD));
            player.sendMessage(Text.translatable("3.爱发电链接：https://afdian.com/a/XHxinhe").formatted(Formatting.YELLOW));
            player.sendMessage(Text.translatable("4.不可二次倒卖，必须遵循Eula协议，请注重版权意识，侵权必究！").formatted(Formatting.YELLOW));
            player.sendMessage(Text.translatable("5.传送插件已加载，可使用的指令有：sethome、home、delhome、tpa、tpahere、back、setback，其中home指令为home 家的名字等。").formatted(Formatting.GOLD));
            player.sendMessage(Text.translatable("6.在关闭存档或服务器前务必用命名牌给村民命名（不可包含villager字样）！").formatted(Formatting.LIGHT_PURPLE));
            player.sendMessage(Text.translatable("aliveandwell.deathcount.lost1").append(Text.of(player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.DEATHS))+"/"+CommonConfig.deathCount)).append(Text.translatable("aliveandwell.deathcount.count")).formatted(Formatting.DARK_PURPLE));
        }

        if(player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.DEATHS)) > CommonConfig.deathCount){
            player.changeGameMode(GameMode.SPECTATOR);
            if(!player.changeGameMode(GameMode.SPECTATOR)){
                player.changeGameMode(GameMode.SPECTATOR);
            }
        } else {
            if(player.isSpectator()){
                player.changeGameMode(GameMode.SURVIVAL);
                player.removeStatusEffect(StatusEffects.NIGHT_VISION);
            }

            player.changeGameMode(GameMode.SURVIVAL);
            if (!player.changeGameMode(GameMode.SURVIVAL)) {
                player.changeGameMode(GameMode.SURVIVAL);
            }
        }

        if(!this.isOperator(player.getGameProfile())){
            if(!player.getGameProfile().getProperties().get("IsCheat2432").isEmpty()){
                player.networkHandler.onDisconnected(Text.translatable("检测到玩家作弊，禁止进入游戏！"));
            }
        }

        if(player.isCreative() && (!this.isOperator(player.getGameProfile()) || !AliveAndWellMain.canCreative)){
            if(!player.getGameProfile().getProperties().get("IsCheat").isEmpty()){
                player.networkHandler.onDisconnected(Text.translatable("检测到玩家作弊，禁止进入游戏！"));
            }else {
                player.getGameProfile().getProperties().put("IsCheat",new Property("IsCheat", "ischeat"));
                player.networkHandler.onDisconnected(Text.translatable("检测到玩家作弊，禁止进入游戏！"));
            }
        }

        if(!AliveAndWellMain.canCreative){
            try {
                if(!(new ConfigLock().isDefaultConfigConnect())){
                    player.networkHandler.onDisconnected(Text.translatable("检测到玩家作弊，禁止进入游戏！"));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
/**
 * @author
 * @reason
 */
@Overwrite public boolean isOperator(GameProfile profile)
{return CommonConfig.areCheatAllowed;}//作弊端或不作弊
}