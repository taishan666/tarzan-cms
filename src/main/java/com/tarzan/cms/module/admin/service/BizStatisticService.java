package com.tarzan.cms.module.admin.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tarzan.cms.module.admin.model.BizArticleLook;
import com.tarzan.cms.module.admin.vo.StatisticVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tarzan liu
 * @date 2021/1/12 3:35 下午
 */
@Service
@AllArgsConstructor
public class BizStatisticService {

    private final BizArticleService articleService;
    private final BizCommentService commentService;
    private final BizArticleLookService articleLookService;

    public StatisticVo indexStatistic() {
        long articleCount = articleService.count();
        long commentCount = commentService.count();
        long lookCount = articleLookService.count();
        List<BizArticleLook> userList=articleLookService.list(Wrappers.<BizArticleLook>lambdaQuery().select(BizArticleLook::getUserIp));
        long userCount = userList.stream().distinct().count();
        int recentDays = 7;
        Map<String, Integer> lookCountByDay = articleLookService.lookCountByDay(recentDays);
        Map<String, Integer> userCountByDay = articleLookService.userCountByDay(recentDays);
        return StatisticVo.builder().articleCount(articleCount).commentCount(commentCount).lookCount(lookCount).userCount(userCount).lookCountByDay(lookCountByDay).userCountByDay(userCountByDay).build();
    }
}
