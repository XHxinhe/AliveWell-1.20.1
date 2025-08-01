package com.XHxinhe.aliveandwell.util.config;

import com.XHxinhe.aliveandwell.AliveAndWellMain;
import com.google.gson.JsonObject;

public class CommonConfig {
    public static boolean areCheatAllowed = true;
    public static boolean b = true;
    public static int deathCount = 50;
    public static boolean c = true;
    public static int tptime = 900;
    public static int xptime = 10;
    public static int netherDay = 64;

    public CommonConfig(){
    }
    public JsonObject serialize(){
        JsonObject root = new JsonObject();
        JsonObject entry = new JsonObject();
        //参数一：条目名称     参数二：值
        entry.addProperty("desc:", "是否开启op作弊模式，默认false，不可配置");
        entry.addProperty("areCheatAllowed", areCheatAllowed);
        entry.addProperty("desc1:", "是否开启模组完整性检查，默认true，不可配置");
        entry.addProperty("areChectMods", b);
        entry.addProperty("desc2:", "是否开启村民交易，默认true,不可配置");
        entry.addProperty("areAllowedTrade", c);
        entry.addProperty("desc3:", "最大死亡次数50 无法配置");
        entry.addProperty("deathCount", deathCount);
        entry.addProperty("desc4:", "传送冷却时间1200，无法配置");
        entry.addProperty("tptime", tptime);
        entry.addProperty("desc5:", "传送每天消耗经验倍数10，无法配置");
        entry.addProperty("xptime", xptime);
        entry.addProperty("desc6:", "地狱开启天数,默认64,不可配置");
        entry.addProperty("netherDay", netherDay);
        root.add("aliveandwell", entry);
        return root;
    }

    //反序列化
    public void deserialize(JsonObject data) {
        if (data == null) {
            AliveAndWellMain.LOGGER.error("Config file was empty!");
        } else {
            try {
                if(deathCount >= 50) {  // 死亡次数不低于50
                    deathCount = 50;
                }
                if(tptime <= 900) {     // 传送冷却不低于900秒
                    tptime = 900;
                }
                if(xptime <= 10) {      // 经验消耗倍数不低于10
                    xptime = 10;
                }
                if(netherDay <= 64) {   // 地狱开启天数不低于64
                    netherDay = 64;
                }

            } catch (Exception var3) {
                AliveAndWellMain.LOGGER.error("Could not parse config file", var3);
            }
        }
    }
}
