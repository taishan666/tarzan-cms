package com.tarzan.cms.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
    @Resource
    private  JdbcTemplate jdbcTemplate;
    @Getter
    private  String filePrefix="backupSql_";
    @Getter
    private  String sqlBackupPath;
    static   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    private void  init(){
       sqlBackupPath=getBackupPath();
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

    //数据还原
    public synchronized  boolean rollback(String fileName) {
        Long stat=System.currentTimeMillis();
        List<String> list=new ArrayList<>();
        try {
            FileInputStream out = new FileInputStream(sqlBackupPath+fileName);
            InputStreamReader reader = new InputStreamReader(out, StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(reader);
            String line;
            while ((line = in.readLine()) != null) {
                list.add(line);
                if(list.size()>=500){
                    jdbcTemplate.batchUpdate(list.toArray(new String[list.size()]));
                    log.info("分批500条sql完毕。。。。。。");
                    list.clear();
                }
            }
            if(list.size()>0){
                jdbcTemplate.batchUpdate(list.toArray(new String[list.size()]));
                log.info("剩余sql完毕。。。。。。");
                list.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        log.info("恢复数据耗时 "+(System.currentTimeMillis()-stat)+" ms");
        return true;
    }

    //数据备份
    public synchronized boolean backSql() {
        Long stat=System.currentTimeMillis();
        try {
            File dir = new File(sqlBackupPath);
            dir.mkdirs();
            String path = dir.getPath() + "/"+ filePrefix+System.currentTimeMillis()+".sql" ;
            File file = new File(path);
            if (!file.exists()){
                file.createNewFile();
            }
            tableNames().forEach(t->{
               StringBuilder sb=new StringBuilder();
               sb.append("TRUNCATE "+t+";\n");
                List<Map<String, Object>> list=jdbcTemplate.queryForList("select * from "+t);
                list.forEach(e->{
                    sb.append("INSERT INTO "+t+ " VALUES (");
                    e.forEach((k,v)->{
                        if(v instanceof String){
                            if (((String) v).contains("\n")) {
                                v = ((String) v).replaceAll("\n", "\\\\n");
                            }
                            if (((String) v).contains("\r")) {
                                v = ((String) v).replaceAll("\r", "\\\\r");
                            }
                            sb.append("'" + v + "'" + ",");
                        }else if(v instanceof Date){
                            sb.append("'"+format.format(v)+"'"+",");
                        }else{
                            sb.append(v+",");
                        }
                    });
                    sb.append("); \n");
                });
                String sql= sb.toString().replace(",);",");");
                try{
                FileOutputStream out = new FileOutputStream(file, true); //如果追加方式用true
                out.write(sql.getBytes("utf-8"));//注意需要转换对应的字符集
                out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        log.info("备份文件耗时 "+(System.currentTimeMillis()-stat)+" ms");
        return true;
    }

    //获得备份路径 ，jar包启动备份文件夹在同级目录
    private  String getBackupPath() {
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
