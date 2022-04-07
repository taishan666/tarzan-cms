package com.tarzan.cms.modules.admin.vo;

import lombok.Data;

/**
 * @author tarzan liu
 * @since JDK1.8
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
