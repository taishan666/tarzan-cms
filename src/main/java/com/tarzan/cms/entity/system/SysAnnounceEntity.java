package com.tarzan.cms.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:45
  */
@Data
@TableName("of_sys_announce")
public class SysAnnounceEntity {
	//公告ID
	private Integer id;
	//公告标题
	private String title;
	//公告内容
	private String content;
	//公告类型:1、系统公告 2、通知 3、推广
	private String type;
	//发布用户
	private String userId;
	//发布终端:1、app 2、微信 3、管理台
	private String releaseTerminal;
	//创建时间
	private Date createTime;
	//修改时间
	private Date updateTime;
	//排序 从 10 两位开始
	private String sort;
	//备注
	private String remark;
	//1、正常 0、作废
	private String status;
} 
