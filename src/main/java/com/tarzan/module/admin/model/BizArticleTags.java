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
public class BizArticleTags extends BaseVo {
    private static final long serialVersionUID = 2627147974506469978L;

    private Integer tagId;
    private Integer articleId;

}
