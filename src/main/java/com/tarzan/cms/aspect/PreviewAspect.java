package com.tarzan.cms.aspect;

import com.tarzan.cms.common.props.CmsProperties;
import com.tarzan.cms.utils.ResultUtil;
import com.tarzan.cms.utils.WebUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 演示环境拦截器
 * @author pangu
 */
@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class PreviewAspect {

    private  final CmsProperties cmsProperties;

    @Around("(@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController))")
    public Object aroundApi(ProceedingJoinPoint point) throws Throwable {
        //　获取request
        HttpServletRequest request = WebUtil.getRequest();
        String path=request.getServletPath();
        if (StringUtils.equalsIgnoreCase(request.getMethod(), HttpMethod.POST.name())&&(path.contains("/delete")|| path.contains("/edit"))&& cmsProperties.getPreviewEnabled()) {
            return ResultUtil.error("演示环境不能操作！");
        }
        return point.proceed();
    }
}

