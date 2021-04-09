package com.tarzan.cms.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
 /**
  * @author tarzan
  * @date 2021-18-99 18:05:46
  */
@Data
@TableName("of_sys_user")
public class SysUserEntity {
	//用户编号
	private Integer userId;
	//登录名
	private String loginName;
	//用户名称
	private String userName;
	//用户密码
	private String userPassword;
	//性别:1、男 2、女 3、未知
	private String userSex;
	//生日
	private Long userBirthday;
	//手机号
	private String userMobile;
	//邮箱
	private String userEmail;
	//用户头像
	private String faceImageUrl;
	//部门
	private Integer departmentId;
	//职务
	private String duties;
	//排序：数字越小，排名越靠前
	private String sort;
	//状态：0、禁止 1、正常
	private String status;
	//备注
	private String remark;
} 
