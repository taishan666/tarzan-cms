package com.tarzan.cms.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:46
  */
@Data
@TableName("of_sys_weixin_menu")
public class SysWeixinMenuEntity {
	//菜单编号
	private Integer menuId;
	//上级菜单
	private Integer parentId;
	//菜单名称
	private String name;
	//菜单类型
	private String type;
	//菜单地址
	private String url;
	//关键
	private String menuKey;
	//图片ID
	private String mediaId;
	//是否删除；默认值：1
	private String isDel;
	//状态；默认值：1
	private String status;
	//创建时间
	private Date created;
	//修改时间
	private Date updated;
} 
