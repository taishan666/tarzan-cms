package com.tarzan.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.common.util.Pagination;
import com.tarzan.common.util.UUIDUtil;
import com.tarzan.module.admin.mapper.PermissionMapper;
import com.tarzan.module.admin.mapper.RoleMapper;
import com.tarzan.module.admin.mapper.RolePermissionMapper;
import com.tarzan.module.admin.mapper.UserMapper;
import com.tarzan.module.admin.model.Permission;
import com.tarzan.module.admin.model.Role;
import com.tarzan.module.admin.model.RolePermission;
import com.tarzan.module.admin.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class RoleService extends ServiceImpl<RoleMapper, Role> {

    private final PermissionMapper permissionMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final UserMapper userMapper;

    public Set<String> findRoleByUserId(String userId) {
        return baseMapper.findRoleByUserId(userId);
    }

    public IPage<Role> selectRoles(Role role, Integer pageNumber, Integer pageSize) {
        IPage<Role> page = new Pagination<>(pageNumber, pageSize);
        return baseMapper.selectRoles(page, role);
    }

    public int insert(Role role) {
        role.setRoleId(UUIDUtil.getUniqueIdByUUId());
        role.setStatus(1);
        role.setCreateTime(new Date());
        return baseMapper.insert(role);
    }

    public int updateStatusBatch(List<String> roleIds, Integer status) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("roleIds", roleIds);
        params.put("status", status);
        return baseMapper.updateStatusBatch(params);
    }

    public Role findById(Integer roleId) {
        return baseMapper.selectOne(Wrappers.<Role>lambdaQuery().eq(Role::getRoleId, roleId));
    }

    public int updateByRoleId(Role role) {
        Map<String, Object> params = new HashMap<>(3);
        params.put("name", role.getName());
        params.put("description", role.getDescription());
        params.put("role_id", role.getRoleId());
        return baseMapper.updateByRoleId(params);
    }

    public List<Permission> findPermissionsByRoleId(String roleId) {
        return permissionMapper.findByRoleId(roleId);
    }

    public void addAssignPermission(String roleId, List<String> permissionIds) {
        rolePermissionMapper.delete(Wrappers.<RolePermission>lambdaQuery().eq(RolePermission::getRoleId, roleId));
        for (String permissionId : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionMapper.insert(rolePermission);
        }
    }

    public List<User> findByRoleId(String roleId) {
        return userMapper.findByRoleId(roleId);
    }

    public List<User> findByRoleIds(List<String> roleIds) {
        return userMapper.findByRoleIds(roleIds);
    }

}
