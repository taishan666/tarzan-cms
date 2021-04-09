package com.tarzan.cms.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:45
  */
@Data
@TableName("of_sys_menu")
public class SysMenuEntity {
	//功能编号
	private Integer menuId;
	//上级功能编号
	private Integer parentId;
	//菜单名称
	private String name;
	//url连接
	private String url;
	//权限
	private String perms;
	//功能类型
	private String type;
	//菜单图标
	private String icon;
	//菜单显示序号
	private Integer orderNum;
	//1 在用
            0 不在用
	private String status;
	//备注
	private String remark;
} 
