package com.suzi.reader.horizontal_readmodel;

import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suzi on 2016/12/24.
 * 字符串工具类
 * 1.格式化字符串，将小说章节文本格式化，去掉多余的符号，添加段落标识
 * 2.将一章的全部内容，包括标题，转换为Page
 */

public class StringUtils
{


    /**
     * 格式化小说章节文本内容
     * 去掉多余的空格，去掉多余的换行
     *
     * @param str
     * @return
     */
    public static String formatContent(String str)
    {
        str = str.replaceAll("[ ]*", "");// 消除空格
        str = str.replaceAll("(\n)+", Page.PAR_TAG);
        // 消除开头的段落标记
        if (str.startsWith(Page.PAR_TAG))
        {
            str = str.substring(1,str.length());
        }


        return str;
    }


    public static List<Page> chapterToPage(Chapter chapter, int marginWidth, int marginHeight,
                                           int font, int lineSpace)
    {
        Page page;
        List<Page> pageList = new ArrayList<>();

        /*
        字体相关，传入的参数都是以dp为单位
         */
        int mFont = ScreenUtils.dpToPxInt(font);
        int titleFont = ScreenUtils.dpToPxInt(1.3f * font);
        int hintFont = ScreenUtils.dpToPxInt(0.8f * font);
        int mLineSpace = ScreenUtils.dpToPxInt(lineSpace);


        marginWidth = ScreenUtils.dpToPxInt(marginWidth);  // 屏幕margin宽度
        marginHeight = ScreenUtils.dpToPxInt(marginHeight); // 屏幕margin高度

        int mWidth = ScreenUtils.getScreenWidth(); // 屏幕总宽度
        int mHeight = ScreenUtils.getScreenHeight(); // 屏幕总高度

        int mVisibleWidth = mWidth - 2 * marginWidth; // 绘制文本可用宽度

        // 初始绘制的y坐标
        int Init_Y = marginHeight + hintFont + 2 * mLineSpace;
        // y坐标的最大值，如果还大的话就绘制内容会超出屏幕
        int Max_Y = mHeight - (marginHeight + hintFont + 2 * mLineSpace + mFont);

        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mFont);

        String textStr = "字字字字字字字字字字字字字字字字字字字字字字字字字字字字字字字字字字";

        // 一行所能容纳的最大字数
        int linesWordSize = mPaint.breakText(textStr, true, mVisibleWidth, null);

        List<String> lines = new ArrayList<>();
        List<String> pageLines = new ArrayList<>();
        String content = chapter.getContent();
        content = formatContent(content);


        // 将chapter分成一个个的段落
        String[] arr = content.split(Page.PAR_TAG);
        // 循环所有段落，将每一个段落分成一行行
        for (int i = 0; i < arr.length; i++)
        {
            arr[i] = "  " + arr[i]; // 在段首加上缩进
            while (arr[i].length() > 0)
            {
                if (arr[i].length() > linesWordSize)
                {
                    /*
                    一行的开头不出现标点符号
                    判断下一行的开头是不是标点符号，如果是，当前行就少添加一个字，用来填充到下一行的开头
                     */
                    String lastWord = arr[i].substring(linesWordSize, linesWordSize + 1);
                    if (TextUtils.equals(lastWord, "，") || TextUtils.equals(lastWord, "。") ||
                            TextUtils.equals(lastWord, "？") || TextUtils.equals(lastWord, "！") ||
                            TextUtils.equals(lastWord, "”") || TextUtils.equals(lastWord, "》") ||
                            TextUtils.equals(lastWord, "）"))
                    {
                        lines.add(arr[i].substring(0, linesWordSize - 1));
                        arr[i] = arr[i].substring(linesWordSize - 1);
                    } else
                    {
                        lines.add(arr[i].substring(0, linesWordSize));
                        arr[i] = arr[i].substring(linesWordSize);
                    }
                } else
                {
                    lines.add(arr[i]);
                    break;
                }
            }
            lines.set(lines.size() - 1, lines.get(lines.size() - 1) + Page.PAR_TAG);
        }


        pageLines.add(chapter.getTitle() + Page.TITLE_TAG);
        int y = Init_Y + titleFont + 4 * lineSpace;
        int pageNumber = 1; // 页数
        // 按照一页能够容纳的行数，分页
        for (int i = 0; i < lines.size(); i++)
        {
            if (y <= Max_Y)
            {
                Log.i("Y",y+"");
                // 判断是不是段落最后一行,如果是,行间距+2*lineSpace
                if (lines.get(i).endsWith(Page.PAR_TAG))
                {
                    pageLines.add(lines.get(i));
                    y += mFont + 2 * mLineSpace;
                } else
                {
                    pageLines.add(lines.get(i));
                    y += mFont + mLineSpace;
                }
            } else
            {
                page = new Page(new ArrayList<>(pageLines), pageNumber++);
                pageList.add(page);
                pageLines.clear();
                y = Init_Y;
                i--;
            }

            if (i == lines.size()-1)
            {
                page = new Page(new ArrayList<>(pageLines), pageNumber);
                pageList.add(page);
            }

        }


        return pageList;
    }
}


















