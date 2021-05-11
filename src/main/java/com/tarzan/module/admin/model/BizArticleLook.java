package com.tarzan.module.admin.model;

import com.tarzan.module.admin.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BizArticleLook extends BaseVo {
    private static final long serialVersionUID = 1052723347580827581L;

    private Integer articleId;
    private String userId;
    private String userIp;
    private Date lookTime;

}
