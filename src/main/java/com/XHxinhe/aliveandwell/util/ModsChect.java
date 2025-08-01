package com.XHxinhe.aliveandwell.util;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Set;

public class ModsChect {

    /**
     * 客户端的主验证方法。
     * @return 如果验证通过则返回 true，否则返回 false。
     */
    public boolean chectMods() {
        // 获取当前所有已加载的模组ID，使用 Set 效率更高
        Set<String> loadedModIds = FabricLoader.getInstance().getAllMods().stream()
                .map(mod -> mod.getMetadata().getId())
                .collect(Collectors.toSet());

        // 您的调试日志逻辑
        if (AliveAndWellMain.canCreative) {
            AliveAndWellMain.LOGGER.info("--- 客户端加载的模组列表 ({}个) ---", loadedModIds.size());
            loadedModIds.forEach(name -> AliveAndWellMain.LOGGER.info("  " + name));
            AliveAndWellMain.LOGGER.info("--- 列表结束 ---");
        }

        boolean modsAreValid;
        // 根据版本号判断使用哪个验证集
        if (AliveAndWellMain.VERSION.contains("-modrinth")) {
            AliveAndWellMain.LOGGER.info("检测到 Modrinth 版本，开始 Modrinth 模组验证...");
            modsAreValid = modrinth(loadedModIds);
        } else {
            AliveAndWellMain.LOGGER.info("检测到标准版本，开始客户端模组验证...");
            modsAreValid = normal(loadedModIds);
        }

        // 结合特定模组的版本检查
        return modsAreValid && checkDarkfearVersion();
    }

    /**
     * 服务器端的主验证方法。
     * @return 如果验证通过则返回 true，否则返回 false。
     */
    public boolean chectModsServer() {
        // 获取当前所有已加载的模组ID
        Set<String> loadedModIds = FabricLoader.getInstance().getAllMods().stream()
                .map(mod -> mod.getMetadata().getId())
                .collect(Collectors.toSet());

        // 您的调试日志逻辑
        if (AliveAndWellMain.canCreative) {
            AliveAndWellMain.LOGGER.info("--- 服务器加载的模组列表 ({}个) ---", loadedModIds.size());
            loadedModIds.forEach(name -> AliveAndWellMain.LOGGER.info("  " + name));
            AliveAndWellMain.LOGGER.info("--- 列表结束 ---");
        }

        boolean modsAreValid;
        // 根据版本号判断使用哪个验证集
        if (AliveAndWellMain.VERSION.contains("-modrinth")) {
            AliveAndWellMain.LOGGER.info("检测到 Modrinth 版本，开始 Modrinth (服务器) 模组验证...");
            modsAreValid = modrinth(loadedModIds);
        } else {
            AliveAndWellMain.LOGGER.info("检测到标准版本，开始服务器模组验证...");
            modsAreValid = server(loadedModIds);
        }

        // 结合特定模组的版本检查
        return modsAreValid && checkDarkfearVersion();
    }

    /**
     * 检查 'darkfear' 模组的版本是否为 "1.0.3"。
     * @return 如果版本正确则返回 true，否则返回 false 并记录错误。
     */
    private boolean checkDarkfearVersion() {
        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer("darkfear");
        if (modContainer.isPresent()) {
            String version = modContainer.get().getMetadata().getVersion().getFriendlyString();
            if ("1.0.2".equals(version)) {
                AliveAndWellMain.LOGGER.info("[版本检测] 'darkfear' 模组版本正确 (1.0.2)。");
                return true;
            } else {
                AliveAndWellMain.LOGGER.error("[版本检测失败] 'darkfear' 模组版本不正确! 预期: 1.0.2, 实际: {}", version);
                return false;
            }
        } else {
            AliveAndWellMain.LOGGER.error("[版本检测失败] 关键模组 'darkfear' 未找到!");
            return false;
        }
    }

    // =================================================================================
    // 下方是三个详细的模组列表验证方法
    // =================================================================================

