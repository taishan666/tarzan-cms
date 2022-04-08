package com.tarzan.cms.modules.admin.model.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.cms.modules.admin.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("biz_article_look")
public class ArticleLook extends BaseVo {

    private Integer articleId;
    private String userId;
    private String userIp;
    private Date lookTime;

}
