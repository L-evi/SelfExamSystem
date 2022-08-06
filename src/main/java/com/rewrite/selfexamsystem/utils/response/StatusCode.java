package com.rewrite.selfexamsystem.utils.response;

/**
 * @author: Levi
 * @description: 状态码接口，所有状态码都要实现他
 * @since: 2022/7/19 13:20
 * @version: 3.1（Created By Spring Boot）
 */

public interface StatusCode {

    int getCode();


    String getMsg();

    int getStatus();
}
