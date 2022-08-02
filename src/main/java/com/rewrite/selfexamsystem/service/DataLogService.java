package com.rewrite.selfexamsystem.service;

import com.rewrite.selfexamsystem.utils.response.ResponseResult;

import javax.xml.ws.Response;

/**
 * @author Levi
 * @version 3.1（Created By Spring Boot）
 * @description : 历史操作Service的接口
 * @since 2022/7/24 23:19
 */

public interface DataLogService {
    ResponseResult ShowLog(String token) throws Exception;
}
