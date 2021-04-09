package com.tarzan.cms.entity.cms;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:45
  */
@Data
@TableName("of_cms_content_field")
public class CmsContentFieldEntity {
	//字段编号
	private Long fieldId;
	//内容编码
	private Integer contentId;
	//模型模板
	private Integer formId;
	//字段名称
	private String name;
	//填写内容
	private String value;
	//排序
	private Integer sort;
	//备注
	private String remark;
} 
