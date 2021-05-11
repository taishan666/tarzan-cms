package com.tarzan.module.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tarzan.module.admin.model.BizTheme;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
public interface BizThemeMapper extends BaseMapper<BizTheme> {

    int setInvaid();

    int updateStatusById(Integer id);

    int deleteBatch(Integer[] ids);
}