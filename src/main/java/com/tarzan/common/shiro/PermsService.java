package com.tarzan.common.shiro;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;

/**
 * js调用 thymeleaf 实现按钮权限
 *
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Component("perms")
public class PermsService {
    public boolean hasPerm(String Menu) {
        return SecurityUtils.getSubject().isPermitted(Menu);
    }
}
