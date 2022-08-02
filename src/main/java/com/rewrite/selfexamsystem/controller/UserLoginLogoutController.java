package com.rewrite.selfexamsystem.controller;

import com.rewrite.selfexamsystem.Annotation.DataLogAnnotation;
import com.rewrite.selfexamsystem.domain.LoginData;
import com.rewrite.selfexamsystem.service.UserLoginLogoutService;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Levi
 * @description: 用于用户登录、注销的接口
 * @since: 2022/7/20 10:29
 * @version: 3.1（Created By Spring Boot）
 */
@RestController
public class UserLoginLogoutController {
    @Autowired
    private UserLoginLogoutService userLoginLogoutService;

    /**
     * @param loginData:
     * @return
     * @description: 用于用户登录、注销的Controller
     * @author Levi
     * @since 2022/7/20 10:18
     */
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public ResponseResult UserLogin(@RequestBody LoginData loginData) {
//        插入身份信息到账号中
        loginData.setUsername(loginData.getUsername() + "|" + "user");
        return userLoginLogoutService.login(loginData);
    }

    @DataLogAnnotation(thing = "用户注销登录", peopleType = "User")
    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public ResponseResult UserLogout() {
        return userLoginLogoutService.logout();
    }

}
