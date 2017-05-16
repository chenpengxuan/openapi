/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.openapi.model;

/**
 * @author luoshiqian 2017/5/12 15:12
 */
public enum ReturnCode {


    /******************************* 公共返回码 *************************************************/

    SUCCESS("0000", "成功"),

    LACK_PARAM("0001", "缺少必填参数"),

    INVALID_AUTH_CODE("0002", "非法授权码"),

    INVALID_REQ_TIME("0003", "非法请求时间"),

    SIGN_VERIFY_FAIL("0004", "验签失败"),

    INVALID_API_NAME("0005", "非法api名称"),

    INVALID_API_ID("0006", "非法app_id"),

    BIZ_PARAM_JSON_FORMAT_ERR("1001", "业务请求参数JSON格式不正确!"),

    ERROR("0009", "处理失败,请稍后重试"),

    /******************************* 公共返回码 *************************************************/

    ;

    String code;
    String message;

    ReturnCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
