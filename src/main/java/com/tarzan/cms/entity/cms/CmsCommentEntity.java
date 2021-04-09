package com.tarzan.cms.entity.cms;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:45
  */
@Data
@TableName("of_cms_comment")
public class CmsCommentEntity {
	//评论编号
	private Integer commentId;
	//站点编号
	private Integer siteId;
	//内容编号
	private Integer contentId;
	//评论类型；默认值：1
	private Integer commentType;
	//评论标题
	private String commentTitle;
	//评论图片
	private String commentUrl;
	//评论内容
	private String commentContent;
	//评论人
	private String commentName;
	//评论时间
	private Date commentTime;
	//ip地址
	private String commentIp;
	//发布时间
	private Date createTime;
	//审核状态0、待审核  1、通过 2、未通过；默认值：0
	private Integer checkStatus;
	//状态:0、删除 1、正常 2、禁止；默认值：1
	private Integer status;
	//备注
	private String remark;
} 
