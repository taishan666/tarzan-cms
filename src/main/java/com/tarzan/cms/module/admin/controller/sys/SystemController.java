package com.tarzan.cms.module.admin.controller.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.common.enums.UserEnum;
import com.tarzan.cms.common.event.LoginLogEvent;
import com.tarzan.cms.module.admin.model.biz.Category;
import com.tarzan.cms.module.admin.model.log.LoginLog;
import com.tarzan.cms.module.admin.model.sys.Menu;
import com.tarzan.cms.module.admin.model.sys.User;
import com.tarzan.cms.module.admin.service.biz.CategoryService;
import com.tarzan.cms.module.admin.service.log.LoginLogService;
import com.tarzan.cms.module.admin.service.sys.MenuService;
import com.tarzan.cms.module.admin.service.sys.UserService;
import com.tarzan.cms.module.admin.vo.ChangePasswordVo;
import com.tarzan.cms.module.admin.vo.base.ResponseVo;
import com.tarzan.cms.shiro.MyShiroRealm;
import com.tarzan.cms.utils.*;
import com.wf.captcha.utils.CaptchaUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 后台首页、登录等接口
 *
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/system")
public class SystemController {

    private final UserService userService;
    private final MenuService MenuService;
    private final CategoryService categoryService;
    private final LoginLogService loginLogService;
    private final MyShiroRealm shiroRealm;



    //注册
    @GetMapping(value = "/register")
    public String register(Model model){
        Category category = new Category();
        category.setStatus(CoreConst.STATUS_VALID);
        model.addAttribute("categoryList",categoryService.selectCategories(category));
        return "system/register";
    }

    //提交注册
    @PostMapping("/register")
    @ResponseBody
    public ResponseVo register(HttpServletRequest request, User registerUser, String confirmPassword, String verification){
        //判断验证码
        if (!CaptchaUtil.ver(verification, request)) {
            // 清除session中的验证码
            CaptchaUtil.clear(request);
            return ResultUtil.error("验证码错误！");
        }
        String username = registerUser.getUsername();
        User user = userService.selectByUsername(username);
        if (null != user) {
            return ResultUtil.error("用户名已存在！");
        }
        String password = registerUser.getPassword();
        //判断两次输入密码是否相等
        if (confirmPassword != null && password != null) {
            if (!confirmPassword.equals(password)) {
                return ResultUtil.error("两次密码不一致！");
            }
        }
        registerUser.setStatus(CoreConst.STATUS_VALID);
        Date date = new Date();
        registerUser.setCreateTime(date);
        registerUser.setUpdateTime(date);
        registerUser.setLastLoginTime(date);
        PasswordHelper.encryptPassword(registerUser);
        //注册
        boolean flag = userService.register(registerUser);
        if(flag){
            return ResultUtil.success("注册成功！");
        }else {
            return ResultUtil.error("注册失败，请稍后再试！");
        }
    }

    /**
     * 访问登录
     *
     * @param model
     */
    @GetMapping("/login")
    public ModelAndView login(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        if (SecurityUtils.getSubject().isAuthenticated()) {
            modelAndView.setView(new RedirectView("/admin", true, false));
            return modelAndView;
        }
        model.addAttribute("categoryList", categoryService.selectCategories(new Category().setStatus(CoreConst.STATUS_VALID)));
        modelAndView.setViewName("system/login");
        return modelAndView;
    }

