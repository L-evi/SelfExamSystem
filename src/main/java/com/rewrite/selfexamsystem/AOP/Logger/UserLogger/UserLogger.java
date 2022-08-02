package com.rewrite.selfexamsystem.AOP.Logger.UserLogger;

import com.alibaba.fastjson.JSONObject;
import com.rewrite.selfexamsystem.Annotation.DataLogAnnotation;
import com.rewrite.selfexamsystem.mapper.DataLogMapper;
import com.rewrite.selfexamsystem.domain.DataLog;
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

import static com.rewrite.selfexamsystem.utils.IPAddrUtil.getIPAddress;

/**
 * @description : 用于User界面Controller层面上的日志类 TODO 日志注解需要重写某一些参数
 * @author : Levi
 * @since : 2022/7/14 16:02
 * @version : 3.1 (created by Spring Boot)
 */
@Aspect
@Component
public class UserLogger {
    //    日志Mapper
    @Autowired
    private DataLogMapper dataLogMapper;

    @Pointcut(value = "@annotation(com.rewrite.selfexamsystem.Annotation.DataLogAnnotation)")
    public void UserPointcut() {
    }

    /**
     * @param proceedingJoinPoint: Around独有的控制变量
     * @param logAnnotation: 自定义注解DataLogAnnotation获取字段的变量
     * @return : 返回Around的执行结果的Object
     * @description : 此函数用于日志类中的切面及其逻辑的实现，可以获取到操作时间、IP、操作人、操作人类型、操作时间、操作状态并且放入到数据库中
     * @author : Levi
     * @since : 2022/7/15 15:45
     */
    @Around("UserPointcut() && @annotation(logAnnotation)")
    public Object UserLoggerAround(ProceedingJoinPoint proceedingJoinPoint, DataLogAnnotation logAnnotation) throws Throwable {
        Object res = proceedingJoinPoint.proceed();
        DataLog dataLog = new DataLog();
        //    日志主体
//            1、获取时间
//        System.out.println("获取时间");
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dataLog.setTime(simpleDateFormat.format(date));
//            2、获取IP地址
//        System.out.println("获取IP地址");
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        dataLog.setIp(getIPAddress(httpServletRequest));
//            3、获取操作人员username
        String username = "UserInformation";
        if ("POST".equals(httpServletRequest.getMethod())) {
            Object[] args = proceedingJoinPoint.getArgs();
            Object object = args[0];
//            忽略掉其中的HTTP信息
            if (object instanceof HttpServletRequest || object instanceof HttpServletResponse) {
                return null;
            }
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(object));
            username = (String) jsonObject.get("username");
        }
        dataLog.setUsername(username);
//            4、获取事件：从注解中获取事件
//        System.out.println("获取事件");
        dataLog.setThing(logAnnotation.thing());
//            5、获取操作人员类型：从注解中获取操作人员类型
        dataLog.setType(logAnnotation.peopleType());
//            6、操作是否成功
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(res));
//        String status = (String) jsonObject.get("status");
        String thing = logAnnotation.thing();
//        if (status.equals("1")) {
//            dataLog.setStatus("成功");
//        } else {
//            dataLog.setStatus("失败");
//        }
//           最后：将获取到的信息写入数据库
        System.out.println("写入日志：" + dataLog.toString());
//        dataLogMapper.addDataLog(dataLog);
        return res;
    }
}
