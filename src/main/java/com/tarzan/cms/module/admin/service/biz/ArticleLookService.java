package com.tarzan.cms.module.admin.service.biz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.tarzan.cms.module.admin.mapper.biz.ArticleLookMapper;
import com.tarzan.cms.module.admin.model.biz.ArticleLook;
import com.tarzan.cms.utils.DateUtil;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tarzan liu
 * @version V1.0
 */
@Service
public class ArticleLookService extends ServiceImpl<ArticleLookMapper, ArticleLook> {

    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

    public int checkArticleLook(Integer articleId, String userIp, Date lookTime) {
        return count(Wrappers.lambdaQuery(new ArticleLook().setArticleId(articleId).setUserIp(userIp).setLookTime(lookTime)));
    }

    public  Map<String,Long> looksByDay(int day){
        Map<String,Long> looksByDayMap= Maps.newLinkedHashMap();
        looksGroupMap(day).forEach((k,v)->looksByDayMap.put(k,Long.valueOf(v.size())));
       return looksByDayMap;
    }

    public  Map<String,Long> usersByDay(int day){
        Map<String,List<ArticleLook>> lookMap=looksGroupMap(day);
        Map<String,Long> usersByDayMap=Maps.newLinkedHashMap();
        lookMap.forEach((k,v)->{
            List<String> users= v.stream().map(ArticleLook::getUserId).collect(Collectors.toList());
            users=users.stream().distinct().collect(Collectors.toList());
            usersByDayMap.put(k,Long.valueOf(users.size()));
        });
        return usersByDayMap;
    }


    private Map<String,List<ArticleLook>>  looksGroupMap(int day){
        Date curDate=new Date();
        Date beforeWeekDate= DateUtil.addDays(curDate,-day);
        LambdaQueryWrapper<ArticleLook> wrapper=new LambdaQueryWrapper();
        wrapper.ge(ArticleLook::getLookTime,sdf.format(beforeWeekDate));
        wrapper.le(ArticleLook::getLookTime,sdf.format(curDate));
        List<ArticleLook> list=list(wrapper);
        return list.stream().collect(Collectors.groupingBy(e->sdf.format(e.getLookTime())));
    }

}
