package com.tarzan.module.admin.model;

import com.baomidou.mybatisplus.annotation.TableField;
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
public class BizComment extends BaseVo {
    private static final long serialVersionUID = -7221371985694751121L;

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
    private Integer loveCount;
    @TableField(exist = false)
    BizComment parent;
    @TableField(exist = false)
    BizArticle article;

}
