/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.openapi.config;

import com.baidu.disconf.client.DisconfMgrBean;
import com.ymatou.openapi.constants.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author luoshiqian 2016/8/30 15:49
 */
@Configuration
public class DisconfMgr {

    @Bean(name = "disconfMgrBean", destroyMethod = "destroy")
    public DisconfMgrBean disconfMgrBean(TomcatConfig tomcatConfig) {

        DisconfMgrBean disconfMgrBean = new DisconfMgrBean();
        disconfMgrBean.setScanPackage("com.ymatou.openapi");

        disconfMgrBean.setDisconfInitCallback(() -> {
            Constants.TOMCAT_CONFIG = tomcatConfig;
        });
        return disconfMgrBean;
    }

}
