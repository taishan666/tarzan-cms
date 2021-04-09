package com.tarzan.cms.entity.cms;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:45
  */
@Data
@TableName("of_cms_form")
public class CmsFormEntity {
	//表单编号
	private Integer formId;
	//表单分类
	private Integer catId;
	//1、内容 2、栏目3、单页 4、其它
	private Integer type;
	//表单名称
	private String formName;
	//表单描述
	private String formDesc;
	//表单参数
	private String formParams;
	//扩展参数
	private String extParams;
	//创建时间
	private Date createTime;
	//修改时间
	private Date updateTime;
	//0、删除 1、启用 2、停用
	private Integer status;
	//备注
	private String remark;
} 
