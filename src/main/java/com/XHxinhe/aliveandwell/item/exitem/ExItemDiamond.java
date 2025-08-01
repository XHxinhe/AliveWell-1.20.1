package com.XHxinhe.aliveandwell.item.exitem;

/**
 * 经验存储卡 (Diamond)
 * 最大存储: 50,000, 每次操作: 500
 */
public class ExItemDiamond extends ExperienceStorageItem {
    public ExItemDiamond() {
        super(50_000, 500, new Settings().fireproof());
    }
}