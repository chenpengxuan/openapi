/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */
package com.ymatou.openapi.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ymatou.openapi.infrastructure.model.AuthCode;

@Repository
public interface AuthCodeRepository extends JpaRepository<AuthCode, Long> {


}
