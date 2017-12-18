package com.suzi.reader.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Suzi on 2016/11/24.
 */

public class SharedPreferenceUtils
{
    /**
     * 创建SP文件，初始化数值，主要是阅读相关的参数
     * @param context 上下文
     */
    public static void initSP(Context context)
    {
        try
        {
            SharedPreferences sp = context.getSharedPreferences("read_setting", Context
                    .MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            // 登录次数
            editor.putInt("LoginTime",0);
            // 阅读字体大小
            editor.putInt("FontSize",16);
            // 阅读字体颜色
            editor.putInt("FontColor",0);
            // 阅读背景颜色
            editor.putInt("ReadBackground",0);
            // 行间距
            editor.putFloat("FontSpacing",1.3f);

            editor.apply();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    /**
     * 得到阅读背景颜色
     * @param context 上下文
     * @return
     */
    public static float getFontSpacing(Context context)
    {
        float fontSpacing = 1.3f;
        try
        {
            SharedPreferences sp = context.getSharedPreferences("read_setting", Context
                    .MODE_PRIVATE);
            fontSpacing = sp.getFloat("FontSpacing",1.3f);

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return fontSpacing;
    }


    /**
     * 得到阅读背景颜色
     * @param context 上下文
     * @return
     */
    public static int getReadBackground(Context context)
    {
        int readBackground = 0;
        try
        {
            SharedPreferences sp = context.getSharedPreferences("read_setting", Context
                    .MODE_PRIVATE);
            readBackground = sp.getInt("ReadBackground",0);

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return readBackground;
    }


    /**
     * 得到阅读字体颜色
     * @param context 上下文
     * @return
     */
    public static int getFontColor(Context context)
    {
        int fontColor = 0;
        try
        {
            SharedPreferences sp = context.getSharedPreferences("read_setting", Context
                    .MODE_PRIVATE);
            fontColor = sp.getInt("FontColor",0);

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return fontColor;
    }


    /**
     * 得到登录次数
     * @param context 上下文
     * @return
     */
    public static int getLoginTime(Context context)
    {
        int loginTime = 0;
        try
        {
            SharedPreferences sp = context.getSharedPreferences("read_setting", Context
                    .MODE_PRIVATE);
            loginTime = sp.getInt("LoginTime",0);

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return loginTime;
    }


    /**
     * 得到阅读字体大小
     * @param context 上下文
     * @return
     */
    public static int getFontSize(Context context)
    {
        int fontSize = 16;
        try
        {
            SharedPreferences sp = context.getSharedPreferences("read_setting", Context
                    .MODE_PRIVATE);
            fontSize = sp.getInt("FontSize",16);

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return fontSize;
    }


    /**
     * 修改登录次数
     * @param context
     */
    public static void setLoginTime(Context context)
    {
        try
        {
            SharedPreferences sp = context.getSharedPreferences("read_setting", Context
                    .MODE_PRIVATE);
            int loginTime = sp.getInt("LoginTime",0) + 1;
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("LoginTime",loginTime);
            editor.apply();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 修改字体大小
     * @param context
     */
    public static void setFontSize(Context context,int fontSize)
    {
        try
        {
            SharedPreferences sp = context.getSharedPreferences("read_setting", Context
                    .MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("FontSize",fontSize);
            editor.apply();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 修改字体颜色
     * @param context
     */
    public static void setFontColor(Context context,int fontColor)
    {
        try
        {
            SharedPreferences sp = context.getSharedPreferences("read_setting", Context
                    .MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("FontColor",fontColor);
            editor.apply();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 修改阅读背景颜色
     * @param context
     */
    public static void setReadBackground(Context context,int readBackground)
    {
        try
        {
            SharedPreferences sp = context.getSharedPreferences("read_setting", Context
                    .MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("ReadBackground",readBackground);
            editor.apply();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 修改行间距
     * @param context
     */
    public static void setFontSpacing(Context context,float fontSpacing)
    {
        try
        {
            SharedPreferences sp = context.getSharedPreferences("read_setting", Context
                    .MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putFloat("FontSpacing",fontSpacing);
            editor.apply();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
































