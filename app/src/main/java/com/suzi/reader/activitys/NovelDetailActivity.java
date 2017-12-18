package com.suzi.reader.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.suzi.reader.R;
import com.suzi.reader.database.ChapterDBManager;
import com.suzi.reader.database.NovelDBManager;
import com.suzi.reader.entities.Chapter;
import com.suzi.reader.entities.ChapterTag;
import com.suzi.reader.entities.Novel;
import com.suzi.reader.entities.NovelDetail;
import com.suzi.reader.utils.NovelUtils;
import com.suzi.reader.utils.UIUtils;

import java.util.ArrayList;

public class NovelDetailActivity extends Activity implements View.OnClickListener
{

    private String TAG = "NovelDetailActivity";

    private static final int SUCCESS = 1;
    private static final int START_ADD = 2;
    private static final int END_ADD = 3;

    // 布局控件
    private Button bt_detail_back;
    private Button bt_detail_try_to_read;
    private Button bt_detail_add;
    private Button bt_detail_download;

    private ImageView iv_detail_image;

    private TextView tv_detail_name;
    private TextView tv_detail_author;
    private TextView tv_detail_status;
    private TextView tv_detail_latestchapter;
    private TextView tv_detail_updatetime;
    private TextView tv_detail_description;

    /*
    从上一个Activity传过来的值：小说的url,小说的名字
     */
    private String url = "";
    private String name = "";

    private NovelDetail novelDetail;
    private Bitmap bitmap;

    /*
    数据库相关
     */
    private NovelDBManager novelDBManager;
    private ChapterDBManager chapterDBManager;

    private AlertDialog loadDialog;


