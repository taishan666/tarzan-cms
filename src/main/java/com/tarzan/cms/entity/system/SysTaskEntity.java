package com.tarzan.cms.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:46
  */
@Data
@TableName("of_sys_task")
public class SysTaskEntity {
	//序号
	private Long jobId;
	//类路径
	private String classPath;
	//实例名称
	private String beanName;
	//实例描述
	private String jobDesc;
	//参数
	private String params;
	//表达式
	private String cronExpression;
	//创建时间
	private Date createTime;
	//状态: 0 删除 1未启动 2、已启动  3已停止      
            
	private String status;
	//备注
	private String remark;
} 
