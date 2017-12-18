package com.suzi.reader.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.suzi.reader.R;
import com.suzi.reader.utils.UIUtils;

public class TryToReadActivity extends Activity
{

    private Button bt_try_back;
    private WebView wv_try;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UIUtils.setActivityNoTitle(this);
        setContentView(R.layout.activity_try_to_read);

        Intent intent = getIntent();
        url = intent.getStringExtra("url");

        bt_try_back = (Button) findViewById(R.id.bt_try_back);
        wv_try = (WebView) findViewById(R.id.wv_try);

        wv_try.setWebChromeClient(new WebChromeClient());
        wv_try.setWebViewClient(new WebViewClient());
//        wv_try.getSettings().setJavaScriptEnabled(true);
        wv_try.loadUrl(url);

    }
}















