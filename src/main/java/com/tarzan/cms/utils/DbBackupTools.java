package com.tarzan.cms.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
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
public class DbBackupTools {
    private static final String driver = "com.mysql.cj.jdbc.Driver";//驱动
    private static final String user = "root";  //数据库账号
    private static final String pwd = "123456"; //数据库密码
    private static final String url = "jdbc:mysql://127.0.0.1:3306/tarzan_cms_test" + "?user=" + user + "&password=" + pwd+"&useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull"; //链接参数
    private static final String dbBackupPath = "src/main/java/sql/";
    private static Connection getConnection = null;
    private static JdbcTemplate jdbcTemplate=null;
   static   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 链接数据库
     */
    private static Connection getConnections() {
        try {
            Class.forName(driver);
            getConnection = DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getConnection;
    }

    /**
     * 获取jdbc数据源
     */
    private static JdbcTemplate getJdbcTemplate() {
        try {
            DruidDataSource ds = new DruidDataSource();
            ds.setUsername(user);
            ds.setPassword(pwd);
            ds.setUrl(url);
            ds.setDriverClassName(driver);
            jdbcTemplate=new JdbcTemplate(ds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jdbcTemplate;
    }


    //获取所有表名称
    private static List<String> tableNames() {
        List<String> tableNames= Lists.newArrayList();
        getConnection = getConnections();
        try {
            DatabaseMetaData dbmd = getConnection.getMetaData();
            ResultSet rs = dbmd.getTables(getConnection.getCatalog(), null, null, new String[] { "TABLE" });
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
    private static String getDataSql() {
        jdbcTemplate= getJdbcTemplate();
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
    public synchronized static boolean rollback(String fileName) {
        jdbcTemplate= getJdbcTemplate();
        List<String> list=Lists.newArrayList();
        try {
            FileInputStream out = new FileInputStream(dbBackupPath+fileName);
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
    public synchronized static boolean backSql() {
        try {
            File dir = new File(dbBackupPath);
            dir.mkdirs();
            String path = dir.getPath() + "/"+ "backupSql_"+System.currentTimeMillis()+".sql" ;
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

    private synchronized static String getBackupPath() {
        String classPath = new DbBackupTools().getClass().getResource("/").getPath();
        if (classPath.indexOf(".jar") > 0) {
            classPath = classPath.substring(0, classPath.lastIndexOf(".jar"));
            classPath = classPath.substring(6, classPath.lastIndexOf("/"));
            String sqlBackupPath = File.separator + classPath + "/dbBackup/";
            log.info("========数据备份路径：" + sqlBackupPath + "========");
            return sqlBackupPath;
        } else {
            String projectPath = classPath.replace("target/classes/", "");
            String sqlBackupPath = projectPath + "/dbBackup/";
            log.info("========数据备份路径：" + sqlBackupPath + "========");
            return sqlBackupPath;
        }
    }






    public static void main(String[] args) {
        backSql();
        rollback("backupSql_1626862416476.sql");
    }

}
