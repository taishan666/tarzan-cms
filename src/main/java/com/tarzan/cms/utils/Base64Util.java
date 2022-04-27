package com.tarzan.cms.utils;


import java.nio.charset.Charset;
import org.springframework.util.Base64Utils;

public class Base64Util extends Base64Utils {
    public Base64Util() {
    }

    public static String encode(String value) {
        return encode(value, Charsets.UTF_8);
    }

    public static String encode(String value, Charset charset) {
        byte[] val = value.getBytes(charset);
        return new String(encode(val), charset);
    }

    public static String encodeUrlSafe(String value) {
        return encodeUrlSafe(value, Charsets.UTF_8);
    }

    public static String encodeUrlSafe(String value, Charset charset) {
        byte[] val = value.getBytes(charset);
        return new String(encodeUrlSafe(val), charset);
    }

    public static String decode(String value) {
        return decode(value, Charsets.UTF_8);
    }

    public static String decode(String value, Charset charset) {
        byte[] val = value.getBytes(charset);
        byte[] decodedValue = decode(val);
        return new String(decodedValue, charset);
    }

    public static String decodeUrlSafe(String value) {
        return decodeUrlSafe(value, Charsets.UTF_8);
    }

    public static String decodeUrlSafe(String value, Charset charset) {
        byte[] val = value.getBytes(charset);
        byte[] decodedValue = decodeUrlSafe(val);
        return new String(decodedValue, charset);
    }
}
