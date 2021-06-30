package com.tarzan.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tarzan.common.util.UUIDUtil;
import com.tarzan.module.admin.mapper.*;
import com.tarzan.module.admin.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class RoleService extends ServiceImpl<RoleMapper, Role> {

    private final MenuMapper MenuMapper;
    private final RoleMenuMapper RoleMenuMapper;
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;

    public Set<String> findRoleByUserId(Integer userId) {
        List<UserRole> list=userRoleMapper.selectList(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, userId));
        if(list==null){
            return null;
        }
        return  list.stream().map(e->String.valueOf(e.getRoleId())).collect(Collectors.toSet());
    }

    public IPage<Role> selectRoles(Role role, Integer pageNumber, Integer pageSize) {
        IPage<Role> page = new Page<>(pageNumber, pageSize);
        return baseMapper.selectPage(page, Wrappers.<Role>lambdaQuery().eq(Role::getStatus, 1).like(StringUtils.isNotBlank(role.getName()),Role::getName,role.getName()));
    }

    public int insert(Role role) {
        role.setStatus(1);
        role.setCreateTime(new Date());
        return baseMapper.insert(role);
    }

    public int updateStatusBatch(List<Integer> roleIds, Integer status) {
        return baseMapper.update(new Role().setStatus(status),Wrappers.<Role>lambdaUpdate().in(Role::getId, roleIds));
    }

    public List<Menu> findPermissionsByRoleId(Integer roleId) {
        return MenuMapper.findByRoleId(roleId);
    }

    public void addAssignPermission(Integer roleId, List<String> menuIds) {
        RoleMenuMapper.delete(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId, roleId));
        for (String menuId : menuIds) {
            RoleMenu RoleMenu = new RoleMenu();
            RoleMenu.setRoleId(roleId);
            RoleMenu.setMenuId(Integer.valueOf(menuId));
            RoleMenuMapper.insert(RoleMenu);
        }
    }

    public List<User> findByRoleId(Integer roleId) {
        return userMapper.findByRoleId(roleId);
    }

    public List<User> findByRoleIds(List<Integer> roleIds) {
        return userMapper.findByRoleIds(roleIds);
    }

}
