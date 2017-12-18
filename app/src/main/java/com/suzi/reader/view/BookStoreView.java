package com.suzi.reader.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suzi.reader.R;
import com.suzi.reader.activitys.NovelListActivity;
import com.suzi.reader.utils.ConstantsValues;

/**
 * Created by Suzi on 2016/11/14.
 */

public class BookStoreView extends Fragment
{
    public static final String TAG_LIST = "LIST"; // 表示榜单
    public static final String TAG_ALL = "ALL"; // 表示大全

    private Context mContext;

    /*
    页面控件
     */
    private TextView tv_bookstore_allvisit;
    private TextView tv_bookstore_allvote;
    private TextView tv_bookstore_mouthvisit;
    private TextView tv_bookstore_mouthvote;
    private TextView tv_bookstore_postdate;
    private TextView tv_bookstore_masterdate;
    private TextView tv_bookstore_goodnum;
    private TextView tv_bookstore_size;

    private LinearLayout ll_bookstore_01;
    private LinearLayout ll_bookstore_02;
    private LinearLayout ll_bookstore_03;
    private LinearLayout ll_bookstore_04;
    private LinearLayout ll_bookstore_05;
    private LinearLayout ll_bookstore_06;
    private LinearLayout ll_bookstore_07;
    private LinearLayout ll_bookstore_08;
    private LinearLayout ll_bookstore_09;
    private LinearLayout ll_bookstore_10;


    @SuppressLint({"NewApi", "ValidFragment"})
    public BookStoreView()
    {
    }

    @SuppressLint({"NewApi", "ValidFragment"})
    public BookStoreView(Context context)
    {
        super();
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.model_bookstore, container, false);

        tv_bookstore_allvisit = (TextView) view.findViewById(R.id.tv_bookstore_allvisit);
        tv_bookstore_allvote = (TextView) view.findViewById(R.id.tv_bookstore_allvote);
        tv_bookstore_mouthvisit = (TextView) view.findViewById(R.id.tv_bookstore_mouthvisit);
        tv_bookstore_mouthvote = (TextView) view.findViewById(R.id.tv_bookstore_mouthvote);
        tv_bookstore_postdate = (TextView) view.findViewById(R.id.tv_bookstore_postdate);
        tv_bookstore_masterdate = (TextView) view.findViewById(R.id.tv_bookstore_masterdate);
        tv_bookstore_goodnum = (TextView) view.findViewById(R.id.tv_bookstore_goodnum);
        tv_bookstore_size = (TextView) view.findViewById(R.id.tv_bookstore_size);

        ll_bookstore_01 = (LinearLayout) view.findViewById(R.id.ll_bookstore_01);
        ll_bookstore_02 = (LinearLayout) view.findViewById(R.id.ll_bookstore_02);
        ll_bookstore_03 = (LinearLayout) view.findViewById(R.id.ll_bookstore_03);
        ll_bookstore_04 = (LinearLayout) view.findViewById(R.id.ll_bookstore_04);
        ll_bookstore_05 = (LinearLayout) view.findViewById(R.id.ll_bookstore_05);
        ll_bookstore_06 = (LinearLayout) view.findViewById(R.id.ll_bookstore_06);
        ll_bookstore_07 = (LinearLayout) view.findViewById(R.id.ll_bookstore_07);
        ll_bookstore_08 = (LinearLayout) view.findViewById(R.id.ll_bookstore_08);
        ll_bookstore_09 = (LinearLayout) view.findViewById(R.id.ll_bookstore_09);
        ll_bookstore_10 = (LinearLayout) view.findViewById(R.id.ll_bookstore_10);


        setOnClickListener(tv_bookstore_allvisit, ConstantsValues.ALL_VISIT,"总排行榜",TAG_LIST);
        setOnClickListener(tv_bookstore_allvote,ConstantsValues.ALL_VOTE,"总推荐榜",TAG_LIST);
        setOnClickListener(tv_bookstore_mouthvisit,ConstantsValues.MOUTH_VISIT,"月排行榜",TAG_LIST);
        setOnClickListener(tv_bookstore_mouthvote,ConstantsValues.MOUTH_VOTE,"月推荐榜",TAG_LIST);
        setOnClickListener(tv_bookstore_postdate, ConstantsValues.POST_DATE,"最新入库",TAG_LIST);
        setOnClickListener(tv_bookstore_masterdate,ConstantsValues.MASTER_UPDATE,"转载更新",TAG_LIST);
        setOnClickListener(tv_bookstore_goodnum,ConstantsValues.GOOD_NUM,"总收藏榜",TAG_LIST);
        setOnClickListener(tv_bookstore_size,ConstantsValues.SIZE,"字数排行",TAG_LIST);

        setOnClickListener(ll_bookstore_01,ConstantsValues.MAP_01,"玄幻魔法",TAG_ALL);
        setOnClickListener(ll_bookstore_02,ConstantsValues.MAP_02,"武侠修真",TAG_ALL);
        setOnClickListener(ll_bookstore_03,ConstantsValues.MAP_03,"都市言情",TAG_ALL);
        setOnClickListener(ll_bookstore_04,ConstantsValues.MAP_04,"历史军事",TAG_ALL);
        setOnClickListener(ll_bookstore_05,ConstantsValues.MAP_05,"侦探推理",TAG_ALL);
        setOnClickListener(ll_bookstore_06,ConstantsValues.MAP_06,"网游动漫",TAG_ALL);
        setOnClickListener(ll_bookstore_07,ConstantsValues.MAP_07,"科幻小说",TAG_ALL);
        setOnClickListener(ll_bookstore_08,ConstantsValues.MAP_08,"恐怖灵异",TAG_ALL);
        setOnClickListener(ll_bookstore_09,ConstantsValues.MAP_09,"散文诗词",TAG_ALL);
        setOnClickListener(ll_bookstore_10,ConstantsValues.MAP_10,"其他",TAG_ALL);

        return view;

    }


    /**
     * 设置Item点击事件，点击进入小说列表Activity
     * @param view
     * @param url
     * @param title_name
     */
    public void setOnClickListener(View view, final String url, final String title_name, final
    String tag)
    {
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, NovelListActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("title_name",title_name);
                intent.putExtra("TAG",tag);
                startActivity(intent);
            }
        });
    }
}










