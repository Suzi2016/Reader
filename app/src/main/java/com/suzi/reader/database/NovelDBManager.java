package com.suzi.reader.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suzi.reader.entities.Novel;

import java.util.ArrayList;

/**
 * Created by Suzi on 2016/10/25.
 * 封装了操作数据库的方法，可以直接调用
 */

public class NovelDBManager
{
    private ReaderDBHelper readerDBHelper;
    private SQLiteDatabase db;
    private Context mContext;

    /*
    SQL语句
     */
    // 插入一条记录(电话，小说名字，小说url,是否显示到书架
    private final String INSERT = "insert into novel(tel,name,url,isShelf) values(?,?,?,?);";
    // 查询全部
    private final String SELECT_ALL = "select * from novel where isShelf=1;";
    // 当添加一本小说时，更改novel的数据:readChapter、readChapterID、lastChapter、lastChapterID、chapterNumber
    private final String UPDATE_WHEN_ADD = "update novel set readChapter=?,readChapterID=?,lastChapter=?,lastChapterID=?,chapterNumber=? where url=?;";
    // 当点击阅读一本小说时，更改小说的updates和reads
    private final String UPDATE_WHEN_OPEN = "update novel set updates=0,reads=reads+1 where " +
            "url=?;";
    // 查询lastChapterID
    private final String SELECT_LAST_CHAPTER_ID = "select lastChapterID from novel where url=?;";
    // 查询readChapterID
    private final String SELECT_READ_CHAPTER_ID = "select readChapterID from novel where url=?;";
    // 更新readChapter和readChapterID
    private final String UPDATE_READ = "update novel set readChapter=?,readChapterID=? where url=?;";
    // 更新lastChapter和lastChapterID
    private final String UPDATE_LAST = "update novel set lastChapter=?,lastChapterID=? where url=?;";
    // 更新updates
    private final String UPDATE_UPDATES = "update novel set updates=? where url=?;";
    // 更新chapterNumber
    private final String UPDATE_CHAPTER_NUMBER = "update novel set chapterNumber=? where url=?;";
    // 查询指定小说是否在当前数据库中
    private final String SELECT_NOVEL_EXIST = "select count(*) from novel where url=?";
    // 查询指定小说是否在当前数据库中且isShelf=1
    private final String SELECT_NOVEL_SHELF = "select count(*) from novel where url=? and " +
            "isShelf=1";
    // 删除一条记录
    private final String DELETE = "delete from novel where url=?;";
    // 设置小说是否在书架显示
    private final String UPDATE_SHELF = "update novel set isShelf=? where url=?;";
    // 更新电话号码(单个)
    private final String UPDATE_TEL = "update novel set tel=? where url=?;";
    // 更新所有小说的电话号码
    private final String UPDATE_ALL_TEL = "update novel set tel=?;";




    /**
     * 私有构造方法，与下面的getInstance()方法结合，只能有一个实例被使用
     * 在使用之前调用openNovelDBManager()方法打开数据库
     */
    private NovelDBManager(Context context)
    {
        this.mContext = context;
        this.readerDBHelper = new ReaderDBHelper(context);
        this.db = readerDBHelper.getWritableDatabase();
    }

    private static NovelDBManager novelDBManager = null;

    public static NovelDBManager getInstance(Context context)
    {
        if (novelDBManager == null)
        {
            novelDBManager = new NovelDBManager(context);
        }
        return novelDBManager;
    }

//    public void openNovelDBManager(Context context)
//    {
//        /*
//        判断数据库是否打开，打开了就说明在使用，那就等其关闭，然后再打开使用
//         */
//        if (novelDBManager.db != null)
//        {
//            while (novelDBManager.db.isOpen())
//            {
//                //                SystemClock.sleep(4000);
//                //                novelDBManager.
//                int i = 0;
//                i++;
//                Log.i("数据库里", i + "................");
//            }
//        }
//
//        novelDBManager.db.isDbLockedByCurrentThread();
//        novelDBManager.mContext = context;
//        novelDBManager.readerDBHelper = new ReaderDBHelper(context);
//        novelDBManager.db = readerDBHelper.getWritableDatabase();
//    }


