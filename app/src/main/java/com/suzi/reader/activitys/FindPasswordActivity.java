package com.suzi.reader.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.suzi.reader.R;
import com.suzi.reader.utils.UIUtils;

public class FindPasswordActivity extends Activity implements View.OnClickListener
{

    // 控件相关
    private Button bt_find_password_back;
    private Button bt_find_password_ok;
    private Button bt_find_password_get_code;
    private EditText et_find_password_phone;
    private EditText et_find_password_code;
    private EditText et_find_password_pw;
    private EditText et_find_password_re_pw;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UIUtils.setActivityNoTitle(this);
        setContentView(R.layout.activity_find_password);

        bindView();

    }

    /**
     * 绑定布局控件，设置监听事件（主要是Button的点击事件）
     */
    private void bindView()
    {
        bt_find_password_back = (Button) findViewById(R.id.bt_find_password_back);
        bt_find_password_get_code = (Button) findViewById(R.id.bt_find_password_get_code);
        bt_find_password_ok = (Button) findViewById(R.id.bt_find_password_ok);

        et_find_password_phone = (EditText) findViewById(R.id.et_find_password_phone);
        et_find_password_code = (EditText) findViewById(R.id.et_find_password_code);
        et_find_password_pw = (EditText) findViewById(R.id.et_find_password_pw);
        et_find_password_re_pw = (EditText) findViewById(R.id.et_find_password_re_pw);

        bt_find_password_back.setOnClickListener(this);
        bt_find_password_get_code.setOnClickListener(this);
        bt_find_password_ok.setOnClickListener(this);

    }

    /**
     * Button的点击事件
     */
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            // 返回
            case R.id.bt_find_password_back:
                finish();
                break;
            // 获取验证码
            case R.id.bt_find_password_get_code:

                break;
            // 确定
            case R.id.bt_find_password_ok:

                break;
        }
    }
}























