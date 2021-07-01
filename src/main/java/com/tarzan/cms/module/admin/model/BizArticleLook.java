package com.tarzan.cms.module.admin.model;

import com.tarzan.cms.module.admin.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class BizArticleLook extends BaseVo {

    private Integer articleId;
    private String userId;
    private String userIp;
    private Date lookTime;

}
