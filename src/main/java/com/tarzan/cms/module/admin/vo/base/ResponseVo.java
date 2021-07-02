package com.tarzan.cms.module.admin.vo.base;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Data
@AllArgsConstructor
public class ResponseVo<T> {

    private Integer status;
    private String msg;
    private T data;

    public ResponseVo(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

}