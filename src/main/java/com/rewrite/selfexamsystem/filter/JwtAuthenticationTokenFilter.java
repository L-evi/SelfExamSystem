package com.rewrite.selfexamsystem.filter;

import com.alibaba.fastjson.JSONObject;
import com.rewrite.selfexamsystem.domain.Admin;
import com.rewrite.selfexamsystem.domain.LoginData;
import com.rewrite.selfexamsystem.mapper.LoginDataMapper;
import com.rewrite.selfexamsystem.utils.JwtUtil;
import com.rewrite.selfexamsystem.utils.redis.RedisCache;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author: Levi
 * @description: Jwt的过滤器，在登录页面之前
 * @since: 2022/7/20 17:32
 * @version: 3.1（Created By Spring Boot）
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private LoginDataMapper loginDataMapper;

    @Autowired
    private RedisCache redisCache;


    /**
     * @param request:     互联网请求
     * @param response:    互联网响应
     * @param filterChain: 过滤器
     * @return
     * @throws ServletException 网络异常
     * @throws IOException      IO异常
     * @description : JWT的验证过滤器
     * @author Levi
     * @since 2022/8/6 21:59
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        获取token
        String token = request.getHeader("token");
//        如果没有token则说明是登录页面
        if (!Strings.hasText(token)) {
//            放行
            filterChain.doFilter(request, response);
            return;
        }
//        解析token
        String username;
        String role;
        try {
            Claims claims = JwtUtil.parseJwt(token);
            JSONObject jsonObject = JSONObject.parseObject(claims.getSubject());
            username = jsonObject.get("username").toString();
            role = jsonObject.get("role").toString();
        } catch (Exception e) {
            throw new RuntimeException("token非法");
        }
//        根据不同status采取不同的验证策略
        if ("user".equals(role)) {
            //        从数据库中取出指定用户信息
//        如果用的是redis，这里还要看redis中取出的用户数据是不是空，如果是空则说明用户未登录
            LoginData loginData = redisCache.getCacheObject("user_data:" + username);
            if (Objects.isNull(loginData)) {
                throw new RuntimeException("user用户未登录");
            }
            //        存入到SecurityContextHolder：第一个用户主体，第二个用户密码，可以null，第三个权限信息，稍后补充
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginData, null, null);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else if ("admin".equals(role)) {
            Admin admin = redisCache.getCacheObject("admin:" + username);
            if (Objects.isNull(admin)) {
                throw new RuntimeException("admin用户未登录");
            }
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(admin, null, null);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        //        放行
        filterChain.doFilter(request, response);
    }
}
