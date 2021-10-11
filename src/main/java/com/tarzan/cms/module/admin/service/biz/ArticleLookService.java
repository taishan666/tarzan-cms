package com.tarzan.cms.module.admin.service.biz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.module.admin.mapper.biz.ArticleLookMapper;
import com.tarzan.cms.module.admin.model.biz.ArticleLook;
import com.tarzan.cms.utils.DateUtil;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tarzan liu
 * @since JDK1.8
 */
@Service
public class ArticleLookService extends ServiceImpl<ArticleLookMapper, ArticleLook> {

    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

    private static Map<String, Long> buildRecentDayMap(int day) {
        Date now = new Date();
        LinkedHashMap<String, Long> map = Maps.newLinkedHashMap();
        for (int i = day; i >= 1; i--) {
            Long count = CoreConst.ENABLE_DEMO_DATA ? RandomUtils.nextLong(20l, 100l) : 0l;
            map.put(DateUtil.format(DateUtil.addDays(now, -i), DateUtil.webFormat), count);
        }
        return map;
    }


    public int checkArticleLook(Integer articleId, String userIp, Date lookTime) {
        return count(Wrappers.lambdaQuery(new ArticleLook().setArticleId(articleId).setUserIp(userIp).setLookTime(lookTime)));
    }

    public  Map<String,Long> looksByDay(int day){
        Map<String,Long> looksByDayMap= buildRecentDayMap(day);
        looksGroupMap(day).forEach((k,v)->looksByDayMap.put(k,Long.valueOf(v.size())));
       return looksByDayMap;
    }

    public  Map<String,Long> usersByDay(int day){
        Map<String,List<ArticleLook>> lookMap=looksGroupMap(day);
        Map<String,Long> usersByDayMap=buildRecentDayMap(day);
        lookMap.forEach((k,v)->{
            List<String> users= v.stream().map(ArticleLook::getUserIp).collect(Collectors.toList());
            users=users.stream().distinct().collect(Collectors.toList());
            usersByDayMap.put(k,Long.valueOf(users.size()));
        });
        return usersByDayMap;
    }

    private Map<String,List<ArticleLook>>  looksGroupMap(int day){
        Date curDate=new Date();
        Date beforeWeekDate= DateUtil.addDays(curDate,-day+1);
        LambdaQueryWrapper<ArticleLook> wrapper=new LambdaQueryWrapper();
        wrapper.ge(ArticleLook::getLookTime,sdf.format(beforeWeekDate));
        wrapper.lt(ArticleLook::getLookTime,sdf.format(curDate));
        List<ArticleLook> list=list(wrapper);
        return list.stream().collect(Collectors.groupingBy(e->sdf.format(e.getLookTime())));
    }


}
