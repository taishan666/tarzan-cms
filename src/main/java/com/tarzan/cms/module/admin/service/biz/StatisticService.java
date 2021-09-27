package com.tarzan.cms.module.admin.service.biz;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tarzan.cms.module.admin.model.biz.ArticleLook;
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
public class StatisticService {

    private final ArticleService articleService;
    private final CommentService commentService;
    private final ArticleLookService articleLookService;

    public StatisticVo indexStatistic() {
        long articleCount = articleService.count();
        long commentCount = commentService.count();
        long lookCount = articleLookService.count();
        List<ArticleLook> userList=articleLookService.list(Wrappers.<ArticleLook>lambdaQuery().select(ArticleLook::getUserIp));
        long userCount = userList.stream().distinct().count();
        int recentDays = 7;
        Map<String, Long> lookCountByDay = articleLookService.looksByDay(recentDays);
        Map<String, Long> userCountByDay = articleLookService.usersByDay(recentDays);
        return StatisticVo.builder().articleCount(articleCount).commentCount(commentCount).lookCount(lookCount).userCount(userCount).lookCountByDay(lookCountByDay).userCountByDay(userCountByDay).build();
    }
}
