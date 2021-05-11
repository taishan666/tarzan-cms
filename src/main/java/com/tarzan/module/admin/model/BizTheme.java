package com.tarzan.module.admin.model;

import com.tarzan.module.admin.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BizTheme extends BaseVo {
    private static final long serialVersionUID = -1364438867316136662L;

    private String name;

    private String description;

    private String img;

    private Integer status;

}