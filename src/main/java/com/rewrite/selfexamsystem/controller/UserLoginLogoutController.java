package com.rewrite.selfexamsystem.controller;

import com.alibaba.fastjson.JSONObject;
import com.rewrite.selfexamsystem.Annotation.DataLogAnnotation;
import com.rewrite.selfexamsystem.domain.LoginData;
import com.rewrite.selfexamsystem.service.UserLoginLogoutService;
import com.rewrite.selfexamsystem.utils.KaptchaUtil;
import com.rewrite.selfexamsystem.utils.redis.RedisCache;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import com.rewrite.selfexamsystem.utils.response.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
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
     * @param getMap: 从中获取账号密码以及验证码等信息
     * @return 返回用户是否登陆成功等信息学
     * @description: 用户登录：通过账号密码验证码进行登录
     * @author Levi
     * @since 2022/7/20 10:18
     */
    @DataLogAnnotation(thing = "用户登录", peopleType = "user")
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public ResponseResult UserLogin(@RequestBody Map<String, Object> getMap, @RequestHeader Map<String, Object> headerMap) {
        JSONObject jsonObject = new JSONObject();
        //        验证验证码
        String uuid = (String) headerMap.get("uuid");
        String encode = redisCache.getCacheObject("verify_code:" + uuid);
        String verifyCode = (String) getMap.get("verifyCode");
        Map<String, Object> res = KaptchaUtil.checkVerifyCode(encode, verifyCode.toLowerCase(Locale.ROOT));
        if ("fail".equals(res.get("status"))) {
            res.remove("status");
            jsonObject.putAll(res);
            return new ResponseResult(ResultCode.LOGIN_TIMEOUT, jsonObject);
        }
//        插入身份信息到账号中
        LoginData loginData = new LoginData();
        loginData.setUsername(getMap.get("username") + "|" + "user");
        loginData.setPassword((String) getMap.get("password"));
        return userLoginLogoutService.login(loginData);
    }

    /**
     * @param :
     * @return 返回用户是否注销登录成功等信息
     * @description : 用户注销登录：用户注销登录并删除redis中相关信息
     * @author Levi
     * @since 2022/8/6 20:02
     */
    @DataLogAnnotation(thing = "用户注销登录", peopleType = "user")
    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public ResponseResult UserLogout() {
        return userLoginLogoutService.logout();
    }

}
