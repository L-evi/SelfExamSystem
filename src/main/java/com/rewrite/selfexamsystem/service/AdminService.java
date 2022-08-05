package com.rewrite.selfexamsystem.service;

import com.rewrite.selfexamsystem.utils.response.ResponseResult;

/**
 * @Author Levi
 * @Date 2022/5/7 11:43
 * @Version 3.0 (created by Spring Boot)
 */
public interface AdminService {
    ResponseResult adminTokenLogin(String token) throws Exception;
}
