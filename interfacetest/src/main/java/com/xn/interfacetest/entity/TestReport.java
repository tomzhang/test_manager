/*
 * Copyright (c) 2014-2015, Yunnex and/or its affiliates. All rights reserved. Use, Copy is subject to authorized license.
 */
package com.xn.interfacetest.entity;

import com.xn.common.base.BaseEntity;

/**
 * TestReport 实体类
 * 
 * @author Carol
 * @date 2017-02-14
 */
public class TestReport extends BaseEntity {

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = 1L;

    /**
     * 主键列
     * id 
     */
    private Long id;

    /**
     * 
     * 测试报告名称 
     */
    private String name;

    /**
     * 
     * 用例id，逗号隔开 
     */
    private String caseIds;

    /**
     * 
     * 系统id 
     */
    private Long systemId;

    /**
     * 
     * 计划id 
     */
    private Long planId;

    /**
     * 
     * 接口id，逗号隔开 
     */
    private String interfaceIds;

    /**
     * 
     * 执行用例数 
     */
    private Long caseCount;

    /**
     * 
     * 用例通过数 
     */
    private Long passCaseCount;

    /**
     * 
     * 失败用例数 
     */
    private Long failCaseCout;

    /**
     * 
     * 错误用例数 
     */
    private Long errorCaseCout;

    /**
     * 
     * 测试结果 
     */
    private String result;

    /**
     * 计划开始执行的时间
     */
    private String beginTime;

    /**
     * 计划执行结束的时间
     */
    private String endTime;

    /**
     * 计划执行消耗的时间
     */
    private Integer costTime;
	
	public TestReport(){
	    // default constructor
	}
    
    public TestReport(Long id){
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCaseIds() {
        return this.caseIds;
    }
    
    public void setCaseIds(String caseIds) {
        this.caseIds = caseIds;
    }
    
    public Long getSystemId() {
        return this.systemId;
    }
    
    public void setSystemId(Long systemId) {
        this.systemId = systemId;
    }
    
    public Long getPlanId() {
        return this.planId;
    }
    
    public void setPlanId(Long planId) {
        this.planId = planId;
    }
    
    public String getInterfaceIds() {
        return this.interfaceIds;
    }
    
    public void setInterfaceIds(String interfaceIds) {
        this.interfaceIds = interfaceIds;
    }
    
    public Long getCaseCount() {
        return this.caseCount;
    }
    
    public void setCaseCount(Long caseCount) {
        this.caseCount = caseCount;
    }
    
    public Long getPassCaseCount() {
        return this.passCaseCount;
    }
    
    public void setPassCaseCount(Long passCaseCount) {
        this.passCaseCount = passCaseCount;
    }
    
    public Long getFailCaseCout() {
        return this.failCaseCout;
    }
    
    public void setFailCaseCout(Long failCaseCout) {
        this.failCaseCout = failCaseCout;
    }
    
    public Long getErrorCaseCout() {
        return this.errorCaseCout;
    }
    
    public void setErrorCaseCout(Long errorCaseCout) {
        this.errorCaseCout = errorCaseCout;
    }
    
    public String getResult() {
        return this.result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getCostTime() {
        return costTime;
    }

    public void setCostTime(Integer costTime) {
        this.costTime = costTime;
    }
}
