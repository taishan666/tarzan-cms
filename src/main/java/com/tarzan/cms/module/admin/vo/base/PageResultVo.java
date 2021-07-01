package com.tarzan.cms.module.admin.vo.base;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Data
@AllArgsConstructor
public class PageResultVo {
    private List rows;
    private Long total;

}
