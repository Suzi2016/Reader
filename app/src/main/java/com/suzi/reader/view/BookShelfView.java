package com.suzi.reader.view;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.suzi.reader.R;
import com.suzi.reader.activitys.NovelDetailActivity;
import com.suzi.reader.activitys.ReadNovelActivity;
import com.suzi.reader.activitys.SearchActivity;
import com.suzi.reader.adapter.NovelListAdapter;
import com.suzi.reader.database.ChapterDBManager;
import com.suzi.reader.database.NovelDBManager;
import com.suzi.reader.entities.Chapter;
import com.suzi.reader.entities.ChapterTag;
import com.suzi.reader.entities.Novel;
import com.suzi.reader.utils.NovelUtils;
import com.suzi.reader.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by Suzi on 2016/11/14.
 */

public class BookShelfView extends Fragment
{

    public static final int NO_UPDATE = 0;
    public static final int UPDATE = 1;

    private final String TAG = "BookShelfView";

    private Context mContext;

    /*
    声明控件
     */
    private static PullToRefreshListView lv_bookshelf;


    /*
    ListView头部和尾部
     */
    private View listHeader;
    private View listFooter;

    /*
    ListViwe相关
    */
    private ArrayList<Novel> novels;
    private NovelListAdapter adapter;

    /*
    数据库相关
     */
    private NovelDBManager novelDBManager;
    private ChapterDBManager chapterDBManager;

    // 在刷新时用来标记当前是第几本小说
    private static int tag_which_novel = 0;
    // 在刷新时用来标记有几本小说有更新内容
    private static int updateNovelNumber = 0;
    // 在刷新时用来标记当前已经刷新了几本小说
    private static int updatedNovelNumber = 0;

    private BroadcastReceiver receiver;

    /*
    关于长按Item弹出菜单选项
     */
    private View menuView;
    private TextView tv_shelfmenu_name;
    private TextView bt_shelfmenu_detail;
    private TextView bt_shelfmenu_delete;
    private TextView bt_shelfmenu_download;
    private Button bt_shelfmenu_cancel;
    private AlertDialog menuDialog;
    private AlertDialog deleteDialog;



    /*
    更新小说时，如果有更新，在handler中更新“更新提示”
     */
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if (msg.what == UPDATE)
            {
                lv_bookshelf.onRefreshComplete();
                initAdapt();
                Toast.makeText(mContext, updateNovelNumber+"本小说更新", Toast.LENGTH_SHORT)
                        .show();
            }else if (msg.what == NO_UPDATE)
            {

                lv_bookshelf.onRefreshComplete();
                Toast.makeText(mContext, "暂无更新", Toast.LENGTH_SHORT).show();
            }
        }
    };



    @SuppressLint({"NewApi", "ValidFragment"})
    public BookShelfView()
    {
    }

    @SuppressLint({"NewApi", "ValidFragment"})
    public BookShelfView(Context context)
    {
        super();
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        novelDBManager = NovelDBManager.getInstance(mContext);
        chapterDBManager = ChapterDBManager.getInstance(mContext);

        try
        {
            // 注册广播，用于在后台服务更新小说
            receiver = new MyReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction("suzi.luoluo.myreceiver");
            mContext.registerReceiver(receiver,filter); //有时候会报空指针异常错误
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        /*
        刷新ListView
         */
        initAdapt();

        //  设置第一次启动的时候自动下拉刷新
//        if (ConstantsValues.isFirstLunch)
//        {
//            lv_bookshelf.setRefreshing(true);
//            ConstantsValues.isFirstLunch = false;
//        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        // 关闭广播
        mContext.unregisterReceiver(receiver);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.model_bookshelf, container, false);

        // 绑定控件
        lv_bookshelf = (PullToRefreshListView) view.findViewById(R.id.lv_bookshelf);

        // 设置下拉刷新属性
        initPullToRefresh();

        // 添加头部和尾部
        listHeader = View.inflate(mContext, R.layout.booklists_header, null);
        listFooter = View.inflate(mContext, R.layout.booklists_footer, null);
        // 头部点击事件，点击进入搜索界面
        listHeader.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, SearchActivity.class);
                startActivity(intent);
            }
        });
        // 将pullToRefresh listView转换为普通的listView，然后就可以添加头部了
        ListView lv = lv_bookshelf.getRefreshableView();
        lv.addHeaderView(listHeader);
        lv.addFooterView(listFooter);

        /*
        设置ListView的Item点击事件
         */
        lv_bookshelf.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // 跳转到小说显示页面
                String novelUrl = novels.get(position - 2).getUrl();
                int readChapterID = novels.get(position - 2).getReadChapterID();

                // 滑屏阅读模式
