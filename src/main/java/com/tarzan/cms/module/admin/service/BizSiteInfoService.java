package com.tarzan.cms.module.admin.service;

import com.tarzan.cms.module.admin.mapper.BizArticleMapper;
import com.tarzan.cms.module.admin.vo.SiteInfoVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class BizSiteInfoService {

    private final BizArticleMapper bizArticleMapper;

    public SiteInfoVo getSiteInfo() {
        return bizArticleMapper.getSiteInfo();
    }

}
