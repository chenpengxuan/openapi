/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.openapi.model;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * @author luoshiqian 2017/5/12 15:10
 */
public class BaseRequest extends PrintFriendliness {
    /**
     * 序列化版本
     */
    private static final long serialVersionUID = 1995776180594622716L;

    /**
     * 数据验证器
     */
    private static Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 请求Id
     */
    private String requestId;

    private String sourceIp;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    /**
     * 验证数据有效性
     */
    public void validate() {
        StringBuilder errorMsgs = new StringBuilder();
        Set<ConstraintViolation<BaseRequest>> violations = VALIDATOR.validate(this);

        if (violations != null && violations.size() > 0) {
            for (ConstraintViolation<BaseRequest> violation : violations) {
                errorMsgs.append(violation.getMessage()).append("|");
            }
            throw new IllegalArgumentException(errorMsgs.substring(0, errorMsgs.length() - 1));
        }
    }

}
