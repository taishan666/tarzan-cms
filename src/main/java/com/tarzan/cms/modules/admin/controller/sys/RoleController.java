package com.tarzan.cms.modules.admin.controller.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tarzan.cms.cache.RoleCache;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.modules.admin.model.sys.Menu;
import com.tarzan.cms.modules.admin.model.sys.Role;
import com.tarzan.cms.modules.admin.model.sys.User;
import com.tarzan.cms.modules.admin.service.sys.MenuService;
import com.tarzan.cms.modules.admin.service.sys.RoleService;
import com.tarzan.cms.modules.admin.vo.PermissionTreeListVo;
import com.tarzan.cms.modules.admin.vo.base.PageResultVo;
import com.tarzan.cms.modules.admin.vo.base.ResponseVo;
import com.tarzan.cms.shiro.MyShiroRealm;
import com.tarzan.cms.utils.ResultUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 后台角色配置
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Slf4j
@Controller
@RequestMapping("/role")
@AllArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final MenuService MenuService;
    private final MyShiroRealm myShiroRealm;


    @GetMapping("/add")
    public String add() {
        return CoreConst.ADMIN_PREFIX + "role/form";
    }

    /*角色列表数据*/
    @PostMapping("/list")
    @ResponseBody
    public PageResultVo pageRoles(Role role, Integer pageNumber, Integer pageSize) {
        try {
            IPage<Role> rolePage = roleService.selectRoles(role, pageNumber, pageSize);
            return ResultUtil.table(rolePage.getRecords(), rolePage.getTotal());
        } catch (Exception e) {
            log.error(String.format("RoleController.loadRoles%s", e));
            throw e;
        }
    }

    /*新增角色*/
    @PostMapping("/add")
    @ResponseBody
    public ResponseVo addRole(Role role) {
        try {
            boolean flag = roleService.insert(role);
            if (flag) {
                RoleCache.save(role);
                return ResultUtil.success("添加角色成功");
            } else {
                return ResultUtil.error("添加角色失败");
            }
        } catch (Exception e) {
            log.error(String.format("RoleController.addRole%s", e));
            throw e;
        }
    }

    /*删除角色*/
    @PostMapping("/delete")
    @ResponseBody
    public ResponseVo deleteRole(Integer id) {
        return batchDeleteRole(Arrays.asList(id));
    }

    /*批量删除角色*/
    @PostMapping("/batch/delete")
    @ResponseBody
    public ResponseVo batchDeleteRole(@RequestBody List<Integer> ids) {
        if (CollectionUtils.isNotEmpty(roleService.findByRoleIds(ids))) {
            return ResultUtil.error("删除失败,选择的角色下存在用户");
        }
        boolean flag = roleService.updateStatusBatch(ids, CoreConst.STATUS_INVALID);
        if (flag) {
            RoleCache.delete(ids);
            return ResultUtil.success("删除角色成功");
        } else {
            return ResultUtil.error("删除角色失败");
        }
    }

    /*编辑角色详情*/
    @GetMapping("/edit")
    public String detail(Model model, Integer id) {
        Role role = roleService.getById(id);
        model.addAttribute("role", role);
        return CoreConst.ADMIN_PREFIX + "role/form";
    }

    /*编辑角色*/
    @PostMapping("/edit")
    @ResponseBody
    public ResponseVo editRole(@ModelAttribute("role") Role role) {
        boolean flag = roleService.updateById(role);
        if (flag) {
            RoleCache.save(role);
            return ResultUtil.success("编辑角色成功");
        } else {
            return ResultUtil.error("编辑角色失败");
        }
    }

    /*分配权限列表查询*/
    @PostMapping("/assign/menu/list")
    @ResponseBody
    public List<PermissionTreeListVo> assignRole(Integer id) {
        List<PermissionTreeListVo> listVos = new ArrayList<>();
        List<Menu> allMenus = MenuService.selectAll(CoreConst.STATUS_VALID);
        List<Menu> hasMenus = roleService.findPermissionsByRoleId(id);
        for (Menu menu : allMenus) {
            PermissionTreeListVo vo = new PermissionTreeListVo();
            vo.setId(menu.getId());
            vo.setMenuId(menu.getId());
            vo.setName(menu.getName());
            vo.setParentId(menu.getParentId());
            for (Menu hasMenu : hasMenus) {
                //有权限则选中
                if (hasMenu.getId().equals(menu.getId())) {
                    vo.setChecked(true);
                    break;
                }
            }
            listVos.add(vo);
        }
        return listVos;
    }


    /*分配权限*/
    @PostMapping("/assign/menu")
    @ResponseBody
    public ResponseVo assignRole(Integer id, String menuIdStr) {
        List<String> menuIdsList = new ArrayList<>();
        if (StringUtils.isNotBlank(menuIdStr)) {
            String[] permissionIds = menuIdStr.split(",");
            menuIdsList = Arrays.asList(permissionIds);
        }
        try {
            roleService.addAssignPermission(id, menuIdsList);
            /*重新加载角色下所有用户权限*/
            List<User> userList = roleService.findByRoleId(id);
            if (!userList.isEmpty()) {
                List<Integer> userIds = new ArrayList<>();
                for (User user : userList) {
                    userIds.add(user.getId());
                }
                myShiroRealm.clearAuthorizationByUserId(userIds);
            }
            return ResultUtil.success("分配权限成功");
        } catch (Exception e) {
            return ResultUtil.error("分配权限失败");
        }
    }

}
