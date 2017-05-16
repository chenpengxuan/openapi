/*
 *
 * (C) Copyright 2017 Ymatou (http://www.ymatou.com/). All rights reserved.
 *
 */

package com.ymatou.openapi.service.impl;

import com.ymatou.openapi.infrastructure.model.AuthCode;
import com.ymatou.openapi.infrastructure.repository.jpa.AuthCodeRepository;
import com.ymatou.openapi.service.AuthCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author luoshiqian 2017/5/15 10:34
 */
@Service
public class AuthCodeServiceImpl extends BaseServiceImpl<AuthCode> implements AuthCodeService {

    private AuthCodeRepository authCodeRepository;


    @Autowired
    public AuthCodeServiceImpl(AuthCodeRepository authCodeRepository) {
        super(authCodeRepository);
        this.authCodeRepository = authCodeRepository;
    }

}
