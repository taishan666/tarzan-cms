package com.tarzan.cms.auth.granter;

import com.tarzan.cms.auth.constant.AppConstant;
import com.tarzan.cms.auth.enums.TokenUserEnum;
import com.tarzan.cms.auth.model.UserInfo;
import com.tarzan.cms.auth.provider.ITokenGranter;
import com.tarzan.cms.auth.provider.TokenParameter;
import com.tarzan.cms.auth.support.Kv;
import com.tarzan.cms.modules.admin.service.sys.UserService;
import com.tarzan.cms.utils.DigestUtil;
import com.tarzan.cms.utils.Func;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * PasswordTokenGranter
 *
 * @author tarzan 
 */
@Component
@AllArgsConstructor
public class PasswordTokenGranter implements ITokenGranter {

    public static final String GRANT_TYPE = "password";

    private final UserService userService;

    @Override
    public UserInfo grant(TokenParameter tokenParameter) {
        Kv args =tokenParameter.getArgs();
        String username = args.getStr("username");
        String password = args.getStr("password");
        UserInfo userInfo=null;
        if (Func.isNoneBlank(username, password)) {
            // 获取用户类型
            String userType = tokenParameter.getArgs().getStr("userType");
            // 根据不同用户类型调用对应的接口返回数据，用户可自行拓展
            if (userType.equals(TokenUserEnum.WEB.getName())) {
                userInfo = userService.userInfo(AppConstant.DEFAULT_TENANT_ID, username, DigestUtil.hex(password));
            } else if (userType.equals(TokenUserEnum.APP.getName())) {
                userInfo = userService.userInfo(AppConstant.DEFAULT_TENANT_ID, username, DigestUtil.hex(password));
            } else {
                userInfo = userService.userInfo(AppConstant.DEFAULT_TENANT_ID, username, DigestUtil.hex(password));
            }
        }
        return userInfo;
    }


}
