package com.XHxinhe.aliveandwell.hometpaback;

import com.XHxinhe.aliveandwell.hometpaback.util.IStoreHome;
import com.XHxinhe.aliveandwell.hometpaback.util.TeleportUtils;
import com.XHxinhe.aliveandwell.util.config.CommonConfig;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.minecraft.command.argument.EntityArgumentType.getPlayer;

public class Tpa {

    private final ArrayList<TPARequest> activeTPA = new ArrayList<>();
    private final HashMap<UUID, Long> recentRequests = new HashMap<>();
    public static int cooldownTimeTooeasy = CommonConfig.tptime;
    private int xpCostTime=CommonConfig.xptime;
    private final int timeout=60;

    @Nullable
    private static CompletableFuture<Suggestions> filterSuggestionsByInput(SuggestionsBuilder builder, List<String> values) {
        String start = builder.getRemaining().toLowerCase();
        values.stream().filter(s -> s.toLowerCase().startsWith(start)).forEach(builder::suggest);
        return builder.buildFuture();
    }

    private CompletableFuture<Suggestions> getTPAInitSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        ServerCommandSource scs = context.getSource();

        List<String> activeTargets = Stream.concat(
                activeTPA.stream().map(tpaRequest -> tpaRequest.rTo.getEntityName()),
                activeTPA.stream().map(tpaRequest -> tpaRequest.rFrom.getEntityName())
        ).toList();
        List<String> others = Arrays.stream(scs.getServer().getPlayerNames())
                .filter(s -> !s.equals(scs.getName()) && !activeTargets.contains(s))
                .collect(Collectors.toList());
        return filterSuggestionsByInput(builder, others);
    }

    private CompletableFuture<Suggestions> getTPATargetSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        List<String> activeTargets = activeTPA.stream().map(tpaRequest -> tpaRequest.rFrom.getName().getString()).collect(Collectors.toList());
        return filterSuggestionsByInput(builder, activeTargets);
    }

    private CompletableFuture<Suggestions> getTPASenderSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        List<String> activeTargets = activeTPA.stream().map(tpaRequest -> tpaRequest.rTo.getName().getString()).collect(Collectors.toList());
        return filterSuggestionsByInput(builder, activeTargets);
    }

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
            dispatcher.register(CommandManager.literal("tpa")
                    .requires(isPlayer)
                    .then(CommandManager.argument("target", EntityArgumentType.player()).suggests(this::getTPAInitSuggestions)
                            .executes(ctx -> tpaInit(ctx, getPlayer(ctx, "target")))));

            dispatcher.register(CommandManager.literal("tpahere")
                    .requires(isPlayer)
                    .then(CommandManager.argument("target", EntityArgumentType.player()).suggests(this::getTPAInitSuggestions)
                            .executes(ctx -> tpaHere(ctx, getPlayer(ctx, "target")))));

            dispatcher.register(CommandManager.literal("tpaaccept")
                    .requires(isPlayer)
                    .then(CommandManager.argument("target", EntityArgumentType.player()).suggests(this::getTPATargetSuggestions)
                            .executes(ctx -> tpaAccept(ctx, getPlayer(ctx, "target"))))
                    .executes(ctx -> tpaAccept(ctx, null)));

            dispatcher.register(CommandManager.literal("tpadeny")
                    .requires(isPlayer)
                    .then(CommandManager.argument("target", EntityArgumentType.player()).suggests(this::getTPATargetSuggestions)
                            .executes(ctx -> tpaDeny(ctx, getPlayer(ctx, "target"))))
                    .executes(ctx -> tpaDeny(ctx, null)));

            dispatcher.register(CommandManager.literal("tpacancel")
                    .requires(isPlayer)
                    .then(CommandManager.argument("target", EntityArgumentType.player()).suggests(this::getTPASenderSuggestions)
                            .executes(ctx -> tpaCancel(ctx, getPlayer(ctx, "target"))))
                    .executes(ctx -> tpaCancel(ctx, null))
            );
        });
    }

    public int tpaInit(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity tTo) throws CommandSyntaxException {
        final ServerPlayerEntity tFrom = ctx.getSource().getPlayerOrThrow();

        if (tFrom.equals(tTo)) {
            tFrom.sendMessage(Text.translatable("aliveandwell.hometpaback.notpself").formatted(Formatting.RED));
            return 1;
        }

        TPARequest tr = new TPARequest(tFrom, tTo, false, timeout * 1000);

        String st1 = tr.tFrom.getWorld().getRegistryKey().toString().substring(0,tr.tFrom.getWorld().getRegistryKey().toString().indexOf(":"));
        String st2 = tr.tTo.getWorld().getRegistryKey().toString().substring(0,tr.tTo.getWorld().getRegistryKey().toString().indexOf(":"));
        if((!tr.tFrom.getWorld().getRegistryKey().equals( tr.tTo.getWorld().getRegistryKey())) && (((st1.equals(st2) && !st1.contains("minecells"))) && ((st1.equals(st2) && !st1.contains("twilightforest"))))
                || !st1.equals(st2)
        ){
            ctx.getSource().sendMessage(Text.translatable("aliveandwell.hometpaback.nosameworld").formatted(Formatting.RED));
            return 0;
        }

        if (dfafsfsa(tFrom)) return 1;

        if (activeTPA.stream().anyMatch(tpaRequest -> tpaRequest.equals(tr))) {
            tFrom.sendMessage(Text.translatable("aliveandwell.hometpaback.hastprequest").formatted(Formatting.RED));
            return 1;
        }
        tr.setTimeoutCallback(() -> {
            activeTPA.remove(tr);
            tFrom.sendMessage(Text.translatable("aliveandwell.hometpaback.youtpto").append(Text.of(tTo.getName().getString())).append(Text.translatable("aliveandwell.hometpaback.tptimeout")).formatted(Formatting.RED));
            tTo.sendMessage(Text.translatable("aliveandwell.hometpaback.tpfrom").append(Text.of(tFrom.getName().getString())).append(Text.translatable("aliveandwell.hometpaback.tptimeout")).formatted(Formatting.RED));
        });
        activeTPA.add(tr);

        tFrom.sendMessage(
                Text.translatable("aliveandwell.hometpaback.youtpto").formatted(Formatting.LIGHT_PURPLE)
                        .append(Text.translatable(tTo.getName().getString()).formatted(Formatting.AQUA))
                        .append(Text.translatable("aliveandwell.hometpaback.canceltp").formatted(Formatting.LIGHT_PURPLE))
                        .append(Text.translatable("/tpacancel [<player>]").styled(s ->
                                s.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpacancel " + tTo.getName().getString()))
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("/tpacancel " + tTo.getName().getString())))
                                        .withColor(Formatting.GOLD)))
                        .append(Text.translatable("aliveandwell.hometpaback.requesttimeout").append(Text.of(String.valueOf(timeout))).append(Text.translatable("aliveandwell.hometpaback.second")) ).formatted(Formatting.LIGHT_PURPLE));

        tTo.sendMessage(
                Text.translatable(tFrom.getName().getString()).formatted(Formatting.AQUA)
                        .append(Text.translatable("aliveandwell.hometpaback.tptoyou").formatted(Formatting.LIGHT_PURPLE))
                        .append(Text.translatable("aliveandwell.hometpaback.accepttp").formatted(Formatting.LIGHT_PURPLE))
                        .append(Text.translatable("/tpaaccept [<player>]").styled(s ->
                                s.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaaccept " + tFrom.getName().getString()))
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("/tpaaccept " + tFrom.getName().getString())))
                                        .withColor(Formatting.GOLD)))
                        .append(Text.translatable("aliveandwell.hometpaback.denytp").formatted(Formatting.LIGHT_PURPLE))
                        .append(Text.translatable("/tpadeny [<player>]").styled(s ->
                                s.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadeny " + tFrom.getName().getString()))
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("/tpadeny " + tFrom.getName().getString())))
                                        .withColor(Formatting.GOLD)))
                        .append(Text.translatable("aliveandwell.hometpaback.thetp").append(Text.of(String.valueOf(timeout))).append(Text.translatable("aliveandwell.hometpaback.secondtimeout"))).formatted(Formatting.LIGHT_PURPLE));
        return 1;
    }

    public int tpaHere(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity tFrom) throws CommandSyntaxException {
        final ServerPlayerEntity tTo = ctx.getSource().getPlayerOrThrow();

        if (tTo.equals(tFrom)) {
            tTo.sendMessage(Text.translatable("aliveandwell.hometpaback.notpself").formatted(Formatting.RED));
            return 1;
        }

        if (dfafsfsa(tFrom)) return 1;

        TPARequest tr = new TPARequest(tFrom, tTo, true, timeout * 1000);
        if (activeTPA.stream().anyMatch(tpaRequest -> tpaRequest.equals(tr))) {
            tTo.sendMessage(Text.translatable("aliveandwell.hometpaback.hastprequest").formatted(Formatting.RED), false);
            return 1;
        }
        tr.setTimeoutCallback(() -> {
            activeTPA.remove(tr);
            tTo.sendMessage(Text.translatable("aliveandwell.hometpaback.tpfrom").append(Text.of(tFrom.getName().getString())).append(Text.translatable("aliveandwell.hometpaback.tptimeout")).formatted(Formatting.RED), false);
            tFrom.sendMessage(Text.translatable("aliveandwell.hometpaback.youtpto").append(Text.of(tTo.getName().getString())).append(Text.translatable("aliveandwell.hometpaback.tptimeout")).formatted(Formatting.RED), false);
        });
        activeTPA.add(tr);

        tTo.sendMessage(
                Text.translatable("aliveandwell.hometpaback.youhasrequest").formatted(Formatting.LIGHT_PURPLE)
                        .append(Text.translatable(tFrom.getName().getString()).formatted(Formatting.AQUA))
                        .append(Text.translatable("aliveandwell.hometpaback.tphere").formatted(Formatting.LIGHT_PURPLE))
                        .append(Text.translatable("/tpacancel [<player>]").styled(s ->
                                s.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpacancel " + tFrom.getName().getString()))
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("/tpacancel " + tFrom.getName().getString())))
                                        .withColor(Formatting.GOLD)))
                        .append(Text.translatable("aliveandwell.hometpaback.thetprequest").append(Text.of(String.valueOf(timeout))).append(Text.translatable("aliveandwell.hometpaback.secondtimeout")).formatted(Formatting.LIGHT_PURPLE)),
                false);

        tFrom.sendMessage(
                Text.translatable(tTo.getName().getString()).formatted(Formatting.AQUA)
                        .append(Text.translatable("aliveandwell.hometpaback.tpyoutohere").formatted(Formatting.LIGHT_PURPLE))
                        .append(Text.translatable("aliveandwell.hometpaback.accepttp").formatted(Formatting.LIGHT_PURPLE))
                        .append(Text.translatable("/tpaaccept [<player>]").styled(s ->
                                s.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaaccept " + tTo.getName().getString()))
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("/tpaaccept " + tTo.getName().getString())))
                                        .withColor(Formatting.GOLD)))
                        .append(Text.translatable("aliveandwell.hometpaback.denytp").formatted(Formatting.LIGHT_PURPLE))
                        .append(Text.translatable("/tpadeny [<player>]").styled(s ->
                                s.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadeny " + tTo.getName().getString()))
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("/tpadeny " + tTo.getName().getString())))
                                        .withColor(Formatting.GOLD)))
                        .append(Text.translatable("aliveandwell.hometpaback.thetprequest").append(Text.of(String.valueOf(timeout))).append(Text.translatable("aliveandwell.hometpaback.secondtimeout"))).formatted(Formatting.LIGHT_PURPLE),
                false);
        return 1;
    }

    private boolean dfafsfsa(ServerPlayerEntity tFrom) {
        if (recentRequests.containsKey(tFrom.getUuid())) {
            long diff = Instant.now().getEpochSecond() - recentRequests.get(tFrom.getUuid());
            if(cooldownTimeTooeasy < 0){
                cooldownTimeTooeasy=0;
            }
            String nima1 = "你的脸皮真是你身上最神奇的地方，可大可小，可薄可厚，甚至可有可无！";
            String nima2 = "没爹没娘的傻逼真他妈可怜！";
            String nima3 = "恬不知耻说的就是你这个孤儿！";
            String nima4 = "去你妈的傻逼玩意儿，傻逼！！！！！！！！！！！！！！！";
            if (diff < cooldownTimeTooeasy) {
                System.out.println("请不要做【马户】【又鸟】！！！");
                tFrom.sendMessage(Text.translatable("aliveandwell.hometpaback.youhas").append(String.valueOf(cooldownTimeTooeasy - diff))
                        .append(Text.translatable("aliveandwell.hometpaback.secondtp")).formatted(Formatting.RED), false);
                return true;
            }
        }
        return false;
    }

    private enum TPAAction {
        ACCEPT, DENY, CANCEL
    }

    private TPARequest getTPARequest(ServerPlayerEntity rFrom, ServerPlayerEntity rTo, TPAAction action) {
        Optional<TPARequest> otr = activeTPA.stream()
                .filter(tpaRequest -> tpaRequest.rFrom.equals(rFrom) && tpaRequest.rTo.equals(rTo)).findFirst();

        if (otr.isEmpty()) {
            if (action == TPAAction.CANCEL) {
                rFrom.sendMessage(Text.translatable("aliveandwell.hometpaback.norequest").formatted(Formatting.RED), false);
            } else {
                rTo.sendMessage(Text.translatable("aliveandwell.hometpaback.norequest").formatted(Formatting.RED), false);
            }
            return null;
        }

        return otr.get();
    }

    public int tpaAccept(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity rFrom) throws CommandSyntaxException {
        final ServerPlayerEntity rTo = ctx.getSource().getPlayerOrThrow();

        if (rFrom == null) {
            TPARequest[] candidates;
            candidates = activeTPA.stream().filter(tpaRequest -> tpaRequest.rTo.equals(rTo)).toArray(TPARequest[]::new);
            if (candidates.length > 1) {
                MutableText text = Text.translatable("aliveandwell.hometpaback.morerequest").formatted(Formatting.LIGHT_PURPLE);
                Arrays.stream(candidates).map(tpaRequest -> tpaRequest.rFrom.getName().getString()).forEach(name ->
                        text.append(Text.translatable(name).styled(s ->
                                s.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaaccept " + name))
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("/tpaaccept " + name)))
                                        .withColor(Formatting.GOLD))).append(" "));
                assert rTo != null;
                rTo.sendMessage(text);
                return 1;
            }
            if (candidates.length < 1) {
                assert rTo != null;
                rTo.sendMessage(Text.translatable("aliveandwell.hometpaback.younorequest").formatted(Formatting.RED));
                return 1;
            }
            rFrom = candidates[0].rFrom;
        }

        TPARequest tr = getTPARequest(rFrom, rTo, TPAAction.ACCEPT);
        if (tr == null) return 1;

        if(xpCostTime<0){
            xpCostTime=0;
        }

        int experience = ((int)(tr.rFrom.getWorld().getLevelProperties().getTimeOfDay()/ 24000L)+1)*xpCostTime;
        if (experience >= 1000 ){
            experience = 1000;
        }
        if(tr.rFrom.totalExperience < experience){
            tr.rFrom.sendMessage(Text.translatable("aliveandwell.hometpaback.xpno").append(Text.translatable("aliveandwell.hometpaback.xpneed")).append(Text.literal(String.valueOf(experience))).append(Text.translatable("aliveandwell.hometpaback.xpcount")).formatted(Formatting.RED),false);
            tr.tTo.sendMessage(tr.rFrom.getName().copy().append(Text.translatable("aliveandwell.hometpaback.xpno")).append(Text.translatable("aliveandwell.hometpaback.xpneed")).append(Text.literal(String.valueOf(experience))).append(Text.translatable("aliveandwell.hometpaback.xpcount")).formatted(Formatting.RED),false);
            return 0;
        }

        String st1 = tr.tFrom.getWorld().getRegistryKey().toString().substring(0,tr.tFrom.getWorld().getRegistryKey().toString().indexOf(":"));
        String st2 = tr.tTo.getWorld().getRegistryKey().toString().substring(0,tr.tTo.getWorld().getRegistryKey().toString().indexOf(":"));
        if((!tr.tFrom.getWorld().getRegistryKey().equals( tr.tTo.getWorld().getRegistryKey())) && (((st1.equals(st2) && !st1.contains("minecells"))) && ((st1.equals(st2) && !st1.contains("twilightforest"))))
                || !st1.equals(st2)
        ){
            Objects.requireNonNull(ctx.getSource().getPlayer()).sendMessage(Text.translatable("aliveandwell.hometpaback.nosameworld").formatted(Formatting.RED), false);
            return 0;
        }

        int stand_still = 5;
        TeleportUtils.genericTeleport(true, stand_still, rFrom, () -> {
            if (tr.tFrom.isRemoved() || tr.tTo.isRemoved()) tr.refreshPlayers();

            int experience1 = ((int)(tr.tFrom.getWorld().getLevelProperties().getTimeOfDay()/ 24000L)+1)*xpCostTime;
            if (experience1 >= 1000 ){
                experience1 = 1000;
            }

            //经验足够且同一世界
            if((tr.rFrom.totalExperience >= experience1 && tr.tFrom.getWorld().getRegistryKey() == tr.tTo.getWorld().getRegistryKey()
                    && (st1.equals(st2) && st1.contains("minecraft")))
                    || (st1.equals(st2) && st1.contains("minecells")) || (st1.equals(st2) && st1.contains("twilightforest"))
            ){
                //设置back点
                ((IStoreHome)tr.tFrom).addBack(new Vec3d(tr.tFrom.getX(), tr.tFrom.getY(), tr.tFrom.getZ()),tr.tFrom.getWorld().getRegistryKey());
                tr.tFrom.sendMessage(Text.translatable("aliveandwell.hometpaback.setback").append(Text.of("("+tr.tFrom.getBlockPos().getX()+","+ tr.tFrom.getBlockPos().getY()+","+tr.tFrom.getBlockPos().getZ()+")"+"【"+tr.tFrom.getWorld().getRegistryKey().getValue().getPath()+"】")).formatted(Formatting.LIGHT_PURPLE));

                tr.tFrom.teleport((ServerWorld) tr.tTo.getWorld(), tr.tTo.getX(), tr.tTo.getY(), tr.tTo.getZ(), tr.tTo.getYaw(), tr.tTo.getPitch());
                tr.rFrom.addExperience(-experience1);//每次传送消耗100经验===========================================

                switch (TPACooldownMode.WhoTeleported) {
                    case BothUsers -> {
                        recentRequests.put(tr.tFrom.getUuid(), Instant.now().getEpochSecond());
                        recentRequests.put(tr.tTo.getUuid(), Instant.now().getEpochSecond());
                    }
                    case WhoInitiated -> recentRequests.put(tr.rFrom.getUuid(), Instant.now().getEpochSecond());
                    case WhoTeleported -> recentRequests.put(tr.tFrom.getUuid(), Instant.now().getEpochSecond());
                }
                tr.rFrom.sendMessage(Text.translatable("aliveandwell.hometpaback.xpcost").append(Text.translatable(Formatting.GREEN+String.valueOf(experience1))).append(Text.translatable("aliveandwell.hometpaback.xpcount")),false);
            }else if(tr.rFrom.totalExperience < experience1 && tr.rFrom.getWorld().getRegistryKey().equals(tr.tTo.getWorld().getRegistryKey())){
                tr.rFrom.sendMessage(Text.translatable("aliveandwell.hometpaback.xpno1").append(Text.of(String.valueOf(experience1))).append(Text.translatable("aliveandwell.hometpaback.notp")).formatted(Formatting.RED),false);
            }else if((!tr.rFrom.getWorld().getRegistryKey().equals(tr.tTo.getWorld().getRegistryKey()) && (st1.equals(st2) && st1.contains("minecraft")))
                    || !st1.equals(st2)
            ){
                tr.rFrom.sendMessage(Text.translatable("aliveandwell.hometpaback.nosameworld1").formatted(Formatting.RED));
            }
        });

        tr.cancelTimeout();
        activeTPA.remove(tr);
        tr.rTo.sendMessage(Text.translatable("aliveandwell.hometpaback.acceptrequest"));
        tr.rFrom.sendMessage(Text.translatable(tr.rTo.getName().getString()).formatted(Formatting.AQUA)
                .append(Text.translatable("aliveandwell.hometpaback.acceptrequest1").formatted(Formatting.LIGHT_PURPLE)));
        return 1;
    }


    public int tpaDeny(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity rFrom) throws CommandSyntaxException {
        final ServerPlayerEntity rTo = ctx.getSource().getPlayerOrThrow();

        if (rFrom == null) {
            TPARequest[] candidates;
            candidates = activeTPA.stream().filter(tpaRequest -> tpaRequest.rTo.equals(rTo)).toArray(TPARequest[]::new);
            if (candidates.length > 1) {
                MutableText text = Text.translatable("aliveandwell.hometpaback.morerequest1").formatted(Formatting.LIGHT_PURPLE);
                Arrays.stream(candidates).map(tpaRequest -> tpaRequest.rFrom.getName().getString()).forEach(name ->
                        text.append(Text.translatable(name).styled(s ->
                                s.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadeny " + name))
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("/tpadeny " + name)))
                                        .withColor(Formatting.GOLD))).append(" "));
                rTo.sendMessage(text, false);
                return 1;
            }
            if (candidates.length < 1) {
                rTo.sendMessage(Text.translatable("aliveandwell.hometpaback.younorequest").formatted(Formatting.RED));
                return 1;
            }
            rFrom = candidates[0].rFrom;
        }

        TPARequest tr = getTPARequest(rFrom, rTo, TPAAction.DENY);
        if (tr == null) return 1;
        tr.cancelTimeout();
        activeTPA.remove(tr);
        tr.rTo.sendMessage(Text.translatable("aliveandwell.hometpaback.youcancelrequest"), false);
        tr.rFrom.sendMessage(Text.translatable(tr.rTo.getName().getString()).formatted(Formatting.AQUA)
                .append(Text.translatable("aliveandwell.hometpaback.youcancelrequest1").formatted(Formatting.RED)), false);
        return 1;
    }

    public int tpaCancel(CommandContext<ServerCommandSource> ctx, ServerPlayerEntity rTo) throws CommandSyntaxException {
        final ServerPlayerEntity rFrom = ctx.getSource().getPlayerOrThrow();

        if (rTo == null) {
            TPARequest[] candidates;
            candidates = activeTPA.stream().filter(tpaRequest -> tpaRequest.rFrom.equals(rFrom)).toArray(TPARequest[]::new);
            if (candidates.length > 1) {
                MutableText text = Text.translatable("aliveandwell.hometpaback.morerequest2").formatted(Formatting.LIGHT_PURPLE);
                Arrays.stream(candidates).map(tpaRequest -> tpaRequest.rTo.getName().getString()).forEach(name ->
                        text.append(Text.translatable(name).styled(s ->
                                s.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpacancel " + name))
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("/tpacancel " + name)))
                                        .withColor(Formatting.GOLD))).append(" "));
                rFrom.sendMessage(text, false);
                return 1;
            }
            if (candidates.length < 1) {
                rFrom.sendMessage(Text.translatable("aliveandwell.hometpaback.younorequest").formatted(Formatting.RED), false);
                return 1;
            }
            rTo = candidates[0].rTo;
        }

        System.out.printf("%s -> %s\n", rFrom.getName().getString(), rTo.getName().getString());
        TPARequest tr = getTPARequest(rFrom, rTo, TPAAction.CANCEL);
        if (tr == null) return 1;
        tr.cancelTimeout();
        activeTPA.remove(tr);
        tr.rFrom.sendMessage(Text.translatable("aliveandwell.hometpaback.youcancelrequest").formatted(Formatting.RED), false);
        tr.rTo.sendMessage(Text.translatable(tr.rFrom.getName().getString()).formatted(Formatting.AQUA)
                .append(Text.translatable("aliveandwell.hometpaback.youcancelrequest1").formatted(Formatting.RED)), false);
        return 1;
    }


    enum TPACooldownMode {
        WhoTeleported, WhoInitiated, BothUsers
    }

    static class TPARequest {
        ServerPlayerEntity tFrom;
        ServerPlayerEntity tTo;

        ServerPlayerEntity rFrom;
        ServerPlayerEntity rTo;

        boolean tpaHere;
        long timeout;

        Timer timer;

        public TPARequest(ServerPlayerEntity tFrom, ServerPlayerEntity tTo, boolean tpaHere, int timeoutMS) {
            this.tFrom = tFrom;
            this.tTo = tTo;
            this.tpaHere = tpaHere;
            this.timeout = timeoutMS;
            this.rFrom = tpaHere ? tTo : tFrom;
            this.rTo = tpaHere ? tFrom : tTo;
        }

        void setTimeoutCallback(Timeout callback) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    callback.onTimeout();
                }
            }, timeout);
        }

        void cancelTimeout() {
            timer.cancel();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TPARequest that = (TPARequest) o;
            return tFrom.equals(that.tFrom) &&
                    tTo.equals(that.tTo);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tFrom, tTo);
        }

        @Override
        public String toString() {
            return "TPARequest{" + "tFrom=" + tFrom +
                    ", tTo=" + tTo +
                    ", rFrom=" + rFrom +
                    ", rTo=" + rTo +
                    ", tpaHere=" + tpaHere +
                    '}';
        }

        public void refreshPlayers() {
            this.tFrom = tFrom.server.getPlayerManager().getPlayer(tFrom.getUuid());
            this.tTo = tTo.server.getPlayerManager().getPlayer(tTo.getUuid());
            this.rFrom = this.tpaHere ? tTo : tFrom;
            this.rTo = this.tpaHere ? tFrom : tTo;
            assert tFrom != null && tTo != null;
        }
    }

    interface Timeout {
        void onTimeout();
    }
}
