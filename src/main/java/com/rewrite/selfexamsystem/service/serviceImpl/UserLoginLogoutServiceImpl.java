package com.rewrite.selfexamsystem.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.rewrite.selfexamsystem.domain.LoginData;
import com.rewrite.selfexamsystem.domain.UserInformation;
import com.rewrite.selfexamsystem.mapper.UserInformationMapper;
import com.rewrite.selfexamsystem.service.UserLoginLogoutService;
import com.rewrite.selfexamsystem.utils.JwtUtil;
import com.rewrite.selfexamsystem.utils.redis.RedisCache;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import com.rewrite.selfexamsystem.utils.response.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author: Levi
 * @description: 用于用户登录、注销登录的Service
 * @since: 2022/7/20 10:31
 * @version: 3.1（Created By Spring Boot）
 */
@Service
public class UserLoginLogoutServiceImpl implements UserLoginLogoutService {
    //    将账号密码提交到Security进行托管
    @Autowired
    private AuthenticationManager authenticationManager;

    //    查找用户详细信息
    @Autowired
    private UserInformationMapper userInformationMapper;

    //    利用redis存储登录之后的数据
    @Autowired
    private RedisCache redisCache;

    /**
     * @param loginData: 登录用户信息
     * @return 返回响应体
     * @description : 用户登录：通过Spring Security进行用户验证登录
     * @author Levi
     * @since 2022/8/6 22:18
     */
    @Override
    public ResponseResult login(LoginData loginData) {
//        调用authenticationManager中的方法authenticate进行用户认证
//        把登录的账号密码全部封装到其中
//        UsernamePasswordAuthenticationToken实现了Authentication，因此我们创建这个对象即可
//        principal是认证主体，credentials是密码
        Object username = loginData.getUsername();
        Object password = loginData.getPassword();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
//        根据返回值是不是null判断是否认证成功，如果为null则不成功
//        如果认证不成功给出提示
        if (Objects.isNull(authentication)) {
            throw new RuntimeException("登陆失败");
        }
//        获取登录之后的用户信息
        loginData = new LoginData();
        loginData = (LoginData) authentication.getPrincipal();
//      获取用户详细信息user_information放入到redis中
        UserInformation userInformation = userInformationMapper.selectByUsername(loginData.getUsername());
        redisCache.setCacheObject("user_information:" + userInformation.getUsername(), userInformation);
//        将用户user_data的username放入到redis中
        redisCache.setCacheObject("user_data:" + loginData.getUsername(), loginData);
//        利用userid生成jwt放回响应体中
        String userid = loginData.getUsername();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", userid);
        jsonObject.put("role", "user");//添加角色信息
        String token = JwtUtil.createJwt(String.valueOf(jsonObject));
//        返回响应
        jsonObject = new JSONObject();
        jsonObject.put("token", token);
        jsonObject.put("des", "登录成功");
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

    /**
     * @param :
     * @return 返回用户注销登录是否成功等信息
     * @description : 用户注销登录：用户退出登录并删除redis中的用户信息
     * @author Levi
     * @since 2022/8/5 15:43
     */
    @Override
    public ResponseResult logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginData loginData = (LoginData) authentication.getPrincipal();
//        删除redis中对应username的user_data和user_information的数据
        redisCache.deleteObject("user_data:" + loginData.getUsername());
        redisCache.deleteObject("user_information:" + loginData.getUsername());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("des", "注销登录成功");
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }


}
