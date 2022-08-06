package com.rewrite.selfexamsystem.utils.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.xml.ws.Response;

/**
 * @author: Levi
 * @description: 用于响应Controller请求的响应体；注解@JsonInclude(JsonInclude.Include.NON_NULL)返回到前端的数据如果null则不显示
 * @since: 2022/7/19 13:16
 * @version: 3.1（Created By Spring Boot）
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> {


    //    状态码
    private int code;

    //    状态信息
    private String msg;

    //    状态标志
    private int status;
    //    枚举类
    private ResultCode resultCode;

    //    返回的数据对象data：使用泛型
    private T data;

    // 手动设置返回vo
    public ResponseResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 默认返回成功状态码，数据对象
    public ResponseResult(T data) {
        this.code = ResultCode.SUCCESS.getCode();
        this.msg = ResultCode.SUCCESS.getMsg();
        this.data = data;
    }

    // 返回指定状态码，数据对象
    public ResponseResult(StatusCode statusCode, T data) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.data = data;
    }

    // 只返回状态码
    public ResponseResult(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.data = null;
    }

    //    返回枚举类
    public ResponseResult(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
        this.status = resultCode.getStatus();
        this.data = data;
    }
}
