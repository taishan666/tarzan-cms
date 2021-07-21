package com.tarzan.cms.module.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Data
public class DbBackupVO {

    private String fileName;
    private Long size;
    private String type;
    private String url;
    private Integer status;
    private Date createTime;

}
