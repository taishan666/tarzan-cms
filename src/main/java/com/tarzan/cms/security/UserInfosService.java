package com.tarzan.cms.security;


import com.tarzan.cms.module.admin.model.sys.Role;
import com.tarzan.cms.module.admin.model.sys.User;
import com.tarzan.cms.module.admin.service.sys.MenuService;
import com.tarzan.cms.module.admin.service.sys.RoleService;
import com.tarzan.cms.module.admin.service.sys.UserService;
import com.tarzan.cms.security.dto.JwtUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author codermy
 * @createTime 2020/7/16
 */
@Service
@Slf4j
public class UserInfosService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Override
    public JwtUserDto loadUserByUsername(String userName){
        // 根据用户名获取用户
        User user = userService.selectByUsername(userName);
        if (user == null ){
            throw new BadCredentialsException("用户名或密码错误");
        }else if (user.getStatus().equals(1)) {
            throw new LockedException("用户被锁定,请联系管理员解锁");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Set<String> collect = menuService.findPermsByUserId(user.getId());
        for (String authority : collect){
            if (!("").equals(authority) & authority !=null){
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority);
                grantedAuthorities.add(grantedAuthority);
            }
        }
        //将用户所拥有的权限加入GrantedAuthority集合中
        JwtUserDto loginUser =new JwtUserDto(user,grantedAuthorities);
        loginUser.setRoleList(getRoleInfo(user));
        return loginUser;
    }


    public List<Role> getRoleInfo(User user) {
        Set<String> roleIds = roleService.findRoleByUserId(user.getId());
        return roleService.listByIds(roleIds);
    }

}