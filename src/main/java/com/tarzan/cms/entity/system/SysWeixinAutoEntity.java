package com.tarzan.cms.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:46
  */
@Data
@TableName("of_sys_weixin_auto")
public class SysWeixinAutoEntity {
	//
	private Integer id;
	//关键字
	private String autoKey;
	//内容
	private String content;
	//是否删除；默认值：1
	private String isDel;
	//状态；默认值：1
	private String status;
	//创建时间
	private Date created;
	//修改时间
	private Date updated;
} 