    /**
     * 标准版客户端模组验证。
     * [重要] 此列表已根据最新日志进行了修正，加入了 'entityculling'，以匹配您当前客户端环境中的 372 个模组。
     */
    public boolean normal(Collection<String> names) {
        List<String> requiredMods = Arrays.asList(
                "achiopt", "ad_astra", "additionalentityattributes", "advanced_runtime_resource_pack",
                "advancedbackups", "adventurez", "ae2", "aftershock", "aliveandwell", "alternate-current",
                "anvilrestoration", "appleskin", "arachnids", "architectury", "arrowhead", "attributefix",
                "awesomedungeon", "awesomedungeonnether", "azurelib", "badpackets", "balm-fabric", "benched",
                "bettercombat", "betterfpsdist", "betterstats", "blue_endless_jankson", "bookshelf",
                "bosses_of_mass_destruction", "botania", "botarium", "bowinfinityfix", "caffeineconfig",
                "cardinal-components", "cardinal-components-base", "cardinal-components-block",
                "cardinal-components-chunk", "cardinal-components-entity", "cardinal-components-item",
                "cardinal-components-level", "cardinal-components-scoreboard", "cardinal-components-world",
                "cavespiderspawn", "charmofundying", "chunky", "cleancut", "cloth-basic-math", "cloth-config",
                "collective", "com_akuleshov7_ktoml-core-jvm", "com_akuleshov7_ktoml-file-jvm",
                "com_akuleshov7_ktoml-source-jvm", "com_electronwill_night-config_core",
                "com_electronwill_night-config_toml", "com_eliotlash_mclib_mclib",
                "com_google_code_findbugs_jsr305", "com_squareup_okio_okio-jvm",
                "com_teamresourceful_bytecodecs", "com_teamresourceful_yabn",
                "com_twelvemonkeys_common_common-image", "com_twelvemonkeys_common_common-io",
                "com_twelvemonkeys_common_common-lang", "com_twelvemonkeys_imageio_imageio-core",
                "com_twelvemonkeys_imageio_imageio-metadata", "com_twelvemonkeys_imageio_imageio-webp",
                "com_velocitypowered_velocity-native", "combatroll", "configuration", "connectivity", "coroutil",
                "create", "createaddition", "creeperoverhaul", "crowdin-translate", "cull-less-leaves",
                "cupboard", "cursery", "customportalapi", "customskinloader", "darkfear", "deeperdarker", "diet",
                "disable_custom_worlds_advice", "doom", "dragonfight", "dragonloot", "dripstone_fluid_lib",
                "dungeons_arise", "dungeons_arise_seven_seas", "earthtojavamobs", "eldritch_mobs", "elytraslot",
                "enchdesc", "endrem", "enhancedblockentities", "entityculling", "error_notifier", "euphonium", "explosiveenhancement",
                "extendedae", "extensibleenums", "extra_details", "fabric-api", "fabric-api-base",
                "fabric-api-lookup-api-v1", "fabric-biome-api-v1", "fabric-block-api-v1",
                "fabric-block-view-api-v2", "fabric-blockrenderlayer-v1", "fabric-client-tags-api-v1",
                "fabric-command-api-v1", "fabric-command-api-v2", "fabric-commands-v0", "fabric-containers-v0",
                "fabric-content-registries-v0", "fabric-convention-tags-v1", "fabric-crash-report-info-v1",
                "fabric-data-attachment-api-v1", "fabric-data-generation-api-v1", "fabric-dimensions-v1",
                "fabric-entity-events-v1", "fabric-events-interaction-v0", "fabric-events-lifecycle-v0",
                "fabric-game-rule-api-v1", "fabric-item-api-v1", "fabric-item-group-api-v1",
                "fabric-key-binding-api-v1", "fabric-keybindings-v0", "fabric-language-kotlin",
                "fabric-lifecycle-events-v1", "fabric-loot-api-v2", "fabric-loot-tables-v1",
                "fabric-message-api-v1", "fabric-mining-level-api-v1", "fabric-model-loading-api-v1",
                "fabric-models-v0", "fabric-networking-api-v1", "fabric-networking-v0",
                "fabric-object-builder-api-v1", "fabric-particles-v1", "fabric-permissions-api-v0",
                "fabric-recipe-api-v1", "fabric-registry-sync-v0", "fabric-renderer-api-v1",
                "fabric-renderer-indigo", "fabric-renderer-registries-v1", "fabric-rendering-data-attachment-v1",
                "fabric-rendering-fluids-v1", "fabric-rendering-v0", "fabric-rendering-v1",
                "fabric-resource-conditions-api-v1", "fabric-resource-loader-v0", "fabric-screen-api-v1",
                "fabric-screen-handler-api-v1", "fabric-sound-api-v1", "fabric-transfer-api-v1",
                "fabric-transitive-access-wideners-v1", "fabricloader", "fabricshieldlib", "ferritecore", "fiber",
                "flywheel", "folk_sisby_kaleido-config", "forgeconfigapiport", "fractal", "ftblibrary", "ftbquests",
                "ftbteams", "ftbxmodcompat", "geckolib", "gitbm", "gpumemleakfix", "here-be-no-dragons",
                "human_reborn", "huskspawn", "illagerinvasion", "immediatelyfast", "immersive_aircraft",
                "immersive_melodies", "improvedmobs", "imst", "indium", "ingameime", "inmis", "inmisaddon",
                "inventoryhud", "inventoryprofilesnext", "io_github_douira_glsl-transformer", "iris", "java",
                "javazoom_jlayer", "javax_annotation_javax_annotation-api", "jecharacters", "kaffees_dual_ride",
                "kirin", "krypton", "kubejs", "lanserverproperties", "levelz", "libipn", "libraryferret", "libz",
                "lithium", "lootr", "lunar", "maelstrom_library", "magna", "matchbooks", "mc249136", "mcdw",
                "memoryclearermissnotoredict", "memoryleakfix", "midnightlib", "milk", "minecells", "minecraft",
                "mixinextras", "mkb", "mm", "mobtimizations", "mobz", "modern_industrialization", "modernfix",
                "modifiers", "monolib", "movingelevators", "mr_camps_castles_carriages", "mr_reds_morestructures",
                "mr_ships", "mr_true_ending", "multipart_entities", "mythicmounts", "nametagtweaks",
                "nebulus_cherry_blossem_tree", "net_lenni0451_reflect", "nice_boat", "nicer-skies", "no-telemetry",
                "noindium", "noisium", "notenoughanimations", "nyctophobia", "omega-config", "org_anarres_jcpp",
                "org_antlr_antlr4-runtime", "org_apache_commons_commons-math3", "org_javassist_javassist",
                "org_jetbrains_kotlin_kotlin-reflect", "org_jetbrains_kotlin_kotlin-stdlib",
                "org_jetbrains_kotlin_kotlin-stdlib-jdk7", "org_jetbrains_kotlin_kotlin-stdlib-jdk8",
                "org_jetbrains_kotlinx_atomicfu-jvm", "org_jetbrains_kotlinx_kotlinx-coroutines-core-jvm",
                "org_jetbrains_kotlinx_kotlinx-coroutines-jdk8", "org_jetbrains_kotlinx_kotlinx-datetime-jvm",
                "org_jetbrains_kotlinx_kotlinx-serialization-cbor-jvm",
                "org_jetbrains_kotlinx_kotlinx-serialization-core-jvm",
                "org_jetbrains_kotlinx_kotlinx-serialization-json-jvm", "org_jheaps_jheaps",
                "org_jgrapht_jgrapht-core", "org_quiltmc_parsers_gson", "org_quiltmc_parsers_json",
                "org_reflections_reflections", "overflowingbars", "owo", "packet_tweaker", "paradise_lost",
                "patchouli", "physicsmod", "placeholder-api", "player-animator", "playerabilitylib",
                "polymer-blocks", "polymer-bundled", "polymer-common", "polymer-core", "polymer-networking",
                "polymer-registry-sync-manipulator", "polymer-resource-pack", "polymer-virtual-entity",
                "polymorph", "porting_lib_accessors", "porting_lib_attributes", "porting_lib_base",
                "porting_lib_brewing", "porting_lib_client_events", "porting_lib_common", "porting_lib_config",
                "porting_lib_core", "porting_lib_data", "porting_lib_entity", "porting_lib_extensions",
                "porting_lib_fluids", "porting_lib_gametest", "porting_lib_lazy_registration", "porting_lib_loot",
                "porting_lib_mixin_extensions", "porting_lib_model_builders", "porting_lib_model_generators",
                "porting_lib_model_loader", "porting_lib_model_materials", "porting_lib_models",
                "porting_lib_networking", "porting_lib_obj_loader", "porting_lib_registries", "porting_lib_tags",
                "porting_lib_tool_actions", "porting_lib_transfer", "porting_lib_utility", "presencefootsteps",
                "prioritytarget", "puffish_skills", "puzzlesaccessapi", "puzzleslib", "reach-entity-attributes",
                "reeses-sodium-options", "registrate-fabric", "reinfchest", "reinfcore", "replaymod",
                "resourcefulconfig", "resourcefullib", "revelationary", "reverb", "rhino", "roughlyenoughitems",
                "rpgdifficulty", "seasons", "seasonsextras", "server_translations_api", "servercore",
                "serverpingerfixer", "shetiphiancore", "shouldersurfing", "simpleprivatechest", "smartbrainlib",
                "smoothswapping", "sodium", "sodium-extra", "soulsweapons", "spark", "spectrelib", "spectrum",
                "spoornbountymobs", "spruceui", "step-height-entity-attribute", "supermartijn642configlib",
                "supermartijn642corelib", "tcdcommons", "team_reborn_energy", "tectonic", "tenshilib",
                "terrablender", "threadtweak", "tlc", "tooltipfix", "torchbowmod", "torohealth", "trinkets",
                "twilightforest", "villagesandpillages", "voidz", "watersource", "wi_zoom", "withdrawals", "wthit",
                "xaerominimap", "xaeroworldmap", "xlpackets", "yet_another_config_lib_v3", "yungsapi",
                "yungsbridges", "zombiehorsespawn"
        );
        // 预期数量已从 371 更新为 372
        return performCheck("标准客户端", requiredMods, names, 372);
    }

