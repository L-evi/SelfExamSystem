package com.rewrite.selfexamsystem.handler;

import com.alibaba.fastjson.JSON;
import com.rewrite.selfexamsystem.utils.WebUtil;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import com.rewrite.selfexamsystem.utils.response.ResultCode;
import com.rewrite.selfexamsystem.utils.response.StatusCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: Levi
 * @description: 用于登录超时时，security的自定义失败处理器
 * @since: 2022/7/21 2:28
 * @version: 3.1（Created By Spring Boot）
 */
@Component
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseResult result = new ResponseResult(ResultCode.LOGIN_TIMEOUT, null);
        String json = JSON.toJSONString(result);
        WebUtil.renderString(response, json);
    }
}
