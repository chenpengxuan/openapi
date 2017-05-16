/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.openapi.service;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Maps;
import com.ymatou.openapi.infrastructure.model.Application;
import com.ymatou.openapi.infrastructure.model.AuthCode;
import com.ymatou.openapi.util.AesUtil;

/**
 * @author luoshiqian 2017/5/15 10:32
 */
@Component
public class CacheService {

    public static final Logger logger = LoggerFactory.getLogger(CacheService.class);

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private AuthCodeService authCodeService;
    private Timer timer = new Timer(true);
    /**
     * key: appId
     */
    private static Map<String, Application> applicationCache = Maps.newConcurrentMap();

    /**
     * key: appId_authCode
     */
    private static Map<String, AuthCode> authCodeCache = Maps.newConcurrentMap();


    @PostConstruct
    public void init() {
        reload();
        timer.schedule(new ReloadTask(), 60 * 1000, 60 * 1000);
    }

    private void reload() {

        List<Application> applicationList = applicationService.findAll();
        if (!CollectionUtils.isEmpty(applicationList)) {
            applicationCache = applicationList.stream()
                    .collect(Collectors.toMap(Application::getAppId, application -> {
                        try {
                            application.setAppSecretDecrypted(AesUtil.decrypt(application.getAppSecret()));
                        } catch (Exception e) {
                            logger.error("AppSecretDecrypted error,appSecret:{}", application.getAppSecret());
                        }
                        return application;
                    }));
        }

        List<AuthCode> authCodeList = authCodeService.findAll();
        if (!CollectionUtils.isEmpty(authCodeList)) {
            authCodeCache = authCodeList.stream().collect(Collectors.toMap(
                    authCode -> generateAuthCodeCacheKey(authCode.getAppId(), authCode.getAuthCode()),
                    ac -> ac));
        }
    }


    /**
     * 获取应用
     * 
     * @param appId
     * @return
     */
    public Optional<Application> findByAppId(String appId) {
        return Optional.ofNullable(applicationCache.get(appId));
    }

    /**
     * 获取授权信息
     * 
     * @param appId
     * @param authCode
     * @return
     */
    public Optional<AuthCode> findByAppIdAndAuthCode(String appId, String authCode) {
        return Optional.ofNullable(authCodeCache.get(generateAuthCodeCacheKey(appId, authCode)));
    }


    public String generateAuthCodeCacheKey(String appId, String authCode) {
        return StringUtils.join(new String[] {appId, authCode}, "_");
    }


    private class ReloadTask extends TimerTask {
        @Override
        public void run() {
            try {
                reload();
            } catch (Exception e) {
                logger.error("reload cache error", e);
            }
        }
    }

}