    /**
     * Modrinth 版客户端模组验证。
     */
    public boolean modrinth(Collection<String> names) {
        List<String> requiredMods = Arrays.asList(
                "ad_astra", "advanced_runtime_resource_pack", "adventurez", "ae2", "aftershock", "aliveandwell",
                "alternate-current", "appleskin", "arachnids", "architectury", "attributefix", "awesomedungeon",
                "awesomedungeonnether", "azurelib", "badpackets", "bclib", "bettercombat", "betterfpsdist",
                "blue_endless_jankson", "bosses_of_mass_destruction", "botarium", "caffeineconfig",
                "cardinal-components", "cardinal-components-base", "cardinal-components-block",
                "cardinal-components-chunk", "cardinal-components-entity", "cardinal-components-item",
                "cardinal-components-level", "cardinal-components-scoreboard", "cardinal-components-world",
                "carryon", "charmofundying", "chunky", "cleancut", "cloth-basic-math", "cloth-config",
                "collective", "com_electronwill_night-config_core", "com_electronwill_night-config_toml",
                "com_eliotlash_mclib_mclib", "com_github_llamalad7_mixinextras", "com_teamresourceful_bytecodecs",
                "com_teamresourceful_yabn", "com_twelvemonkeys_common_common-image",
                "com_twelvemonkeys_common_common-io", "com_twelvemonkeys_common_common-lang",
                "com_twelvemonkeys_imageio_imageio-core", "com_twelvemonkeys_imageio_imageio-metadata",
                "com_twelvemonkeys_imageio_imageio-webp", "com_velocitypowered_velocity-native", "combatroll",
                "common-protection-api", "config_toolkit", "connectivity", "corgilib", "creeperoverhaul",
                "crowdin-translate", "cull-less-leaves", "cupboard", "customportalapi", "darkfear", "diet",
                "disable_custom_worlds_advice", "doom", "dragonfight", "earthtojavamobs", "elytraslot", "endrem",
                "enhancedblockentities", "entity_model_features", "entity_texture_features", "enhancedcelestials",
                "entityculling", "explosiveenhancement", "extended_drawers", "extra_details", "fabric-api",
                "fabric-api-base", "fabric-api-lookup-api-v1", "fabric-biome-api-v1", "fabric-block-api-v1",
                "fabric-block-view-api-v2", "fabric-blockrenderlayer-v1", "fabric-client-tags-api-v1",
                "fabric-command-api-v1", "fabric-command-api-v2", "fabric-commands-v0", "fabric-containers-v0",
                "fabric-content-registries-v0", "fabric-convention-tags-v1", "fabric-crash-report-info-v1",
                "fabric-data-generation-api-v1", "fabric-dimensions-v1", "fabric-entity-events-v1",
                "fabric-events-interaction-v0", "fabric-events-lifecycle-v0", "fabric-game-rule-api-v1",
                "fabric-item-api-v1", "fabric-item-group-api-v1", "fabric-key-binding-api-v1",
                "fabric-keybindings-v0", "fabric-language-kotlin", "fabric-lifecycle-events-v1",
                "fabric-loot-api-v2", "fabric-loot-tables-v1", "fabric-message-api-v1",
                "fabric-mining-level-api-v1", "fabric-model-loading-api-v1", "fabric-models-v0",
                "fabric-networking-api-v1", "fabric-networking-v0", "fabric-object-builder-api-v1",
                "fabric-particles-v1", "fabric-permissions-api-v0", "fabric-recipe-api-v1",
                "fabric-registry-sync-v0", "fabric-renderer-api-v1", "fabric-renderer-indigo",
                "fabric-renderer-registries-v1", "fabric-rendering-data-attachment-v1",
                "fabric-rendering-fluids-v1", "fabric-rendering-v0", "fabric-rendering-v1",
                "fabric-resource-conditions-api-v1", "fabric-resource-loader-v0", "fabric-screen-api-v1",
                "fabric-screen-handler-api-v1", "fabric-sound-api-v1", "fabric-transfer-api-v1",
                "fabric-transitive-access-wideners-v1", "fabricloader", "faster_entity_animations", "ferritecore",
                "fiber", "ftblibrary", "ftbquests", "ftbteams", "geckolib", "gitbm", "gpumemleakfix", "graphlib",
                "hashs_falcons_mr", "human_reborn", "huskspawn", "hwg", "immediatelyfast", "immersive_aircraft",
                "improvedmobs", "imst", "indium", "inventoryhud", "inventoryprofilesnext",
                "io_determann_shadow-api-17", "io_github_douira_glsl-transformer", "iris", "ironchests",
                "itemfilters", "java", "kaffees_dual_ride", "kambrik", "kirin", "kmodlib-overlay", "krypton",
                "kubejs", "lanserverproperties", "levelz", "libipn", "libnetworkstack", "libraryferret", "libz",
                "libzoomer", "lithium", "lootr", "maelstrom_library", "magna", "mc249136", "mcdw", "minecells",
                "minecraft", "modern_industrialization", "modernfix", "modmenu", "multipart_entities",
                "mythicmounts", "nametagtweaks", "nckey", "net_fabricmc_javapoet", "net_lenni0451_reflect",
                "nicer-skies", "no-telemetry", "noindium", "notenoughanimations", "nyctophobia", "omega-config",
                "org_anarres_jcpp", "org_antlr_antlr4-runtime", "org_apache_commons_commons-math3",
                "org_apache_httpcomponents_httpmime", "org_javassist_javassist", "org_jetbrains_kotlin_kotlin-reflect",
                "org_jetbrains_kotlin_kotlin-stdlib", "org_jetbrains_kotlin_kotlin-stdlib-jdk7",
                "org_jetbrains_kotlin_kotlin-stdlib-jdk8", "org_jetbrains_kotlinx_atomicfu-jvm",
                "org_jetbrains_kotlinx_kotlinx-coroutines-core-jvm", "org_jetbrains_kotlinx_kotlinx-coroutines-jdk8",
                "org_jetbrains_kotlinx_kotlinx-datetime-jvm", "org_jetbrains_kotlinx_kotlinx-serialization-cbor-jvm",
                "org_jetbrains_kotlinx_kotlinx-serialization-core-jvm",
                "org_jetbrains_kotlinx_kotlinx-serialization-json-jvm", "org_reflections_reflections", "owo",
                "packedup", "packet_tweaker", "patchouli", "player-animator", "playerabilitylib", "polymer-blocks",
                "polymer-bundled", "polymer-common", "polymer-core", "polymer-networking",
                "polymer-registry-sync-manipulator", "polymer-resource-pack", "polymer-virtual-entity",
                "polymorph", "presencefootsteps", "puffish_skills", "reach-entity-attributes",
                "reeses-sodium-options", "resourcefulconfig", "resourcefulconfig199a", "resourcefullib", "rhino",
                "rpgdifficulty", "senguiengling", "simpleprivatechest", "smartbrainlib", "sodium", "sodium-extra",
                "soulsweapons", "spark", "spectrelib", "spoornbountymobs", "spruceui", "starlight",
                "supermartijn642configlib", "supermartijn642corelib", "team_reborn_energy", "tectonic",
                "tenshilib", "terrablender", "terralith", "tlc", "tooltipfix", "torchbowmod", "trinkets",
                "uselessreptile", "veinmining", "voidz", "watching", "wi_zoom", "wthit", "xaerominimap",
                "xaeroworldmap", "yet_another_config_lib_v3", "yungsapi", "yungsbridges", "zombiehorsespawn"
        );
        return performCheck("Modrinth 客户端", requiredMods, names, 274);
    }

