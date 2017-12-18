package com.suzi.reader.entities;

/**
 * Created by Suzi on 2016/10/26.
 * 用来封装小说的详情
 */

public class NovelDetail
{
    private String description;
    private String imageUrl;
    private String category;
    private String author;
    private String update_time;
    private String latest_chapter;
    private String status;

    public NovelDetail()
    {
    }

    public NovelDetail(String description, String imageUrl, String category, String author,
                       String update_time, String latest_chapter, String status)
    {
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.author = author;
        this.update_time = update_time;
        this.latest_chapter = latest_chapter;
        this.status = status;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getUpdate_time()
    {
        return update_time;
    }

    public void setUpdate_time(String update_time)
    {
        this.update_time = update_time;
    }

    public String getLatest_chapter()
    {
        return latest_chapter;
    }

    public void setLatest_chapter(String latest_chapter)
    {
        this.latest_chapter = latest_chapter;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}















