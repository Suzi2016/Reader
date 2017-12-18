package com.suzi.reader.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suzi.reader.R;
import com.suzi.reader.activitys.AboutActivity;
import com.suzi.reader.activitys.HelpActivity;
import com.suzi.reader.activitys.StatementActivity;
import com.suzi.reader.utils.ReadSettingUtils;
import com.suzi.reader.utils.SharedPreferenceUtils;

/**
 * Created by Suzi on 2016/11/14.
 */

public class MineView extends Fragment
{

    private Context mContext;

    /*
    布局控件
     */
    private LinearLayout ll_mine_font;
    private LinearLayout ll_mine_spacing;
    private LinearLayout ll_mine_bg;
    private LinearLayout ll_mine_help;
    private LinearLayout ll_mine_statement;
    private LinearLayout ll_mine_about;
    private LinearLayout ll_mine_color;

    private TextView tv_mine_font;
    private TextView tv_mine_spacing;
    private TextView tv_mine_bg;
    private TextView tv_mine_quit;
    private TextView tv_mine_color;


    /*
    弹出的Dialog相关的View
     */
    private View dialogView;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    // 字体大小选择dialog
    private TextView tv_fontsize_selector_12;
    private TextView tv_fontsize_selector_14;
    private TextView tv_fontsize_selector_16;
    private TextView tv_fontsize_selector_18;
    private TextView tv_fontsize_selector_20;

    // 字体颜色选择dialog
    private TextView tv_fontcolor_selector_brown;
    private TextView tv_fontcolor_selector_black;
    private TextView tv_fontcolor_selector_gray;
    private TextView tv_fontcolor_selector_red;
    private TextView tv_fontcolor_selector_colorAccent;
    private TextView tv_fontcolor_selector_dark_gray;

    // 行间距选择dialog
    private TextView tv_fontspacing_selector_1;
    private TextView tv_fontspacing_selector_2;
    private TextView tv_fontspacing_selector_3;
    private TextView tv_fontspacing_selector_4;
    private TextView tv_fontspacing_selector_5;

    // 阅读背景选择dialog
    private TextView tv_readbg_selector_0;
    private TextView tv_readbg_selector_1;
    private TextView tv_readbg_selector_2;
    private TextView tv_readbg_selector_3;
    private TextView tv_readbg_selector_4;
    private TextView tv_readbg_selector_5;
    private TextView tv_readbg_selector_6;
    private TextView tv_readbg_selector_7;
    private TextView tv_readbg_selector_8;


    @SuppressLint({"NewApi", "ValidFragment"})
    public MineView()
    {
    }

    @SuppressLint({"NewApi", "ValidFragment"})
    public MineView(Context context)
    {
        super();
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.model_mine, container, false);

        // 绑定控件
        ll_mine_font = (LinearLayout) view.findViewById(R.id.ll_mine_font);
        ll_mine_spacing = (LinearLayout) view.findViewById(R.id.ll_mine_spacing);
        ll_mine_bg = (LinearLayout) view.findViewById(R.id.ll_mine_bg);
        ll_mine_help = (LinearLayout) view.findViewById(R.id.ll_mine_help);
        ll_mine_statement = (LinearLayout) view.findViewById(R.id.ll_mine_statement);
        ll_mine_about = (LinearLayout) view.findViewById(R.id.ll_mine_about);
        ll_mine_color = (LinearLayout) view.findViewById(R.id.ll_mine_color);

        tv_mine_font = (TextView) view.findViewById(R.id.tv_mine_font);
        tv_mine_spacing = (TextView) view.findViewById(R.id.tv_mine_spacing);
        tv_mine_bg = (TextView) view.findViewById(R.id.tv_mine_bg);
        tv_mine_quit = (TextView) view.findViewById(R.id.tv_mine_quit);
        tv_mine_color = (TextView) view.findViewById(R.id.tv_mine_color);

        initViewDate();

        onClickListener();

//        onTouchListener();

