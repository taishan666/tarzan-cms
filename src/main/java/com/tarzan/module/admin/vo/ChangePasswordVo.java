package com.tarzan.module.admin.vo;

import lombok.Data;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Data
public class ChangePasswordVo {

    String oldPassword;
    String newPassword;
    String confirmNewPassword;

}
