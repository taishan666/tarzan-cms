package com.tarzan.cms.common.handle;

import com.tarzan.cms.common.constant.CoreConst;
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
            if(mv.getViewName().contains(CoreConst.THEME_PREFIX)){
                mv.addAllObjects(commonDataService.getAllCommonData());
            }
            if(mv.getViewName().contains(CoreConst.ERROR_PREFIX)){
                mv.addAllObjects(commonDataService.getCommonData(CommonDataService.DataTypeEnum.WEB_THEME));
            }
            if(mv.getViewName().contains(CoreConst.SYSTEM_PREFIX)){
                mv.addAllObjects(commonDataService.getCommonData(CommonDataService.DataTypeEnum.WEB_THEME));
            }
            if(mv.getViewName().contains(CoreConst.SYSTEM_PREFIX)||mv.getViewName().contains(CoreConst.ERROR_PREFIX)){
                mv.addAllObjects(commonDataService.getCommonData(CommonDataService.DataTypeEnum.CATEGORY_LIST));
                mv.addAllObjects(commonDataService.getCommonData(CommonDataService.DataTypeEnum.SITE_CONFIG));
            }
        }

    }
}