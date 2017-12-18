package com.suzi.reader.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.suzi.reader.R;

public class HelpActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Button bt_help_back = (Button) findViewById(R.id.bt_help_back);
        bt_help_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }
}
