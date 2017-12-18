package com.suzi.reader.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.suzi.reader.R;
import com.suzi.reader.database.ChapterDBManager;
import com.suzi.reader.database.NovelDBManager;
import com.suzi.reader.horizontal_readmodel.Chapter;
import com.suzi.reader.horizontal_readmodel.OnReadStateChangeListener;
import com.suzi.reader.horizontal_readmodel.Page;
import com.suzi.reader.horizontal_readmodel.ScanView;
import com.suzi.reader.horizontal_readmodel.StringUtils;
import com.suzi.reader.utils.NovelUtils;
import com.suzi.reader.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class HorizontalReadActivity extends Activity implements View.OnClickListener
{

    private String TAG = "HorizontalReadActivity";

    private final int SUCCESS = 1;
    private final int FAIL = 0;
    private final int SUCCESS_PRE = 2;
    private final int SUCCESS_CUR = 3;
    private final int SUCCESS_NEXT = 4;

    /*
    控件相关
     */
    private ScanView scanview;
    private Button bt_horizontal;

    // 从上一个Activity传来的值
    private String novelUrl;

    // 数据库相关
    private ChapterDBManager chapterDBManager;
    private NovelDBManager novelDBManager;

    // 用来存放page信息
    private List<Page> preList;
    private List<Page> curList;
    private List<Page> nextList;
    private List<Page> pageList = new ArrayList<>();

    // 相关Chapter信息
    private Chapter preChapter;
    private Chapter curChapter;
    private Chapter nextChapter;

    // 小说当前阅读章节Id，最后章节Id
    private int readChapterId,lasthapterId;

    // 当前阅读页面的下标，当前章节内容是否为空
    private int index = 1;
    private boolean curContentIsNull = true;


    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case SUCCESS_PRE:

                    break;
                case SUCCESS_CUR:
                    openBook();
                    break;
                case SUCCESS_NEXT:

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UIUtils.setActivityNoTitle(this);
        setContentView(R.layout.activity_horizontal_read);

        bindView();

        /*
        接受从上一个Activity传过来的数据
        */
        Intent intent = getIntent();
        novelUrl = intent.getStringExtra("novelUrl");

        chapterDBManager = ChapterDBManager.getInstance(this);
        novelDBManager = NovelDBManager.getInstance(this);

        openBook();

//        int readChapterId = novelDBManager.getReadChapterID(novelUrl);
//
//        String s = chapterDBManager.getNovelInfo(novelUrl,readChapterId);
//        s = StringUtils.formatContent(s);
//        Log.i(TAG,s);
//        String title = chapterDBManager.getNovelName(novelUrl,readChapterId);
//
//        Chapter chapter = new Chapter(title,s,0);
//        pageList = StringUtils.chapterToPage(chapter,16,16,16,8);
//
//        scanview.setAdapter(pageList, 1, this);

    }

    private void bindView()
    {
        scanview = (ScanView) findViewById(R.id.scanview_horizontal);
        bt_horizontal = (Button) findViewById(R.id.bt_horizontal);
        bt_horizontal.setOnClickListener(this);

        scanview.setOnReadStateChangeListener(new OnReadStateChangeListener()
        {
            @Override
            public void onChapterChanged()
            {

            }

            @Override
            public void onPageChanged(int index)
            {
                // 根据index判断是否更改了章节，如果更改就重新设置pageList
                if (index<=preList.size())
                {
                    /*
                    跳换到了前一章
                     */
                    // 重新保存阅读进度
                    int newReadChapterId = novelDBManager.getReadChapterID(novelUrl) - 1;
                    String newReadChapter = chapterDBManager.getNovelName(novelUrl,
                            newReadChapterId);
                    novelDBManager.updateRead(newReadChapter,newReadChapterId,novelUrl);
                    HorizontalReadActivity.this.index = preList.size();
                    // 重新设置pageList
                    openBook();

                }else if (index > preList.size()+curList.size())
                {
                    /*
                    跳换到了下一章
                     */
                    // 重新保存阅读进度
                    int newReadChapterId = novelDBManager.getReadChapterID(novelUrl) + 1;
                    String newReadChapter = chapterDBManager.getNovelName(novelUrl,
                            newReadChapterId);
                    novelDBManager.updateRead(newReadChapter,newReadChapterId,novelUrl);
                    HorizontalReadActivity.this.index = 1;
                    // 重新设置pageList
                    openBook();


                }

            }

            @Override
            public void onLoadChapterFailure()
            {

            }

            @Override
            public void onCenterClick()
            {

            }

            @Override
            public void onFlip()
            {

            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.bt_horizontal:

                break;
        }
    }


    public void openBook()
    {
        // 得到小说当前阅读章节Id
        readChapterId = novelDBManager.getReadChapterID(novelUrl);
        Log.i(TAG,"readChapterId：" + readChapterId);

        // 得到小说最后阅读章节Id
        lasthapterId = novelDBManager.getLastChapterID(novelUrl);

        // 根据Id判断要获取的相关章节内容。如果当前章节是第一章， 就不要获取前面的章节；是最后一章就不要获取下一章节
        String tempContent = "";
        String tempTitle = "";

        if (readChapterId == 1)
        {
            preList = new ArrayList<>();
        }else {
            tempContent = chapterDBManager.getNovelInfo(novelUrl,readChapterId-1);
            tempTitle = chapterDBManager.getNovelName(novelUrl,readChapterId-1);

            if (TextUtils.isEmpty(tempContent))
            {
                preList = new ArrayList<>();
            }else {
                preChapter = new Chapter(tempTitle,tempContent,0);
                preList = StringUtils.chapterToPage(preChapter,16,16,16,8);
            }
        }

        tempContent = chapterDBManager.getNovelInfo(novelUrl,readChapterId);
        tempTitle = chapterDBManager.getNovelName(novelUrl,readChapterId);

        if (!TextUtils.isEmpty(tempContent))
        {
            curChapter = new Chapter(tempTitle,tempContent,0);
            curList = StringUtils.chapterToPage(curChapter,16,16,16,8);
            curContentIsNull = false;
        }else {
            setContents(chapterDBManager.getChapterUrl(novelUrl, readChapterId),SUCCESS_CUR);
        }

        if (readChapterId == lasthapterId)
        {
            nextList = new ArrayList<>();
        }else {
            tempContent = chapterDBManager.getNovelInfo(novelUrl,readChapterId+1);
            tempTitle = chapterDBManager.getNovelName(novelUrl,readChapterId+1);
            if (TextUtils.isEmpty(tempContent))
            {
                nextList = new ArrayList<>();
            }else {
                nextChapter = new Chapter(tempTitle,tempContent,0);
                nextList = StringUtils.chapterToPage(nextChapter,16,16,16,8);
            }

        }


        if (!curContentIsNull)
        {
            pageList.clear();

            pageList.addAll(preList);
            pageList.addAll(curList);
            pageList.addAll(nextList);

            if (scanview.isSetAdaptet())
            {
                scanview.setData(pageList,index + preList.size());
                Log.i(TAG,"setAdater.setData");
            }else {
                scanview.setAdapter(pageList, index + preList.size(), this);
            }
        }

    }


    /**
     * 根据章节url获取具体的章节内容
     * 获取成功，就将章节内容存到数据库里，然后在handler中更新相关数据
     * @param url 章节url
     */
    public void setContents(final String url,final int state)
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

                    msg.what = state;
                    msg.obj = content;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }


}
























