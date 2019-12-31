package com.xiao.searchappmessage.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiao.searchappmessage.MainActivity;
import com.xiao.searchappmessage.R;
import com.xiao.searchappmessage.bean.AppMessage;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    private ArrayList<AppMessage> appMessages = new ArrayList<>();
    private MainActivity mainActivity;

    public MyAdapter(MainActivity mainActivity, ArrayList<AppMessage> appMessages) {
        this.mainActivity = mainActivity;
        this.appMessages = appMessages;
    }

    @Override
    public int getCount() {
        return appMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return appMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AppMessage appMessage = (AppMessage) getItem(position);

        final ViewHolder viewHolder;
        // 当view为空时才加载布局，否则，直接修改内容
        if (convertView == null) {
            // 通过inflate的方法加载布局，context需要在使用这个Adapter的Activity中传入。
            convertView = View.inflate(mainActivity, R.layout.item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder); // 用setTag方法将处理好的viewHolder放入view中
        } else { // 否则，让convertView等于view，然后从中取出ViewHolder即可
            viewHolder = (ViewHolder) convertView.getTag();
        }


        //为Item 里面的组件设置相应的数据
        viewHolder.img_icon.setImageDrawable(appMessage.getIconDrawable());
        viewHolder.tv_app_name.setText(appMessage.getAppName());
        viewHolder.checkBox.setChecked(appMessage.isCheck());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.checkBox.setChecked(!appMessage.isCheck());
                appMessage.setCheck(viewHolder.checkBox.isChecked());
                mainActivity.update();
            }
        });

        //返回含有数据的view
        return convertView;
    }

    private class ViewHolder {
        public ImageView img_icon;
        public TextView tv_app_name;
        public CheckBox checkBox;

        public ViewHolder(View convertView) {
            checkBox = convertView.findViewById(R.id.checkbox);
            img_icon = convertView.findViewById(R.id.img_icon);
            tv_app_name = convertView.findViewById(R.id.tv_app_name);
        }
    }

    public ArrayList<AppMessage> getAppMessages() {
        return appMessages;
    }
}