    /**
     * 插入一条记录
     * 电话，小说名字，小说url,是否显示到书架
     */
    public void insert(String tel, String name, String url,String isShelf)
    {
        if (db.isOpen())
        {
            Object[] objs = {tel, name, url,isShelf};
            db.execSQL(INSERT, objs);
        }
        
    }

    /**
     * 查询全部，返回一个ArrayList<Novel>
     */
    public ArrayList<Novel> getNovels()
    {
        ArrayList<Novel> novels = new ArrayList<Novel>();
        Novel novel;

        if (db.isOpen())
        {
            novels = new ArrayList<Novel>();
            String tel;
            String name;
            String url;
            String readChapter;
            String lastChapter;
            int readChapterID;
            int lastChapterID;
            int chapterNumber;
            int updates;
            int reads;

            Cursor cursor = db.rawQuery(SELECT_ALL, null);
            if (cursor.getCount() > 0)
            {
                for (int i = 0; i < cursor.getCount(); i++)
                {
                    cursor.moveToNext();
                    tel = cursor.getString(cursor.getColumnIndex("tel"));
                    name = cursor.getString(cursor.getColumnIndex("name"));
                    url = cursor.getString(cursor.getColumnIndex("url"));
                    readChapter = cursor.getString(cursor.getColumnIndex("readChapter"));
                    lastChapter = cursor.getString(cursor.getColumnIndex("lastChapter"));

                    readChapterID = cursor.getInt(cursor.getColumnIndex("readChapterID"));
                    lastChapterID = cursor.getInt(cursor.getColumnIndex("lastChapterID"));
                    updates = cursor.getInt(cursor.getColumnIndex("updates"));
                    chapterNumber = cursor.getInt(cursor.getColumnIndex("chapterNumber"));
                    reads = cursor.getInt(cursor.getColumnIndex("reads"));

                    novel = new Novel(reads, tel, name, url, readChapter, readChapterID,
                            lastChapter, lastChapterID, updates, chapterNumber);

                    novels.add(novel);
                }
            }
            cursor.close();
            
        }

        return novels;
    }


    /**
     * 当添加一本小说时，更改novel的数据
     */
    public void updateWhenAddNovel(Novel novel)
    {
        if (db.isOpen())
        {
            db.execSQL(UPDATE_WHEN_ADD, new String[]{novel.getReadChapter(), String.valueOf(novel.getReadChapterID()),
                    novel.getLastChapter(), String.valueOf(novel.getLastChapterID()), String
                    .valueOf(novel.getChapterNumber()), novel.getUrl()});
            Log.i("数据库中", "成功了");
            
        }
    }

    /**
     * 当打开一本小说时，更改novel的数据
     */
    public void updateWhenOpenNovel(String url)
    {
        if (db.isOpen())
        {
            Object[] objs = {url};
            db.execSQL(UPDATE_WHEN_OPEN, objs);
            
        }
    }


    /**
     * 得到lastChapterId
     */
    public int getLastChapterID(String novelUrl)
    {
        int id = 0;
        if (db.isOpen())
        {
            Cursor cursor = db.rawQuery(SELECT_LAST_CHAPTER_ID,new String[]{novelUrl});
            if (cursor.getCount() > 0)
            {
                cursor.moveToNext();
                id = cursor.getInt(cursor.getColumnIndex("lastChapterID"));
            }
            cursor.close();
            
        }

        return id;
    }


    /**
     * 得到readChapterId
     */
    public int getReadChapterID(String novelUrl)
    {
        int id = 0;
        if (db.isOpen())
        {
            Cursor cursor = db.rawQuery(SELECT_READ_CHAPTER_ID,new String[]{novelUrl});
            if (cursor.getCount() > 0)
            {
                cursor.moveToNext();
                id = cursor.getInt(cursor.getColumnIndex("readChapterID"));
            }
            cursor.close();

        }

        return id;
    }


