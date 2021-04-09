package com.tarzan.cms.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:46
  */
@Data
@TableName("of_sys_sequence")
public class SysSequenceEntity {
	//序列名 采用表名
	private String name;
	//开始值；默认值：1
	private Integer startValue;
	//最大值；默认值：1000000
	private Integer maxValue;
	//当前值；默认值：0
	private Integer currentValue;
	//增量；默认值：1
	private Integer increment;
} 
