package com.suzi.reader.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.suzi.reader.database.ChapterDBManager;
import com.suzi.reader.database.NovelDBManager;
import com.suzi.reader.entities.Chapter;
import com.suzi.reader.entities.ChapterTag;
import com.suzi.reader.entities.Novel;
import com.suzi.reader.utils.NovelUtils;
import com.suzi.reader.utils.UIUtils;

import java.util.ArrayList;

public class UpdateNovel extends Service
{

    // 在刷新时用来标记当前是第几本小说
    private static int tag_which_novel = 0;
    // 在刷新时用来标记有几本小说有更新内容
    private static int updateNovelNumber = 0;
    // 在刷新时用来标记当前已经刷新了几本小说
    private static int updatedNovelNumber = 0;
    // 在刷新时用来记录更新的小说的名字
    private static String updateNovelName;

    public UpdateNovel()
    {
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // 访问网络获取更新
        new Thread(new Runnable()
        {
            NovelDBManager novelDBManager = NovelDBManager.getInstance(UpdateNovel.this);
            ChapterDBManager chapterDBManager = ChapterDBManager.getInstance(UpdateNovel.this);
            ArrayList<Novel> novels;

            @Override
            public void run()
            {
                while (true)
                {
                    try
                    {
                        novels = novelDBManager.getNovels();

                        updateNovelNumber = 0;
                        updatedNovelNumber = 0;
                        tag_which_novel = -1;

                        for (int i = 0; i < novels.size(); i++)
                        {
                            new Thread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    tag_which_novel++;
                                    int i = tag_which_novel;
                                    ArrayList<ChapterTag> chapterTags;
                                    NovelUtils utils = new NovelUtils();
                                    chapterTags = utils.getUpdateChapter(novels.get(i).getUrl(),
                                            novels.get(i).getChapterNumber());
                                    if (chapterTags.size() > 0)
                                    {
                                        // 记录更新的小说的名字
                                        updateNovelName = novels.get(i).getName();
                                        updateNovelNumber++;
                                        // 将chapterTags转化为Chapters
                                        ArrayList<Chapter> chapters = NovelUtils.chapterTagsTochapters
                                                (chapterTags, novels.get(i).getUrl());
                                        for (int j = 1;j<=chapters.size();j++)
                                        {
                                            chapters.get(j-1).setNovelID(novels.get(i).getLastChapterID()
                                                    +j);
                                        }
                                        // 将chapters存到数据库
                                        chapterDBManager.insertManyRecord(chapters);

                                        // 更新novel表中updates
                                        novelDBManager.updateUpdates(
                                                chapterTags.size(),
                                                novels.get(i).getUrl());
                                        // 更新novel表中的lastChapter[ID]
                                        novelDBManager.updateLast(
                                                chapters.get(chapters.size()-1).getChapterName(),
                                                novels.get(i).getLastChapterID()+chapters.size(),
                                                novels.get(i).getUrl());
                                        // 更新novel表中的chapterNumber
                                        novelDBManager.updateChapterNumber(
                                                novels.get(i).getChapterNumber()+chapters.size(),
                                                novels.get(i).getUrl());

                                    }
                                    // 更新"已经刷新了的小说数目"
                                    updatedNovelNumber++;

                                    // 判断当前是否是最后一本小说
                                    if (updatedNovelNumber == (novels.size()))
                                    {
                                        if (updateNovelNumber > 0)
                                        {
                                            String notificationMsg;
                                            if (updateNovelNumber == 1)
                                            {
                                                notificationMsg = "《" + updateNovelName +"》有更新";
                                            }else {
                                                notificationMsg = "《" + updateNovelName +"》等"+
                                                        updateNovelNumber +
                                                        "本小说有更新";
                                            }
                                            // 在通知栏通知
                                            UIUtils.showNotification(UpdateNovel.this,2,notificationMsg);
                                            // 发送广播
                                            Intent intent=new Intent();
                                            intent.putExtra("updateNumber", updateNovelNumber);
                                            intent.setAction("suzi.luoluo.myreceiver");
                                            sendBroadcast(intent);
                                            Log.i("UpdateNovel","发送了广播");
                                        }
                                    }

                                }
                            }).start();
                        }

                        Log.i("UpdateNovel","开始刷新了");
                        // 每隔180秒刷新一次
                        Thread.sleep(180 * 1000);

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }

            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }
}
