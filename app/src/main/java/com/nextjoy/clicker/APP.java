package com.nextjoy.clicker;

import android.app.Application;

import com.tencent.bugly.Bugly;

/**
 * Created by hao on 2017/3/28.
 */

public class APP extends Application {
    public static APP app;
    public static final boolean DEBUG = false;
    final String KEY = "e90782a2ef";

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Bugly.init(getApplicationContext(), KEY, DEBUG);
    }
}
