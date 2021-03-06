/*
 * Copyright (c) 2014-2015, Yunnex and/or its affiliates. All rights reserved. Use, Copy is subject to authorized license.
 */
package com.xn.interfacetest.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.xn.interfacetest.Enum.ParamFormatTypeEnum;
import com.xn.interfacetest.api.TestJarMethodService;
import com.xn.interfacetest.api.TestParamsService;
import com.xn.interfacetest.dto.*;
import com.xn.interfacetest.entity.TestJarMethod;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xn.common.utils.BeanUtils;
import com.xn.common.utils.PageInfo;
import com.xn.common.utils.PageResult;
import com.xn.interfacetest.api.TestInterfaceService;
import com.xn.interfacetest.api.TestServiceService;
import com.xn.interfacetest.dao.TestInterfaceMapper;
import com.xn.interfacetest.dao.TestParamsMapper;
import com.xn.interfacetest.entity.TestInterface;
import com.xn.interfacetest.entity.TestParams;
import com.xn.interfacetest.util.CollectionUtils;

/**
 * TestInterface Service实现
 * 
 * @author Carol
 * @date 2017-02-14
 */
@Service
@Transactional
public class TestInterfaceServiceImpl implements TestInterfaceService {

    /**
     *  Dao
     */

    @Autowired
    private TestInterfaceMapper testInterfaceMapper;

    @Autowired
    private TestServiceService testServiceService;

    @Autowired
    private TestJarMethodService testJarMethodService;

    @Autowired
    private TestParamsMapper testParamsMapper;

    @Autowired
    private TestParamsService testParamsService;

    @Override
    @Transactional(readOnly = true)
    public TestInterfaceDto get(Object condition)
	{  
        TestInterface testInterface = testInterfaceMapper.get(condition);
        TestInterfaceDto testInterfaceDto = BeanUtils.toBean(testInterface,TestInterfaceDto.class);
	    return testInterfaceDto;  
	}  

