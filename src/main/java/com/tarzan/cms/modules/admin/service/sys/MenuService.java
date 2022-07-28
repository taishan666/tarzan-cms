package com.tarzan.cms.modules.admin.service.sys;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.modules.admin.mapper.sys.MenuMapper;
import com.tarzan.cms.modules.admin.model.sys.Menu;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class MenuService extends ServiceImpl<MenuMapper, Menu> {

    private static final Pattern SLASH_PATTERN = Pattern.compile("/");

    private  List<Menu> buildPermissionTree(List<Menu> permissionList) {
        Map<Integer, List<Menu>> parentIdToPermissionListMap = permissionList.stream().peek(p -> {
            if (StringUtils.startsWith(p.getUrl(), "/")) {
                p.setUrl(SLASH_PATTERN.matcher(p.getUrl()).replaceFirst("#"));
            }
        }).collect(Collectors.groupingBy(Menu::getParentId));
        List<Menu> rootLevelPermissionList = parentIdToPermissionListMap.getOrDefault(CoreConst.TOP_MENU_ID, Collections.emptyList());
        fetchChildren(rootLevelPermissionList, parentIdToPermissionListMap);
        return rootLevelPermissionList;
    }

    private  void fetchChildren(List<Menu> menuListList, Map<Integer, List<Menu>> parentIdToPermissionListMap) {
        if (CollectionUtils.isEmpty(menuListList)) {
            return;
        }
        for (Menu menu : menuListList) {
            List<Menu> childrenList = parentIdToPermissionListMap.get(menu.getId());
            fetchChildren(childrenList, parentIdToPermissionListMap);
            menu.setChildren(childrenList);
        }
    }

    public Set<String> findPermsByUserId(Integer userId) {
        return baseMapper.findPermsByUserId(userId);
    }

    public List<Menu> selectAll(Integer status) {
        return  list(Wrappers.<Menu>lambdaQuery().eq(Menu::getStatus,status).orderByAsc(Menu::getOrderNum));
    }

    public List<Menu> selectAllMenuName(Integer status) {
        return  list(Wrappers.<Menu>lambdaQuery().eq(Menu::getStatus,status).orderByAsc(Menu::getOrderNum));
    }

    public List<Menu> selectMenuByUserId(Integer userId) {
        return baseMapper.selectMenuByUserId(userId);
    }

    public List<Menu> selectMenuTreeByUserId(Integer userId) {
        return buildPermissionTree(baseMapper.selectMenuByUserId(userId));
    }

    public boolean insert(Menu Menu) {
        Date date = new Date();
        Menu.setStatus(CoreConst.STATUS_VALID);
        Menu.setCreateTime(date);
        Menu.setUpdateTime(date);
        return save(Menu);
    }

    public int updateStatus(Integer id, Integer status) {
        return baseMapper.updateById(new Menu().setId(id).setStatus(status));
    }

    public long selectSubPermsByPermissionId(Integer id) {
        return baseMapper.selectCount(Wrappers.lambdaQuery(new Menu().setParentId(id).setStatus(1)));
    }
}
