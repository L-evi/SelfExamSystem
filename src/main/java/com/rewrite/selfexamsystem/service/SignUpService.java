package com.rewrite.selfexamsystem.service;

import com.rewrite.selfexamsystem.domain.SignUp;
import com.rewrite.selfexamsystem.domain.UserInformation;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author: Levi
 * @description: SignUp报名Service接口
 * @since: 2022/7/29 23:14
 * @version: 3.1（Created By Spring Boot）
 */

public interface SignUpService {
    /**
     * 用户层面
     */
    ResponseResult getUserStatus(String token) throws Exception;

    ResponseResult getUserInformation(String token) throws Exception;

    ResponseResult uploadUserInformation(String token, MultipartFile[] files, SignUp signUp, UserInformation userInformation) throws Exception;

    ResponseResult updateUserInformation(String token, MultipartFile[] files, SignUp signUp, UserInformation userInformation) throws Exception;

    ResponseResult deleteUserInformation(String token) throws Exception;

    /**
     * 管理员层面
     */
    ResponseResult adminGetPersoonNumber(String token) throws Exception;

    ResponseResult adminGetSearchNumber();

    ResponseResult adminGetInformation(String page, String token) throws Exception;

    ResponseResult adminSearchInformation(Map<String, Object> getMap, String token) throws Exception;

    ResponseResult adminExamine(SignUp signUp, String token) throws Exception;
}
