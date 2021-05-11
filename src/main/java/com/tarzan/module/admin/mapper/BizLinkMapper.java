package com.tarzan.module.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tarzan.module.admin.model.BizLink;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
public interface BizLinkMapper extends BaseMapper<BizLink> {

    List<BizLink> selectLinks(@Param("page") IPage<BizLink> page, @Param("bizLink") BizLink bizLink);

    int deleteBatch(Integer[] ids);

}