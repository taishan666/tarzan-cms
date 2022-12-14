package com.tarzan.cms.common.exception;

/**
 * 自定义上传文件未找到异常
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
public class UploadFileNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -7812372861340782170L;

    public UploadFileNotFoundException() {
    }

    public UploadFileNotFoundException(String message) {
        super(message);
    }

}
