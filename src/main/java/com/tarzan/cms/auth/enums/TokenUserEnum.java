/*
 * Copyright (c) 2018-2028, tarzan  Zhuang All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution. Neither the name of the dreamlu.net developer nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. Author: tarzan  庄骞 (smalltarzan @163.com)
 */
package com.tarzan.cms.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型枚举
 *
 * @author tarzan 
 */
@Getter
@AllArgsConstructor
public enum TokenUserEnum {

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
