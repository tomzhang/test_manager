/*
 * Copyright (c) 2014-2015, Yunnex and/or its affiliates. All rights reserved. Use, Copy is subject to authorized license.
 */
package com.xn.interfacetest.service.impl;

import java.util.*;

import com.google.common.collect.Lists;
import com.xn.interfacetest.Enum.*;
import com.xn.interfacetest.Exception.AssertNotEqualException;
import com.xn.interfacetest.command.*;
import com.xn.interfacetest.dto.*;
import com.xn.interfacetest.entity.*;
import com.xn.interfacetest.model.AssertKeyValueVo;
import com.xn.interfacetest.model.KeyValueStore;
import com.xn.interfacetest.response.Assert;
import com.xn.interfacetest.result.ReportResult;
import com.xn.interfacetest.service.*;
import com.xn.interfacetest.util.DBUtil;
import com.xn.interfacetest.util.RedisUtil;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xn.common.utils.BeanUtils;
import com.xn.common.utils.CollectionUtils;
import com.xn.common.utils.PageInfo;
import com.xn.common.utils.PageResult;
import com.xn.interfacetest.dao.TestCaseMapper;

/**
 * TestCase Service实现
 * 
 * @author Carol
 * @date 2017-02-14
 */
@Service
@Transactional
public class TestCaseServiceImpl implements TestCaseService {
    private static final Logger logger = LoggerFactory.getLogger(TestCaseServiceImpl.class);
    /**
     *  Dao
     */
    @Autowired
    private TestCaseMapper testCaseMapper;

    @Autowired
    private TestInterfaceService testInterfaceService;

    @Autowired
    private TestParamsService testParamsService;

    @Autowired
    private RelationCaseRedisService relationCaseRedisService;

    @Autowired
    private  RelationCaseDatabaseService relationCaseDatabaseService;

    @Autowired
    private  DataAssertService dataAssertService;

    @Autowired
    private  ParamsAssertService paramsAssertService;

    @Autowired
    private TestReportService testReportService;

    @Autowired
    private TestEnvironmentService testEnvironmentService;

    @Autowired
    private RelationServiceEnvironmentService relationServiceEnvironmentService;

    @Override
    @Transactional(readOnly = true)
    public TestCaseDto get(Object condition)
	{  
        TestCase testCase = testCaseMapper.get(condition);
        TestCaseDto testCaseDto = BeanUtils.toBean(testCase,TestCaseDto.class);
	    return testCaseDto;  
	}  

