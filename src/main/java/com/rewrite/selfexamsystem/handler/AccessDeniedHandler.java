package com.rewrite.selfexamsystem.handler;

import com.alibaba.fastjson.JSON;
import com.rewrite.selfexamsystem.utils.WebUtil;
import com.rewrite.selfexamsystem.utils.response.ResponseResult;
import com.rewrite.selfexamsystem.utils.response.ResultCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: Levi
 * @description: 当未登录、权限不足时的security自定义失败处理
 * @since: 2022/7/21 2:18
 * @version: 3.1（Created By Spring Boot）
 */
@Component
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

    /**
     * @param request
     * @param response
     * @param accessDeniedException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseResult result = new ResponseResult(ResultCode.TOKEN_EXPIRATION, null);
        String json = JSON.toJSONString(result);
        WebUtil.renderString(response, json);
    }
}
