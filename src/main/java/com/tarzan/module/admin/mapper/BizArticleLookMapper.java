package com.tarzan.module.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tarzan.module.admin.model.BizArticleLook;
import com.tarzan.module.admin.vo.CountVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
public interface BizArticleLookMapper extends BaseMapper<BizArticleLook> {

    int checkArticleLook(@Param("articleId") Integer articleId, @Param("userIp") String userIp, @Param("lookTime") Date lookTime);

    List<CountVo> lookCountByDay(int day);

    List<CountVo> userCountByDay(int day);
}
