package com.rewrite.selfexamsystem.service;
// 声明服务层的函数

import com.rewrite.selfexamsystem.domain.LoginData;
import com.rewrite.selfexamsystem.domain.UserInformation;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;

/**
 * @author Levi
 * @version 3.0 (created by Spring Boot)
 * @description : 用户层面Service的Service接口
 * @since 2022/5/7 11:45
 */
public interface UserService {
    ResponseResult UserRegisterService(UserInformation userInformation, LoginData loginData);

    ResponseResult UserTokenLogin(String token) throws Exception;

    ResponseResult UserPersonalInformation(String token) throws Exception;

    ResponseResult UserModifyInformation(UserInformation userInformation, String token) throws Exception;

    ResponseResult userForgetPassword(String username);

    ResponseResult userResetPassword(LoginData loginData, String emailVerify);
}
