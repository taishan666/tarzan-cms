package com.tarzan.cms.entity.cms;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:45
  */
@Data
@TableName("of_cms_field")
public class CmsFieldEntity {
	//字段编号
	private Integer fieldId;
	//表单编号
	private Integer formId;
	//字段名称
	private String fieldName;
	//字段描述
	private String fieldDesc;
	//提示文字
	private String fieldDefaultValue;
	//1、text 2、checkbox3、radio4、file 5、image 6、password 7、number 
	private String fieldType;
	//字段子类型
	private String fieldSubType;
	//字段排序
	private Integer fieldSort;
	//正则表达
	private String fieldRegular;
	//验证类型
	private String fieldVerification;
	//是否禁用:
	private Integer isDisabled;
	//是否必填
	private Integer isRequired;
	//是否打印
	private Integer isPrint;
	//是否默认0、系统默认1、不默认 
	private Integer isDefault;
	//状态0、删除 1、显示 2、不显
	private Integer status;
	//备注
	private String remark;
} 
