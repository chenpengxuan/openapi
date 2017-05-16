/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.openapi.model;

/**
 * @author luoshiqian 2017/5/12 14:37
 */
public class OpenApiResult extends BaseResponse {


    private Object content;

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public static OpenApiResult newFailInstance(ReturnCode returnCode) {
        return newFailInstance(returnCode, returnCode.getMessage());
    }

    public static OpenApiResult newFailInstance(ReturnCode returnCode, String message) {
        return newInstance(returnCode.getCode(), message, false);
    }

    public static OpenApiResult newInstance(String returnCode, String message, boolean success) {
        OpenApiResult result = new OpenApiResult();
        result.setCode(returnCode);
        result.setMessage(message);
        result.setSuccess(success);
        return result;
    }


    public static OpenApiResult newInstance(com.ymatou.openapi.biz.facade.resp.BaseResponse bizResp) {
        OpenApiResult result = new OpenApiResult();

        result.setSuccess(bizResp.isSuccess());
        result.setCode(bizResp.getResultCode());
        result.setMessage(bizResp.getMessage());
        result.setContent(bizResp.getContent());

        return result;
    }
}
