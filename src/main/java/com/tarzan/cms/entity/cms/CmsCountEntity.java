package com.tarzan.cms.entity.cms;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:45
  */
@Data
@TableName("of_cms_count")
public class CmsCountEntity {
	//编号
	private Integer id;
	//站点编号
	private Integer siteId;
	//统计日期
	private Long countDate;
	//统计时间
	private Long countTime;
	//今日内容数
	private Integer dayContentCount;
	//累计内容数
	private Integer totalContentCount;
	//今日访问数
	private Integer dayAccessCount;
	//累计访问数
	private Integer totalAccessCount;
	//今日评论数
	private Integer dayCommentCount;
	//累计评论数
	private Integer totalCommentCount;
	//今天留言数
	private Integer dayBbsCount;
	//累计留言数
	private Integer totalBbsCount;
	//创建时间
	private Date createTime;
} 
