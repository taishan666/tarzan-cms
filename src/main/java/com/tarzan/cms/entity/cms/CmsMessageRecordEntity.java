package com.tarzan.cms.entity.cms;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:45
  */
@Data
@TableName("of_cms_message_record")
public class CmsMessageRecordEntity {
	//记录编号
	private Integer id;
	//应用编号
	private String appId;
	//消息编号
	private Integer messageId;
	//用户编号
	private String userId;
	//是否已读0、未读 1、已读；默认值：0
	private Boolean isRead;
	//发送时间
	private Date sendTime;
	//阅读时间
	private Date readTime;
	//状态0、未发送1、已发送成功 2、发送失败 ；默认值：0
	private Boolean status;
	//备注
	private String remark;
} 
