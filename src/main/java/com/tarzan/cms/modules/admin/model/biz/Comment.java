package com.tarzan.cms.modules.admin.model.biz;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.cms.modules.admin.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("biz_comment")
public class Comment extends BaseVo {

    private Integer userId;
    private Integer sid;
    private Integer pid;
    private String qq;
    private String nickname;
    private String avatar;
    private String email;
    private String url;
    private Integer status;
    private String ip;
    private String lng;
    private String lat;
    private String address;
    private String os;
    private String osShortName;
    private String browser;
    private String browserShortName;
    private String content;
    private String remark;
    private Integer support;
    private Integer oppose;
    @TableField(exist = false)
    private Long loveCount;
    @TableField(exist = false)
    Comment parent;
    @TableField(exist = false)
    Article article;

}
