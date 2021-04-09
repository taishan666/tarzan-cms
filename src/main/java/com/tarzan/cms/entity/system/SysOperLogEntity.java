package com.tarzan.cms.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:46
  */
@Data
@TableName("of_sys_oper_log")
public class SysOperLogEntity {
	//序号
	private Integer logid;
	//用户编号
	private String userId;
	//用户名
	private String userName;
	//功能
	private String functionName;
	//功能代码
	private String businessCode;
	//如：100000 代表登录则填写100000，使用api接口编码
	private String operType;
	//操作日期
	private Long operDate;
	//操作时间
	private Date operTime;
	//操作内容描述
	private String operDesc;
	//1 在用
            0 不在用
	private String status;
	//备注
	private String remark;
} 
