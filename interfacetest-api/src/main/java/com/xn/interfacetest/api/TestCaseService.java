/*
 * Copyright (c) 2014-2015, Yunnex and/or its affiliates. All rights reserved. Use, Copy is subject to authorized license.
 */
package com.xn.interfacetest.api;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import com.xn.common.base.CommonResult;
import com.xn.common.utils.PageResult;
import com.xn.interfacetest.dto.TestCaseDto;
import com.xn.interfacetest.dto.TestEnvironmentDto;
import com.xn.interfacetest.dto.TestReportDto;
import com.xn.interfacetest.dto.TestSuitDto;
import org.springframework.web.multipart.MultipartFile;


/**
 * TestCase Service
 * 
 * @author Carol
 * @date 2017-02-14
 */
public interface TestCaseService {

    /**
     * 查询单个记录
     * 主键：id 
     * @param condition 主键/Map/查询对象
     * @return 
     */
    TestCaseDto get(Object condition);

    /**
     * 统计数量
     * 
     * @param condition 查询条件对象
     * @return 统计数量
     */
    long count(TestCaseDto condition);

    /**
     * 根据组合条件查询
     * 
     * @param condition 查询对象
     * @return 集合,如果不存在,返回Empty List
     */
    List<TestCaseDto> list(TestCaseDto condition);

    /**
     * 根据组合条件查询,不建议用该方法进行分页  
     * 
     * @param condition 查询对象
     * @return 集合,如果不存在,返回Empty List
     */
    List<TestCaseDto> list(Map<String, Object> condition);
    
    /**
     * 根据组合条件做分页查询,需要condition中包含分页对象page  
     * 
     * @param condition 查询对象
     * @return 集合,如果不存在,返回Empty List
     */
    PageResult<TestCaseDto> page(Map<String, Object> condition);
    

    /**
     * 保存
     * 
     * @param testCase 
     * @return 带主键的DTO
     */
    TestCaseDto save(TestCaseDto testCaseDto);

    /**
     * 批量保存
     * 
     * @param testCases 
     * @return 带主键的DTO
     */
    int save(List<TestCaseDto> testCaseDtos);

    /**
     * 更新
     * 
     * @param testCase 
     * @return 操作影响行数
     */
    int update(TestCaseDto testCaseDto);
    
    /**
     * 根据主键删除
     * 不建议，建议使用delete(TestCase testCase)
     * @param id 主键
     * @return 操作影响行数
     */
    int deleteByPK(Long id);
    
    /**
     * 删除
     * 
     * @param id 主键
     * @return 操作影响行数
     */
    int delete(TestCaseDto testCaseDto);
    
    /**
     * 根据主键删除
     * 不建议，建议使用delete(TestCaseDto testCase)
     * @param id 主键
     * @return 操作影响行数
     */
    public int deleteBatchByPK(List<Long> ids);
    
    
    /**
     * 批量删除
     * 
     * @param id 主键
     * @return 操作影响行数
     */
    int deleteBatch(List<TestCaseDto> testCases);

    /**
     * 更新除了开关以外的字段
     * @param testCaseDto
     */
    int updatePart(TestCaseDto testCaseDto);

    /**
     * 根据条件查询
     * @param params
     * @return
     */
    PageResult<TestCaseDto> listByParams(Map<String, Object> params);

    /**
     * 通过测试集查询测试用例
     * @param id
     * @return
     */
    List<TestCaseDto> listBySuitId(Long id);

    /**
     * 批量执行测试用例-指定执行环境
     * @param testCaseDtoList
     * @param testEnvironmentDto
     * @param testReportDto
     */
    void excuteCaseList(List<TestCaseDto> testCaseDtoList, TestEnvironmentDto testEnvironmentDto, Long planId, TestReportDto testReportDto, TestSuitDto suitDto) throws Exception;

    /**
     * 测试用例调试--不保存测试结果
     * @param caseId
     * @param environmentId
     */
    void testRun(Long caseId, Long environmentId) throws Exception;

    /**
     * 根据caseId组合查询case
     * @param caseIds
     * @return
     */
    List<TestCaseDto> getByCaseIds(String caseIds);

    void changeStatusList(int status, List<TestCaseDto> testCaseDtoList);

    List<TestCaseDto> listAllBySuitList(List<TestSuitDto> testSuitDtoList);

    /**
     * 复制用例
     * @param params
     */
    void copyCase(Map<String, Object> params);

    /**
     * 根据用例编号查询用例
     * @param number
     * @return
     */
    List<TestCaseDto> getByCaseNum(String number);

    /**
     * 查询用例信息，以接口排序
     * @param suitId
     * @param isDelete
     * @return
     */
    List<TestCaseDto> listBySuitIdOrderByInterfaceId(Long suitId);

    /**
     * 查询用例信息，以接口排序
     * @param suitId
     * @param isDelete
     * @return
     */
    List<TestCaseDto> listAllOrderByInterface();

    StringBuffer dealWithExcelFile(String path) throws Exception;
}
