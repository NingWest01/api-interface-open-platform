package com.ning.api.common;

/**
 * 自定义错误码
 */
public enum ErrorCode {

    SUCCESS(0, "ok"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "无权限"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    PARAMETER_ERROR(50002, "参数错误"),
    NO_HAVE_ERROR(50003, "信息不存在"),
    NO_RETURN_ERROR(50004, "错误返回，接口调用失败"),
    STATUS_ERROR(50006, "接口状态信息异常"),
    REQUEST_EXHAUSTION(50007, "接口请求次数耗尽"),
    OBJECT_IS_HAVE(50008, "信息已经存在"),
    OPERATION_ERROR(50001, "操作失败");


    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
