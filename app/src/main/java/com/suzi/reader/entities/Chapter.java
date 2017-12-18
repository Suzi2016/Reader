package com.suzi.reader.entities;

/**
 * Created by Suzi on 2016/10/25.
 * 用于封装小说的章节
 */

public class Chapter
{
    private int id;  // ID
    private int novelID;  // 在所属小说中的编号
    private String novelUrl;  // 小说url，用于标记章节属于哪本小说
    private String chapterUrl;  // 章节url
    private String chapterName;  // 章节名字
    private String chapterInfo;  // 章节内容

    public Chapter()
    {
    }

    public Chapter(int novelID, String novelUrl, String chapterUrl, String chapterName)
    {
        this.novelID = novelID;
        this.novelUrl = novelUrl;
        this.chapterUrl = chapterUrl;
        this.chapterName = chapterName;
    }

    public Chapter(int novelID, String novelUrl, String chapterUrl, String chapterName, String
            chapterInfo)
    {
        this.novelID = novelID;
        this.novelUrl = novelUrl;
        this.chapterUrl = chapterUrl;
        this.chapterName = chapterName;
        this.chapterInfo = chapterInfo;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getNovelID()
    {
        return novelID;
    }

    public void setNovelID(int novelID)
    {
        this.novelID = novelID;
    }

    public String getNovelUrl()
    {
        return novelUrl;
    }

    public void setNovelUrl(String novelUrl)
    {
        this.novelUrl = novelUrl;
    }

    public String getChapterUrl()
    {
        return chapterUrl;
    }

    public void setChapterUrl(String chapterUrl)
    {
        this.chapterUrl = chapterUrl;
    }

    public String getChapterName()
    {
        return chapterName;
    }

    public void setChapterName(String chapterName)
    {
        this.chapterName = chapterName;
    }

    public String getChapterInfo()
    {
        return chapterInfo;
    }

    public void setChapterInfo(String chapterInfo)
    {
        this.chapterInfo = chapterInfo;
    }
}
















