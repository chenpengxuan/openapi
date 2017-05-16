/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.openapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ymatou.openapi.infrastructure.model.Application;
import com.ymatou.openapi.infrastructure.repository.jpa.ApplicationRepository;
import com.ymatou.openapi.infrastructure.repository.mapper.ApplicationMapper;
import com.ymatou.openapi.service.ApplicationService;

/**
 * @author luoshiqian 2017/5/10 15:59
 */
@Service
public class ApplicationServiceImpl extends BaseServiceImpl<Application> implements ApplicationService {

    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
        super(applicationRepository);
        this.applicationRepository = applicationRepository;
    }
}
