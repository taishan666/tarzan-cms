package com.tarzan.cms.module.admin.model;


import com.tarzan.cms.module.admin.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class BizLove extends BaseVo {

    private Integer bizId;
    private Integer bizType;
    private String userId;
    private String userIp;
    private Integer status;

}