package com.tarzan.cms.auth.endpoint;

import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.tarzan.cms.auth.cache.RetryLimitCache;
import com.tarzan.cms.auth.constant.CacheConstant;
import com.tarzan.cms.auth.constant.TokenConstant;
import com.tarzan.cms.auth.model.TokenUser;
import com.tarzan.cms.auth.model.UserInfo;
import com.tarzan.cms.auth.props.JwtProperties;
import com.tarzan.cms.auth.provider.ITokenGranter;
import com.tarzan.cms.auth.provider.TokenGranterBuilder;
import com.tarzan.cms.auth.provider.TokenParameter;
import com.tarzan.cms.auth.support.Kv;
import com.tarzan.cms.auth.utils.AuthUtil;
import com.tarzan.cms.auth.utils.JwtUtil;
import com.tarzan.cms.auth.utils.TokenUtil;
import com.tarzan.cms.modules.admin.service.sys.UserService;
import com.tarzan.cms.utils.Func;
import com.tarzan.cms.utils.RedisUtil;
import com.tarzan.cms.utils.WebUtil;
import com.wf.captcha.SpecCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;



/**
 * 令牌端点
 *
 * @author tarzan 
 */

@ApiSort(1)
@RestController
@AllArgsConstructor
@RequestMapping("auth")
@Api(value = "用户授权认证（授权接口）", tags = "用户授权认证（授权接口）", description = "用户授权认证（授权接口）")
public class BladeTokenEndPoint {

    private final RedisUtil redisUtil;
    private final JwtProperties jwtProperties;
    private final UserService userService;

    @PostMapping("/oauth/token")
    @ApiOperation(value = "获取认证令牌", notes = "传入租户ID:tenantId,账号:account,密码:password")
    public Kv token(@ApiParam(value = "账号", required = true) @RequestParam(required = false) String username,
        @ApiParam(value = "密码", required = true) @RequestParam(required = false) String password) {

        Kv authInfo = Kv.create();
        String grantType = WebUtil.getRequest().getParameter("grant_type");
        String refreshToken = WebUtil.getRequest().getParameter("refresh_token");

        String userType = Func.toStr(WebUtil.getRequest().getHeader(TokenUtil.USER_TYPE_HEADER_KEY), TokenUtil.DEFAULT_USER_TYPE);

        TokenParameter tokenParameter = new TokenParameter();
        tokenParameter.getArgs().set("tenantId", "000000").set("username", username).set("password", password).set("grantType", grantType).set("refreshToken", refreshToken).set("userType", userType);

        ITokenGranter granter = TokenGranterBuilder.getGranter(grantType);
        UserInfo userInfo = granter.grant(tokenParameter);
        if(isOverLimit(username,5)){
            return authInfo.set("error_code", HttpServletResponse.SC_BAD_REQUEST).set("error_description", "密码输错次数达到上限，请30分钟后重试");
        }
        if (userInfo == null || userInfo.getUser() == null) {
            return authInfo.set("error_code", HttpServletResponse.SC_BAD_REQUEST).set("error_description", "用户名或密码不正确");
        }

/*        if ((userInfo.getUser().getValidityType() == 1
            && (userInfo.getUser().getValidityStart().getTime() > System.currentTimeMillis() || userInfo.getUser().getValidityEnd().getTime() < System.currentTimeMillis()))) {
            return authInfo.set("error_code", HttpServletResponse.SC_BAD_REQUEST).set("error_description", "账号已过期");
        }*/

        if (userInfo.getUser().getStatus() == 2) {
            return authInfo.set("error_code", HttpServletResponse.SC_BAD_REQUEST).set("error_description", "账号被禁用无法登陆");
        }
/*        if (userInfo.getUser().getStatus() == 1) {
            RetryLimitCache.remove(username);
        }*/
        Kv kv = TokenUtil.createAuthInfo(userInfo);
        return kv;
    }

    public boolean isOverLimit(String username,Integer limit) {
        //设置次数
        AtomicInteger retryCount = RetryLimitCache.get(username);
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
        }
        if (retryCount.incrementAndGet() > limit) {
            //重试次数如果大于限制次数，就锁定
           return true;
        }
        //并将其保存到缓存中(有效时长30分)
        RetryLimitCache.put(username, retryCount, 1800L);
        return false;
    }



    @GetMapping("/oauth/logout")
    @ApiOperation(value = "退出登录")
    public Kv logout() {
        TokenUser user = AuthUtil.getUser();
        if (user != null && jwtProperties.getState()) {
            String token = JwtUtil.getTokenKey(WebUtil.getRequest().getHeader(TokenConstant.HEADER));
            JwtUtil.removeToken(user.getTenantId(), String.valueOf(user.getUserId()), token);
        }
        return Kv.create().set("success", "true").set("msg", "success");
    }

    @GetMapping("/oauth/captcha")
    @ApiOperation(value = "获取验证码")
    public Kv captcha() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        String verCode = specCaptcha.text().toLowerCase();
        String key = UUID.randomUUID().toString();
        // 存入redis并设置过期时间为30分钟
        redisUtil.setEx(CacheConstant.CAPTCHA_KEY + key, verCode, Duration.ofMinutes(30));
        // 将key和base64返回给前端
        return Kv.create().set("key", key).set("image", specCaptcha.toBase64());
    }

    @GetMapping("/oauth/clear-cache")
    @ApiOperation(value = "清除缓存")
    public Kv clearCache() {
        redisUtil.del(CacheConstant.USER_CACHE,CacheConstant.CAPTCHA_KEY);
        return Kv.create().set("success", "true").set("msg", "success");
    }
}