    /**
     * 标准版服务器模组验证。
     */
    public boolean server(Collection<String> names) {
        List<String> requiredMods = Arrays.asList(
                "achiopt", "ad_astra", "additionalentityattributes", "advancedbackups", "adventurez", "ae2",
                "aftershock", "aliveandwell", "alternate-current", "antixray", "anvilrestoration", "appleskin",
                "arachnids", "architectury", "arrowhead", "attributefix", "awesomedungeon", "awesomedungeonnether",
                "azurelib", "badpackets", "balm-fabric", "benched", "bettercombat", "betterfpsdist", "betterstats",
                "blue_endless_jankson", "bookshelf", "bosses_of_mass_destruction", "botania", "botarium",
                "bowinfinityfix", "cardinal-components", "cardinal-components-base", "cardinal-components-block",
                "cardinal-components-chunk", "cardinal-components-entity", "cardinal-components-item",
                "cardinal-components-level", "cardinal-components-scoreboard", "cardinal-components-world",
                "cavespiderspawn", "charmofundying", "chunky", "cleancut", "cloth-basic-math", "cloth-config",
                "collective", "com_electronwill_night-config_core", "com_electronwill_night-config_toml",
                "com_eliotlash_mclib_mclib", "com_google_code_findbugs_jsr305", "com_moandjiezana_toml_toml4j",
                "com_teamresourceful_bytecodecs", "com_teamresourceful_yabn", "com_twelvemonkeys_common_common-image",
                "com_twelvemonkeys_common_common-io", "com_twelvemonkeys_common_common-lang",
                "com_twelvemonkeys_imageio_imageio-core", "com_twelvemonkeys_imageio_imageio-metadata",
                "com_twelvemonkeys_imageio_imageio-webp", "com_velocitypowered_velocity-native", "combatroll",
                "configuration", "connectivity", "coroutil", "create", "createaddition", "creeperoverhaul",
                "cupboard", "cursery", "customportalapi", "darkfear", "deeperdarker", "diet", "doom",
                "dragonfight", "dragonloot", "dripstone_fluid_lib", "dungeons_arise", "dungeons_arise_seven_seas",
                "earthtojavamobs", "eldritch_mobs", "elytraslot", "endrem", "error_notifier", "euphonium",
                "extendedae", "extensibleenums", "fabric-api", "fabric-api-base", "fabric-api-lookup-api-v1",
                "fabric-biome-api-v1", "fabric-block-api-v1", "fabric-block-view-api-v2", "fabric-command-api-v1",
                "fabric-command-api-v2", "fabric-commands-v0", "fabric-containers-v0", "fabric-content-registries-v0",
                "fabric-convention-tags-v1", "fabric-crash-report-info-v1", "fabric-data-attachment-api-v1",
                "fabric-data-generation-api-v1", "fabric-dimensions-v1", "fabric-entity-events-v1",
                "fabric-events-interaction-v0", "fabric-events-lifecycle-v0", "fabric-game-rule-api-v1",
                "fabric-item-api-v1", "fabric-item-group-api-v1", "fabric-language-kotlin",
                "fabric-lifecycle-events-v1", "fabric-loot-api-v2", "fabric-loot-tables-v1",
                "fabric-message-api-v1", "fabric-mining-level-api-v1", "fabric-networking-api-v1",
                "fabric-networking-v0", "fabric-object-builder-api-v1", "fabric-particles-v1",
                "fabric-permissions-api-v0", "fabric-recipe-api-v1", "fabric-registry-sync-v0",
                "fabric-rendering-data-attachment-v1", "fabric-rendering-fluids-v1",
                "fabric-resource-conditions-api-v1", "fabric-resource-loader-v0", "fabric-screen-handler-api-v1",
                "fabric-transfer-api-v1", "fabric-transitive-access-wideners-v1", "fabricloader", "fabricshieldlib",
                "ferritecore", "fiber", "folk_sisby_kaleido-config", "forgeconfigapiport", "fractal", "ftblibrary",
                "ftbquests", "ftbteams", "ftbxmodcompat", "geckolib", "gitbm", "gpumemleakfix", "human_reborn",
                "huskspawn", "illagerinvasion", "immersive_aircraft", "immersive_melodies", "improvedmobs", "imst",
                "inmis", "inmisaddon", "java", "javax_annotation_javax_annotation-api", "javazoom_jlayer",
                "kaffees_dual_ride", "krypton", "kubejs", "lanserverproperties", "levelz", "libraryferret", "libz",
                "lithium", "lootr", "lunar", "maelstrom_library", "magna", "matchbooks", "mc249136", "mcdw",
                "memoryclearermissnotoredict", "memoryleakfix", "midnightlib", "milk", "minecells", "minecraft",
                "mixinextras", "mm", "mobtimizations", "mobz", "modern_industrialization", "modernfix", "modifiers",
                "monolib", "movingelevators", "mr_camps_castles_carriages", "mr_reds_morestructures", "mr_ships",
                "mr_true_ending", "multipart_entities", "mythicmounts", "nametagtweaks", "nebulus_cherry_blossem_tree",
                "nice_boat", "noisium", "notenoughanimations", "nyctophobia", "omega-config",
                "org_apache_commons_commons-math3", "org_javassist_javassist", "org_jetbrains_kotlin_kotlin-reflect",
                "org_jetbrains_kotlin_kotlin-stdlib", "org_jetbrains_kotlin_kotlin-stdlib-jdk7",
                "org_jetbrains_kotlin_kotlin-stdlib-jdk8", "org_jetbrains_kotlinx_atomicfu-jvm",
                "org_jetbrains_kotlinx_kotlinx-coroutines-core-jvm", "org_jetbrains_kotlinx_kotlinx-coroutines-jdk8",
                "org_jetbrains_kotlinx_kotlinx-datetime-jvm", "org_jetbrains_kotlinx_kotlinx-serialization-cbor-jvm",
                "org_jetbrains_kotlinx_kotlinx-serialization-core-jvm",
                "org_jetbrains_kotlinx_kotlinx-serialization-json-jvm", "org_jgrapht_jgrapht-core",
                "org_jheaps_jheaps", "org_quiltmc_parsers_gson", "org_quiltmc_parsers_json",
                "org_reflections_reflections", "owo", "packet_tweaker", "paradise_lost", "patchouli", "physicsmod",
                "placeholder-api", "player-animator", "playerabilitylib", "polymer-autohost", "polymer-blocks",
                "polymer-bundled", "polymer-common", "polymer-core", "polymer-networking",
                "polymer-registry-sync-manipulator", "polymer-resource-pack", "polymer-virtual-entity",
                "polymorph", "porting_lib_accessors", "porting_lib_attributes", "porting_lib_base", "porting_lib_brewing",
                "porting_lib_client_events", "porting_lib_common", "porting_lib_config", "porting_lib_core",
                "porting_lib_data", "porting_lib_entity", "porting_lib_extensions", "porting_lib_fluids",
                "porting_lib_gametest", "porting_lib_lazy_registration", "porting_lib_loot",
                "porting_lib_mixin_extensions", "porting_lib_model_builders", "porting_lib_model_generators",
                "porting_lib_model_loader", "porting_lib_model_materials", "porting_lib_models",
                "porting_lib_networking", "porting_lib_obj_loader", "porting_lib_registries", "porting_lib_tags",
                "porting_lib_tool_actions", "porting_lib_transfer", "porting_lib_utility", "prioritytarget",
                "puffish_skills", "puzzlesaccessapi", "puzzleslib", "reach-entity-attributes", "registrate-fabric",
                "reinfchest", "reinfcore", "resourcefulconfig", "resourcefullib", "revelationary", "reverb",
                "rhino", "roughlyenoughitems", "rpgdifficulty", "seasons", "seasonsextras",
                "server_translations_api", "servercore", "shetiphiancore", "simpleprivatechest", "smartbrainlib",
                "soulsweapons", "spark", "spectrelib", "spectrum", "spoornbountymobs", "step-height-entity-attribute",
                "supermartijn642configlib", "supermartijn642corelib", "tcdcommons", "team_reborn_energy", "tectonic",
                "tenshilib", "terrablender", "threadtweak", "tlc", "torchbowmod", "trinkets", "twilightforest",
                "villagesandpillages", "voidz", "wthit", "xaerominimap", "xaeroworldmap", "xlpackets",
                "yet_another_config_lib_v3", "yungsapi", "yungsbridges", "zombiehorsespawn", "watersource", "withdrawals"
        );
        return performCheck("标准服务器", requiredMods, names, 315);
    }

