package com.tarzan.cms.module.admin.model;

import com.tarzan.cms.module.admin.vo.base.BaseVo;
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

    private Integer tagId;
    private Integer articleId;

}
