package com.suzi.reader.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.suzi.reader.R;
import com.suzi.reader.adapter.ContentsListAdapter;
import com.suzi.reader.database.ChapterDBManager;
import com.suzi.reader.database.NovelDBManager;
import com.suzi.reader.utils.UIUtils;

import java.util.ArrayList;
import java.util.Collections;

public class ContentsActivity extends Activity implements View.OnClickListener, View.OnTouchListener
{

    // 控件
    private ListView lv_contents;
    private Button bt_contents_back;
    private Button bt_contents_order;

    // ListView相关
    private ArrayList<String> chapterNames = new ArrayList<String>();
    private ContentsListAdapter adapter;

    // 小说的url
    private String novelUrl;

    // 当前阅读的章节ID
    private int readChapterId;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UIUtils.setActivityNoTitle(this);
        setContentView(R.layout.activity_contents);

        // 得到从上一个Activity传过来的数据
        Intent intent = getIntent();
        novelUrl = intent.getStringExtra("novelUrl");
        readChapterId = intent.getIntExtra("readChapterID",0);

        bindView();

        initDate();
        setListViewAdapter();

    }


    private void initDate()
    {
        ChapterDBManager chapterDBManager = ChapterDBManager.getInstance(this);
        chapterNames = chapterDBManager.getAllChapterName(novelUrl);
    }

    private void setListViewAdapter()
    {
        adapter = new ContentsListAdapter(this, R.layout.listview_contents, chapterNames,
                readChapterId);
        lv_contents.setAdapter(adapter);

        /*
        当前行显示在第五行，如果当前行是第一行则显示在第一行
         */
        int selection = readChapterId - 5;
        if (selection < 0)
        {
            selection = 0;
        }
        lv_contents.setSelection(selection);


        adapter.notifyDataSetChanged();
        lv_contents.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // 用来标记当前选中的章节所对应的chapterReadId
                int tag;
                // 显示"正序"说明当前是逆序的
                if (TextUtils.equals(bt_contents_order.getText().toString(), "正序"))
                {
                    tag = parent.getCount() - position - 1;
                } else
                {
                    tag = position;
                }
                // 选中的章节名
                String chapterName = chapterNames.get(tag);
                Log.i("选中的章节名",chapterName + "...." + tag + novelUrl);

                NovelDBManager novelDBManager = NovelDBManager.getInstance(ContentsActivity.this);
                novelDBManager.updateRead(chapterName,tag,novelUrl);

                Intent intent = new Intent(ContentsActivity.this,ReadNovelActivity.class);
                intent.putExtra("novelUrl",novelUrl);
                intent.putExtra("readChapterID",tag);
                startActivity(intent);

//                novelTagDBManager.setReadChapterId(novelName, tag);
//                finish();
            }
        });
    }


    private void bindView()
    {
        lv_contents = (ListView) findViewById(R.id.lv_contents);
        bt_contents_back = (Button) findViewById(R.id.bt_contents_back);
        bt_contents_order = (Button) findViewById(R.id.bt_contents_order);

        bt_contents_back.setOnClickListener(this);
        bt_contents_order.setOnClickListener(this);
        bt_contents_back.setOnTouchListener(this);
        bt_contents_order.setOnTouchListener(this);
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            //  返回
            case R.id.bt_contents_back:
                finish();
                break;
            //  排序（选择正序显示或者逆序显示）
            case R.id.bt_contents_order:
                String orderString = bt_contents_order.getText().toString().trim();
                if (TextUtils.equals(orderString, "逆序"))
                {
                    bt_contents_order.setText("正序");
                    Collections.reverse(chapterNames);
                    readChapterId = chapterNames.size() - readChapterId + 1;
                    setListViewAdapter();
                    adapter.notifyDataSetChanged();
                } else
                {
                    bt_contents_order.setText("逆序");
                    Collections.reverse(chapterNames);
                    readChapterId = chapterNames.size() - readChapterId + 1;
                    setListViewAdapter();
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    /**
     * 按下去的时候改变button字体颜色
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (v.getId())
        {
            case R.id.bt_contents_back:
                setButtonTextColor(bt_contents_back,event);
                break;

            case R.id.bt_contents_order:
                setButtonTextColor(bt_contents_order,event);
                break;
        }

        return false;
    }


    /**
     * 将一个ArrayList的值倒序过来
     *
     * @param list
     */
    public ArrayList<String> orderArraylist(ArrayList<String> list)
    {
        ArrayList<String> temp = new ArrayList<String>();
        for (int i = list.size() - 1; i >= 0; i--)
        {
            temp.add(list.get(i));
        }
        return temp;
    }


    public void setButtonTextColor(Button bt,MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            bt.setTextColor(getResources().getColor(R.color.blue));
        } else if (event.getAction() == MotionEvent.ACTION_UP)
        {
            bt.setTextColor(getResources().getColor(R.color.black));
        }
    }

}






















