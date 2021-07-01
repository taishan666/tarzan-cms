package com.tarzan.cms.module.admin.service;

import com.alibaba.fastjson.JSON;
import com.tarzan.cms.common.config.properties.FileUploadProperties;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.common.util.*;
import com.tarzan.cms.enums.SysConfigKey;
import com.tarzan.cms.exception.UploadFileNotFoundException;
import com.tarzan.cms.module.admin.vo.CloudStorageConfigVo;
import com.tarzan.cms.module.admin.vo.UploadResponse;
import com.tarzan.cms.module.admin.vo.base.ResponseVo;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tarzan liu
 * @date 2020/3/31 2:41 下午
 */
@Slf4j
@Service
@AllArgsConstructor
public class OssService {

    private final SysConfigService sysConfigService;
    private final FileUploadProperties fileUploadProperties;

    @SneakyThrows
    public UploadResponse upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new UploadFileNotFoundException(UploadResponse.ErrorEnum.FILE_NOT_FOUND.msg);
        }
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
        String value = sysConfigService.selectAll().get(SysConfigKey.CLOUD_STORAGE_CONFIG.getValue());
        CloudStorageConfigVo cloudStorageConfig = JSON.parseObject(value, CloudStorageConfigVo.class);

        String md5 = MD5Util.getMessageDigest(file.getBytes());
        String dir;
        String filePath;
        String domain;
        String url = null;
        ResponseVo<?> responseVo;
        switch (cloudStorageConfig.getType()) {
            case CoreConst.UPLOAD_TYPE_QINIUYUN:
                domain = cloudStorageConfig.getQiniuDomain();
                dir = cloudStorageConfig.getQiniuPrefix();
                filePath = String.format("%1$s/%2$s%3$s", dir, md5, suffix);
                responseVo = QiNiuYunUtil.uploadFile(cloudStorageConfig, filePath, file.getBytes());
                url = String.format("%1$s/%2$s", domain, filePath);
                break;
            case CoreConst.UPLOAD_TYPE_ALIYUN:
                domain = cloudStorageConfig.getAliyunDomain();
                dir = cloudStorageConfig.getAliyunPrefix();
                filePath = String.format("%1$s/%2$s%3$s", dir, md5, suffix);
                responseVo = AliYunUtil.uploadFile(cloudStorageConfig, filePath, file.getBytes());
                url = String.format("%1$s/%2$s", domain, filePath);
                break;
            case CoreConst.UPLOAD_TYPE_LOCAL:
                String relativePath = FileUploadUtil.uploadLocal(file, fileUploadProperties.getUploadFolder());
                String accessPrefixUrl = fileUploadProperties.getAccessPrefixUrl();
                if (!StringUtils.endsWith(accessPrefixUrl, "/")) {
                    accessPrefixUrl += '/';
                }
                url = accessPrefixUrl + relativePath;
                responseVo = ResultUtil.success();
                break;
            default:
                responseVo = ResultUtil.error("未配置云存储类型");
        }

        if (responseVo.getStatus().equals(CoreConst.SUCCESS_CODE)) {
            return UploadResponse.success(url, originalFilename, suffix, url, CoreConst.SUCCESS_CODE);
        } else {
            return UploadResponse.failed(originalFilename, CoreConst.FAIL_CODE, responseVo.getMsg());
        }
    }

    @Cacheable(value = "oss", key = "'config'")
    public CloudStorageConfigVo getOssConfig() {
        String value = sysConfigService.selectAll().get(SysConfigKey.CLOUD_STORAGE_CONFIG.getValue());
        return JSON.parseObject(value, CloudStorageConfigVo.class);
    }

    @CacheEvict(value = "oss", key = "'config'")
    public boolean updateOssConfig(CloudStorageConfigVo cloudStorageConfig) {
        String value = JSON.toJSONString(cloudStorageConfig);
        return sysConfigService.updateByKey(SysConfigKey.CLOUD_STORAGE_CONFIG.getValue(), value);
    }

    public int getOssConfigType() {
        return getOssConfig().getType();
    }


}
