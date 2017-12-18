package com.suzi.reader.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.suzi.reader.entities.Chapter;

import java.util.ArrayList;

/**
 * Created by Suzi on 2016/10/25.
 * 封装了操作数据库的方法，可以直接调用
 */

public class ChapterDBManager
{
    private ReaderDBHelper readerDBHelper;
    private SQLiteDatabase db;
    private Context mContext;
    
    
    /*
    SQL语句
     */
    // 插入一条记录:在当前小说中的编号、小说的url、章节url、章节名字
    private final String INSERT = "insert into chapter(novelID,novelUrl,chapterUrl,chapterName) " +
            "values(?,?,?,?);";
    // 查询章节名，条件：小说url和章节在所在小说的编号
    private final String SELECT_CHAPTER_NAME = "select chapterName from chapter where " +
            "novelUrl=? and novelID=?;";
    // 查询章节内容，条件：小说url和章节在所在小说的编号
    private final String SELECT_CHAPTER_INFO = "select chapterInfo from chapter where " +
            "novelUrl=? and novelID=?;";
    // 查询指定小说的章节数
    private final String SELECT_NOVEL_COUNT = "select count(*) from chapter where novelUrl=?;";
    // 查询章节url
    private final String SELECT_CHAPTER_URL = "select chapterUrl from chapter where " +
            "novelUrl=? and novelID=?;";
    // 更新小说章节的信息：chapterInfo
    private final String UPDATE_CHAPTER_INFO = "update chapter set chapterInfo=? where chapterUrl=?;";
    // 查询指定小说的所有章节名
    private final String SELECT_ALL_CHAPTER_NAME = "select chapterName from chapter where " +
            "novelUrl=?;";
    // 删除指定小说的所有记录
    private final String DELETE = "delete from chapter where novelUrl=?;";
    // 获取指定小说所有chapterInfo为空的chapterUrl
    private final String SELECT_CHAPTERURL_INFOISNULL = "select chapterUrl from chapter where chapterInfo is null and novelUrl=?;";



    /**
     * 私有构造方法，与下面的getInstance()方法结合，只能有一个实例被使用
     * 在使用之前调用openChapterDBManager()方法打开数据库
     */
    private ChapterDBManager(Context context)
    {
        this.mContext = context;
        this.readerDBHelper = new ReaderDBHelper(context);
        this.db = readerDBHelper.getWritableDatabase();
    }

    private static ChapterDBManager chapterDBManager = null;

    public static ChapterDBManager getInstance(Context context)
    {
        if (chapterDBManager == null)
        {
            chapterDBManager = new ChapterDBManager(context);
        }
        return chapterDBManager;
    }

//    public void openChapterDBManager(Context context)
//    {
//                /*
//        判断数据库是否打开，打开了就说明在使用，那就等其关闭，然后再打开使用
//         */
//        if (chapterDBManager.db != null)
//        {
//            while (chapterDBManager.db.isOpen())
//            {
//                //                SystemClock.sleep(4000);
//                //                novelDBManager.
//                int i = 0;
//                i++;
//                Log.i("数据库里", i + "................");
//            }
//        }
//
//        chapterDBManager.mContext = context;
//        chapterDBManager.readerDBHelper = new ReaderDBHelper(context);
//        chapterDBManager.db = readerDBHelper.getWritableDatabase();
//    }


    /**
     * 插入一条记录
     */
    public void insertSingleRecord(Chapter chapter)
    {
        if (db.isOpen())
        {
            Object[] objs = {chapter.getNovelID(),chapter.getNovelUrl(),chapter.getChapterUrl(),
                    chapter.getChapterName()};
            db.execSQL(INSERT,objs);
        }
        
    }


