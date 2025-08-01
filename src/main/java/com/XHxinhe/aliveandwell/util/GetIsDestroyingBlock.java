package com.XHxinhe.aliveandwell.util;

import net.minecraft.entity.mob.ZombieEntity;

import java.lang.reflect.Field;

public class GetIsDestroyingBlock {
    public GetIsDestroyingBlock() {
    }

    public static boolean getIsDestroyingBlock(ZombieEntity zombieEntity) throws IllegalAccessException {
        Field is_destroying_block = null;

        try {
            is_destroying_block = ZombieEntity.class.getDeclaredField("is_destroying_block");
            is_destroying_block.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return is_destroying_block.getBoolean(zombieEntity);
    }
}
