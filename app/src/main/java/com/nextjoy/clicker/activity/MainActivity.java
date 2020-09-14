package com.nextjoy.clicker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nextjoy.clicker.Config;
import com.nextjoy.clicker.R;
import com.nextjoy.clicker.base.BaseActivity;
import com.nextjoy.clicker.floatwindow.FloatWindowService;
import com.nextjoy.clicker.utils.CmdUtils;
import com.nextjoy.clicker.utils.CmdUtilsDingDing;
import com.nextjoy.clicker.utils.DataArray;
import com.nextjoy.clicker.utils.DevicesUtils;
import com.nextjoy.clicker.utils.DingDingUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hao on 2017/3/28.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.btn1)
    Button btn1;
    @Bind(R.id.btn2)
    Button btn2;
    @Bind(R.id.btn3)
    Button btn3;
    @Bind(R.id.btn4)
    Button btn4;
    @Bind(R.id.btn5)
    Button btn5;
    @Bind(R.id.btn6)
    Button btn6;
    @Bind(R.id.btn7)
    Button btn7;
    @Bind(R.id.show_msg)
    TextView show_msg;

    @Override
    protected int setLayoutId() {
        DevicesUtils.checkRegister(MainActivity.this);
        startService(new Intent(MainActivity.this, FloatWindowService.class));
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        StringBuilder sb = new StringBuilder();
        sb.append("自动回复次数:" + Config.doReplyCount + "次\n");
        sb.append("自动刷附近的人次数:" + Config.doDiscoverCount + "次\n");
        sb.append("自动朋友圈点赞次数:" + Config.doPraiseCount + "次\n");
        show_msg.setText(sb.toString());
    }

    private static Handler handler = new Handler(Looper.getMainLooper());

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                eventAction(ACTION_BTN1, MainActivity.this);
                break;
            case R.id.btn2:
                eventAction(ACTION_BTN2, MainActivity.this);
                break;
            case R.id.btn3:
                eventAction(ACTION_BTN3, MainActivity.this);
                break;
            case R.id.btn4:
                eventAction(ACTION_BTN4, MainActivity.this);
                break;
            case R.id.btn5:
                eventAction(ACTION_BTN5, MainActivity.this);
                break;
            case R.id.btn6:
                eventAction(ACTION_BTN6, MainActivity.this);
                break;
            case R.id.btn7:
                eventAction(ACTION_BTN7, MainActivity.this);
                break;
        }
    }

    public static final int ACTION_BTN1 = 1;
    public static final int ACTION_BTN2 = 2;
    public static final int ACTION_BTN3 = 3;
    public static final int ACTION_BTN4 = 4;
    public static final int ACTION_BTN5 = 5;
    public static final int ACTION_BTN6 = 6;
    public static final int ACTION_BTN7 = 7;

    public static void eventAction(int action, Context context) {
        switch (action) {
            case ACTION_BTN1:
                CmdUtils.ins().openWX();
                CmdUtils.ins().autoReply(handler, DataArray.ins().getSendMsg());
                break;
            case ACTION_BTN2:
                CmdUtils.ins().openWX();
                CmdUtils.ins().autoReply(handler);
                break;
            case ACTION_BTN3:
                CmdUtils.ins().openWX();
                CmdUtils.ins().autoDiscover(handler);
                break;
            case ACTION_BTN4:
                CmdUtils.ins().openWX();
                CmdUtils.ins().autoPraise(handler);
                break;
            case ACTION_BTN5:
                CmdUtils.ins().openWX();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        CmdUtils.ins().autoDoAllCommend(handler);
                    }
                }.start();
                break;
            case ACTION_BTN6:
                CmdUtils.ins().openWX();
                CmdUtils.ins().dump();
                break;
            case ACTION_BTN7:
                CmdUtilsDingDing.ins().openDD();
                DingDingUtils.getWifiName();
                break;
        }
    }
}
