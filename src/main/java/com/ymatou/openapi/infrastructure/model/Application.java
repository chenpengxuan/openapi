/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.openapi.infrastructure.model;

import javax.persistence.*;

/**
 * @author luoshiqian 2017/5/10 15:10
 */
@Entity
@Table(name = "application")
public class Application extends Audit {

    @Id
    @Column(name = "app_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String appId;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "type")
    private String type;

    @Column(name = "app_secret")
    private String appSecret;

    @Column(name = "seller_id")
    private Long sellerId;

    @Transient
    private String appSecretDecrypted;


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getAppSecretDecrypted() {
        return appSecretDecrypted;
    }

    public void setAppSecretDecrypted(String appSecretDecrypted) {
        this.appSecretDecrypted = appSecretDecrypted;
    }
}
