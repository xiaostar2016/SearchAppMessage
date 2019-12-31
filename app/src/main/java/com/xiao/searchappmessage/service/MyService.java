package com.xiao.searchappmessage.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;


import com.xiao.searchappmessage.common.CommonMethod;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyService extends Service {
    public static final String TRANSFORM_MESSAGE = "transform_message";
    public static final String STOP_TRANSFORM_MESSAGE = "stop_transform_message";
    public static final String DELAY_TIME_TRANSFORM_MESSAGE = "delay_time_transform_message";
    public static final int TRANSFORM_TIME_NUM = 1002;
    public int CURRENT_APP_DELAY_TIME = 10000;
    public int delayTime;
    private MyHandler myHandler;
    private String[] packageNames;

    private int currentNum;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myHandler = new MyHandler();
        currentNum = 0;
        delayTime = 60000;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean state = intent.getBooleanExtra(STOP_TRANSFORM_MESSAGE, false);
        if (state) {
            String content = intent.getStringExtra(TRANSFORM_MESSAGE);
            if (content != null && !content.equals("")) {
                packageNames = content.split(",");
                for (int i = 0; i < packageNames.length; i++) {
                    Log.d("test", "packageName: " + packageNames[i]);
                }

                delayTime = intent.getIntExtra(DELAY_TIME_TRANSFORM_MESSAGE, 60) * 1000;
                myHandler.sendEmptyMessage(TRANSFORM_TIME_NUM);
            }
        } else {
            myHandler.removeCallbacksAndMessages(null);
            Log.d("test", "myHandler.removeCallbacksAndMessages(null);");
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (packageNames != null) {
                switch (msg.what) {
                    case TRANSFORM_TIME_NUM:
                        Log.d("test", "Handler TRANSFORM_TIME_NUM ");

                        String packageName = packageNames[currentNum];
                        Log.d("test", " packageNames[currentNum]: " + packageName);

                        CommonMethod.openPackage(MyService.this, packageName);

                        currentNum++;
                        if (currentNum == packageNames.length) {
                            currentNum = 0;
                        }

                        if (!packageName.equals(MyService.this.getPackageName())) {
                            sendEmptyMessageDelayed(TRANSFORM_TIME_NUM, delayTime);
                        } else {
                            sendEmptyMessageDelayed(TRANSFORM_TIME_NUM, CURRENT_APP_DELAY_TIME);
                        }

                        break;
                    default:
                        break;
                }
            }

            super.handleMessage(msg);
        }
    }
}
