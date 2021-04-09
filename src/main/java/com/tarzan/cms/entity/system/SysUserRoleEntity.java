package com.tarzan.cms.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:46
  */
@Data
@TableName("of_sys_user_role")
public class SysUserRoleEntity {
	//用户角色编号
	private Integer userRoleId;
	//角色编号
	private Integer roleId;
	//用户编号
	private Integer userId;
	//创建时间
	private Date createTime;
	//数据状态:1、正常 0、删除
	private String status;
} 
