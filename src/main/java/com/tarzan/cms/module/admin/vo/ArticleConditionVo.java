package com.tarzan.cms.module.admin.vo;

import com.tarzan.cms.module.admin.vo.base.BaseConditionVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ArticleConditionVo extends BaseConditionVo {
    private Integer categoryId;
    private Integer tagId;
    private Integer status;
    private Boolean top;
    private Boolean recommended;
    private Boolean slider;
    private Boolean original;
    private Boolean random;
    private Boolean recentFlag;
    private Boolean sliderFlag;
    private List<Long> tagIds;
    private String keywords;

}

