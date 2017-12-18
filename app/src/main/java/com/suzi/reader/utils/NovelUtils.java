package com.suzi.reader.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.suzi.reader.entities.Chapter;
import com.suzi.reader.entities.ChapterTag;
import com.suzi.reader.entities.NovelDetail;
import com.suzi.reader.entities.NovelTag;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Suzi on 2016/10/25.
 * 用来获取小说信息，包括搜索，更新等
 */

public class NovelUtils
{

    /**
     * 通过关键词搜索，得到一个包含NovelTag的ArrayList
     *
     * @param keyWords 搜索的关键词
     * @return ArrayList<NovelTag>
     */
    public static ArrayList<NovelTag> searchNovelByKeyWords(String keyWords)
    {
        String url = "http://zhannei.baidu.com/cse/search?s=8253726671271885340&entry=1&q=" + keyWords;
        //        Log.i("NovelUtils",url);
        ArrayList<NovelTag> novelTags = new ArrayList<NovelTag>();
        NovelTag novelTag;

        try
        {
            Document doc = Jsoup.connect(url)
                    .timeout(8000)
                    .get();

            Elements links1 = doc.getElementsByClass("game-legend-a");
            Elements links2 = doc.getElementsByClass("result-game-item-desc");
            Elements links3 = doc.getElementsByClass("result-game-item-pic-link-img");

            String novelUrl = "";
            String desc = "";
            String name = "";
            String imgUrl = "";

            for (int i = 0; i < links1.size(); i++)
            {
                novelUrl = links1.get(i).attr("onclick");
                novelUrl = novelUrl.replace("window.location='", "");
                novelUrl = novelUrl.replace("'", "");

                desc = links2.get(i).text();

                name = links3.get(i).attr("alt");
                name = name.replace("<em>", "");
                name = name.replace("</em>", "");

                imgUrl = links3.get(i).attr("src");

                novelTag = new NovelTag(novelUrl, name, imgUrl, desc);
                novelTags.add(novelTag);

                Log.i("小说Url", novelUrl);
                Log.i("小说简介", desc);
                Log.i("小说名字", name);
                Log.i("ImgUrl", imgUrl);

            }


        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return novelTags;
    }


    /**
     * 根据图片url获取Bitmap对象
     *
     * @param url 图片url
     * @return bitmap图片
     */
    public static Bitmap getBitmapByUrl(String url)
    {
        URL myFileURL;
        Bitmap bitmap = null;
        try
        {
            myFileURL = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
            // 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(8000);
            // 连接设置获得数据流
            conn.setDoInput(true);
            // 不使用缓存
            conn.setUseCaches(false);
            // 得到数据流
            InputStream is = conn.getInputStream();
            // 解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            // 关闭数据流
            is.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return bitmap;
    }


    /**
     * 根据novelUrl查询小说简介信息，并返回
     *
     * @param novelUrl
     * @return
     */
    public static NovelDetail getNovelDetail(String novelUrl)
    {
        NovelDetail description = new NovelDetail();

        // 将手机地址改成电脑端地址
        novelUrl = novelUrl.replace("http://m.23us.com", "http://www.23us.com");

        try
        {
            Document doc = Jsoup.connect(novelUrl)
                    .timeout(12000)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36"
                                    + " (KHTML, like Gecko) Chrome/42.0.2311.22 Safari/537.36")
                    .get();

            Elements links = doc.getElementsByTag("head");

            //  将HTML文本转化为字符串
            String s = links.toString();

            /*
            定义正则表达式
             */
            String regex_description = "(.*)property=\"og:description\" content=\"";
            String regex_image = "(.*)property=\"og:image\" content=\"";
            String regex_category = "(.*)name=\"og:novel:category\" content=\"";
            String regex_author = "(.*)name=\"og:novel:author\" content=\"";
            String regex_status = "(.*)name=\"og:novel:status\" content=\"";
            String regex_update_time = "(.*)name=\"og:novel:update_time\" content=\"";
            String regex_latest_chapter_name = "(.*)name=\"og:novel:latest_chapter_name\" " +
                    "content=\"";

            String regex_1 = "\">";

            /*
            截取以目标字符串为首的字符串
             */
            String[] arr_description = s.split(regex_description);
            String[] arr_image = s.split(regex_image);
            String[] arr_category = s.split(regex_category);
            String[] arr_author = s.split(regex_author);
            String[] arr_status = s.split(regex_status);
            String[] arr_update_time = s.split(regex_update_time);
            String[] arr_latest_chapter_name = s.split(regex_latest_chapter_name);


            /*
            将上面截取到的字符串再进一步转化为需要的字符串，并将其存入NovelDetail对象中
             */
            String temp;
            String[] result;

            temp = arr_description[1];
            result = temp.split(regex_1);
            description.setDescription(result[0]);

            temp = arr_image[1];
            result = temp.split(regex_1);
            description.setImageUrl(result[0]);

            temp = arr_category[1];
            result = temp.split(regex_1);
            description.setCategory(result[0]);

            temp = arr_author[1];
            result = temp.split(regex_1);
            description.setAuthor(result[0]);

            temp = arr_status[1];
            result = temp.split(regex_1);
            description.setStatus(result[0]);

            temp = arr_update_time[1];
            result = temp.split(regex_1);
            description.setUpdate_time(result[0]);

            temp = arr_latest_chapter_name[1];
            result = temp.split(regex_1);
            description.setLatest_chapter(result[0]);


        } catch (Exception e)
        {
            e.printStackTrace();
        }
        //        Log.i("Des>>>>>",description.getDescription());
        return description;
    }


    /**
     * @param url 小说url链接
     * @return 小说所有章节url信息和章节名
     */
    public static ArrayList<ChapterTag> getChapterUrlInfo(String url)
    {
        // 将手机地址改成电脑端地址
        url = url.replace("http://m.23us.com", "http://www.23us.com");

        Document doc = null;
        ArrayList<ChapterTag> chapterUrlInfos = new ArrayList<ChapterTag>();
        ChapterTag chapterUrlInfo;

        try
        {
            doc = Jsoup.connect(url)
                    .timeout(8000)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36"
                                    + " (KHTML, like Gecko) Chrome/42.0.2311.22 Safari/537.36")
                    .get();
            Element content = doc.getElementById("at");
            Elements links = content.getElementsByTag("a");

            for (int i = 0; i < links.size(); i++)
            {
                String href = links.get(i).attr("abs:href");  // 获得章节名绝对路径
                String text = links.get(i).text();  // 章节名

                chapterUrlInfo = new ChapterTag(href, text);
                chapterUrlInfos.add(chapterUrlInfo);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return chapterUrlInfos;
    }


    /**
     * 将chapterTags转换为chapters
     */
    public static ArrayList<Chapter> chapterTagsTochapters(ArrayList<ChapterTag> chapterTags,
                                                           String novelUrl)
    {
        ArrayList<Chapter> chapters = new ArrayList<Chapter>();
        Chapter chapter;
        for (int i = 0; i < chapterTags.size(); i++)
        {
            chapter = new Chapter(i, novelUrl, chapterTags.get(i).getUrl(), chapterTags.get(i)
                    .getName());
            chapters.add(chapter);
        }

        return chapters;
    }


    /**
     * @param url 指定章节的url
     * @return 章节内容
     */
    public static String getChapterInfo(String url)
    {
        // 将手机地址改成电脑端地址
        url = url.replace("http://m.23us.com", "http://www.23us.com");

        Document doc = null;
        String novelContent = "";

        Log.i("20161028", url + "aaaaaaaa");

        try
        {
            doc = Jsoup.connect(url)
                    .timeout(8000)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36"
                                    + " (KHTML, like Gecko) Chrome/42.0.2311.22 Safari/537.36")
                    .get();
            Element content = doc.getElementById("contents");
            Elements links = content.getElementsByTag("dd");
            novelContent = links.html();
        } catch (Exception e)
        {
            e.printStackTrace();
            return "获取失败";
        }

        /**
         * 排版，去掉<br>,&nbsp
         */
        novelContent = novelContent.replaceAll("(<br>)|(&nbsp;)", "\n");
        novelContent = novelContent.replaceAll("(\n){3,}", "\n\n");

        return novelContent;
    }


    public ArrayList<ChapterTag> getUpdateChapter(String url, int chapterNumber)
    {
        // 将手机地址改成电脑端地址
        url = url.replace("http://m.23us.com", "http://www.23us.com");


        Log.i("打印url",url);

        Document doc = null;
        ArrayList<ChapterTag> chapterUrlInfos = new ArrayList<ChapterTag>();
        ChapterTag chapterUrlInfo;

        try
        {
            doc = Jsoup.connect(url)
                    .timeout(8000)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36"
                                    + " (KHTML, like Gecko) Chrome/42.0.2311.22 Safari/537.36")
                    .get();
            Element content = doc.getElementById("at");
            Elements links = content.getElementsByTag("a");

            if (links.size() > chapterNumber)
            {
                for (int i = chapterNumber; i < links.size(); i++)
                {
                    String href = links.get(i).attr("abs:href");  // 获得章节名绝对路径
                    String text = links.get(i).text();  // 章节名

                    chapterUrlInfo = new ChapterTag(href, text);
                    chapterUrlInfos.add(chapterUrlInfo);
                }
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return chapterUrlInfos;
    }


    /**
     * 用来判断服务是否运行.
     *
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context context, String className)
    {
        ActivityManager myManager=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService =
                (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(30);
        for(int i = 0 ; i<runningService.size();i++)
        {
            if(runningService.get(i).service.getClassName().toString().equals(className))
            {
                return true;
            }
        }
        return false;
    }


    /**
     * 得到排行榜等榜单的所有小说的名字和url信息
     * @param url
     * @return
     */
    public static ArrayList<ChapterTag> getOrderNovelInfo(String url)
    {
        ArrayList<ChapterTag> chapterTags = new ArrayList<>();
        ChapterTag chapterTag;
        Log.i("NovelUtils",url);
        try
        {
            Document doc = Jsoup.connect(url)
                    .timeout(8000)
                    .userAgent(
                            "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36"
                                    + " (KHTML, like Gecko) Chrome/42.0.2311.22 Safari/537.36")
                    .get();

            Element elements = doc.getElementById("content");

            Elements hinks = elements.getElementsByTag("tr ");

            for (int i = 1; i < hinks.size(); i++)
            {
                Elements elements2 = hinks.get(i).getElementsByClass("L");
                String href = elements2.get(1).html();
                String s1 = "\">";
                String temp = href.split(s1)[0];
                temp = temp.replace("<a href=\"", "");
                temp = temp.replace("\" target=\"_blank", "");

                String text = elements2.get(0).text();
                text = text.replace("[简介]","");

                Log.i("NovelUtils",temp + text);

                chapterTag = new ChapterTag(temp,text);
                chapterTags.add(chapterTag);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return chapterTags;
    }


    /**
     * 得到单一类型的所有小说的名字和url
     * @param url
     * @return
     */
    public static ArrayList<ChapterTag> getAllClassNovelInfo(String url)
    {
        ArrayList<ChapterTag> chapterTags = new ArrayList<>();
        ChapterTag chapterTag;

        try
        {
            Document doc = Jsoup.connect(url)
                    .timeout(8000)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36"
                                    + " (KHTML, like Gecko) Chrome/42.0.2311.22 Safari/537.36")
                    .get();

            Elements elements = doc.getElementsByAttribute("title");
            for (int i = 0; i < elements.size(); i++)
            {
                String href = elements.get(i).attr("abs:href");
                String text = elements.get(i).text();

                chapterTag = new ChapterTag(href,text);
                chapterTags.add(chapterTag);
            }
            System.out.println(elements.size());

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return chapterTags;

    }

}
























