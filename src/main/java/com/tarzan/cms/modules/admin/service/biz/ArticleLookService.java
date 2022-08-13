package com.tarzan.cms.modules.admin.service.biz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.tarzan.cms.modules.admin.mapper.biz.ArticleLookMapper;
import com.tarzan.cms.modules.admin.model.biz.ArticleLook;
import com.tarzan.cms.utils.DateUtil;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tarzan liu
 * @since JDK1.8
 */
@Service
public class ArticleLookService extends ServiceImpl<ArticleLookMapper, ArticleLook> {

    private static Map<String, Long> buildRecentDayMap(int day) {
        Date now = new Date();
        LinkedHashMap<String, Long> map = Maps.newLinkedHashMap();
        for (int i = day; i >= 1; i--) {
            Long count = 0L;
            map.put(DateUtil.format(DateUtil.addDays(now, -i), DateUtil.webFormat), count);
        }
        return map;
    }


    public long checkArticleLook(Integer articleId, String userIp, Date lookTime) {
        return count(Wrappers.lambdaQuery(new ArticleLook().setArticleId(articleId).setUserIp(userIp).setLookTime(lookTime)));
    }

    public  Map<String,Long> looksByDay(Map<String,List<ArticleLook>> lookMap,int day){
        Map<String,Long> looksByDayMap= buildRecentDayMap(day);
        lookMap.forEach((k,v)->looksByDayMap.put(k, (long) v.size()));
       return looksByDayMap;
    }

    public  Map<String,Long> usersByDay(Map<String,List<ArticleLook>> lookMap,int day){
        Map<String,Long> usersByDayMap=buildRecentDayMap(day);
        lookMap.forEach((k,v)->{
            Set<String> users= v.stream().map(ArticleLook::getUserIp).collect(Collectors.toSet());
            usersByDayMap.put(k, (long) users.size());
        });
        return usersByDayMap;
    }

    public Map<String,List<ArticleLook>>  looksGroupMap(int day){
        List<ArticleLook> list=looksRecentDays(day);
        return list.stream().collect(Collectors.groupingBy(e->DateUtil.format(e.getLookTime(), DateUtil.webFormat)));
    }

    private List<ArticleLook> looksRecentDays(int day){
        Date curDate=DateUtil.getDayBegin(new Date());
        Date beforeWeekDate= DateUtil.addDays(curDate,-day);
        LambdaQueryWrapper<ArticleLook> wrapper=Wrappers.lambdaQuery();
        wrapper.select(ArticleLook::getId,ArticleLook::getUserIp,ArticleLook::getLookTime);
        wrapper.ge(ArticleLook::getLookTime,beforeWeekDate);
        wrapper.le(ArticleLook::getLookTime,curDate);
        return super.list(wrapper);
    }


}
