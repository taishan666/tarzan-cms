package com.tarzan.cms.module.admin.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tarzan.cms.module.admin.model.sys.User;
import org.apache.ibatis.annotations.Param;


/**
 * @author tarzan liu
 * @since JDK1.8
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


}
