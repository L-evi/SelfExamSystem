package com.rewrite.selfexamsystem.AOP.Logger;

import com.alibaba.fastjson.JSONObject;
import com.rewrite.selfexamsystem.Annotation.DataLogAnnotation;
import com.rewrite.selfexamsystem.domain.DataLog;
import com.rewrite.selfexamsystem.mapper.DataLogMapper;
import com.rewrite.selfexamsystem.utils.IPAddrUtil;
import com.rewrite.selfexamsystem.utils.JwtUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Levi
 * @version 3.1（Created By Spring Boot）
 * @description :  用于日志记录的类
 * @since 2022/8/5 23:26
 */
@Aspect
@Component
public class Loggr {
    @Autowired
    private DataLogMapper dataLogMapper;

    @Pointcut(value = "@annotation(com.rewrite.selfexamsystem.Annotation.DataLogAnnotation)")
    public void UserPointcut() {
    }

    @Around("UserPointcut() && @annotation(dataLogAnnotation)")
    public Object loggerAround(ProceedingJoinPoint proceedingJoinPoint, DataLogAnnotation dataLogAnnotation) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        DataLog dataLog = new DataLog();
//        日志主体
//        获取时间
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dataLog.setTime(simpleDateFormat.format(date));
//        获取IP地址
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        dataLog.setIp(IPAddrUtil.getIPAddress(httpServletRequest));
//        获取操作人类型以及username
        String token = servletRequestAttributes.getRequest().getHeader("token");
//        如果有token就从token里面获取，如果没有就从参数中获取
        if (token == null || token.isEmpty() || "".equals(token)) {
            if ("POST".equals(httpServletRequest.getMethod())) {
                Object[] args = proceedingJoinPoint.getArgs();
                Object object = args[0];
//            忽略其中的HTTP信息
                if (object instanceof HttpServletRequest || object instanceof HttpServletResponse) {
                    return null;
                }
                JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(object));
                String username = (String) jsonObject.get("username");
                String role = (String) jsonObject.get("role");
                if (username != null && !username.isEmpty() && !"".equals(username)) {
                    dataLog.setUsername(username);
                }
                if (role != null && !role.isEmpty() && !"".equals(role)) {
                    dataLog.setType(role);
                } else {
                    dataLog.setType(dataLogAnnotation.peopleType());
                }
            }
        } else {
            JSONObject jsonObject = JSONObject.parseObject(JwtUtil.parseJwt(token).getSubject());
            String username = (String) jsonObject.get("username");
            String role = (String) jsonObject.get("role");
            dataLog.setType(role);
            dataLog.setUsername(username);
        }
//        获取时间：从注解中获取事件
        dataLog.setThing(dataLogAnnotation.thing());
//        操作是否成功：从结果中获取
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(result));
        String status = String.valueOf(jsonObject.get("status"));
        if (status.equals("1")) {
            dataLog.setStatus("成功");
            dataLog.setRes("操作成功");
        } else {
            JSONObject data = (JSONObject) jsonObject.get("data");
            String res = (String) data.get("des");
            dataLog.setRes(res);
        }
//        写入到数据库中
        dataLogMapper.addDataLog(dataLog);
        return result;
    }
}
