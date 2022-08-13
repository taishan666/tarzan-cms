package com.tarzan.cms.cache;

import com.tarzan.cms.modules.admin.model.sys.Role;
import com.tarzan.cms.modules.admin.service.sys.RoleService;
import com.tarzan.cms.utils.SpringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tarzan
 */
public class RoleCache {

    private static final RoleService  ROLE_SERVICE;
    private static Map<Integer, String> roleMap = new HashMap<>();

    static {
        ROLE_SERVICE = SpringUtil.getBean(RoleService.class);
    }

    public static void initRole() {
        List<Role> list = ROLE_SERVICE.lambdaQuery().select(Role::getId, Role::getName).list();
        roleMap = list.stream().collect(Collectors.toMap(Role::getId, Role::getName));
    }

    public static String getName(Integer id) {
        String name = roleMap.get(id);
        if (name == null) {
            name = ROLE_SERVICE.getById(id).getName();
            roleMap.put(id, name);
        }
        return name;
    }

    public static void save(Role role) {
        roleMap.put(role.getId(), role.getName());
    }

    public static void delete(List<Integer> ids) {
        ids.forEach(e->roleMap.remove(e));
    }
}
