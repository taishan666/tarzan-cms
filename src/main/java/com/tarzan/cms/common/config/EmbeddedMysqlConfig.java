package com.tarzan.cms.common.config;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.ScriptResolver;
import com.wix.mysql.config.MysqldConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.config.Charset.UTF8;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v5_6_23;

@Configuration
@ConditionalOnProperty(prefix = "cms",name = "embedded.mysql.enabled",havingValue = "true")
public class EmbeddedMysqlConfig {

    private EmbeddedMysql mysql;

    @PostConstruct
    public void launchDb(){
        //mysql版本
        MysqldConfig config = aMysqldConfig(v5_6_23)
                .withCharset(UTF8)
                //端口号
                .withPort(3306)
                //用户名密码
                .withUser("tarzan", "123456")
                //时区
                .withTimeZone("Asia/Shanghai")
                //超时
                .withTimeout(1, TimeUnit.MINUTES)
                .withServerVariable("max_connect_errors", 666)
                .build();
        mysql = anEmbeddedMysql(config)
                //初始化数据表结构
                .addSchema("tarzan_cms", ScriptResolver.classPathScript("db/schema.sql"))
                .start();
    }

    @PreDestroy
    public void stopDb(){
        mysql.stop();
    }
}

