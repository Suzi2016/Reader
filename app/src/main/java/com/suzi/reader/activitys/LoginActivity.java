package com.suzi.reader.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.suzi.reader.R;
import com.suzi.reader.utils.UIUtils;

public class LoginActivity extends Activity implements View.OnClickListener
{

    // 控件相关
    private Button bt_login_back;
    private Button bt_login_login;
    private EditText et_login_number;
    private EditText et_login_password;
    private ImageButton ib_login_clear_number;
    private ImageButton ib_login_clear_password;
    private TextView tv_login_forget;
    private TextView tv_login_register;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UIUtils.setActivityNoTitle(this);
        setContentView(R.layout.activity_login);

        bindView();
    }

    private void bindView()
    {
        bt_login_back = (Button) findViewById(R.id.bt_login_back);
        bt_login_login = (Button) findViewById(R.id.bt_login_login);
        et_login_number = (EditText) findViewById(R.id.et_login_number);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
        ib_login_clear_number = (ImageButton) findViewById(R.id.ib_login_clear_number);
        ib_login_clear_password = (ImageButton) findViewById(R.id.ib_login_clear_password);
        tv_login_forget = (TextView) findViewById(R.id.tv_login_forget);
        tv_login_register = (TextView) findViewById(R.id.tv_login_register);

        bt_login_back.setOnClickListener(this);
        bt_login_login.setOnClickListener(this);
        ib_login_clear_number.setOnClickListener(this);
        ib_login_clear_password.setOnClickListener(this);
        tv_login_forget.setOnClickListener(this);
        tv_login_register.setOnClickListener(this);

        // 设置监听输入状态，根据EditText中是否有内容从而显示清除按钮
        UIUtils.setTextChangedListener(et_login_number, ib_login_clear_number);
        UIUtils.setTextChangedListener(et_login_password,ib_login_clear_password);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            // 返回
            case R.id.bt_login_back:
                finish();
                break;
            // 登录
            case R.id.bt_login_login:
                //TODO 登录
                break;
            // 清空账号
            case R.id.ib_login_clear_number:
                et_login_number.setText("");
                break;
            // 清空密码
            case R.id.ib_login_clear_password:
                et_login_password.setText("");
                break;
            // 忘记密码
            case R.id.tv_login_forget:
                Intent intent = new Intent(this,FindPasswordActivity.class);
                startActivity(intent);
                break;
            // 注册
            case R.id.tv_login_register:
                Intent intent2 = new Intent(this,RegisterActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
























