package com.tarzan.cms.entity.cms;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:45
  */
@Data
@TableName("of_cms_ad")
public class CmsAdEntity {
	//轮播图图编号
	private Integer adId;
	//站点编号
	private Integer siteId;
	//轮播图名称
	private String adName;
	//广告版位
	private String adEdition;
	//广告图片地址
	private String adImageUrl;
	//广告类型:1.图片 2.文件3.代码
	private String adType;
	//图片跳转链接地址
	private String adJumpUrl;
	//提交日期时间
	private Date requestTime;
	//展现次数
	private Date startDate;
	//结束日期
	private Date stopDate;
	//显示的顺序
	private String sortOrder;
	//0.删除；1.启用；2.停用
	private String status;
	//备注
	private String remark;
} 
