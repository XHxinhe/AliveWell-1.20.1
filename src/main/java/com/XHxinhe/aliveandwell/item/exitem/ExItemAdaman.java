package com.XHxinhe.aliveandwell.item.exitem;

/**
 * 经验存储卡 (Adaman)
 * 最大存储: 200,000, 每次操作: 5,000
 */
public class ExItemAdaman extends ExperienceStorageItem {
    public ExItemAdaman() {
        super(200_000, 5_000, new Settings().fireproof());
    }
}