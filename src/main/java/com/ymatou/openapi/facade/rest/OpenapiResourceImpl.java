/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.openapi.facade.rest;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

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
@Path("/{api:(?i:api)}/{v1:(?i:v1)}")
@Produces({"application/json; charset=UTF-8"})
@Service(protocol = "rest")
public class OpenapiResourceImpl implements OpenapiResource {

    private static final Logger logger = LoggerFactory.getLogger(OpenapiResourceImpl.class);

    @Autowired
    private OpenapiFacade openapiFacade;

    @Path("/{appId}/{method}")
    @POST
    @Override
    public OpenApiResult gateway(@PathParam("appId") String appId, @PathParam("method") String method,
           @Context HttpServletRequest request) {

        OpenApiResult openApiResult = new OpenApiResult();
        String body;
        String requestId = Utils.uuid();
        // log日志配有"logPrefix"占位符
        MDC.put(Constants.LOG_PREFIX, requestId);
        try {
            body = StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));
            OpenapiReq openapiReq = JSON.parseObject(body, OpenapiReq.class);
            openapiReq.setRequestId(requestId);
            openapiReq.setSourceIp(Utils.getRequestIp(request));

            openApiResult = openapiFacade.gateway(openapiReq);
        } catch (IOException e) {
            // todo
            // openApiResult.setCode(ReturnCode.);
        } catch (JSONException e) {
            // todo
        } catch (Exception e) {
            // todo
        } finally {
            MDC.clear();
        }
        return openApiResult;
    }
}
