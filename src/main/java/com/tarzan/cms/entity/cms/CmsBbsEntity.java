package com.tarzan.cms.entity.cms;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:45
  */
@Data
@TableName("of_cms_bbs")
public class CmsBbsEntity {
	//留言编号
	private Integer bbsId;
	//站点编号
	private Integer siteId;
	//留言标题
	private String title;
	//留言内容
	private String content;
	//回复内容
	private String revContent;
	//手机号
	private Integer mobile;
	//邮箱
	private String email;
	//qq号
	private String qq;
	//点击数
	private Integer clicks;
	//创建时间
	private Date createTime;
	//修改时间
	private Date updateTime;
	//排序
	private Integer sort;
	//0、未审核 1、审核通过 2、审核不通过；默认值：0
	private Integer isCheck;
	//状态:0、删除 1、正常；默认值：1
	private Integer status;
	//备注
	private String remark;
} 
