package com.tarzan.cms.modules.admin.controller.biz;

import com.tarzan.cms.utils.ResultUtil;
import com.tarzan.cms.modules.admin.service.sys.UserService;
import com.tarzan.cms.modules.admin.vo.UserOnlineVo;
import com.tarzan.cms.modules.admin.vo.UserSessionVo;
import com.tarzan.cms.modules.admin.vo.base.PageResultVo;
import com.tarzan.cms.modules.admin.vo.base.ResponseVo;
import lombok.AllArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.List;

/**
 * 后台在线用户管理
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Controller
@RequestMapping("/online/user")
@AllArgsConstructor
public class OnlineUserController {

    private final UserService userService;

    // 在线用户列表
    @PostMapping("/list")
    @ResponseBody
    public PageResultVo onlineUsers(UserOnlineVo user, Integer pageNumber, Integer pageSize) {
        List<UserOnlineVo> userList = userService.selectOnlineUsers(user);
        int endIndex = Math.min(pageNumber * pageSize, userList.size());
        return ResultUtil.table(userList.subList((pageNumber - 1) * pageSize, endIndex), (long) userList.size());
    }

    // 强制踢出用户
    @PostMapping("/kickOut")
    @ResponseBody
    public ResponseVo kickOut(String sessionId, String username) {
        try {
            if (SecurityUtils.getSubject().getSession().getId().equals(sessionId)) {
                return ResultUtil.error("不能踢出自己");
            }
            userService.kickOut(sessionId, username);
            return ResultUtil.success("踢出用户成功");
        } catch (Exception e) {
            return ResultUtil.error("踢出用户失败");
        }
    }

    // 批量强制踢出用户
    @PostMapping("/batch/kickOut")
    @ResponseBody
    public ResponseVo kickOut(@RequestBody List<UserSessionVo> sessions) {
        try {
            //要踢出的用户中是否有自己
            boolean hasOwn = false;
            Serializable sessionId = SecurityUtils.getSubject().getSession().getId();
            for (UserSessionVo sessionVo : sessions) {
                if (sessionVo.getSessionId().equals(sessionId)) {
                    hasOwn = true;
                } else {
                    userService.kickOut(sessionVo.getSessionId(), sessionVo.getUsername());
                }


            }
            if (hasOwn) {
                return ResultUtil.success("不能踢出自己");
            }
            return ResultUtil.success("踢出用户成功");
        } catch (Exception e) {
            return ResultUtil.error("踢出用户失败");
        }
    }
}