    /**
     * 提交登录
     *
     * @param request
     * @param username
     * @param password
     * @param verification
     * @param rememberMe
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public ResponseVo login(HttpServletRequest request, String username, String password, String verification,
                            @RequestParam(value = "rememberMe", defaultValue = "0") Integer rememberMe) {
        //判断验证码
        if (!CaptchaUtil.ver(verification, request)) {
            // 清除session中的验证码
            CaptchaUtil.clear(request);
            return ResultUtil.error("验证码错误！");
        }
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            token.setRememberMe(1 == rememberMe);
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
        } catch (LockedAccountException e) {
            token.clear();
            return ResultUtil.error("用户已经被锁定不能登录，请联系管理员！");
        } catch (AuthenticationException e) {
            token.clear();
            return ResultUtil.error("用户名或者密码错误！");
        }
        //更新最后登录时间
        User user=(User) SecurityUtils.getSubject().getPrincipal();
        userService.updateLastLoginTime(user);
        //异步保存登录日志
        String userType= request.getHeader(CoreConst.USER_TYPE_HEADER_KEY)==null?UserEnum.WEB.getName():request.getHeader(CoreConst.USER_TYPE_HEADER_KEY);
        saveLoginLog(userType,user);
        return ResultUtil.success("登录成功！");
    }


    /**
     * 保存日志
     *
     * @param userInfo
     * @return
     */
    private void saveLoginLog(String userType,User userInfo) {
        Date now = new Date();
        LoginLog loginLog = new LoginLog();
        loginLog.setCreateTime(now);
        loginLog.setStartTime(now);
        loginLog.setSourceIp(IpUtil.getIpAddr(WebUtil.getRequest()));
        if (userType.equals(UserEnum.WEB.getName())) {
            loginLog.setSource("PC登录");
        } else if (userType.equals(UserEnum.APP.getName())) {
            loginLog.setSource("APP登录");
        }
        loginLog.setName(userInfo.getNickname());
        loginLog.setPhone(userInfo.getPhone());
        loginLog.setLoginName(userInfo.getUsername());
        SpringUtil.publishEvent(new LoginLogEvent(loginLog));
    }

    /**
     * 踢出
     *
     * @param model
     * @return
     */
    @GetMapping("/kickOut")
    public String kickOut(Model model) {
        model.addAttribute("categoryList", categoryService.selectCategories(new Category().setStatus(CoreConst.STATUS_VALID)));
        return "system/kickOut";
    }

    /**
     * 登出
     *
     * @return
     */
    @RequestMapping("/logout")
    public ModelAndView logout() {
        Subject subject = SecurityUtils.getSubject();
        if (null != subject) {
            String username = ((User) SecurityUtils.getSubject().getPrincipal()).getUsername();
            Serializable sessionId = SecurityUtils.getSubject().getSession().getId();
            userService.kickOut(sessionId, username);
            QueryWrapper<LoginLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(LoginLog::getLoginName, username);
            List<LoginLog> loginLogList = loginLogService.list(queryWrapper.lambda().orderByDesc(LoginLog::getCreateTime));
            if (CollectionUtils.isNotEmpty(loginLogList)) {
                LoginLog loginLog = loginLogList.get(0);
                loginLog.setEndTime(new Date());
                SpringUtil.publishEvent(new LoginLogEvent(loginLog));
            }
        }
        subject.logout();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("system/login");
        return modelAndView;
    }

    /**
     * 获取当前登录用户的菜单
     *
     * @return
     */
    @PostMapping("/menu")
    @ResponseBody
    public List<Menu> getMenus() {
        return MenuService.selectMenuByUserId(((User) SecurityUtils.getSubject().getPrincipal()).getId());
    }


    /**
     * 编辑个人信息
     */
    @GetMapping("/userInfo")
    public String userDetail(Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("user", user);
        return CoreConst.ADMIN_PREFIX + "user/info";
    }

    /**
     * 修改密码
     */
    @GetMapping("/password")
    public String password(Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("user", user);
        return CoreConst.ADMIN_PREFIX + "user/password";
    }


    /**
     * 修改密码
     */
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    @ResponseBody
    public ResponseVo changePassword(ChangePasswordVo changePasswordVo) {
        if (!changePasswordVo.getNewPassword().equals(changePasswordVo.getConfirmNewPassword())) {
            return ResultUtil.error("两次密码输入不一致");
        }
        User loginUser = userService.selectByUserId(((User) SecurityUtils.getSubject().getPrincipal()).getId());
        User newUser = BeanUtil.copy(loginUser, User.class);
        String sysOldPassword = loginUser.getPassword();
        newUser.setPassword(changePasswordVo.getOldPassword());
        String entryOldPassword = PasswordHelper.getPassword(newUser);
        if (sysOldPassword.equals(entryOldPassword)) {
            newUser.setPassword(changePasswordVo.getNewPassword());
            PasswordHelper.encryptPassword(newUser);
            userService.updateById(newUser);
            //*清除登录缓存*//
            List<Integer> userIds = new ArrayList<>();
            userIds.add(loginUser.getId());
            shiroRealm.removeCachedAuthenticationInfo(userIds);
        } else {
            return ResultUtil.error("您输入的旧密码有误");
        }
        return ResultUtil.success("修改密码成功");
    }




}
