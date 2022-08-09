package com.tarzan.cms.modules.admin.controller.common;


import com.tarzan.cms.common.constant.CoreConst;
import com.tarzan.cms.modules.admin.model.server.Server;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 后台服务器监控
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Controller
@RequestMapping("/server")
@Slf4j
@AllArgsConstructor
public class ServerController {
    /* 数据监控 */
    @GetMapping(value = "/monitoring")
    public String server(Model model) throws Exception {
        Server server = new Server();
        server.copyTo();
        model.addAttribute("server", server);
        return CoreConst.ADMIN_PREFIX + "/server";
    }
}
