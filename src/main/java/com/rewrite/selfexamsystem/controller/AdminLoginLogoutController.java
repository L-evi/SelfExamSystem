package com.rewrite.selfexamsystem.controller;

import com.rewrite.selfexamsystem.domain.LoginData;
import com.rewrite.selfexamsystem.service.AdminLoginLogoutService;
import com.rewrite.selfexamsystem.utils.KaptchaUtil;
import com.rewrite.selfexamsystem.utils.redis.RedisCache;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import com.rewrite.selfexamsystem.utils.response.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author: Levi
 * @description: 管理员用于登录、注销的Controller
 * @since: 2022/7/22 22:04
 * @version: 3.1（Created By Spring Boot）
 */
@RestController
@RequestMapping(value = "/admin")
public class AdminLoginLogoutController {
    @Autowired
    private AdminLoginLogoutService adminLoginLogoutService;

    @Autowired
    private RedisCache redisCache;

    /**
     * @param getMap:    获取管理员登录信息
     * @param headerMap: 获取验证码的uuid
     * @return 返回管理员是否登陆成功的信息
     * @description : 管理员登录：管理员通过账号密码以及验证码进行登录
     * @author Levi
     * @since 2022/8/5 11:06
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseResult AdminLogin(@RequestBody Map<String, Object> getMap, @RequestHeader Map<String, Object> headerMap) {
//        验证验证码
        String uuid = (String) headerMap.get("uuid");
        String encode = redisCache.getCacheObject("verify_code:" + uuid);
        String verifyCode = (String) getMap.get("verifyCode");
        Map<String, Object> res = KaptchaUtil.checkVerifyCode(encode, verifyCode);
        if ("fail".equals(res.get("status"))) {
            res.remove("status");
            return new ResponseResult(ResultCode.LOGIN_TIMEOUT, res);
        }
//        插入身份信息到账号中
        LoginData loginData = new LoginData();
        loginData.setUsername(getMap.get("username") + "|" + "admin");
        loginData.setPassword((String) getMap.get("password"));
        return adminLoginLogoutService.login(loginData);
    }

    /**
     * @param :
     * @return 返回管理员注销登录是否成功等信息
     * @description : 管理员注销登录：管理员注销登录的controller接口
     * @author Levi
     * @since 2022/8/5 11:19
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseResult AdminLogout() {
        return adminLoginLogoutService.logout();
    }
}
