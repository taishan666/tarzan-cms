package com.tarzan.cms.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserEnum {

    /**
     * web
     */
    WEB("web", 1),

    /**
     * app
     */
    APP("app", 2),;

    final String name;
    final int category;

}