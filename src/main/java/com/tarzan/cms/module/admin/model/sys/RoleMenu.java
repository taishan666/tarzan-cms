package com.tarzan.cms.module.admin.model.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Data
@Accessors(chain = true)
@TableName("sys_role_menu")
public class RoleMenu implements Serializable {


    private Integer id;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 权限id
     */
    private Integer menuId;

}