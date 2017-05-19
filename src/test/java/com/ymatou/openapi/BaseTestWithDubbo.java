/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.openapi;


import com.ymatou.openapi.constants.Constants;
import com.ymatou.openapi.starter.Application;
import com.ymatou.openapi.starter.DubboConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author luoshiqian 2017/4/20 18:59
 */

//@ImportResource("classpath:spring/dubbo-provider.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BaseTest.class)
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = "com.ymatou.openapi",excludeFilters={
        @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE,value=Application.class),
//        @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE,value=DubboConfig.class),
        @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE,value=BaseTest.class)
    }
)
public class BaseTestWithDubbo {

    @Test
    public void startup()throws Exception{
//        Constants.ctx = new AnnotationConfigApplicationContext(BaseTestWithDubbo.class);
//
//        Constants.ctx.start();

        System.in.read();
    }
}
