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


    public  void  collect(){
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



    public  Article readArticle(String url) {
        Article article=new Article();
        Document doc=  getDocument(url);
        //获取文章标题
        Elements title = doc.select("h1[id=articleContentId]");
        Elements author = doc.select("profile-name");
        Elements time = doc.select("span[class=time]");
        String description = doc.select("meta[name=description]").attr("content");
        //获取文章内容
        Elements content = doc.select("[class=htmledit_views]");
        System.out.println(title.text());
        article.setTitle(title.text());
        article.setContent(content.html());
        article.setDescription(description);
        article.setAuthor(author.text());
        article.setStatus(1);
        article.setIsMarkdown(false);
        article.setCategoryId(1);
        try {
            article.setCreateTime(DateUtil.parseDateNewFormat(time.text()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        articleService.save(article);
        return article;
    }

    public  boolean readPage(String webUrl,int pageNum) {
        Document doc = getDocument(webUrl+"/article/list/"+pageNum);
        // 获取目标HTML代码
        Elements elements = doc.select("[class=article-list]");
        //文章列表
        Elements articles = elements.select("[class=article-item-box csdn-tracking-statistics]");
        if (articles.size() == 0) {
            return false;
        }
        List<Article>  list=new ArrayList<>();
        articles.forEach(e -> {
            String url = e.select("a").attr("href");
            list.add(readArticle(url));
            try {
                //等待3秒
                Thread.sleep(200);
            } catch (InterruptedException interruptedException) {
                System.out.println("线程中断故障");
            }
        });

        return true;
    }

}
