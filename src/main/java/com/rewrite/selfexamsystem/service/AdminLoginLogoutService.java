package com.rewrite.selfexamsystem.service;

import com.rewrite.selfexamsystem.domain.LoginData;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;

/**
 * @author: Levi
 * @description:
 * @since: 2022/7/22 22:17
 * @version: 3.1（Created By Spring Boot）
 */

public interface AdminLoginLogoutService {

    ResponseResult login(LoginData loginData);

    ResponseResult logout();
}
