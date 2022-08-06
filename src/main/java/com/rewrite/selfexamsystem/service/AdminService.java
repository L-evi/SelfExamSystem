package com.rewrite.selfexamsystem.service;

import com.rewrite.selfexamsystem.utils.response.ResponseResult;

/**
 * @author Levi
 * @version 3.0 (created by Spring Boot)
 * @description : 管理员Service接口
 * @since 2022/5/7 11:43
 */
public interface AdminService {
    ResponseResult adminTokenLogin(String token) throws Exception;
}
