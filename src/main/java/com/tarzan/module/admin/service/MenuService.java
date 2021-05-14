package com.tarzan.module.admin.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.common.util.CoreConst;
import com.tarzan.common.util.UUIDUtil;
import com.tarzan.module.admin.mapper.MenuMapper;
import com.tarzan.module.admin.model.Menu;
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
public class MenuService extends ServiceImpl<MenuMapper, Menu> {

    private static final Pattern SLASH_PATTERN = Pattern.compile("/");

    private static List<Menu> buildPermissionTree(List<Menu> permissionList) {
        Map<Integer, List<Menu>> parentIdToPermissionListMap = permissionList.stream().peek(p -> {
            if (StringUtils.startsWith(p.getUrl(), "/")) {
                p.setUrl(SLASH_PATTERN.matcher(p.getUrl()).replaceFirst("#"));
            }
        }).collect(Collectors.groupingBy(Menu::getParentId));
        List<Menu> rootLevelPermissionList = parentIdToPermissionListMap.getOrDefault(CoreConst.TOP_MENU_ID, Collections.emptyList());
        fetchChildren(rootLevelPermissionList, parentIdToPermissionListMap);
        return rootLevelPermissionList;
    }

    private static void fetchChildren(List<Menu> permissionList, Map<Integer, List<Menu>> parentIdToPermissionListMap) {
        if (CollectionUtils.isEmpty(permissionList)) {
            return;
        }
        for (Menu Menu : permissionList) {
            List<Menu> childrenList = parentIdToPermissionListMap.get(Menu.getId());
            fetchChildren(childrenList, parentIdToPermissionListMap);
            Menu.setChildren(childrenList);
        }
    }

    public Set<String> findPermsByUserId(String userId) {
        return baseMapper.findPermsByUserId(userId);
    }

    public List<Menu> selectAll(Integer status) {
        return  baseMapper.selectList(Wrappers.<Menu>lambdaQuery().eq(Menu::getStatus,status).orderByAsc(Menu::getOrderNum));
    }

    public List<Menu> selectAllMenuName(Integer status) {
        return  baseMapper.selectList(Wrappers.<Menu>lambdaQuery().eq(Menu::getStatus,status).orderByAsc(Menu::getOrderNum));
    }

    public List<Menu> selectMenuByUserId(String userId) {
        return baseMapper.selectMenuByUserId(userId);
    }

    public List<Menu> selectMenuTreeByUserId(String userId) {
        return buildPermissionTree(baseMapper.selectMenuByUserId(userId));
    }

    public int insert(Menu Menu) {
        Date date = new Date();
        Menu.setPermissionId(UUIDUtil.getUniqueIdByUUId());
        Menu.setStatus(CoreConst.STATUS_VALID);
        Menu.setCreateTime(date);
        Menu.setUpdateTime(date);
        return baseMapper.insert(Menu);
    }

    public int updateStatus(Integer id, Integer status) {
        return baseMapper.updateById(new Menu().setId(id).setStatus(status));
    }

    public int selectSubPermsByPermissionId(Integer id) {
        return baseMapper.selectCount(Wrappers.lambdaQuery(new Menu().setParentId(id).setStatus(1)));
    }
}
