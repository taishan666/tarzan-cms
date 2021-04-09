package com.tarzan.cms.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:46
  */
@Data
@TableName("of_sys_user_site")
public class SysUserSiteEntity {
	//用户角色编号
	private Integer userRoleId;
	//角色编号
	private Integer roleId;
	//站点编号
	private Integer siteId;
	//数据状态:1、正常 0、删除；默认值：1
	private String status;
} 
