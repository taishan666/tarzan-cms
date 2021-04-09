package com.tarzan.cms.entity.cms;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:45
  */
@Data
@TableName("of_cms_api")
public class CmsApiEntity {
	//接口编号
	private Integer apiId;
	//接口名称
	private String apiName;
	//接口代码
	private String apiCode;
	//接口地址
	private String apiUrl;
	//每日限制调用次数(0无限制)
	private Integer limitCallDay;
	//总调用次数
	private Integer callTotalCount;
	//月调用次数
	private Integer callMonthCount;
	//周调用次数
	private Integer callWeekCount;
	//日调用次数
	private Integer callDayCount;
	//是否禁用0、未禁用 1、禁用
	private Boolean disabled;
	//状态:0、删除 1、正常 
	private Integer status;
	//备注
	private String remark;
} 
