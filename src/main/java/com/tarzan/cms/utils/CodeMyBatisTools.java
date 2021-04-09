package com.tarzan.cms.utils;


import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * mybatis通用生成工具
 * 可生成业务接口、dao接口、实体类、映射文件。
 *
 * @author Liu Yutao
 * @className mbTools
 * @email koal@vip.qq.com
 * @date 2016/3/10 19:44
 */

public class CodeMyBatisTools {
    private static final String driver = "com.mysql.cj.jdbc.Driver";//驱动
    private static final String user = "root";  //数据库账号
    private static final String pwd = "123456"; //数据库密码
    private static final String url = "jdbc:mysql://127.0.0.1:3306/ofcms" + "?user=" + user + "&password=" + pwd+"&useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull"; //链接参数
    private static String tableName = "of_cms_ad"; // 数据库表名
    private static String aliasName = "cms_ad"; // 数据库别名,可以与数据库表名相同
    private static final String packagePath = "com/tarzan/cms"; //mapper.xml命名空间路径
    private static final String packageName = "com.tarzan.cms"; //mapper.xml命名空间路径
    private static final String author = "tarzan"; // 作者
    private static final String rootPathName = "src/main/java/"; // 默认生成主文件夹路径
    private static Connection getConnection = null;
    static SimpleDateFormat format = new SimpleDateFormat("YYYY-HH-DD HH:mm:ss");
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
     * 格式化默认值
     */
    private static String defaultValue(String value) {
        if (StringUtils.isNotBlank(value)) {
            return "；默认值：" + value;
        }
        {
            return "";
        }
    }

    private  static String getAliasName(String tableName,String prefix){
        return tableName.substring(prefix.length());
    }

    /**
     * 格式化数据类型
     * 返回的是基本类型的包装类
     * 如果使用基本数据类型long
     */
    private static String formatType(String typeValue) {

        if ("bit".equalsIgnoreCase(typeValue)) {
            return "Boolean";
        }
        else if (
                typeValue.equalsIgnoreCase("int") || typeValue.equalsIgnoreCase("int unsigned")
                        || typeValue.equalsIgnoreCase("tinyint") || typeValue.equalsIgnoreCase("tinyint unsigned")
                        || typeValue.equalsIgnoreCase("smallint") || typeValue.equalsIgnoreCase("smallint unsigned")
                        || typeValue.equalsIgnoreCase("mediumint") || typeValue.equalsIgnoreCase("mediumint unsigned")
                ) {
            return "Integer";
        } else if (typeValue.equalsIgnoreCase("bigint") || typeValue.equalsIgnoreCase("bigint unsigned")) {
            return "Long";
        } else if (typeValue.equalsIgnoreCase("float") || typeValue.equalsIgnoreCase("float unsigned")) {
            return "Float";
        } else if (typeValue.equalsIgnoreCase("decimal") || typeValue.equalsIgnoreCase("decimal unsigned") || typeValue.equalsIgnoreCase("numeric") || typeValue.equalsIgnoreCase("numeric unsigned")
                || typeValue.equalsIgnoreCase("real") || typeValue.equalsIgnoreCase("real unsigned") || typeValue.equalsIgnoreCase("money") || typeValue.equalsIgnoreCase("money unsigned")
                || typeValue.equalsIgnoreCase("smallmoney") || typeValue.equalsIgnoreCase("smallmoney unsigned")) {
            return "Double";
        } else if (typeValue.equalsIgnoreCase("varchar") || typeValue.equalsIgnoreCase("char")
                || typeValue.equalsIgnoreCase("nvarchar") || typeValue.equalsIgnoreCase("nchar")
                || typeValue.equalsIgnoreCase("text")) {
            return "String";
        } else if (typeValue.equalsIgnoreCase("datetime")) {
            return "Date";
        } else if (typeValue.equalsIgnoreCase("image")) {
            return "Blod";
        } else {
            return "Long";
        }

    }


    /**
     * 驼峰转换
     */
    private static String columnToProperty(String column) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (column == null || column.isEmpty()) {
            // 没必要转换
            return "";}
        else column =column.toLowerCase();
        