    @Override
    @Transactional(readOnly = true)
    public long count(TestInterfaceDto condition) {
        return testInterfaceMapper.count(condition);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestInterfaceDto> list(TestInterfaceDto condition) {
        List<TestInterface> list = testInterfaceMapper.list(condition);
        List<TestInterfaceDto> dtoList = CollectionUtils.transform(list, TestInterfaceDto.class);
        return dtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestInterfaceDto> list(Map<String,Object> condition) {
        List<TestInterface> list = testInterfaceMapper.list(condition);
        List<TestInterfaceDto> dtoList = CollectionUtils.transform(list, TestInterfaceDto.class);
        return dtoList;
    }
    
    @Override
    @Transactional(readOnly = true)
    public PageResult<TestInterfaceDto> page(Map<String,Object> condition){
        return PageResult.wrap((PageInfo) condition.get("page"), list(condition));
    }

    @Override
    public TestInterfaceDto save(TestInterfaceDto testInterfaceDto) {
        TestInterface testInterface = BeanUtils.toBean(testInterfaceDto,TestInterface.class);
        //保存接口字段
        if(null != testInterfaceDto.getId()){
            //保存参数字段到参数表
            updateParams(testInterface);

            testInterfaceMapper.update(testInterface);

            //保存加密方法到方法表
            saveMethodPropertis(testInterface);
        }else{
            testInterfaceMapper.save(testInterface);
            testInterfaceDto.setId(testInterface.getId());
            //保存参数字段到参数表
            addParams(testInterface);
            //保存加密方法到方法表
            saveMethodPropertis(testInterface);
        }
        return testInterfaceDto;
    }

    private void updateParams(TestInterface testInterface) {
        TestInterface testInterfaceExist = testInterfaceMapper.get(testInterface.getId());
        if(null != testInterfaceExist){
            String existParams = testInterfaceExist.getParams();
            String newParams = testInterface.getParams();
            if(StringUtils.isNotBlank(newParams) && !newParams.equals(existParams)) {
                String[] existArray = existParams.split(",|，");
                //把字符串数组转为集合
                List<String> existlist = new ArrayList<String>();
                if (null != existArray && existArray.length > 0) {
                    existlist = Arrays.asList(existArray);
                }

                String[] newArray = newParams.split(",|，");
                List<String> newlist = new ArrayList<String>();
                if (null != newArray && newArray.length > 0) {
                    newlist = Arrays.asList(newArray);
                }

                //比较元素是否存在，存在的不变化，不存在的增加，
                for (String existParam : existlist) {
                    //新参数列表中不包含的字段-删除
                    if (!newlist.contains(existParam)) {
                        testParamsMapper.deleteByInterfaceIdAndParamName(testInterface.getId(), existParam);
                    }
                }

                for (String newParam : newlist) {
                    //旧参数列表中不包含的字段-新增
                    if (!existlist.contains(newParam)) {
                        TestParams paramsExist = testParamsMapper.getParamsByInterfaceIdAndName(testInterface.getId(), newParam, 0);
                        //同一接口下不能存在同名的参数，不存在再保存
                        if (null == paramsExist) {
                            TestParamsDto testParams = new TestParamsDto();
                            testParams.setInterfaceId(testInterface.getId());
                            testParams.setName(newParam);

                            //特殊格式参数--加密
                            if (newParam.endsWith("(-e)")) {
                                testParams.setFormatType(ParamFormatTypeEnum.ENCRYPT.getId());
                                testParams.setName(newParam.substring(0, newParam.indexOf("(-e)")));
                            } else {
                                //普通参数
                                testParams.setFormatType(ParamFormatTypeEnum.NORMAL.getId());
                            }
                            testParamsService.save(testParams);
                        }
                    }

                }
            }
        }
    }

    /**
     * 保存加密方法和参数列表
     * @param testInterface
     */
    private void saveMethodPropertis(TestInterface testInterface) {
        TestInterface testInterfaceExist = testInterfaceMapper.get(testInterface.getId());
        if(null != testInterfaceExist){
            if(StringUtils.isBlank(testInterfaceExist.getClassName()) || StringUtils.isBlank(testInterfaceExist.getMethodName())){
                return;
            }

            String className = testInterfaceExist.getClassName();
            //如：encrypt,getdata
            String methodName = testInterfaceExist.getMethodName();
            //如：name,age;name,height;
            String paramsTypes = testInterfaceExist.getParamsTypes();
            //如：zhangsan,18;huhu,183
            String paramsValues = testInterfaceExist.getParamsValues();

            //取方法名
            String[] methodNameArray = methodName.split(",|，");
            String[] paramsTypesArray = new String[methodNameArray.length];
            if(StringUtils.isNotBlank(paramsTypes)){
                paramsTypesArray = paramsTypes.split(";|；");
            }
            String[] paramsValuesArray = new String[methodNameArray.length];
            if(StringUtils.isNotBlank(paramsValues)){
                paramsValuesArray = paramsValues.split(";|；");
            }
            //保存每一个方法名和对应的参数列表
            for(int i=0;i<methodNameArray.length;i++){
                //查询方法名是否存在
                TestJarMethodDto testJarMethodDto = testJarMethodService.getByMethodNameAndInterfaceId(methodNameArray[i],testInterface.getId());
                if(testJarMethodDto == null){
                    testJarMethodDto = new TestJarMethodDto();
                }
                //保存方法的属性
                testJarMethodDto.setClassName(className);
                testJarMethodDto.setMethodName(methodNameArray[i]);
                testJarMethodDto.setInterfaceId(testInterface.getId());

                //可能有的方法没有参数，所以方法的个数不代表参数列表的个数，3个方法也许只有2个方法有参数
                if(i<paramsTypesArray.length){
                    testJarMethodDto.setParamsTypes(paramsTypesArray[i]);
                }
                if(i<paramsValuesArray.length){
                    testJarMethodDto.setParamsValues(paramsValuesArray[i]);
                }
                //保存
                testJarMethodService.save(testJarMethodDto);
            }
        }
    }

    /**
     * 保存参数
     * @param testInterface
     */
    private void addParams(TestInterface testInterface){
        String newParams = testInterface.getParams();
        if(StringUtils.isNotBlank(newParams)) {
            String[] newArray = newParams.split(",|，");
            List<String> newlist = new ArrayList<String>();
            if (null != newArray && newArray.length > 0) {
                newlist = Arrays.asList(newArray);
            }
            for (String newParam : newlist) {
                TestParamsDto testParams = new TestParamsDto();
                testParams.setInterfaceId(testInterface.getId());
                testParams.setName(newParam);

                //特殊格式参数--加密
                if (newParam.endsWith("(-e)")) {
                    testParams.setFormatType(ParamFormatTypeEnum.ENCRYPT.getId());
                    testParams.setName(newParam.substring(0, newParam.indexOf("(-e)")));
                } else {
                    //普通参数
                    testParams.setFormatType(ParamFormatTypeEnum.NORMAL.getId());
                }
                testParamsService.save(testParams);
            }
        }
    }

    @Override
    public int save(List<TestInterfaceDto> testInterfaceDtos) {
        if (testInterfaceDtos == null || testInterfaceDtos.isEmpty()) {
            return 0;
        }
        List<TestInterface> testInterfaces = CollectionUtils.transform(testInterfaceDtos, TestInterface.class);
        return testInterfaceMapper.saveBatch(testInterfaces);
    }

    @Override
    public int update(TestInterfaceDto testInterfaceDto) {
        TestInterface testInterface = BeanUtils.toBean(testInterfaceDto,TestInterface.class);
        return testInterfaceMapper.update(testInterface);
    }
    
    @Override
    public int deleteByPK(Long id) {
        return testInterfaceMapper.deleteByPK(id);
    }

    @Override
    public int delete(TestInterfaceDto testInterfaceDto) {
        TestInterface testInterface = BeanUtils.toBean(testInterfaceDto,TestInterface.class);
        return testInterfaceMapper.delete(testInterface);
    }
    
    @Override
    public int deleteBatchByPK(List<Long> ids) {
        return testInterfaceMapper.deleteBatchByPK(ids);
    }
    
    @Override
    public int deleteBatch(List<TestInterfaceDto> testInterfaces) {
        return 0;
    }

    @Override
    public PageResult<TestInterfaceDto> listByParams(Map<String, Object> params) {
        List<TestInterface> list = testInterfaceMapper.list(params);
        List<TestInterfaceDto> dtoList = CollectionUtils.transform(list, TestInterfaceDto.class);
        for(TestInterfaceDto testInterfaceDto: dtoList){
            TestServiceDto serviceDto = testServiceService.get(testInterfaceDto.getServiceId());
            testInterfaceDto.setTestServiceDto(serviceDto);
        }
        return  PageResult.wrap((PageInfo) params.get("page"),dtoList);
    }

    @Override
    public List<TestInterfaceDto> listAllBySuitId(Long suitId) {
        List<TestInterface> list = testInterfaceMapper.listAllBySuitId(suitId);
        List<TestInterfaceDto> dtoList = CollectionUtils.transform(list, TestInterfaceDto.class);
        return dtoList;
    }

    @Override
    public List<TestInterfaceDto> listAll() {
        List<TestInterface> list = testInterfaceMapper.list(null);
        List<TestInterfaceDto> dtoList = CollectionUtils.transform(list, TestInterfaceDto.class);
        for(TestInterfaceDto testInterfaceDto: dtoList){
            TestServiceDto serviceDto = testServiceService.getWithSystem(testInterfaceDto.getServiceId());
            testInterfaceDto.setTestServiceDto(serviceDto);
        }
        return dtoList;
    }

    @Override
    public String getParamsByInterfaceId(String interfaceId) {
        return testInterfaceMapper.getParamsByInterfaceId(interfaceId);
    }

    @Override
    public TestInterfaceDto getByCaseId(Long caseId) {
        TestInterface testInterface = testInterfaceMapper.getByCaseId(caseId);
        TestInterfaceDto testInterfaceDto = BeanUtils.toBean(testInterface,TestInterfaceDto.class);
        return testInterfaceDto;
    }

    @Override
    public List<TestInterfaceDto> listWithInfoByIds(String[] interfaceArray) {
        List<TestInterface> list = testInterfaceMapper.listWithInfoByIds(interfaceArray);
        List<TestInterfaceDto> dtoList = CollectionUtils.transform(list, TestInterfaceDto.class);
        for(TestInterfaceDto testInterfaceDto: dtoList){
            TestServiceDto serviceDto = testServiceService.getWithSystem(testInterfaceDto.getServiceId());
            testInterfaceDto.setTestServiceDto(serviceDto);
        }
        return dtoList;
    }

    @Override
    public List<TestInterfaceDto> getByInterfaceIds(String interfaceIds) {
        String[] interfaceArray = interfaceIds.split(",|，");
        List<TestInterface> list = testInterfaceMapper.listWithInfoByIds(interfaceArray);
        List<TestInterfaceDto> dtoList = CollectionUtils.transform(list, TestInterfaceDto.class);
        return dtoList;
    }

    @Override
    public void changeStatusList(int status, List<TestInterfaceDto> interfaceIdList) {
        testInterfaceMapper.changeStatusList(status, interfaceIdList);
    }

    @Override
    public List<TestInterfaceDto> listAllBySuitList(List<TestSuitDto> testSuitDtoList) {
        List<TestInterface> list = testInterfaceMapper.listAllBySuitList(testSuitDtoList);
        List<TestInterfaceDto> dtoList = CollectionUtils.transform(list, TestInterfaceDto.class);
        return dtoList;
    }

    @Override
    public TestInterfaceDto getByServiceIdAndInterfaceName(Long serviceId, String name) {
        TestInterface testInterface = testInterfaceMapper.getByServiceIdAndInterfaceName(serviceId,name);
        TestInterfaceDto testInterfaceDto = BeanUtils.toBean(testInterface,TestInterfaceDto.class);
        return testInterfaceDto;
    }
}
