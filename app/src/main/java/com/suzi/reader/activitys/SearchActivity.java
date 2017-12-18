package com.suzi.reader.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.suzi.reader.R;
import com.suzi.reader.adapter.NovelTagAdapter;
import com.suzi.reader.entities.NovelTag;
import com.suzi.reader.utils.NovelUtils;
import com.suzi.reader.utils.UIUtils;

import java.util.ArrayList;

public class SearchActivity extends Activity implements View.OnClickListener
{
    private static final int SUCCESS = 1;
    private static final int FAIL = 0;
    private static final int START_ADD = 2;
    private static final int END_ADD = 3;


    // 布局控件相关
    private Button bt_search_back;
    private Button bt_search_search;
    private Button bt_search_clear;
    private EditText et_search_input;
    private TextView tv_search_noResultTip;
    private ListView lv_search_result;

    // ListView相关
    private ArrayList<NovelTag> novelTags = new ArrayList<NovelTag>();
    private NovelTagAdapter adapter;

    private String keyWords;

    private AlertDialog loadDialog;


    /*
    将搜索结果在Handler中更新显示
     */
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);

            switch (msg.what)
            {
                // 更新UI显示
                case SUCCESS:
                    adapter = new NovelTagAdapter(SearchActivity.this,R.layout.list_novel_tag_show,
                            novelTags);
                    lv_search_result.setAdapter(adapter);
                    lv_search_result.setVisibility(ListView.VISIBLE);
                    break;
                // 显示没有搜索到的提示
                case FAIL:
                    tv_search_noResultTip.setVisibility(TextView.VISIBLE);
                    break;
                // 创建一个Dialog
                case START_ADD:
                    View view = View.inflate(SearchActivity.this,R.layout.dialog_loading,null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                    loadDialog = builder.create();
                    loadDialog.setView(view);
                    loadDialog.setCanceledOnTouchOutside(false);
                    // 屏蔽返回键
                    loadDialog.setOnKeyListener(new DialogInterface.OnKeyListener()
                    {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
                        {
                            if (keyCode == KeyEvent.KEYCODE_BACK)
                            {
                                return true;
                            }
                            return false;
                        }
                    });
                    loadDialog.show();
                    break;
                // 让创建的Dialog消失
                case END_ADD:
                    loadDialog.dismiss();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UIUtils.setActivityNoTitle(this);
        setContentView(R.layout.activity_search);

        bindView();

    }

    private void bindView()
    {
        /*
        Button
         */
        bt_search_back = (Button) findViewById(R.id.bt_search_back);
        bt_search_search = (Button) findViewById(R.id.bt_search_search);
        bt_search_clear = (Button) findViewById(R.id.bt_search_clear);

        bt_search_back.setOnClickListener(this);
        bt_search_search.setOnClickListener(this);
        bt_search_clear.setOnClickListener(this);

        /*
        EditText
         */
        et_search_input = (EditText) findViewById(R.id.et_search_input);

        /*
        TextView 用于没有搜索到结果时候提示
         */
        tv_search_noResultTip = (TextView) findViewById(R.id.tv_search_noResultTip);

        /*
        ListView 显示搜索结果
         */
        lv_search_result = (ListView) findViewById(R.id.lv_search_result);
        View lv_header = View.inflate(this,R.layout.search_results_header,null);
        lv_search_result.addHeaderView(lv_header);

        /*
        为Item设置点击事件，跳转到小说详情页
        并传递小说名字和url
         */
        lv_search_result.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // position是从Header开始计数，如果点击Head而就会报异常
                if (position > 0)
                {
                    String url = novelTags.get(position-1).getUrl();
                    url = url.replace("23wx", "23us");
                    String name = novelTags.get(position-1).getNovelName();
                    Intent intent = new Intent(SearchActivity.this,NovelDetailActivity.class);
                    intent.putExtra("url",url);
                    intent.putExtra("name",name);
                    startActivity(intent);
                }
            }
        });

    }

    /**
     * Button点击事件
     * @param v 要处理的Button
     */
    @Override
    public void onClick(View v)
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context
                .INPUT_METHOD_SERVICE);
        switch (v.getId())
        {
            // 返回
            case R.id.bt_search_back:
                // 先隐藏输入啊
                imm.hideSoftInputFromWindow(et_search_input.getWindowToken(), 0);
                finish();
                break;
            // 搜索
            case R.id.bt_search_search:
                // 判断输入框中内容是否为空，为空提示不搜索
                keyWords = et_search_input.getText().toString().trim();
                if (TextUtils.isEmpty(keyWords))
                {
                    Toast.makeText(this, "搜索内容不能为空哦~", Toast.LENGTH_SHORT).show();
                }else {
                    //  隐藏输入法软键盘
                    imm.hideSoftInputFromWindow(et_search_input.getWindowToken(), 0);

                    // 先将原来的结果隐藏
                    lv_search_result.setVisibility(ListView.GONE);

                    // 没有搜索到结果提示也隐藏
                    tv_search_noResultTip.setVisibility(TextView.GONE);

                    // 开一个线程进行网络访问，搜索关键词，并将结果在Handle中处理
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // 发Handler消息，弹出Dialog提示正在加载
                            Message msg_show = new Message();
                            msg_show.what = START_ADD;
                            handler.sendMessage(msg_show);

                            novelTags = NovelUtils.searchNovelByKeyWords(keyWords);
                            Message msg = new Message();
                            if (novelTags.size() == 0)
                            {
                                msg.what = FAIL;
                            }else {
                                msg.what = SUCCESS;
                            }
                            handler.sendMessage(msg);

                            // 发Handler消息，让弹出的提示正在加载的Dialog消失
                            Message msg2 = new Message();
                            msg2.what = END_ADD;
                            handler.sendMessage(msg2);
                        }
                    }).start();
                }

                break;
            // 清空输入框内容
            case R.id.bt_search_clear:
                et_search_input.setText("");
                imm.showSoftInput(et_search_input,InputMethodManager.SHOW_FORCED);
                break;
        }
    }
}























