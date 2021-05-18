package com.tarzan.module.admin.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Data
@TableName("sys_user_role")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 2455220013366482894L;

    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 角色id
     */
    private Integer roleId;

}