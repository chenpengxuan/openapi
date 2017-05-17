/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.openapi.facade.rest;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.ymatou.openapi.model.ReturnCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.ymatou.openapi.constants.Constants;
import com.ymatou.openapi.facade.OpenapiFacade;
import com.ymatou.openapi.model.OpenApiResult;
import com.ymatou.openapi.model.OpenapiReq;
import com.ymatou.openapi.util.Utils;

/**
 * @author luoshiqian 2017/5/12 14:40
 */
@Component("openapiResource")
@Path("/api")
@Produces({"application/json; charset=UTF-8"})
@Consumes({MediaType.APPLICATION_JSON})
@Service(protocol = "rest")
public class OpenapiResourceImpl implements OpenapiResource {

    private static final Logger logger = LoggerFactory.getLogger(OpenapiResourceImpl.class);

    @Autowired
    private OpenapiFacade openapiFacade;

    @Path("/v1")
    @POST
    @Override
    public OpenApiResult gateway(@QueryParam("appId") String appId, @QueryParam("method") String method,
                                 @Context HttpServletRequest request) {
        String body = null;
        OpenapiReq openapiReq  = null;

        try {
            body = StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));
            openapiReq = JSON.parseObject(body, OpenapiReq.class);
        } catch (Exception e) {
            logger.error("Failed to parse request json body:{}", body, e);
            return OpenApiResult.newFailInstance(ReturnCode.BIZ_PARAM_JSON_FORMAT_ERR);
        }
        if ( openapiReq == null ) {
            logger.error("Receive empty json body:{}", body);
            return OpenApiResult.newFailInstance(ReturnCode.BIZ_PARAM_JSON_FORMAT_ERR);
        }
        try{
            openapiReq.setRequestId(Utils.uuid() + "_" + openapiReq.getNonceStr());
            // log日志配有"logPrefix"占位符
            MDC.put(Constants.LOG_PREFIX, openapiReq.getRequestId());

            openapiReq.setSourceIp(Utils.getRequestIp(request));

            return openapiFacade.gateway(openapiReq);

        } catch (Exception e) {
            logger.error("Failed to execute method {} for app {}", method, appId, e);
            return OpenApiResult.newFailInstance(ReturnCode.ERROR);
        } finally {
            MDC.clear();
        }
    }
}
