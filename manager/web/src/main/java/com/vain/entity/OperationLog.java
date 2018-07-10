package com.vain.entity;

import com.vain.base.entity.PagedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author vain
 * @date： 2017/11/3 11:12
 * @description： 日志实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationLog extends PagedEntity {
    /**
     * 操作类型
     */
    private Integer operationType;

    /**
     * 操作用户Id
     */
    private Integer userId;

    /**
     * 操作数据id
     */
    private String operationData;

    /**
     * 操作ip
     */
    private String operationIP;

    /**
     * 操作类
     */
    private String className;


    /**
     * 操作方法
     */
    private String methodName;

    /**
     * 异常信息
     */
    private String exceptionMessage;

    /**
     * 操作信息
     */
    private String info;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 操作时间
     */
    private Date createTime;

    /**
     * 状态集合
     */
    private List<Integer> operationTypes;

    /**
     * 批量操作id
     */
    private List<Integer> ids;
}
