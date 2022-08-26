package com.tarzan.cms.utils;

import com.tarzan.cms.modules.admin.model.biz.Article;
import com.tarzan.cms.modules.admin.service.biz.ArticleService;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文章采集
 * @author tarzan
 * @date 2021/5/31
 */
@Component
@AllArgsConstructor
public class ArticleCollect {

    private final ArticleService articleService;


    //网站地址
    private static String webUrl="https://blog.csdn.net/weixin_40986713";

    public static void main(String[] args) {
        collect();
    }


    public static void  collect(){
        int pageNum=0;
        while (true){
            pageNum++;
            if(!readPage(webUrl,pageNum)){
                break;
            }
        }
    }


    /**
     * @param url 访问路径
     * @return
     */
    public static Document getDocument(String url) {
        try {
            //5000是设置连接超时时间，单位ms
            return Jsoup.connect(url).timeout(5000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static Article readArticle(String url) {
        Article article=new Article();
        Document doc=  getDocument(url);
        //获取文章标题
        Elements title = doc.select("h1[id=articleContentId]");
        Elements readNum = doc.select("span[class=read-count]");
        System.out.println(title.text()+"   阅读数："+readNum.text());
        return article;
    }

    public static boolean readPage(String webUrl,int pageNum) {
        Document doc = getDocument(webUrl+"/article/list/"+pageNum);
        // 获取目标HTML代码
        Elements elements = doc.select("[class=article-list]");
        //文章列表
        Elements articles = elements.select("[class=article-item-box csdn-tracking-statistics]");
        if (articles.size() == 0) {
            return false;
        }
        articles.forEach(e -> {
            String url = e.select("a").attr("href");
            String readnum = e.select("span[class=read-num]").get(0).text();
            if(Integer.valueOf(readnum)<1000){
                readArticle(url);
            }
            try {
                Integer[] times=new Integer[]{10000,20000,30000};
                //等待3秒
                Thread.sleep(Arrays.stream(times).findAny().get());
            } catch (InterruptedException interruptedException) {
                System.out.println("线程中断故障");
            }
        });

        return true;
    }

}
