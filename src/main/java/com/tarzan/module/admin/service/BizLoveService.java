package com.tarzan.module.admin.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.module.admin.mapper.BizLoveMapper;
import com.tarzan.module.admin.model.BizLove;
import org.springframework.stereotype.Service;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Service
public class BizLoveService extends ServiceImpl<BizLoveMapper, BizLove> {

    public BizLove checkLove(Integer bizId, String userIp) {
        return baseMapper.selectOne(Wrappers.lambdaQuery(new BizLove().setBizId(bizId).setUserIp(userIp)));
    }
}
