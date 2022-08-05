package com.rewrite.selfexamsystem.service.serviceImpl;
// serviceImpl：对Service中声明的函数进行定义


import com.alibaba.fastjson.JSONObject;
import com.rewrite.selfexamsystem.domain.LoginData;
import com.rewrite.selfexamsystem.domain.UserInformation;
import com.rewrite.selfexamsystem.mapper.LoginDataMapper;
import com.rewrite.selfexamsystem.mapper.UserInformationMapper;
import com.rewrite.selfexamsystem.service.UserService;
import com.rewrite.selfexamsystem.utils.JwtUtil;
import com.rewrite.selfexamsystem.utils.redis.RedisCache;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import com.rewrite.selfexamsystem.utils.response.ResultCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Levi
 * @version 3.1 (created by Spring Boot)
 * @description : 用于用户各种操作（除登录外）的类
 * @since 2022/7/21 11:44
 */
@Service
public class UserServiceImpl implements UserService {
    //    引入Mapper操作数据库
    @Autowired
    private UserInformationMapper userInformationMapper;
    @Autowired
    private LoginDataMapper loginDataMapper;

    //    引入redis操作
    @Autowired
    private RedisCache redisCache;

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
     * @description: 将前端传回来需要修改的用户数据替换掉数据库中指定的数据 TODO 邮箱验证
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
}
