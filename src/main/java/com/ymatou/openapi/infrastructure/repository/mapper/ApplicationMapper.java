/*
 *
 *  (C) Copyright 2017 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.openapi.infrastructure.repository.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ymatou.common.mybatis.annotation.MyBatisDao;
import com.ymatou.openapi.infrastructure.model.Application;


@MyBatisDao
public interface ApplicationMapper {

    List<Application> findByUser(@Param("app") Application user);

    Page<Application> findByUser(@Param("app") Application user, @Param("pageable") Pageable pageable);

}
