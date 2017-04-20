/*
 * Copyright (c) 2014-2015, Yunnex and/or its affiliates. All rights reserved. Use, Copy is subject to authorized license.
 */
package com.xn.interfacetest.dao;

import java.util.List;
import java.util.Map;

import com.xn.common.base.BaseMapper;
import com.xn.interfacetest.entity.TestCase;
import org.springframework.stereotype.Service;


/**
 * TestCase Dao 接口
 * 
 * @author Carol
 * @date 2017-02-14
 */
@Service
public interface TestCaseMapper extends BaseMapper<TestCase, Long> {

    int updatePart(TestCase testCase);

    List<TestCase> listByParams(Map<String, Object> params);

    List<TestCase> listBySuitId(Long suitId);

    List<TestCase> getByCaseIds(String[] ids);
}
