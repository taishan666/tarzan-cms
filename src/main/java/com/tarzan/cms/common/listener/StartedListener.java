package com.tarzan.cms.common.listener;

import com.tarzan.cms.cache.CategoryCache;
import com.tarzan.cms.cache.RoleCache;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.common.props.CmsProperties;
import com.tarzan.cms.modules.admin.service.common.CommonDataService;
import com.tarzan.cms.shiro.ShiroService;
import com.tarzan.cms.utils.AppInstallTools;
import com.tarzan.cms.utils.ArticleCollect;
import com.tarzan.cms.utils.FileUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.file.*;
import java.util.Collections;

/**
 *
 * @author tarzan
 * @date 2021-10-05
 */
@Slf4j
@Component
@AllArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StartedListener implements ApplicationListener<ApplicationStartedEvent> {

    private final ArticleCollect articleCollect;
    private final AppInstallTools appInstallTools;
    private final CmsProperties cmsProperties;
    private final ShiroService shiroService;
    private final CommonDataService commonDataService;

    @Override
    public void onApplicationEvent(@NonNull ApplicationStartedEvent event) {
        initThemes();
        appInstallTools.install();
        shiroService.updatePermission();
        initDataCache();
        printStartInfo(event);
      //  articleCollect.collect();
    }

    private void initDataCache() {
        commonDataService.getAllCommonData();
        CategoryCache.initCategory();
        RoleCache.initRole();
    }
    /**
     * 打印信息
     */
    private void printStartInfo(ApplicationStartedEvent event) {
        ConfigurableApplicationContext context=event.getApplicationContext();
        Environment env = context.getEnvironment();
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String port = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (contextPath == null) {
            contextPath = "";
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Application is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + contextPath + "\n\t" +
                "External: \thttp://" + ip + ':' + port + contextPath + '\n' +
                "----------------------------------------------------------");
    }


    /**
     * 初始化主题
     */
    private void initThemes() {
        String themeFolder= "templates/theme";
        try {
            String themeClassPath = ResourceUtils.CLASSPATH_URL_PREFIX + themeFolder;

            URI themeUri = ResourceUtils.getURL(themeClassPath).toURI();

            log.debug("Theme uri: [{}]", themeUri);

            Path source;

            if (CoreConst.JAR.equalsIgnoreCase(themeUri.getScheme())) {
                // Create new file system for jar
                FileSystem fileSystem = getFileSystem(themeUri);
                source = fileSystem.getPath("/BOOT-INF/classes/"+themeFolder);
            } else {
                source = Paths.get(themeUri);
            }

            // Create theme folder
            Path themePath =  Paths.get(cmsProperties.getThemeDir());

            // Fix the problem that the project cannot start after moving to a new server
            if (Files.notExists(themePath)) {
                FileUtil.copyFolder(source, themePath);
                log.debug("Copied theme folder from [{}] to [{}]", source, themePath);
            } else {
                FileUtil.deleteFolder(themePath);
                FileUtil.copyFolder(source, themePath);
                log.debug("Skipped copying theme folder due to existence of theme folder");
            }
        } catch (Exception e) {
            if (e instanceof FileNotFoundException) {
                log.error("Please check location: classpath:{}",themeFolder);
            }
            log.error("Initialize internal theme to user path error!", e);
        }
    }

    @NonNull
    private FileSystem getFileSystem(@NonNull URI uri) throws IOException {
        Assert.notNull(uri, "Uri must not be null");
        FileSystem fileSystem;
        try {
            fileSystem = FileSystems.getFileSystem(uri);
        } catch (FileSystemNotFoundException e) {
            fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
        }
        return fileSystem;
    }



}
