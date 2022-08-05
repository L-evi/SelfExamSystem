package com.rewrite.selfexamsystem.service;
// 声明服务层的函数

import com.rewrite.selfexamsystem.domain.LoginData;
import com.rewrite.selfexamsystem.domain.UserInformation;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;

/**
 * @Author Levi
 * @Date 2022/5/7 11:45
 * @Version 3.0 (created by Spring Boot)
 */
public interface UserService {
    //    用户注册接口
    ResponseResult UserRegisterService(UserInformation userInformation, LoginData loginData);

    //    用户利用token登录接口（前端刷新）
    ResponseResult UserTokenLogin(String token) throws Exception;

    ResponseResult UserPersonalInformation(String token) throws Exception;

    ResponseResult UserModifyInformation(UserInformation userInformation, String token) throws Exception;

    ResponseResult userForgetPassword(String username);

    ResponseResult userResetPassword(LoginData loginData, String emailVerify);
}