    /**
     * 插入多条记录
     */
    public void insertManyRecord(ArrayList<Chapter> chapters)
    {
        if (db.isOpen())
        {
            /*
            使用事务，如果不使用的话，没一次插入操作都虎会默认当做一次事务，这样就会相当耗时
            如果使用事务，那么就会只当做一次事务
            事务的执行过程可能会抛出异常，所以要try-catch
             */
            // 开启事务
            db.beginTransaction();

            Chapter chapter;
            try
            {
                for (int i = 0;i<chapters.size();i++)
                {
                    chapter = chapters.get(i);
                    Object[] objs = {chapter.getNovelID(),chapter.getNovelUrl(),chapter.getChapterUrl(),
                            chapter.getChapterName()};
                    db.execSQL(INSERT,objs);
                }

                // 设置事务成功
                db.setTransactionSuccessful();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            // 设置事务结束
            db.endTransaction();
        }
        
    }


    /**
     * 得到章节名
     */
    public String getNovelName(String novelUrl,int novelID)
    {
        String name = "";
        if (db.isOpen())
        {
            Cursor cursor = db.rawQuery(SELECT_CHAPTER_NAME,new String[]{novelUrl,novelID+""});
            if (cursor.getCount() > 0)
            {
                cursor.moveToNext();
                name = cursor.getString(cursor.getColumnIndex("chapterName"));
            }
            cursor.close();
            
        }

        return name;
    }


    /**
     * 得到章节内容
     */
    public String getNovelInfo(String novelUrl,int novelID)
    {
        String info = "";
        if (db.isOpen())
        {
            Cursor cursor = db.rawQuery(SELECT_CHAPTER_INFO,new String[]{novelUrl,String
                    .valueOf(novelID)});
            if (cursor.getCount() > 0)
            {
                cursor.moveToNext();
                info = cursor.getString(cursor.getColumnIndex("chapterInfo"));
            }
            cursor.close();
            
        }

        return info;
    }


    /**
     * 得到指定小说的章节数
     */
    public int getChapterCount(String novelUrl)
    {
        int count = 0;
        if (db.isOpen())
        {
            Cursor cursor = db.rawQuery(SELECT_NOVEL_COUNT,new String[]{novelUrl});
            if (cursor.getCount() > 0)
            {
                cursor.moveToNext();
                count = cursor.getInt(cursor.getColumnIndex("count(*)"));
            }
            cursor.close();
            
        }

        return count;
    }


    /**
     * 得到章节的url
     * @param novelID 章节在小说中的编号
     */
    public String getChapterUrl(String novelUrl,int novelID)
    {
        String url = "";
        if (db.isOpen())
        {
            Cursor cursor = db.rawQuery(SELECT_CHAPTER_URL,new String[]{novelUrl,novelID+""});
            if (cursor.getCount() > 0)
            {
                cursor.moveToNext();
                url = cursor.getString(cursor.getColumnIndex("chapterUrl"));
            }
            cursor.close();
            
        }

        return url;
    }


    /**
     * 更新章节内容
     * @param chapterInfo 要更新的章节的内容
     * @param chapterUrl 章节url
     */
    public void updateChapterInfo(String chapterInfo,String chapterUrl)
    {
        if (db.isOpen())
        {
            db.execSQL(UPDATE_CHAPTER_INFO,new String[]{chapterInfo,chapterUrl});
            
        }
    }


    /**
     * 得到指定小说的所有章节名
     */
    public ArrayList<String> getAllChapterName(String novelUrl)
    {
        ArrayList<String> names = new ArrayList<String>();
        String name;
        if (db.isOpen())
        {
            Cursor cursor = db.rawQuery(SELECT_ALL_CHAPTER_NAME,new String[]{novelUrl});
            if (cursor.getCount() > 0)
            {
                for (int i = 0;i<cursor.getCount();i++)
                {
                    cursor.moveToNext();
                    name = cursor.getString(cursor.getColumnIndex("chapterName"));
                    names.add(name);
                }
            }
            cursor.close();
        }

        return names;
    }


    /**
     * 删除指定小说的所有记录
     */
    public void delete(String novelUrl)
    {
        if (db.isOpen())
        {
            db.execSQL(DELETE,new String[]{novelUrl});
        }
    }


    /**
     * 返回数据库中指定小说所有章节内容为空的章节url
     * @param novelUrl 小说url
     * @return ArrayList<String>,包含章节url的ArrayList
     */
    public ArrayList<String> getChapterUrlOfInfoIsNull(String novelUrl)
    {
        ArrayList<String> chapterUrls = new ArrayList<String>();
        String chapterUrl = "";

        if (db.isOpen())
        {
            Cursor cursor = db.rawQuery(SELECT_CHAPTERURL_INFOISNULL,new String[]{novelUrl});
            if (cursor.getCount() > 0)
            {
                for (int i = 0;i<cursor.getCount();i++)
                {
                    cursor.moveToNext();
                    chapterUrl = cursor.getString(cursor.getColumnIndex("chapterUrl"));
                    chapterUrls.add(chapterUrl);
                }
                cursor.close();
            }
        }

        return chapterUrls;
    }


    /**
     * 更新多条记录的章节信息
     * @param chapterUrls 对应的章节url的ArrayList<String>
     * @param chapterInfos 包含所有要更新的章节内容的ArrayList<String>
     */
    public void updateManyChapterInfo(ArrayList<String> chapterUrls,ArrayList<String> chapterInfos)
    {
        if (db.isOpen())
        {
            // 开启事务
            db.beginTransaction();

            try
            {
                // 更新数据
                for (int i = 0;i<chapterInfos.size();i++)
                {
                    db.execSQL(UPDATE_CHAPTER_INFO,new String[]{chapterInfos.get(i),chapterUrls
                            .get(i)});
                }
                db.setTransactionSuccessful();
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            db.endTransaction();
        }
    }


}


















