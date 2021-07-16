package com.tarzan.cms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author tarzan liu
 * @version V1.0
 * @date 2021年5月11日
 */
@Slf4j
@SpringBootApplication
public class CmsApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext context= SpringApplication.run(CmsApplication.class, args);
        Environment env = context.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (contextPath == null) {
            contextPath = "";
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Application is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + contextPath + "\n\t" +
                "External: \thttp://" + ip + ':' + port + contextPath + '\n' +
                "----------------------------------------------------------");
    }

/*    @Bean
    public ServletRegistrationBean<StatViewServlet> druidStatViewServlet() {
        ServletRegistrationBean<StatViewServlet> registrationBean = new ServletRegistrationBean<>(new StatViewServlet(),  "/druid/*");
        registrationBean.addInitParameter("allow", "");// IP白名单 (没有配置或者为空，则允许所有访问)
        //registrationBean.addInitParameter("loginUsername", "root");
       // registrationBean.addInitParameter("loginPassword", "123456");
        registrationBean.addInitParameter("enable", "true");
        registrationBean.addInitParameter("resetEnable", "false");
        return registrationBean;
    }*/

}
