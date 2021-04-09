package com.tarzan.cms.entity.cms;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:45
  */
@Data
@TableName("of_cms_column")
public class CmsColumnEntity {
	//栏目编号
	private Integer columnId;
	//站点编号
	private Integer siteId;
	//栏目编码
	private String columnCode;
	//上级栏目
	private String upColumnId;
	//表单编号
	private Integer formId;
	//栏目名称
	private String columnName;
	//栏目英文
	private String columnEnglish;
	//栏目描述
	private String columnDesc;
	//栏目内容
	private String columnContent;
	//栏目图
	private String columnImage;
	//模板路径
	private String templatePath;
	//外部链接
	private String contentUrl;
	//页面标题
	private String title;
	//关键词
	private String keywords;
	//描述文字
	private String description;
	//首页
	private String columnIndexPage;
	//列表页
	private String columnListPage;
	//内容页
	private String columnContentPage;
	//是否单页 1、是 0、不是
	private Integer isOpen;
	//是否显示 0、未显示 1、栏目页 2、单页
	private Integer isShow;
	//排序
	private Integer sort;
	//创建时间
	private Date createTime;
	//修改时间
	private Date upateTime;
	//状态:0、删除 1、正常
	private Integer status;
	//备注
	private String remark;
} 
