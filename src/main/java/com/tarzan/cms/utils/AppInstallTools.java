package com.tarzan.cms.utils;

import com.tarzan.cms.common.constant.CoreConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AppInstallTools {


    @Resource
    private JdbcTemplate jdbcTemplate;

    @Value("${spring.datasource.url}")
    private String url;

    private final static String h2Driver="jdbc:h2:";

    private final static String mysqlDriver="jdbc:mysql:";


    public void  install(){
        if (url.contains(h2Driver)) {
            installSQL("schema-h2.sql");
        }
        if (url.contains(mysqlDriver)) {
            installSQL("schema-mysql.sql");
        }
    }

    private  void  installSQL(String fileName){
        if(tableNames().size()==0){
            InputStream dbIos = this.getClass().getResourceAsStream("/db/"+ fileName);
            try {
                InputStreamReader reader = new InputStreamReader(dbIos, StandardCharsets.UTF_8);
                BufferedReader in = new BufferedReader(reader);
                String txt = FileCopyUtils.copyToString(in);
                jdbcTemplate.batchUpdate(txt);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            CoreConst.IS_INSTALLED.set(true);
        }
    }

    //获取所有表名称
    private  List<String> tableNames() {
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
