package com.tarzan.cms.entity.cms;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:45
  */
@Data
@TableName("of_cms_link")
public class CmsLinkEntity {
	//链接编号
	private Integer linkId;
	//站点编号
	private Integer siteId;
	//链接名称
	private String linkName;
	//链接地址
	private String linkUrl;
	//链接图标
	private String linkImage;
	//点击数
	private Integer clicks;
	//创建时间
	private Date createTime;
	//修改时间
	private Date updateTime;
	//排序
	private String sort;
	//是否显示 0、不显1、显示
	private String isShow;
	//状态:0、删除 1、正常
	private String status;
	//备注
	private String remark;
} 
