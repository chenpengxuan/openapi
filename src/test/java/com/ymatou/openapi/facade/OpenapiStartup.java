/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.openapi.facade;

import static mockit.Deencapsulation.setField;

import com.alibaba.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymatou.openapi.BaseTestWithDubbo;
import com.ymatou.openapi.biz.facade.OpenapiBizFacade;
import com.ymatou.openapi.biz.facade.model.ResultCode;
import com.ymatou.openapi.biz.facade.req.OpenapiBizReq;
import com.ymatou.openapi.biz.facade.resp.BaseResponse;

import mockit.Mock;
import mockit.MockUp;

/**
 * @author luoshiqian 2017/5/16 15:02
 */
public class OpenapiStartup extends BaseTestWithDubbo {

    @Autowired
    public OpenapiFacade openapiFacade;
    @Reference
    private OpenapiBizFacade openapiBizFacade;

    @Test
    public void start() throws Exception {
        this.mockOpenBizFacadeSuccess();
        System.in.read();
    }


    private void mockOpenBizFacadeSuccess() {
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

        setField(openapiFacade, openapiBizFacade);
    }

}
