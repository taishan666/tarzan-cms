package com.tarzan.cms.utils;


import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

public class Func {
    public Func() {
    }

    public static String toStr(Object str) {
        return toStr(str, "");
    }

    public static String toStr(Object str, String defaultValue) {
        return null != str && !str.equals("null") ? String.valueOf(str) : defaultValue;
    }

    public static int toInt(final Object str) {
        return NumberUtil.toInt(String.valueOf(str));
    }

    public static int toInt(@Nullable final Object str, final int defaultValue) {
        return NumberUtil.toInt(String.valueOf(str), defaultValue);
    }

    public static long toLong(final Object str) {
        return NumberUtil.toLong(String.valueOf(str));
    }

    public static long toLong(@Nullable final Object str, final long defaultValue) {
        return NumberUtil.toLong(String.valueOf(str), defaultValue);
    }

    public static Double toDouble(Object value) {
        return toDouble(String.valueOf(value), -1.0D);
    }

    public static Double toDouble(Object value, Double defaultValue) {
        return NumberUtil.toDouble(String.valueOf(value), defaultValue);
    }

    public static Float toFloat(Object value) {
        return toFloat(String.valueOf(value), -1.0F);
    }

    public static Float toFloat(Object value, Float defaultValue) {
        return NumberUtil.toFloat(String.valueOf(value), defaultValue);
    }

    public static Boolean toBoolean(Object value) {
        return toBoolean(value, (Boolean)null);
    }

    public static Boolean toBoolean(Object value, Boolean defaultValue) {
        if (value != null) {
            String val = String.valueOf(value);
            val = val.toLowerCase().trim();
            return Boolean.parseBoolean(val);
        } else {
            return defaultValue;
        }
    }

    public static boolean isNoneBlank(final CharSequence... css) {
        return StringUtil.isNoneBlank(css);
    }

    public static String toStrWithEmpty(Object str, String defaultValue) {
        return null != str && !str.equals("null") && !str.equals("") ? String.valueOf(str) : defaultValue;
    }

    public static boolean isAnyBlank(final CharSequence... css) {
        return StringUtil.isAnyBlank(css);
    }

    public static boolean isEmpty(@Nullable Object obj) {
        return ObjectUtils.isEmpty(obj);
    }

    public static boolean equals(Object obj1, Object obj2) {
        return Objects.equals(obj1, obj2);
    }
}
