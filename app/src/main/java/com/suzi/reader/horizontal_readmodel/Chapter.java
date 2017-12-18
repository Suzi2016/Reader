package com.suzi.reader.horizontal_readmodel;

/**
 * Created by Suzi on 2016/12/24.
 * 用来存放一章的信息
 */

public class Chapter
{
    private String title; // 章节标题
    private String content; // 章节内容
    private int readPosition; // 当前章节阅读到的地方


    public Chapter()
    {
    }

    public Chapter(String title, String content, int readPosition)
    {
        this.title = title;
        this.content = content;
        this.readPosition = readPosition;
    }

    public int getReadPosition()
    {
        return readPosition;
    }

    public void setReadPosition(int readPosition)
    {
        this.readPosition = readPosition;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
}
















