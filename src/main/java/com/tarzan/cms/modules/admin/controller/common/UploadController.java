package com.tarzan.cms.modules.admin.controller.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.utils.ResultUtil;
import com.tarzan.cms.modules.admin.service.common.OssService;
import com.tarzan.cms.modules.admin.vo.CloudStorageConfigVo;
import com.tarzan.cms.modules.admin.vo.UploadResponse;
import com.tarzan.cms.modules.admin.vo.base.ResponseVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

/**
 * 后台文件上传接口、云存储配置
 *
 * @author tarzan liu
 * @since JDK1.8
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
    @PostMapping("/editor/upload")
    public JSONArray editorUpload(@RequestParam(value = "file[]", required = false) MultipartFile[] files) {
        JSONArray  array=new JSONArray();
        Arrays.asList(files).forEach(file->{
            UploadResponse responseVo = upload(file);
            JSONObject object=new JSONObject();
            if (CoreConst.SUCCESS_CODE.equals(responseVo.getStatus())) {
                object.put("error", 0);
                object.put("url", responseVo.getUrl());
                object.put("name",responseVo.getFileName());
            }else{
                object.put("error", "上传失败！");
                object.put("url", responseVo.getUrl());
                object.put("name",responseVo.getFileName());
            }
            array.add(object);
        });
        return array;
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
