package com.XHxinhe.aliveandwell.hometpaback;

import com.XHxinhe.aliveandwell.hometpaback.util.IStoreHome;
import com.XHxinhe.aliveandwell.hometpaback.util.TeleportUtils;
import com.XHxinhe.aliveandwell.hometpaback.util.TextUtils;
import com.XHxinhe.aliveandwell.util.config.CommonConfig;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class Homes {
    private final HashMap<UUID, Long> recentRequests = new HashMap<>();
    public static int cooldownTimeTooeasy = CommonConfig.tptime;
    private int xpCostTime=CommonConfig.xptime;
    private final int maxHomes = 10;

    public  void register(){

        Predicate<ServerCommandSource> isPlayer = source -> {
            try {
                source.getPlayerOrThrow();
                return true;
            } catch(CommandSyntaxException e) {
                return false;
            }
        };
        CommandRegistrationCallback.EVENT.register((dispatcher, registry, environment) -> {
            // /home [name]
            dispatcher.register(CommandManager.literal("home")
                    .requires(isPlayer).executes(command -> homeInit(command, null))
                    .then(CommandManager.argument("name", StringArgumentType.greedyString()).suggests(this::getHomeSuggestions)
                            .executes(ctx -> homeInit(ctx, StringArgumentType.getString(ctx, "name")))));

            dispatcher.register(CommandManager.literal("sethome")
                    .requires(isPlayer)
                    .executes(ctx -> homeSet(ctx, null))
                    .then(CommandManager.argument("name", StringArgumentType.greedyString())
                            .executes(ctx -> homeSet(ctx, StringArgumentType.getString(ctx, "name")))));

            dispatcher.register(CommandManager.literal("delhome")
                    .requires(isPlayer)
                    .then(CommandManager.argument("name", StringArgumentType.greedyString()).suggests(this::getHomeSuggestions)
                            .executes(ctx -> homeDel(ctx, StringArgumentType.getString(ctx, "name")))));

            dispatcher.register(CommandManager.literal("homes")
                    .executes(this::homeList)
                    .then(CommandManager.literal("delete")
                            .requires(isPlayer)
                            .then(CommandManager.argument("name", StringArgumentType.greedyString()).suggests(this::getHomeSuggestions)
                                    .executes(ctx -> homeDel(ctx, StringArgumentType.getString(ctx, "name")))))
            );
        });
    }

    private boolean dfafsfsa(ServerPlayerEntity tFrom) {
        if (recentRequests.containsKey(tFrom.getUuid())) {
            long diff = Instant.now().getEpochSecond() - recentRequests.get(tFrom.getUuid());
            if (diff < cooldownTimeTooeasy) {
                System.out.println("请不要做【马户】【又鸟】！！！");
                String nima1 = "你的脸皮真是你身上最神奇的地方，可大可小，可薄可厚，甚至可有可无！";
                String nima2 = "没爹没娘的傻逼真他妈可怜！";
                String nima3 = "恬不知耻说的就是你这个孤儿！";
                String nima4 = "去你妈的傻逼玩意儿，傻逼！！！！！！！！！！！！！！！";
                tFrom.sendMessage(Text.translatable("aliveandwell.hometpaback.cooldowntime").append(Text.translatable(String.valueOf(cooldownTimeTooeasy - diff)).append(Text.translatable("aliveandwell.hometpaback.second"))).formatted(Formatting.RED), false);
                return true;
            }
        }
        return false;
    }
    private CompletableFuture<Suggestions> getHomeSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        String start = builder.getRemaining().toLowerCase();
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        if (player != null) {
            ((IStoreHome)player).getHomes().stream()
                    .map(HomeComponent::getName)
                    .sorted(String::compareToIgnoreCase)
                    .filter(v -> v.toLowerCase().startsWith(start))
                    .forEach(builder::suggest);
        }
        return builder.buildFuture();
    }

    /**
     * 传送到家园
     */
    int homeInit(CommandContext<ServerCommandSource> ctx, String name) throws CommandSyntaxException {
        ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
        if (name == null) name = "main";

        String finalName = name;
        Optional<HomeComponent> home = ((IStoreHome)player).getHomes()
                .stream().filter(v -> v.getName().equals(finalName)).findFirst();

        if (home.isEmpty()) {
            ctx.getSource().sendFeedback(() -> Text.translatable("aliveandwell.hometpaback.nohome").formatted(Formatting.RED), false);
            return 0;
        }

        if(xpCostTime<0){
            xpCostTime=0;
        }

        int  experience1 = ((int)(player.getWorld().getLevelProperties().getTimeOfDay()/ 24000L)+1)*xpCostTime;
        if (experience1 >= 1000 ){
            experience1 = 1000;
        }
        if(player.totalExperience < experience1){
            player.sendMessage(Text.translatable("aliveandwell.hometpaback.xpno").append(Text.translatable("aliveandwell.hometpaback.xpneed")).append(Text.literal(String.valueOf(experience1))).append(Text.translatable("aliveandwell.hometpaback.xpcount")).formatted(Formatting.RED),false);
            return 0;
        }

        String st1 = player.getWorld().getRegistryKey().toString().substring(0,player.getWorld().getRegistryKey().toString().indexOf(":"));
        String st2 = home.get().getDimID().toString().substring(0,home.get().getDimID().toString().indexOf(":"));
        if(!player.getWorld().getRegistryKey().equals(home.get().getDimID()) && (st1.equals(st2) && !st1.contains("minecells")) && !st1.contains("twilightforest")
                || !st1.equals(st2)
        ){
            ctx.getSource().sendFeedback(() -> Text.translatable("aliveandwell.hometpaback.nosameworld").formatted(Formatting.RED), false);
            return 0;
        }

        if (dfafsfsa(player)) return 1;

        // 传送逻辑
        TeleportUtils.genericTeleport(true, 3, player, () -> {
            int experience = ((int)(player.getWorld().getLevelProperties().getTimeOfDay()/ 24000L)+1)*xpCostTime;
            if (experience >= 1000 ){
                experience = 1000;
            }

            if(player.totalExperience >= experience && player.getWorld().getRegistryKey().equals(home.get().getDimID()) && st1.contains("minecraft") || st1.contains("minecells") || st1.contains("twilightforest")){

                //设置back点
                ((IStoreHome)player).addBack(new Vec3d(player.getX(), player.getY(), player.getZ()),player.getWorld().getRegistryKey());
                player.sendMessage(Text.translatable("aliveandwell.hometpaback.setback").append(Text.literal("("+player.getBlockPos().getX()+","+ player.getBlockPos().getY()+","+player.getBlockPos().getZ()+")"+"【"+player.getWorld().getRegistryKey().getValue().getPath()+"】")).formatted(Formatting.LIGHT_PURPLE),false);

                player.teleport(
                        Objects.requireNonNull(ctx.getSource().getServer().getWorld(home.get().getDimID())),
                        home.get().getX(), home.get().getY(), home.get().geyZ(),
                        home.get().getYaw(), home.get().getPitch());
                player.addExperience(-experience);//每次传送消耗经验=============================================
                recentRequests.put(player.getUuid(), Instant.now().getEpochSecond());

                player.sendMessage(Text.translatable("aliveandwell.hometpaback.xpcost").append(Text.literal(String.valueOf(experience))).append(Text.translatable("aliveandwell.hometpaback.xpcount")).formatted(Formatting.GREEN),false);
            }else if (player.totalExperience < experience && player.getWorld().getRegistryKey().equals(home.get().getDimID())){//
                int finalExperience = experience;
                ctx.getSource().sendFeedback(() -> Text.translatable("aliveandwell.hometpaback.xpno1").append(Text.literal(String.valueOf(finalExperience))).append(Text.translatable("aliveandwell.hometpaback.notp")).formatted(Formatting.RED), false);
            } else if(!player.getWorld().getRegistryKey().equals(home.get().getDimID()) && !st1.contains("minecells") && !st1.contains("twilightforest")
            ){
                ctx.getSource().sendFeedback(() -> Text.translatable("aliveandwell.hometpaback.nosameworld").formatted(Formatting.RED), false);
            }
        });

        return 1;
    }

    int homeSet(CommandContext<ServerCommandSource> ctx, String name) throws CommandSyntaxException {
        if (name == null) name = "main";
        ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
        if (((IStoreHome)player).getHomes().size() >= maxHomes) {
            ctx.getSource().sendFeedback(() -> Text.translatable("aliveandwell.hometpaback.maxhomes").formatted(Formatting.RED), false);
            return 1;
        }

        if (((IStoreHome)player).addHome(new HomeComponent(
                player.getPos(),
                player.getPitch(),
                player.getYaw(),
                player.getWorld().getRegistryKey(),
                name))) {

            String finalName = name;
            Optional<HomeComponent> home = ((IStoreHome)player).getHomes()
                    .stream().filter(v -> v.getName().equals(finalName)).findFirst();

            if (home.isEmpty()) {
                ctx.getSource().sendFeedback(() -> Text.translatable("aliveandwell.hometpaback.homewrong").formatted(Formatting.RED), true);
                return 1;
            }

            String finalName1 = name;
            ctx.getSource().sendFeedback(() -> Text.translatable("aliveandwell.hometpaback.sethome",
                    Text.literal(finalName1).styled(s -> s.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, home.get().toText(ctx.getSource().getServer())))
                            .withColor(Formatting.GOLD))).formatted(Formatting.LIGHT_PURPLE), false);
        } else {
            ctx.getSource().sendFeedback(() -> Text.translatable("aliveandwell.hometpaback.hashome").formatted(Formatting.RED), false);
            return 1;
        }
        return 1;
    }

    /**
     * 删除家园
     */
    int homeDel(CommandContext<ServerCommandSource> ctx, String name) throws CommandSyntaxException {
        ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
        if (((IStoreHome)player).removeHome(name)) {
            Optional<HomeComponent> home = ((IStoreHome)player).getHomes()
                    .stream().filter(v -> v.getName().equals(name)).findFirst();

            if (home.isPresent()) {
                ctx.getSource().sendFeedback(() -> Text.translatable("aliveandwell.hometpaback.removehomewrong").formatted(Formatting.RED), true);
                return 1;
            }

            ctx.getSource().sendFeedback(() -> Text.translatable("aliveandwell.hometpaback.removehome",
                    Text.literal(name).formatted(Formatting.GOLD)).formatted(Formatting.LIGHT_PURPLE), false);
        } else {
            ctx.getSource().sendFeedback(() -> Text.translatable("aliveandwell.hometpaback.noremovehome").formatted(Formatting.RED), false);
            return 1;
        }
        return 1;
    }

    /**
     * 列出所有家园
     */
    int homeList(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        return homeList(ctx, ctx.getSource().getPlayerOrThrow());
    }

    /**
     * 列出指定玩家的所有家园
     */
    int homeList(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity player) {
        List<HomeComponent> homes = ((IStoreHome)player).getHomes();
        List<Text> list = new ArrayList<>();
        homes.stream().sorted((h1, h2) -> h1.getName().compareToIgnoreCase(h2.getName())).forEach(h ->
                list.add(Text.literal(h.getName()).styled(s ->
                        s.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home " + h.getName()))
                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                        Text.empty().append(Text.translatable("aliveandwell.hometpaback.clicktp").formatted(Formatting.ITALIC))
                                                .append(h.toText(ctx.getSource().getServer()))))
                                .withColor(Formatting.GOLD))));
        ctx.getSource().sendFeedback(() -> Text.translatable("%s/%s:\n", homes.size(), maxHomes).append(TextUtils.join(list, Text.literal(", "))), false);
        return 1;
    }
}