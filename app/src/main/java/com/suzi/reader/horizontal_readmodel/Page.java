package com.suzi.reader.horizontal_readmodel;

import java.util.List;

/**
 * Created by Suzi on 2016/12/24.
 * 用来存放一页的数据
 */

public class Page
{
    /*
    用来标识当前页是哪一页，第一页，中间页，最后一页，第一页且是最后一页
     */
    public static final int FIRST_PAGE = 0;
    public static final int MIDDLE_PAGE = 1;
    public static final int LAST_PAGE = 2;
    public static final int FIRST_AND_LAST_PAGE = 3;

    /*
    标题、换行、换段落的标记
     */
    public static final String TITLE_TAG = "@";
    public static final String ROW_TAG = "#";
    public static final String PAR_TAG = "%";

    private List<String> lines; // 用来存放当前页所有的行，没一行都是一条字符串
    private int pageState = 0; // 标识当前页是第几页
//    private int readPosition = 0; // 当前章所阅读到的地方


    public Page()
    {
    }

    public Page(List<String> lines, int pageState)
    {
        this.lines = lines;
        this.pageState = pageState;
    }

    public List<String> getLines()
    {
        return lines;
    }

    public void setLines(List<String> lines)
    {
        this.lines = lines;
    }

    public int getPageState()
    {
        return pageState;
    }

    public void setPageState(int pageState)
    {
        this.pageState = pageState;
    }
}














