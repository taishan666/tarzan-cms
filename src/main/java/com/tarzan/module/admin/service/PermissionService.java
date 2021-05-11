package com.tarzan.module.admin.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.common.util.CoreConst;
import com.tarzan.common.util.UUIDUtil;
import com.tarzan.module.admin.mapper.PermissionMapper;
import com.tarzan.module.admin.model.Permission;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class PermissionService extends ServiceImpl<PermissionMapper, Permission> {

    private static final Pattern SLASH_PATTERN = Pattern.compile("/");

    private static List<Permission> buildPermissionTree(List<Permission> permissionList) {
        Map<Integer, List<Permission>> parentIdToPermissionListMap = permissionList.stream().peek(p -> {
            if (StringUtils.startsWith(p.getUrl(), "/")) {
                p.setUrl(SLASH_PATTERN.matcher(p.getUrl()).replaceFirst("#"));
            }
        }).collect(Collectors.groupingBy(Permission::getParentId));
        List<Permission> rootLevelPermissionList = parentIdToPermissionListMap.getOrDefault(CoreConst.TOP_MENU_ID, Collections.emptyList());
        fetchChildren(rootLevelPermissionList, parentIdToPermissionListMap);
        return rootLevelPermissionList;
    }

    private static void fetchChildren(List<Permission> permissionList, Map<Integer, List<Permission>> parentIdToPermissionListMap) {
        if (CollectionUtils.isEmpty(permissionList)) {
            return;
        }
        for (Permission permission : permissionList) {
            List<Permission> childrenList = parentIdToPermissionListMap.get(permission.getId());
            fetchChildren(childrenList, parentIdToPermissionListMap);
            permission.setChildren(childrenList);
        }
    }

    public Set<String> findPermsByUserId(String userId) {
        return baseMapper.findPermsByUserId(userId);
    }

    public List<Permission> selectAll(Integer status) {
        return baseMapper.selectAllPerms(status);
    }

    public List<Permission> selectAllMenuName(Integer status) {
        return baseMapper.selectAllMenuName(status);
    }

    public List<Permission> selectMenuByUserId(String userId) {
        return baseMapper.selectMenuByUserId(userId);
    }

    public List<Permission> selectMenuTreeByUserId(String userId) {
        return buildPermissionTree(baseMapper.selectMenuByUserId(userId));
    }

    public int insert(Permission permission) {
        Date date = new Date();
        permission.setPermissionId(UUIDUtil.getUniqueIdByUUId());
        permission.setStatus(CoreConst.STATUS_VALID);
        permission.setCreateTime(date);
        permission.setUpdateTime(date);
        return baseMapper.insert(permission);
    }

    public int updateStatus(String permissionId, Integer status) {
        return baseMapper.updateStatusByPermissionId(permissionId, status);
    }

    public Permission findByPermissionId(String permissionId) {
        return baseMapper.selectByPermissionId(permissionId);
    }

    public Permission findById(Integer id) {
        return baseMapper.selectById(id);
    }

    public int updateByPermissionId(Permission permission) {
        return baseMapper.updateByPermissionId(permission);
    }

    public int selectSubPermsByPermissionId(String permissionId) {
        return baseMapper.selectSubPermsByPermissionId(permissionId);
    }
}
