package com.tarzan.cms.modules.admin.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.tarzan.cms.cache.RoleCache;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.modules.admin.mapper.sys.*;
import com.tarzan.cms.modules.admin.model.sys.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class RoleService extends ServiceImpl<RoleMapper, Role> {

    private final MenuMapper menuMapper;
    private final RoleMenuMapper roleMenuMapper;
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;

    public Set<String> findRoleByUserId(Integer userId) {
        List<UserRole> list=userRoleMapper.selectList(Wrappers.<UserRole>lambdaQuery().select(UserRole::getRoleId).eq(UserRole::getUserId, userId));
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return  list.stream().map(e->String.valueOf(e.getRoleId())).collect(Collectors.toSet());
    }

    public  Map<Integer,String> findRoleNameByUserIds(List<Integer> userIds) {
        List<UserRole> list=userRoleMapper.selectList(Wrappers.<UserRole>lambdaQuery().in(UserRole::getUserId, userIds));
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        Map<Integer,List<UserRole>> map= list.stream().collect(Collectors.groupingBy(UserRole::getUserId));
        Map<Integer,String> result=new HashMap<>(3);
        map.forEach((k,v)->{
             Set<String> roleNames=new HashSet<>();
             v.forEach(e->roleNames.add(RoleCache.getName(e.getRoleId())));
            result.put(k, StringUtils.join(roleNames,","));
        });
        return  result;
    }





    public IPage<Role> selectRoles(Role role, Integer pageNumber, Integer pageSize) {
        IPage<Role> page = new Page<>(pageNumber, pageSize);
        return page(page, Wrappers.<Role>lambdaQuery().eq(Role::getStatus, 1).like(StringUtils.isNotBlank(role.getName()),Role::getName,role.getName()));
    }

    public boolean insert(Role role) {
        Date date=new Date();
        role.setStatus(CoreConst.STATUS_VALID);
        role.setCreateTime(date);
        role.setUpdateTime(date);
        return save(role);
    }

    public boolean updateStatusBatch(List<Integer> roleIds, Integer status) {
        return update(new Role().setStatus(status),Wrappers.<Role>lambdaUpdate().in(Role::getId, roleIds));
    }

    public List<Menu> findPermissionsByRoleId(Integer roleId) {
        List<RoleMenu> roleMenus= roleMenuMapper.selectList(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId, roleId));
        List<Integer> menuIds=roleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
        List<Menu> menus= Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(menuIds)) {
             menus = menuMapper.selectList(Wrappers.<Menu>lambdaQuery()
                    .in(Menu::getId, menuIds)
                    .eq(Menu::getStatus, CoreConst.STATUS_VALID)
                    .select(Menu::getId, Menu::getName, Menu::getParentId));
        }
        return menus;
    }

    public void addAssignPermission(Integer roleId, List<String> menuIds) {
        roleMenuMapper.delete(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId, roleId));
        for (String menuId : menuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(Integer.valueOf(menuId));
            roleMenuMapper.insert(roleMenu);
        }
    }

    public List<User> findByRoleId(Integer roleId) {
        List<UserRole> userRoles= userRoleMapper.selectList(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getRoleId,roleId));
        if(CollectionUtils.isNotEmpty(userRoles)){
            List<Integer> userIds=userRoles.stream().map(UserRole::getUserId).collect(Collectors.toList());
            return userMapper.selectBatchIds(userIds);
        }
        return Collections.emptyList();
    }

    public List<User> findByRoleIds(List<Integer> roleIds) {
        List<UserRole> userRoles= userRoleMapper.selectList(Wrappers.<UserRole>lambdaQuery().in(UserRole::getRoleId,roleIds));
        List<User> users=Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(userRoles)){
            List<Integer> userIds=userRoles.stream().map(UserRole::getUserId).collect(Collectors.toList());
            users=userMapper.selectBatchIds(userIds);
        }
      return users;
    }

}
