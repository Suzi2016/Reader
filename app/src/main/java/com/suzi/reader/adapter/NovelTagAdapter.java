package com.suzi.reader.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.suzi.reader.R;
import com.suzi.reader.entities.NovelTag;

import java.util.List;

/**
 * Created by Suzi on 2016/10/25.
 * 用来封装小说搜索结果的
 */

public class NovelTagAdapter extends ArrayAdapter<NovelTag>
{
    private int resourceId;
    private Context mContext;


    public NovelTagAdapter(Context context, int resource, List<NovelTag> objects)
    {
        super(context, resource, objects);
        resourceId = resource;
        mContext = context;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        NovelTag novelTag = getItem(position);
        View view;
        ViewHolder holder;
        if (convertView == null)
        {
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            holder = new ViewHolder();
            holder.tv_novelName = (TextView) view.findViewById(R.id.tv_list_novel_tag_show_name);
            holder.tv_desc = (TextView) view.findViewById(R.id.tv_list_novel_tag_show_desc);
//            holder.iv = (ImageView) view.findViewById(R.id.iv_list_novel_tag_show);
            view.setTag(holder);
        }else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        if (novelTag != null)
        {
            holder.tv_novelName.setText(novelTag.getNovelName());
            holder.tv_desc.setText(novelTag.getDesc());
        }

        return view;
    }


    private class ViewHolder
    {
//        ImageView iv;
        TextView tv_novelName;
        TextView tv_desc;
    }
}

























