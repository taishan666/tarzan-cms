package com.tarzan.cms.auth.constant;

/**
 * 通用常量
 *
 * @author tarzan 
 */
public interface AppConstant {


    Long DEFAULT_ID = 0L;

    Long DEFAULT_USER_ID=0L;

    /**
     * 默认租户ID
     */
    String DEFAULT_TENANT_ID = "000000";

    /**
     * sword 系统名
     */
    String SWORD_NAME = "sword";

    /**
     * saber 系统名
     */
    String SABER_NAME = "saber";

    /**
     * 顶级父节点id
     */
    Long TOP_PARENT_ID = 0L;

    /**
     * 顶级父节点名称
     */
    String TOP_PARENT_NAME = "顶级";

    /**
     * 未封存状态值
     */
    Integer NOT_SEALED_ID = 0;

    /**
     * 默认密码
     */
    String DEFAULT_PASSWORD = "123456";

    /**
     * 默认密码参数值
     */
    String DEFAULT_PARAM_PASSWORD = "account.initPassword";

    /**
     * 默认排序字段
     */
    String SORT_FIELD = "sort";

    /**
     * 数据权限类型
     */
    Integer DATA_SCOPE_CATEGORY = 1;

    /**
     * 接口权限类型
     */
    Integer API_SCOPE_CATEGORY = 2;

    /** 默认byte[] 长度，多用于构建缓冲区 */
    Integer BYTES_LENGTH = 1024;

    /** IPV6 本机地址 */
    String IPV6_LOCAL = "0:0:0:0:0:0:0:1";

    /** IPV4 本机地址 */
    String IPV4_LOCAL = "127.0.0.1";

    /** 通用模糊查询替换模板 */
    String LIKE_TEMPLATE = "%{0}%";

    /** 半角逗号 */
    String COMMA = ",";

    /** 是 */
    int YES = 1;

    /** 否 */
    int NO = 0;

    /** 删除标记：已删除 */
    int DELETED = 1;

    /** 删除标记：未删除 */
    int NOT_DELETED = 0;

    /** 状态：禁用/停用 */
    int DISABLE = 0;

    /** 状态：启用 */
    int ENABLE = 1;

    /** 空字符串 */
    String EMPTY_STRING = "";

    /** 0字符串 */
    String ZERO_STRING = "0";

    /** 0 */
    int ZERO = 0;

    /** 默认页码 */
    int DEFAULT_PAGE_NUM = 1;

    /** 默认每页记录数 */
    int DEFAULT_ROWS = 10;

    /** 环境标记：非系统内部 */
    int NOT_SYSTEM = 0;

    /** 环境标记：系统内部 */
    int SYSTEM = 1;

    Long SLEEP_TIME = 500L;

    Long TOTAL_SLEEP_TIME = 10000L;

}
