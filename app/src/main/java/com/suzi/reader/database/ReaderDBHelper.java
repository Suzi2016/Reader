package com.suzi.reader.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Suzi on 2016/10/25.
 */

public class ReaderDBHelper extends SQLiteOpenHelper
{

    private static final String DATABASE_NAME = "Reader.db";
    private static final int DATABASE_VERSION = 1;

    /*
    创建数据库表的语句
     */
    // 创建novel表
    private final String CREATE_TABLE_NOVEL = "CREATE TABLE [novel] (\n" +
            "  [id] integer NOT NULL PRIMARY KEY AUTOINCREMENT, \n" +
            "  [tel] text, \n" +
            "  [name] text, \n" +
            "  [url] text, \n" +
            "  [readChapter] text, \n" +
            "  [readChapterID] integer, \n" +
            "  [lastChapter] text, \n" +
            "  [lastChapterID] integer, \n" +
            "  [updates] INTEGER DEFAULT (0), \n" +
            "  [chapterNumber] integer, \n" +
            "  [reads] INTEGER DEFAULT (0),\n" +
            "  isShelf integer);";
    // 创建chapter表
    private final String CREATE_TABLE_CHAPTER = "CREATE TABLE chapter(\n" +
            "id integer primary key autoincrement,\n" +
            "novelID integer,\n" +
            "novelUrl text,\n" +
            "chapterUrl text,\n" +
            "chapterName text,\n" +
            "chapterInfo text\n" +
            ");\n";


    public ReaderDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * 数据库第一次创建时调用此方法
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_NOVEL);
        db.execSQL(CREATE_TABLE_CHAPTER);
    }

    /**
     * 数据库有更新时调用此方法
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
