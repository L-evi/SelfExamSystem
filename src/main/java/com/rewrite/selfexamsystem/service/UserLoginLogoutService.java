package com.rewrite.selfexamsystem.service;

import com.rewrite.selfexamsystem.domain.LoginData;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;

/**
 * @author: Levi
 * @description: 用于用于用户登录、退出登录的接口文件
 * @since: 2022/7/20 10:31
 * @version: 3.1（Created By Spring Boot）
 */

public interface UserLoginLogoutService {
    ResponseResult login(LoginData loginData);

    ResponseResult logout();
}
