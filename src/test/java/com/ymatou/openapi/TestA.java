/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.openapi;import com.google.common.collect.Maps;
import com.ymatou.openapi.util.AesUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

/**
 * @author luoshiqian 2017/5/9 10:57
 */
public class TestA {

    @Test
    public void testRandom()throws Exception {
        System.out.println(RandomStringUtils.randomAlphabetic(18));
        System.out.println(RandomStringUtils.randomAlphanumeric(18));
        System.out.println(RandomStringUtils.randomAlphanumeric(32));
        System.out.println(RandomStringUtils.randomAlphanumeric(32));

        String appSecret = RandomStringUtils.randomAlphanumeric(32);
        System.out.println(appSecret);
        String oneEn = AesUtil.encrypt(appSecret);
        System.out.println(oneEn);


        System.out.println(AesUtil.decrypt("4D02F7B3575D0AE22C4F4BF8471654EA4D58BA8C08452690F2AD0096B197B4270F3EB40B478BCAAB55D708A06EB47106"));
    }

    @Test
    public void test() {

        while (true) {
            long start = System.currentTimeMillis();
            SortedMap<String, String> map = Maps.newTreeMap();

            map.put("appid", RandomStringUtils.randomAlphanumeric(18));
            map.put("mch_id", RandomStringUtils.randomNumeric(18));
            map.put("device_info", "1000");
            map.put("body", "test");
            map.put("nonce_str", "ibuaiVcKdpRxkhJA");

            String key = "192006250b4c09247ec02edce69f6a2d";

            StringBuffer sb = new StringBuffer();
            Set es = map.entrySet();
            Iterator it = es.iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String k = (String) entry.getKey();
                String v = (String) entry.getValue();
                if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                    sb.append(k + "=" + v + "&");
                }
            }
            sb.append("key=" + key);

            System.out.println(AesUtil.getMd5(sb.toString()).toUpperCase());
            long end = System.currentTimeMillis();

            System.out.println(end - start);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }


    @Test
    public void testExample() {

        String appId = "zWYVVFagTfenOHDPTm";
        String appSecret = "cvxEvN7q2ixmN6Y8DFRJmuP79H2zxctK";
        String nonceStr = "3g3jJVfI9CWwKMr45x9SkB0gbi9kAn28";
        String authCode = "UkeV6CUfk8OKKv1UkjEmfBDU75ZjunA0";

        SortedMap<String, String> map = Maps.newTreeMap();

        map.put("app_id", appId);
        map.put("method", "ymatou.skus.stock.update");
        map.put("sign_method", "MD5");
        map.put("timestamp", "2017-01-01 12:00:00");
        map.put("sign", "2017-01-01 12:00:00");
        map.put("nonce_str", nonceStr);
        map.put("auth_code", authCode);
//        map.put("biz_content", "{\"skuStocks\":[{\"outer_sku_id\":\"393992\",\"stock_num\":10},{\"outer_sku_id\":\"393993\",\"stock_num\":12}]}");


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

        System.out.println(sb.toString());
        System.out.println(AesUtil.getMd5(sb.toString()).toUpperCase());


    }

    @Test
    public void test1(){
        String str = "app_id=zWYVVFagTfenOHDPTm&auth_code=UkeV6CUfk8OKKv1UkjEmfBDU75ZjunA0&method=ymatou.skus.stock.update&nonce_str=3g3jJVfI9CWwKMr45x9SkB0gbi9kAn28&sign_method=MD5&timestamp=2017-01-01 12:00:00"
                ;
        String signtem = str +"&app_secret=cvxEvN7q2ixmN6Y8DFRJmuP79H2zxctK";
        System.out.println(signtem);
        System.out.println(AesUtil.getMd5(signtem).toUpperCase());
    }

    @Test
    public void test2()throws Exception{
        int i=0;
        while (i<100){
            String appSecret = RandomStringUtils.randomAlphanumeric(32);
            System.out.println(appSecret);
            String oneEn = AesUtil.encrypt(appSecret);
            System.out.println(oneEn);
            System.out.println(oneEn.length());
            TimeUnit.SECONDS.sleep(3);
            i++;
        }

    }
}
