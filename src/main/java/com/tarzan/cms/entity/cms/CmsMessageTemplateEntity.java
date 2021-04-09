package com.tarzan.cms.entity.cms;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:45
  */
@Data
@TableName("of_cms_message_template")
public class CmsMessageTemplateEntity {
	//模板编号
	private Integer templateId;
	//应用编号
	private String appId;
	//模板标题
	private String templateTitle;
	//行业
	private String templateIndustry;
	//详情内容
	private String templateContent;
	//创建时间
	private Date createTime;
	//修改时间
	private Date updateTime;
	//模板类型
	private String templateType;
	//状态0、删除 1、启用 2、禁用
	private Boolean templateStatus;
	//备注
	private String remark;
} 
