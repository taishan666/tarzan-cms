package com.tarzan.cms.module.admin.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.module.admin.mapper.sys.*;
import com.tarzan.cms.module.admin.model.sys.*;
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
        List<UserRole> list=userRoleMapper.selectList(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, userId));
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
        Set<Integer> roleIds= list.stream().map(e->e.getRoleId()).collect(Collectors.toSet());
        Map<Integer,List<UserRole>> map= list.stream().collect(Collectors.groupingBy(UserRole::getUserId));
        if(CollectionUtils.isEmpty(roleIds)){
            return null;
        }
        List<Role> roles=list(Wrappers.<Role>lambdaQuery().in(Role::getId,roleIds));
        Map<Integer,String> roleMap=roles.stream().collect(Collectors.toMap(Role::getId,Role::getName));
        Map<Integer,String> result=new HashMap<>();
        map.forEach((k,v)->{
             Set<String> roleNames=new HashSet<>();
             v.forEach(e->{
                 if (roleMap.get(e.getRoleId())!=null){
                     roleNames.add(roleMap.get(e.getRoleId()));
                 }
             });
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
        role.setStatus(1);
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
            RoleMenu RoleMenu = new RoleMenu();
            RoleMenu.setRoleId(roleId);
            RoleMenu.setMenuId(Integer.valueOf(menuId));
            roleMenuMapper.insert(RoleMenu);
        }
    }

    public List<User> findByRoleId(Integer roleId) {
        return findByRoleIds(Arrays.asList(roleId));
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
