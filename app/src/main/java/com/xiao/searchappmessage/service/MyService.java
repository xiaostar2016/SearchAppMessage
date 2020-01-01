package com.xiao.searchappmessage.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;


import com.xiao.searchappmessage.common.CommonMethod;
import com.xiao.searchappmessage.handler.MyHandler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyService extends Service {
    public static final String TRANSFORM_MESSAGE = "transform_message";
    public static final String STOP_TRANSFORM_MESSAGE = "stop_transform_message";
    public static final String DELAY_TIME_TRANSFORM_MESSAGE = "delay_time_transform_message";

    private MyHandler myHandler;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myHandler = MyHandler.getInstance();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean state = intent.getBooleanExtra(STOP_TRANSFORM_MESSAGE, false);
        if (state) {
            String content = intent.getStringExtra(TRANSFORM_MESSAGE);
            if (content != null && !content.equals("")) {
                String[] packageNames = content.split(",");
                for (int i = 0; i < packageNames.length; i++) {
                    Log.d("test", "packageName: " + packageNames[i]);
                }
                myHandler.setPackageNames(packageNames);
                int delayTime = intent.getIntExtra(DELAY_TIME_TRANSFORM_MESSAGE, 60) * 1000;
                myHandler.setDelayTime(delayTime);
                myHandler.sendEmptyMessage(MyHandler.TRANSFORM_TIME_NUM);
            }
        } else {
            myHandler.removeCallbacksAndMessages(null);
            Log.d("test", "myHandler.removeCallbacksAndMessages(null);");
        }

        return super.onStartCommand(intent, flags, startId);
    }

}
