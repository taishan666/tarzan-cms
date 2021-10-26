package com.tarzan.cms.module.admin.service.biz;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.cms.common.properties.CmsProperties;
import com.tarzan.cms.module.admin.mapper.biz.ThemeMapper;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.module.admin.model.biz.Theme;
import com.tarzan.cms.module.admin.vo.base.ResponseVo;
import com.tarzan.cms.utils.FileUtil;
import com.tarzan.cms.utils.ResultUtil;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class ThemeService extends ServiceImpl<ThemeMapper, Theme> {

    private final CmsProperties cmsProperties;

    @CacheEvict(value = "theme", allEntries = true)
    public ResponseVo upload(MultipartFile file) {
        try {
            // 获取文件名
            String themeName = "test";
            String themePath = cmsProperties.getThemeDir() + File.separator+themeName;
            File themeDir = new File(themePath);
            // 创建文件根目录
            if (!themeDir.exists() && !themeDir.mkdirs()) {
                log.error("创建文件夹失败: "+themePath);
                return null;
            }
       /*     if(!FileUtil.isEmpty(Paths.get(themePath))){
                return  ResultUtil.error("主题已安装");
            }*/
            FileUtil.unzip(file.getBytes(), Paths.get(themePath));
            Optional<File> themeRoot= Arrays.asList(themeDir.listFiles()).stream().findFirst();
            FileUtil.copyFolder(Paths.get(themeRoot.get().getPath()),Paths.get(themePath));
            FileUtil.deleteFolder(Paths.get(themeRoot.get().getPath()));
            Theme theme=new Theme();
            theme.setName(themeName);
            theme.setImg("theme"+ File.separator+themeName+File.separator+"screenshot.png");
            theme.setStatus(0);
            save(theme);
        } catch (IOException e) {
            return  ResultUtil.error(e.getMessage());
        }
        return ResultUtil.success();
    }


    @CacheEvict(value = "theme", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public boolean useTheme(Integer id) {
        update(new Theme().setStatus(0),null);
        return  update(new Theme().setStatus(CoreConst.STATUS_VALID),Wrappers.<Theme>lambdaUpdate().eq(Theme::getId, id));
    }


    @CacheEvict(value = "theme", allEntries = true)
    public boolean delete(Integer id)  {
        Theme theme=getById(id);
        Assert.notNull(theme,"主题不存在！");
        String themesPath =cmsProperties.getThemeDir()+File.separator+theme.getName();
        try {
            FileUtil.deleteFolder(Paths.get(themesPath));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return removeById(id);
    }

    @Cacheable(value = "theme", key = "'current'")
    public Theme selectCurrent() {
        return getOne(Wrappers.<Theme>lambdaQuery().eq(Theme::getStatus, CoreConst.STATUS_VALID));
    }

    @Cacheable(value = "theme", key = "'themeName'")
    public String getTheme() {
        return CoreConst.THEME_PREFIX+selectCurrent().getName();
    }

    @CacheEvict(value = "theme", allEntries = true)
    public boolean deleteBatch(List<Integer> ids) {
        return removeByIds(ids);
    }
}
