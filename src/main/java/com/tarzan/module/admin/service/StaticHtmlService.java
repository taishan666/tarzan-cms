package com.tarzan.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tarzan.common.config.properties.StaticizeProperties;
import com.tarzan.common.util.CoreConst;
import com.tarzan.common.util.Pagination;
import com.tarzan.component.CommonDataService;
import com.tarzan.module.admin.model.BizArticle;
import com.tarzan.module.admin.model.BizArticleTags;
import com.tarzan.module.admin.model.BizCategory;
import com.tarzan.module.admin.model.BizTags;
import com.tarzan.module.admin.vo.ArticleConditionVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.tarzan.common.util.CoreConst.THEME_PREFIX;

@Slf4j
@Service
@AllArgsConstructor
public class StaticHtmlService {

    private final StaticizeProperties staticizeProperties;
    private final TemplateEngine templateEngine;
    private final BizArticleService bizArticleService;
    private final BizCategoryService bizCategoryService;
    private final BizTagsService bizTagsService;
    private final BizArticleTagsService bizArticleTagsService;
    private final BizThemeService bizThemeService;
    private final CommonDataService commonDataService;

    public void makeStaticSite(HttpServletRequest request, HttpServletResponse response, Boolean force) {
        createIndexHtml(request, response, true);
        createArticleHtml(request, response, true);
        createCategoryHtml(request, response, true);
        createTagHtml(request, response, true);
        createCommmentHtml(request, response, true);
    }

    public void createIndexHtml(HttpServletRequest request, HttpServletResponse response, Boolean force) {
        List<BizArticle> sliderList = bizArticleService.sliderList();
        ArticleConditionVo vo = new ArticleConditionVo();
        vo.setStatus(CoreConst.STATUS_VALID);
        int total = bizArticleService.count(Wrappers.<BizArticle>lambdaQuery().eq(BizArticle::getStatus, CoreConst.STATUS_VALID));
        if (total == 0) {
            Map<String, Object> paramMap = ImmutableMap.of("pageUrl", "blog/index", "categoryId", "index", "sliderList", sliderList, "page", new Pagination<>(1, 10), "articleList", Collections.emptyList());
            createHtml(request, response, force, paramMap, "index", "index");
            return;
        }
        IPage<Object> pagination = new Pagination<>(vo.getPageNumber(), vo.getPageSize()).setTotal(total);
        for (long pageNum = 1; pageNum <= pagination.getPages(); pageNum++) {
            IPage<BizArticle> page = new Pagination<>(vo.getPageNumber(), vo.getPageSize());
            List<BizArticle> articleList = bizArticleService.findByCondition(page, vo);
            Map<String, Object> paramMap = ImmutableMap.of("pageUrl", "blog/index", "categoryId", "index", "sliderList", sliderList, "page", page, "articleList", articleList);
            if (pageNum == 1) {
                createHtml(request, response, force, paramMap, "index", "index");
            }
            createHtml(request, response, force, paramMap, "index", "index" + File.separator + pageNum);
        }
    }

    public void createArticleHtml(HttpServletRequest request, HttpServletResponse response, Boolean force) {
        List<BizArticle> articleList = bizArticleService.findByCondition(new Pagination<>(1, 99999), new ArticleConditionVo().setStatus(CoreConst.STATUS_VALID));
        for (BizArticle article : articleList) {
            Map<String, Object> paramMap = Maps.newHashMap();
            paramMap.put("article", article);
            paramMap.put("categoryId", article.getCategoryId());
            createHtml(request, response, force, paramMap, "article", String.valueOf(article.getId()));
        }
    }

    public void createCommmentHtml(HttpServletRequest request, HttpServletResponse response, Boolean force) {
        createHtml(request, response, force, Collections.emptyMap(), "comment", "comment");
    }