    /*
    在Handler中更新UI（小说详情显示）
     */
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);

            switch (msg.what)
            {
                // 更新UI显示
                case SUCCESS:
                    iv_detail_image.setImageBitmap(bitmap);
                    tv_detail_author.setText(novelDetail.getAuthor() + "/" + novelDetail
                            .getCategory());
                    tv_detail_status.setText(novelDetail.getStatus());
                    tv_detail_latestchapter.setText("最新章节：" + novelDetail.getLatest_chapter());
                    tv_detail_updatetime.setText(novelDetail.getUpdate_time() + " 更新");
                    tv_detail_description.setText(novelDetail.getDescription());
                    break;
                // 创建一个Dialog
                case START_ADD:
                    View view = View.inflate(NovelDetailActivity.this, R.layout.dialog_loading,
                            null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(NovelDetailActivity
                            .this);
                    loadDialog = builder.create();
                    loadDialog.setView(view);
                    loadDialog.setCanceledOnTouchOutside(false);
                    // 屏蔽返回键
                    loadDialog.setOnKeyListener(new DialogInterface.OnKeyListener()
                    {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
                        {
                            if (keyCode == KeyEvent.KEYCODE_BACK)
                            {
                                return true;
                            }
                            return false;
                        }
                    });
                    loadDialog.show();
                    break;
                // 让创建的Dialog消失
                case END_ADD:
                    loadDialog.dismiss();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UIUtils.setActivityNoTitle(this);
        setContentView(R.layout.activity_novel_detail);

        /*
        从上一个Activity传入小说的Url
         */
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        name = intent.getStringExtra("name");

        /*
        手机搜索得到的小说url链接是手机网站地址，要将其转换为电脑网站地址
         */
        url = url.replace("http://m.23wx.com", "http://www.23wx.com");

        /*
        初始化数据库
         */
        chapterDBManager = ChapterDBManager.getInstance(this);
        novelDBManager = NovelDBManager.getInstance(this);

        /*
        初始化布局控件，设置监听事件等
         */
        bindView();

        /*
        根据url获取小说的详情，并在Handler中更新UI界面
         */
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                novelDetail = NovelUtils.getNovelDetail(url);
                bitmap = NovelUtils.getBitmapByUrl(novelDetail.getImageUrl());
                Message msg = new Message();
                msg.what = SUCCESS;
                handler.sendMessage(msg);
            }
        }).start();

    }

    private void bindView()
    {

        bt_detail_back = (Button) findViewById(R.id.bt_detail_back);
        bt_detail_try_to_read = (Button) findViewById(R.id.bt_detail_try_to_read);
        bt_detail_add = (Button) findViewById(R.id.bt_detail_add);
        bt_detail_download = (Button) findViewById(R.id.bt_detail_download);

        iv_detail_image = (ImageView) findViewById(R.id.iv_detail_image);

        tv_detail_name = (TextView) findViewById(R.id.tv_detail_name);
        tv_detail_status = (TextView) findViewById(R.id.tv_detail_status);
        tv_detail_author = (TextView) findViewById(R.id.tv_detail_author);
        tv_detail_latestchapter = (TextView) findViewById(R.id.tv_detail_latestchapter);
        tv_detail_updatetime = (TextView) findViewById(R.id.tv_detail_updatetime);
        tv_detail_description = (TextView) findViewById(R.id.tv_detail_description);

        /*
        设置小说的名字
         */
        tv_detail_name.setText(name);

        /*
        给Button设置点击监听事件
         */
        bt_detail_back.setOnClickListener(this);
        bt_detail_try_to_read.setOnClickListener(this);
        bt_detail_add.setOnClickListener(this);
        bt_detail_download.setOnClickListener(this);

        /*
        判断当前小说是否已经在书架，如果已经在了就把屏蔽试读、加入书架Button
         */
        if (novelDBManager.isExistOfShelf(url))
        {
            // 设置添加按钮不可点击,text设置为“已添加”
            bt_detail_add.setClickable(false);
            bt_detail_add.setBackgroundColor(getResources().getColor(R.color.gray));
            bt_detail_add.setText("已添加");
        }

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            // 返回
            case R.id.bt_detail_back:
                finish();
                break;
            // 点击试读
            case R.id.bt_detail_try_to_read:
                /*
                1.判断小说信息是否已经添加到了数据库
                2.如果没有添加到数据库，先将小说章节信息等添加到数据库（Novel表和Chapter表）和下面添加到书架一样，
                  但是isShelf=0，tel=0
                3.打开ReadNovelActivity
                4.在退出的时候提示是否添加到书架
                 */
                if (!novelDBManager.isExistOfNovel(url))
                {
                    novelDBManager.insert("0", name, url, "0");
                    /*
                    开一个线程，更新新添加的小说的所有章节信息
                     */
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // 发Handler消息，弹出Dialog提示正在加载
                            Message msg = new Message();
                            msg.what = START_ADD;
                            handler.sendMessage(msg);


                            // 拿到所有章节信息
                            ArrayList<ChapterTag> chapterTags = NovelUtils.getChapterUrlInfo(url);

                            // 更新Novel表中的数据,Ps:每次使用数据库都要先打开
                            int readChapterID = 0;
                            String readChapter = chapterTags.get(readChapterID).getName();
                            int lastChapterID = chapterTags.size() - 1;
                            String lastChapter = chapterTags.get(lastChapterID).getName();
                            int chapterNumber = lastChapterID + 1;
                            Novel novel = new Novel(readChapter, readChapterID, lastChapter,
                                    lastChapterID, chapterNumber, url);
                            Log.i("添加小说后更新novel表", readChapter + "...." + lastChapter);
                            novelDBManager.updateWhenAddNovel(novel);

                            // 将chapterTags转化为Chapters
                            ArrayList<Chapter> chapters = NovelUtils.chapterTagsTochapters
                                    (chapterTags, url);
                            // 将chapters存到数据库
                            chapterDBManager.insertManyRecord(chapters);

                            // 发Handler消息，让弹出的提示正在加载的Dialog消失
                            Message msg2 = new Message();
                            msg2.what = END_ADD;
                            handler.sendMessage(msg2);

                            Intent intent = new Intent(NovelDetailActivity.this,
                                    ReadNovelActivity.class);
                            intent.putExtra("novelUrl", url);
                            startActivity(intent);

                        }
                    }).start();
                } else
                {
                    Intent intent = new Intent(NovelDetailActivity.this, ReadNovelActivity.class);
                    intent.putExtra("novelUrl", url);
                    startActivity(intent);
                }
                break;
            // 添加到书架
            case R.id.bt_detail_add:

                //TODO 很重要! 需要判断是否有网络，没有就退出


                /*
                判断是否在数据库中，如果在了就设置isShelf=1，
                没有就从网络获取数据添加到数据库中
                 */
                if (novelDBManager.isExistOfNovel(url))
                {
                    novelDBManager.setNovelIsShelf(url);
                } else
                {
                    // 往数据库里添加一条数据
                    //TODO 电话号码要改
                    novelDBManager.insert("13945617845", name, url, "1");
                    /*
                    开一个线程，更新新添加的小说的所有章节信息
                    */
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // 发Handler消息，弹出Dialog提示正在加载
                            Message msg = new Message();
                            msg.what = START_ADD;
                            handler.sendMessage(msg);

                            // 拿到所有章节信息
                            ArrayList<ChapterTag> chapterTags = NovelUtils.getChapterUrlInfo(url);

                            // 更新Novel表中的数据,Ps:每次使用数据库都要先打开
                            int readChapterID = 0;
                            String readChapter = chapterTags.get(readChapterID).getName();
                            int lastChapterID = chapterTags.size() - 1;
                            String lastChapter = chapterTags.get(lastChapterID).getName();
                            int chapterNumber = lastChapterID + 1;
                            Novel novel = new Novel(readChapter, readChapterID, lastChapter,
                                    lastChapterID, chapterNumber, url);
                            Log.i("添加小说后更新novel表", readChapter + "...." + lastChapter);
                            novelDBManager.updateWhenAddNovel(novel);

                            // 将chapterTags转化为Chapters
                            ArrayList<Chapter> chapters = NovelUtils.chapterTagsTochapters
                                    (chapterTags, url);
                            // 将chapters存到数据库
                            chapterDBManager.insertManyRecord(chapters);

                            // 发Handler消息，让弹出的提示正在加载的Dialog消失
                            Message msg2 = new Message();
                            msg2.what = END_ADD;
                            handler.sendMessage(msg2);

                        }
                    }).start();
                }

                // 设置添加按钮不可点击,text设置为“已添加”
                bt_detail_add.setClickable(false);
                bt_detail_add.setBackgroundColor(getResources().getColor(R.color.gray));
                bt_detail_add.setText("已添加");

                break;
            // 点击缓存小说，并添加到书架
            case R.id.bt_detail_download:
                /*
                首先将小说添加到书架，然后后台开启线程缓存所有章节信息
                 */

                /*
                判断是否在数据库中，如果在了就设置isShelf=1，
                没有就从网络获取数据添加到数据库中
                 */
                if (novelDBManager.isExistOfNovel(url))
                {
                    novelDBManager.setNovelIsShelf(url);
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // 得到所有需要缓存的章节的url
                            ArrayList<String> chapterUrls = chapterDBManager.getChapterUrlOfInfoIsNull(url);
                            // 根据章节url获取所有的章节内容
                            ArrayList<String> chapterInfos = new ArrayList<String>();
                            String chapterInfo = "";
                            for (int i = 0;i<chapterUrls.size();i++)
                            {
                                chapterInfo = NovelUtils.getChapterInfo(chapterUrls.get(i));
                                chapterDBManager.updateChapterInfo(chapterInfo,chapterUrls.get(i));
//                                chapterInfos.add(chapterInfo);
                            }
//                            // 将章节内容存到数据库中
//                            chapterDBManager.updateManyChapterInfo(chapterUrls,chapterInfos);
                            // 在通知栏通知用户已经缓存完成
                            UIUtils.showNotification(NovelDetailActivity.this,1,"小说《"+name+"》已经缓存完毕");

                        }
                    }).start();

                } else
                {
                    // 往数据库里添加一条数据
                    //TODO 电话号码要改
                    novelDBManager.insert("13945617845", name, url, "1");
                    /*
                    开一个线程，更新新添加的小说的所有章节信息
                    */
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // 发Handler消息，弹出Dialog提示正在加载
                            Message msg = new Message();
                            msg.what = START_ADD;
                            handler.sendMessage(msg);

                            // 拿到所有章节信息
                            ArrayList<ChapterTag> chapterTags = NovelUtils.getChapterUrlInfo(url);

                            // 更新Novel表中的数据,Ps:每次使用数据库都要先打开
                            int readChapterID = 0;
                            String readChapter = chapterTags.get(readChapterID).getName();
                            int lastChapterID = chapterTags.size() - 1;
                            String lastChapter = chapterTags.get(lastChapterID).getName();
                            int chapterNumber = lastChapterID + 1;
                            Novel novel = new Novel(readChapter, readChapterID, lastChapter,
                                    lastChapterID, chapterNumber, url);
                            Log.i("添加小说后更新novel表", readChapter + "...." + lastChapter);
                            novelDBManager.updateWhenAddNovel(novel);

                            // 将chapterTags转化为Chapters
                            ArrayList<Chapter> chapters = NovelUtils.chapterTagsTochapters
                                    (chapterTags, url);
                            // 将chapters存到数据库
                            chapterDBManager.insertManyRecord(chapters);

                            // 发Handler消息，让弹出的提示正在加载的Dialog消失
                            Message msg2 = new Message();
                            msg2.what = END_ADD;
                            handler.sendMessage(msg2);



                            // 得到所有需要缓存的章节的url
                            ArrayList<String> chapterUrls = chapterDBManager.getChapterUrlOfInfoIsNull(url);
                            // 根据章节url获取所有的章节内容
                            ArrayList<String> chapterInfos = new ArrayList<String>();
                            String chapterInfo = "";
                            for (int i = 0;i<chapterUrls.size();i++)
                            {
                                chapterInfo = NovelUtils.getChapterInfo(chapterUrls.get(i));
                                chapterDBManager.updateChapterInfo(chapterInfo,chapterUrls.get(i));
//                                chapterInfos.add(chapterInfo);
                            }
                            // 将章节内容存到数据库中
//                            chapterDBManager.updateManyChapterInfo(chapterUrls,chapterInfos);
                            // 在通知栏通知用户已经缓存完成
                            UIUtils.showNotification(NovelDetailActivity.this,1,"小说《"+name+"》已经缓存完毕");

                        }
                    }).start();
                }

                // 在通知栏通知用户开始缓存
                UIUtils.showNotification(NovelDetailActivity.this,1,"小说《"+ name +"》开始缓存...");


                // 设置添加按钮不可点击,text设置为“已添加”
                bt_detail_add.setClickable(false);
                bt_detail_add.setBackgroundColor(getResources().getColor(R.color.gray));
                bt_detail_add.setText("已添加");



                break;
        }
    }


    public void setNotification(int id,String msg)
    {
        Intent notificationIntent =new Intent(this, MainActivity.class); // 点击该通知后要跳转的Activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("落落")
                .setContentText(msg)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setOngoing(true)
                .build();

        // 创建一个NotificationManager的引用
        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

        notificationManager.notify(id,notification);

    }
}






