    @Override
    @Transactional(readOnly = true)
    public long count(TestCaseDto condition) {
        return testCaseMapper.count(condition);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestCaseDto> list(TestCaseDto condition) {
        List<TestCase> list = testCaseMapper.list(condition);
        List<TestCaseDto> dtoList = CollectionUtils.transform(list, TestCaseDto.class);
        return dtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestCaseDto> list(Map<String,Object> condition) {
        List<TestCase> list = testCaseMapper.list(condition);
        List<TestCaseDto> dtoList = CollectionUtils.transform(list, TestCaseDto.class);
        return dtoList;
    }
    
    @Override
    @Transactional(readOnly = true)
    public PageResult<TestCaseDto> page(Map<String,Object> condition){
        return PageResult.wrap((PageInfo) condition.get("page"), list(condition));
    }

    @Override
    public TestCaseDto save(TestCaseDto testCaseDto) {
        TestCase testCase = BeanUtils.toBean(testCaseDto,TestCase.class);
        if(null == testCaseDto.getId()){
            testCaseMapper.save(testCase);
        } else {
            testCaseMapper.update(testCase);
        }

        testCaseDto.setId(testCase.getId());
        return testCaseDto;
    }

    @Override
    public int save(List<TestCaseDto> testCaseDtos) {
        if (testCaseDtos == null || testCaseDtos.isEmpty()) {
            return 0;
        }
        List<TestCase> testCases = CollectionUtils.transform(testCaseDtos, TestCase.class);
        return testCaseMapper.saveBatch(testCases);
    }

    @Override
    public int update(TestCaseDto testCaseDto) {
        TestCase testCase = BeanUtils.toBean(testCaseDto,TestCase.class);
        return testCaseMapper.update(testCase);
    }
    
    @Override
    public int deleteByPK(Long id) {
        return testCaseMapper.deleteByPK(id);
    }

    @Override
    public int delete(TestCaseDto testCaseDto) {
        TestCase testCase = BeanUtils.toBean(testCaseDto,TestCase.class);
        return testCaseMapper.delete(testCase);
    }
    
    @Override
    public int deleteBatchByPK(List<Long> ids) {
        return testCaseMapper.deleteBatchByPK(ids);
    }
    
    @Override
    public int deleteBatch(List<TestCaseDto> testCases) {
        return 0;
    }

    @Override
    public int updatePart(TestCaseDto testCaseDto) {
        TestCase testCase = BeanUtils.toBean(testCaseDto,TestCase.class);
        return testCaseMapper.updatePart(testCase);
    }

    @Override
    public List<TestCaseDto> listByParams(Map<String, Object> params) {
        List<TestCase> list = testCaseMapper.listByParams(params);
        List<TestCaseDto> dtoList = CollectionUtils.transform(list, TestCaseDto.class);
        for(TestCaseDto testCaseDto: dtoList ){
            if(null != testCaseDto.getInterfaceId()){
                TestInterfaceDto testInterfaceDto = testInterfaceService.get(testCaseDto.getInterfaceId());
                testCaseDto.setInterfaceDto(testInterfaceDto);
            }
        }
        return dtoList;
    }

    @Override
    public List<TestCaseDto> listBySuitId(Long suitId) {
        List<TestCase> list = testCaseMapper.listBySuitId(suitId);
        List<TestCaseDto> dtoList = CollectionUtils.transform(list, TestCaseDto.class);
        return dtoList;
    }

    @Override
    public void excuteCaseList(List<TestCaseDto> testCaseDtoList, TestEnvironmentDto testEnvironmentDto,Long planId, Long reportId,Long suitId) throws Exception{
        //遍历执行测试用例
        for(TestCaseDto caseDto:testCaseDtoList){
            logger.info("==========遍历执行测试用例========");
            this.excuteCase(caseDto,testEnvironmentDto,planId,reportId,suitId);
        }
    }

    @Override
    public void testRun(Long caseId, Long environmentId) throws Exception{
        //判断是dubbo接口还是http接口
        logger.info("==========联调测试用例========");
        TestInterfaceDto testInterfaceDto = testInterfaceService.getByCaseId(caseId);
        if(null != testInterfaceDto){
            if(InterfaceTypeEnum.HTTP.getId() == testInterfaceDto.getType()){
                testRunHttp(testInterfaceDto,caseId,environmentId);
            } else if(InterfaceTypeEnum.DUBBO.getId() == testInterfaceDto.getType()){
                testRunDubbo(testInterfaceDto,caseId,environmentId);
            }
        }
    }

    /**
     * dubbo接口联调
     * @param testInterfaceDto
     * @param caseId
     * @param environmentId
     */
    private void testRunDubbo(TestInterfaceDto testInterfaceDto, Long caseId, Long environmentId) {
        logger.info("==========联调测试用例DUBBO========");
        TestCaseDto testCaseDto = this.get(caseId);
        TestEnvironmentDto testEnvironmentDto = testEnvironmentService.get(environmentId);
        this.excuteDubbo(testCaseDto,testInterfaceDto,testEnvironmentDto,null,null);
    }

    /**
     * http接口联调
     * @param testInterfaceDto
     * @param caseId
     * @param environmentId
     */
    private void testRunHttp(TestInterfaceDto testInterfaceDto, Long caseId, Long environmentId)throws Exception {
        logger.info("==========联调测试用例HTTP========");
        TestCaseDto testCaseDto = this.get(caseId);
        TestEnvironmentDto testEnvironmentDto = testEnvironmentService.get(environmentId);
        this.excuteHttp(testCaseDto,testInterfaceDto,testEnvironmentDto,null,null,null);
    }

    /**
     * 执行测试用例
     * @param caseDto 用例
     * @param testEnvironmentDto 执行环境
     */
    private void excuteCase(TestCaseDto caseDto, TestEnvironmentDto testEnvironmentDto,Long planId, Long reportId,Long suitId) throws Exception {
        ReportResult.totalPlus();
        TestReportDto testReportDto = testReportService.get(reportId);

        //将执行的用例id保存到报告表
        String existCaseIds =  testReportDto.getCaseIds();
        if(null != testReportDto && StringUtils.isNotBlank(existCaseIds)){
            String[] caseIdsArray = existCaseIds.split(",|，");
            List list = Arrays.asList(caseIdsArray);
            //不存在当前测试集用例id的时候加进去，以免重复
            if(!list.contains(caseDto.getId())){
                String casesInReport = "," + existCaseIds  + caseDto.getId();
                testReportDto.setCaseIds(casesInReport);
                logger.info("==========即将更新的caseIds=" + casesInReport + "========");
            }
        } else if(null != testReportDto && StringUtils.isBlank(existCaseIds)){
            //没有保存过用例则直接保存
            testReportDto.setCaseIds(caseDto.getId() + "");
        }


        //查询用例所属接口的基本配置
        logger.info("==========查询用例id=" + caseDto.getId() + "所属接口信息========");
        TestInterfaceDto interfaceDto = testInterfaceService.getByCaseId(caseDto.getId());
        String existInterfaceIds =  testReportDto.getInterfaceIds();
        if(null != interfaceDto){
            logger.info("==========接口信息{}:" + interfaceDto.toString());
            if(null != testReportDto && StringUtils.isNotBlank(existInterfaceIds)) {
                String[] interfaceIdsArray = existInterfaceIds.split(",|，");
                List list = Arrays.asList(interfaceIdsArray);
                //不存在当前测试集用例id的时候加进去，以免重复
                if(!list.contains(interfaceDto.getId())){
                    String interfacesInReport = "," + existInterfaceIds  + interfaceDto.getId();
                    testReportDto.setInterfaceIds(interfacesInReport);
                    logger.info("==========即将更新的interfaceIds=" + interfacesInReport + "========");
                }
            } else if(null != testReportDto && StringUtils.isBlank(existInterfaceIds)){
                //没有保存过用例则直接保存
                testReportDto.setInterfaceIds(interfaceDto.getId() + "");
            }
            testReportService.update(testReportDto);
            if(InterfaceTypeEnum.DUBBO.getId() == interfaceDto.getType()){
                //dubbo接口
                this.excuteDubbo(caseDto,interfaceDto,testEnvironmentDto,planId,testReportDto);
            } else if(InterfaceTypeEnum.HTTP.getId() == interfaceDto.getType()){
                //http接口
                this.excuteHttp(caseDto,interfaceDto,testEnvironmentDto,planId,testReportDto,suitId);
            }


        }

    }

    /**
     * http接口的用例执行方法
     * @param caseDto
     * @param interfaceDto
     * @param testEnvironmentDto
     */
    private void excuteHttp(TestCaseDto caseDto, TestInterfaceDto interfaceDto, TestEnvironmentDto testEnvironmentDto,Long planId,TestReportDto testReportDto,Long suitId) throws Exception{
        Long caseId = caseDto.getId(); //用例id
        Long interfaceId = interfaceDto.getId();//接口id
        Long environmentId = testEnvironmentDto.getId();//环境id
        Long reportId = null != testReportDto?testReportDto.getId():null;
        logger.info("==========http接口用例执行:用例id｛｝接口id｛｝环境id｛｝========" + caseId + "," + interfaceId + "," + environmentId);
            //初始化数据库
        boolean flag = DBUtil.getDBInit(environmentId,caseId);


        TestCaseCommand testCaseCommand = new TestCaseCommand();
        //进行执行用例之前的初始化工作
        testCaseCommand.setBeforeCommand(getBeforeCommand(testEnvironmentDto,caseDto));
        //数据清除
        testCaseCommand.setAfterCommand(getAfterCommand(testEnvironmentDto,caseDto));
        //断言初始化
        testCaseCommand.setAssertCommand(getAssertCommand(caseDto, interfaceDto,reportId));

        //========================发送http请求初始化===============================
        //取超时时间---默认200000
        String timeout = "200000";
        //请求方式get/post
        String requestType = RequestTypeEnum.getName(interfaceDto.getRequestType());
        //协议类型
        String propType = HttpTypeEnum.getName(interfaceDto.getProtocolType());
        //取http接口的url
        String url = interfaceDto.getUrl();
        //contentType
        String contentType = interfaceDto.getContentType();
        logger.info("contentType:" + contentType);
        //http请求的服务器地址
        RelationServiceEnvironmentDto relationServiceEnvironmentDto = relationServiceEnvironmentService.getByCaseAndEnvironment(interfaceDto.getServiceId(),environmentId);
        if(null != relationServiceEnvironmentDto){
            url = propType + "://" + relationServiceEnvironmentDto.getIpAddress() + ":" + relationServiceEnvironmentDto.getHttpPort() + "/" + url;
        }
        logger.info("url:" + url);
        //请求参数处理9
        String paramsStr  = formatParams(caseDto,contentType);
        logger.info("paramsStr:" + paramsStr);

        testCaseCommand.setCaseCommand(getCaseCommand(requestType,url,paramsStr,contentType,timeout,propType, caseId, interfaceId, planId, reportId, suitId));

        //执行测试用例
        testCaseCommand.execute();

        if (flag) {
            DBUtil.DBClose();
        }
    }

    //初始化用例的执行
    private CaseCommand getCaseCommand(String requestType,String url,String paramsStr,String contentType,String timeout,String propType,
                                       Long caseId,Long interfaceId,Long planId,Long reportId,Long suitId) {
        return new HttpCaseCommand(requestType,url,paramsStr,contentType,timeout,propType, caseId, interfaceId, planId, reportId, suitId);
    }

    //初始化字段校验
    private AssertCommand getAssertCommand(TestCaseDto caseDto, TestInterfaceDto interfaceDto,Long reportId) {
        Assert assertItem = new Assert(interfaceDto.getName(), interfaceDto.getName(), caseDto.getName());
        List<AssertKeyValueVo> paralist = new ArrayList();
       // List<KeyValueStore> redislist = new ArrayList();

        List<Command> dbAssertCommandList = new ArrayList();
        ParaAssertCommand paraAssertCommand=null;
        DBAssertCommand dbAssertCommand = null;

        //获取断言信息
        //1.数据库断言
        List<DataAssertDto> dataAssertDtoList = dataAssertService.getByCaseId(caseDto.getId());
        if(null != dataAssertDtoList && dataAssertDtoList.size() > 0){
            for(DataAssertDto dataAssert:dataAssertDtoList){
                dbAssertCommand =  new DBAssertCommand();
                dbAssertCommand.setName(dataAssert.getDatabaseName());
                dbAssertCommand.setSql(dataAssert.getSqlStr());
                dbAssertCommand.setAssertItem(assertItem);
                dbAssertCommand.setId(dataAssert.getId());
                dbAssertCommandList.add(dbAssertCommand);
                logger.info("数据库断言内容：" + dbAssertCommand.toString());
            }
        }

//        //2.redis断言-----暂时不做
//        List<Command> redisAssertCommandList = new ArrayList();
//        List<RedisAssertDto> redisAssertDtoList = redisAssertService.getByCaseId(caseDto.getId());

        //3.响应字段断言
        List<ParamsAssertDto> paramsAssertDtoList = paramsAssertService.getByCaseId(caseDto.getId());
        if(null != paramsAssertDtoList && paramsAssertDtoList.size() > 0){
            for(ParamsAssertDto paramsDto : paramsAssertDtoList){
                AssertKeyValueVo keyValueStore = new AssertKeyValueVo(paramsDto.getAssertParam(),paramsDto.getRightValue(),paramsDto.getId());
                paralist.add(keyValueStore);
                logger.info("响应字段断言内容：" + keyValueStore.toString());
            }
            paraAssertCommand = new ParaAssertCommand(paralist);
            paraAssertCommand.setAssertItem(assertItem);
        }
        AssertCommand assertCommand = new AssertCommand(paraAssertCommand, null, dbAssertCommandList, assertItem,reportId);
        return assertCommand;

    }

    //初始化执行用例之后要做的事情
    private List<Command> getAfterCommand(TestEnvironmentDto testEnvironmentDto, TestCaseDto testcaseDto) {
        List<Command> list = new ArrayList();
        Long caseId = testcaseDto.getId(); //用例id
        Long environmentId = testEnvironmentDto.getId();//环境id

        //初始化redis
        RedisUtil redisUtil = new RedisUtil(caseId,environmentId);

        //数据准备
        if(testcaseDto.getDataPrepare() > 0) {
            //数据准备---查询数据库准备
            List<RelationCaseDatabaseDto> relationCaseDatabaseDtoList = relationCaseDatabaseService.getByCaseIdAndOperateType(caseId, OperationTypeEnum.CLEAR.getId());
            if (null != relationCaseDatabaseDtoList && relationCaseDatabaseDtoList.size() > 0) {
                for (RelationCaseDatabaseDto relationCaseDatbase : relationCaseDatabaseDtoList) {
                    //执行数据准备的sql
                    logger.info("==========数据准备执行sql：" + relationCaseDatbase.getSqlStr());
                    list.add(new DBCommand(relationCaseDatbase.getDatabaseName(), relationCaseDatbase.getSqlStr()));
                }
            }
            //数据准备---redis准备
            List<RelationCaseRedisDto> relationCaseRedisDtoList = relationCaseRedisService.getByCaseIdAndOperateType(caseId, OperationTypeEnum.CLEAR.getId());
            if(null != relationCaseRedisDtoList && relationCaseRedisDtoList.size() > 0 ){
                for(RelationCaseRedisDto relationCaseRedisDto : relationCaseRedisDtoList){
                    RedisCommand redisCommand = new RedisCommand();
                    redisCommand.setKey(relationCaseRedisDto.getKey());
                    redisCommand.setMethodName(RedisOperationTypeEnum.getName(relationCaseRedisDto.getRedisOperateType()));
                    redisCommand.setTime(relationCaseRedisDto.getTime());
                    redisCommand.setValue(relationCaseRedisDto.getValue());
                    list.add(redisCommand);
                }
            }
        }
        return list;
    }

    //初始化执行用例之前要做的事情
    private List<Command> getBeforeCommand(TestEnvironmentDto testEnvironmentDto,TestCaseDto testcaseDto) {
        List<Command> list = new ArrayList();

        Long caseId = testcaseDto.getId(); //用例id
        Long environmentId = testEnvironmentDto.getId();//环境id

        //初始化redis
        RedisUtil redisUtil = new RedisUtil(caseId,environmentId);

        //数据准备
        if(testcaseDto.getDataPrepare() > 0) {
            //数据准备---查询数据库准备
            List<RelationCaseDatabaseDto> relationCaseDatabaseDtoList = relationCaseDatabaseService.getByCaseIdAndOperateType(caseId, OperationTypeEnum.PREPARE.getId());
            if (null != relationCaseDatabaseDtoList && relationCaseDatabaseDtoList.size() > 0) {
                for (RelationCaseDatabaseDto relationCaseDatbase : relationCaseDatabaseDtoList) {
                    //执行数据准备的sql
                    logger.info("==========数据准备执行sql：" + relationCaseDatbase.getSqlStr());
                    list.add(new DBCommand(relationCaseDatbase.getDatabaseName(), relationCaseDatbase.getSqlStr()));
                }
            }
            //数据准备---redis准备
            List<RelationCaseRedisDto> relationCaseRedisDtoList = relationCaseRedisService.getByCaseIdAndOperateType(caseId, OperationTypeEnum.PREPARE.getId());
            if(null != relationCaseRedisDtoList && relationCaseRedisDtoList.size() > 0 ){
                for(RelationCaseRedisDto relationCaseRedisDto : relationCaseRedisDtoList){
                    RedisCommand redisCommand = new RedisCommand();
                    redisCommand.setKey(relationCaseRedisDto.getKey());
                    redisCommand.setMethodName(RedisOperationTypeEnum.getName(relationCaseRedisDto.getRedisOperateType()));
                    redisCommand.setTime(relationCaseRedisDto.getTime());
                    redisCommand.setValue(relationCaseRedisDto.getValue());
                    list.add(redisCommand);
                }
            }
        }

      return list;
    }

    private String formatParams(TestCaseDto caseDto, String contentType) {
        //参数,如果没有配置自定义类型就说明是配置了参数,如果配置了就取自定义参数
        String paramsStr = "";
        if(null == caseDto.getCustomParams() && null == caseDto.getCustomParamsType()){
            //获取参数列表
            List<ParamDto> testParamsDtoList = testParamsService.listByCaseIdFromRelation(caseDto.getId());
            if(contentType.equals("application/json")){
                //将参数转化为json字符串类型
                if(null != testParamsDtoList && testParamsDtoList.size() > 0){
                    JSONObject jsonObject = new JSONObject();
                    Iterator iterator = testParamsDtoList.iterator();
                    while(iterator.hasNext()){
                        ParamDto param = (ParamDto) iterator.next();
                        jsonObject.put(param.getName(),param.getValue());
                    }
                    paramsStr = jsonObject.toString();
                }

            } else {
                //将参数转化为字符串类型
                if(null != testParamsDtoList && testParamsDtoList.size() > 0){
                    Iterator iterator = testParamsDtoList.iterator();
                    while(iterator.hasNext()){
                        ParamDto param = (ParamDto) iterator.next();
                        paramsStr += param.getName() + "=" + param.getValue();
                        paramsStr += "&";
                    }
                    paramsStr = paramsStr.substring(0,paramsStr.lastIndexOf("&"));
                }
            }
        } else if(null != caseDto.getCustomParams()){
            paramsStr = caseDto.getCustomParams();
        }
        return paramsStr;
    }

    /**
     * dubbo接口的用例执行方法
     * @param caseDto
     * @param interfaceDto
     * @param testEnvironmentDto
     * @param testReportDto
     */
    private void excuteDubbo(TestCaseDto caseDto, TestInterfaceDto interfaceDto, TestEnvironmentDto testEnvironmentDto, Long planId, TestReportDto testReportDto) {
        logger.info("==========dubbo接口用例执行:用例id｛｝接口id｛｝环境id｛｝========" + caseDto.getId() + "," + interfaceDto.getId() + "," + testEnvironmentDto.getId());



    }

}
