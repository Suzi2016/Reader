package com.suzi.reader.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.suzi.reader.R;
import com.suzi.reader.horizontal_readmodel.AppUtils;
import com.suzi.reader.utils.SharedPreferenceUtils;
import com.suzi.reader.utils.UIUtils;

public class SplashActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UIUtils.setActivityFullScreen(this);
        setContentView(R.layout.activity_splash);

        AppUtils.init(getApplicationContext());

        // 启动更新小说的服务
//        startService(new Intent(SplashActivity.this, UpdateNovel.class));

        // 判断是否是第一次登录，如果是的就初始化设置参数
        int loginTime = SharedPreferenceUtils.getLoginTime(this);
        if (loginTime == 0)
        {
            SharedPreferenceUtils.initSP(this);
        }

        // 修改登录次数
        SharedPreferenceUtils.setLoginTime(this);

        // 颜值两秒启动MainActivity
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                SystemClock.sleep(1000);
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).start();

    }
}
