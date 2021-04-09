package com.tarzan.cms.entity.cms;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:45
  */
@Data
@TableName("of_cms_access")
public class CmsAccessEntity {
	//访问编号
	private Integer accessId;
	//站点编号
	private Integer siteId;
	//访问IP
	private String accessIp;
	//访问进入页面
	private String accessEntryPage;
	//访问最后页面
	private String accessLastPage;
	//访问日期
	private Long accessDate;
	//访问时间
	private Long accessTime;
	//访问来源
	private String accessSource;
	//访问关键字
	private String accessKeyword;
} 
