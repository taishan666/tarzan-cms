package com.tarzan.cms.modules.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tarzan liu
 * @date 2021/1/12 3:35 下午
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticVo {

    private Long articleCount;
    private Long commentCount;
    private Long lookCount;
    private Long userCount;
    private Map<String, Long> lookCountByDay;
    private Map<String, Long> userCountByDay;

}