        return view;
    }


    /**
     * 点击事件
     */
    private void onClickListener()
    {
        // 设置退出点击事件
        tv_mine_quit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("你确定退出吗?").setTitle("提示").setPositiveButton("确定", new
                        DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        getActivity().finish();
                    }
                }).setNegativeButton("取消", null);
                builder.create().show();
            }
        });

        // 字体大小
        ll_mine_font.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialogView = View.inflate(mContext, R.layout.dialog_fontsize_selector, null);
                builder = new AlertDialog.Builder(mContext);
                builder.setView(dialogView);

                // 设置选择项Item点击事件
                tv_fontsize_selector_12 = (TextView) dialogView.findViewById(R.id
                        .tv_fontsize_selector_12);
                tv_fontsize_selector_14 = (TextView) dialogView.findViewById(R.id
                        .tv_fontsize_selector_14);
                tv_fontsize_selector_16 = (TextView) dialogView.findViewById(R.id
                        .tv_fontsize_selector_16);
                tv_fontsize_selector_18 = (TextView) dialogView.findViewById(R.id
                        .tv_fontsize_selector_18);
                tv_fontsize_selector_20 = (TextView) dialogView.findViewById(R.id
                        .tv_fontsize_selector_20);

                tv_fontsize_selector_12.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        fontSizeItemClick(12);
                    }
                });

                tv_fontsize_selector_14.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        fontSizeItemClick(14);
                    }
                });

                tv_fontsize_selector_16.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        fontSizeItemClick(16);
                    }
                });

                tv_fontsize_selector_18.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        fontSizeItemClick(8);
                    }
                });

                tv_fontsize_selector_20.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        fontSizeItemClick(20);
                    }
                });

                dialog = builder.create();
                dialog.show();
            }
        });

        // 字体颜色
        ll_mine_color.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialogView = View.inflate(mContext, R.layout.dialog_fontcolor_selector, null);
                builder = new AlertDialog.Builder(mContext);
                builder.setView(dialogView);

                // 设置选择项Item点击事件
                tv_fontcolor_selector_black = (TextView) dialogView.findViewById(R.id
                        .tv_fontcolor_selector_black);
                tv_fontcolor_selector_brown = (TextView) dialogView.findViewById(R.id
                        .tv_fontcolor_selector_brown);
                tv_fontcolor_selector_gray = (TextView) dialogView.findViewById(R.id
                        .tv_fontcolor_selector_gray);
                tv_fontcolor_selector_red = (TextView) dialogView.findViewById(R.id
                        .tv_fontcolor_selector_red);
                tv_fontcolor_selector_colorAccent = (TextView) dialogView.findViewById(R.id
                        .tv_fontcolor_selector_colorAccent);
                tv_fontcolor_selector_dark_gray = (TextView) dialogView.findViewById(R.id
                        .tv_fontcolor_selector_dark_gray);

                tv_fontcolor_selector_brown.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        fontColorItemClick(0);
                    }
                });

                tv_fontcolor_selector_black.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        fontColorItemClick(1);
                    }
                });

                tv_fontcolor_selector_gray.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        fontColorItemClick(2);
                    }
                });

                tv_fontcolor_selector_red.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        fontColorItemClick(3);
                    }
                });

                tv_fontcolor_selector_colorAccent.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        fontColorItemClick(4);
                    }
                });

                tv_fontcolor_selector_dark_gray.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        fontColorItemClick(5);
                    }
                });

                dialog = builder.create();
                dialog.show();
            }
        });

        // 行间距
        ll_mine_spacing.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialogView = View.inflate(mContext, R.layout.dialog_fontsapcing_selector, null);
                builder = new AlertDialog.Builder(mContext);
                builder.setView(dialogView);

                // 设置选择项Item点击事件
                tv_fontspacing_selector_1 = (TextView) dialogView.findViewById(R.id
                        .tv_fontspacing_selector_1);
                tv_fontspacing_selector_2 = (TextView) dialogView.findViewById(R.id
                        .tv_fontspacing_selector_2);
                tv_fontspacing_selector_3 = (TextView) dialogView.findViewById(R.id
                        .tv_fontspacing_selector_3);
                tv_fontspacing_selector_4 = (TextView) dialogView.findViewById(R.id
                        .tv_fontspacing_selector_4);
                tv_fontspacing_selector_5 = (TextView) dialogView.findViewById(R.id
                        .tv_fontspacing_selector_5);

                tv_fontspacing_selector_1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        fontSpacingItemClick(1.1f);
                    }
                });

                tv_fontspacing_selector_2.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        fontSpacingItemClick(1.3f);
                    }
                });

                tv_fontspacing_selector_3.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        fontSpacingItemClick(1.5f);
                    }
                });

                tv_fontspacing_selector_4.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        fontSpacingItemClick(1.7f);
                    }
                });

                tv_fontspacing_selector_5.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        fontSpacingItemClick(1.9f);
                    }
                });

                dialog = builder.create();
                dialog.show();
            }
        });

        // 阅读背景
        ll_mine_bg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialogView = View.inflate(mContext, R.layout.dialog_readbg_selector, null);
                builder = new AlertDialog.Builder(mContext);
                builder.setView(dialogView);

                // 设置选择项Item点击事件
                tv_readbg_selector_0 = (TextView) dialogView.findViewById(R.id
                        .tv_readbg_selector_0);
                tv_readbg_selector_1 = (TextView) dialogView.findViewById(R.id
                        .tv_readbg_selector_1);
                tv_readbg_selector_2 = (TextView) dialogView.findViewById(R.id
                        .tv_readbg_selector_2);
                tv_readbg_selector_3 = (TextView) dialogView.findViewById(R.id
                        .tv_readbg_selector_3);
                tv_readbg_selector_4 = (TextView) dialogView.findViewById(R.id
                        .tv_readbg_selector_4);
                tv_readbg_selector_5 = (TextView) dialogView.findViewById(R.id
                        .tv_readbg_selector_5);
                tv_readbg_selector_6 = (TextView) dialogView.findViewById(R.id
                        .tv_readbg_selector_6);
                tv_readbg_selector_7 = (TextView) dialogView.findViewById(R.id
                        .tv_readbg_selector_7);
                tv_readbg_selector_8 = (TextView) dialogView.findViewById(R.id
                        .tv_readbg_selector_8);

                tv_readbg_selector_0.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        readBgItemClick(0);
                    }
                });

                tv_readbg_selector_1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        readBgItemClick(1);
                    }
                });

                tv_readbg_selector_2.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        readBgItemClick(2);
                    }
                });

                tv_readbg_selector_3.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        readBgItemClick(3);
                    }
                });

                tv_readbg_selector_4.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        readBgItemClick(4);
                    }
                });

                tv_readbg_selector_5.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        readBgItemClick(5);
                    }
                });

                tv_readbg_selector_6.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        readBgItemClick(6);
                    }
                });

                tv_readbg_selector_7.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        readBgItemClick(7);
                    }
                });

                tv_readbg_selector_8.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        readBgItemClick(8);
                    }
                });

                dialog = builder.create();
                dialog.show();
            }
        });

        // 帮助
        ll_mine_help.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(mContext, HelpActivity.class));
            }
        });

        // 声明
        ll_mine_statement.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(mContext, StatementActivity.class));
            }
        });

        // 关于
        ll_mine_about.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(mContext, AboutActivity.class));
            }
        });
    }


    /**
     * 字体大小选择Item点击事件要做的工作
     *
     * @param size
     */
    private void fontSizeItemClick(int size)
    {
        SharedPreferenceUtils.setFontSize(mContext, size);
        dialog.dismiss();
        initViewDate();
    }

    /**
     * 字体颜色选择Item点击事件要做的工作
     *
     * @param fontColor
     */
    private void fontColorItemClick(int fontColor)
    {
        SharedPreferenceUtils.setFontColor(mContext, fontColor);
        dialog.dismiss();
        initViewDate();
    }


    /**
     * 行间距选择Item点击事件要做的工作
     *
     * @param fontSpacing
     */
    private void fontSpacingItemClick(float fontSpacing)
    {
        SharedPreferenceUtils.setFontSpacing(mContext, fontSpacing);
        dialog.dismiss();
        initViewDate();
    }


    /**
     * 阅读背景选择Item点击事件要做的工作
     *
     * @param bgColor
     */
    private void readBgItemClick(int bgColor)
    {
        SharedPreferenceUtils.setReadBackground(mContext, bgColor);
        dialog.dismiss();
        initViewDate();
    }


    /**
     * 设置View的OnTouchListener事件
     */
    private void onTouchListener()
    {
        ll_mine_font.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                setViewBackgroundColor(v, event);
                return false;
            }
        });

        ll_mine_color.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                setViewBackgroundColor(v, event);
                return false;
            }
        });

        ll_mine_spacing.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                setViewBackgroundColor(v, event);
                return false;
            }
        });

        ll_mine_bg.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                setViewBackgroundColor(v, event);
                return false;
            }
        });

        ll_mine_help.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                setViewBackgroundColor(v, event);
                return false;
            }
        });

        ll_mine_statement.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                setViewBackgroundColor(v, event);
                return false;
            }
        });

        ll_mine_about.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                setViewBackgroundColor(v, event);
                return false;
            }
        });

