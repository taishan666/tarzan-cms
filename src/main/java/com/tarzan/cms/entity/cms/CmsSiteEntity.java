package com.tarzan.cms.entity.cms;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:45
  */
@Data
@TableName("of_cms_site")
public class CmsSiteEntity {
	//站点编号
	private Integer siteId;
	//站点名称
	private String siteName;
	//站点简称
	private String sitePath;
	//关键字
	private String keywords;
	//域名
	private String domainName;
	//访问协议
	private String accessProtocol;
	//访问地址
	private String accessPath;
	//模板名称
	private String templateName;
	//模板路径
	private String templatePath;
	//是否主站:0、否1、是
	private String isMaster;
	//排序
	private String sort;
	//创建时间
	private Date createTime;
	//修改时间
	private Date updateTime;
	//0、删除 1、正常
	private String status;
	//备注
	private String remark;
} 
