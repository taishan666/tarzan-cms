package com.tarzan.cms.module.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tarzan.cms.module.admin.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据user参数查询用户列表
     *
     * @param page
     * @param user
     * @return list
     */
    IPage<User> selectUsers(@Param("page") IPage<User> page, @Param("user") User user);

    /**
     * 根据角色id查询用户list
     *
     * @param roleId
     * @return list
     */
    List<User> findByRoleId(Integer roleId);

    /**
     * 根据角色id查询用户list
     *
     * @param roleIds
     * @return list
     */
    List<User> findByRoleIds(List<Integer> roleIds);
}
