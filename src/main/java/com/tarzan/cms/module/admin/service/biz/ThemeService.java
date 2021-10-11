package com.tarzan.cms.module.admin.service.biz;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.cms.module.admin.mapper.biz.ThemeMapper;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.module.admin.model.biz.Theme;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class ThemeService extends ServiceImpl<ThemeMapper, Theme> {


    @CacheEvict(value = "theme", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public boolean useTheme(Integer id) {
        update(new Theme().setStatus(0),null);
        return  update(new Theme().setStatus(1),Wrappers.<Theme>lambdaUpdate().eq(Theme::getId, id));
    }

    @Cacheable(value = "theme", key = "'current'")
    public Theme selectCurrent() {
        return getOne(Wrappers.<Theme>lambdaQuery().eq(Theme::getStatus, CoreConst.STATUS_VALID));
    }

    @CacheEvict(value = "theme", allEntries = true)
    public boolean deleteBatch(List<Integer> ids) {
        return removeByIds(ids);
    }
}
