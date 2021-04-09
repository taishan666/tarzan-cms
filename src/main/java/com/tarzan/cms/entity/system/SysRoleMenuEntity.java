package com.tarzan.cms.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:46
  */
@Data
@TableName("of_sys_role_menu")
public class SysRoleMenuEntity {
	//角色功能编号
	private Integer roleMenuId;
	//角色编号
	private Integer roleId;
	//功能编号
	private Integer menuId;
	//数据状态:0、删除1、正常；默认值：1
	private String status;
} 
