package com.tarzan.cms.modules.admin.model.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
@TableName("sys_user")
public class User implements Serializable {

    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 加密盐值
     */
    private String salt;

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 邮箱
     */
    private String email;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 年龄：1男2女
     */
    private Integer sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 头像
     */
    private String img;

    /**
     * 用户状态：1有效; 0无效
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 登录ip
     */
    @TableField(exist = false)
    private String loginIpAddress;

    /**
     * 角色名称（附加值）
     */
    @TableField(exist = false)
    private String roleName;

    /**
     * 重写获取盐值方法，自定义realm使用
     * Gets credentials salt.
     *
     * @return the credentials salt
     */
    public String getCredentialsSalt() {
        return username + "tarzan.com" + salt;
    }


}