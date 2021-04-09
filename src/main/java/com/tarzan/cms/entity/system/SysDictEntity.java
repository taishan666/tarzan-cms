package com.tarzan.cms.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:45
  */
@Data
@TableName("of_sys_dict")
public class SysDictEntity {
	//字典编号
	private Integer dictId;
	//字典名称
	private String dictName;
	//字典值
	private String dictValue;
	//字典描述
	private String dictDesc;
	//字典分组
	private String dictGroup;
	//状态:1、正常 0、作废
	private String status;
} 
