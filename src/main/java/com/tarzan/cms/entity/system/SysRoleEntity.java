package com.tarzan.cms.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:46
  */
@Data
@TableName("of_sys_role")
public class SysRoleEntity {
	//角色编号
	private Integer roleId;
	//角色名称
	private String roleName;
	//角色描述
	private String roleDesc;
	//0：后台管理型，对应菜单模式
            1：app管理型，对应参数模式
	private String roleType;
	//创建时间
	private Date createTime;
	//修改时间
	private Date updateTime;
	//数据状态:1 、有效0、失效
	private String status;
	//备注
	private String remark;
} 
