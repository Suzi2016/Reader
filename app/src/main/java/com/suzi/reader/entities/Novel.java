package com.suzi.reader.entities;

/**
 * Created by Suzi on 2016/10/25.
 * 用于封装小说信息
 */

public class Novel
{
    private int id;  // ID
    private String tel;  // 手机号码（相当于账户），用于这本小说标记属于哪一个用户
    private String name;  // 小说名字
    private String url;  // 小说对应的url
    private String readChapter;  // 小说当前阅读的章节的章节名
    private int readChapterID;  // 小说当前阅读的章节的编号
    private String lastChapter;  // 小说最新章节的章节名
    private int lastChapterID;  // 小说最新章节的编号
    private int updates;  // 用来标记是否有更新（可以存放更新的章节数）
    private int chapterNumber;  // 章节的总数目
    private int reads;  // 小说阅读的次数
    private int caches;  // 是否是缓存（要不要加入书架），加入书架：0，缓存：1

    public Novel()
    {
    }

    public Novel(String readChapter, int readChapterID, String lastChapter, int lastChapterID,
                 int chapterNumber,String url)
    {
        this.readChapter = readChapter;
        this.readChapterID = readChapterID;
        this.lastChapter = lastChapter;
        this.lastChapterID = lastChapterID;
        this.chapterNumber = chapterNumber;
        this.url = url;
    }

    public Novel(int reads, String tel, String name, String url, String readChapter, int
            readChapterID, String lastChapter, int lastChapterID, int updates, int chapterNumber)
    {
        this.reads = reads;
        this.tel = tel;
        this.name = name;
        this.url = url;
        this.readChapter = readChapter;
        this.readChapterID = readChapterID;
        this.lastChapter = lastChapter;
        this.lastChapterID = lastChapterID;
        this.updates = updates;
        this.chapterNumber = chapterNumber;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTel()
    {
        return tel;
    }

    public void setTel(String tel)
    {
        this.tel = tel;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getReadChapter()
    {
        return readChapter;
    }

    public void setReadChapter(String readChapter)
    {
        this.readChapter = readChapter;
    }

    public int getReadChapterID()
    {
        return readChapterID;
    }

    public void setReadChapterID(int readChapterID)
    {
        this.readChapterID = readChapterID;
    }

    public String getLastChapter()
    {
        return lastChapter;
    }

    public void setLastChapter(String lastChapter)
    {
        this.lastChapter = lastChapter;
    }

    public int getLastChapterID()
    {
        return lastChapterID;
    }

    public void setLastChapterID(int lastChapterID)
    {
        this.lastChapterID = lastChapterID;
    }

    public int getupdates()
    {
        return updates;
    }

    public void setupdates(int updates)
    {
        this.updates = updates;
    }

    public int getChapterNumber()
    {
        return chapterNumber;
    }

    public void setChapterNumber(int chapterNumber)
    {
        this.chapterNumber = chapterNumber;
    }

    public int getReads()
    {
        return reads;
    }

    public void setReads(int reads)
    {
        this.reads = reads;
    }
}





















