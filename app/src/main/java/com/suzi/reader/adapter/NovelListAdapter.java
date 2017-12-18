package com.suzi.reader.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.suzi.reader.R;
import com.suzi.reader.entities.Novel;

import java.util.List;

/**
 * Created by Suzi on 2016/10/26.
 */

public class NovelListAdapter extends ArrayAdapter<Novel>
{
    private int resourceId;
    private Context mContext;

    public NovelListAdapter(Context context, int resource, List<Novel> objects)
    {
        super(context, resource, objects);
        resourceId = resource;
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Novel novelList = getItem(position);
        View view;
        ViewHolder holder;
        if (convertView == null)
        {
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            holder = new ViewHolder();
            holder.tv_novelName = (TextView) view.findViewById(R.id.tv_listview_novellist_novelname);
            holder.tv_latest = (TextView) view.findViewById(R.id.tv_listview_novellist_latest);
            holder.tv_read = (TextView) view.findViewById(R.id.tv_listview_novellist_read);
            holder.iv = (ImageView) view.findViewById(R.id.iv_listview_novellist);
            view.setTag(holder);
        }else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        /*
        判断是否阅读过，如果没有，就提示“未阅读”
         */
        if (novelList.getReads() == 0)
        {
            holder.tv_latest.setText("未阅读");
            holder.tv_read.setText("");
        }else {
            holder.tv_latest.setText("最新：" + novelList.getLastChapter());

            /*
            判断当前阅读章节是否是最后一章，如果是则提示已读完
             */
            if (TextUtils.equals(novelList.getLastChapter(),novelList.getReadChapter()))
            {
                holder.tv_read.setText("已读完");
            }else {
                holder.tv_read.setText("读至：" + novelList.getReadChapter());
            }
        }

        holder.tv_novelName.setText(novelList.getName());

        /*
        更新章节数是否大于0，大于说明有更新就显示更新提示
         */
        if (novelList.getupdates() > 0)
        {
            holder.iv.setVisibility(ImageView.VISIBLE);
        } else {
            holder.iv.setVisibility(ImageView.GONE);
        }

        return view;

    }

    class ViewHolder
    {
        TextView tv_novelName;
        TextView tv_latest;
        TextView tv_read;
        ImageView iv;
    }
}




















