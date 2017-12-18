package com.suzi.reader.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.suzi.reader.R;

public class AboutActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Button bt = (Button) findViewById(R.id.bt_about_back);
        bt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }
}
