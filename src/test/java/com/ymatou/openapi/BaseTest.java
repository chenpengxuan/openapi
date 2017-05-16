/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.openapi;

import com.ymatou.openapi.starter.Application;
import com.ymatou.openapi.starter.DubboConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author luoshiqian 2017/3/27 16:34
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BaseTest.class)
@Configuration
@ComponentScan(basePackages = "com.ymatou",excludeFilters={
        @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE,value=Application.class),
        @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE,value=DubboConfig.class),
        @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE,value=BaseTestWithDubbo.class)
}
)
public class BaseTest {

    @Test
    public void startup()throws Exception{
        System.in.read();
    }
}
