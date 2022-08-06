package com.rewrite.selfexamsystem.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.rewrite.selfexamsystem.domain.Admin;
import com.rewrite.selfexamsystem.service.AdminService;
import com.rewrite.selfexamsystem.utils.JwtUtil;
import com.rewrite.selfexamsystem.utils.redis.RedisCache;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import com.rewrite.selfexamsystem.utils.response.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Levi
 * @version 3.0 (created by Spring Boot)
 * @description : 管理员Service层的实现类
 * @since 2022/5/7 11:43
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private RedisCache redisCache;

    /**
     * @param token: 鉴权信息以及获取username
     * @return 返回是否通过token登录成功等信息
     * @description : 通过token登录并返回相关信息
     * @author Levi
     * @since 2022/8/5 10:54
     */
    @Override
    public ResponseResult adminTokenLogin(String token) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(JwtUtil.parseJwt(token).getSubject());
        //      鉴权
        String role = (String) jsonObject.get("role");
        if (!"admin".equals(role)) {
            jsonObject = new JSONObject();
            jsonObject.put("des", "权限不足，无法登录");
            return new ResponseResult(ResultCode.TOKEN_EXPIRATION, jsonObject);
        }
        String username = (String) jsonObject.get("username");
//        从redis中获取相关信息
        Admin admin = redisCache.getCacheObject("admin:" + username);
//        将获取到的信息存回到json中放入响应体中
        jsonObject = new JSONObject();
        jsonObject.put("des", "Token登录成功");
        jsonObject.put("username", username);
        jsonObject.put("permisson", admin.getPermisson());
        jsonObject.put("token", token);
        return new ResponseResult(ResultCode.SUCCESS, jsonObject);
    }
}