    public void createCategoryHtml(HttpServletRequest request, HttpServletResponse response, Boolean force) {
        List<BizCategory> categoryList = bizCategoryService.list(Wrappers.<BizCategory>lambdaQuery().eq(BizCategory::getStatus, CoreConst.STATUS_VALID));
        for (BizCategory category : categoryList) {

            ArticleConditionVo vo = new ArticleConditionVo();
            vo.setStatus(CoreConst.STATUS_VALID);
            vo.setCategoryId(category.getId());

            int total = bizArticleService.count(Wrappers.<BizArticle>lambdaQuery().eq(BizArticle::getCategoryId, category.getId()).eq(BizArticle::getStatus, CoreConst.STATUS_VALID));
            if (total == 0) {
                Map<String, Object> paramMap = ImmutableMap.of("pageUrl", "blog/category/" + category.getId(), "categoryId", category.getId(), "categoryName", category.getName(), "articleList", Collections.emptyList());
                createHtml(request, response, force, paramMap, "index", "category" + File.separator + category.getId());
                continue;
            }
            IPage<Object> pagination = new Pagination<>(vo.getPageNumber(), vo.getPageSize()).setTotal(total);
            for (long pageNum = 1; pageNum <= pagination.getPages(); pageNum++) {

                Pagination<BizArticle> page = new Pagination<>(pageNum, vo.getPageSize());
                List<BizArticle> articleList = bizArticleService.findByCondition(page, vo);
                Map<String, Object> paramMap = ImmutableMap.of("pageUrl", "blog/category/" + category.getId(), "categoryId", category.getId(), "categoryName", category.getName(), "page", page, "articleList", articleList);
                if (pageNum == 1) {
                    createHtml(request, response, force, paramMap, "index", "category" + File.separator + category.getId());
                }
                createHtml(request, response, force, paramMap, "index", "category" + File.separator + category.getId() + File.separator + pageNum);
            }
        }
    }

    public void createTagHtml(HttpServletRequest request, HttpServletResponse response, Boolean force) {
        List<BizTags> tagsList = bizTagsService.list();
        for (BizTags tag : tagsList) {

            ArticleConditionVo vo = new ArticleConditionVo();
            vo.setTagId(tag.getId());

            int total = bizArticleTagsService.count(Wrappers.<BizArticleTags>lambdaQuery().eq(BizArticleTags::getTagId, tag.getId()));
            if (total == 0) {
                Map<String, Object> paramMap = ImmutableMap.of("pageUrl", "blog/tag/" + tag.getId(), "articleList", Collections.emptyList());
                createHtml(request, response, force, paramMap, "index", "tag" + File.separator + tag.getId());
                continue;
            }
            IPage<Object> pagination = new Pagination<>(vo.getPageNumber(), vo.getPageSize()).setTotal(total);
            for (long pageNum = 1; pageNum <= pagination.getPages(); pageNum++) {

                Pagination<BizArticle> page = new Pagination<>(pageNum, vo.getPageSize());
                List<BizArticle> articleList = bizArticleService.findByCondition(page, vo);
                Map<String, Object> paramMap = ImmutableMap.of("pageUrl", "blog/tag/" + tag.getId(), "page", page, "articleList", articleList);
                if (pageNum == 1) {
                    createHtml(request, response, force, paramMap, "index", "tag" + File.separator + tag.getId());
                }
                createHtml(request, response, force, paramMap, "index", "tag" + File.separator + tag.getId() + File.separator + pageNum);
            }
        }
    }


    public void createHtml(HttpServletRequest request, HttpServletResponse response, Boolean force, Map<String, Object> map, String templateUrl, String fileName) {
        if (StringUtils.isBlank(staticizeProperties.getFolder())) {
            throw new IllegalArgumentException("请先在Yml配置静态页面生成路径");
        }
        log.info("开始生成静态页面: {}", fileName);

        templateUrl = StringUtils.removeStart(templateUrl, File.separator);

        PrintWriter writer = null;
        try {
            Map<String, Object> paramMap = commonDataService.getAllCommonData();
            // 获取页面数据
            paramMap.putAll(map);
            // 创建thymeleaf上下文对象
            WebContext context = new WebContext(request, response, request.getServletContext());
            // 把数据放入上下文对象
            context.setVariables(paramMap);
            // 文件生成路径
            String fileUrl = StringUtils.appendIfMissing(staticizeProperties.getFolder(), File.separator) + templateUrl + File.separator + fileName + ".html";
            // 自动创建上层文件夹
            File directory = new File(StringUtils.substringBeforeLast(fileUrl, File.separator));
            if (!directory.exists()) {
                directory.mkdirs();
            }
            // 创建输出流
            File file = new File(fileUrl);
            if (Boolean.FALSE.equals(force) && file.exists()) {
                // 不强制覆盖现有文件
                return;
            }
            writer = new PrintWriter(file);
            // 执行页面静态化方法
            templateEngine.process(THEME_PREFIX + bizThemeService.selectCurrent().getName() + File.separator + templateUrl, context, writer);
        } catch (Exception e) {
            log.error("页面静态化出错：{}", e.getMessage(), e);
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }


}
