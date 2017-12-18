package com.suzi.reader.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.suzi.reader.R;
import com.suzi.reader.database.ChapterDBManager;
import com.suzi.reader.database.NovelDBManager;
import com.suzi.reader.utils.NovelUtils;
import com.suzi.reader.utils.ReadSettingUtils;
import com.suzi.reader.utils.SharedPreferenceUtils;
import com.suzi.reader.utils.UIUtils;

public class ReadNovelActivity extends Activity implements View.OnClickListener, View
        .OnTouchListener
{
    private static final int SUCCESS = 1;
    private static final int FAIL = 0;

    // 控件
    private TextView tv_read_title;
    private TextView tv_read_contents;
    private Button bt_read_lastchapter;
    private Button bt_read_contents;
    private Button bt_read_nextchapter;
    private ScrollView sv_read_contents;
    private LinearLayout ll_read;

    // 当前展示章节的url
    private String tempUrl;
    // 当前展示章节的ID
    private int tempReadChapterID;
    // 当前展示章节的标题
    private String tempTitle;
    // 当前展示章节的内容
    private String tempInfo;

    /*
    从上一个Activity传过来的数据：novelUrl,readChapterID
     */
    private String novelUrl;
    //    private int readChapterID;

    // 数据库相关
    private ChapterDBManager chapterDBManager;
    private NovelDBManager novelDBManager;

    // 记录ScrolleView滑动的位置
    private int scroll_X = 0, scroll_Y = 0;

    // 记录是否点击了Button
    private boolean isClicked = false;

    /*
    在handler中更新小说章节的内容
     */
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if (msg.what == SUCCESS)
            {
                // 判断是不是获取失败提示，如果是就设置字体颜色是橙色
                if (TextUtils.equals("获取失败", msg.obj.toString()))
                {

                }
                tv_read_contents.setText(msg.obj.toString() + "\n\n");
                // 屏蔽加载提示
                ll_read.setVisibility(LinearLayout.GONE);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UIUtils.setActivityNoTitle(this);
        setContentView(R.layout.activity_read_novel);

        /*
        接受从上一个Activity传过来的数据
        */
        Intent intent = getIntent();
        novelUrl = intent.getStringExtra("novelUrl");

        bindView();

        chapterDBManager = ChapterDBManager.getInstance(this);
        novelDBManager = NovelDBManager.getInstance(this);
    }

    /**
     * 显示小说内容
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        tempReadChapterID = novelDBManager.getReadChapterID(novelUrl);
        Log.i("tempeadChapterID", tempReadChapterID + "....test");
        showDate(tempReadChapterID);

        // 初始化ScrollView的位置
        sv_read_contents.post(new Runnable()
        {
            @Override
            public void run()
            {
                sv_read_contents.scrollTo(scroll_X, scroll_Y);
            }
        });
    }

    /**
     * 在数据库里更新改变的数据：readChapter,readChapterID
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        novelDBManager.updateRead(tempTitle, tempReadChapterID, novelUrl);

        // 记录ScrollView的位置
        scroll_X = sv_read_contents.getScrollX();
        scroll_Y = sv_read_contents.getScrollY();
    }

    private void bindView()
    {
        tv_read_title = (TextView) findViewById(R.id.tv_read_title);
        tv_read_contents = (TextView) findViewById(R.id.tv_read_contents);
        bt_read_lastchapter = (Button) findViewById(R.id.bt_read_lastchapter);
        bt_read_contents = (Button) findViewById(R.id.bt_read_contents);
        bt_read_nextchapter = (Button) findViewById(R.id.bt_read_nextchapter);
        sv_read_contents = (ScrollView) findViewById(R.id.sv_read_contents);
        ll_read = (LinearLayout) findViewById(R.id.ll_read);

        bt_read_lastchapter.setOnClickListener(this);
        bt_read_contents.setOnClickListener(this);
        bt_read_nextchapter.setOnClickListener(this);

        bt_read_lastchapter.setOnTouchListener(this);
        bt_read_contents.setOnTouchListener(this);
        bt_read_nextchapter.setOnTouchListener(this);

        // 从SP获取阅读参数并初始化
        int fontSize = SharedPreferenceUtils.getFontSize(this);
        int fontColor = SharedPreferenceUtils.getFontColor(this);
        int readBg = SharedPreferenceUtils.getReadBackground(this);
        float fontSpacing = SharedPreferenceUtils.getFontSpacing(this);

        ReadSettingUtils.setFontSize(tv_read_contents, fontSize);
        ReadSettingUtils.setFontColor(this, tv_read_contents, fontColor);
        ReadSettingUtils.setBackgroundColor(this, sv_read_contents, readBg);
        ReadSettingUtils.setFontSpacing(tv_read_contents, fontSpacing);


    }

    @Override
    public void onClick(View v)
    {
        isClicked = true;
        switch (v.getId())
        {
            // 阅读上一章
            case R.id.bt_read_lastchapter:
                showDate(--tempReadChapterID);
                break;
            //
            case R.id.bt_read_contents:
                Intent intent = new Intent();
                intent.putExtra("novelUrl", novelUrl);
                intent.putExtra("readChapterID", tempReadChapterID + 1);
                intent.setClass(ReadNovelActivity.this, ContentsActivity.class);
                startActivity(intent);
                break;
            //
            case R.id.bt_read_nextchapter:
                showDate(++tempReadChapterID);
                break;

        }
    }


    /**
     * 设置上下章菜单是否可以点击
     */
    public void setButtonClickable()
    {
        /*
        如果当前章是第一章，则上一章Button不能点击
        如果当前章是最后一章，则下一章Button不能点击
         */
        int lastChapterId = novelDBManager.getLastChapterID(novelUrl);  // 最后一章的id
        if (tempReadChapterID == 0)
        {
            bt_read_lastchapter.setClickable(false);
            //            bt_read_lastchapter.setBackgroundColor(getResources().getColor(R.color
            // .gray));
            bt_read_lastchapter.setTextColor(getResources().getColor(R.color.gray_blue));
        } else
        {
            bt_read_lastchapter.setClickable(true);
            //            bt_read_lastchapter.setBackgroundDrawable(getResources().getDrawable(R
            //                    .drawable.bt_style_blue));
            bt_read_lastchapter.setTextColor(getResources().getColor(R.color.white));
        }

        if (tempReadChapterID == lastChapterId)
        {
            bt_read_nextchapter.setClickable(false);
            //            bt_read_nextchapter.setBackgroundColor(getResources().getColor(R.color
            // .gray));
            bt_read_nextchapter.setTextColor(getResources().getColor(R.color.gray_blue));
        } else
        {
            bt_read_nextchapter.setClickable(true);
            //            bt_read_nextchapter.setBackgroundDrawable(getResources().getDrawable(R
            //                    .drawable.bt_style_blue));
            bt_read_nextchapter.setTextColor(getResources().getColor(R.color.white));
        }
    }


    /**
     * 从网络获取章节内容，并再UI线程中更新
     *
     * @param url
     */
    public void setContents(final String url)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                /*
                从网络获取章节内容
                1.根据url获取章节内容
                2.通过Handler在UI线程中更新数据
                 */
                String content = NovelUtils.getChapterInfo(url);
                Message msg = new Message();
                if (TextUtils.isEmpty(content))
                {
                    msg.what = FAIL;
                } else
                {
                    // 判断是否是获取失败提示，如果不是就写入数据库
                    if (!TextUtils.equals("获取失败", content))
                    {
                        // 将章节内容写到数据库里
                        chapterDBManager.updateChapterInfo(content, url);
                    }

                    msg.what = SUCCESS;
                    msg.obj = content;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * 显示小说的标题个内容
     *
     * @param chapterId 当前章节的id
     */
    private void showDate(int chapterId)
    {
        // 清空页面数据，并且设置下次从头开始显示
        tv_read_contents.setText("");
        sv_read_contents.scrollTo(0, 0);

        // 显示加载提示
        ll_read.setVisibility(LinearLayout.VISIBLE);

        // 显示小说的标题，从数据库获取数据
        tempTitle = chapterDBManager.getNovelName(novelUrl, chapterId);
        tv_read_title.setText(tempTitle);

        // 显示小说的内容，先从数据库获取，没有就从网络获取
        tempInfo = chapterDBManager.getNovelInfo(novelUrl, chapterId);
        if (TextUtils.isEmpty(tempInfo))
        {
            /*
            如果从数据库没有获取到内容，就从网络获取
            1.先获取当前章节的url
            2.请求网络，获取内容
             */
            String url = chapterDBManager.getChapterUrl(novelUrl, tempReadChapterID);
            Log.i("url", url + "....test");
            setContents(url);

        } else
        {
            tv_read_contents.setText(tempInfo + "\n\n");
            // 屏蔽加载提示
            ll_read.setVisibility(LinearLayout.GONE);
        }

        setButtonClickable();
    }


    /**
     * 点击Button改变字体颜色，实现按键效果，结合onTouch()方法
     *
     * @param bt
     * @param event
     */
    public void setButtonTextColor(Button bt, MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            bt.setTextColor(getResources().getColor(R.color.gray_blue));
        } else if (event.getAction() == MotionEvent.ACTION_UP)
        {
            /*
            如果是进入最后一章或者第一章，就不将颜色改回来，或者设置成不可点击的颜色
            1.首先判断是否点击了，只有点击了才会进行点击事件跳转章节
            2.然后判断是否是要进入最后一章或者进入第一章
             */
            int lastChapterId = novelDBManager.getLastChapterID(novelUrl);  // 最后一章的id
            if (isClicked)
            {
                boolean b = (bt.equals(bt_read_nextchapter)) && (tempReadChapterID ==
                        lastChapterId - 1);
                Log.i("ReadNovelActivity", b + ".......");
                if ((bt.equals(bt_read_lastchapter)) && (tempReadChapterID == 1))
                {
                } else if ((bt.equals(bt_read_nextchapter)) && (tempReadChapterID ==
                        lastChapterId - 1))
                {
                } else
                {
                    bt.setTextColor(getResources().getColor(R.color.white));
                    Log.i("ReadNovelActivity", "改变了颜色————白色");
                }
            } else
            {
                bt.setTextColor(getResources().getColor(R.color.white));
            }
            isClicked = false;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (v.getId())
        {
            // 阅读上一章
            case R.id.bt_read_lastchapter:
                setButtonTextColor(bt_read_lastchapter, event);
                break;
            //
            case R.id.bt_read_contents:
                setButtonTextColor(bt_read_contents, event);
                break;
            //
            case R.id.bt_read_nextchapter:
                setButtonTextColor(bt_read_nextchapter, event);
                break;

        }

        return false;
    }
}






















