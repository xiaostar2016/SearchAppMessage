package com.xiao.searchappmessage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.xiao.searchappmessage.adapter.MyAdapter;
import com.xiao.searchappmessage.bean.AppMessage;
import com.xiao.searchappmessage.common.CommonMethod;
import com.xiao.searchappmessage.handler.MyHandler;
import com.xiao.searchappmessage.service.MyService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_time;
    private ListView list_app_message;
    private MyAdapter myAdapter;
    private ArrayList<AppMessage> appMessages = new ArrayList<>();
    StringBuilder isCheckPackageNameContent = new StringBuilder();
    private Button btn_start;
    private LinearLayout ll_display;
    private Button btn_running_state;

    private static boolean isRunning = false;
    private String TRANSFORM_MESSAGE = "transform_message";
    private String TRANSFORM_MESSAGE_STATE = "transform_message_state";
    private static String transformMessage = "";
    private MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (null != savedInstanceState) {
            transformMessage = savedInstanceState.getString(TRANSFORM_MESSAGE);
            isRunning = savedInstanceState.getBoolean(TRANSFORM_MESSAGE_STATE);
            Log.e("test", "onCreate transformMessage = " + transformMessage);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myHandler = MyHandler.getInstance();

        initView();
        initData();
    }

    private void initView() {
        edit_time = findViewById(R.id.edit_time);
        list_app_message = findViewById(R.id.list_app_message);
        btn_start = findViewById(R.id.btn_start);
        btn_running_state = findViewById(R.id.btn_running_state);
        ll_display = findViewById(R.id.ll_display);
        ll_display.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        btn_start.setOnClickListener(this);
        btn_running_state.setOnClickListener(this);
    }

    private void initData() {
        String[] transforms = null;
        if (transformMessage != null) {
            transforms = transformMessage.split(",");
        }

        if (transforms == null) {
            transforms = myHandler.getPackageNames();
        }


        PackageManager pManager = this.getPackageManager();

        List<PackageInfo> allApps = CommonMethod.getAllApps(this);
        for (int i = 0; i < allApps.size(); i++) {
            PackageInfo packageInfo = allApps.get(i);

            String packageName = packageInfo.applicationInfo.packageName;
            Drawable iconDrawable = pManager.getApplicationIcon(packageInfo.applicationInfo);
            String appName = pManager.getApplicationLabel(packageInfo.applicationInfo).toString();

            AppMessage appMessage = new AppMessage();
            appMessage.setAppName(appName);
            appMessage.setPackageName(packageName);
            appMessage.setIconDrawable(iconDrawable);

            if (transforms != null) {
                for (int j = 0; j < transforms.length; j++) {
                    if (packageName.equals(transforms[j])) {
                        appMessage.setCheck(true);
                    }
                }
            }

            appMessages.add(appMessage);
        }

        myAdapter = new MyAdapter(this, appMessages);
        list_app_message.setAdapter(myAdapter);

        if (isRunning) {
            ll_display.setVisibility(View.VISIBLE);
        } else {
            ll_display.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                runningAppSwitch(true);
                break;
            case R.id.btn_running_state:
                runningAppSwitch(false);
                break;
            default:
                break;
        }
    }

    private void runningAppSwitch(boolean state) {
        if (isCheckPackageNameContent == null || isCheckPackageNameContent.toString().equals("")) {
            Toast.makeText(this, "未选择应用", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MainActivity.this, MyService.class);
        if (state) {
            ll_display.setVisibility(View.VISIBLE);
            intent.putExtra(MyService.STOP_TRANSFORM_MESSAGE, true);
            intent.putExtra(MyService.TRANSFORM_MESSAGE, isCheckPackageNameContent.toString());

            String delayTime = edit_time.getText().toString();
            intent.putExtra(MyService.DELAY_TIME_TRANSFORM_MESSAGE, Integer.valueOf(delayTime.equals("") ? "60" : delayTime));
        } else {
            ll_display.setVisibility(View.GONE);
            intent.putExtra(MyService.STOP_TRANSFORM_MESSAGE, false);
        }
        startService(intent);
        isRunning = state;
    }

    public void update() {
        appMessages = myAdapter.getAppMessages();
        isCheckPackageNameContent = new StringBuilder();
        for (int i = 0; i < appMessages.size(); i++) {
            AppMessage appMessage = appMessages.get(i);
            if (appMessage.isCheck()) {
                isCheckPackageNameContent.append(appMessage.getPackageName()).append(",");
            }
        }

        Log.d("test", isCheckPackageNameContent.toString());
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save away the original text, so we still have it if the activity
        // needs to be killed while paused.
        savedInstanceState.putString(TRANSFORM_MESSAGE, isCheckPackageNameContent.toString());
        savedInstanceState.putBoolean(TRANSFORM_MESSAGE_STATE, isRunning);
        super.onSaveInstanceState(savedInstanceState);
        Log.e("test", "onSaveInstanceState");

    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        transformMessage = savedInstanceState.getString(TRANSFORM_MESSAGE);
        isRunning = savedInstanceState.getBoolean(TRANSFORM_MESSAGE_STATE);
        Log.e("test", "onRestoreInstanceState: " + " transformMessage = " + transformMessage);

    }

}
