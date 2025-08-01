package com.XHxinhe.aliveandwell.item.exitem;

/**
 * 经验存储卡 (Copper)
 * 最大存储: 1,000, 每次操作: 10
 */
public class ExItemCopper extends ExperienceStorageItem {
    public ExItemCopper() {
        super(1_000, 10, new Settings());
    }
}