/*
 * Copyright (c) 2014-2015, Yunnex and/or its affiliates. All rights reserved. Use, Copy is subject to authorized license.
 */
package com.xn.interfacetest.api;

import java.util.List;
import java.util.Map;

import com.xn.common.utils.PageResult;
import com.xn.interfacetest.dto.RelationCaseRedisDto;


/**
 * RelationCaseRedis Service
 * 
 * @author Carol
 * @date 2017-03-02
 */
public interface RelationCaseRedisService {

    /**
     * 查询单个记录
     * 主键：id 
     * @param condition 主键/Map/查询对象
     * @return 
     */
    RelationCaseRedisDto get(Object condition);

    /**
     * 统计数量
     * 
     * @param condition 查询条件对象
     * @return 统计数量
     */
    long count(RelationCaseRedisDto condition);

    /**
     * 根据组合条件查询
     * 
     * @param condition 查询对象
     * @return 集合,如果不存在,返回Empty List
     */
    List<RelationCaseRedisDto> list(RelationCaseRedisDto condition);

    /**
     * 根据组合条件查询,不建议用该方法进行分页  
     * 
     * @param condition 查询对象
     * @return 集合,如果不存在,返回Empty List
     */
    List<RelationCaseRedisDto> list(Map<String, Object> condition);
    
    /**
     * 根据组合条件做分页查询,需要condition中包含分页对象page  
     * 
     * @param condition 查询对象
     * @return 集合,如果不存在,返回Empty List
     */
    PageResult<RelationCaseRedisDto> page(Map<String, Object> condition);
    

    /**
     * 保存
     * 
     * @param relationCaseRedis 
     * @return 带主键的DTO
     */
    RelationCaseRedisDto save(RelationCaseRedisDto relationCaseRedisDto);

    /**
     * 批量保存
     * 
     * @param relationCaseRediss 
     * @return 带主键的DTO
     */
    int save(List<RelationCaseRedisDto> relationCaseRedisDtos);

    /**
     * 更新
     * 
     * @param relationCaseRedis 
     * @return 操作影响行数
     */
    int update(RelationCaseRedisDto relationCaseRedisDto);
    
    /**
     * 根据主键删除
     * 不建议，建议使用delete(RelationCaseRedis relationCaseRedis)
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
    int delete(RelationCaseRedisDto relationCaseRedisDto);
    
    /**
     * 根据主键删除
     * 不建议，建议使用delete(RelationCaseRedisDto relationCaseRedis)
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
    int deleteBatch(List<RelationCaseRedisDto> relationCaseRediss);

    /**
     * 根据用例id和操作类型查询redis操作
     * @param caseId
     * @param id
     * @return
     */
    List<RelationCaseRedisDto> getByCaseIdAndOperateType(Long caseId, int id);
}
