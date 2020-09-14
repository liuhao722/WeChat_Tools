package com.nextjoy.clicker.utils;

import android.app.ActivityManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;

import com.nextjoy.clicker.APP;
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
 * Created by hao on 2017/3/28.
 */

public class CmdUtilsTemp {
    ClipboardManager copy = (ClipboardManager) APP.app.getSystemService(CLIPBOARD_SERVICE);
    private static final CmdUtilsTemp ourInstance = new CmdUtilsTemp();

    public static CmdUtilsTemp ins() {
        return ourInstance;
    }

    private CmdUtilsTemp() {
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
    public String WX_CLASS = "com.tencent.mm/com.tencent.mm.ui.LauncherUI";
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
            "uiautomator dump " + fileName + "\n"//8--截取屏幕
    };

    public void openMySelf() {
        execute(String.format(cmdStr[5], "com.nextjoy.clicker/com.nextjoy.clicker.activity.MainActivity"));// 打开主Activity
    }

    /**
     * 打开微信并且隐藏至后台
     */
    public void openWX() {
        execute(String.format(cmdStr[0], KeyEvent.KEYCODE_HOME));// 切换至后台
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

    /**
     * 回复微信信息----弹出输入法
     */
    public void autoReply(final Handler handler) {
        if (!stop) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    copy.setText(DataArray.ins().getSendMsg());                      // 设置到粘贴内容里
                    click(TAB1, TAB_Y);                     // 点击微信按钮
                    click(FIRST_MSG_Y, FIRST_MSG_Y);        // 点击进入第一条消息
                    click(ADD_FRIEND_X, ADD_FRIEND_Y);      // 点击接受好友添加的请求
                    click(FIRST_MSG_Y, REPLY_EDIT_Y);       // 点击弹出输入法后的输入框
                    swipe(FIRST_MSG_Y, REPLY_COPY_EDIT_Y, FIRST_MSG_Y, REPLY_COPY_EDIT_Y, 1000);  // 长按输入框出粘贴除提示
                    click(BACK_Y, REPLY_COPY_Y1);           // 点击粘贴键
                    click(SEND_X, REPLY_COPY_EDIT_Y);       // 点击发送按钮
                    click(BACK_X, BACK_Y);                  // 点击返回按钮
                    swipe(FIRST_MSG_Y, FIRST_MSG_Y, FIRST_MSG_Y, FIRST_MSG_Y, 1000);    // 长按出删除提示当前聊天的提示
                    if (!APP.DEBUG) {    // 正式环境打开删除当前第一条信息操作
                        click(FIRST_MSG_Y, DELETE_MSG_Y);   // 已经回复了该条信息，点击删除该条信息
                    }
                    autoReply(handler);
                }
            }, 300);
        }

    }

    public int replyCount = 0;
    public int discoverCount = 0;
    public int praiseCount = 0;
    //最终执行循环的次数
    final int doReplyCount = 5;
    final int doDiscoverCount = 1;
    final int doPraiseCount = 10;

    public void autoDoAllCommend(final Handler handler) {
        if (!stop) {
            if (replyCount < doReplyCount) {               // 执行5次自动回复信息
                copy.setText(DataArray.ins().getSendMsg());                      // 设置到粘贴内容里
                click(TAB1, TAB_Y);                     // 点击微信按钮
                click(FIRST_MSG_Y, FIRST_MSG_Y);        // 点击进入第一条消息
                click(ADD_FRIEND_X, ADD_FRIEND_Y);      // 点击接受好友添加的请求
                click(FIRST_MSG_Y, REPLY_EDIT_Y);       // 点击弹出输入法后的输入框
                swipe(FIRST_MSG_Y, REPLY_COPY_EDIT_Y, FIRST_MSG_Y, REPLY_COPY_EDIT_Y, 1000);  // 长按输入框出粘贴除提示
                click(BACK_Y, REPLY_COPY_Y1);           // 点击粘贴键
                click(SEND_X, REPLY_COPY_EDIT_Y);       // 点击发送按钮
                click(BACK_X, BACK_Y);                  // 点击返回按钮
                swipe(FIRST_MSG_Y, FIRST_MSG_Y, FIRST_MSG_Y, FIRST_MSG_Y, 1000);    // 长按出删除提示当前聊天的提示
                if (!APP.DEBUG) {    // 正式环境打开删除当前第一条信息操作
                    click(FIRST_MSG_Y, DELETE_MSG_Y);   // 已经回复了该条信息，点击删除该条信息
                }
                replyCount++;
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                autoDoAllCommend(handler);
            } else if (discoverCount < doDiscoverCount) {     // 执行一次附近的人
                if (APP.DEBUG) {
                    click(TAB3, TAB_Y);                         // 多点击发现按钮一次，清除长按弹窗
                }
                click(TAB3, TAB_Y);                         // 点击发现按钮
                click(SCREEN_X_CENTER, DISCOVER_Y);         // 点击附近的人
                try {
                    Thread.sleep(12000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                discoverCount++;
                click(BACK_X, BACK_Y);                      // 点击返回按钮
                autoDoAllCommend(handler);
            } else if (praiseCount < doPraiseCount) {       // 执行5次朋友圈点赞
                if (praiseCount == 0 && count == 0) {
                    click(TAB3, TAB_Y);                         // 点击发现按钮
                    click(SCREEN_X_CENTER, FRIEND_Y);           // 点击朋友圈
                }
                if (count < 2) {
                    count++;
                    pageDown();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    autoDoAllCommend(handler);
                } else {
                    if (swipeCount < 2) {//滑动三次 执行一次点赞逻辑
                        swipe(SCREEN_X_CENTER, PRAISE_FROM_Y, SCREEN_X_CENTER, PRAISE_TO_Y, 100);  // 向下滑动
                        swipeCount++;
                        autoDoAllCommend(handler);
                    } else {
                        click(PRAISE_X, PRAISE_Y);                      // 点击赞菜单按钮
                        if (!APP.DEBUG) {
                            click(SCREEN_X_CENTER, PRAISE_Y);               // 点击赞按钮
                        }
                        swipeCount = 0;
                        praiseCount++;
                        if (praiseCount == doPraiseCount) {
                            try {
                                Thread.sleep(30 * 1000);// 间隔
                                click(BACK_X, BACK_Y);                      // 点击返回按钮
                                clearCount();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        autoDoAllCommend(handler);
                    }
                }
            }
        }
    }

    public void clearCount() {
        replyCount = 0;
        discoverCount = 0;
        praiseCount = 0;
        swipeCount = 0;
        count = 0;
    }

    /**
     * 刷新附近的人
     *
     * @param handler
     */
    public void autoDiscover(Handler handler) {
        click(TAB3, TAB_Y);                         // 点击发现按钮
        click(SCREEN_X_CENTER, DISCOVER_Y);         // 点击附近的人
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                click(BACK_X, BACK_Y);                      // 点击返回按钮
            }
        }, 10000);
    }

    /**
     * 自动点赞朋友圈
     *
     * @param handler
     */
    public void autoPraise(final Handler handler) {
        click(TAB3, TAB_Y);                         // 点击发现按钮
        click(SCREEN_X_CENTER, FRIEND_Y);           // 点击朋友圈
        pageDown(handler);
    }

    public boolean stop = false;

    public void stopCMD(Context context, boolean stop) {
//        this.stop = stop;
        if (stop) {
//            Intent i = new Intent(context, MainActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            PendingIntent pi = PendingIntent.getBroadcast(context, 123, i, PendingIntent.FLAG_UPDATE_CURRENT);
//            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//            manager.set(AlarmManager.RTC_WAKEUP, 2000, pi);

//            Intent intent = new Intent(context, FloatWindowService.class);
//            context.stopService(intent);
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            manager.killBackgroundProcesses("com.nextjoy.clicker");
        }
    }

    public int count = 0;
    public int swipeCount = 0;

    public void pageDown(final Handler handler) {
        if (!stop) {
            if (count < 3) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        count++;
                        pageDown();
                        pageDown(handler);
                    }
                }, 300);
            } else {
                if (swipeCount < 1) {//滑动三次 执行一次点赞逻辑
                    swipe(SCREEN_X_CENTER, PRAISE_FROM_Y, SCREEN_X_CENTER, PRAISE_TO_Y, 100);  // 滑动弹出删除按钮
                    swipeCount++;
                    pageDown(handler);
                } else {
                    click(PRAISE_X, PRAISE_Y);                      // 点击赞菜单按钮
                    if (!APP.DEBUG) {
                        click(SCREEN_X_CENTER, PRAISE_Y);               // 点击赞按钮
                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeCount = 0;
                            pageDown(handler);
                        }
                    }, 500);
                }
            }
        }
    }

    /**
     * 翻页 自动翻页
     */
    public void pageDown() {
        execute(String.format(cmdStr[0], 93));
    }

    public void sleep(int time) {
        execute(String.format(cmdStr[4], time));
    }

    public void dump() {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                click(TAB3, TAB_Y);                         // 点击发现按钮
                click(SCREEN_X_CENTER, FRIEND_Y);           // 点击朋友圈
                swipe(SCREEN_X_CENTER, 1300, SCREEN_X_CENTER, 815, 100);  // 滑动弹出删除按钮
                execute(cmdStr[8]);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                File file = new File(fileName);
                InputStream is = null;
                try {
                    is = new FileInputStream(file);
                    List<WXPraiseXY> data = XMLParse.ins().parse(is);
                    if (data != null && data.size() > 0) {
                        for (int i = 0; i < data.size(); i++) {
                            Log.e("info", data.get(i).toString());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 2000);
    }
}