    /**
     * 通用的模组检查逻辑。
     * @param profileName 检查配置文件的名称 (例如 "标准客户端")，用于日志输出。
     * @param requiredMods 必需的模组ID列表。
     * @param loadedMods 当前加载的模组ID集合。
     * @param expectedSize 预期的模组总数。
     * @return 如果检查通过则返回 true。
     */
    private boolean performCheck(String profileName, List<String> requiredMods, Collection<String> loadedMods, int expectedSize) {
        AliveAndWellMain.LOGGER.info("--- 开始 {} 模组依赖检测 ---", profileName);
        boolean allChecksPassed = true;

        // 检查缺失的模组
        for (String requiredModId : requiredMods) {
            if (!loadedMods.contains(requiredModId)) {
                AliveAndWellMain.LOGGER.error("[依赖检测失败] {} 必需的模组 '{}' 未找到!", profileName, requiredModId);
                allChecksPassed = false;
            }
        }

        // 检查总数是否匹配
        if (loadedMods.size() != expectedSize) {
            AliveAndWellMain.LOGGER.error("[依赖检测失败] {} 模组总数不匹配! 预期: {}, 实际: {}", profileName, expectedSize, loadedMods.size());
            allChecksPassed = false;

            // 找出多余的模组
            List<String> extraMods = loadedMods.stream()
                    .filter(mod -> !requiredMods.contains(mod))
                    .collect(Collectors.toList());
            if (!extraMods.isEmpty()) {
                AliveAndWellMain.LOGGER.warn("检测到以下多余的模组: {}", extraMods);
            }
        }

        if (allChecksPassed) {
            AliveAndWellMain.LOGGER.info("--- {} 模组依赖检测通过 ---", profileName);
        } else {
            AliveAndWellMain.LOGGER.error("--- {} 模组依赖检测未通过 ---", profileName);
        }
        return allChecksPassed;
    }
}
