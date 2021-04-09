package com.tarzan.cms.entity.cms;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:45
  */
@Data
@TableName("of_cms_file")
public class CmsFileEntity {
	//文件编号
	private Integer fileId;
	//站点编号
	private Integer siteId;
	//文件名称
	private String fileName;
	//文件目录
	private String filePath;
	//分类1、图片 2、文件
	private String type;
	//创建时间
	private Date createTime;
	//修改时间
	private Date updatePerson;
	//0、删除 1、正常
	private String status;
	//备注
	private String remark;
} 
