package com.vain.base.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author vain
 * @description: 分类实体父类
 * @date 2017/8/31 11:42
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PagedEntity extends Entity {

    /**
     * 当前页码 返回对象不解析 此字段
     */
    @JSONField(serialize = false)
    private Integer currentPage;

    /**
     * 每页的大小
     */
    @JSONField(serialize = false)
    private Integer pageSize;

    /**
     * 如果请求没有传分页参数，给默认值
     */
    public void initPageParam() {
        if (this.currentPage == null || this.pageSize == null) {
            this.currentPage = 1;
            this.pageSize = 20;
        }
    }

}
