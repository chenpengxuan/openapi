/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.openapi.facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.ymatou.openapi.model.OpenApiResult;
import com.ymatou.openapi.model.OpenapiReq;
import com.ymatou.openapi.model.ReturnCode;

/**
 * @author luoshiqian 2017/5/16 14:09
 */
public class OpenapiFacadeRestTest {
    private static final Logger logger = LoggerFactory.getLogger(OpenapiFacadeRestTest.class);

    String url = "http://openapi.iapi.ymatou.com/api/v1?app_id=%s&method=%s";

    HttpClient httpClient = buildClient();

    private CloseableHttpClient buildClient() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setDefaultMaxPerRoute(20);
        cm.setMaxTotal(100);

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();

        return HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).setConnectionManager(cm).build();
    }

    @Test
    public void testGateWay() throws Exception {
        String appId = "Ce0I6qRbFFl1KFW5rj";
        String authCode = "OEshdxP9vk0PvdadZv1RvErKMT54ZFVv";
        String appSecret = "di9P75Nu3NxlsCwJxYlfgD51tAwVfELV";

        String method = "app.method";
        String bizContent = "";

        String path = String.format(url, appId, method);
        OpenapiReq req = OpenapiFacadeTest.createOpenApiReq(appId, authCode, appSecret, method, bizContent);

        String respBody = httpRequest(path, JSON.toJSONString(req));

        logger.info("respbody:{}", respBody);

        OpenApiResult openApiResult = JSON.parseObject(respBody, OpenApiResult.class);

        assertTrue(openApiResult.isSuccess());
        assertEquals(ReturnCode.SUCCESS.getCode(), openApiResult.getCode());
    }


    private String httpRequest(String url, String request) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        StringEntity postEntity = new StringEntity(request, "UTF-8");
        httpPost.setEntity(postEntity);
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");

        HttpResponse response = httpClient.execute(httpPost);

        String respBody = EntityUtils.toString(response.getEntity(), "UTF-8");
        return respBody;
    }



    @Test
    public void getGetOrderDetail() throws Exception {
        String appId = "Ce0I6qRbFFl1KFW5rj";
        String authCode = "OEshdxP9vk0PvdadZv1RvErKMT54ZFVv";
        String appSecret = "di9P75Nu3NxlsCwJxYlfgD51tAwVfELV";

        String method = "ymatou.order.detail.get";
        String bizContent = "{\"order_id\":112531026L}";

        String path = String.format(url, appId, method);
        OpenapiReq req = OpenapiFacadeTest.createOpenApiReq(appId, authCode, appSecret, method, bizContent);

        String respBody = httpRequest(path, JSON.toJSONString(req));

        logger.info("respbody:{}", respBody);

        OpenApiResult openApiResult = JSON.parseObject(respBody, OpenApiResult.class);

        assertTrue(openApiResult.isSuccess());
        assertEquals(ReturnCode.SUCCESS.getCode(), openApiResult.getCode());
    }
}
