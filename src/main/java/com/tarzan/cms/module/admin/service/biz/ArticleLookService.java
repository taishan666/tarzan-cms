package com.tarzan.cms.module.admin.service.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.utils.DateUtil;
import com.tarzan.cms.module.admin.mapper.biz.ArticleLookMapper;
import com.tarzan.cms.module.admin.model.biz.ArticleLook;
import com.tarzan.cms.module.admin.vo.CountVo;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tarzan liu
 * @version V1.0
 */
@Service
public class ArticleLookService extends ServiceImpl<ArticleLookMapper, ArticleLook> {

    private static Map<String, Integer> buildRecentDayMap(int day) {
        Date now = new Date();
        LinkedHashMap<String, Integer> map = Maps.newLinkedHashMap();
        for (int i = day - 1; i >= 0; i--) {
            int count = CoreConst.ENABLE_DEMO_DATA ? RandomUtils.nextInt(20, 100) : 0;
            map.put(DateUtil.format(DateUtil.addDays(now, -i), DateUtil.webFormat), count);
        }
        return map;
    }

    public int checkArticleLook(Integer articleId, String userIp, Date lookTime) {
        return count(Wrappers.lambdaQuery(new ArticleLook().setArticleId(articleId).setUserIp(userIp).setLookTime(lookTime)));
    }

    public Map<String, Integer> lookCountByDay(int day) {
        List<CountVo> list = baseMapper.lookCountByDay(day-1);
        Map<String, Integer> lookCountByDayMap = buildRecentDayMap(day);
        if (CollectionUtils.isNotEmpty(list)) {
            lookCountByDayMap.putAll(list.stream().collect(Collectors.toMap(CountVo::getDay, CountVo::getCount)));
        }
        return lookCountByDayMap;
    }

    public Map<String, Integer> userCountByDay(int day) {
        List<CountVo> list = baseMapper.userCountByDay(day-1);
        Map<String, Integer> userCountByDayMap = buildRecentDayMap(day);
        if (CollectionUtils.isNotEmpty(list)) {
            userCountByDayMap.putAll(list.stream().collect(Collectors.toMap(CountVo::getDay, CountVo::getCount)));
        }
        return userCountByDayMap;
    }
}
