/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.openapi.facade;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.collect.Maps;
import com.ymatou.openapi.biz.facade.OpenapiBizFacade;
import com.ymatou.openapi.biz.facade.req.OpenapiBizReq;
import com.ymatou.openapi.biz.facade.resp.BaseResponse;
import com.ymatou.openapi.infrastructure.model.Application;
import com.ymatou.openapi.infrastructure.model.AuthCode;
import com.ymatou.openapi.model.OpenApiResult;
import com.ymatou.openapi.model.OpenapiReq;
import com.ymatou.openapi.model.ReturnCode;
import com.ymatou.openapi.service.CacheService;
import com.ymatou.openapi.util.AesUtil;
import com.ymatou.performancemonitorclient.PerformanceStatisticContainer;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.ymatou.openapi.constants.Constants.FORMATTER_YYYYMMDDHHMMSS;
import static com.ymatou.openapi.model.OpenApiResult.newFailInstance;
import static com.ymatou.openapi.model.OpenApiResult.newInstance;

/**
 * @author luoshiqian 2017/5/10 17:32
 */
@Component
public class OpenapiFacade {

    public static final Logger logger = LoggerFactory.getLogger(OpenapiFacade.class);

    @Autowired
    private CacheService cacheService;
    @Reference(retries = 1,check = false)
    private OpenapiBizFacade openapiBizFacade;


    public OpenApiResult gateway(OpenapiReq openapiReq) {

        // 验证参数必填

        // 验证appid存在
        if (StringUtils.isBlank(openapiReq.getAppId())) {
            return newFailInstance(ReturnCode.INVALID_PARAM, "app_id 必填!");
        }
        if (StringUtils.isBlank(openapiReq.getMethod())) {
            return newFailInstance(ReturnCode.INVALID_PARAM, "method 必填!");
        }
        // 验证signmethod md5
        if (StringUtils.isBlank(openapiReq.getSignMethod()) && !"MD5".equals(openapiReq.getSignMethod())) {
            return newFailInstance(ReturnCode.INVALID_PARAM, "sign_method 必填传 MD5 ");
        }
        if (StringUtils.isBlank(openapiReq.getAuthCode())) {
            return newFailInstance(ReturnCode.INVALID_PARAM, "auth_code 必填!");
        }
        if (StringUtils.isBlank(openapiReq.getTimestamp())) {
            return newFailInstance(ReturnCode.INVALID_PARAM, "timestamp 必填!");
        }
        if (StringUtils.isBlank(openapiReq.getNonceStr())) {
            return newFailInstance(ReturnCode.INVALID_PARAM, "nonce_str 必填!");
        }
        if (StringUtils.isBlank(openapiReq.getSign())) {
            return newFailInstance(ReturnCode.INVALID_PARAM, "sign 必填!");
        }
        // 验证timestamp 10分钟之内
        DateTime requestTime;
        try {
            requestTime = DateTime.parse(openapiReq.getTimestamp(), FORMATTER_YYYYMMDDHHMMSS);
        } catch (Exception e) {
            logger.warn("requestTime format error:{}", openapiReq);
            return newFailInstance(ReturnCode.INVALID_PARAM, "timestamp 格式不正确!");
        }

        int diffMinutes = Minutes.minutesBetween(requestTime, DateTime.now()).getMinutes();
        if (diffMinutes > 10 || diffMinutes < -10) {
            return newFailInstance(ReturnCode.INVALID_REQ_TIME);
        }

        // 验证nonceStr 32位之类
        if (openapiReq.getNonceStr().length() > 32) {
            return newFailInstance(ReturnCode.INVALID_PARAM, "非法nonceStr!");
        }

        Optional<Application> applicationOptional = cacheService.findByAppId(openapiReq.getAppId());
        if (!applicationOptional.isPresent()) {
            return newFailInstance(ReturnCode.INVALID_API_ID);
        }

        // 验证authCode 查询到相应sellerId
        Optional<AuthCode> authCodeOptional =
                cacheService.findByAppIdAndAuthCode(openapiReq.getAppId(), openapiReq.getAuthCode());
        if (!authCodeOptional.isPresent()) {
            return newFailInstance(ReturnCode.INVALID_AUTH_CODE);
        }

        Application application = applicationOptional.get();
        AuthCode authCode = authCodeOptional.get();

        // 验证authCode没有失效
        if(authCode.getExpireTime().before(new Date())){
            return newFailInstance(ReturnCode.INVALID_AUTH_CODE,"授权码过期!");
        }

        // 验证sign
        if(verifySign(application,openapiReq)){
            OpenapiBizReq bizReq = new OpenapiBizReq();
            bizReq.setSellerId(authCode.getSellerId());
            bizReq.setMethod(openapiReq.getMethod());
            bizReq.setBizContent(openapiReq.getBizContent());
            bizReq.setAuthCode(openapiReq.getAuthCode());
            bizReq.setAppId(openapiReq.getAppId());
            bizReq.setRequestId(openapiReq.getRequestId());
            bizReq.setSourceIp(openapiReq.getSourceIp());

            BaseResponse bizResp = PerformanceStatisticContainer.addWithReturn(() -> openapiBizFacade.execute(bizReq),
                    bizReq.getMethod());

            return newInstance(bizResp);
        } else {
            return newFailInstance(ReturnCode.SIGN_VERIFY_FAIL);
        }
    }


    /**
     * 验签
     * 
     * @return
     */
    private boolean verifySign(Application application, OpenapiReq openapiReq) {

        Map<String, String> map = Maps.newTreeMap();
        map.put("app_id", openapiReq.getAppId());
        map.put("method", openapiReq.getMethod());
        map.put("sign_method", openapiReq.getSignMethod());
        map.put("auth_code", openapiReq.getAuthCode());
        map.put("timestamp", openapiReq.getTimestamp());
        map.put("nonce_str", openapiReq.getNonceStr());
        map.put("biz_content", openapiReq.getBizContent());


        String serverSign = generateMD5(map, application.getAppSecretDecrypted());
        if (openapiReq.getSign().equals(serverSign)) {
            return true;
        } else {
            logger.warn("signVerify fail, req:{},serverSign:{}", openapiReq, serverSign);
            return false;
        }
    }


    public static String generateMD5(Map map, String appSecret) {
        StringBuffer sb = new StringBuffer();
        Set es = map.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"app_secret".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("app_secret=" + appSecret);

        return AesUtil.getMd5(sb.toString()).toUpperCase();
    }



}
