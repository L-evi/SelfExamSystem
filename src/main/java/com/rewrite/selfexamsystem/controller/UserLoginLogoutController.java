package com.rewrite.selfexamsystem.controller;

import com.rewrite.selfexamsystem.Annotation.DataLogAnnotation;
import com.rewrite.selfexamsystem.domain.LoginData;
import com.rewrite.selfexamsystem.service.UserLoginLogoutService;
import com.rewrite.selfexamsystem.utils.KaptchaUtil;
import com.rewrite.selfexamsystem.utils.redis.RedisCache;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import com.rewrite.selfexamsystem.utils.response.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @Autowired
    private RedisCache redisCache;

    /**
     * @param getMap:
     * @return
     * @description: 用于用户登录、注销的Controller
     * @author Levi
     * @since 2022/7/20 10:18
     */
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public ResponseResult UserLogin(@RequestBody Map<String, Object> getMap, @RequestHeader Map<String, Object> headerMap) {
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
        loginData.setUsername(getMap.get("username") + "|" + "user");
        loginData.setPassword((String) getMap.get("password"));
        return userLoginLogoutService.login(loginData);
    }

    @DataLogAnnotation(thing = "用户注销登录", peopleType = "User")
    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public ResponseResult UserLogout() {
        return userLoginLogoutService.logout();
    }

}
