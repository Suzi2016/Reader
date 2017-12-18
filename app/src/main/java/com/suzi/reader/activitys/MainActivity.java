package com.suzi.reader.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suzi.reader.R;
import com.suzi.reader.horizontal_readmodel.ToastUtils;
import com.suzi.reader.service.UpdateNovel;
import com.suzi.reader.utils.ConstantsValues;
import com.suzi.reader.view.BookShelfView;
import com.suzi.reader.view.BookStoreView;
import com.suzi.reader.view.MineView;
import com.suzi.reader.view.MyViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener
{
    private final String TAG = "MainActivity";

    /*
    控件相关
     */
    private TextView tv_main_title;
    private MyViewPager viewpager;
    private RelativeLayout rl_main_bookshelf;
    private RelativeLayout rl_main_bookstore;
    private RelativeLayout rl_main_bookorder;
    private RelativeLayout rl_main_mine;
    private ImageView im_main_bookshelf;
    private ImageView im_main_bookstore;
    private ImageView im_main_bookorder;
    private ImageView im_main_mine;
    private TextView tv_main_bookshelf;
    private TextView tv_main_bookstore;
    private TextView tv_main_bookorder;
    private TextView tv_main_mine;

    // 页面集合
    private List<Fragment> fragmentList;

    // Fragment页面
    private BookShelfView bookShelfView;
    private BookStoreView bookStoreView;
//    private BookOrderView bookOrderView;
    private MineView mineView;

    // 记录系统时间
    private long systemTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();

        // 启动后台更新小说的服务
        //        Intent intent = new Intent();
        //        intent.setAction("suzi.luoluo.updatenovel");
        //        startService(intent);

        //        startService(new Intent(MainActivity.this, UpdateNovel.class));


    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }


    private void bindView()
    {
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        viewpager = (MyViewPager) findViewById(R.id.viewpager);
        rl_main_bookshelf = (RelativeLayout) findViewById(R.id.rl_main_bookshelf);
        rl_main_bookstore = (RelativeLayout) findViewById(R.id.rl_main_bookstore);
        rl_main_bookorder = (RelativeLayout) findViewById(R.id.rl_main_bookorder);
        rl_main_mine = (RelativeLayout) findViewById(R.id.rl_main_mine);
        im_main_bookshelf = (ImageView) findViewById(R.id.im_main_bookshelf);
        im_main_bookstore = (ImageView) findViewById(R.id.im_main_bookstore);
        im_main_bookorder = (ImageView) findViewById(R.id.im_main_bookorder);
        im_main_mine = (ImageView) findViewById(R.id.im_main_mine);
        tv_main_bookshelf = (TextView) findViewById(R.id.tv_main_bookshelf);
        tv_main_bookstore = (TextView) findViewById(R.id.tv_main_bookstore);
        tv_main_bookorder = (TextView) findViewById(R.id.tv_main_bookorder);
        tv_main_mine = (TextView) findViewById(R.id.tv_main_mine);

        /*
        设置点击事件
        主要是底部导航栏，点击切换不同的页面
         */
        rl_main_bookshelf.setOnClickListener(this);
        rl_main_bookstore.setOnClickListener(this);
        rl_main_bookorder.setOnClickListener(this);
        rl_main_mine.setOnClickListener(this);

        // 初始化页面数据
        initList();

        // 设置页面之间不可滑动
        viewpager.setScrollble(false);

        // 设置页面不被销毁
        viewpager.setOffscreenPageLimit(3);

        viewpager.setAdapter(new MyFragmentStatePagerAdapter(getSupportFragmentManager()));

    }


    /**
     * 初始化页面数据
     */
    private void initList()
    {
        fragmentList = new ArrayList<Fragment>();
        bookShelfView = new BookShelfView(this);
        bookStoreView = new BookStoreView(this);
//        bookOrderView = new BookOrderView(this);
        mineView = new MineView(this);
        fragmentList.add(bookShelfView);
        fragmentList.add(bookStoreView);
//        fragmentList.add(bookOrderView);
        fragmentList.add(mineView);
    }


    /**
     * Button点击事件
     *
     * @param v Button
     */
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            // 书架
            case R.id.rl_main_bookshelf:
                changeView(0);
                break;
            // 书库
            case R.id.rl_main_bookstore:
                changeView(1);
                break;
