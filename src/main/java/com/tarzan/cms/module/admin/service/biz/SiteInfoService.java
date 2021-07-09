package com.tarzan.cms.module.admin.service.biz;

import com.tarzan.cms.module.admin.mapper.biz.ArticleMapper;
import com.tarzan.cms.module.admin.vo.SiteInfoVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class SiteInfoService {

    private final ArticleMapper articleMapper;

    public SiteInfoVo getSiteInfo() {
        return articleMapper.getSiteInfo();
    }

}
