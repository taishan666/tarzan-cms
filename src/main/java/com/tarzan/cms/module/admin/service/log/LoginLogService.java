package com.tarzan.cms.module.admin.service.log;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.cms.module.admin.mapper.log.LoginLogMapper;
import com.tarzan.cms.module.admin.model.log.LoginLog;
import org.springframework.stereotype.Service;

@Service
public class LoginLogService extends ServiceImpl<LoginLogMapper, LoginLog> {
}
