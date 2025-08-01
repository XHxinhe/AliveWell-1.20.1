package com.XHxinhe.aliveandwell.util;

import net.fabricmc.loader.api.FabricLoader;
import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public class ConfigLock {
    private static final Path gameFilePath = FabricLoader.getInstance().getGameDir();//游戏文件路径
    private static final Path gameConfigFilePath = FabricLoader.getInstance().getConfigDir();//游戏文件路径

    public static final String doom_file = "kubejs/data/gloom/doom.json";
    public static final String doom_fileConfig = "doom.json";

    public static final String hwg_file = "kubejs/data/phwgu/hwg.json";
    public static final String hwg_fileConfig = "hwg.json";

    public static final String levelz_file = "kubejs/data/hlevelka/levelz.json5";
    public static final String levelz_fileConfig = "levelz.json5";
    public static final String playerex_file = "kubejs/data/wplaygex/playerex.json";
    public static final String playerex_fileConfig = "playerex.json";
    public static final String rpgdifficulty_file = "kubejs/data/vexrpgk/rpgdifficulty.json";
    public static final String rpgdifficulty_fileConfig = "rpgdifficulty.json";

    public static final String spoornbountymobs_file = "kubejs/data/opsooboune/spoornbountymobs.json5";
    public static final String spoornbountymobs_fileConfig = "spoornbountymobs.json5";
    public static final String diet_server_file = "kubejs/data/riadmantion/diet-server.toml";
    public static final String diet_server_fileConfig = "diet-server.toml";

    public static final String diet_fruits_file = "kubejs/data/xdianet/fruits.json";
    public static final String diet_fruits_file1 = "kubejs/data/diet/diet/groups/fruits.json";

    public static final String diet_grains_file = "kubejs/data/xdianet/grains.json";
    public static final String diet_grains_file1 = "kubejs/data/diet/diet/groups/grains.json";

    public static final String diet_proteins_file = "kubejs/data/xdianet/proteins.json";
    public static final String diet_proteins_file1 = "kubejs/data/diet/diet/groups/proteins.json";

    public static final String diet_sugars_file = "kubejs/data/xdianet/sugars.json";
    public static final String diet_sugars_file1 = "kubejs/data/diet/diet/groups/sugars.json";

    public static final String diet_vegetables_file = "kubejs/data/xdianet/vegetables.json";
    public static final String diet_vegetables_file1 = "kubejs/data/diet/diet/groups/vegetables.json";
    //========================diet===============================

    //========================diet items===============================
    public static final String diet_items_fruits_file = "kubejs/data/uarray/fruits.json";
    public static final String diet_items_fruits_file1 = "kubejs/data/diet/tags/items/fruits.json";

    public static final String diet_items_grains_file = "kubejs/data/graykiy/grains.json";
    public static final String diet_items_grains_file1 = "kubejs/data/diet/tags/items/grains.json";

    public static final String diet_items_ingredients_file = "kubejs/data/emarryom/ingredients.json";
    public static final String diet_items_ingredients_file1 = "kubejs/data/diet/tags/items/ingredients.json";

    public static final String diet_items_proteins_file = "kubejs/data/operatemon/proteins.json";
    public static final String diet_items_proteins_file1 = "kubejs/data/diet/tags/items/proteins.json";

    public static final String diet_items_special_food_file = "kubejs/data/usrallays/special_food.json";
    public static final String diet_items_special_food_file1 = "kubejs/data/diet/tags/items/special_food.json";

    public static final String diet_items_sugars_file = "kubejs/data/kuoobsey/sugars.json";
    public static final String diet_items_sugars_file1 = "kubejs/data/diet/tags/items/sugars.json";

    public static final String diet_items_vegetables_file = "kubejs/data/feuimant/vegetables.json";
    public static final String diet_items_vegetables_file1 = "kubejs/data/diet/tags/items/vegetables.json";
    //========================diet items===============================

    public static final String bettercombat_axe_file = "kubejs/data/piatomxing/axe.json";
    public static final String bettercombat_axe_file1 = "kubejs/data/bettercombat/weapon_attributes/axe.json";

    public static final String bettercombat_pickaxe_file = "kubejs/data/mintcontor/pickaxe.json";
    public static final String bettercombat_pickaxe_file1 = "kubejs/data/bettercombat/weapon_attributes/pickaxe.json";

    public static final String bettercombat_sword_file = "kubejs/data/niteungrant/sword.json";
    public static final String bettercombat_sword_file1 = "kubejs/data/bettercombat/weapon_attributes/sword.json";

    public static final String improvedmobs_common_file = "kubejs/data/gaegdas/common.json";
    public static final String improvedmobs_common_fileConfig = "improvedmobs/common.json";
    public static final String improvedmobs_equipment_file = "kubejs/data/grgzhthd/equipment.json";
    public static final String improvedmobs_equipment_fileConfig = "improvedmobs/equipment.json";

    public static final String diet_suites_builtin_file = "kubejs/data/wsanment/builtin.json";
    public static final String diet_suites_builtin_file1 = "kubejs/data/diet/diet/suites/builtin.json";

    public static final String mchalo_file = "kubejs/data/majvoi/mchalo.json";
    public static final String mchalo_fileConfig = "mchalo.json";

    public ConfigLock() {
    }

    public boolean isDefaultConfigConnect() throws IOException {
        return this.isSameDoom() && this.isSameLevelz() && this.isSamePlayerex() && this.isSameRpgdifficulty() && this.isSameSpoornbountymobs();
    }

    public boolean isDefaultConfig() throws IOException {
        return this.isSameHwg() && this.isSameDietFruits() && this.isSameDietGrains() && this.isSameDietProteins() && this.isSameDietSugars() && this.isSameDietVegetables() && this.isSameDietServer() && this.isSameItemsfruits() && this.isSameItemsGrains() && this.isSameItemsIngredients() && this.isSameItemsProteins() && this.isSameItemsSpecialFood() && this.isSameItemsSugars() && this.isSameItemsVegetables() && this.isSameBettercombatAxe() && this.isSameBettercombatPickaxe() && this.isSameBettercombatSword() && this.isSameImprovedmobsCommon() && this.isSameImprovedmobsEquipment() && this.isSameDietSuitesBuiltin() && this.isSameMchalo();
    }

//    public boolean isDefaultConfig() throws IOException {
//        return isSameDoom() && isSameHwg() && isSameLevelz() && isSamePlayerex() && isSameRpgdifficulty() && isSameSpoornbountymobs()
//                && isSameDietFruits() && isSameDietGrains() && isSameDietProteins() && isSameDietSugars() && isSameDietVegetables() && isSameDietServer()
//                && isSameItemsfruits() && isSameItemsGrains() && isSameItemsIngredients() && isSameItemsProteins() && isSameItemsSpecialFood() && isSameItemsSugars() && isSameItemsVegetables()
//                && isSameBettercombatAxe() && isSameBettercombatPickaxe() && isSameBettercombatSword()
//                ;
//    }
    public boolean isSameImprovedmobsCommon() throws IOException {
        File file1 = gameFilePath.resolve(improvedmobs_common_file).toFile();//整合包默认配置
        File file2 = gameConfigFilePath.resolve(improvedmobs_common_fileConfig).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("improvedmobs")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }
    public boolean isSameImprovedmobsEquipment() throws IOException {
        File file1 = gameFilePath.resolve(improvedmobs_equipment_file).toFile();//整合包默认配置
        File file2 = gameConfigFilePath.resolve(improvedmobs_equipment_fileConfig).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("improvedmobs")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSameDietSuitesBuiltin() throws IOException {
        File file1 = gameFilePath.resolve(diet_suites_builtin_file).toFile();//整合包默认配置
        File file2 = gameFilePath.resolve(diet_suites_builtin_file1).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("diet")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSameDoom() throws IOException {
        File file1 = gameFilePath.resolve(doom_file).toFile();//整合包默认配置
        File file2 = gameConfigFilePath.resolve(doom_fileConfig).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("doom")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSameHwg() throws IOException {
        File file1 = gameFilePath.resolve(hwg_file).toFile();//整合包默认配置
        File file2 = gameConfigFilePath.resolve(hwg_fileConfig).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("hwg")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSameLevelz() throws IOException {
        File file1 = gameFilePath.resolve(levelz_file).toFile();//整合包默认配置
        File file2 = gameConfigFilePath.resolve(levelz_fileConfig).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("levelz")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSamePlayerex() throws IOException {
        File file1 = gameFilePath.resolve(playerex_file).toFile();//整合包默认配置
        File file2 = gameConfigFilePath.resolve(playerex_fileConfig).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("playerex")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSameRpgdifficulty() throws IOException {
        File file1 = gameFilePath.resolve(rpgdifficulty_file).toFile();//整合包默认配置
        File file2 = gameConfigFilePath.resolve(rpgdifficulty_fileConfig).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("rpgdifficulty")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSameSpoornbountymobs() throws IOException {
        File file1 = gameFilePath.resolve(spoornbountymobs_file).toFile();//整合包默认配置
        File file2 = gameConfigFilePath.resolve(spoornbountymobs_fileConfig).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("spoornbountymobs")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSameDietServer() throws IOException {
        File file1 = gameFilePath.resolve(diet_server_file).toFile();//整合包默认配置
        File file2 = gameConfigFilePath.resolve(diet_server_fileConfig).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("diet")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSameDietFruits() throws IOException {
        File file1 = gameFilePath.resolve(diet_fruits_file).toFile();//整合包默认配置
        File file2 = gameFilePath.resolve(diet_fruits_file1).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("diet")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSameDietGrains() throws IOException {
        File file1 = gameFilePath.resolve(diet_grains_file).toFile();//整合包默认配置
        File file2 = gameFilePath.resolve(diet_grains_file1).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("diet")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSameDietProteins() throws IOException {
        File file1 = gameFilePath.resolve(diet_proteins_file).toFile();//整合包默认配置
        File file2 = gameFilePath.resolve(diet_proteins_file1).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("diet")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSameDietSugars() throws IOException {
        File file1 = gameFilePath.resolve(diet_sugars_file).toFile();//整合包默认配置
        File file2 = gameFilePath.resolve(diet_sugars_file1).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("diet")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSameDietVegetables() throws IOException {
        File file1 = gameFilePath.resolve(diet_vegetables_file).toFile();//整合包默认配置
        File file2 = gameFilePath.resolve(diet_vegetables_file1).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("diet")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSameItemsfruits() throws IOException {
        File file1 = gameFilePath.resolve(diet_items_fruits_file).toFile();//整合包默认配置
        File file2 = gameFilePath.resolve(diet_items_fruits_file1).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("diet")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSameItemsGrains() throws IOException {
        File file1 = gameFilePath.resolve(diet_items_grains_file).toFile();//整合包默认配置
        File file2 = gameFilePath.resolve(diet_items_grains_file1).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("diet")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSameItemsIngredients() throws IOException {
        File file1 = gameFilePath.resolve(diet_items_ingredients_file).toFile();//整合包默认配置
        File file2 = gameFilePath.resolve(diet_items_ingredients_file1).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("diet")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSameItemsProteins() throws IOException {
        File file1 = gameFilePath.resolve(diet_items_proteins_file).toFile();//整合包默认配置
        File file2 = gameFilePath.resolve(diet_items_proteins_file1).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("diet")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSameItemsSpecialFood() throws IOException {
        File file1 = gameFilePath.resolve(diet_items_special_food_file).toFile();//整合包默认配置
        File file2 = gameFilePath.resolve(diet_items_special_food_file1).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("diet")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSameItemsSugars() throws IOException {
        File file1 = gameFilePath.resolve(diet_items_sugars_file).toFile();//整合包默认配置
        File file2 = gameFilePath.resolve(diet_items_sugars_file1).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("diet")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSameItemsVegetables() throws IOException {
        File file1 = gameFilePath.resolve(diet_items_vegetables_file).toFile();//整合包默认配置
        File file2 = gameFilePath.resolve(diet_items_vegetables_file1).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("diet")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSameBettercombatAxe() throws IOException {
        File file1 = gameFilePath.resolve(bettercombat_axe_file).toFile();//整合包默认配置
        File file2 = gameFilePath.resolve(bettercombat_axe_file1).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("bettercombat")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSameBettercombatPickaxe() throws IOException {
        File file1 = gameFilePath.resolve(bettercombat_pickaxe_file).toFile();//整合包默认配置
        File file2 = gameFilePath.resolve(bettercombat_pickaxe_file1).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("bettercombat")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }
    public boolean isSameBettercombatSword() throws IOException {
        File file1 = gameFilePath.resolve(bettercombat_sword_file).toFile();//整合包默认配置
        File file2 = gameFilePath.resolve(bettercombat_sword_file1).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("bettercombat")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSameMchalo() throws IOException {
        File file1 = gameFilePath.resolve(mchalo_file).toFile();//整合包默认配置
        File file2 = gameConfigFilePath.resolve(mchalo_fileConfig).toFile();//实际加载的配置

        if(FabricLoader.getInstance().isModLoaded("mchalo")){
            return isSameFile(file1, file2);
        }else {
            return true;
        }
    }

    public boolean isSameFile(File file1, File file2) throws IOException {
        FileInputStream fis1 = null;
        FileInputStream fis2 = null;

        boolean var7;
        try {
            fis1 = new FileInputStream(file1);
            fis2 = new FileInputStream(file2);
            int len1 = fis1.available();//返回总的字节数
            int len2 = fis2.available();
            if(len1 == len2){//长度相同，则比较具体内容
                //比较内容
                //建立两个字节缓冲区
                byte[] data1 = new byte[len1];
                byte[] data2 = new byte[len2];

                //分别将两个文件的内容读入缓冲区
                fis1.read(data1);
                fis2.read(data2);

                //依次比较两个文件中的每一个字节
                for (int i = 0; i < len1; i++) {
                    //只要有一个字节不同，则两个文件就不一样
                    if(data1[i] != data2[i]){
//                        System.out.println("两个文件内容不一样");
                        return false;
                    }
                }
//                System.out.println("两个文件内容一样");
                return true;
            }else {
//                System.out.println("两个文件长度一样");
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {//关闭文件流，防止内存泄漏
            if(fis1 != null){
                try {
                    fis1.close();
                }catch (IOException e){
                    //忽略
                    e.printStackTrace();
                }
            }

            if(fis2 != null){
                try {
                    fis2.close();
                }catch (IOException e){
                    //忽略
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
