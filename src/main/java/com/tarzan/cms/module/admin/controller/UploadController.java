package com.tarzan.cms.module.admin.controller;

import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.utils.ResultUtil;
import com.tarzan.cms.module.admin.service.OssService;
import com.tarzan.cms.module.admin.vo.CloudStorageConfigVo;
import com.tarzan.cms.module.admin.vo.UploadResponse;
import com.tarzan.cms.module.admin.vo.base.ResponseVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 后台文件上传接口、云存储配置
 *
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Slf4j
@Controller
@RequestMapping("/attachment")
@AllArgsConstructor
public class UploadController {

    private final OssService ossService;


    @ResponseBody
    @PostMapping("/upload")
    public UploadResponse upload(@RequestParam(value = "file", required = false) MultipartFile file) {
        return ossService.upload(file);
    }

    @ResponseBody
    @PostMapping("/uploadForCkEditor")
    public Object upload2QiniuForMd(@RequestParam("upload") MultipartFile file) {
        Map<String, Object> resultMap = new HashMap<>(2);
        UploadResponse responseVo = upload(file);
        if (CoreConst.SUCCESS_CODE.equals(responseVo.getStatus())) {
            resultMap.put("uploaded", 1);
            resultMap.put("url", responseVo.getUrl());
            return resultMap;
        }
        return resultMap;
    }

    @GetMapping("/config")
    public String config(Model model) {
        model.addAttribute("cloudStorageConfig", ossService.getOssConfig());
        return CoreConst.ADMIN_PREFIX + "upload/config";
    }

    @ResponseBody
    @PostMapping("/saveConfig")
    public ResponseVo saveConfig(CloudStorageConfigVo cloudStorageConfig) {
        boolean success = ossService.updateOssConfig(cloudStorageConfig);
        if (success) {
            return ResultUtil.success("云存储配置保存成功！");
        } else {
            return ResultUtil.error("云存储配置保存失败！");
        }
    }

}
