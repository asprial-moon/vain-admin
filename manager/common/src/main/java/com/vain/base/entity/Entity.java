package com.vain.base.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author vain
 * @description: 所有数据对象的超类
 * @date 2017/8/31 11:41
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Entity {

    /**
     * 实体记录的id
     */
    protected Integer id;

}
