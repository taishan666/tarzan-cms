package com.tarzan.module.admin.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.tarzan.common.util.CoreConst;
import com.tarzan.common.util.DateUtil;
import com.tarzan.module.admin.mapper.BizArticleLookMapper;
import com.tarzan.module.admin.model.BizArticleLook;
import com.tarzan.module.admin.vo.CountVo;
import org.apache.commons.collections4.CollectionUtils;
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
 * @date 2021年5月11日
 */
@Service
public class BizArticleLookService extends ServiceImpl<BizArticleLookMapper, BizArticleLook> {

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
        return baseMapper.checkArticleLook(articleId, userIp, lookTime);
    }

    public Map<String, Integer> lookCountByDay(int day) {
        List<CountVo> list = baseMapper.lookCountByDay(day);
        Map<String, Integer> lookCountByDayMap = buildRecentDayMap(day + 1);
        if (CollectionUtils.isNotEmpty(list)) {
            lookCountByDayMap.putAll(list.stream().collect(Collectors.toMap(CountVo::getDay, CountVo::getCount)));
        }
        return lookCountByDayMap;
    }

    public Map<String, Integer> userCountByDay(int day) {
        List<CountVo> list = baseMapper.userCountByDay(day);
        Map<String, Integer> userCountByDayMap = buildRecentDayMap(day + 1);
        if (CollectionUtils.isNotEmpty(list)) {
            userCountByDayMap.putAll(list.stream().collect(Collectors.toMap(CountVo::getDay, CountVo::getCount)));
        }
        return userCountByDayMap;
    }
}
