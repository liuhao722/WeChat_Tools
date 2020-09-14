package com.nextjoy.clicker.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import com.nextjoy.clicker.Config;
import com.nextjoy.clicker.activity.MainActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DevicesUtils {
    private static final String URL = "https://leancloud.cn:443/1.1/classes/devices/58dcc50f44d904006dfd783a";
    private static final String APP_ID = "onXRojJheCNkP2qg3Sd6fE0B-gzGzoHsz";
    private static final String APP_KEY = "SUB7qOw1qQvWdlPrHH29xwry";

    private static OkHttpClient okHttpClient;

    public static void checkRegister(final Context context) {
        if (context instanceof MainActivity) {
            isConnection(context);
            if (okHttpClient == null) {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                okHttpClient = builder.build();
            }
            Request.Builder resBuilder = new Request.Builder();
            resBuilder.addHeader("X-LC-Id", APP_ID);
            resBuilder.addHeader("X-LC-Key", APP_KEY);
            resBuilder.url(URL);
            okHttpClient.newCall(resBuilder.build()).enqueue(new Callback() {

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject data = new JSONObject(response.body().string());
                                boolean isAntiVillain = data.optBoolean("to_death");
                                Config.ins().setAntiVillain(isAntiVillain);
                                if (isAntiVillain) {
                                    Process.killProcess(Process.myPid());
                                    return;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * 获取网络状态
     */
    public static boolean isConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isConnection = false;
        if (networkInfo != null && networkInfo.isAvailable()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                isConnection = true;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                isConnection = true;
            } else if (type.equalsIgnoreCase("ETHERNET")) {
                isConnection = true;
            }
        }
        if (!isConnection && context instanceof MainActivity) {
            if (Config.ins().isAntiVillain()) {
                Process.killProcess(Process.myPid());
            }
        }
        return isConnection;
    }
}
