package com.XHxinhe.aliveandwell.item.exitem;

/**
 * 经验存储卡 (Gold)
 * 最大存储: 10,000, 每次操作: 100
 */
public class ExItemGold extends ExperienceStorageItem {
    public ExItemGold() {
        super(10_000, 100, new Settings());
    }
}