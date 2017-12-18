package com.suzi.reader.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.suzi.reader.R;
import com.suzi.reader.activitys.MainActivity;

/**
 * Created by Suzi on 2016/10/23.
 */

public class UIUtils
{
    /**
     * 设置Activity全屏,调用的时候传入this即可
     *
     * @param activity
     */
    public static void setActivityFullScreen(Activity activity)
    {
        // 去掉标题栏
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 去掉信息栏
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 去掉标题栏
     *
     * @param activity
     */
    public static void setActivityNoTitle(Activity activity)
    {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }


    /**
     * 监听EditText输入状态，根据是否有输入内容显示ImageButton（清空按钮）
     *
     * @param editText
     * @param imageButton
     */
    public static void setTextChangedListener(final EditText editText,
                                              final ImageButton imageButton)
    {
        editText.addTextChangedListener(new TextWatcher()
        {
            // 输入中
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3)
            {
                UIUtils.setImageButtonVisiable(editText, imageButton);
            }

            // 输入前
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3)
            {
                UIUtils.setImageButtonVisiable(editText, imageButton);
            }

            // 输入后
            @Override
            public void afterTextChanged(Editable arg0)
            {
                UIUtils.setImageButtonVisiable(editText, imageButton);
            }
        });
    }


    /**
     * 根据EditText是否有数据判断ImageButton是否可见，有数据则可见，没有则不可见
     *
     * @param editText
     * @param imageButton
     */
    public static void setImageButtonVisiable(final EditText editText,
                                              final ImageButton imageButton)
    {
        // 判断editText是否为空
        if (TextUtils.isEmpty(String.valueOf(editText.getText()).trim()))
        {
            // 如果搜索框内没有内容,imageButton设置不可见
            imageButton.setVisibility(Button.GONE);
        } else
        {
            // 如果搜索框内有内容,imageButton设置可见
            imageButton.setVisibility(Button.VISIBLE);
        }
    }


    /**
     *
     * @param context 上下文
     * @param msg 要显示的消息
     */
    public static void showNotification(Context context,int id,String msg)
    {
        Intent notificationIntent =new Intent(context, MainActivity.class); // 点击该通知后要跳转的Activity
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(context)
                .setAutoCancel(true)
                .setContentTitle("落落")
                .setContentText(msg)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .build();

        //FLAG_AUTO_CANCEL   该通知能被状态栏的清除按钮给清除掉
        //FLAG_NO_CLEAR      该通知不能被状态栏的清除按钮给清除掉
        //FLAG_ONGOING_EVENT 通知放置在正在运行
        //FLAG_INSISTENT     是否一直进行，比如音乐一直播放，知道用户响应
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        //DEFAULT_ALL     使用所有默认值，比如声音，震动，闪屏等等
        //DEFAULT_LIGHTS  使用默认闪光提示
        //DEFAULT_SOUND  使用默认提示声音
        //DEFAULT_VIBRATE 使用默认手机震动，需加上<uses-permission android:name="android.permission.VIBRATE" />权限
        notification.defaults |= Notification.DEFAULT_LIGHTS ;
        notification.defaults |= Notification.DEFAULT_SOUND ;
        //叠加效果常量
        //notification.defaults=Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND;
        notification.ledARGB = Color.GREEN;
        notification.ledOnMS =30 * 1000; //闪光时间，毫秒

        // 创建一个NotificationManager的引用
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

        notificationManager.notify(id,notification);



    }




}















