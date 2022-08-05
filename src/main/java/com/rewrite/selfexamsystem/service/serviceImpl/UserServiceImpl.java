package com.rewrite.selfexamsystem.service.serviceImpl;
// serviceImpl：对Service中声明的函数进行定义


import com.alibaba.fastjson.JSONObject;
import com.rewrite.selfexamsystem.domain.LoginData;
import com.rewrite.selfexamsystem.domain.UserInformation;
import com.rewrite.selfexamsystem.mapper.LoginDataMapper;
import com.rewrite.selfexamsystem.mapper.UserInformationMapper;
import com.rewrite.selfexamsystem.service.UserService;
import com.rewrite.selfexamsystem.utils.JwtUtil;
import com.rewrite.selfexamsystem.utils.KaptchaUtil;
import com.rewrite.selfexamsystem.utils.MailUtil;
import com.rewrite.selfexamsystem.utils.redis.RedisCache;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import com.rewrite.selfexamsystem.utils.response.ResultCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Levi
 * @version 3.1 (created by Spring Boot)
 * @description : 用于用户各种操作（除登录外）的类
 * @since 2022/7/21 11:44
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserInformationMapper userInformationMapper;
    @Autowired
    private LoginDataMapper loginDataMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private JavaMailSender mailSender;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    /**
     * @param userInformation: 用户详细信息
     * @param loginData:       用户账号密码
     * @return 返回注册是否成功的响应体
     * @description: 通过前端传回来的用户信息进行注册，并且进行检验用户是否存在
     * @author Levi
     * @since 2022/7/21 22:50
     */
    @Override
    public ResponseResult UserRegisterService(UserInformation userInformation, LoginData loginData) {
//        密码加密
//        看一下是不是已经有了这一个用户信息
        LoginData user = loginDataMapper.selectUserDataByUsername(loginData.getUsername());
        if (!Objects.isNull(user)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("des", "注册失败，该账户已被注册");
            return new ResponseResult(ResultCode.INVALID_PARAMETER, jsonObject);
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        loginData.setPassword(bCryptPasswordEncoder.encode(loginData.getPassword()));
//        将数据写入到数据库中
        userInformationMapper.addUser(userInformation);
        loginDataMapper.addUsernamePassword(loginData);
//        返回响应到前端
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("des", "注册成功");
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

    /**
     * @param token: 前端传回来的token
     * @return 返回前端username等登录信息
     * @description: 通过token解析出username返回到前端
     * @author Levi
     * @since 2022/7/21 22:28
     */
    @Override
    public ResponseResult UserTokenLogin(String token) throws Exception {
        Claims claims = JwtUtil.parseJwt(token);
        JSONObject jsonObject = JSONObject.parseObject(claims.getSubject());
//      鉴权
        String role = (String) jsonObject.get("role");
        if (!"user".equals(role)) {
            jsonObject = new JSONObject();
            jsonObject.put("des", "权限不足，无法登录");
            return new ResponseResult(ResultCode.TOKEN_EXPIRATION, jsonObject);
        }
        String username = (String) jsonObject.get("username");
//        从redis中获取相关信息
        UserInformation userInformation = redisCache.getCacheObject("user_information:" + username);
//        将获取到的信息存回到json中放入响应体中
        jsonObject = new JSONObject();
        jsonObject.put("des", "Token登录成功");
        jsonObject.put("username", username);
        jsonObject.put("name", userInformation.getName());
        jsonObject.put("token", token);
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

    /**
     * @param token: 其中含有已经加密的信息username
     * @return 将查询到的用户信息以及查询结果返回到前端
     * @description: 通过token中的username，在redis中查询对应信息返回到前端
     * @author Levi
     * @since 2022/7/21 22:52
     */
    @Override
    public ResponseResult UserPersonalInformation(String token) throws Exception {
//        从token中解析出username
        JSONObject jsonObject = JSONObject.parseObject(JwtUtil.parseJwt(token).getSubject());
        String username = (String) jsonObject.get("username");
//        从redis中获取信息
        UserInformation userInformation = redisCache.getCacheObject("user_information:" + username);
//        将用户信息放入到json中返回
        jsonObject = (JSONObject) JSONObject.toJSON(userInformation);
        jsonObject.put("des", "用户信息获取成功");
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

    /**
     * @param userInformation: 前端传进来的修改后的用户信息
     * @param token:           前端传进来的Token
     * @return 根据修改结果返回前端是否成功的信息
     * @description: 将前端传回来需要修改的用户数据替换掉数据库中指定的数据
     * @author Levi
     * @since 2022/7/21 23:38
     */
    @Override
    public ResponseResult UserModifyInformation(UserInformation userInformation, String token) throws Exception {
//        解析Token
        JSONObject jsonObject = JSONObject.parseObject(JwtUtil.parseJwt(token).getSubject());
        String username = (String) jsonObject.get("username");
//        修改的是token中对应的用户信息
        userInformation.setUsername(username);
        jsonObject = new JSONObject();
//        从数据库中更新信息
        if (!userInformationMapper.updateUserInformation(userInformation)) {
            jsonObject.put("des", "修改用户信息失败");
            return new ResponseResult(ResultCode.DATABASE_ERROR, jsonObject);
        }
        jsonObject.put("des", "修改用户信息成功");
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

    @Override
    public ResponseResult userForgetPassword(String username) {
        JSONObject jsonObject = new JSONObject();
//        通过username查找邮箱
        String email = userInformationMapper.getEmail(username);
        if (email == null || email.isEmpty() || "".equals(email)) {
            jsonObject.put("des", "用户尚未注册");
            return new ResponseResult(ResultCode.INVALID_PARAMETER, jsonObject);
        }
//        生成验证码（六位）
        String randomText = KaptchaUtil.getRandomText(6);
//        发送邮件
        String[] recipients = new String[1];
        recipients[0] = email;
        MailUtil mailUtil = new MailUtil("3573897471@qq.com", recipients, "重置密码邮箱认证（勿回复）", "你的邮箱验证码为" + randomText + "，请于十分钟内进行验证\n" + "请勿泄露该验证码");
        if (!mailUtil.sendTextMail(mailSender)) {
            jsonObject.put("des", "邮件发送失败");
            return new ResponseResult(ResultCode.SYSTEM_ERROR, jsonObject);
        }
//        将验证码以及username放入redis中，并且有效时间为十分钟
        redisCache.setCacheObject("email_verify:" + username, bCryptPasswordEncoder.encode(randomText), 10, TimeUnit.MINUTES);
//        返回信息
        jsonObject.put("des", "邮件发送成功，请前往认证");
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

    @Override
    public ResponseResult userResetPassword(LoginData loginData, String emailVerify) {
        JSONObject jsonObject = new JSONObject();
//        从redis中获取验证码并且对比
        String encode = redisCache.getCacheObject("email_verify:" + loginData.getUsername());
        Map<String, Object> res = KaptchaUtil.checkVerifyCode(encode, emailVerify);
        if ("fail".equals(res.get("status"))) {
            res.remove("status");
            return new ResponseResult(ResultCode.REQUEST_TIMEOUT, res);
        }
//        加密用户密码
        loginData.setPassword(bCryptPasswordEncoder.encode(loginData.getPassword()));
//        修改用户密码
        int updatePassword = loginDataMapper.updatePassword(loginData);
//        验证修改是否成功
        if (updatePassword == 0) {
            jsonObject.put("des", "重置密码失败");
            return new ResponseResult(ResultCode.SERVER_ERROR, jsonObject);
        }
//        从redis中删除对应用户信息
        redisCache.deleteObject("user_data:" + loginData.getUsername());
        redisCache.deleteObject("user_information:" + loginData.getUsername());
        jsonObject.put("des", "修改密码成功，请重新登录");
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

}
