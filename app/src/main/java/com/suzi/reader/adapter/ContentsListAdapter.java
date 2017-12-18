package com.suzi.reader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.suzi.reader.R;

import java.util.List;

/**
 * Created by Suzi on 2016/10/27.
 * 展示小说目录
 */

public class ContentsListAdapter extends ArrayAdapter<String>
{

    private int resourceId;
    private int chapterReadId;
    private Context mContext;

    public ContentsListAdapter(Context context, int resource, List<String> objects,int
            chapterReadId)
    {
        super(context, resource, objects);
        resourceId = resource;
        this.chapterReadId = chapterReadId;
        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String chapterName = getItem(position);
        View view;
        ViewHolder holder;
        if (convertView == null)
        {
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            holder = new ViewHolder();
            holder.tv = (TextView) view.findViewById(R.id.tv_listview_contents);
            view.setTag(holder);
        }else
        {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        if (chapterReadId == getItemId(position)+1)
        {
            holder.tv.setTextColor(mContext.getResources().getColor(R.color.blue));
        }else{
            holder.tv.setTextColor(mContext.getResources().getColor(R.color.black));
        }
        holder.tv.setText(chapterName);
        return view;
    }

    class ViewHolder
    {
        TextView tv;
    }


}
























