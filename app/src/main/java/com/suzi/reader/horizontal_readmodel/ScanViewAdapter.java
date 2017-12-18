package com.suzi.reader.horizontal_readmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.suzi.reader.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ScanViewAdapter extends PageAdapter
{
    private Context context;
    private List<Page> pageList;

    // 当前小说对应的url
    private String novelUrl;

    /*
    绘制相关
     */
    // 与屏幕边缘的间距
    private int marginWidth, marginHeight;
    // 屏幕大小
    private int mWidth, mHeight;
    // 行间距
    private int lineSpace;
    // 阅读背景
    private int pageBg;
    // 字体大小
    private int fontSize, titleFontSize, hintFontSize;
    // 字体颜色
    private int fontColor, titleFontColor, hintFontColor;
    // 当前绘制的y坐标
    private int y = 0;

    // 当前绘制章节的章节标题
    private String title = "";

    // 绘制时间相关
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

    // 监听器
    private OnReadStateChangeListener listener;

    public ScanViewAdapter(Context context, List<Page> pageList)
    {
        this.context = context;
        this.pageList = pageList;

    }


    public void init()
    {
        int dpFont = 16;
        marginWidth = ScreenUtils.dpToPxInt(16);
        marginHeight = ScreenUtils.dpToPxInt(16);
        mWidth = ScreenUtils.getScreenWidth();
        mHeight = ScreenUtils.getScreenHeight();
        lineSpace = ScreenUtils.dpToPxInt(8);
        fontSize = ScreenUtils.dpToPxInt(dpFont);
        titleFontSize = ScreenUtils.dpToPxInt(1.3f * dpFont);
        hintFontSize = ScreenUtils.dpToPxInt(0.8f * dpFont);
        pageBg = context.getResources().getColor(R.color.white);
        fontColor = context.getResources().getColor(R.color.brown);
        titleFontColor = context.getResources().getColor(R.color.black);
        hintFontColor = context.getResources().getColor(R.color.dark_gray);

        Log.i("LineSpace", lineSpace + "");
        Log.i("fontSize", fontSize + "");
    }


    public void setData(List<Page> pageList)
    {
//        this.pageList = pageList;
    }


    public void addContent(View view, int position)
    {
        if (position < 1 || position > getCount())
        {
            return;
        }

        ImageView imageview = (ImageView) view.findViewById(R.id.imageview_scanview);

        Bitmap b = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(b);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(fontColor);
        paint.setTextSize(fontSize);

        Paint titlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        titlePaint.setColor(titleFontColor);
        titlePaint.setTextSize(titleFontSize);

        Paint hintPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        hintPaint.setColor(hintFontColor);
        hintPaint.setTextSize(hintFontSize);

        // 绘制背景
        canvas.drawColor(pageBg);

        // 绘制顶部提示内容
        y += marginHeight + ScreenUtils.getStatusBarHeight(context);
        // 判断是不是第一页，第一页就不绘制
        if (pageList.get(position - 1).getPageState() != 1)
        {
            canvas.drawText(title, marginWidth, y, hintPaint);
        }
        y += 2 * lineSpace;
        y += hintFontSize;

        if (pageList.get(position - 1).getLines().size() > 0)
        {
            for (String line : pageList.get(position - 1).getLines())
            {
                Log.i("View_Y", y + "");
                if (line.endsWith(Page.TITLE_TAG))
                {
                    title = line.substring(0, line.length() - 1);
                    canvas.drawText(title, marginWidth, y, titlePaint);
                    y += 3 * lineSpace + titleFontSize;
                } else if (line.endsWith(Page.PAR_TAG))
                {
                    if (line.startsWith(" "))
                    {
                        canvas.drawText(line.substring(2, line.length() - 1), marginWidth + 2 *
                                fontSize, y, paint);
                    } else
                    {
                        canvas.drawText(line.substring(0, line.length() - 1), marginWidth, y,
                                paint);
                    }
                    y += 2 * lineSpace + fontSize;
                } else
                {
                    if (line.startsWith(" "))
                    {
                        canvas.drawText(line.substring(2, line.length()), marginWidth + 2 *
                                fontSize, y, paint);
                    } else
                    {
                        canvas.drawText(line, marginWidth, y, paint);
                    }
                    y += lineSpace + fontSize;
                }
            }
        }

        // 绘制底部提示信息(页码，时间)
        String pageNumber = pageList.get(position - 1).getPageState() + "/" + pageList.size();
        String mTime = dateFormat.format(new Date());

        y = mHeight - marginHeight - hintFontSize + 2 * lineSpace;
        canvas.drawText(mTime, marginWidth, y, hintPaint);
        canvas.drawText(pageNumber, mWidth - marginWidth - 2 * hintFontSize, y, hintPaint);

        y = 0;
        imageview.setImageBitmap(b);


    }

    public int getCount()
    {
        return pageList.size();
    }

    public View getView()
    {
        View view = LayoutInflater.from(context).inflate(R.layout.page_layout, null);
        return view;
    }

    public void setOnReadStateChangeListener(OnReadStateChangeListener listener)
    {
        this.listener = listener;
    }

    void onChapterChanged() {
        if (listener != null)
            listener.onChapterChanged();
    }

    void onPageChanged(int index) {
        if (listener != null)
            listener.onPageChanged(index);
    }

    void onLoadChapterFailure(int chapter) {
        if (listener != null)
            listener.onLoadChapterFailure();
    }


}























