package com.tarzan.cms.common.handle;

import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.modules.admin.service.common.CommonDataService;
import com.tarzan.cms.utils.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author tarzan liu
 */
@Service
@Component
@AllArgsConstructor
public class CommonDataHandler implements HandlerInterceptor {

    private final CommonDataService commonDataService;

    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler){
        if (handler instanceof HandlerMethod) {
            String uri = request.getRequestURI();
            if (!CoreConst.IS_REGISTERED.get() && !CoreConst.SYSTEM_REGISTER.equals(uri)) {
                if (uri.lastIndexOf(".") < 1) {
                    try {
                        response.sendRedirect(CoreConst.SYSTEM_REGISTER);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                } else {
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,HttpServletResponse response,Object handler,ModelAndView mv) throws Exception {
            if (mv != null) {
                if(!CoreConst.IS_REGISTERED.get()&&CoreConst.SYSTEM_REGISTER.equals(request.getServletPath())) {
                 mv.setViewName("admin/system/register");
               }
                String viewName= mv.getViewName();
                if(StringUtil.isNotBlank(viewName)){
                    if(viewName.contains(CoreConst.THEME_PREFIX)){
                        mv.addAllObjects(commonDataService.getAllCommonData());
                    }
                    if(viewName.contains(CoreConst.ERROR_PREFIX)){
                        mv.addAllObjects(commonDataService.getCommonData(CommonDataService.DataTypeEnum.WEB_THEME));
                        mv.addAllObjects(commonDataService.getCommonData(CommonDataService.DataTypeEnum.SITE_INFO));
                    }
                }
            }
    }
}