/*
 * Copyright (c) 2014-2015, Yunnex and/or its affiliates. All rights reserved. Use, Copy is subject to authorized license.
 */
package com.xn.interfacetest.dao;

import com.xn.common.base.BaseMapper;
import com.xn.interfacetest.entity.TestJarMethod;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TestJarMethod Dao 接口
 * 
 * @author Carol
 * @date 2017-05-05
 */
public interface TestJarMethodMapper extends BaseMapper<TestJarMethod, Long> {

    TestJarMethod getByMethodNameAndInterfaceId(@Param("methodName") String methodName,@Param("interfaceId") Long interfaceId );

    List<TestJarMethod> getByInterfaceId(Long interfaceId);
}
