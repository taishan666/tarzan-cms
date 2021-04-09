package com.tarzan.cms.entity.cms;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:45
  */
@Data
@TableName("of_cms_api_account")
public class CmsApiAccountEntity {
	//编号
	private Integer id;
	//应用编号
	private String appId;
	//应用密钥
	private String appKey;
	//AES加解密密钥
	private String aesKey;
	//是否禁用0、未禁用 1、禁用
	private Boolean disabled;
	//状态:0、删除 1、正常 
	private Integer status;
	//备注
	private String remark;
} 
