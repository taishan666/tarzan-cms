package com.tarzan.module.admin.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Data
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = -902800328539403089L;

    private Integer id;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 权限id
     */
    private String permissionId;

}