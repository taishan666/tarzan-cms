package com.tarzan.cms.module.admin.mapper.biz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tarzan.cms.module.admin.model.biz.ArticleLook;
import com.tarzan.cms.module.admin.vo.CountVo;

import java.util.List;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
public interface ArticleLookMapper extends BaseMapper<ArticleLook> {

    List<CountVo> lookCountByDay(int day);

    List<CountVo> userCountByDay(int day);
}
