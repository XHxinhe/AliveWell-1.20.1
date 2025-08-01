package com.XHxinhe.aliveandwell.util;

public interface VillagerNbt {
    boolean villager$contains(String key);

    boolean villager$getBoolean(String key);

    void villager$putBoolean();

    int villager$getInt(String key);

    void villager$putInt();
    void villager$removeString(String key);
}
