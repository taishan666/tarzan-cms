package com.tarzan.cms.common.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class EmbeddedH2Config {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    private final static String h2Driver="org.h2.Driver";

    @PostConstruct
    private void  init(){
        if (h2Driver.equals(driverClassName)) {
            installSQL();
        }
    }

    private void  installSQL(){
        if(tableNames().size()==0){
            String classPath = this.getClass().getResource("/").getPath();
            try {
                FileInputStream out = new FileInputStream(classPath + "db/schema-h2.sql");
                InputStreamReader reader = new InputStreamReader(out, StandardCharsets.UTF_8);
                BufferedReader in = new BufferedReader(reader);
                String txt = FileCopyUtils.copyToString(in);
                jdbcTemplate.batchUpdate(txt);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //获取所有表名称
    private List<String> tableNames() {
        List<String> tableNames= new ArrayList<>();
        try {
            Connection getConnection=jdbcTemplate.getDataSource().getConnection();
            DatabaseMetaData metaData = getConnection.getMetaData();
            ResultSet rs = metaData.getTables(getConnection.getCatalog(), null, null, new String[] { "TABLE" });
            while (rs.next()) {
                String tableName=rs.getString("TABLE_NAME");
                tableNames.add(tableName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableNames;
    }


}
