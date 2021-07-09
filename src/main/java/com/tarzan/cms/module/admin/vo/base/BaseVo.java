package com.tarzan.cms.module.admin.vo.base;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Data
public abstract class BaseVo implements Serializable {
  //  private static final long serialVersionUID = 1L;


    private Integer id;

    private Date createTime;
    private Date updateTime;

}