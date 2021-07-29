package com.tarzan.cms.module.admin.service.log;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.cms.module.admin.mapper.log.LogErrorMapper;
import com.tarzan.cms.module.admin.model.log.LogError;
import org.springframework.stereotype.Service;

@Service
public class LogErrorService extends ServiceImpl<LogErrorMapper, LogError> {
}
