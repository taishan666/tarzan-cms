package com.tarzan.cms.common.handle;

import com.tarzan.cms.module.admin.service.common.CommonDataService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tarzan liu
 */
@Component
@AllArgsConstructor
public class CommonDataHandler implements HandlerInterceptor {

    private final CommonDataService commonDataService;


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv) throws Exception {
        if (mv != null) {
            mv.addAllObjects(commonDataService.getAllCommonData());
        }
    }
}