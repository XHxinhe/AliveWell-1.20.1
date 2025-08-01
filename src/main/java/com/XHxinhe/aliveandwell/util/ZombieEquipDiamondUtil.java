package com.XHxinhe.aliveandwell.util;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Random;

public class ZombieEquipDiamondUtil {
    public static void equip(ZombieEntity entity){
        int rand = new Random().nextInt(100)+1;//几率
        int j = new Random().nextInt(20);//装备：头盔、胸甲、护腿、靴子，搭配工具的几率
        int k = new Random().nextInt(5);//工具类型：剑，镐子，斧子，铲子，锄头
        int k1 = new Random().nextInt(5);//工具类型：剑，镐子，斧子，铲子，锄头
        int l = new Random().nextInt(30);
        //有30%几率装备铁剑，20%几率+装备一件，15%几率+装备两件，10%装备三件，5%装备四件
        if(rand  <= 30){
            if (k==0){
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
            }else if (k==1) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));
            } else if (k==2) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_PICKAXE));
            } else if (k==3) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SHOVEL));
            } else {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_HOE));
            }
        } else if (rand <=50) {
            if(j == 0 ){
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));//剑
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
            } else if (j==1) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
            } else if (j==2) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            } else if (j==3) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (j==4) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));//斧子
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
            }else if (j==5) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
            }else if (j==6) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            }else if (j==7) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (j==8) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_PICKAXE));//镐子
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
            }else if (j==9) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_PICKAXE));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
            }else if (j==10) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_PICKAXE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            }else if (j==11) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_PICKAXE));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (j==12) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SHOVEL));//铲子
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
            }else if (j==13) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SHOVEL));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
            }else if (j==14) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SHOVEL));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            }else if (j==15) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SHOVEL));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (j==16) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_HOE));//锄头
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
            }else if (j==17) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_HOE));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
            }else if (j==18) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_HOE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            }else {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_HOE));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }
        } else if (rand <=65) {
            if(l == 0 ){//12,13,14,23,24,34
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));//剑12=================================
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
            } else if (l==1) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));//13
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            } else if (l==2) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));//14
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            } else if (l==3) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));//23
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            }else if (l==4) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));//24
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==5) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));//34
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==6) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));//斧子12=================================
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
            }else if (l==7) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));//13
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            }else if (l==8) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));//14
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==9) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));//23
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            }else if (l==10) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));//24
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==11) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));//34
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==12) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_PICKAXE));//镐子12=================================
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
            }else if (l==13) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_PICKAXE));//13
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            }else if (l==14) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_PICKAXE));//14
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==15) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_PICKAXE));//23
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            }else if (l==16) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_PICKAXE));//24
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==17) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_PICKAXE));//34
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==18) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SHOVEL));//铲子12=================================
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
            }else if (l==19) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SHOVEL));//13
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            }else if (l==20) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SHOVEL));//14
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==21) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SHOVEL));//23
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            }else if (l==22) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SHOVEL));//24
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==23) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SHOVEL));//34
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==24) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_HOE));//锄头12=================================
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
            }else if (l==25) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_HOE));//13
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            }else if (l==26) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_HOE));//14
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==27) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_HOE));//23
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            }else if (l==28) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_HOE));//24
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_HOE));//34
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }
        } else if (rand <= 75) {
            if(l == 0 ){//123,124,134,234
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));//剑123=================================
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            } else if (l==1) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));//124
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            } else if (l==2) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));//134
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            } else if (l==3) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));//234
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==6) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));//斧子123=================================
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            }else if (l==7) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));//124
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==8) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));//134
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==9) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));//234
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==12) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_PICKAXE));//镐子123=================================
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            }else if (l==13) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_PICKAXE));//124
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==14) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_PICKAXE));//134
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==15) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_PICKAXE));//234
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==18) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SHOVEL));//铲子123=================================
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            }else if (l==19) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SHOVEL));//124
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==20) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SHOVEL));//134
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==21) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SHOVEL));//234
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==24) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_HOE));//锄头123=================================
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            }else if (l==25) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_HOE));//124
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==26) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_HOE));//134
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (l==27) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_HOE));//234
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }
        } else if (rand <= 80) {
            if (k1==0) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));//1234
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (k1==1) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));//1234
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (k1==2) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_PICKAXE));//1234
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else if (k1==3) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SHOVEL));//1234
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }else {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_HOE));//1234
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }
        }else {
            if (k==0){
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));//234
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
            }else if (k==1) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));//234
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
            } else if (k==2) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));//234
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
            } else if (k==3) {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));//234
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
            } else {
                entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));//234
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
            }
        }
    }
}
