package com.tarzan.cms.shiro;

import com.tarzan.cms.common.properties.FileUploadProperties;
import com.tarzan.cms.common.properties.StaticHtmlProperties;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.module.admin.model.sys.Menu;
import com.tarzan.cms.module.admin.service.sys.MenuService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 初始化、动态更新shiro用户权限
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class ShiroService {

    private final MenuService MenuService;
    private final ShiroFilterFactoryBean shiroFilterFactoryBean;
    private final FileUploadProperties fileUploadProperties;
    private final StaticHtmlProperties staticHtmlProperties;

    @PostConstruct
    public void init() {
        updatePermission();
    }

    /**
     * 初始化权限
     */
    public Map<String, String> loadFilterChainDefinitions() {
        // 权限控制map.从数据库获取
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/blog/**", "anon");
        filterChainDefinitionMap.put("/register", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/kickOut", "anon");
        filterChainDefinitionMap.put("/error/**", "anon");
        filterChainDefinitionMap.put("/admin1/css/**", "anon");
        filterChainDefinitionMap.put("/admin1/js/**", "anon");
        filterChainDefinitionMap.put("/admin1/img/**", "anon");
        filterChainDefinitionMap.put("/admin1/images/**", "anon");
        filterChainDefinitionMap.put("/admin1/libs/**", "anon");
        filterChainDefinitionMap.put("/theme/**", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/verificationCode", "anon");
        filterChainDefinitionMap.put(fileUploadProperties.getAccessPathPattern(), "anon");
        filterChainDefinitionMap.put(staticHtmlProperties.getAccessPathPattern(), "anon");
        List<Menu> menuList = MenuService.selectAll(CoreConst.STATUS_VALID);
        for (Menu menu : menuList) {
            if (StringUtils.isNotBlank(menu.getUrl()) && StringUtils.isNotBlank(menu.getPerms())) {
                String perm = "perms[" + menu.getPerms() + ']';
                filterChainDefinitionMap.put(menu.getUrl(), perm + ",kickOut");
            }
        }
        filterChainDefinitionMap.put("/**", "user,kickOut");
        return filterChainDefinitionMap;
    }

    /**
     * 重新加载权限
     */
    public void updatePermission() {
        synchronized (shiroFilterFactoryBean) {
            AbstractShiroFilter shiroFilter;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
            } catch (Exception e) {
                throw new RuntimeException("get ShiroFilter from shiroFilterFactoryBean error!");
            }
            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
                    .getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver
                    .getFilterChainManager();
            // 清空老的权限控制
            manager.getFilterChains().clear();
            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            shiroFilterFactoryBean.setFilterChainDefinitionMap(loadFilterChainDefinitions());
            // 重新构建生成
            Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
            chains.forEach((url, perm) -> manager.createChain(url, StringUtils.deleteWhitespace(perm)));
        }
    }
}
