package com.tarzan.cms.entity.cms;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:45
  */
@Data
@TableName("of_cms_topic")
public class CmsTopicEntity {
	//专题编号
	private Integer topicId;
	//站点编号
	private Integer siteId;
	//专题名称
	private String topicName;
	//专题地址
	private String topicUrl;
	//专题图标
	private String topicImage;
	//专题内容描述
	private String description;
	//专题内容图
	private String topicContentImage;
	//专题模板
	private String topicTemplate;
	//点击数
	private Integer clicks;
	//创建时间
	private Date createTime;
	//修改时间
	private Date updateTime;
	//排序
	private Integer sort;
	//是否显示 0、不显1、显示
	private Integer isShow;
	//状态:0、删除 1、正常
	private Integer status;
	//备注
	private String remark;
} 
