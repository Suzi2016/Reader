package com.suzi.reader.entities;

/**
 * Created by Suzi on 2016/10/25.
 * 用于封装小说显示信息的，比如搜索结果显示
 */

public class NovelTag
{
    private String url;  // 小说的url
    private String novelName;  // 小说名字
    private String imgUrl;  // 小说封面图片url
    private String desc;  // 小说简介

    public NovelTag()
    {
    }

    public NovelTag(String url, String novelName, String imgUrl, String desc)
    {
        this.url = url;
        this.novelName = novelName;
        this.imgUrl = imgUrl;
        this.desc = desc;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getNovelName()
    {
        return novelName;
    }

    public void setNovelName(String novelName)
    {
        this.novelName = novelName;
    }

    public String getImgUrl()
    {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }
}
