/*
 * Copyright (c) 2014-2015, Yunnex and/or its affiliates. All rights reserved. Use, Copy is subject to authorized license.
 */
package com.xn.interfacetest.dao;


import com.xn.common.base.BaseMapper;
import com.xn.interfacetest.entity.RelationCaseRedis;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * RelationCaseRedis Dao 接口
 * 
 * @author Carol
 * @date 2017-03-02
 */
public interface RelationCaseRedisMapper extends BaseMapper<RelationCaseRedis, Long> {

    List<RelationCaseRedis> getByCaseIdAndOperateType(@Param("caseId") Long caseId,@Param("operateType")  int operateType);
}
