package com.tarzan.cms.module.admin.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.cms.module.admin.mapper.BizThemeMapper;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.module.admin.model.BizTheme;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class BizThemeService extends ServiceImpl<BizThemeMapper, BizTheme> {


    @CacheEvict(value = "theme", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public boolean useTheme(Integer id) {
        update(new BizTheme().setStatus(0),null);
        return  update(new BizTheme().setStatus(1),Wrappers.<BizTheme>lambdaUpdate().eq(BizTheme::getId, id));
    }

    @Cacheable(value = "theme", key = "'current'")
    public BizTheme selectCurrent() {
        return getOne(Wrappers.<BizTheme>lambdaQuery().eq(BizTheme::getStatus, CoreConst.STATUS_VALID));
    }

    @CacheEvict(value = "theme", allEntries = true)
    public boolean deleteBatch(List<Integer> ids) {
        return removeByIds(ids);
    }
}
