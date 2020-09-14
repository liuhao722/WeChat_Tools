package com.nextjoy.clicker.utils;

import android.app.ActivityManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.nextjoy.clicker.APP;
import com.nextjoy.clicker.Config;
import com.nextjoy.clicker.entity.WXPraiseXY;
import com.nextjoy.clicker.xml.XMLParse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * 对应的钉钉打卡
 * Created by hao on 2017/3/28.
 */

public class CmdUtilsDingDing {
    ClipboardManager copy = (ClipboardManager) APP.app.getSystemService(CLIPBOARD_SERVICE);
    private static final CmdUtilsDingDing ourInstance = new CmdUtilsDingDing();

    public static CmdUtilsDingDing ins() {
        return ourInstance;
    }

    private CmdUtilsDingDing() {
    }

    private OutputStream os;
    Process process;

    /**
     * <dd>方法作用： 执行adb命令
     * <dd>注意事项： 注意，Runtime.getRuntime().exec("su").getOutputStream();网上前辈的经验说这句话貌似很耗时，所以不要每次都执行这句代码
     *
     * @param cmd 具体命令语句
     */
    public void execute(String cmd) {
        try {
            if (os == null) {
                process = Runtime.getRuntime().exec("su");
                os = process.getOutputStream();
            }
            os.write(cmd.getBytes());
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String fileName = "/mnt/sdcard/window_dump.xml";
    public String WX_CLASS = "com.alibaba.android.rimet/com.alibaba.android.rimet.biz.SplashActivity";
    /**
     * 需要的命令集合
     */
    public String[] cmdStr = {
            "input keyevent %d\n",                      // 0---点击
            "input keyevent --longpress %d\n",          // 1---长按 +KEYCODE
            "input text %s\n",                          // 2---输入对应的text
            "input tap %d %d\n",                        // 3---指定的坐标 x-y
            "sleep %d\n",                               // 4---休眠时间
            "am start -n %s\n",                         // 5---打开指定的activity【全路径】
            "am start -n " + WX_CLASS + "\n",           // 6---打开微信
            "input swipe %d %d %d %d %d\n",             // 7---滑动 点击 或者长按都可以使用该命令 五个参数从x1 y1 到x2 y2 持续多少毫秒
            "uiautomator dump " + fileName + "\n"       // 8---截取屏幕
    };

    public void openMySelf() {
        execute(String.format(cmdStr[5], "com.nextjoy.clicker/com.nextjoy.clicker.activity.MainActivity"));// 打开主Activity
    }

    /**
     * 打开微信并且隐藏至后台
     */
    public void openDD() {
//        execute(String.format(cmdStr[0], KeyEvent.KEYCODE_HOME));// 切换至后台
        execute(cmdStr[6]);// 打开微信客户端
    }

    /**
     * 滑动 点击 或者长按都可以使用该命令 五个参数从x1 y1 到x2 y2 持续多少毫秒
     *
     * @param fromX 起点X位置
     * @param fromY 起点Y位置
     * @param toX   滑动到X的位置
     * @param toY   滑动到Y的位置
     * @param time  长按时长
     */
    public void swipe(int fromX, int fromY, int toX, int toY, int time) {
        execute(String.format(cmdStr[7], fromX, fromY, toX, toY, time));
    }

    /**
     * 点击指定位置
     *
     * @param positionX 点击的位置_X
     * @param positionY 点击的位置_Y
     */
    public void click(int positionX, int positionY) {
        execute(String.format(cmdStr[3], positionX, positionY));//单纯点击
    }

    /**
     * 所有的点击像素都要加上对应的topbar 20dp
     */
    //返回按钮的位置xy坐标
    private final int BACK_X = (int) (AppUtils.getDensity() * 30);
    private final int BACK_Y = (int) (AppUtils.getDensity() * 50);
    //微信第一条消息的位置 xy坐标
    private final int FIRST_MSG_Y = (int) (AppUtils.getDensity() * 96);
    //消息回复框位置
    private final int REPLY_EDIT_Y = (int) (AppUtils.getScreenH() - AppUtils.getDensity() * 24);
    //消息回复框粘贴位置
    private final int REPLY_COPY_Y = (int) (AppUtils.getScreenH() - AppUtils.getDensity() * 64);
    //键盘弹起后 粘贴的位置
    private final int REPLY_COPY_Y1 = (int) (AppUtils.getScreenH() * 0.458f);
    //键盘弹起后 输入框的位置Y
    private final int REPLY_COPY_EDIT_Y = (int) (AppUtils.getScreenH() * 0.536458f);

    //消息删除的xy坐标
    private final int DELETE_MSG_Y = (int) (AppUtils.getDensity() * 357);
    //发送按钮位置的xy
    private final int SEND_X = (int) (AppUtils.getScreenW() - 15 * AppUtils.getDensity());
    private final int SEND_Y = (int) (AppUtils.getScreenH() - 15 * AppUtils.getDensity());
    //接受按钮的位置X
    private final int ADD_FRIEND_X = (int) (AppUtils.getScreenW() * 0.83334f);
    private final int ADD_FRIEND_Y = (int) (AppUtils.getScreenH() * 0.15625f);
    //发现按钮的位置xy
    private final int TAB1 = (int) (AppUtils.getScreenW() * 0.129f);
    private final int TAB2 = (int) (AppUtils.getScreenW() * 0.37f);
    private final int TAB3 = (int) (AppUtils.getScreenW() * 0.62f);
    private final int TAB4 = (int) (AppUtils.getScreenW() * 0.87f);
    private final int TAB_Y = (int) (AppUtils.getScreenH() - 30 * AppUtils.getDensity());
    //附近的人的y坐标
    private final int DISCOVER_Y = (int) (AppUtils.getScreenH() * 0.469f);
    //朋友圈的y坐标
    private final int FRIEND_Y = (int) (AppUtils.getScreenH() * 0.172f);
    //屏幕一半的位置x
    private final int SCREEN_X_CENTER = (int) (AppUtils.getScreenW() / 2);
    //朋友圈点赞按钮的xy坐标
    private final int PRAISE_X = (int) (AppUtils.getScreenW() * 0.935185f);
    private final int PRAISE_Y = (int) (AppUtils.getScreenH() * 0.880208f);
    //滑动Y点
    private final int PRAISE_FROM_Y = (int) (AppUtils.getScreenH() * 0.9f);
    private final int PRAISE_TO_Y = (int) (AppUtils.getScreenH() * 0.2f);

    /**
     * 回复微信信息----不弹输入法
     *
     * @param handler
     * @param msg     回复的内容
     */
    public void autoReply(Handler handler, String msg) {
        copy.setText(msg);                      // 设置到粘贴内容里
        click(TAB1, TAB_Y);                     // 点击微信按钮
        click(FIRST_MSG_Y, FIRST_MSG_Y);        // 点击进入第一条消息
        click(ADD_FRIEND_X, ADD_FRIEND_Y);      // 点击接受好友添加的请求
        swipe(FIRST_MSG_Y, REPLY_EDIT_Y, FIRST_MSG_Y, REPLY_EDIT_Y, 1000);  // 长按输入框出粘贴除提示
        click(BACK_Y, REPLY_COPY_Y);            // 点击粘贴键
        click(SEND_X, SEND_Y);                  // 点击发送按钮
        click(BACK_X, BACK_Y);                  // 点击返回按钮
        swipe(FIRST_MSG_Y, FIRST_MSG_Y, FIRST_MSG_Y, FIRST_MSG_Y, 1000);    // 长按出删除提示当前聊天的提示
        if (!APP.DEBUG) {    // 正式环境打开删除当前第一条信息操作
            click(FIRST_MSG_Y, DELETE_MSG_Y);   // 已经回复了该条信息，点击删除该条信息
        }
    }
}
