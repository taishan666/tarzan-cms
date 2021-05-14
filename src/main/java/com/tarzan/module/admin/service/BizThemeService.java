package com.tarzan.module.admin.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.common.util.CoreConst;
import com.tarzan.module.admin.mapper.BizThemeMapper;
import com.tarzan.module.admin.model.BizTheme;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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
    public int useTheme(Integer id) {
        baseMapper.update(new BizTheme().setStatus(0),null);
        return  baseMapper.update(new BizTheme().setStatus(1),Wrappers.<BizTheme>lambdaUpdate().eq(BizTheme::getId, id));
    }

    @Cacheable(value = "theme", key = "'current'")
    public BizTheme selectCurrent() {
        return baseMapper.selectOne(Wrappers.<BizTheme>lambdaQuery().eq(BizTheme::getStatus, CoreConst.STATUS_VALID));
    }

    @CacheEvict(value = "theme", allEntries = true)
    public boolean deleteBatch(List<Integer> ids) {
        return removeByIds(ids);
    }
}