         if (!column.contains("_")) {
            // 不含下划线，仅将首字母小写
            return column.substring(0, 1).toLowerCase() + column.substring(1);
        } else {
            // 用下划线将原始字符串分割
            String[] columns = column.split("_");
            for (String columnSplit : columns) {
                // 跳过原始字符串中开头、结尾的下换线或双重下划线
                if (columnSplit.isEmpty()) {
                    continue;
                }
                // 处理真正的驼峰片段
                if (result.length() == 0) {
                    // 第一个驼峰片段，全部字母都小写
                    result.append(columnSplit.toLowerCase());
                } else {
                    // 其他的驼峰片段，首字母大写
                    result.append(columnSplit.substring(0, 1).toUpperCase()).append(columnSplit.substring(1).toLowerCase());
                }
            }
            return result.toString();
        }
    }

    /**
     * 实体名称转换
     */
    private static String formatBeanName(String column) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (column == null || column.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!column.contains("_")) {
            // 不含下划线，仅将首字母大写
            return column.substring(0, 1).toUpperCase() + column.substring(1);
        } else {
            // 用下划线将原始字符串分割
            String[] columns = column.split("_");
            for (String columnSplit : columns) {
                // 跳过原始字符串中开头、结尾的下换线或双重下划线
                if (columnSplit.isEmpty()) {
                    continue;
                }
                // 处理真正的驼峰片段
                result.append(columnSplit.substring(0, 1).toUpperCase()).append(columnSplit.substring(1).toLowerCase());
            }
            return result.toString();
        }
    }

    
    /**
     * 实体类字段
     */
    private static void getBean(String tableName,String aliasName) {
        getConnection = getConnections();
        StringBuilder sb = new StringBuilder();
        try {
            DatabaseMetaData dbmd = getConnection.getMetaData();
            ResultSet rs = dbmd.getColumns(null, "%", tableName, "%");
            String beanName = formatBeanName(aliasName);
            sb.append("package "+packageName+".entity;\n\n");
            sb.append("import com.baomidou.mybatisplus.annotation.TableName;\n");
            sb.append("import lombok.Data;\n");
            int length=sb.length();
            boolean dateFlag=false;
            sb.append(  " /**\n" +
	                    "  * @author " + author + "\n" +
	                    "  * @date "+ format.format(new Date())+"\n" +
	                    "  */\n" +
                        "@Data\n" +
                        "@TableName(\""+tableName+"\")\n" +
	                    "public class "+beanName+"Entity {\n");
            while (rs.next()) {
                if(formatType(rs.getString("TYPE_NAME")).equals("Date")){
                    dateFlag=true;
                }
                sb.append("\t//").append(rs.getString("REMARKS")).append(defaultValue(rs.getString("COLUMN_DEF"))).append("\n");
                sb.append("\tprivate ").append(formatType(rs.getString("TYPE_NAME"))).append(" ").append(columnToProperty(rs.getString("COLUMN_NAME"))).append(";\n");
            }
            sb.append("} ");
            if(dateFlag){
                sb.insert(length, "import java.util.Date;\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        write(sb.toString(),"Entity.java","entity");
        System.err.println("\n类型：JAVA数据层实体类（bean.java）" + "\n状态：成功" + "\n时间：" + format.format(new Date()) + "\n");
    }
    

    /**
     * 生成DAO层接口
     */
    private static void getMapper(String tableName,String aliasName) {
        StringBuilder sb = new StringBuilder();
        try {
            String beanName = formatBeanName(aliasName);
            sb.append("package "+packageName+".mapper;\n\n");
            sb.append("import com.baomidou.mybatisplus.core.mapper.BaseMapper;\n");
            sb.append("import "+packageName+".entity."+beanName+"Entity;\n");
            sb.append(  "/**\n" +
	                    " * @author " + author + "\n" +
	                    " * @date "+ format.format(new Date())+"\n" +
	                    " */\n" +
	                    "public interface "+beanName+"Mapper extends BaseMapper<"+beanName+"Entity>{\n" +
	                    " \n" +
	            		"}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        write(sb.toString(),"Mapper.java","mapper");
        System.err.println("\n类型：JAVA数据持久层接口（dao.java）" + "\n状态：成功" + "\n时间：" + format.format(new Date()) + "\n");
    }

    
    /**
     * 生成SERVICE层接口
     */
    private static void getService(String tableName,String aliasName) {
        StringBuilder sb = new StringBuilder();
        try {
            String beanName = formatBeanName(aliasName);
            sb.append("package "+packageName+".service;\n\n");
            sb.append("import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;\n");
            sb.append("import org.springframework.stereotype.Service;\n");
            sb.append("import "+packageName+".mapper."+beanName+"Mapper;\n");
            sb.append("import "+packageName+".entity."+beanName+"Entity;\n");
            sb.append(  "/**\n" +
	                    " * @author " + author + "\n" +
	                    " * @date "+ format.format(new Date())+"\n" +
	                    " */\n" +
                        "@Service\n" +
	                    "public class "+beanName+"Service extends ServiceImpl<"+beanName+"Mapper, "+beanName+"Entity>{\n" +
	                    "\n" +
	            		"}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        write(sb.toString(),"Service.java","service");
        System.err.println("\n类型：JAVA业务层接口（service.java）" + "\n状态：成功" + "\n时间：" + format.format(new Date()) + "\n");
       
    }

    /**
     * 写文件，支持中文字符，在linux redhad下测试过
     * @param str 文本内容
     * @param name 文本名称
     * */
    private static void write(String str, String name,String type) {
        try {
            File dir = new File(rootPathName +packagePath+ "/" + type);
            dir.mkdirs();
            String path = dir.getPath() + "/" + formatBeanName(aliasName)+name;
            File file = new File(path);
            if (!file.exists())
                file.createNewFile();
            
            FileOutputStream out = new FileOutputStream(file, false); //如果追加方式用true
            StringBuilder sb = new StringBuilder();
            sb.append(str + "\n");
            out.write(sb.toString().getBytes("utf-8"));//注意需要转换对应的字符集
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void tableNames() {
        getConnection = getConnections();
        try {
            DatabaseMetaData dbmd = getConnection.getMetaData();
            ResultSet rs = dbmd.getTables(getConnection.getCatalog(), null, null, new String[] { "TABLE" });
            while (rs.next()) {
                tableName=rs.getString("TABLE_NAME");
                aliasName=getAliasName(tableName,"of_");
                //实体
                	getBean(tableName,aliasName);
                //dao层接口
                	getMapper(tableName,aliasName);
                //业务类接口
                	getService(tableName,aliasName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


 
    public static void main(String[] args) {
    //  tableNames();


        //实体
    //	getBean(tableName,aliasName);
    	//dao层接口
    //	getMapper(tableName,aliasName);
        //业务类接口
    //	getService(tableName,aliasName);
    }

}
