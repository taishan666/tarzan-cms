package com.tarzan.cms.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tarzan.cms.module.admin.model.sys.Role;
import com.tarzan.cms.module.admin.model.sys.User;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author codermy
 * @createTime 2020/7/16
 */
@Data
@ToString
public class JwtUserDto implements UserDetails {

    /**
     * 用户数据
     */
    private User user;

    private List<Role> roleList;
    /**
     * 用户权限的集合
     */
    @JsonIgnore
    private List<GrantedAuthority> authorities;

    public List<String> getRoles() {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }


    /**
     * 加密后的密码
     * @return
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }


    /**
     * 用户名
     * @return
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }


    /**
     * 是否过期
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    /**
     * 是否锁定
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    /**
     * 凭证是否过期
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    /**
     * 是否可用
     * @return
     */
    @Override
    public boolean isEnabled() {
        return user.getStatus() == 1 ? true : false;
    }


    public JwtUserDto(User user, List<GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }
}