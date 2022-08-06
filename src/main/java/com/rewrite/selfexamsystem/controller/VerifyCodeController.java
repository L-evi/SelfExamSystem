package com.rewrite.selfexamsystem.controller;

import com.alibaba.fastjson.JSONObject;
import com.rewrite.selfexamsystem.Annotation.DataLogAnnotation;
import com.rewrite.selfexamsystem.utils.KaptchaUtil;
import com.rewrite.selfexamsystem.utils.redis.RedisCache;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import com.rewrite.selfexamsystem.utils.response.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Levi
 * @version 3.1（Created By Spring Boot）
 * @description :  获取验证码的接口
 * @since 2022/8/4 22:33
 */
@RestController
public class VerifyCodeController {
    @Autowired
    private RedisCache redisCache;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @DataLogAnnotation()
    @RequestMapping(value = "/getVerifyCode", method = RequestMethod.GET)
    public void getVerifyCode(HttpServletRequest request, HttpServletResponse response) {
        String randomText = KaptchaUtil.getRandomText(4);
        try {
            BufferedImage verifyImage = KaptchaUtil.getVerifyImage(200, 69, randomText);
            response.setContentType("image/jpeg");
//            设置验证码标识符UUID
            String uuid = UUID.randomUUID().toString();
            response.setHeader("verifyCodeUUID", uuid);
//            将uuid以及验证码加密放入redis中，过期时间60秒
            String encode = bCryptPasswordEncoder.encode(randomText.toLowerCase(Locale.ROOT));
            redisCache.setCacheObject("verify_code:" + uuid, encode, 60, TimeUnit.SECONDS);
//            将图片给前端
            OutputStream stream = response.getOutputStream();
            ImageIO.write(verifyImage, "jpg", stream);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/checkVerifyCode", method = RequestMethod.POST)
    public ResponseResult checkVerifyCode(@RequestHeader Map<String, Object> headerMap, @RequestBody Map<String, Object> getMap) {
        JSONObject jsonObject = new JSONObject();
        String uuid = (String) headerMap.get("uuid");
        String encode = redisCache.getCacheObject("verify_code:" + uuid);
        String verifyCode = (String) getMap.get("verifyCode");
        Map<String, Object> res = KaptchaUtil.checkVerifyCode(encode, verifyCode);
        if ("fail".equals(res.get("status"))) {
            res.remove("status");
            jsonObject.putAll(res);
            return new ResponseResult(ResultCode.REQUEST_TIMEOUT, jsonObject);
        }
        res.remove("status");
        return new ResponseResult(ResultCode.SUCCESS, res);
    }

}
