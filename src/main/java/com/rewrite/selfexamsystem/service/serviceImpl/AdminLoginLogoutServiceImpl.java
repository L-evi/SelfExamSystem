package com.rewrite.selfexamsystem.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.rewrite.selfexamsystem.domain.Admin;
import com.rewrite.selfexamsystem.domain.LoginData;
import com.rewrite.selfexamsystem.mapper.AdminMapper;
import com.rewrite.selfexamsystem.service.AdminLoginLogoutService;
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
 * @description: 管理员用于登录退出的Service操作类
 * @since: 2022/7/23 13:48
 * @version: 3.1（Created By Spring Boot）
 */
@Service
public class AdminLoginLogoutServiceImpl implements AdminLoginLogoutService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private AdminMapper adminMapper;

    /**
     * @param loginData: 登录用户信息
     * @return 返回响应体
     * @description : 管理员登录：通过Spring Security查询数据库进行管理员验证登录
     * @author Levi
     * @since 2022/8/6 22:12
     */
    @Override
    public ResponseResult login(LoginData loginData) {
        Object Username = loginData.getUsername();
        Object Password = loginData.getPassword();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(Username, Password);
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

//        如果对象为空则说明账户号密码错误
        if (Objects.isNull(authentication)) {
            throw new RuntimeException("admin账号或密码错误");
        }
//        生成jwt
        loginData = new LoginData();
        loginData = (LoginData) authentication.getPrincipal();
        String username = loginData.getUsername();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("role", "admin");
        String jwt = JwtUtil.createJwt(String.valueOf(jsonObject));
//        查询admin中的详细信息
        Admin admin = adminMapper.selectByAdminUsername(username);
//        将用户名存入redis
        redisCache.setCacheObject("admin:" + username, admin);
//        将token响应给前端
        jsonObject = new JSONObject();
        jsonObject.put("des", "管理员登录成功");
        jsonObject.put("token", jwt);
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }

    /**
     * @param :
     * @return 返回管理员注销登录是否成功等信息
     * @description : 管理员注销登录：管理员注销登录，并删除redis中的数据
     * @author Levi
     * @since 2022/8/5 11:18
     */
    @Override
    public ResponseResult logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = (Admin) authentication.getPrincipal();
//        删除redis中对应username的user_data和user_information的数据
        redisCache.deleteObject("admin:" + admin.getUsername());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("des", "注销登录成功");
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }
}
