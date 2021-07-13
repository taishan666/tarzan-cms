package com.tarzan.cms.module.admin.controller.sys;

import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.shiro.ShiroService;
import com.tarzan.cms.utils.ResultUtil;
import com.tarzan.cms.module.admin.model.sys.Menu;
import com.tarzan.cms.module.admin.service.sys.MenuService;
import com.tarzan.cms.module.admin.vo.base.ResponseVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台权限配置管理
 *
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Slf4j
@Controller
@RequestMapping("/menu")
@AllArgsConstructor
public class MenuController {

    /**
     * 1:全部资源，2：菜单资源
     */
    private static final String[] MENU_FLAG = {"1", "2"};

    private final MenuService menuService;
    private final ShiroService shiroService;


    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("menu", new Menu().setType(0));
        return CoreConst.ADMIN_PREFIX + "menu/form";
    }

    /*权限列表数据*/
    @PostMapping("/list")
    @ResponseBody
    public List<Menu> loadMenus(String flag) {
        List<Menu> permissionListList = new ArrayList<Menu>();
        if (StringUtils.isBlank(flag) || MENU_FLAG[0].equals(flag)) {
            permissionListList = menuService.selectAll(CoreConst.STATUS_VALID);
        } else if (MENU_FLAG[1].equals(flag)) {
            permissionListList = menuService.selectAllMenuName(CoreConst.STATUS_VALID);
        }
        return permissionListList;
    }

    /*添加权限*/
    @ResponseBody
    @PostMapping("/add")
    public ResponseVo addMenu(Menu menu) {
        try {
            boolean flag = menuService.insert(menu);
            if (flag) {
                shiroService.updatePermission();
                return ResultUtil.success("添加权限成功");
            } else {
                return ResultUtil.error("添加权限失败");
            }
        } catch (Exception e) {
            log.error(String.format("MenuController.addMenu%s", e));
            throw e;
        }
    }

    /*删除权限*/
    @ResponseBody
    @PostMapping("/delete")
    public ResponseVo deleteMenu(Integer id) {
        try {
            int subPermsByPermissionIdCount = menuService.selectSubPermsByPermissionId(id);
            if (subPermsByPermissionIdCount > 0) {
                return ResultUtil.error("改资源存在下级资源，无法删除！");
            }
            int a = menuService.updateStatus(id, CoreConst.STATUS_INVALID);
            if (a > 0) {
                shiroService.updatePermission();
                return ResultUtil.success("删除权限成功");
            } else {
                return ResultUtil.error("删除权限失败");
            }
        } catch (Exception e) {
            log.error(String.format("MenuController.deleteMenu%s", e));
            throw e;
        }
    }

    /*权限详情*/
    @GetMapping("/edit")
    public ModelAndView detail(Model model, Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        Menu menu = menuService.getById(id);
        if (null != menu) {
            if (CoreConst.TOP_MENU_ID.equals(menu.getParentId())) {
                model.addAttribute("parentName", CoreConst.TOP_MENU_NAME);
            } else {
                Menu parent = menuService.getById(menu.getParentId());
                if (parent != null) {
                    model.addAttribute("parentName", parent.getName());
                }
            }
            model.addAttribute("menu", menu);
            modelAndView.setViewName(CoreConst.ADMIN_PREFIX + "menu/form");
        } else {
            log.error("根据权限id获取权限详情失败，权限id: {}", id);
            modelAndView.setView(new RedirectView("/error/500", true, false));
        }
        return modelAndView;
    }

    /*编辑权限*/
    @ResponseBody
    @PostMapping("/edit")
    public ResponseVo editMenu(@ModelAttribute("menu") Menu Menu) {
        boolean flag = menuService.updateById(Menu);
        if (flag) {
            shiroService.updatePermission();
            return ResultUtil.success("编辑权限成功");
        } else {
            return ResultUtil.error("编辑权限失败");
        }
    }

}
