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
public class BizTags extends BaseVo {
    private static final long serialVersionUID = 3578477956306175100L;

    private String name;
    private String description;

}
