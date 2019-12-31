package com.xiao.searchappmessage.bean;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class AppMessage {
    private String appName;
    private String packageName;
    private Drawable iconDrawable;
    private boolean isCheck;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public void setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }


    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public String toString() {
        return "AppMessage{" +
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", iconDrawable=" + iconDrawable +
                ", isCheck=" + isCheck +
                '}';
    }

}
