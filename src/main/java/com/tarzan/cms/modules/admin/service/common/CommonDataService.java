package com.tarzan.cms.modules.admin.service.common;

import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.modules.admin.model.biz.Link;
import com.tarzan.cms.modules.admin.model.biz.Tags;
import com.tarzan.cms.modules.admin.service.biz.*;
import com.tarzan.cms.modules.admin.service.sys.SysConfigService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * thymeleaf调用后台的工具类
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Slf4j
@Service("commonDataService")
@AllArgsConstructor
public class CommonDataService {

    private final CategoryService bizCategoryService;
    private final ArticleService bizArticleService;
    private final TagsService bizTagsService;
    private final LinkService bizLinkService;
    private final SiteInfoService siteInfoService;
    private final ThemeService themeService;
    private final SysConfigService sysConfigService;

    public Object get(String moduleName) {
        try {
            DataTypeEnum dataTypeEnum = DataTypeEnum.valueOf(moduleName);
            switch (dataTypeEnum) {
                case CATEGORY_LIST:
                    return bizCategoryService.selectCategories(CoreConst.STATUS_VALID);
                case TAG_LIST:
                    return bizTagsService.selectTags(new Tags());
                case RECENT_LIST:
                    return bizArticleService.recentList(CoreConst.PAGE_SIZE);
                case RECOMMENDED_LIST:
                    return bizArticleService.recommendedList(CoreConst.PAGE_SIZE);
                case HOT_LIST:
                    return bizArticleService.hotList(CoreConst.PAGE_SIZE);
                case LINK_LIST:
                    return bizLinkService.selectLinks(new Link().setStatus(CoreConst.STATUS_VALID));
                case SITE_INFO:
                    return siteInfoService.getSiteInfo();
                case WEB_THEME:
                    return themeService.getTheme();
                case SITE_CONFIG:
                    return sysConfigService.selectAll();
                default:
                    return StringUtils.EMPTY;
            }
        } catch (Exception e) {
            log.error("获取网站公共信息[{}]发生异常: {}", moduleName, e.getMessage(), e);
        }
        return StringUtils.EMPTY;
    }

    public Map<String, Object> getAllCommonData() {
        Map<String, Object> result = new HashMap<>(DataTypeEnum.values().length);
        for (DataTypeEnum dataTypeEnum : DataTypeEnum.values()) {
            result.putAll(getCommonData(dataTypeEnum));
        }
        return result;
    }

    public Map<String, Object> getCommonData(DataTypeEnum dataTypeEnum) {
        Map<String, Object> result =  new HashMap<>(1);
        result.put(dataTypeEnum.name(), get(dataTypeEnum.name()));
        return result;
    }


    public enum DataTypeEnum {
        // 分类
        CATEGORY_LIST,
        // 标签
        TAG_LIST,
        //轮播文章
        SLIDER_LIST,
        //最近文章
        RECENT_LIST,
        //推荐文章
        RECOMMENDED_LIST,
        //热门文章
        HOT_LIST,
        //随机文章
        RANDOM_LIST,
        //友链
        LINK_LIST,
        //网站信息统计
        SITE_INFO,
        //网站信息统计
        WEB_THEME,
        //网站基本信息配置
        SITE_CONFIG
    }
}
