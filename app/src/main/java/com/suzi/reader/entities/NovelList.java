package com.suzi.reader.entities;

/**
 * Created by Suzi on 2016/10/26.
 * 用于封装书架列表的listview显示对象
 */

public class NovelList
{
    private String novelName;
    private boolean isShowNewImage = false;
    private String latest;
    private String read;

    public NovelList()
    {
    }

    public NovelList(String novelName, boolean isShowNewImage, String latest, String read)
    {
        this.novelName = novelName;
        this.isShowNewImage = isShowNewImage;
        this.latest = latest;
        this.read = read;
    }

    public String getNovelName()
    {
        return novelName;
    }

    public void setNovelName(String novelName)
    {
        this.novelName = novelName;
    }

    public boolean isShowNewImage()
    {
        return isShowNewImage;
    }

    public void setShowNewImage(boolean showNewImage)
    {
        isShowNewImage = showNewImage;
    }

    public String getLatest()
    {
        return latest;
    }

    public void setLatest(String latest)
    {
        this.latest = latest;
    }

    public String getRead()
    {
        return read;
    }

    public void setRead(String read)
    {
        this.read = read;
    }
}
