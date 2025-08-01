package com.XHxinhe.aliveandwell.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;

import java.lang.reflect.Field;

public class CanJoinEnd {
    public CanJoinEnd() {
    }

    public static boolean canJoinEnd1(ServerPlayerEntity player) throws IllegalAccessException {
        Field can_end1 = null;

        try {
            can_end1 = ServerPlayerEntity.class.getDeclaredField("can_end1");
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        assert can_end1 != null;

        return can_end1.getBoolean(player);
    }

    public static boolean canJoinEnd2(ServerPlayerEntity player) throws IllegalAccessException {
        Field can_end2 = null;

        try {
            can_end2 = ServerPlayerEntity.class.getDeclaredField("can_end2");
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        assert can_end2 != null;

        return can_end2.getBoolean(player);
    }

    public static boolean canJoinEnd3(ServerPlayerEntity player) throws IllegalAccessException {
        Field can_end3 = null;

        try {
            can_end3 = ServerPlayerEntity.class.getDeclaredField("can_end3");
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        assert can_end3 != null;
        if(FabricLoader.getInstance().isModLoaded("adventurez")) {
            return can_end3.getBoolean(player);
        }else {
            return true;
        }
    }

    public static boolean canJoinEnd4(ServerPlayerEntity player) throws IllegalAccessException {
        Field can_end4 = null;
        try {
            can_end4 = ServerPlayerEntity.class.getDeclaredField("can_end4");
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        assert can_end4 != null;
        if(FabricLoader.getInstance().isModLoaded("bosses_of_mass_destruction")) {
            return can_end4.getBoolean(player);
        }else {
            return true;
        }
    }

    public static boolean canJoinEnd5(ServerPlayerEntity player) throws IllegalAccessException {
        Field can_end5 = null;
        try {
            can_end5 = ServerPlayerEntity.class.getDeclaredField("can_end5");
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        assert can_end5 != null;
        if(FabricLoader.getInstance().isModLoaded("bosses_of_mass_destruction")) {
            return can_end5.getBoolean(player);
        }else {
            return true;
        }
    }

    public static boolean canJoinEnd6(ServerPlayerEntity player) throws IllegalAccessException {
        Field can_end6 = null;
        try {
            can_end6 = ServerPlayerEntity.class.getDeclaredField("can_end6");
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        assert can_end6 != null;
        if(FabricLoader.getInstance().isModLoaded("bosses_of_mass_destruction")) {
            return can_end6.getBoolean(player);
        }else {
            return true;
        }
    }

    public static boolean canJoinEnd7(ServerPlayerEntity player) throws IllegalAccessException {
        Field can_end7 = null;
        try {
            can_end7 = ServerPlayerEntity.class.getDeclaredField("can_end7");
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        assert can_end7 != null;
        if(FabricLoader.getInstance().isModLoaded("doom")) {
            return can_end7.getBoolean(player);
        }else {
            return true;
        }
    }

    public static boolean canJoinEnd8(ServerPlayerEntity player) throws IllegalAccessException {
        Field can_end8 = null;
        try {
            can_end8 = ServerPlayerEntity.class.getDeclaredField("can_end8");
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        assert can_end8 != null;
        if(FabricLoader.getInstance().isModLoaded("doom")) {
            return can_end8.getBoolean(player);
        }else {
            return true;
        }
    }

    public static boolean canJoinEnd9(ServerPlayerEntity player) throws IllegalAccessException {
        Field can_end9 = null;
        try {
            can_end9 = ServerPlayerEntity.class.getDeclaredField("can_end9");
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        assert can_end9 != null;
        if(FabricLoader.getInstance().isModLoaded("doom")) {
            return can_end9.getBoolean(player);
        }else {
            return true;
        }
    }

    public static boolean canJoinEnd10(ServerPlayerEntity player) throws IllegalAccessException {
        Field can_end10 = null;
        try {
            can_end10 = ServerPlayerEntity.class.getDeclaredField("can_end10");
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        assert can_end10 != null;
        if(FabricLoader.getInstance().isModLoaded("twilightforest")) {
            return can_end10.getBoolean(player);
        }else {
            return true;
        }
    }

    public static boolean canJoinEnd11(ServerPlayerEntity player) throws IllegalAccessException {
        Field can_end11 = null;
        try {
            can_end11 = ServerPlayerEntity.class.getDeclaredField("can_end11");
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        assert can_end11 != null;
        if(FabricLoader.getInstance().isModLoaded("minecells")) {
            return can_end11.getBoolean(player);
        }else {
            return true;
        }
    }

    public static boolean canJoinEnd12(ServerPlayerEntity player) throws IllegalAccessException {
        Field can_end12 = null;
        try {
            can_end12 = ServerPlayerEntity.class.getDeclaredField("can_end12");
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        assert can_end12 != null;
        if(FabricLoader.getInstance().isModLoaded("doom")) {
            return can_end12.getBoolean(player);
        }else {
            return true;
        }
    }

    public static boolean canJoinEnd13(ServerPlayerEntity player) throws IllegalAccessException {
        Field can_end13 = null;
        try {
            can_end13 = ServerPlayerEntity.class.getDeclaredField("can_end13");
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        assert can_end13 != null;
        if(FabricLoader.getInstance().isModLoaded("botania")) {
            return can_end13.getBoolean(player);
        }else {
            return true;
        }
    }

    public static boolean canJoinEnd14(ServerPlayerEntity player) throws IllegalAccessException {
        Field can_end14 = null;
        try {
            can_end14 = ServerPlayerEntity.class.getDeclaredField("can_end14");
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        assert can_end14 != null;
        if(FabricLoader.getInstance().isModLoaded("soulsweapons")) {
            return can_end14.getBoolean(player);
        }else {
            return true;
        }
    }
    public static boolean canJoinEnd15(ServerPlayerEntity player) throws IllegalAccessException {
        Field can_end15 = null;
        try {
            can_end15 = ServerPlayerEntity.class.getDeclaredField("can_end15");
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        assert can_end15 != null;
        if(FabricLoader.getInstance().isModLoaded("soulsweapons")) {
            return can_end15.getBoolean(player);
        }else {
            return true;
        }
    }
    public static boolean canJoinEnd16(ServerPlayerEntity player) throws IllegalAccessException {
        Field can_end16 = null;
        try {
            can_end16 = ServerPlayerEntity.class.getDeclaredField("can_end16");
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        assert can_end16 != null;
        if(FabricLoader.getInstance().isModLoaded("soulsweapons")) {
            return can_end16.getBoolean(player);
        }else {
            return true;
        }
    }
    public static boolean canJoinEnd17(ServerPlayerEntity player) throws IllegalAccessException {
        Field can_end17 = null;
        try {
            can_end17 = ServerPlayerEntity.class.getDeclaredField("can_end17");
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        assert can_end17 != null;
        if(FabricLoader.getInstance().isModLoaded("soulsweapons")) {
            return can_end17.getBoolean(player);
        }else {
            return true;
        }
    }
    public static boolean canJoinEnd18(ServerPlayerEntity player) throws IllegalAccessException {
        Field can_end18 = null;
        try {
            can_end18 = ServerPlayerEntity.class.getDeclaredField("can_end18");
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        assert can_end18 != null;
        if(FabricLoader.getInstance().isModLoaded("soulsweapons")) {
            return can_end18.getBoolean(player);
        }else {
            return true;
        }
    }

    public static boolean canJoinEnd19(ServerPlayerEntity player) throws IllegalAccessException {
        Field can_end19 = null;
        try {
            can_end19 = ServerPlayerEntity.class.getDeclaredField("can_end19");
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        assert can_end19 != null;
        if(FabricLoader.getInstance().isModLoaded("invade")) {
            return can_end19.getBoolean(player);
        }else {
            return true;
        }
    }
    public static boolean canJoinEnd20(ServerPlayerEntity player) throws IllegalAccessException {
        Field can_end20 = null;
        try {
            can_end20 = ServerPlayerEntity.class.getDeclaredField("can_end20");
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        assert can_end20 != null;
        if(FabricLoader.getInstance().isModLoaded("illagerinvasion")) {
            return can_end20.getBoolean(player);
        }else {
            return true;
        }
    }

    public static boolean canJoinEnd(ServerPlayerEntity player) throws IllegalAccessException {
        return canJoinEnd1(player) && canJoinEnd2(player) && canJoinEnd3(player) && canJoinEnd4(player) && canJoinEnd5(player) && canJoinEnd6(player) && canJoinEnd7(player) && canJoinEnd8(player) && canJoinEnd9(player) && canJoinEnd10(player) && canJoinEnd11(player) && canJoinEnd12(player) && canJoinEnd13(player)&& canJoinEnd14(player) && canJoinEnd15(player) && canJoinEnd16(player) && canJoinEnd17(player) && canJoinEnd18(player)  && canJoinEnd19(player)  && canJoinEnd20(player);
    }

}
