package com.tarzan.cms.module.admin.service;

import com.tarzan.cms.module.admin.mapper.BizArticleMapper;
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

    public Map<String, Object> getSiteInfo() {
        return bizArticleMapper.getSiteInfo();
    }

}