//        tv_mine_quit.setOnTouchListener(new View.OnTouchListener()
//        {
//            @Override
//            public boolean onTouch(View v, MotionEvent event)
//            {
//                setViewBackgroundColor(v, event);
//                return false;
//            }
//        });


    }


    public void setViewBackgroundColor(View view, MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            view.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
        } else if (event.getAction() == MotionEvent.ACTION_UP)
        {
            view.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        } else if (event.getAction() == MotionEvent.ACTION_MOVE)
        {
//            if (inRangeOfView(view, event))
//            {
//                view.setBackgroundColor(mContext.getResources().getColor(R.color.white));
//            }
            float x = event.getRawX(); // 获取相对于屏幕左上角的 x 坐标值
            float y = event.getRawY(); // 获取相对于屏幕左上角的 y 坐标值
            RectF rect = calcViewScreenLocation(view);
            boolean isInViewRect = rect.contains(x, y);
            if (! isInViewRect)
            {
                view.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            }
        }
    }


    /**
     * 计算指定的 View 在屏幕中的坐标。
     */
    public static RectF calcViewScreenLocation(View view)
    {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + view.getWidth(), location[1] +
                view.getHeight());
    }



    /**
     * 初始化控件展示的设置参数的值
     */
    public void initViewDate()
    {
        /*
        初始化控件显示的值
        从SP中获取
        */
        // 从SP获取阅读参数并初始化
        int fontSize = SharedPreferenceUtils.getFontSize(mContext);
        int fontColor = SharedPreferenceUtils.getFontColor(mContext);
        int readBg = SharedPreferenceUtils.getReadBackground(mContext);
        float fontSpacing = SharedPreferenceUtils.getFontSpacing(mContext);

        tv_mine_font.setText(fontSize + "号");
        ReadSettingUtils.setBackgroundColor2(mContext, tv_mine_color, fontColor);
        tv_mine_spacing.setText(fontSpacing + "");
        ReadSettingUtils.setBackgroundColor(mContext, tv_mine_bg, readBg);
    }


}