//            // 排行
//            case R.id.rl_main_bookorder:
//                changeView(2);
//                break;
            // 我的
            case R.id.rl_main_mine:
                changeView(2);
                break;
        }
    }


    /**
     * 设置显示的页面
     */
    public void changeView(int i)
    {
        viewpager.setCurrentItem(i, false);
    }


    /**
     * 定义自己的ViewPager适配器
     */
    class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter
    {

        public MyFragmentStatePagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            return fragmentList.get(position);
        }

        @Override
        public int getCount()
        {
            return fragmentList.size();
        }

        @Override
        public void finishUpdate(ViewGroup container)
        {
            super.finishUpdate(container);
            // 设置导航选项按钮的颜色
            int currentItem = viewpager.getCurrentItem();
            switch (currentItem)
            {
                case 0:
                    tv_main_title.setText("书架");
                    changeTextViewColor(tv_main_bookshelf, tv_main_bookstore, tv_main_bookorder,
                            tv_main_mine);
                    im_main_bookshelf.setImageResource(R.drawable.main_bookshelf_clicked);
                    im_main_bookstore.setImageResource(R.drawable.main_bookstore);
                    im_main_bookorder.setImageResource(R.drawable.main_bookorder);
                    im_main_mine.setImageResource(R.drawable.main_mine);
                    break;
                case 1:
                    tv_main_title.setText("书库");
                    changeTextViewColor(tv_main_bookstore, tv_main_bookshelf, tv_main_bookorder,
                            tv_main_mine);
                    im_main_bookshelf.setImageResource(R.drawable.main_bookshelf);
                    im_main_bookstore.setImageResource(R.drawable.main_bookstore_clicked);
                    im_main_bookorder.setImageResource(R.drawable.main_bookorder);
                    im_main_mine.setImageResource(R.drawable.main_mine);
                    break;
//                case 2:
//                    tv_main_title.setText("排行");
//                    changeTextViewColor(tv_main_bookorder, tv_main_bookshelf, tv_main_bookstore,
//                            tv_main_mine);
//                    im_main_bookshelf.setImageResource(R.drawable.main_bookshelf);
//                    im_main_bookstore.setImageResource(R.drawable.main_bookstore);
//                    im_main_bookorder.setImageResource(R.drawable.main_bookorder_clicked);
//                    im_main_mine.setImageResource(R.drawable.main_mine);
//                    break;
                case 2:
                    tv_main_title.setText("我的");
                    changeTextViewColor(tv_main_mine, tv_main_bookshelf, tv_main_bookstore,
                            tv_main_bookorder);
                    im_main_bookshelf.setImageResource(R.drawable.main_bookshelf);
                    im_main_bookstore.setImageResource(R.drawable.main_bookstore);
                    im_main_bookorder.setImageResource(R.drawable.main_bookorder);
                    im_main_mine.setImageResource(R.drawable.main_mine_clicked);
                    break;
                default:
                    break;
            }
        }


        /**
         * 将选中的设置成blue，其他设置成dark_gray
         */
        public void changeTextViewColor(TextView blue, TextView gray_01, TextView gray_02,
                                        TextView gray_03)
        {
            blue.setTextColor(getResources().getColor(R.color.blue));

            gray_01.setTextColor(getResources().getColor(R.color.dark_gray));

            gray_02.setTextColor(getResources().getColor(R.color.dark_gray));

            gray_03.setTextColor(getResources().getColor(R.color.dark_gray));
        }

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ConstantsValues.isFirstLunch = true;
        // 关闭服务
        stopService(new Intent(MainActivity.this, UpdateNovel.class));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (viewpager.getCurrentItem() == 0)
            {
//                /**
//                 * 按返回键不退出应用，只挂起，相当于按Home键
//                 */
//                moveTaskToBack(false);

                // 得到当前系统时间
                long curSystemTime = System.currentTimeMillis();
                // 和记录的系统时间比较，如果差别在200以内，就退出，否则重新设置systemTime
                Log.i(TAG,"curSystemTime:"+curSystemTime);
                Log.i(TAG,"systemTime:"+systemTime);
                if (curSystemTime - systemTime < 2000)
                {
                    finish();
                }else {
                    // 提示再按一次退出
                    ToastUtils.showSingleToast("再按一次退出");
                    systemTime = curSystemTime;
                }


            }else
            {
                // 如果不是显示书架的那页，就退回到书架页面
                changeView(0);
            }
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }


}


















