package com.rewrite.selfexamsystem.utils.response;

/**
 * @author: Levi
 * @description: 状态码接口，所有状态码都要实现他
 * @since: 2022/7/19 13:20
 * @version: 3.1（Created By Spring Boot）
 */

public interface StatusCode {
    /**
     * @param
     * @return 返回状态码Code
     * @description: 用于返回状态码的函数
     * @author Levi
     * @since 2022/7/19 13:23
     */
    int getCode();

    /**
     * @param
     * @return 返回状态信息
     * @description: 返回状态信息Msg的函数
     * @author Levi
     * @since 2022/7/19 13:24
     */
    String getMsg();

    int getStatus();
}
