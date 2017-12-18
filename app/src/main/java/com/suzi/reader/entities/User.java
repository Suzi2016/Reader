package com.suzi.reader.entities;

/**
 * Created by Suzi on 2016/10/25.
 * 用于封装用户信息，账号和密码，以及昵称
 */

public class User
{
    private int id;  // ID
    private String tel; // 电话号码
    private String name;  // 昵称
    private String password;  // 密码

    public User()
    {
    }

    public User(String tel, String name, String password)
    {
        this.tel = tel;
        this.name = name;
        this.password = password;
    }

    public User(int id, String tel, String name, String password)
    {
        this.id = id;
        this.tel = tel;
        this.name = name;
        this.password = password;
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

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
















