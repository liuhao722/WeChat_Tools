package com.nextjoy.clicker;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hao on 2017/3/30.
 */

public class Config {
    //最终执行循环的次数
    public static final int doReplyCount = 10;
    public static final int doDiscoverCount = 1;
    public static final int doPraiseCount = 20;

    private static final Config ourInstance = new Config();

    public static Config ins() {
        return ourInstance;
    }

    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    private static final String SAFE_CONFIG_NAME = "clicker_config_name";
    public static final String IS_ANTI_VILLAIN = "is_anti_villain";

    private Config() {
        sp = APP.app.getSharedPreferences(SAFE_CONFIG_NAME,
                Context.MODE_PRIVATE);
        edit = sp.edit();
    }

    /**
     * 防坏人设置
     */
    public void setAntiVillain(boolean isAntiVillain) {
        edit.putBoolean(IS_ANTI_VILLAIN, isAntiVillain);
        edit.commit();
    }

    public boolean isAntiVillain() {
        return sp.getBoolean(IS_ANTI_VILLAIN, false);
    }
}
