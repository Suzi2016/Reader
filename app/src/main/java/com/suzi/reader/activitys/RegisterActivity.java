package com.suzi.reader.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.suzi.reader.R;
import com.suzi.reader.utils.UIUtils;

public class RegisterActivity extends Activity implements View.OnClickListener
{

    // 控件相关
    private Button bt_register_back;
    private Button bt_register_ok;
    private Button bt_register_get_code;
    private EditText et_register_phone;
    private EditText et_register_code;
    private EditText et_register_pw;
    private EditText et_register_re_pw;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UIUtils.setActivityNoTitle(this);
        setContentView(R.layout.activity_register);

        bindView();

    }

    /**
     * 绑定布局控件，设置监听事件（主要是Button的点击事件）
     */
    private void bindView()
    {
        bt_register_back = (Button) findViewById(R.id.bt_register_back);
        bt_register_get_code = (Button) findViewById(R.id.bt_register_get_code);
        bt_register_ok = (Button) findViewById(R.id.bt_register_ok);

        et_register_phone = (EditText) findViewById(R.id.et_register_phone);
        et_register_code = (EditText) findViewById(R.id.et_register_code);
        et_register_pw = (EditText) findViewById(R.id.et_register_pw);
        et_register_re_pw = (EditText) findViewById(R.id.et_register_re_pw);

        bt_register_back.setOnClickListener(this);
        bt_register_get_code.setOnClickListener(this);
        bt_register_ok.setOnClickListener(this);

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
            case R.id.bt_register_back:
                finish();
                break;
            // 获取验证码
            case R.id.bt_register_get_code:

                break;
            // 确定
            case R.id.bt_register_ok:

                break;
        }
    }
}
