package com.tarzan.cms.modules.admin.vo;

import lombok.Data;

@Data
public class SiteInfoVo {

    private Long articleCount;
    private Long tagCount;
    private Long categoryCount;
    private Long commentCount;

}
