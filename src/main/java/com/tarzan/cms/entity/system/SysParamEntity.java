package com.tarzan.cms.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:46
  */
@Data
@TableName("of_sys_param")
public class SysParamEntity {
	//参数编号
	private Integer paramId;
	//参数名称
	private String paramName;
	//参数值
	private String paramValue;
	//参数描述
	private String paramDesc;
	//参数分组:相同为一组
	private String paramGroup;
	//输入类型：‘text’-文本输入，‘int’-整数，‘number’-数字，‘select’-下拉单选，‘mselect'-下拉多选，‘date’-日期，‘time'-时间
	private String paramType;
	//是否显示：0、不显示 1、显示；默认值：1
	private String isShow;
	//状态:1、正常 0、作废
	private String status;
	//备注
	private String remark;
} 
