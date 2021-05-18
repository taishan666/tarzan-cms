package com.tarzan.module.admin.vo;

import lombok.Data;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Data
public class PermissionTreeListVo {
    private Integer id;
    private Integer menuId;
    private String name;
    private Integer parentId;
    private Boolean open = true;
    private Boolean checked = false;
}
