package com.tarzan.module.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tarzan.module.admin.model.BizCategory;

import java.util.List;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
public interface BizCategoryMapper extends BaseMapper<BizCategory> {

    List<BizCategory> selectCategories(BizCategory bizCategory);

    int deleteBatch(Integer[] ids);

    BizCategory getById(Integer id);
}