    /**
     * 更新readChapter和readChapterID
     */
    public void updateRead(String readChapter,int readChapterID,String novelUrl)
    {
        if (db.isOpen())
        {
            Log.i("readChapter",readChapter);
            Log.i("readChapterID",readChapterID+"");
            Log.i("novelUrl",novelUrl);
            db.execSQL(UPDATE_READ,new String[]{readChapter,String.valueOf(readChapterID),
                    novelUrl});
            Log.i("updateRead","打印语句。。。。。。");
            
        }
    }

    /**
     * 更新lastChapter和lastChapterID
     */
    public void updateLast(String lastChapter,int lastChapterID,String novelUrl)
    {
        if (db.isOpen())
        {
            db.execSQL(UPDATE_LAST,new String[]{lastChapter,String.valueOf(lastChapterID),novelUrl});
        }
    }


    /**
     * 更新updates
     */
    public void updateUpdates(int updates,String novelUrl)
    {
        if (db.isOpen())
        {
            db.execSQL(UPDATE_UPDATES,new String[]{String.valueOf(updates),novelUrl});
        }
    }


    /**
     * 更新chapterNumber
     */
    public void updateChapterNumber(int chapterNumber,String novelUrl)
    {
        if (db.isOpen())
        {
            db.execSQL(UPDATE_CHAPTER_NUMBER,new String[]{String.valueOf(chapterNumber),novelUrl});
        }
    }


    /**
     * 根据指定小说的url判断当前小说是否在数据库中
     * @return 返回false，如过不在数据库中
     */
    public boolean isExistOfNovel(String url)
    {
        boolean b = false;
        if (db.isOpen())
        {
            Cursor cursor = db.rawQuery(SELECT_NOVEL_EXIST,new String[]{url});
            if (cursor.getCount() > 0)
            {
                cursor.moveToNext();
                int count = cursor.getInt(cursor.getColumnIndex("count(*)"));
                if (count > 0)
                {
                    b = true;
                }
            }
            cursor.close();
        }

        return b;
    }


    /**
     * 根据指定小说的url判断当前小说是否已经添加到书架
     * @return 返回false，如果没有添加到书架
     */
    public boolean isExistOfShelf(String url)
    {
        boolean b = false;
        if (db.isOpen())
        {
            Cursor cursor = db.rawQuery(SELECT_NOVEL_SHELF,new String[]{url});
            if (cursor.getCount() > 0)
            {
                cursor.moveToNext();
                int count = cursor.getInt(cursor.getColumnIndex("count(*)"));
                if (count > 0)
                {
                    b = true;
                }
            }
            cursor.close();
        }

        return b;
    }


    /**
     * 删除一条记录
     */
    public void delete(String url)
    {
        if (db.isOpen())
        {
            db.execSQL(DELETE,new String[]{url});
        }
    }


    /**
     * 设置小说在书架显示
     */
    public void setNovelIsShelf(String url)
    {
        if (db.isOpen())
        {
            Object[] objs = {"1",url};
            db.execSQL(UPDATE_SHELF, objs);
        }
    }


    /**
     * 设置小说不在书架显示
     */
    public void setNovelNotShelf(String url)
    {
        if (db.isOpen())
        {
            Object[] objs = {"0",url};
            db.execSQL(UPDATE_SHELF, objs);
        }
    }


    /**
     * 更新小说对应的电话号码
     */
    public void setTel(String tel,String url)
    {
        if (db.isOpen())
        {
            Object[] objs = {tel,url};
            db.execSQL(UPDATE_TEL, objs);
        }
    }


    /**
     * 更新所有小说对应的电话号码
     */
    public void setAllTel(String tel)
    {
        if (db.isOpen())
        {
            Object[] objs = {tel};
            db.execSQL(UPDATE_ALL_TEL, objs);
        }
    }


}














