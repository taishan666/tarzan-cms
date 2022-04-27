package com.tarzan.cms.utils;


import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.lang.Nullable;
import org.springframework.util.DigestUtils;

public class DigestUtil extends DigestUtils {
    private static final char[] HEX_CODE = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public DigestUtil() {
    }

    public static String md5Hex(final String data) {
        return DigestUtils.md5DigestAsHex(data.getBytes(Charsets.UTF_8));
    }

    public static String md5Hex(final byte[] bytes) {
        return DigestUtils.md5DigestAsHex(bytes);
    }

    public static String sha1Hex(String data) {
        return sha1Hex(data.getBytes(Charsets.UTF_8));
    }

    public static String sha1Hex(final byte[] bytes) {
        return digestHex("SHA-1", bytes);
    }

    public static String sha224Hex(String data) {
        return sha224Hex(data.getBytes(Charsets.UTF_8));
    }

    public static String sha224Hex(final byte[] bytes) {
        return digestHex("SHA-224", bytes);
    }

    public static String sha256Hex(String data) {
        return sha256Hex(data.getBytes(Charsets.UTF_8));
    }

    public static String sha256Hex(final byte[] bytes) {
        return digestHex("SHA-256", bytes);
    }

    public static String sha384Hex(String data) {
        return sha384Hex(data.getBytes(Charsets.UTF_8));
    }

    public static String sha384Hex(final byte[] bytes) {
        return digestHex("SHA-384", bytes);
    }

    public static String sha512Hex(String data) {
        return sha512Hex(data.getBytes(Charsets.UTF_8));
    }

    public static String sha512Hex(final byte[] bytes) {
        return digestHex("SHA-512", bytes);
    }

    public static String digestHex(String algorithm, byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            return encodeHex(md.digest(bytes));
        } catch (NoSuchAlgorithmException var3) {
            throw Exceptions.unchecked(var3);
        }
    }

    public static String hmacMd5Hex(String data, String key) {
        return hmacMd5Hex(data.getBytes(Charsets.UTF_8), key);
    }

    public static String hmacMd5Hex(final byte[] bytes, String key) {
        return digestHMacHex("HmacMD5", bytes, key);
    }

    public static String hmacSha1Hex(String data, String key) {
        return hmacSha1Hex(data.getBytes(Charsets.UTF_8), key);
    }

    public static String hmacSha1Hex(final byte[] bytes, String key) {
        return digestHMacHex("HmacSHA1", bytes, key);
    }

    public static String hmacSha224Hex(String data, String key) {
        return hmacSha224Hex(data.getBytes(Charsets.UTF_8), key);
    }

    public static String hmacSha224Hex(final byte[] bytes, String key) {
        return digestHMacHex("HmacSHA224", bytes, key);
    }

    public static String hmacSha256Hex(String data, String key) {
        return hmacSha256Hex(data.getBytes(Charsets.UTF_8), key);
    }

    public static String hmacSha256Hex(final byte[] bytes, String key) {
        return digestHMacHex("HmacSHA256", bytes, key);
    }

    public static String hmacSha384Hex(String data, String key) {
        return hmacSha384Hex(data.getBytes(Charsets.UTF_8), key);
    }

    public static String hmacSha384Hex(final byte[] bytes, String key) {
        return digestHMacHex("HmacSHA384", bytes, key);
    }

    public static String hmacSha512Hex(String data, String key) {
        return hmacSha512Hex(data.getBytes(Charsets.UTF_8), key);
    }

    public static String hmacSha512Hex(final byte[] bytes, String key) {
        return digestHMacHex("HmacSHA512", bytes, key);
    }

    public static String digestHMacHex(String algorithm, final byte[] bytes, String key) {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(Charsets.UTF_8), algorithm);

        try {
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            return encodeHex(mac.doFinal(bytes));
        } catch (InvalidKeyException | NoSuchAlgorithmException var5) {
            throw Exceptions.unchecked(var5);
        }
    }

    public static String encodeHex(byte[] bytes) {
        StringBuilder r = new StringBuilder(bytes.length * 2);
        byte[] var2 = bytes;
        int var3 = bytes.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte b = var2[var4];
            r.append(HEX_CODE[b >> 4 & 15]);
            r.append(HEX_CODE[b & 15]);
        }

        return r.toString();
    }

    public static boolean slowEquals(@Nullable String a, @Nullable String b) {
        return a != null && b != null ? slowEquals(a.getBytes(Charsets.UTF_8), b.getBytes(Charsets.UTF_8)) : false;
    }

    public static boolean slowEquals(@Nullable byte[] a, @Nullable byte[] b) {
        if (a != null && b != null) {
            if (a.length != b.length) {
                return false;
            } else {
                int diff = a.length ^ b.length;

                for(int i = 0; i < a.length; ++i) {
                    diff |= a[i] ^ b[i];
                }

                return diff == 0;
            }
        } else {
            return false;
        }
    }

    public static String hex(String data) {
        return sha1Hex(data);
    }

    public static String encrypt(String data) {
        return sha1Hex(md5Hex(data));
    }
}
