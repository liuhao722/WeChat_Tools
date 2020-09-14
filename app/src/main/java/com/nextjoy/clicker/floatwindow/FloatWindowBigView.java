package com.nextjoy.clicker.floatwindow;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.nextjoy.clicker.R;
import com.nextjoy.clicker.utils.CmdUtils;

public class FloatWindowBigView extends LinearLayout {

    /**
     * 记录大悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录大悬浮窗的高度
     */
    public static int viewHeight;

    public FloatWindowBigView(final Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.float_window_big, this);
        View view = findViewById(R.id.big_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        Button close = (Button) findViewById(R.id.close);
        Button back = (Button) findViewById(R.id.btn_back);
        Button start = (Button) findViewById(R.id.btn_start);
        Button stop = (Button) findViewById(R.id.btn_stop);
        Button open = (Button) findViewById(R.id.btn_open);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击关闭悬浮窗的时候，移除所有悬浮窗，并停止Service
                MyWindowManager.removeBigWindow(context);
                MyWindowManager.removeSmallWindow(context);
                Intent intent = new Intent(getContext(), FloatWindowService.class);
                context.stopService(intent);
            }
        });
        back.setOnClickListener(new OnClickListener() {//关闭
            @Override
            public void onClick(View v) {
                disMiss(context, false);
            }
        });
        start.setOnClickListener(new OnClickListener() {//开始运行
            @Override
            public void onClick(View v) {
                disMiss(context, false);
            }
        });
        stop.setOnClickListener(new OnClickListener() {//停止运行
            @Override
            public void onClick(View v) {
                disMiss(context, true);
            }
        });
        open.setOnClickListener(new OnClickListener() {//打开工具
            @Override
            public void onClick(View v) {
                CmdUtils.ins().openMySelf();
                disMiss(context, true);
            }
        });
    }

    private void disMiss(Context context, boolean stop) {
        // 点击返回的时候，移除大悬浮窗，创建小悬浮窗
        MyWindowManager.removeBigWindow(context);
        MyWindowManager.createSmallWindow(context);
        CmdUtils.ins().stopCMD(context, stop);
    }
}
