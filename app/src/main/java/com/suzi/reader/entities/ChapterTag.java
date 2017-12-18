package com.suzi.reader.entities;

/**
 * Created by Suzi on 2016/10/25.
 * 用于封装小说章节的章节名和url
 */

public class ChapterTag
{
    private String url;  // 章节的url
    private String name;  // 章节的名字

    public ChapterTag()
    {
    }

    public ChapterTag(String url, String name)
    {
        this.url = url;
        this.name = name;
    }

    /**
     * @param urlAndName 格式：url##name
     * 按格式传入，然后分解出两个字符串，即url和name
     */
    public ChapterTag(String urlAndName)
    {
        String[] arr = urlAndName.split("##");
        this.url = arr[0];
        this.name = arr[1];
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        String s = "";
        return url + "##" + name;
    }


}
