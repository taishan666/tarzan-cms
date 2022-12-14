package com.tarzan.cms.modules.admin.model.biz;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.cms.modules.admin.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("biz_article")
public class Article extends BaseVo {

    private String title;
    private Integer userId;
    private String author;
    private String coverImage;
    private String qrcodePath;
    private Boolean isMarkdown;
    private String content;
    private String contentMd;
    private Integer top;
    private Integer categoryId;
    private Integer status;
    private Integer recommended;
    private Integer slider;
    private String sliderImg;
    private Integer original;
    private String description;
    private String keywords;
    //是否开启评论
    private Integer comment;
    @TableField(exist = false)
    private Long lookCount = 0L;
    @TableField(exist = false)
    private Long commentCount = 0L;
    @TableField(exist = false)
    private Long loveCount = 0L;
    @TableField(exist = false)
    private Integer newFlag = 0;
    @TableField(exist = false)
    List<Tags> tags;
    @TableField(exist = false)
    Category bizCategory;

}
