package com.suzi.reader.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.suzi.reader.R;
import com.suzi.reader.utils.UIUtils;

public class WelcomeActivity extends Activity implements View.OnClickListener
{

    // 布局控件相关
    private Button bt_welcome_login;
    private Button bt_welcome_register;
    private Button bt_welcome_visitor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UIUtils.setActivityNoTitle(this);
        setContentView(R.layout.activity_welcome);

        bindView();

    }

    private void bindView()
    {
        bt_welcome_login = (Button) findViewById(R.id.bt_welcome_login);
        bt_welcome_register = (Button) findViewById(R.id.bt_welcome_register);
        bt_welcome_visitor = (Button) findViewById(R.id.bt_welcome_visitor);

        bt_welcome_login.setOnClickListener(this);
        bt_welcome_register.setOnClickListener(this);
        bt_welcome_visitor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            // 跳转到登录
            case R.id.bt_welcome_login:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                break;
            // 跳转到注册
            case R.id.bt_welcome_register:
                Intent intent2 = new Intent(this,RegisterActivity.class);
                startActivity(intent2);
                break;
            // 以游客的身份进入
            case R.id.bt_welcome_visitor:
                Intent intent3 = new Intent(this,MainActivity.class);
                startActivity(intent3);
                break;
        }
    }
}
















