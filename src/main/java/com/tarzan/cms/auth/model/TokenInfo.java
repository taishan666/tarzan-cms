package com.tarzan.cms.auth.model;

import lombok.Data;

@Data
public class TokenInfo {
    private String token;
    private int expire;

}
