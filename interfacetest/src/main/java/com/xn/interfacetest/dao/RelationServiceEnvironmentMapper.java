/*
 * Copyright (c) 2014-2015, Yunnex and/or its affiliates. All rights reserved. Use, Copy is subject to authorized license.
 */
package com.xn.interfacetest.dao;

import com.xn.common.base.BaseMapper;
import com.xn.interfacetest.entity.RelationServiceEnvironment;
import org.apache.ibatis.annotations.Param;

/**
 * RelationServiceEnvironment Dao 接口
 * 
 * @author Carol
 * @date 2017-02-14
 */
public interface RelationServiceEnvironmentMapper extends BaseMapper<RelationServiceEnvironment, Long> {

    RelationServiceEnvironment getByCaseAndEnvironment(@Param("serviceId") Long serviceId,@Param("environmentId") Long environmentId);
}
