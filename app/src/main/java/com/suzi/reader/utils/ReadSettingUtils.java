package com.suzi.reader.utils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.suzi.reader.R;

/**
 * 根据从SP获取到的阅读参数，将其设置给相应的View
 * Created by Suzi on 2016/11/24.
 */

public class ReadSettingUtils
{
    public static void setFontSize(TextView tv, int fontSize)
    {
        tv.setTextSize(fontSize);
    }

    public static void setBackgroundColor(Context context,View view, int color)
    {
        switch (color)
        {
            case 0:
                view.setBackgroundColor(context.getResources().getColor(R.color.read_bg_05));
                break;
            case 1:
                view.setBackgroundColor(context.getResources().getColor(R.color.read_bg_01));
                break;
            case 2:
                view.setBackgroundColor(context.getResources().getColor(R.color.read_bg_02));
                break;
            case 3:
                view.setBackgroundColor(context.getResources().getColor(R.color.read_bg_03));
                break;
            case 4:
                view.setBackgroundColor(context.getResources().getColor(R.color.read_bg_04));
                break;
            case 5:
                view.setBackgroundColor(context.getResources().getColor(R.color.read_bg_06));
                break;
            case 6:
                view.setBackgroundColor(context.getResources().getColor(R.color.read_bg_07));
                break;
            case 7:
                view.setBackgroundColor(context.getResources().getColor(R.color.read_bg_08));
                break;
            case 8:
                view.setBackgroundColor(context.getResources().getColor(R.color.read_bg_09));
                break;

        }
    }


    /**
     * 设置阅读字体颜色
     * @param context
     * @param tv
     * @param color
     */
    public static void setFontColor(Context context,TextView tv, int color)
    {
        switch (color)
        {
            case 0:
                tv.setTextColor(context.getResources().getColor(R.color.brown));
                break;
            case 1:
                tv.setTextColor(context.getResources().getColor(R.color.black));
                break;
            case 2:
                tv.setTextColor(context.getResources().getColor(R.color.gray));
                break;
            case 3:
                tv.setTextColor(context.getResources().getColor(R.color.red));
                break;
            case 4:
                tv.setTextColor(context.getResources().getColor(R.color.colorAccent));
                break;
            case 5:
                tv.setTextColor(context.getResources().getColor(R.color.dark_gray));
                break;

        }
    }

    public static void setBackgroundColor2(Context context,View view, int color)
    {
        switch (color)
        {
            case 0:
                view.setBackgroundColor(context.getResources().getColor(R.color.brown));
                break;
            case 1:
                view.setBackgroundColor(context.getResources().getColor(R.color.black));
                break;
            case 2:
                view.setBackgroundColor(context.getResources().getColor(R.color.gray));
                break;
            case 3:
                view.setBackgroundColor(context.getResources().getColor(R.color.red));
                break;
            case 4:
                view.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                break;
            case 5:
                view.setBackgroundColor(context.getResources().getColor(R.color.dark_gray));
                break;
        }
    }

    public static void setFontSpacing(TextView tv,float fontSpacing)
    {
        tv.setLineSpacing(0,fontSpacing);
    }
}


















