package com.xiao.searchappmessage.handler;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.xiao.searchappmessage.MainActivity;
import com.xiao.searchappmessage.common.CommonMethod;
import com.xiao.searchappmessage.service.MyService;

import androidx.annotation.NonNull;

public class MyHandler extends Handler {
    public static final int TRANSFORM_TIME_NUM = 1002;
    public int CURRENT_APP_DELAY_TIME = 3000;
    private static int currentNum;
    private static MyHandler myHandler;
    private int delayTime = 60000;
    private String[] packageNames;
    private MyService myService;

    public static synchronized MyHandler getInstance() {
        if (myHandler == null) {
            myHandler = new MyHandler();
        }
        return myHandler;
    }

    private MyHandler() {
        currentNum = 0;
    }

    public void setService(MyService myService) {
        this.myService = myService;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }


    @Override
    public void handleMessage(@NonNull Message msg) {
        if (packageNames != null) {
            switch (msg.what) {
                case TRANSFORM_TIME_NUM:
                    Log.d("test", "Handler TRANSFORM_TIME_NUM ");

                    if (packageNames == null || packageNames.length == 0) {
                        break;
                    }

                    String packageName = packageNames[currentNum];
                    Log.d("test", " packageNames[currentNum]: " + packageName);
                    CommonMethod.openPackage(myService, packageName);


                    currentNum++;
                    if (currentNum == packageNames.length) {
                        currentNum = 0;
                    }

                    if (!packageName.equals(myService.getPackageName())) {
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

    public void setPackageNames(String[] packageNames) {
        this.packageNames = packageNames;
    }

    public String[] getPackageNames() {
        return packageNames;
    }
}
