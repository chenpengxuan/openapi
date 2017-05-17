/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.openapi.facade.aspect;


import com.ymatou.openapi.model.BaseRequest;
import com.ymatou.openapi.model.BaseResponse;
import com.ymatou.openapi.model.ReturnCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * Facade AOP.
 * <p>
 * 实现与业务无关的通用操作。
 * <p>
 * 1，日志
 * <p>
 * 2，异常处理等
 *
 * @author tuwenjie
 */
@Aspect
@Component
public class FacadeAspect {

    private static final Logger DEFAULT_LOGGER = LoggerFactory.getLogger(FacadeAspect.class);

    @Pointcut("execution(* com.ymatou.openapi.facade.*Facade.*(*)) && args(req)")
    public void executeFacade(BaseRequest req) {}

    @Around("executeFacade(req)")
    public Object aroundFacadeExecution(ProceedingJoinPoint joinPoint, BaseRequest req)
            throws InstantiationException, IllegalAccessException {
        Logger logger = DEFAULT_LOGGER;

        if (req == null) {
            logger.error("{} Recv: null", joinPoint.getSignature());
            return buildErrorResponse(joinPoint, ReturnCode.ERROR);
        }

        long startTime = System.currentTimeMillis();

        logger.info("Recv:" + req);

        Object resp = null;

        try {
            req.validate();
            resp = joinPoint.proceed(new Object[] {req});
        } catch (IllegalArgumentException e) {
            // 无效参数异常
            resp = buildErrorResponse(joinPoint, ReturnCode.INVALID_PARAM);
            logger.error("Invalid request: {}", req, e);
        } catch (Throwable e) {
            // 未知异常
            resp = buildErrorResponse(joinPoint, ReturnCode.ERROR);
            logger.error("Unknown error in executing request:{}", req, e);
        } finally {
            logger.info("Consumed:{}ms Resp:{}", System.currentTimeMillis()-startTime, resp);
        }

        return resp;
    }


    private BaseResponse buildErrorResponse(ProceedingJoinPoint joinPoint, ReturnCode resultCode)
            throws InstantiationException, IllegalAccessException {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        BaseResponse resp = (BaseResponse) ms.getReturnType().newInstance();
        resp.setCode(resultCode.getCode());
        resp.setMessage(resultCode.getMessage());
        resp.setSuccess(false);
        return resp;
    }

    private String getRequestFlag(BaseRequest req) {
        return req.getClass().getSimpleName() + "|" + req.getRequestId();
    }

}
