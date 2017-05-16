/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.openapi.facade;

import com.google.common.collect.Maps;
import com.ymatou.openapi.BaseTest;
import com.ymatou.openapi.biz.facade.OpenapiBizFacade;
import com.ymatou.openapi.biz.facade.model.ResultCode;
import com.ymatou.openapi.biz.facade.req.OpenapiBizReq;
import com.ymatou.openapi.biz.facade.resp.BaseResponse;
import com.ymatou.openapi.constants.Constants;
import com.ymatou.openapi.model.OpenApiResult;
import com.ymatou.openapi.model.OpenapiReq;
import com.ymatou.openapi.model.ReturnCode;
import com.ymatou.openapi.util.Utils;
import mockit.Mock;
import mockit.MockUp;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static mockit.Deencapsulation.setField;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author luoshiqian 2017/5/16 14:09
 */
public class OpenapiFacadeTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(OpenapiFacadeTest.class);

    @Autowired
    private OpenapiFacade openapiFacade;

    @Autowired(required = false)
    private OpenapiBizFacade openapiBizFacade;

    @Test
    public void testGateWay(){
        OpenapiReq req = createOpenApiReq("app.method","");

        this.mockOpenBizFacadeSuccess();

        OpenApiResult openApiResult = openapiFacade.gateway(req);

        logger.info("result:{}",openApiResult);

        assertTrue(openApiResult.isSuccess());
        assertEquals(ReturnCode.SUCCESS.getCode(),openApiResult.getCode());
        assertEquals("成功",openApiResult.getMessage());
    }

    public static OpenapiReq createOpenApiReq(String method,String bizContent){
        String appId = "Ce0I6qRbFFl1KFW5rj";
        String authCode = "OEshdxP9vk0PvdadZv1RvErKMT54ZFVv";
        String appSecret = "di9P75Nu3NxlsCwJxYlfgD51tAwVfELV";

        return createOpenApiReq(appId,authCode,appSecret,method,bizContent);
    }

    public static OpenapiReq createOpenApiReq(String appId,String authCode,String appSecret,String method,String bizContent){

        String timestamp = DateTime.now().toString(Constants.FORMATTER_YYYYMMDDHHMMSS);
        String nonceStr = RandomStringUtils.randomAlphanumeric(32);

        Map<String, String> map = Maps.newTreeMap();
        map.put("app_id", appId);
        map.put("method", method);
        map.put("sign_method", "MD5");
        map.put("auth_code", authCode);
        map.put("timestamp", timestamp);
        map.put("nonce_str", nonceStr);
        map.put("biz_content", bizContent);
        String sign = OpenapiFacade.generateMD5(map,appSecret);

        OpenapiReq req = new OpenapiReq();
        req.setRequestId(Utils.uuid());
        req.setSourceIp(Utils.localIp());
        req.setAppId(appId);
        req.setAuthCode(authCode);
        req.setMethod(method);
        req.setNonceStr(nonceStr);
        req.setTimestamp(timestamp);
        req.setSignMethod("MD5");
        req.setSign(sign);

        return req;
    }



    private void mockOpenBizFacadeSuccess(){
        openapiBizFacade = new MockUp<OpenapiBizFacade>() {
            @Mock
            BaseResponse execute(OpenapiBizReq bizReq) {
                BaseResponse result = new BaseResponse();
                result.setSuccess(true);
                result.setMessage("成功");
                result.setResultCode(ResultCode.SUCCESS.getCode());
                return result;
            }
        }.getMockInstance();

        setField(openapiFacade,openapiBizFacade);
    }

}
