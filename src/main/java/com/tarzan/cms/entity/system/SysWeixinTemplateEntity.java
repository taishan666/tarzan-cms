package com.tarzan.cms.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:46
  */
@Data
@TableName("of_sys_weixin_template")
public class SysWeixinTemplateEntity {
	//序号
	private Integer id;
	//模版ID
	private String templateKey;
	//标题
	private String title;
	//一级行业
	private String firstclass;
	//二级行业
	private String secondclass;
	//详情
	private String content;
} 
