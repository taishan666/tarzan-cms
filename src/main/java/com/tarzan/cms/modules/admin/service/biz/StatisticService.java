package com.tarzan.cms.modules.admin.service.biz;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tarzan.cms.modules.admin.model.biz.ArticleLook;
import com.tarzan.cms.modules.admin.vo.StatisticVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tarzan liu
 * @date 2021/1/12 3:35 下午
 */
@Service
@AllArgsConstructor
@Slf4j
public class StatisticService {

    private final ArticleService articleService;
    private final CommentService commentService;
    private final ArticleLookService articleLookService;
    private final RedisTemplate<String, Object> redisTemplate;


    public StatisticVo indexStatistic() {
        return   getData("statistic", () -> getStatistic());
    }

    private StatisticVo getStatistic() {
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

    private <T> T getData(String key, Supplier<T> supplier) {
        if (!StringUtils.isEmpty(key)) {
            T t = (T) redisTemplate.opsForValue().get(key);
            if (Objects.isNull(t)) {
                t = supplier.get();
                if (Objects.nonNull(t)) {
                    redisTemplate.opsForValue().set(key, t,12, TimeUnit.HOURS);
                }
            }
            return t;
        } else {
            return null;
        }
    }
}
