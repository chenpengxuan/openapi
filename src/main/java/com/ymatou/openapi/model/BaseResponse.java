/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.openapi.model;


import com.alibaba.fastjson.annotation.JSONField;

/**
 * 响应基类. <em>其所有子类必须有默认的构造函数</em>
 * 
 * @author tuwenjie
 *
 */
public class BaseResponse extends PrintFriendliness {

    private static final long serialVersionUID = -5719901720924490738L;

    @JSONField(serialize = false)
    private boolean isSuccess;

    private String code;

    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public BaseResponse(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public BaseResponse() {}

    public static BaseResponse newSuccessInstance() {
        BaseResponse result = new BaseResponse();
        result.setSuccess(true);
        return result;
    }

    public static BaseResponse newFailInstance(ReturnCode returnCode){
        return newFailInstance(returnCode,returnCode.getMessage());
    }

    public static BaseResponse newFailInstance(ReturnCode returnCode,String message){
        BaseResponse result = new BaseResponse();
        result.setCode(returnCode.getCode());
        result.setMessage(message);
        result.setSuccess(false);
        return result;
    }
}
