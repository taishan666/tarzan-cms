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

    private Integer articleCount;
    private Integer commentCount;
    private Integer lookCount;
    private Integer userCount;
    private Map<String, Integer> lookCountByDay;
    private Map<String, Integer> userCountByDay;

}
