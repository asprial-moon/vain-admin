package com.vain.enums;


public enum StatusCode {
    SUCCESS(200, "请求成功"),
    NOT_MODIFY(304, "当前已经是最新数据"),
    TOKEN_INVALID(401, "token已失效"),
    TOKEN_LEGAL(402, "token不合法"),
    FORBIDDEN(403, "无权操作"),
    NOT_FOUND(404, "请求的数据不存在"),
    INTERNAL_SERVER_ERROR(500, "服务器异常"),
    PARAMETER_ERROR(501,"参数错误"),
    FAIl(1000, "请求失败"),
    ACCOUNT_LOCK(1101, "账户已被锁定"),
    ACCOUNT_NOT_EXIST(1102, "账户不存在"),
    ACCOUNT_PASSWORD_ERROR(1103, "密码错误"),
    ACCOUNT_LOGIN(1104, "请登录后再操作"),
    DATA_NOT_MODIFY(1201, "数据未修改"),
    SCHEDULE_EXPRESSION_ERROR(1301,"定时任务表达式错误");
    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
