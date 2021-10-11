package com.tarzan.cms.utils;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Xss防御工具类
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年7月11日
 */
public class XssKillerUtil {
    private static final String[] WHITE_LIST = {"p", "strong", "pre", "code", "span", "blockquote", "em", "a"};
    private static String reg = null;
    private static String legalTags = null;

    static {
        StringBuilder regSb = new StringBuilder("<");
        StringBuilder tagsSb = new StringBuilder();
        for (String s : WHITE_LIST) {
            regSb.append("(?!").append(s).append(" )");
            tagsSb.append('<').append(s).append('>');
        }
        regSb.append("(?!/)[^>]*>");
        reg = regSb.toString();
        legalTags = tagsSb.toString();
    }

    /**
     * xss白名单验证
     *
     * @param xssStr
     * @return
     */
    public static boolean isValid(String xssStr) {
        if (null == xssStr || xssStr.isEmpty()) {
            return true;
        }
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(xssStr);
        while (matcher.find()) {
            String tag = matcher.group();
            if (!legalTags.contains(tag.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 自定义的白名单
     *
     * @return
     */
    private static Whitelist custom() {
        return Whitelist.none().addTags("p", "strong", "pre", "code", "span", "blockquote", "br").addAttributes("span", "class");
    }

    /**
     * 根据白名单，剔除多余的属性、标签
     *
     * @param xssStr
     * @return
     */
    public static String clean(String xssStr) {
        if (null == xssStr || xssStr.isEmpty()) {
            return "";
        }
        return Jsoup.clean(xssStr, custom());
    }

}