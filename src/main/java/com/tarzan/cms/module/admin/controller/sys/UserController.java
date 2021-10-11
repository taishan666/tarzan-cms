package com.tarzan.cms.module.admin.controller.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.module.admin.model.sys.Role;
import com.tarzan.cms.module.admin.model.sys.User;
import com.tarzan.cms.module.admin.service.sys.RoleService;
import com.tarzan.cms.module.admin.service.sys.UserService;
import com.tarzan.cms.module.admin.vo.base.PageResultVo;
import com.tarzan.cms.module.admin.vo.base.ResponseVo;
import com.tarzan.cms.shiro.MyShiroRealm;
import com.tarzan.cms.utils.PasswordHelper;
import com.tarzan.cms.utils.ResultUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 后台用户配置
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final MyShiroRealm myShiroRealm;
    private final UserService userService;
    private final RoleService roleService;



    /**
     * 用户列表数据
     */
    @PostMapping("/list")
    @ResponseBody
    public PageResultVo loadUsers(User user, Integer pageNumber, Integer pageSize) {
        IPage<User> userPage = userService.selectUsers(user, pageNumber, pageSize);
        return ResultUtil.table(userPage.getRecords(), userPage.getTotal());
    }

    @GetMapping("/add")
    public String add() {
        return CoreConst.ADMIN_PREFIX + "user/form";
    }

    /**
     * 新增用户
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseVo add(User userForm, String confirmPassword) {
        String username = userForm.getUsername();
        User user = userService.selectByUsername(username);
        if (null != user) {
            return ResultUtil.error("用户名已存在");
        }
        String password = userForm.getPassword();
        //判断两次输入密码是否相等
        if (confirmPassword != null && password != null) {
            if (!confirmPassword.equals(password)) {
                return ResultUtil.error("两次密码不一致");
            }
        }
        userForm.setStatus(CoreConst.STATUS_VALID);
        Date date = new Date();
        userForm.setCreateTime(date);
        userForm.setUpdateTime(date);
        userForm.setLastLoginTime(date);
        PasswordHelper.encryptPassword(userForm);
        boolean flag = userService.register(userForm);
        if (flag) {
            return ResultUtil.success("添加用户成功");
        } else {
            return ResultUtil.error("添加用户失败");
        }
    }

    /**
     * 编辑用户详情
     */
    @GetMapping("/edit")
    public String userDetail(Model model, Integer id) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return CoreConst.ADMIN_PREFIX + "user/form";
    }


    /**
     * 编辑用户
     */
    @PostMapping("/edit")
    @ResponseBody
    public ResponseVo editUser(User userForm) {
        boolean flag = userService.updateByUserId(userForm);
        if (flag) {
            return ResultUtil.success("编辑用户成功！");
        } else {
            return ResultUtil.error("编辑用户失败");
        }
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResponseVo deleteUser(Integer id) {
        List<Integer> userIdsList = Arrays.asList(id);
        boolean flag = userService.updateStatusBatch(userIdsList, CoreConst.STATUS_INVALID);
        if (flag) {
            return ResultUtil.success("删除用户成功");
        } else {
            return ResultUtil.error("删除用户失败");
        }
    }

    /**
     * 批量删除用户
     */
    @PostMapping("/batch/delete")
    @ResponseBody
    public ResponseVo batchDeleteUser(@RequestParam("ids[]") Integer[] ids) {
        List<Integer> userIdsList = Arrays.asList(ids);
        boolean a = userService.updateStatusBatch(userIdsList, CoreConst.STATUS_INVALID);
        if (a) {
            return ResultUtil.success("删除用户成功");
        } else {
            return ResultUtil.error("删除用户失败");
        }
    }

    /**
     * 分配角色列表查询
     */
    @PostMapping("/assign/role/list")
    @ResponseBody
    public Map<String, Object> assignRoleList(Integer id) {
        List<Role> roleList = roleService.list(Wrappers.<Role>lambdaQuery().eq(Role::getStatus, CoreConst.STATUS_VALID));
        Set<String> hasRoles = roleService.findRoleByUserId(id);
        Map<String, Object> jsonMap = new HashMap<>(2);
        jsonMap.put("rows", roleList);
        jsonMap.put("hasRoles", hasRoles);
        return jsonMap;
    }

    /**
     * 保存分配角色
     */
    @PostMapping("/assign/role")
    @ResponseBody
    public ResponseVo assignRole(Integer id, String roleIdStr) {
        String[] roleIds = roleIdStr.split(",");
        List<String> roleIdsList = Arrays.asList(roleIds);
        try {
            // 给用户分配角色
            userService.addAssignRole(id, roleIdsList);
            // 重置用户权限
            myShiroRealm.clearAuthorizationByUserId(Collections.singletonList(id));
            return ResultUtil.success("分配角色成功");
        } catch (Exception e) {
            return ResultUtil.error("分配角色失败");
        }
    }



}
