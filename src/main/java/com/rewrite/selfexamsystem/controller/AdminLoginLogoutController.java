package com.rewrite.selfexamsystem.controller;

import com.rewrite.selfexamsystem.domain.LoginData;
import com.rewrite.selfexamsystem.service.AdminLoginLogoutService;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Levi
 * @description: TODO
 * @since: 2022/7/22 22:04
 * @version: 3.1（Created By Spring Boot）
 */
@RestController
@RequestMapping(value = "/admin")
public class AdminLoginLogoutController {
    @Autowired
    private AdminLoginLogoutService adminLoginLogoutService;

    //    TODO admin登录Controller
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseResult AdminLogin(@RequestBody LoginData loginData) {
//        插入身份信息到账号中
        loginData.setUsername(loginData.getUsername() + "|" + "admin");
        return adminLoginLogoutService.login(loginData);
    }

    //    TODO admin注销Controller
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseResult AdminLogout() {
        return null;
    }
}
