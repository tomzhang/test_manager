/*
 * Copyright (c) 2014-2015, Yunnex and/or its affiliates. All rights reserved. Use, Copy is subject to authorized license.
 */
package com.xn.common.company.dto;


import com.xn.common.base.BaseDto;

/**
 * Department Dto 对象
 * 
 * @author Carol
 * @date 2017-02-14
 */
public class DepartmentDto extends BaseDto {
    
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
     * name 
     */
    private String name;

    /**
     * 
     * companyId 
     */
    private Long companyId;

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

    public Long getCompanyId() {
        return this.companyId;
    }
    
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }



}

