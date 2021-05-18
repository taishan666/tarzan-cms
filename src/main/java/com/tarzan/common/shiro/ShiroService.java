package com.tarzan.common.shiro;

import com.tarzan.common.config.properties.FileUploadProperties;
import com.tarzan.common.config.properties.StaticHtmlProperties;
import com.tarzan.common.util.CoreConst;
import com.tarzan.module.admin.model.Menu;
import com.tarzan.module.admin.service.MenuService;
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
 * @version V1.0
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
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/libs/**", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/verificationCode", "anon");
        filterChainDefinitionMap.put(fileUploadProperties.getAccessPathPattern(), "anon");
        filterChainDefinitionMap.put(staticHtmlProperties.getAccessPathPattern(), "anon");
        List<Menu> permissionList = MenuService.selectAll(CoreConst.STATUS_VALID);
        for (Menu menu : permissionList) {
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
