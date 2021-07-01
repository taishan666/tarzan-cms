package com.tarzan.cms.module.admin.controller;

import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.base.Captcha;
import com.wf.captcha.utils.CaptchaUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 获取验证码图片
 *
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Slf4j
@Controller
@AllArgsConstructor
public class CaptchaController {


    /**
     * 获取验证码图片
     * Gets captcha code.
     *
     * @param request  the request
     * @param response the response
     * @throws IOException the io exception
     */
    @RequestMapping("/verificationCode")
    public void getCaptchaCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        GifCaptcha captcha = new GifCaptcha(130, 48);
        captcha.setLen(4);
        CaptchaUtil.out(captcha, request, response);
    }
}
