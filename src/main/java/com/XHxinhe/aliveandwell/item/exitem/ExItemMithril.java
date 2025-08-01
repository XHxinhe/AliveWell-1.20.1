package com.XHxinhe.aliveandwell.item.exitem;

/**
 * 经验存储卡 (Mithril)
 * 最大存储: 100,000, 每次操作: 1,000
 */
public class ExItemMithril extends ExperienceStorageItem {
    public ExItemMithril() {
        super(100_000, 1_000, new Settings().fireproof());
    }
}