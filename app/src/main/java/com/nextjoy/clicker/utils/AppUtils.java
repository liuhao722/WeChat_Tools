package com.nextjoy.clicker.utils;

import com.nextjoy.clicker.APP;

/**
 * Created by hao on 2017/3/28.
 */

public class AppUtils {
    private static final AppUtils ourInstance = new AppUtils();

    public static AppUtils ins() {
        return ourInstance;
    }

    private AppUtils() {
    }

    public static int getScreenW() {
        return APP.app.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenH() {
        return APP.app.getResources().getDisplayMetrics().heightPixels;
    }

    public static float getDensity() {
        return APP.app.getResources().getDisplayMetrics().density;
    }
}
