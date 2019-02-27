package com.vain.base.entity;


import com.github.pagehelper.PageInfo;
import com.vain.enums.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author vain
 * @description: restful相应实体
 * @date 2017/8/31 11:42
 */
@Data
@AllArgsConstructor
public class Response<T extends Entity> {

    /**
     * 响应码，参见StatusCode
     *
     * @see com.vain.enums.StatusCode
     */
    private Integer code;

    /**
     * 响应消息，参见StatusCode
     *
     * @see com.vain.enums.StatusCode
     */
    private String message;

    /**
     * 针对分页返回的记录总大小
     */
    private Long totalSize;

    /**
     * 当前页数
     */
    private Integer currentPage;

    /**
     * 分页大小
     */
    private Integer pageSize;

    /**
     * 数据的最后修改时间，用来判断数据是否有更新
     */
    private Timestamp lastModifyTime;

    /**
     * 单条数据
     */
    private Object data;

    /**
     * 列表数据
     */
    private List<T> dataList;

    @SuppressWarnings("nochedcked")
    public Response<T> setData(Object data) {
        this.data = data;
        if (data == null) {
            this.code = StatusCode.NOT_FOUND.getCode();
            this.message = StatusCode.NOT_FOUND.getMessage();
        } else {
            this.code = StatusCode.SUCCESS.getCode();
            this.message = StatusCode.SUCCESS.getMessage();
        }
        if (data instanceof Integer) {
            if ((int) data == 0) {
                //数据库影响的行数为0 没修改数据
                this.code = StatusCode.DATA_NOT_MODIFY.getCode();
                this.message = StatusCode.DATA_NOT_MODIFY.getMessage();
            }
        } else if (data instanceof PageInfo) {
            this.data = null;
            this.totalSize = ((PageInfo) data).getTotal();
            this.dataList = ((PageInfo) data).getList();
            this.currentPage = ((PageInfo) data).getPageNum();
            this.pageSize = ((PageInfo) data).getPageSize();
        }
        return this;
    }

    /**
     * 抽取公用code 如果需要自己设置code 可在设置data之后再set code和msg
     *
     * @param dataList
     */
    @SuppressWarnings("all")
    public Response<T> setDataList(List<T> dataList) {
        this.dataList = dataList;
        if (dataList == null) {
            this.code = StatusCode.NOT_FOUND.getCode();
            this.message = StatusCode.NOT_FOUND.getMessage();
        } else {
            this.code = StatusCode.SUCCESS.getCode();
            this.message = StatusCode.SUCCESS.getMessage();
        }
        return this;
    }

    public Response() {
        this.code = StatusCode.SUCCESS.getCode();
        this.message = StatusCode.SUCCESS.getMessage();
    }

    public Response(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.message = statusCode.getMessage();
    }

    public void setStatusCode(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.message = statusCode.getMessage();
    }

    public static Response success() {
        return new Response(StatusCode.SUCCESS);
    }

    public static Response success(String message) {
        Response response = new Response(StatusCode.SUCCESS);
        response.setMessage(message);
        return response;
    }

    public static Response error() {
        return new Response(StatusCode.FAIl);
    }

    public static Response error(String message) {
        Response response = new Response(StatusCode.FAIl);
        response.setMessage(message);
        return response;
    }

    public static Response parameterError() {
        return new Response(StatusCode.PARAMETER_ERROR);
    }

    public static Response parameterError(String message) {
        Response response = new Response(StatusCode.PARAMETER_ERROR);
        response.setMessage(message);
        return response;
    }
}