//                Intent intent = new Intent(mContext, HorizontalReadActivity.class);

                // 竖屏阅读模式
                Intent intent = new Intent(mContext, ReadNovelActivity.class);

                intent.putExtra("novelUrl", novelUrl);
                intent.putExtra("readChapterID", readChapterID);
                startActivity(intent);

                // 更新Novel表(数据库中)，表示更新的清零，表示阅读次数的+1处理
                novelDBManager.updateWhenOpenNovel(novelUrl);


            }
        });

        // 设置Item长按事件，弹出删除确认对话框
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
            {
//                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                builder.setMessage("\t\t确定删除本书吗？")
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which)
//                            {
//                                String novelUrl = novels.get(position-2).getUrl();
//                                novelDBManager.delete(novelUrl);
//                                chapterDBManager.delete(novelUrl);
//                                initAdapt();
//                                Toast.makeText(mContext, "已删除", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .setNegativeButton("取消",null);
//                AlertDialog dialog = builder.create();
                //                dialog.show();

                // 初始化小说的信息
                final String novelUrl = novels.get(position-2).getUrl();
                final String novelName = novels.get(position-2).getName();

                menuView = View.inflate(mContext,R.layout.bookshelf_menu,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setView(menuView);
                menuDialog = builder.create();

                Window window = menuDialog.getWindow();
                window.setGravity(Gravity.BOTTOM);  // set dialog at botton
                // set dialog background is transparent
                window.setBackgroundDrawableResource(android.R.color.transparent);

                // 设置菜单顶部显示小说的名字
                tv_shelfmenu_name = (TextView) menuView.findViewById(R.id.tv_shelfmenu_name);
                tv_shelfmenu_name.setText("《"+novelName+"》");

                // 设置菜单点击事件
                bt_shelfmenu_detail = (TextView) menuView.findViewById(R.id.bt_shelfmenu_detail);
                bt_shelfmenu_delete = (TextView) menuView.findViewById(R.id.bt_shelfmenu_delete);
                bt_shelfmenu_download = (TextView) menuView.findViewById(R.id.bt_shelfmenu_download);
                bt_shelfmenu_cancel = (Button) menuView.findViewById(R.id.bt_shelfmenu_cancel);

                // 打开小说详情页
                bt_shelfmenu_detail.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        // 让菜单消失
                        menuDialog.dismiss();

                        Intent intent = new Intent(mContext,NovelDetailActivity.class);
                        intent.putExtra("url",novelUrl);
                        intent.putExtra("name",novelName);
                        startActivity(intent);
                    }
                });

                // 删除小说
                bt_shelfmenu_delete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        // 让菜单消失
                        menuDialog.dismiss();

                        // 弹出dialog提示用户是否删除
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage("\t\t确定删除本书吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        novelDBManager.delete(novelUrl);
                                        chapterDBManager.delete(novelUrl);
                                        initAdapt();
                                        Toast.makeText(mContext, "已删除", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("取消",null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });

                // 缓存整本小说
                bt_shelfmenu_download.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        // 让菜单消失
                        menuDialog.dismiss();

//                        // 在通知栏通知用户开始缓存
//                        UIUtils.showNotification(mContext,1,"小说《"+ novelName +"》开始缓存...");
                        // 弹出Toast提示用户开始缓存
                        Toast.makeText(mContext, "小说《"+ novelName +"》开始缓存...", Toast.LENGTH_SHORT).show();
                        new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                // 得到所有需要缓存的章节的url
                                ArrayList<String> chapterUrls = chapterDBManager
                                        .getChapterUrlOfInfoIsNull(novelUrl);
                                String chapterInfo = "";
                                for (int i = 0;i<chapterUrls.size();i++)
                                {
                                    chapterInfo = NovelUtils.getChapterInfo(chapterUrls.get(i));
                                    chapterDBManager.updateChapterInfo(chapterInfo,chapterUrls.get(i));
                                }
                                // 在通知栏通知用户已经缓存完成
                                UIUtils.showNotification(mContext,1,"小说《"+novelName+"》已经缓存完毕");

                            }
                        }).start();
                    }
                });

                // 取消
                bt_shelfmenu_cancel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        // 让菜单消失
                        menuDialog.dismiss();
                    }
                });

                menuDialog.show();

                return true;
            }

        });



        return view;
    }


    /**
     * 设置ListView下拉刷新属性
     * 刷新所做的事：更新小说
     */
    private void initPullToRefresh()
    {
        ILoadingLayout iLoadingLayout = lv_bookshelf.getLoadingLayoutProxy(true, false);
        iLoadingLayout.setPullLabel("下拉刷新");
        iLoadingLayout.setRefreshingLabel("正在刷新...");
        iLoadingLayout.setReleaseLabel("释放立即刷新");

        lv_bookshelf.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>()
        {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView)
            {
                updateNovelNumber = 0;
                updatedNovelNumber = 0;
                tag_which_novel = -1;
                // 如果novels.size()==0,说明没有小说，不作刷新操作
                if (novels.size() == 0)
                {
                    lv_bookshelf.onRefreshComplete();
                    return;
                }
                for (int i = 0; i < novels.size(); i++)
                {

                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            tag_which_novel++;
                            int i = tag_which_novel;
                            Log.i("开启了一个线程",tag_which_novel+"");
                            ArrayList<ChapterTag> chapterTags;
                            NovelUtils utils = new NovelUtils();
                            chapterTags = utils.getUpdateChapter(novels.get(i).getUrl(),
                                    novels.get(i).getChapterNumber());
                            if (chapterTags.size() > 0)
                            {
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
                            Log.i("updatedNovelNumber",updatedNovelNumber+"");

                            // 判断当前是否是最后一本小说，如果是，那就在handler中更新UI
                            if (updatedNovelNumber == (novels.size()))
                            {
                                Message msg = new Message();
                                if (updateNovelNumber > 0)
                                {
                                    msg.what = UPDATE;
                                }else {
                                    msg.what = NO_UPDATE;
                                }
                                handler.sendMessage(msg);
                                Log.i("MainActivity","发送了Handler消息");
                            }

                        }
                    }).start();
                }

            }
        });

    }

    private void initAdapt()
    {

        try
        {
            // 读取数据库文件，赋值给novels
            novels = novelDBManager.getNovels();
            // 初始化Adapter
            adapter = new NovelListAdapter(mContext, R.layout.listview_novellist, novels);
            // 绑定Adapter
            lv_bookshelf.setAdapter(adapter);
        } catch (Exception e)
        {
            e.printStackTrace();
            Log.i(TAG,"initAdapter.......");
            /*
            如果报错，那就推迟1000ms再次运行
             */
            SystemClock.sleep(1000);
            // 读取数据库文件，赋值给novels
            novels = novelDBManager.getNovels();
            // 初始化Adapter
            adapter = new NovelListAdapter(mContext, R.layout.listview_novellist, novels);
            // 绑定Adapter
            lv_bookshelf.setAdapter(adapter);
        }
    }


    /*
    接受service发送的广播，判断是否有小说更新，如果有更新就刷新UI
     */
    public class MyReceiver extends BroadcastReceiver
    {

        public MyReceiver()
        {
        }

        @Override
        public void onReceive(Context context, Intent intent)
        {
            int num = intent.getIntExtra("updateNumber",0);
            if (num > 0)
            {
                initAdapt();
            }
            context.unregisterReceiver(this);
        }

    }



}
















