package com.tarzan.cms.module.admin.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.tarzan.cms.module.admin.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BizCategory extends BaseVo {

    private Integer pid;
    private String name;
    private String description;
    private Integer sort;
    private Integer status;
    private String icon;


    @TableField(exist = false)
    private BizCategory parent;
    @TableField(exist = false)
    private List<BizCategory> children;

}
