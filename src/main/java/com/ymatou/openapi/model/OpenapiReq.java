/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.openapi.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.validation.constraints.NotNull;

/**
 * @author luoshiqian 2017/5/12 14:48
 */
public class OpenapiReq extends BaseRequest {

    /**
     * 洋码头分配的应用appId
     */
    @JSONField(name = "app_id")
    @NotNull
    private String appId;

    /**
     * api名称
     */
    private String method;

    /**
     * 签名摘要算法，目前只支持：MD5
     */
    @JSONField(name = "sign_method")
    private String signMethod;

    /**
     * 授权码，针对买手与应用的唯一授权码
     */
    @JSONField(name = "auth_code")
    private String authCode;

    /**
     * 时间戳，格式为yyyy-MM-dd HH:mm:ss，时区为GMT+8，例如：2017-01-01 12:00:00。洋码头API服务端允许客户端请求最大时间误差为10分钟。
     */
    private String timestamp;

    /**
     * API输入参数签名结果
     */
    private String sign;

    /**
     * 随机字符串，长度要求在32位以内。推荐随机算法
     */
    @JSONField(name = "nonce_str")
    private String nonceStr;

    /**
     * 业务请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各api请求参数。 传入的是json格式字符串
     */
    @JSONField(name = "biz_content")
    private String bizContent;


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSignMethod() {
        return signMethod;
    }

    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getBizContent() {
        return bizContent;
    }

    public void setBizContent(String bizContent) {
        this.bizContent = bizContent;
    }
}
