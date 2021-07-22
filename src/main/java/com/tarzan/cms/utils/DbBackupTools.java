package com.tarzan.cms.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 数据库数据备份
 *
 * @author tarzan Liu
 * @date 2021/7/10 19:44
 */
@Slf4j
@Component
public class DbBackupTools {
    @Value("${spring.datasource.driverClassName}")
    private String driver;//驱动
    @Value("${spring.datasource.username}")
    private String user;  //数据库账号
    @Value("${spring.datasource.password}")
    private String pwd; //数据库密码
    @Value("${spring.datasource.url}")
    private String url;//链接参数
    @Resource
    private  JdbcTemplate jdbcTemplate;
    private  final  static  String prefix="backupSql_";
    static   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

    //获取数据sql
    private  String getDataSql() {
        StringBuilder sb=new StringBuilder();
        try {
            tableNames().forEach(t->{
                sb.append("TRUNCATE "+t+";\n");
                List<Map<String, Object>> list=jdbcTemplate.queryForList("select * from "+t);
                list.forEach(e->{
                    sb.append("INSERT INTO "+t+ " VALUES (");
                    e.forEach((k,v)->{
                        if(v instanceof String){
                            if(((String) v).contains("\r\n")){
                              v= ((String) v).replaceAll("\r\n","\\\\r\\\\n");
                            }
                            sb.append("'"+v+"'"+",");
                        }else if(v instanceof Date){
                            sb.append("'"+format.format(v)+"'"+",");
                        }else{
                            sb.append(v+",");
                        }
                    });
                    sb.append("); \n");
                });
            });
           return sb.toString().replace(",);",");");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    //数据还原
    public synchronized  boolean rollback(String fileName) {
        List<String> list=new ArrayList<>();
        try {
            FileInputStream out = new FileInputStream(getBackupPath()+fileName);
            InputStreamReader reader = new InputStreamReader(out, StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(reader);
            String line;
            while ((line = in.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        jdbcTemplate.batchUpdate(list.toArray(new String[list.size()]));
        return true;
    }

    //数据备份
    public synchronized boolean backSql() {
        try {
            File dir = new File(getBackupPath());
            dir.mkdirs();
            String path = dir.getPath() + "/"+ prefix+System.currentTimeMillis()+".sql" ;
            File file = new File(path);
            if (!file.exists()){
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file, false); //如果追加方式用true
            out.write(getDataSql().getBytes("utf-8"));//注意需要转换对应的字符集
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public  String getBackupPrefix() {
        return prefix;
    }

    //获得备份路径 ，jar包启动备份文件夹在同级目录
    public  String getBackupPath() {
        String classPath = DbBackupTools.class.getResource("/").getPath();
        if (classPath.indexOf(".jar") > 0) {
            classPath = classPath.substring(0, classPath.lastIndexOf(".jar"));
            classPath = classPath.substring(6, classPath.lastIndexOf("/"));
            String sqlBackupPath = File.separator + classPath + "dbBackup/";
            log.info("========数据备份路径：" + sqlBackupPath + "========");
            return sqlBackupPath;
        } else {
            String sqlBackupPath = classPath + "dbBackup/";
            log.info("========数据备份路径：" + sqlBackupPath + "========");
            return sqlBackupPath;
        }
    }


}
