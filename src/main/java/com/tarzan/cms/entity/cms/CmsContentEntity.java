package com.tarzan.cms.entity.cms;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:45
  */
@Data
@TableName("of_cms_content")
public class CmsContentEntity {
	//内容编号
	private Integer contentId;
	//内容编码
	private String contentCode;
	//站点编号
	private Integer siteId;
	//栏目编码
	private Integer columnId;
	//模型模板
	private Integer formId;
	//主题编号
	private Integer topicId;
	//模板路径
	private String templatePath;
	//外部链接
	private String contentUrl;
	//标题名称
	private String titleName;
	//标题图片
	private String titleUrl;
	//附件
	private String annex;
	//页面标题
	private String title;
	//关键词
	private String keywords;
	//描述文字
	private String description;
	//Tag标签
	private String tag;
	//是否推荐0、否1、是
	private Integer isRecommend;
	//是否置顶0、否1、是
	private Integer isTop;
	//是否首页显示0、否1、是
	private Integer isShow;
	//点击数；默认值：0
	private Integer clicks;
	//发布人
	private String createPeople;
	//发布时间
	private Date createTime;
	//修改时间
	private Date updateTime;
	//审核状态0、待审核  1、通过 2、未通过
	private Integer checkStatus;
	//状态:0、删除 1、正常 2、回收站
	private Integer status;
	//备注
	private String remark;
} 
