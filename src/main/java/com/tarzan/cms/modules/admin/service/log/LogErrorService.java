package com.tarzan.cms.modules.admin.service.log;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.cms.modules.admin.mapper.log.LogErrorMapper;
import com.tarzan.cms.modules.admin.model.log.LogError;
import org.springframework.stereotype.Service;

@Service
public class LogErrorService extends ServiceImpl<LogErrorMapper, LogError> {
}
