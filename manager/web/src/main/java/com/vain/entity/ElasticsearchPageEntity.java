package com.vain.entity;

import com.vain.base.entity.PagedEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author vain
 * @Description
 * @date 2018/12/25 22:35
 */
@Setter
@Getter
public class ElasticsearchPageEntity extends PagedEntity {

    /**
     * 总记录
     */
    private int total;

    /**
     * 数据
     */
    private List<Map<String, Object>> data;

    public ElasticsearchPageEntity(Integer currentPage, Integer pageSize, int total, List<Map<String, Object>> data) {
        super(currentPage, pageSize);
        this.total = total;
        this.data = data;
    }
}
