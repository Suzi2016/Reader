package com.suzi.reader.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.suzi.reader.R;
import com.suzi.reader.entities.ChapterTag;
import com.suzi.reader.utils.NovelUtils;
import com.suzi.reader.view.BookStoreView;

import java.util.ArrayList;

public class NovelListActivity extends Activity implements View.OnClickListener
{

    private static final int SUCCESS = 1;
    private static final int FAIL = 0;

    private String TAG = "NovelListActivity";

    /*
    控件相关
     */
    private Button bt_list_back;
    private TextView tv_list_title;
    private ListView lv_list;
    private LinearLayout ll_list;

    /*
    从上一个Activity传过来的参数
     */
    private String title;
    private String url;
    private String tag;

    /*
    用来记录当前ListView滑动的位置
     */
    private int scrollToX = 0;
    private int scrollToY= 0;

    /*
    用来存放获取到的小说列表的信息
     */
    private ArrayList<ChapterTag> chapterTags = new ArrayList<>();



    /*
    在Handler中更新ListView的信息
     */
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if (msg.what == SUCCESS)
            {
                // 隐藏加载提示控件
                ll_list.setVisibility(View.GONE);

                initAdapter();
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_list);

        // 获取从上一个界面穿过来的值
        Intent intent = getIntent();
        title = intent.getStringExtra("title_name");
        url = intent.getStringExtra("url");
        tag = intent.getStringExtra("TAG");

        // 绑定控件
        bindView();

        Log.i(TAG,TextUtils.equals(tag,BookStoreView.TAG_ALL)+"");

        // 根据url获取要显示的信息
        getNovelInfo();

    }


    @Override
    protected void onResume()
    {
        super.onResume();
        // 将ListView滑动到之前记录的位置
        lv_list.scrollTo(scrollToX,scrollToY);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        // 记录ListVeiw滑动的位置
        scrollToX = lv_list.getScrollX();
        scrollToY = lv_list.getScrollY();
    }

    private void bindView()
    {
        bt_list_back = (Button) findViewById(R.id.bt_list_back);
        tv_list_title = (TextView) findViewById(R.id.tv_list_title);
        lv_list = (ListView) findViewById(R.id.lv_list);
        ll_list = (LinearLayout) findViewById(R.id.ll_list);

        tv_list_title.setText(title);

        bt_list_back.setOnClickListener(this);

        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String name = chapterTags.get(position).getName();
                String url = chapterTags.get(position).getUrl();

                Intent intent = new Intent(NovelListActivity.this,NovelDetailActivity.class);
                intent.putExtra("url",url);
                Log.i(TAG,url);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            // 退出，返回上一界面
            case R.id.bt_list_back:
                finish();
                break;
        }
    }

    public void getNovelInfo()
    {
        /*
        根据榜单不同获取小说列表信息，然后在Handler中更新
         */
        if (TextUtils.equals(tag,BookStoreView.TAG_LIST))
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    Log.i(TAG,"........开始获取info");
                    chapterTags = NovelUtils.getOrderNovelInfo(url);
                    Log.i(TAG,"........"+chapterTags.size());
                    Message msg = new Message();
                    msg.what = SUCCESS;
                    handler.sendMessage(msg);
                }
            }).start();

        }else if (TextUtils.equals(tag,BookStoreView.TAG_ALL))
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    chapterTags = NovelUtils.getAllClassNovelInfo(url);
                    Message msg = new Message();
                    msg.what = SUCCESS;
                    handler.sendMessage(msg);
                }
            }).start();
        }
    }


    private void initAdapter()
    {
        ArrayList<String> names = new ArrayList<>();
//        for (ChapterTag chapterTag:chapterTags)
//        {
//            names.add(chapterTag.getName());
//        }
        String name;
        for (int i = 0;i<chapterTags.size();i++)
        {
            name = (i+1) + "." + chapterTags.get(i).getName();
            names.add(name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout
                .simple_list_item_1,names);
        lv_list.setAdapter(adapter);

        tv_list_title.setText(title+"(共" + names.size() + "本小说)");

    }
}



















