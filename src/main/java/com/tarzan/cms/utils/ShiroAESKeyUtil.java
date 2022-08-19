package com.tarzan.cms.utils;

import org.apache.shiro.crypto.AesCipherService;

import java.security.Key;
import java.util.Base64;

/**
 * @author tarzan
 */
public class ShiroAESKeyUtil {

    public static String getKey(){
        AesCipherService aesCipherService = new AesCipherService();
        Key key = aesCipherService.generateNewKey();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

}
