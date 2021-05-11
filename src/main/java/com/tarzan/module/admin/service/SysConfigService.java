package com.tarzan.module.admin.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.common.util.CoreConst;
import com.tarzan.module.admin.mapper.SysConfigMapper;
import com.tarzan.module.admin.model.SysConfig;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tarzan.common.util.CoreConst.SITE_STATIC_KEY;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class SysConfigService extends ServiceImpl<SysConfigMapper, SysConfig> {


    @PostConstruct
    public void init() {
        CoreConst.SITE_STATIC.set("on".equalsIgnoreCase(selectAll().getOrDefault(SITE_STATIC_KEY, "false")));
    }

    @Cacheable(value = "site", key = "'config'")
    public Map<String, String> selectAll() {
        List<SysConfig> sysConfigs = baseMapper.selectList(Wrappers.emptyWrapper());
        Map<String, String> map = new HashMap<>(sysConfigs.size());
        for (SysConfig config : sysConfigs) {
            map.put(config.getSysKey(), config.getSysValue());
        }
        return map;
    }

    @CacheEvict(value = "site", key = "'config'", allEntries = true)
    public boolean updateByKey(String key, String value) {
        if (getOne(Wrappers.<SysConfig>lambdaQuery().eq(SysConfig::getSysKey, key)) != null) {
            return update(Wrappers.<SysConfig>lambdaUpdate().eq(SysConfig::getSysKey, key).set(SysConfig::getSysValue, value));
        } else {
            return save(new SysConfig().setSysKey(key).setSysValue(value));
        }
    }

    @CacheEvict(value = "site", key = "'config'", allEntries = true)
    public void updateAll(Map<String, String> map, HttpServletRequest request, HttpServletResponse response) {
        map.forEach(this::updateByKey);
    }
}