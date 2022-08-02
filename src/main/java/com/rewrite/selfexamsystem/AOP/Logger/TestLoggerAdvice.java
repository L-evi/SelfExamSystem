package com.rewrite.selfexamsystem.AOP.Logger;

import com.rewrite.selfexamsystem.Annotation.TestAnnotation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * @author: Levi
 * @since: 2022/7/12 15:17
 * @version: 3.1 (Created by Spring Boot)
 */

@Aspect
@Component
class TestLoggerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(TestLoggerAdvice.class);

    //    定义切点
    //    修饰符（可以不写但是不能用*） + 返回类型 + 那些包下的类 + 那些方法 + 方法参数
    //    *：代表不限；..：两个点代表参数不限
    @Pointcut(value = "@annotation(com.rewrite.selfexamsystem.Annotation.TestAnnotation)")
    public static void myPointcut() {
    }

    //    定义Advice通知：ProceedingJoinPoint只能用于环绕通知，因为ProceedingJoinPoint才有proceed方法
//    环绕通知：Around
    @Around("myPointcut()&&@annotation(logger))")
    public Object applicationLogger(ProceedingJoinPoint pjp, TestAnnotation logger) throws Throwable {
        Object res = pjp.proceed();
        System.out.println("测试Annotation的AOP功能demo");
        System.out.println("content : "+ logger.content());
        System.out.println("value : " + logger.value());
        System.out.println(res);
        return res;
    }

    /**
     * @param : null
     * @return : null
     * @description : 定义用户User的所有切点
     * @author : Levi
     * @since : 2022/7/12 19:33
     */
/*    @Pointcut(value = "execution(public * com.rewrite.selfexamsystem.controller.*.*.*(..))")
    public void UserRegisterPointcut() {
    }*/

/*    @Around(value = "UserRegisterPointcut()")
    public static Object UserRegisterLogger(ProceedingJoinPoint pjp) throws Throwable {
        Object res = pjp.proceed();

//        获取用户访问的IP
        ServletRequestAttributes sa = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest HttpRequest = sa.getRequest();
        logger.info("ip : " + getIPAddress(HttpRequest));
//        获取当前时间
        Date day = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("time : " + sdf.format(day));
//        获取用户账号（从参数中）
//        根据POST才会获取username
        if ("POST".equals(HttpRequest.getMethod())) {
            Object[] args = pjp.getArgs();
            Object object = args[0];
//            忽略掉其中的HTTP信息
            if (object instanceof HttpServletRequest || object instanceof HttpServletResponse) {
                return null;
            }
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(object));
            String username = "";
            if (jsonObject.get("username") != null) {
                username = (String) jsonObject.get("username");
            } else {
                username = (String) jsonObject.get("ticket");
            }
            logger.info("username : " + username);
        }
//        获取事件类型（通过调用的方法不同来识别）
        String methodName = pjp.getSignature().getName();
        logger.info("method : " + methodName);
//        返回值
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(res));
        logger.info((String) jsonObject.get("msg"));
//        用户类型一般是固定的
        logger.info("测试");
        return res;
    }*/

    //* 代表该方法为所有类型和所有返回值类型,括号里的..表示该方法的所有类型参数
    /*@AfterReturning(returning = "rev", pointcut = "execution(public * com.rewrite.selfexamsystem.controller.*.*.*(..))")
    public static void after(Object rev) {
        logger.debug("后置通知获取目标对象返回结果--" + rev);
        logger.info(rev.toString());
        logger.debug("后置通知进入");
    }*/
}
