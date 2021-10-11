package com.tarzan.cms.module.admin.service.biz;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.cms.module.admin.mapper.biz.LoveMapper;
import com.tarzan.cms.module.admin.model.biz.Love;
import org.springframework.stereotype.Service;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Service
public class LoveService extends ServiceImpl<LoveMapper, Love> {

    public Love checkLove(Integer bizId, String userIp) {
        return getOne(Wrappers.lambdaQuery(new Love().setBizId(bizId).setUserIp(userIp)));
    }
}
