package com.tarzan.cms.module.admin.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tarzan liu
 * @date 2021/1/12 3:35 下午
 */
@Data
@Builder
public class StatisticVo {

    private Long articleCount;
    private Long commentCount;
    private Long lookCount;
    private Long userCount;
    private Map<String, Long> lookCountByDay;
    private Map<String, Long> userCountByDay;

}
