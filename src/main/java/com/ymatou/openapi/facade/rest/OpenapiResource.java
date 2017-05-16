/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.openapi.facade.rest;

import com.ymatou.openapi.model.OpenApiResult;

import javax.servlet.http.HttpServletRequest;

/**
 * @author luoshiqian 2017/5/12 14:35
 */
public interface OpenapiResource {

    OpenApiResult gateway(String appId, String method, HttpServletRequest request);

}
