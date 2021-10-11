package com.tarzan.cms.module.admin.model.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.cms.module.admin.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("biz_article_tags")
public class ArticleTags extends BaseVo {

    private Integer tagId;
    private Integer articleId;

}
