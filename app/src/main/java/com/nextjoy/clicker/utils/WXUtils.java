package com.nextjoy.clicker.utils;

import com.nextjoy.clicker.entity.WXPraiseXY;

/**
 * Created by hao on 2017/4/1.
 */

public class WXUtils {
    private static final WXUtils ourInstance = new WXUtils();

    public static WXUtils ins() {
        return ourInstance;
    }

    private WXUtils() {
    }

    /**
     * 解析朋友圈的赞按钮的xy坐标
     *
     * @param bounds
     * @return
     */
    public WXPraiseXY parsePraiseXY(String bounds) {
        WXPraiseXY mWXPraiseXY = new WXPraiseXY();
        int x1 = Integer.parseInt(bounds.substring(1, bounds.indexOf(",")));
        int y1 = Integer.parseInt(bounds.substring(bounds.indexOf(",") + 1, bounds.indexOf("]")));
        int x2 = Integer.parseInt(bounds.substring(bounds.lastIndexOf("[") + 1, bounds.lastIndexOf(",")));
        int y2 = Integer.parseInt(bounds.substring(bounds.lastIndexOf(",") + 1, bounds.length() - 1));
        int x = (x1 + x2) / 2;
        int y = (y1 + y2) / 2;
        mWXPraiseXY.x = x;
        mWXPraiseXY.y = y;
        return mWXPraiseXY;
    }
}
