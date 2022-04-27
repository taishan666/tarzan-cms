package com.tarzan.cms.auth.granter;

import com.aliyun.oss.ServiceException;
import com.tarzan.cms.auth.constant.AppConstant;
import com.tarzan.cms.auth.constant.CacheConstant;
import com.tarzan.cms.auth.enums.TokenUserEnum;
import com.tarzan.cms.auth.model.UserInfo;
import com.tarzan.cms.auth.provider.ITokenGranter;
import com.tarzan.cms.auth.provider.TokenParameter;
import com.tarzan.cms.auth.utils.TokenUtil;
import com.tarzan.cms.modules.admin.service.sys.UserService;
import com.tarzan.cms.utils.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证码TokenGranter
 *
 * @author tarzan 
 */
@Component
@AllArgsConstructor
public class CaptchaTokenGranter implements ITokenGranter {

    public static final String GRANT_TYPE = "captcha";

    private final UserService userService;
    private final RedisUtil redisUtil;

    @Override
    public UserInfo grant(TokenParameter tokenParameter) {
        HttpServletRequest request = WebUtil.getRequest();

        String key = request.getHeader(TokenUtil.CAPTCHA_HEADER_KEY);
        String code = request.getHeader(TokenUtil.CAPTCHA_HEADER_CODE);
        // 获取验证码
        String redisCode = redisUtil.get(CacheConstant.CAPTCHA_KEY + key);
        // 判断验证码
        if (code == null || !StringUtil.equalsIgnoreCase(redisCode, code)) {
            throw new ServiceException(TokenUtil.CAPTCHA_NOT_CORRECT);
        }

        String username = tokenParameter.getArgs().getStr("username");
        String password = tokenParameter.getArgs().getStr("password");
        UserInfo userInfo = null;
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
